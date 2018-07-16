package com.example.maternalandchildhospital.publics.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.activity.HomeActivity;
import com.example.maternalandchildhospital.activity.LoginActivity;
import com.example.maternalandchildhospital.async.LoginAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;

public class PromptDialog extends Dialog implements OnClickListener {
	private Context context;
	private TextView tvContent;
	private ProgressBar pb;
	private Button btnEnter;
	private Button btnCancel;

	String strTitle = "";
	String strContent = "";
	String strBtn = "";
	private int type;

	private String strCode = "";
	private boolean canClose = true;

	public PromptDialog(int type, Context context, int theme, String strTitle, String strContent, String strBtn) {
		super(context, R.style.MyDialog);

		String temp[] = strContent.split("\\|");
		this.strContent = strContent;
		if (temp.length == 2) {
			strCode = temp[0];
			this.strContent = temp[1];
		}
		this.context = context;
		this.strTitle = strTitle;
		this.strBtn = strBtn;
		this.type = type;
	}

	public String getStrCode() {

		return strCode;
	}

	public int getType() {
		return type;
	}

	public boolean getCanClose() {
		return canClose;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_dialog_prompt);
		tvContent = (TextView) findViewById(R.id.tv_content_prompt);
		tvContent.setText(strContent);
		pb = (ProgressBar) findViewById(R.id.pb_loading_prompt);

		btnEnter = (Button) findViewById(R.id.btn_enter_prompt);
		btnEnter.setText(strBtn);
		btnEnter.setOnClickListener(this);
		btnEnter.setBackgroundResource(R.drawable.corner_prompt_dialog_btn_left_bg);

		btnCancel = (Button) findViewById(R.id.btn_cancel_prompt);
		btnCancel.setOnClickListener(this);
		btnCancel.setBackgroundResource(R.drawable.corner_prompt_dialog_btn_right_bg);
		canClose = true;
		switch (type) {
		case 0: // loading
			tvContent.setVisibility(View.GONE);
			btnCancel.setVisibility(View.GONE);
			btnEnter.setVisibility(View.GONE);
			btnEnter.setBackgroundResource(R.drawable.corner_prompt_dialog_btn_bg);
			break;
		case 1: // prompt
			pb.setVisibility(View.GONE);
			btnCancel.setVisibility(View.GONE);
			btnEnter.setBackgroundResource(R.drawable.corner_prompt_dialog_btn_bg);
			if ("0001".equals(strCode) || "0002".equals(strCode)) {
				btnEnter.setVisibility(View.VISIBLE);
				btnCancel.setVisibility(View.VISIBLE);
				btnEnter.setBackgroundResource(R.drawable.corner_prompt_dialog_btn_left_bg);
				btnCancel.setBackgroundResource(R.drawable.corner_prompt_dialog_btn_right_bg);
				tvContent.setText(strContent);
				btnEnter.setText("取消");
				btnCancel.setText("重新登录");
				canClose = false;
			}
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			canClose = true;
			Utils.closePromptDialog();
		}
		return false;
	}

	@Override
	public void onClick(View v) {

		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == btnEnter) {
			switch (type) {
			case 0: // loading
				break;
			case 1: // 取消
				//异地登录时候，取消返回到登录页
				if ("0001".equals(strCode) || "0002".equals(strCode)) {
				Utils.clearLocalInfo(context, GlobalInfo.SETTING, GlobalInfo.USERID);
				Utils.clearLocalInfo(context, GlobalInfo.SETTING, GlobalInfo.USERSESSIONID);
				Utils.saveLocalInfo(context, GlobalInfo.SETTING, GlobalInfo.AUTOLOGIN, "false");
				GlobalInfo.init();
				CacheActivityManager.finishActivity();
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
				}
				break;
			}
			canClose = true;
			Utils.closePromptDialog();
		} else if (v == btnCancel) {
			switch (type) {
			case 0: // loading
				break;
			case 1: // 重新登录
				if ("0001".equals(strCode) || "0002".equals(strCode)) {

					Utils.clearLocalInfo(context, GlobalInfo.SETTING, GlobalInfo.USERID);
					Utils.clearLocalInfo(context, GlobalInfo.SETTING, GlobalInfo.USERSESSIONID);

					String login_Name = Utils.readLocalInfo(context, GlobalInfo.SETTING, GlobalInfo.LOGINPHONE);
					String login_Psw = Utils.readLocalInfo(context, GlobalInfo.SETTING, GlobalInfo.LOGINPSW);
					new LoginAsync(true, login_Name, login_Psw, JPushInterface.getRegistrationID(context), context, 0, new UpdateUi() {

						@Override
						public void updateUI(Object ob) {
							// TODO Auto-generated
							// method stub

							Utils.Toast(context, "登录成功");
							Intent intent = new Intent(context, HomeActivity.class);
							context.startActivity(intent);
						}
					}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

				}
				break;
			}
			canClose = true;
			Utils.closePromptDialog();
		}
	}

}
