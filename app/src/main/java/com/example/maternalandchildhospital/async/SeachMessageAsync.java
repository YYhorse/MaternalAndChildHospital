package com.example.maternalandchildhospital.async;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.example.maternalandchildhospital.bean.MsgInfo;
import com.example.maternalandchildhospital.bean.MyInfo;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.net.HttpTask;
import com.example.maternalandchildhospital.net.NetReturnListener;
import com.example.maternalandchildhospital.net.TaskExecutor;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;

/**
 * @author hxc
 *         <p>
 *         获取消息列表
 */
public class SeachMessageAsync extends AsyncTask<Integer, Integer, String> {
	public String api_intf = "/user/seachMessage";
	private String url = GlobalInfo.base_url + api_intf;
	private String content = "";
	private String currentTimestamp = "";
	private boolean tag = false;
	private String str = null;
	private Context context;
	private UpdateUi updateUi;
	private boolean isShowDialog;

	public SeachMessageAsync(boolean isShowDialog, String userId, String userSessionId, String pageNumber, String pageSize, Context context, UpdateUi uu) {
		updateUi = uu;
		GlobalInfo.HttpThread = true;
		this.context = context;
		GlobalInfo.msgDataLoad = true;
		this.isShowDialog = isShowDialog;
		str = null;
		try {
			JSONObject loginObject = new JSONObject();
			loginObject.put("userId", userId);
			loginObject.put("userSessionId", userSessionId);
			loginObject.put("pageNumber", pageNumber);
			loginObject.put("pageSize", pageSize);

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
			Utils.Toast(context, "网络异常，点击重新加载");
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
				Utils.Log("SeachMessageAsync da = " + da);
				if (!Utils.strNullMeans(da)) {
					List<MsgInfo> msgList = new ArrayList<MsgInfo>();
					JSONArray msgArray = new JSONArray(da);
					if (msgArray != null && msgArray.length() > 0) {
						if (msgArray.length() < 10) {
							GlobalInfo.msgDataLoad = false;
						}
					for (int i = 0; i < msgArray.length(); i++) {
						JSONObject jo = msgArray.optJSONObject(i);

						String msgId = jo.optString("msgId");
						String type = jo.optString("type");
						String msgText = jo.optString("msgText");
						String time = jo.optString("time");
						String readFlag = jo.optString("readFlag");

						MsgInfo msg = new MsgInfo();
						msg.setMsgId(msgId);
						msg.setType(type);
						msg.setMsgText(msgText);
						msg.setTime(time);
						msg.setReadFlag(readFlag);

						msgList.add(msg);

					}
					updateUi.updateUI(msgList);
					} else {
							GlobalInfo.msgDataLoad = false;
						}
				} else {
					updateUi.updateUI(null);
					Utils.ShowPromptDialog(context, 1, "提示", "数据错误！", "确定");
				}

			} else {
				updateUi.updateUI(null);
				Utils.ShowPromptDialog(context, 1, "提示", respCode + "|" + respDesc, "确定");
			}

		} catch (Exception e) {
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
