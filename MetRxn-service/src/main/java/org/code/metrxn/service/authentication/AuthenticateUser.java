package org.code.metrxn.service.authentication;

import java.sql.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.code.metrxn.model.authenticate.Session;
import org.code.metrxn.model.authenticate.User;
import org.code.metrxn.repository.authenticate.SessionRepository;
import org.code.metrxn.repository.authenticate.UserRepository;
import org.code.metrxn.util.DateUtil;
import org.code.metrxn.util.JsonUtil;
import org.code.metrxn.util.Logger;
import org.elasticsearch.common.UUID;

/**
 * 
 * @author ambika babuji
 *
 */
@Path("/authenticate")
public class AuthenticateUser {

	UserRepository userRepository;

	SessionRepository sessionRepository;
	
	public AuthenticateUser(){
		userRepository = new UserRepository();
		sessionRepository = new SessionRepository();
	}
	
	public AuthenticateUser(UserRepository userRepository, SessionRepository sessionRepository){
		this.sessionRepository = sessionRepository;
		this.userRepository = userRepository;
	}

	@POST
	@Path("/login")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String authenticateUser(@FormParam("userName") String userName, @FormParam("userPassword") String userPassword) {
		User loggedUser = userRepository.fetchUser(userName, userPassword);
		Session currentSession = new Session("INACTIVE", false, null, null);
		Logger.info("the logged in user name is ( " + userName + ") : " + loggedUser.getUserName() , AuthenticateUser.class);
		if (loggedUser != null){
			currentSession = new Session(UUID.randomUUID().toString(), true, loggedUser, DateUtil.getCurrentDatetime());
			sessionRepository.createSession(currentSession);
		}
		return JsonUtil.toJsonForObject(currentSession).toString(); 
	}

	@POST
	@Path("/logOut")
	public void invalidateSession(@FormParam("sessionId")String sessionId) {
		sessionRepository.invalidateSession(sessionId);
	}

}