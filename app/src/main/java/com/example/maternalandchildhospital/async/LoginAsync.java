package com.example.maternalandchildhospital.async;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.example.maternalandchildhospital.bean.UserInfo;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.net.HttpTask;
import com.example.maternalandchildhospital.net.NetReturnListener;
import com.example.maternalandchildhospital.net.TaskExecutor;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Md5Util;
import com.example.maternalandchildhospital.publics.util.Utils;

/**
 * @author hxc
 *         <p>
 *         登录接口
 */
public class LoginAsync extends AsyncTask<Integer, Integer, String> {
	public String api_intf = "/user/login";
	private String url = GlobalInfo.base_url + api_intf;
	private String content = "";
	private String currentTimestamp = "";
	private boolean tag = false;
	private String str = null;
	private Context context;
	private UpdateUi updateUi;

	private boolean isShowDialog;
	// 1 init 0 default
	private int activityType = 0;

	public LoginAsync(boolean isShowDialog, String phoneNumber, String password, String pushToken, Context context, int type, UpdateUi uu) {
		updateUi = uu;
		GlobalInfo.HttpThread = true;
		this.context = context;
		this.isShowDialog = isShowDialog;
		this.activityType = type;

		str = null;

		try {
			JSONObject loginObject = new JSONObject();
			loginObject.put("phoneNumber", phoneNumber);
			loginObject.put("password", Md5Util.getMD5ByString(password));
			loginObject.put("pushToken", pushToken+"|0");

			content = Utils.SendNetJson(loginObject);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
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
				Utils.Log("time = " + num);
				Thread.sleep(500);
				num++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		Utils.Log("str = " + str);
		return str;
	}

	protected void onPostExecute(String str) {
		Utils.Log("LoginAsync str = " + str);
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
			if (!isShowDialog) {
				updateUi.updateUI("111");
			}
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
				Utils.Log("LoginAsync da = " + da);
				if (!Utils.strNullMeans(da)) {
					JSONObject data = new JSONObject(da);

					String userId = data.optString("userId");
					String userSessionId = data.optString("userSessionId");
					String yunId = data.optString("yunId");
					String userName = data.optString("userName");
					String imageUrl = data.optString("imageUrl");
					String eDCode = data.optString("eDCode");
					String registerDate = data.optString("registerDate");
					String isReceiveMsg = data.optString("isReceiveMsg");
					String isRealName = data.optString("isRealName");

					UserInfo userInfo = new UserInfo();
					userInfo.setUserId(userId);
					userInfo.setUserSessionId(userSessionId);
					userInfo.setYunId(yunId);
					userInfo.setUserName(userName);
					userInfo.setImageUrl(imageUrl);
					userInfo.seteDCode(eDCode);
					userInfo.setRegisterDate(registerDate);
					userInfo.setIsReceiveMsg(isReceiveMsg);
					userInfo.setIsRealName(isRealName);

					GlobalInfo.userInfo = userInfo;
					Utils.saveLocalInfo(context, GlobalInfo.SETTING, GlobalInfo.USERID, userId);
					Utils.saveLocalInfo(context, GlobalInfo.SETTING, GlobalInfo.USERSESSIONID, userSessionId);
					updateUi.updateUI("");

				} else {
					Utils.ShowPromptDialog(context, 1, "提示", "数据错误！", "确定");
				}

			} else {
				if ("9999".equals(respCode)) {
					respDesc = "获取登录信息失败，请稍后再试";
				}
				Utils.ShowPromptDialog(context, 1, "提示", respCode + "|" + respDesc, "确定");
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
