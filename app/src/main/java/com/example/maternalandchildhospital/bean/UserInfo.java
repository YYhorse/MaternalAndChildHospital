package com.example.maternalandchildhospital.bean;

public class UserInfo {
	//用户ID
	private String 	userId = "";
	//用户验证Key
	private String 	userSessionId = "";
	//孕期ID
	private String 	yunId = "";                  
	//用户姓名
	private String 	userName = "";
	//用户头像url
	private String 	imageUrl = "";
	//用户二维码  如果没有实名认证，可能没有二维码
	private String 	eDCode = "";              
	//注册时间  Yyyy-mm-dd
	private String 	registerDate = "";        
	//是否接收消息  1: 是  0：否
	private String 	isReceiveMsg = "";    
	//是否已实名认证 1: 是  
	private String 	isRealName = "";
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserSessionId() {
		return userSessionId;
	}
	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}
	public String getYunId() {
		return yunId;
	}
	public void setYunId(String yunId) {
		this.yunId = yunId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String geteDCode() {
		return eDCode;
	}
	public void seteDCode(String eDCode) {
		this.eDCode = eDCode;
	}
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public String getIsReceiveMsg() {
		return isReceiveMsg;
	}
	public void setIsReceiveMsg(String isReceiveMsg) {
		this.isReceiveMsg = isReceiveMsg;
	}
	public String getIsRealName() {
		return isRealName;
	}
	public void setIsRealName(String isRealName) {
		this.isRealName = isRealName;
	}      
	
	
}
