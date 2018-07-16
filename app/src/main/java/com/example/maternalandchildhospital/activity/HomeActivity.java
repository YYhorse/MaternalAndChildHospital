package com.example.maternalandchildhospital.activity;

import android.content.Intent;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.async.LogoutAsync;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.fragment.FragmentHome;
import com.example.maternalandchildhospital.fragment.FragmentKnowledge;
import com.example.maternalandchildhospital.fragment.FragmentMy;
import com.example.maternalandchildhospital.fragment.FragmentSetting;
import com.example.maternalandchildhospital.interfaces.MenuListener;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.EnterDialog;
import com.example.maternalandchildhospital.publics.view.SlidingMenu;

public class HomeActivity extends FragmentActivity implements MenuListener, OnClickListener {

	/** 侧边栏 */
	private SlidingMenu mMenu;
	private LinearLayout llHeadMemu;
	private ImageView ivHeadMenu;
	private TextView tvNameMenu;
	private ImageView ivAuthenticationMenu;
	private ImageView ivBarcodeMenu;
	private TextView tvRegisterDateMenu;
	private LinearLayout llHomeMenu;
	private LinearLayout llKnowledgeMenu;
	private LinearLayout llMyConcernMenu;
	private LinearLayout llChildHealthMenu;
	private LinearLayout llGuideMenu;
	private LinearLayout llSettingMenu;
	private LinearLayout llExitMenu;

	/** fragment */
	private FragmentManager fm;
	private FragmentTransaction ft;
	private FragmentHome fragHome;
	private FragmentKnowledge fragKnowledge;
	private FragmentSetting fragSetting;
	private FragmentMy fragMy;

