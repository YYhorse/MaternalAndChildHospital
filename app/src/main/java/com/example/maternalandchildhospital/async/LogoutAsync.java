package com.example.maternalandchildhospital.async;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.net.HttpTask;
import com.example.maternalandchildhospital.net.NetReturnListener;
import com.example.maternalandchildhospital.net.TaskExecutor;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;

/**
 * @author hxc<p>
 *     退出登录
 */
public class LogoutAsync extends AsyncTask<Integer, Integer, String> {
	public String api_intf = "/user/loginOut";
	private String url = GlobalInfo.base_url + api_intf;
	private String content = "";
	private String currentTimestamp = "";
	private boolean tag = false;
	private String str = null;
	private Context context;
	private UpdateUi updateUi;

	private boolean isShowDialog;

	public LogoutAsync(boolean isShowDialog, String userId, String userSessionId, Context context, UpdateUi uu) {
		updateUi = uu;
		GlobalInfo.HttpThread = true;
		this.context = context;
		this.isShowDialog = isShowDialog;
		str = null;
		try {
			JSONObject loginObject = new JSONObject();
			loginObject.put("userId", userId);
			loginObject.put("userSessionId", userSessionId);

			content = Utils.SendNetJson(loginObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected String doInBackground(Integer... params) {

		SimpleDateFormat dateFormater = new SimpleDateFormat("mmssSSS");
		currentTimestamp = dateFormater.format(new Date());

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

		return str;
	}

	protected void onPostExecute(String str) {
		if (isShowDialog) {
			Utils.closePromptDialog();
		}
		if (!GlobalInfo.HttpThread) {
			return;
		}
		// 网络连接异常
		if ("1".equals(str) || str == null || "".equals(str)) {
			updateUi.updateUI("");
			return;

		}
		try {
			String json = Utils.ReturnNetJson(str);
			JSONObject json1 = new JSONObject(json);
			String respCode = json1.optString("respCode");
			String respDesc = json1.optString("respDesc");
			String msgExt = json1.optString("msgExt");
			if ("0000".equals(respCode)) {
				String da = json1.optString("data");
				Utils.Log("LogoutAsync da = " + da);
				if (!Utils.strNullMeans(da)) {
					JSONObject data = new JSONObject(da);
					String result = data.optString("result");// 请求操作是否成功Successed：成功
																// Failed：失败
					updateUi.updateUI(result);
				} else {
					updateUi.updateUI("");
				}

			} else {
				updateUi.updateUI("");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (isShowDialog) {
			Utils.ShowPromptDialog(context, 0, "联网中...", "", "取消");
		}
	}

}
