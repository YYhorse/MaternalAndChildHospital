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

import com.example.maternalandchildhospital.bean.Article;
import com.example.maternalandchildhospital.bean.HomePageInfo;
import com.example.maternalandchildhospital.bean.ProductionInspectionManual;
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
 *         返回首页需要的数据
 */
public class HomePageAsync extends AsyncTask<Integer, Integer, String> {
	public String api_intf = "/user/homePage";
	private String url = GlobalInfo.base_url + api_intf;
	private String content = "";
	private String currentTimestamp = "";
	private boolean tag = false;
	private String str = null;
	private Context context;
	private UpdateUi updateUi;

	public HomePageAsync(String userId, String userSessionId, String yunId, Context context, UpdateUi uu) {
		updateUi = uu;
		GlobalInfo.HttpThread = true;
		this.context = context;
		str = null;
		try {
			JSONObject loginObject = new JSONObject();
			loginObject.put("userId", userId);
			loginObject.put("userSessionId", userSessionId);
			loginObject.put("yunId", yunId);

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
		// Utils.closePromptDialog();
		if (!GlobalInfo.HttpThread) {
			return;
		}
		// 网络连接异常
		if ("1".equals(str) || str == null || "".equals(str)) {
			Utils.Toast(context, "网络异常，点击重新加载");
			// Utils.ShowPromptDialog(context, 1, "提示", "网络异常，请稍后再试", "确定");
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
				Utils.Log("HomePageAsync da = " + da);
				if (!Utils.strNullMeans(da)) {
					HomePageInfo homePageInfo = new HomePageInfo();

					GlobalInfo.homePageInfo = null;

					JSONObject data = new JSONObject(da);
					JSONArray yunTestArray = data.optJSONArray("yunTestList");
					List<ProductionInspectionManual> yunTestList = new ArrayList<ProductionInspectionManual>();
					for (int i = 0; i < yunTestArray.length(); i++) {
						JSONObject js = yunTestArray.getJSONObject(i);
						ProductionInspectionManual pim = new ProductionInspectionManual();
						String serialNo = js.optString("serialNo");
						String serialNoCN = js.optString("serialNoCN");
						String yunWeeks = js.optString("yunWeeks");
						String dueDate = js.optString("dueDate");
						String weekName = js.optString("weekName");
						String yunTestContent = js.optString("yunTestContent");
						String status = js.optString("status");
						String finishTime = js.optString("finishTime");

						pim.setSerialNo(serialNoCN);
						pim.setSerialNoCN(serialNoCN);
						pim.setYunWeeks(yunWeeks);
						pim.setDueDate(dueDate);
						pim.setWeekName(weekName);
						pim.setYunTestContent(yunTestContent);
						pim.setStatus(status);
						pim.setFinishTime(finishTime);

						yunTestList.add(pim);
					}
					JSONArray articleArray = data.optJSONArray("articleList");
					List<Article> articleList = new ArrayList<Article>();
					for (int i = 0; i < articleArray.length(); i++) {
						JSONObject js = articleArray.getJSONObject(i);
						Article article = new Article();
						String articleTitle = js.optString("articleTitle");
						String resume = js.optString("resume");
						String articleId = js.optString("articleId");

						article.setArticleId(articleId);
						article.setResume(resume);
						article.setArticleTitle(articleTitle);

						articleList.add(article);
					}

					String doctorCount = data.optString("doctorCount");
					String msgCount = data.optString("msgCount");
					String nextYunTest = data.optString("nextYunTest");
					String days = data.optString("days");
					String yunTestSerial = data.optString("yunTestSerial");
					String yunWeeks = data.optString("yunWeeks");
					String yunRemainder = data.optString("yunRemainder");
					String yunTestContent = data.optString("yunTestContent");

					homePageInfo.setArticleList(articleList);
					homePageInfo.setpIMList(yunTestList);
					homePageInfo.setDoctorCount(doctorCount);
					homePageInfo.setMsgCount(msgCount);
					homePageInfo.setNextYunTest(nextYunTest);
					homePageInfo.setDays(days);
					homePageInfo.setYunTestSerial(yunTestSerial);
					homePageInfo.setYunWeeks(yunWeeks);
					homePageInfo.setYunRemainder(yunRemainder);
					homePageInfo.setYunTestContent(yunTestContent);

					GlobalInfo.homePageInfo = homePageInfo;
					updateUi.updateUI(homePageInfo);

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
		// Utils.ShowPromptDialog(context, 0, "联网中...", "", "取消");
	}

}