	private String userId = "";
	private String userSessionId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		userId = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.USERID);
		userSessionId = Utils.readLocalInfo(this, GlobalInfo.SETTING, GlobalInfo.USERSESSIONID);

		fm = getSupportFragmentManager();
		initView();
		showTipsDialog();
	}

	private void showTipsDialog() {
		String isFirst = Utils.readLocalInfo(this, "Home_hint", "isFirst");
		if (!"1".equals(isFirst)) {
			View layout = LayoutInflater.from(this).inflate(R.layout.first_tips_dialog, null);
			final Dialog dialog = Utils.show(this, layout, Gravity.CENTER, true);
			layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Utils.saveLocalInfo(HomeActivity.this, "Home_hint", "isFirst", "1");
				}
			});
		}

	}

	private void initView() {
		/** 侧边栏 */
		mMenu = (SlidingMenu) findViewById(R.id.id_menu);
		llHeadMemu = (LinearLayout) findViewById(R.id.ll_head_memu);
		llHeadMemu.setOnClickListener(this);
		ivHeadMenu = (ImageView) findViewById(R.id.iv_head_menu);
		tvNameMenu = (TextView) findViewById(R.id.tv_name_menu);
		ivAuthenticationMenu = (ImageView) findViewById(R.id.iv_authentication_menu);
		ivAuthenticationMenu.setOnClickListener(this);
		ivBarcodeMenu = (ImageView) findViewById(R.id.iv_barcode_menu);
		ivBarcodeMenu.setOnClickListener(this);
		tvRegisterDateMenu = (TextView) findViewById(R.id.tv_register_date_menu);
		llHomeMenu = (LinearLayout) findViewById(R.id.ll_home_menu);
		llHomeMenu.setOnClickListener(this);
		llKnowledgeMenu = (LinearLayout) findViewById(R.id.ll_knowledge_menu);
		llKnowledgeMenu.setOnClickListener(this);
		llMyConcernMenu = (LinearLayout) findViewById(R.id.ll_my_concern_menu);
		llMyConcernMenu.setOnClickListener(this);
		llChildHealthMenu = (LinearLayout) findViewById(R.id.ll_child_health_menu);
		llChildHealthMenu.setOnClickListener(this);
		llGuideMenu = (LinearLayout) findViewById(R.id.ll_guide_menu);
		llGuideMenu.setOnClickListener(this);
		llSettingMenu = (LinearLayout) findViewById(R.id.ll_setting_menu);
		llSettingMenu.setOnClickListener(this);
		llExitMenu = (LinearLayout) findViewById(R.id.ll_exit_menu);
		llExitMenu.setOnClickListener(this);

		fragHome = new FragmentHome();
		fragKnowledge = new FragmentKnowledge();
		fragSetting = new FragmentSetting();
		fragMy = new FragmentMy();

		ft = fm.beginTransaction();
		ft.replace(R.id.fl_home, fragHome);
		ft.commit();

		if (GlobalInfo.userInfo != null) {
			tvNameMenu.setText(GlobalInfo.userInfo.getUserName());
			tvRegisterDateMenu.setText("注册时间：" + GlobalInfo.userInfo.getRegisterDate());
			Utils.setIMHeadImage(GlobalInfo.userInfo.getImageUrl(), ivHeadMenu);
		}

	}

	@Override
	public void getMenuListener(int type) {

		mMenu.toggle();

	}

	@Override
	public void onClick(View v) {
		if (!Utils.isFastDoubleClick()) {
			return;
		}

		if (mMenu.isOpen) {// 选择菜单

			if (v == llHeadMemu) {// 我的
				ft = fm.beginTransaction();
				ft.replace(R.id.fl_home, fragMy);
				ft.commit();
			} else if (v == ivAuthenticationMenu) {//
				Utils.ShowEnterDialog(this, "敬请期待", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
				return;

			} else if (v == ivBarcodeMenu) {// 条形码
				Utils.ShowEnterDialog(this, "敬请期待", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
				return;

			} else if (v == llHomeMenu) {// 主页
				ft = fm.beginTransaction();
				ft.replace(R.id.fl_home, fragHome);
				ft.commit();
			} else if (v == llKnowledgeMenu) {// 知识
				ft = fm.beginTransaction();
				ft.replace(R.id.fl_home, fragKnowledge);
				ft.commit();
			} else if (v == llMyConcernMenu) {// 我的关注
				Utils.ShowEnterDialog(this, "敬请期待", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
				return;
			} else if (v == llChildHealthMenu) {// 儿童
				Utils.ShowEnterDialog(this, "敬请期待", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
				return;
			} else if (v == llGuideMenu) {// 指南
				Utils.ShowEnterDialog(this, "敬请期待", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
				return;
			} else if (v == llSettingMenu) {// 设置
				ft = fm.beginTransaction();
				ft.replace(R.id.fl_home, fragSetting);
				ft.commit();
			} else if (v == llExitMenu) {// 退出

				Utils.ShowEnterDialog(this, "确定要退出吗？", "", "确定", "取消", "提示", EnterDialog.MODE_DOUBLE_BUTTON, new UpdateUi() {

					@Override
					public void updateUI(Object ob) {
						int temp = (Integer) ob;
						if (temp == 1) {
							Utils.saveLocalInfo(HomeActivity.this, GlobalInfo.SETTING, GlobalInfo.AUTOLOGIN, "false");
							GlobalInfo.init();
							Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
							startActivity(intent);
							CacheActivityManager.finishSingleActivity(HomeActivity.this);
						}

					}

				});

				return;
			}

			mMenu.toggle();

		} else {

		}

	}

	protected void logout() {
		Utils.ShowEnterDialog(this, "确定要退出吗？", "", "确定", "取消", "提示", EnterDialog.MODE_DOUBLE_BUTTON, new UpdateUi() {

			@Override
			public void updateUI(Object ob) {
				int temp = (Integer) ob;
				if (temp == 1) {
					if (Utils.isConnNet(HomeActivity.this)) {
						new LogoutAsync(false, userId, userSessionId, HomeActivity.this, new UpdateUi() {

							@Override
							public void updateUI(Object ob) {
								GlobalInfo.init();
								CacheActivityManager.finishActivity();
								System.exit(0);
							}

						}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					} else {
						CacheActivityManager.finishActivity();
						GlobalInfo.init();
						System.exit(0);
					}
				}

			}

		});

	}

	/**
	 * 侧滑栏是否打开 ，用来处理 主界面是否可点击
	 */
	public boolean isMenuOpen() {
		return mMenu.isOpen;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			if (fragKnowledge != null && fragKnowledge.isGoBack()) {
				return false;
			}
			logout();
			return false;
		}
		return false;
	}
}
