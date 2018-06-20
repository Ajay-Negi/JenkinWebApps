package com.dmi.analytics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.dmi.controller.CustomRuleAssetController;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

/**
 * Class to save the json rule file to the specified location and invoke the
 * spark job in Hadoop System.
 * 
 * @author RRavula
 * @author AChowdhury
 */
@Service
public class SparkJobInvoker
{

	private static final Logger LOG = Logger.getLogger(SparkJobInvoker.class);
	
	@Value("${PRIVATE_KEY}")
	private Resource privateKey;

	@Value("${CUSTOM_JARS_LOCATION}")
	private String jarFileLocation;

	@Value("${COMMAND_TO_EXECUTE}")
	private String commandToExecute;

	@Value("${JSON_RULE_FILE_NAME}")
	private String jsonRuleFileName;

	@Value("${HOST}")
	private String host;

	@Value("${USER}")
	private String user;

	@Value("${PASSWORD}")
	private String password;

	@Override
	public String toString()
	{
		return "SparkJobInvoker [privateKey=" + privateKey
				+ ", jsonRuleLocation=" + jarFileLocation
				+ ", commandToExecute=" + commandToExecute
				+ ", jsonRuleFileName=" + jsonRuleFileName + ", host=" + host
				+ ", user=" + user + ", password=" + password + "]";
	}

	/**
	 * Interface method for clients to execute spark job.
	 * 
	 * @param json
	 *            - The Json Rule.
	 * @throws IOException
	 */
	public void invokeJob(String ruleJson, String domainId, Integer subdomainId)
			throws IOException
	{
		// System.out.println(this.toString());

		/*
		 * Resource path = privateKey; File ppk = path.getFile();
		 */

		// System.out.println("ppk path:" + ppk.getPath());

		writeToFile(ruleJson, jsonRuleFileName);
		saveFileInSparkMaster();
		invokeBatchJob(domainId, subdomainId);
	}

