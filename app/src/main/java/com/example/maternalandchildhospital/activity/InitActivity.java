package com.example.maternalandchildhospital.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import cn.jpush.android.api.JPushInterface;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.async.AsyncDownApp;
import com.example.maternalandchildhospital.async.LoginAsync;
import com.example.maternalandchildhospital.async.SysInfoAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.bean.SystemInfo;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.ScreenUtils;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         启动界面
 */

public class InitActivity extends Activity {

	Timer timer;
	private String isFirst = "";
	private String userId = "";
	private String userSessionId = "";

	private String oldPhoneNumber = "";
	private String oldPassword = "";
	private String autoLogin = "";
	private String regId = "";

	private String jobNumber = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init_layout);

		String urlType = getString(R.string.publish_environment);

		if (!CacheActivityManager.activityList.contains(InitActivity.this)) {
			CacheActivityManager.addActivity(InitActivity.this);
		}
		if (urlType.equals("dev")) {
			GlobalInfo.base_url = GlobalInfo.devUrl;
		} else if (urlType.equals("test")) {
			GlobalInfo.base_url = GlobalInfo.testUrl;
		} else if (urlType.equals("prod")) {
			Utils.showLog = false;
			GlobalInfo.base_url = GlobalInfo.prodUrl;
		}
		ScreenUtils.getScreenWidth(this);

		userId = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.USERID);
		userSessionId = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.USERSESSIONID);
		oldPhoneNumber = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.LOGINPHONE);
		oldPassword = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.LOGINPSW);
		autoLogin = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.AUTOLOGIN);
		regId = JPushInterface.getRegistrationID(getApplicationContext());
		jobNumber = oldPhoneNumber;

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		try {
			GlobalInfo.VERSION = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AnalyticsConfig.setAppkey("5878650b99f0c77a09002b70");
		MobclickAgent.setDebugMode(true);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(this);

		isFirst = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.ISFIRST);

		// 延时1.5秒启动main
		TimerTask task = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						synchronized (InitActivity.class) {
							// finishThis();
							new SysInfoAsync(false, GlobalInfo.VERSION, InitActivity.this, new UpdateUi() {

								@Override
								public void updateUI(Object ob) {
									final SystemInfo sysInfo = (SystemInfo) ob;
									if (sysInfo != null) {
										if (!Utils.VersionJudge(sysInfo.getNewSystemVersion(), GlobalInfo.VERSION)) {
											finishThis();
										} else {
											Utils.Log("initAcitivyt upDate");
											upDate(sysInfo);

										}
									} else {
										Intent intent = new Intent(InitActivity.this, LoginActivity.class);
										startActivity(intent);
										CacheActivityManager.finishSingleActivity(InitActivity.this);

									}

								}

							}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						}
					}
				});

			}
		};
		timer = new Timer();
		timer.schedule(task, 1500);

	}

	boolean isMainFirstStart = true;
	
	private boolean updateType = false;

	/**
	 * 更新
	 * 
	 * @param sysInfo
	 */
	private void upDate(SystemInfo sysInfo) {
		/*
		 * 不显示版本号
		 */
		String desc = sysInfo.getDescription();
		switch (sysInfo.getForceUpgrade()) {
		case 0:
			finishThis();
			break;
		case 1: // 强制更新
			updateType = true;
			new AsyncDownApp(InitActivity.this, true, desc, new UpdateUi() {

				@Override
				public void updateUI(Object ob) {
				}
			});
			break;
		case 2: // 非强制更新
			updateType = false;
			new AsyncDownApp(InitActivity.this, false, desc, new UpdateUi() {

				@Override
				public void updateUI(Object ob) {
					if (0 == (Integer) ob) {
						finishThis();
					}
				}
			});
			break;

		default:
			break;
		}
	}

	private void finishThis() {

		MobclickAgent.onEvent(InitActivity.this, "id_startapp", "启动应用");

		if (!Utils.strNullMeans(oldPhoneNumber) && !Utils.strNullMeans(oldPassword)) {
			if ("true".equals(autoLogin)) {
				new LoginAsync(false, oldPhoneNumber, oldPassword, regId, InitActivity.this, 1, new UpdateUi() {

					@Override
					public void updateUI(Object ob) {
						// TODO Auto-generated method
						// stub
						if ("111".equals(ob)) {
							Intent intent = new Intent(InitActivity.this, LoginActivity.class);
							startActivity(intent);
							return;
						}
						Utils.saveLocalInfo(InitActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPHONE, oldPhoneNumber);
						Utils.saveLocalInfo(InitActivity.this, GlobalInfo.SETTING, GlobalInfo.LOGINPSW, oldPassword);
						Utils.saveLocalInfo(InitActivity.this, GlobalInfo.SETTING, GlobalInfo.AUTOLOGIN, "true");
						Utils.Toast(InitActivity.this, "登录成功");
						if (!Utils.strNullMeans((GlobalInfo.userInfo.getYunId()))) {
							Intent intent = new Intent(InitActivity.this, HomeActivity.class);
							startActivity(intent);
						} else {
							Intent intent = new Intent(InitActivity.this, InputInfoActivity.class);
							startActivity(intent);
						}
						CacheActivityManager.finishSingleActivity(InitActivity.this);
					}
				}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				Intent intent = new Intent(InitActivity.this, LoginActivity.class);// RegisterActivity
				startActivity(intent);
				CacheActivityManager.finishSingleActivityByClass(InitActivity.class);
			}
		} else {
			Intent intent = new Intent(InitActivity.this, LoginActivity.class);// RegisterActivity
			startActivity(intent);
			CacheActivityManager.finishSingleActivityByClass(InitActivity.class);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);

		MobclickAgent.onPageStart("InitActivity");
		MobclickAgent.onResume(this);

		JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		String regId = JPushInterface.getRegistrationID(getApplicationContext());
		if (!Utils.strNullMeans(regId)) {
			Utils.saveLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.REGID, regId);
		}
		if(!updateType && !isMainFirstStart){
			
			Intent intent = new Intent(InitActivity.this, LoginActivity.class);// RegisterActivity
			startActivity(intent);
			CacheActivityManager.finishSingleActivityByClass(InitActivity.class);
		}
		
		if(isMainFirstStart){
			isMainFirstStart = false;
		}
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
		MobclickAgent.onPageEnd("InitActivity");
		MobclickAgent.onPause(this);
	}

}
