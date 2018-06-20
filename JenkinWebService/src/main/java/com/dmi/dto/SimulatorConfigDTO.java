package com.dmi.dto;

public class SimulatorConfigDTO
{
	private static final long serialVersionUID = 1L;
	Long domainId;
	Long subDomainId;
	String deviceName;
	Long messageFrequency;
	String frequencyUnit;
	String topicName;
	Long totalMessages;
	String jsonTemplate;

	/**
	 * @return the frequencyUnit
	 */
	public String getFrequencyUnit()
	{
		return frequencyUnit;
	}

	/**
	 * @param frequencyUnit
	 *            the frequencyUnit to set
	 */
	public void setFrequencyUnit(String frequencyUnit)
	{
		this.frequencyUnit = frequencyUnit;
	}

	/**
	 * @return the topicName
	 */
	public String getTopicName()
	{
		return topicName;
	}

	/**
	 * @param topicName
	 *            the topicName to set
	 */
	public void setTopicName(String topicName)
	{
		this.topicName = topicName;
	}

	/**
	 * @param messageFrequency
	 *            the messageFrequency to set
	 */
	public void setMessageFrequency(Long messageFrequency)
	{
		this.messageFrequency = messageFrequency;
	}

	/**
	 * @return the domainId
	 */
	public Long getDomainId()
	{
		return domainId;
	}

	/**
	 * @param domainId
	 *            the domainId to set
	 */
	public void setDomainId(Long domainId)
	{
		this.domainId = domainId;
	}

	/**
	 * @return the subDomainId
	 */
	public Long getSubDomainId()
	{
		return subDomainId;
	}

	/**
	 * @param subDomainId
	 *            the subDomainId to set
	 */
	public void setSubDomainId(Long subDomainId)
	{
		this.subDomainId = subDomainId;
	}

	/**
	 * @return the deviceName
	 */
	public String getDeviceName()
	{
		return deviceName;
	}

	/**
	 * @return the messageFrequency
	 */
	public Long getMessageFrequency()
	{
		return messageFrequency;
	}

	/**
	 * @param deviceName
	 *            the deviceName to set
	 */
	public void setDeviceName(String deviceName)
	{
		this.deviceName = deviceName;
	}



	/**
	 * @return the totalMessages
	 */
	public Long getTotalMessages()
	{
		return totalMessages;
	}

	/**
	 * @param totalMessages
	 *            the totalMessages to set
	 */
	public void setTotalMessages(Long totalMessages)
	{
		this.totalMessages = totalMessages;
	}

	/**
	 * @return the jsonTemplate
	 */
	public String getJsonTemplate()
	{
		return jsonTemplate;
	}

	/**
	 * @param jsonTemplate
	 *            the jsonTemplate to set
	 */
	public void setJsonTemplate(String jsonTemplate)
	{
		this.jsonTemplate = jsonTemplate;
	}

}
