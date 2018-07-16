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
import com.example.maternalandchildhospital.publics.util.Md5Util;
import com.example.maternalandchildhospital.publics.util.Utils;

/**
 * @author hxc
 *         <p>
 *         忘记密码和修改密码
 */
public class ModifiPwdAsync extends AsyncTask<Integer, Integer, String> {
	public String api_intf = "/user/modifiPwd";
	private String url = GlobalInfo.base_url + api_intf;
	private String content = "";
	private String currentTimestamp = "";
	private boolean tag = false;
	private String str = null;
	private Context context;
	private UpdateUi updateUi;

	private long newTime;

	/**
	 * @param userId
	 *            transType=2必填 用户ID
	 * @param userSessionId
	 *            transType=2 必填
	 * @param transType
	 *            1 忘记密码 2 修改密码
	 * @param newPassword
	 *            用户输入的新密码（MD5）
	 * @param phoneNumber
	 *            transType=1 时必填
	 * @param oldPassword
	 *            transType=2 时必填
	 */
	public ModifiPwdAsync(String userId, String userSessionId, String transType, String newPassword, String phoneNumber, String oldPassword, Context context, UpdateUi uu) {
		updateUi = uu;
		GlobalInfo.HttpThread = true;
		this.context = context;
		str = null;
		newTime = System.currentTimeMillis();
		try {
			JSONObject loginObject = new JSONObject();
			loginObject.put("userId", userId);
			loginObject.put("userSessionId", userSessionId);
			loginObject.put("transType", transType);
			loginObject.put("newPassword", Md5Util.getMD5ByString(newPassword));
			loginObject.put("phoneNumber", phoneNumber);
			loginObject.put("oldPassword", Md5Util.getMD5ByString(oldPassword));

			content = Utils.SendNetJson(loginObject);
		} catch (JSONException e) {
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
		if (!GlobalInfo.HttpThread) {
			return;
		}
		// 网络连接异常
		if ("1".equals(str) || str == null || "".equals(str)) {
			Utils.Toast(context, "网络异常，点击重新加载");
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
				if (!Utils.strNullMeans(da)) {
					JSONObject data = new JSONObject(da);
					String result = data.optString("result");
					if ("Successed".equals(result)) {
						updateUi.updateUI(result);
					} else {
						String errMsg = data.optString("errMsg");
						updateUi.updateUI(errMsg);
					}

				} else {
					Utils.ShowPromptDialog(context, 1, "提示", "数据错误！", "确定");
				}

			} else {
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
	}

}
