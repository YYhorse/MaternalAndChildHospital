package com.example.maternalandchildhospital.bean;

public class MyInfo {
	private String userName;
	private String dueDate;
	private String idCard;
	private String community;
	private String isRealName;
	private String eDCode;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public String getIsRealName() {
		return isRealName;
	}

	public void setIsRealName(String isRealName) {
		this.isRealName = isRealName;
	}

	public String geteDCode() {
		return eDCode;
	}

	public void seteDCode(String eDCode) {
		this.eDCode = eDCode;
	}

	@Override
	public String toString() {
		return "MyInfo [userName=" + userName + ", dueDate=" + dueDate + ", idCard=" + idCard + ", community=" + community + ", isRealName=" + isRealName + ", eDCode=" + eDCode + "]";
	}

}
