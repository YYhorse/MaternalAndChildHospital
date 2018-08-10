package com.example.maternalandchildhospital.async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.net.HttpTask;
import com.example.maternalandchildhospital.net.NetReturnListener;
import com.example.maternalandchildhospital.net.TaskExecutor;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hxc
 *         <p/>
 *         发送验证码
 */
public class SendVerifyCodeAsync extends AsyncTask<Integer, Integer, String> {
    public String api_intf = "/user/sendVerifyCode";
    //	private String url = "http://fuyouapi.ichees.com/apiNew/public/index.php/api/SendcodeInfo/sendCode";
    private String url = GlobalInfo.base_url + api_intf;
    private String content = "";
    private String currentTimestamp = "";
    private boolean tag = false;
    private String str = null;
    private Context context;
    private UpdateUi updateUi;

    private long newTime;

    public SendVerifyCodeAsync(String phoneNumber, String codeType, Context context, UpdateUi uu) {
        updateUi = uu;
        GlobalInfo.HttpThread = true;
        this.context = context;
        str = null;
        newTime = System.currentTimeMillis();
        try {
            JSONObject loginObject = new JSONObject();
            loginObject.put("phoneNumber", phoneNumber);
            loginObject.put("codeType", codeType);

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
                // {"json":"{\"respDesc\":\"操作成功\",\"data\":\"{\\\"msg\\\":\\\"发送验证码成功\\\",\\\"result\\\":\\\"Successed\\\",\\\"code\\\":\\\"082505\\\"}\",\"msgExt\":\"\",\"respCode\":\"0000\"}
                String da = json1.optString("data");
                Utils.Log("SendVerifyCodeAsync da = " + da);
                if (!Utils.strNullMeans(da)) {
                    JSONObject data = new JSONObject(da);
                    String result = data.optString("result");
                    String code = data.optString("code");
                    updateUi.updateUI(code);
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
