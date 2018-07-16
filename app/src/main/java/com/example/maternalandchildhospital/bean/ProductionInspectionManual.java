package com.example.maternalandchildhospital.bean;

/**
 * @author hxc<p>
 *  产检手册
 */
public class ProductionInspectionManual {
	//产检手册列表(产检序号)
	private String 	serialNo = "";
	//产检手册列表(产检中文序号)
	private String 	serialNoCN = "";
	//产检手册列表(孕周数)	
	private String 	yunWeeks = "";                  
	//产检手册列表(计划的日期)
	private String 	dueDate = "";
	//产检手册列表(星期几)	
	private String 	weekName = "";
	//产检手册列表(产检项目)
	private String 	yunTestContent = "";              
	//产检手册列表(状态)
	private String 	status = "";        
	//产检手册列表(完成时间)
	private String 	finishTime = "";    
	//是否已实名认证 1: 是  
	private String 	isRealName = "";
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getSerialNoCN() {
		return serialNoCN;
	}
	public void setSerialNoCN(String serialNoCN) {
		this.serialNoCN = serialNoCN;
	}
	public String getYunWeeks() {
		return yunWeeks;
	}
	public void setYunWeeks(String yunWeeks) {
		this.yunWeeks = yunWeeks;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getWeekName() {
		return weekName;
	}
	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}
	public String getYunTestContent() {
		return yunTestContent;
	}
	public void setYunTestContent(String yunTestContent) {
		this.yunTestContent = yunTestContent;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getIsRealName() {
		return isRealName;
	}
	public void setIsRealName(String isRealName) {
		this.isRealName = isRealName;
	}
	
	
}
