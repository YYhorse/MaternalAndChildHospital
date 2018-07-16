package com.example.maternalandchildhospital.publics.util;

import com.example.maternalandchildhospital.bean.HomePageInfo;
import com.example.maternalandchildhospital.bean.SystemInfo;
import com.example.maternalandchildhospital.bean.UserInfo;
import com.example.maternalandchildhospital.bean.ValidateButton;


/**
 * @author hxc
 * 
 */
public class GlobalInfo {
	
	public static boolean HttpThread;
	
	public static UserInfo userInfo;
	
	public static SystemInfo sysInfo;
	
	public static HomePageInfo homePageInfo;
	/**
	 * setting 字段
	 */
	public static final String SETTING = "setting";
	/**
	 * userId 字段
	 */
	public static final String USERID = "userId";
	public static final String LOGINPHONE = "loginphone";
	public static final String LOGINPSW = "loginpsw";
	public static final String AUTOLOGIN = "autoLogin";
	/**
	 * userSessionId 字段
	 */
	public static final String USERSESSIONID = "userSessionId";
	public static final String ISFIRST = "isfirst";
	public static final String VERSION_NAME = "versionName";
	public static String VERSION = "";
	/**
	 * JPUSH id
	 */
	public static final String REGID = "RegistrationId";

	// 正式服务器   现在放的是测试服务器外网
	public static final String prodUrl = "http://121.42.28.104:8080/xgfbySys/v1";
	// 测试服务器 
	public static final String testUrl = "http://10.111.3.4:8080/xgfbySys/v1";
	// 本地服务器 
	public static final String devUrl = "http://10.111.10.42:8080/xgfbySys/v1";

	public static String base_url = "http://121.42.28.104:8080/xgfbySys/v1";

	/*********************************

	/**
	 * 验证码
	 */
	public static boolean validataButtonTag = true;
    public static ValidateButton vb = new ValidateButton();
    public static int remainTime = 0;

    //消息页面的滑动状态
	public static boolean msgDataLoad = true;
	
	public static void init() {
		VERSION = "";
		HttpThread = false;
		sysInfo = null;
		userInfo = null;
		homePageInfo = null;
	}
}
