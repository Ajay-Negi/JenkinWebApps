package com.dmi.analytics;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Class to establish remote ssh session.
 * 
 * @author RRavula
 */
public final class SSHSession {

	/**
	 * Establishes and returns the ssh session.
	 * 
	 * @return session - the ssh session.
	 */
	protected static Session getSession(String host, String user, String password, String key) {
		Session session = null;
		try {
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			jsch.addIdentity(key);

			session = jsch.getSession(user, host, 22);
			session.setConfig(config);
			session.connect(60 * 100);
			/*if (session.isConnected()) {
				logger.info("SSH Session established!");
			}*/
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return session;
	}
}
