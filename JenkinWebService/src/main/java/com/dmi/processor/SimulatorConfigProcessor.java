/*package com.dmi.processor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.change.analytics.SparkJobInvoker;
import com.dmi.constant.Constants;
import com.dmi.exception.ProcessorException;

@Service
public class SimulatorConfigProcessor
{

	@Autowired
	SparkJobInvoker sparkMaster;

	public String startSimulator() throws ProcessorException
	{
		String processId = null;

		try
		{
			String topicName = simulatorConfigDAO.getConfig(domainId,
					subDomainId).getTopicName();
			
			// invoke shell script to execute simulator
			sparkMaster.invokeShellScript(Constants.SIMULATOR_START_FILEPATH,
					Constants.SIMULATOR_START_FILENAME);
			// read pid of simulator.jar
			processId = sparkMaster.readFileInSparkMaster(
					Constants.SIMULATOR_PID_FILEPATH,
					Constants.SIMULATOR_PID_FILENAME);
			processId = processId.replaceAll("(\n.*\n)", "");
		}
		catch (Exception e)
		{
			throw new ProcessorException(e.getMessage());
		}
		return processId;
	}

	public void stopSimulator() throws ProcessorException
	{
		try
		{
			// invoke shell script to execute simulator
			sparkMaster.invokeShellScript(Constants.SIMULATOR_STOP_FILEPATH,
					Constants.SIMULATOR_STOP_FILENAME);
		}
		catch (Exception e)
		{
			throw new ProcessorException(e.getMessage());
		}
	}

	
}
*/