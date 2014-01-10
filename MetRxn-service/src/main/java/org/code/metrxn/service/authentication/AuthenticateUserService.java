package org.code.metrxn.service.authentication;

import java.util.HashMap;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.code.metrxn.model.authenticate.Session;
import org.code.metrxn.model.authenticate.User;
import org.code.metrxn.repository.authenticate.SessionRepository;
import org.code.metrxn.repository.authenticate.UserRepository;
import org.code.metrxn.util.DateUtil;
import org.code.metrxn.util.JsonUtil;
import org.elasticsearch.common.UUID;

/**
 * 
 * @author ambika babuji
 *
 */
@Path("/authenticate")
public class AuthenticateUserService {

	UserRepository userRepository;

	SessionRepository sessionRepository;
	
	public AuthenticateUserService(){
		userRepository = new UserRepository();
		sessionRepository = new SessionRepository();
	}
	
	public AuthenticateUserService(UserRepository userRepository, SessionRepository sessionRepository){
		this.sessionRepository = sessionRepository;
		this.userRepository = userRepository;
	}

	@POST
	@Path("/login")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String authenticateUser(@FormParam("userName") String userName, @FormParam("userPassword") String userPassword) {
		User loggedUser = userRepository.fetchActiveUser(userName, userPassword);
		Session currentSession = new Session(null, false, null, null);
		if (loggedUser != null){
			currentSession = new Session(UUID.randomUUID().toString(), true, loggedUser, DateUtil.getCurrentDatetime());
			sessionRepository.createSession(currentSession);
		}
		return JsonUtil.toJsonForObject(currentSession).toString(); 
	}

	@POST
	@Path("/logOut")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public String invalidateSession(@FormParam("sessionId")String sessionId) {
		HashMap<String, String> response = new HashMap<String, String>();
		if (! sessionRepository.invalidateSession(sessionId)) {
			response.put("Result", "Error in loggin out. Please re- try.");
			response.put("Status", "ERROR");
		} else {
			response.put("Status", "INFO");
			response.put("Result", "Logged out successfully.");
		}
		return JsonUtil.toJsonForObject(response).toString();
		
	}
}