package org.code.metrxn.service.authentication;

import java.util.HashMap;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.code.metrxn.model.authenticate.User;
import org.code.metrxn.repository.authenticate.UserRepository;
import org.code.metrxn.util.JsonUtil;
import org.code.metrxn.util.email.EmailUtil;
import org.elasticsearch.common.UUID;

/**
 * 
 * @author ambika_b
 *
 */
@Path("/user")
public class UserService {

	static UserRepository userRepository = new UserRepository();
	
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public static String addNewUser(@FormParam("userName")String userName, @FormParam("userPassword")String userPassword) {
		HashMap<String, String> response = new HashMap<String, String>();
		String activationToken = UUID.randomUUID().toString();
		if (userRepository.fetchUser(userName) != null) {
			response.put("Result", "User exists already!!");
			response.put("status", "ERROR");
			return JsonUtil.toJsonForObject(response).toString();
		} else 
			userRepository.addUser(new User(userName, userPassword, activationToken, false));
		String contents = "<h2>Please click on the url to activate your account!!</h2>" + "<p><h1> " 
			+ "  <a href= 'http://localhost:8080/MetRxn-service/services/user/activate/"  + activationToken + "' >Activate </a></p></h1>";
		EmailUtil.sendEmail(userName, "Metrxn Account Activation", contents);
		response.put("Result", "Registered successfully, kindly activate your account.");
		response.put("status", "INFO");
		return JsonUtil.toJsonForObject(response).toString();
	}
	
	@GET
	@Path("/activate/{activationToken}")
	@Produces(MediaType.APPLICATION_JSON)
	public static String activateUser(@PathParam("activationToken") String activationToken ) {
		boolean status = userRepository.activateUser(activationToken);
		return status ? "Activated successfully!!" : "Error in activation. Kindly try after sometime!!";
	}
}