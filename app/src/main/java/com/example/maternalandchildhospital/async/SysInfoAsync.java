package com.example.maternalandchildhospital.async;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.maternalandchildhospital.bean.SystemInfo;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.net.HttpTask;
import com.example.maternalandchildhospital.net.NetReturnListener;
import com.example.maternalandchildhospital.net.TaskExecutor;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;

/**
 * 
 * @author hxc
 * @Description 系统信息
 * @date 2015年11月14日
 */
public class SysInfoAsync extends AsyncTask<Integer, Integer, String> {
	public String api_intf = "/user/systemUpdate";
	private String url = GlobalInfo.base_url + api_intf;
	private String content = "";
	private String currentTimestamp = "";
	private boolean tag = false;
	private String str = null;
	private Context context;
	private UpdateUi updateUi;

	private boolean isShowDialog;
	private long newTime;

	public SysInfoAsync(boolean isShowDialog, String systemVersion, Context context, UpdateUi uu) {
		updateUi = uu;
		GlobalInfo.HttpThread = true;
		this.context = context;
		this.isShowDialog = isShowDialog;
		str = null;
		newTime = System.currentTimeMillis();
		try {
			JSONObject loginObject = new JSONObject();
			loginObject.put("appPlatformType", "android");
			loginObject.put("systemVersion", systemVersion);

			content = Utils.SendNetJson(loginObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected String doInBackground(Integer... params) {

		SimpleDateFormat dateFormater = new SimpleDateFormat("mmssSSS");
		currentTimestamp = dateFormater.format(new Date());
		Log.e("doInBackground", System.currentTimeMillis() - newTime + "");
		new TaskExecutor(context).execute(new HttpTask(null, url, content, currentTimestamp, new NetReturnListener() {

			@Override
			public void netReturn(String msg) {
				// TODO Auto-generated method stub
				str = msg;
			}
		}, 0));

		int num = 0;
		while (str == null && num < 60 && GlobalInfo.HttpThread) {
			try {
				Thread.sleep(500);
				num++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		Log.e("doInBackground", System.currentTimeMillis() - newTime + "---");
		return str;
	}

	protected void onPostExecute(String str) {
		// Utils.Log("LoginAsync");
		if (isShowDialog) {
			Utils.closePromptDialog();
		}
		if (!GlobalInfo.HttpThread) {
			return;
		}
		// 网络连接异常
		if ("1".equals(str) || str == null || "".equals(str)) {
			Utils.Toast(context, "网络异常，点击重新加载");
			updateUi.updateUI(null);
			return;

		}
		try {
			String json = Utils.ReturnNetJsonForSys(str);
			JSONObject json1 = new JSONObject(json);
			String respCode = json1.optString("respCode");
			String respDesc = json1.optString("respDesc");
			String msgExt = json1.optString("msgExt");
			if ("0000".equals(respCode)) {
				String da = json1.optString("data");
				Utils.Log("SysInfoAsync da = " + da);
				if (!Utils.strNullMeans(da)) {
					JSONObject data = new JSONObject(da);
					String newSystemVersion = data.optString("newSystemVersion");
					int forceUpgrade = data.optInt("forceUpgrade");
					String downloadUrl = data.optString("downloadUrl");
					String description = data.optString("description");

					SystemInfo sysInfo = new SystemInfo();
					sysInfo.setNewSystemVersion(newSystemVersion);
					sysInfo.setForceUpgrade(forceUpgrade);
					sysInfo.setDescription(description);
					sysInfo.setDownloadUrl(downloadUrl);

					GlobalInfo.sysInfo = sysInfo;

					updateUi.updateUI(sysInfo);
					return;
				} else {
					Utils.ShowPromptDialog(context, 1, "提示", "数据错误！", "确定");
				}

			} else {
				if ("1003".equals(respCode)) {
					updateUi.updateUI(null);
				} else {
					if ("9999".equals(respCode)) {
						respDesc = "获取系统信息失败，请稍后再试";
					}

					Utils.ShowPromptDialog(context, 1, "提示", respCode + "|" + respDesc, "确定");
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateUi.updateUI(null);

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (isShowDialog) {
			Utils.ShowPromptDialog(context, 0, "联网中...", "", "取消");
		}
	}

}
