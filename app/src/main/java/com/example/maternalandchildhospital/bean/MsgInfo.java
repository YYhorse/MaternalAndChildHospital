package com.example.maternalandchildhospital.bean;

/**
 * @author hxc
 *         <p>
 *         消息
 */
public class MsgInfo {
	// 消息ID
	private String msgId = "";
	// 消息类型 系统 or 院方
	private String type = "";
	// 消息内容
	private String msgText = "";
	// 发送时间
	private String time = "";
	// 消息是否已读
	private String readFlag = "";
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMsgText() {
		return msgText;
	}
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getReadFlag() {
		return readFlag;
	}
	public void setReadFlag(String readFlag) {
		this.readFlag = readFlag;
	}

	
}
