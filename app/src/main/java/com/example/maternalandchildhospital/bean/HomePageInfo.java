package com.example.maternalandchildhospital.bean;

import java.util.List;

/**
 * @author hxc
 *         <p>
 *         首页信息
 */
public class HomePageInfo {
	// 距离预产期天数
	private String doctorCount = "";
	// 未读消息数
	private String msgCount = "";
	// 下次产检日期
	private String nextYunTest = "";
	// 距离下次产检的天数
	private String days = "";
	// 第几次产检
	private String yunTestSerial = "";
	// 孕周数
	private String yunWeeks = "";
	// 孕周余数
	private String yunRemainder = "";
	// 检查项目
	private String yunTestContent = "";
	// 产检手册列表
	private List<ProductionInspectionManual> pIMList;
	// 产前小贴士列表
	private List<Article> articleList;

	public String getDoctorCount() {
		return doctorCount;
	}

	public void setDoctorCount(String doctorCount) {
		this.doctorCount = doctorCount;
	}

	public String getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(String msgCount) {
		this.msgCount = msgCount;
	}

	public String getNextYunTest() {
		return nextYunTest;
	}

	public void setNextYunTest(String nextYunTest) {
		this.nextYunTest = nextYunTest;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getYunTestSerial() {
		return yunTestSerial;
	}

	public void setYunTestSerial(String yunTestSerial) {
		this.yunTestSerial = yunTestSerial;
	}

	public String getYunWeeks() {
		return yunWeeks;
	}

	public void setYunWeeks(String yunWeeks) {
		this.yunWeeks = yunWeeks;
	}

	public String getYunRemainder() {
		return yunRemainder;
	}

	public void setYunRemainder(String yunRemainder) {
		this.yunRemainder = yunRemainder;
	}

	public String getYunTestContent() {
		return yunTestContent;
	}

	public void setYunTestContent(String yunTestContent) {
		this.yunTestContent = yunTestContent;
	}

	public List<ProductionInspectionManual> getpIMList() {
		return pIMList;
	}

	public void setpIMList(List<ProductionInspectionManual> pIMList) {
		this.pIMList = pIMList;
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

}
