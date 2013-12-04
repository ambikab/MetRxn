package org.code.metrxn.repository.authenticate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.code.metrxn.model.authenticate.Session;
import org.code.metrxn.util.DBUtil;
import org.code.metrxn.util.Logger;

/**
 * 
 * @author ambika babuji
 *
 */

public class SessionRepository {
	
	Connection dbConnection;
	
	public SessionRepository() {
		super();
		dbConnection = DBUtil.getConnection();
	}
	
	public SessionRepository(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}
	
	public void createSession(Session newSession) {
		String session = "insert into session(sessionId, status, userName, lastModifiedDate) values(?, ?, ?, ?)";
		try {
			PreparedStatement createSession = dbConnection.prepareStatement(session);
			createSession.setString(1, newSession.getSessionId());
			createSession.setInt(2, newSession.isActive() ? 1 : 0);
			createSession.setString(3, newSession.getLoggedUser().getUserName());
			createSession.setDate(4, newSession.getLastModifiedDate());
			createSession.executeUpdate();
		} catch (SQLException e) {
			Logger.error("Error while creating a new session", SessionRepository.class);
			e.printStackTrace();
		}
	}
	
	public boolean invalidateSession(String sessionId) {
		String updateSession = "update session set status = 0 where sessionId = ?";
		int status = 0;
		try {
			PreparedStatement updateSessionStmt = dbConnection.prepareStatement(updateSession);
			updateSessionStmt.setString(1, sessionId);
			status = updateSessionStmt.executeUpdate();
		} catch (SQLException e) {
			Logger.error("Error while destroying the session!!", SessionRepository.class);
			e.printStackTrace();
		}
		return status == 0 ? false : true;
	}

}