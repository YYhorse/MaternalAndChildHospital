package com.example.maternalandchildhospital.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.activity.AboutActivity;
import com.example.maternalandchildhospital.activity.AdviceActivity;
import com.example.maternalandchildhospital.activity.HomeActivity;
import com.example.maternalandchildhospital.activity.ModifyPswActivity;
import com.example.maternalandchildhospital.async.UpdateRemindStatusAsync;
import com.example.maternalandchildhospital.interfaces.MenuListener;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         设置
 */
public class FragmentSetting extends Fragment implements OnClickListener {

	private View viewMain;

	private LinearLayout llReturn;
	private ImageView ivBack;

	private TextView tvTitle;

	private MenuListener menu;
	private LinearLayout llChangePsw;
	private ToggleButton tbRemind;
	private LinearLayout llAdvice;
	private LinearLayout llAbout;
	private TextView tvAccount;

	private boolean isFirst = false;
	private String userId;
	private String userSessionId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (viewMain == null) {
			viewMain = inflater.inflate(R.layout.layout_fragment_setting, null);
			menu = (MenuListener) getActivity();
			initView();
		}
		ViewGroup parent = (ViewGroup) viewMain.getParent();
		if (parent != null) {
			parent.removeView(viewMain);
		}
		return viewMain;
	}

	private void initView() {
		llReturn = (LinearLayout) viewMain.findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);
		ivBack = (ImageView) viewMain.findViewById(R.id.title_iv_back);
		ivBack.setImageResource(R.drawable.btn_menu2);

		tvTitle = (TextView) viewMain.findViewById(R.id.title_tv_name);
		tvTitle.setText("设置");

		tvAccount = (TextView) viewMain.findViewById(R.id.frag_setting_tv_account);
		tvAccount.setText("" + Utils.readLocalInfo(getActivity(), GlobalInfo.SETTING, GlobalInfo.LOGINPHONE));
		llChangePsw = (LinearLayout) viewMain.findViewById(R.id.frag_setting_ll_change_psw);
		llChangePsw.setOnClickListener(this);
		tbRemind = (ToggleButton) viewMain.findViewById(R.id.frag_setting_tb_msg);
		tbRemind.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isFirst) {
					final String isSendMessage = isChecked ? "1" : "0";
					new UpdateRemindStatusAsync(GlobalInfo.userInfo.getUserId(), GlobalInfo.userInfo.getUserSessionId(), isSendMessage, getActivity(), new UpdateUi() {

						@Override
						public void updateUI(Object ob) {
							if (ob != null) {
								String result = (String) ob;
								if (!Utils.strNullMeans(result) && result.equals("Successed")) {
									GlobalInfo.userInfo.setIsReceiveMsg(isSendMessage);
								} else {
									String flag = isSendMessage.equals("1") ? "0" : "1";
									GlobalInfo.userInfo.setIsReceiveMsg(flag);
									initData();
								}
							}
						}
					}).execute();
				}
			}
		});
		llAdvice = (LinearLayout) viewMain.findViewById(R.id.frag_setting_ll_advice);
		llAdvice.setOnClickListener(this);
		llAbout = (LinearLayout) viewMain.findViewById(R.id.frag_setting_ll_about);
		llAbout.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	private void initData() {
		isFirst = true;
		if (GlobalInfo.userInfo != null) {
			String isReceiveMsg = GlobalInfo.userInfo.getIsReceiveMsg();
			if ("1".equals(isReceiveMsg)) {
				tbRemind.setChecked(true);
			} else {
				tbRemind.setChecked(false);
			}
		}
		isFirst = false;
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FragmentSetting");

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("FragmentSetting");
	}

	@Override
	public void onClick(View v) {
		if (!Utils.isFastDoubleClick()) {
			return;
		}
		// if (v == llReturn) {
		// menu.getMenuListener(0);
		// }
		switch (v.getId()) {
		case R.id.title_ll_back:
			menu.getMenuListener(0);
			break;
		case R.id.frag_setting_ll_change_psw:
			if (!((HomeActivity) getActivity()).isMenuOpen()) {// 侧滑栏打开的时候，禁止主界面点击
				Intent changePswIntent = new Intent(getActivity(), ModifyPswActivity.class);
				getActivity().startActivity(changePswIntent);
			}
			break;
		case R.id.frag_setting_ll_advice:
			if (!((HomeActivity) getActivity()).isMenuOpen()) {// 侧滑栏打开的时候，禁止主界面点击
				Intent adviceIntent = new Intent(getActivity(), AdviceActivity.class);
				getActivity().startActivity(adviceIntent);
			}
			break;
		case R.id.frag_setting_ll_about:
			if (!((HomeActivity) getActivity()).isMenuOpen()) {// 侧滑栏打开的时候，禁止主界面点击
				Intent aboutIntent = new Intent(getActivity(), AboutActivity.class);
				getActivity().startActivity(aboutIntent);
			}
			break;

		default:
			break;
		}
	}

}
