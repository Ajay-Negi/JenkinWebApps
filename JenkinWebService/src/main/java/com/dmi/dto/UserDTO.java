package com.dmi.dto;

import java.io.Serializable;

/**
 * @author Ajay Negi
 */

public class UserDTO implements Serializable
{
	private static final long serialVersionUID = -8593244040042347412L;

	private String firstName;
	private String lastName;
	private Long userId;
	private String userName;
	private String address;
	private Long oemId;
	private String oemLogoURl;
	private String oemBackgroundURl;
	private String oemName;
	private String roleCd;
	private String password;
	private String confirmPwd;
	private String primaryEmail;
	private String alternateEmail;
	private String countryCode;
	private String mobileNumber;
	private String oemClassificationAlias;
	private UomDTO uomDTO;
	private String locationServicesStatus;

	public String getConfirmPwd()
	{
		return confirmPwd;
	}

	public void setConfirmPwd(String confirmPwd)
	{
		this.confirmPwd = confirmPwd;
	}

	public Long getUserId()
	{
		return userId;
	}

	public UserDTO setUserId(Long userId)
	{
		this.userId = userId;
		return this;
	}

	public String getUserName()
	{
		return userName;
	}

	public UserDTO setUserName(String userName)
	{
		this.userName = userName;
		return this;
	}

	public String getRoleCd()
	{
		return roleCd;
	}

	public UserDTO setRoleCd(String roleCd)
	{
		this.roleCd = roleCd;
		return this;
	}

	public String getPassword()
	{
		return password;
	}

	public UserDTO setPassword(String password)
	{
		this.password = password;
		return this;
	}

	public Long getOemId() {
		return oemId;
	}

	public UserDTO setOemId(Long oemId) {
		this.oemId = oemId;
		return this;
	}
	
	public String getPrimaryEmail()
	{
		return primaryEmail;
	}

	public void setPrimaryEmail(String primaryEmail)
	{
		this.primaryEmail = primaryEmail;
	}

	public String getAlternateEmail()
	{
		return alternateEmail;
	}

	public void setAlternateEmail(String alternateEmail)
	{
		this.alternateEmail = alternateEmail;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getOemClassificationAlias()
	{
		return oemClassificationAlias;
	}

	public UserDTO setOemClassificationAlias(String oemClassificationAlias)
	{
		this.oemClassificationAlias = oemClassificationAlias;
		return this;
	}

	public String getOemLogoURl()
	{
		return oemLogoURl;
	}

	public UserDTO setOemLogoURl(String oemLogoURl)
	{
		this.oemLogoURl = oemLogoURl;
		return this;
	}

	public String getOemBackgroundURl() {
		return oemBackgroundURl;
	}

	public UserDTO setOemBackgroundURl(String oemBackgroundURl) {
		this.oemBackgroundURl = oemBackgroundURl;
		return this;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getOemName() {
		return oemName;
	}

	public UserDTO setOemName(String oemName) {
		this.oemName = oemName;
		return this;
	}

	
	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public UomDTO getUomDTO()
	{
		return uomDTO;
	}

	public void setUomDTO(UomDTO uomDTO)
	{
		this.uomDTO = uomDTO;
	}

	public String getLocationServicesStatus() {
		return locationServicesStatus;
	}

	public UserDTO setLocationServicesStatus(String locationServicesStatus) {
		this.locationServicesStatus = locationServicesStatus;
		return this;
	}

	@Override
	public String toString()
	{
		return "UserDTO [firstName=" + firstName + ", lastName=" + lastName + ", userId=" + userId
				+ ", userName=" + userName + ", address=" + address + ", oemId=" + oemId
				+ ", oemLogoURl=" + oemLogoURl + ", OemName=" + oemName + ", roleCd=" + roleCd
				+ ", password=" + password + ", confirmPwd=" + confirmPwd + ", primaryEmail="
				+ primaryEmail + ", alternateEmail=" + alternateEmail + ", countryCode="
				+ countryCode + ", mobileNumber=" + mobileNumber + ", oemClassificationAlias="
				+ oemClassificationAlias + ", uomDTO=" + uomDTO + "]";
	}

	
	
	


}