	/*
	 * Write The Rule to a file named searchParams.json
	 */
	public void writeToFile(String jsonRule, String jsonRuleFileName)
	{
		try
		{
			Writer fileWriter = new FileWriter(jsonRuleFileName);
			fileWriter.write(jsonRule);
			fileWriter.close();
		}
		catch (IOException e)
		{
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * Saves the searchParams.json in spark node.
	 * 
	 * @return
	 */
	public int saveFileInSparkMaster()
	{

		try
		{
			

			Resource path = privateKey;
			File ppk = path.getFile();
			String absolutePPKFilePath = ppk.getPath();

			Session session = SSHSession.getSession(host, user, password,
					absolutePPKFilePath);
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel;
			channelSftp.cd(jarFileLocation);

			File f1 = new File(jsonRuleFileName);
			channelSftp.put(new FileInputStream(f1), f1.getName());
			channelSftp.exit();
			session.disconnect();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return 0;
	}

	/**
	 * Saves the jar file for custom rule creation in spark node.
	 * 
	 * @return
	 */
	public int saveFileInSparkMaster(String filePath, String jarName)
	{

		try
		{

			ClassLoader classLoader = getClass().getClassLoader();
			File ppk = new File(classLoader.getResource("oracledbcloudSERVICE.ppk").getFile());
			//Resource path = privateKey;
			//File ppk = path.getFile();
			String absolutePPKFilePath = ppk.getPath();
			System.out.println("ABSOLUTE PATH: "+ absolutePPKFilePath);
			Session session = SSHSession.getSession(host, user, password,
					absolutePPKFilePath);
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel;
			// channelSftp.mkdir(jsonRuleLocation+filePath);
			channelSftp.cd(jarFileLocation + filePath);

			File f1 = new File(jarName);
			channelSftp.put(new FileInputStream(f1), f1.getName());
			channelSftp.exit();
			session.disconnect();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return 0;
	}

	/**
	 * Saves the file at serverFilePAth.
	 * 
	 * @return
	 */
	public int saveFileInSparkMaster(String filePath, String jarName,
			String serverFilePath)
	{

		try
		{

			Resource path = privateKey;
			File ppk = path.getFile();
			String absolutePPKFilePath = ppk.getPath();

			Session session = SSHSession.getSession(host, user, password,
					absolutePPKFilePath);
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel;
			// channelSftp.mkdir(jsonRuleLocation+filePath);
			channelSftp.cd(serverFilePath);

			File f1 = new File(jarName);
			channelSftp.put(new FileInputStream(f1), f1.getName());
			channelSftp.exit();
			session.disconnect();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return 0;
	}

	/**
	 * reads the file at serverFilePAth. Returns null if file not present.
	 * 
	 * @return
	 */
	public String readFileInSparkMaster(String serverFilePath, String fileName)
	{
		String configFileContents = null;
		try
		{

			Resource path = privateKey;
			File ppk = path.getFile();
			String absolutePPKFilePath = ppk.getPath();

			Session session = SSHSession.getSession(host, user, password,
					absolutePPKFilePath);
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp channelSftp = (ChannelSftp) channel;
			channelSftp.cd(serverFilePath);

			System.out.println(serverFilePath + fileName);

			InputStream in = channelSftp.get(serverFilePath + fileName);
			configFileContents = IOUtils.toString(in, "UTF-8");
			channelSftp.exit();
			session.disconnect();
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
		return configFileContents;
	}

	/**
	 * Invokes simulator start shell script on a server.
	 */
	public void invokeShellScript(String serverFilePath,
			String shellScriptFileName)
	{
		int status = -1;

		try
		{
			Resource path = privateKey;
			File ppk = path.getFile();
			String absolutePPKFilePath = ppk.getPath();

			Session session = SSHSession.getSession(host, user, password,
					absolutePPKFilePath);

			String commandToBeExecuted = serverFilePath + shellScriptFileName;

			System.out.println("ShellScriptInvoked----> Command to execute--->"
					+ commandToBeExecuted);

			ChannelExec channel = (ChannelExec) session.openChannel("exec");

			channel.setCommand("sh " + commandToBeExecuted);

			// /

			// messages from the remote side can be read from this stream.
			InputStream in = channel.getInputStream();

			// Set the command that you want to execute
			// In our case its the remote shell script
			// channelExec.setCommand("sh " + scriptFileName);

			// Execute the command
			channel.connect();

			// Read the output from the input stream we set above
			/*
			 * BufferedReader reader = new BufferedReader( new
			 * InputStreamReader(in)); String line;
			 */

			// Read each line from the buffered reader and add it to result list
			// You can also simple print the result here
			/*
			 * while ((line = reader.readLine()) != null) { line += line; }
			 * System.out.println("Script Output-"); System.out.println(line);
			 */

			// retrieve the exit status of the remote command corresponding to
			// this channel
			status = channel.getExitStatus();

			// Safely disconnect channel and disconnect session. If not done
			// then it may cause resource leak
			channel.disconnect();
			session.disconnect();

			if (status < 0)
			{
				System.out.println(status);
				System.out.println("Done, but exit status not set! " + status);
			}
			else if (status > 0)
			{
				System.out.println(status);
				System.out.println("Done, but with error! " + status);
			}
			else
			{
				System.out.println(status);
				System.out.println("Done! " + status);
			}

		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Invokes the spark job in hadoop system.
	 */
	private void invokeBatchJob(String domainId, Integer subdomainId)
	{
		int status = -1;

		try
		{

			Resource path = privateKey;
			File ppk = path.getFile();
			String absolutePPKFilePath = ppk.getPath();

			Session session = SSHSession.getSession(host, user, password,
					absolutePPKFilePath);

			String commandToBeExecuted = commandToExecute + " " + domainId
					+ " " + subdomainId;

			System.out.println("BatchAnalytics----> Command to execute--->"
					+ commandToBeExecuted);

			ChannelExec channel = (ChannelExec) session.openChannel("exec");

			channel.setCommand("sh " + commandToBeExecuted);

			// /

			// messages from the remote side can be read from this stream.
			InputStream in = channel.getInputStream();

			// Set the command that you want to execute
			// In our case its the remote shell script
			// channelExec.setCommand("sh " + scriptFileName);

			// Execute the command
			channel.connect();

			// Read the output from the input stream we set above
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line;

			// Read each line from the buffered reader and add it to result list
			// You can also simple print the result here
			/*
			 * while ((line = reader.readLine()) != null) { //result.add(line);
			 * }
			 */

			// retrieve the exit status of the remote command corresponding to
			// this channel
			status = channel.getExitStatus();

			// Safely disconnect channel and disconnect session. If not done
			// then it may cause resource leak
			channel.disconnect();
			session.disconnect();

			if (status < 0)
			{
				System.out.println(status);
				System.out.println("Done, but exit status not set! " + status);
			}
			else if (status > 0)
			{
				System.out.println(status);
				System.out.println("Done, but with error! " + status);
			}
			else
			{
				System.out.println(status);
				System.out.println("Done! " + status);
			}

			// /
			/*
			 * channel.setInputStream(null); ((ChannelExec)
			 * channel).setErrStream(System.err);
			 * 
			 * channel.connect();
			 */

			/*
			 * try { Thread.sleep(1000); } catch (Exception ex) { }
			 */

			// if (channel.isClosed()) {
			// status = channel.getExitStatus();
			// }

			/*
			 * channel.disconnect(); session.disconnect();
			 * System.out.println("Batch Job Execution Done With Status: " +
			 * status);
			 */
		}
		catch (Exception ex)
		{
			LOG.error(ex.getMessage(), ex);
		}
	}

}
