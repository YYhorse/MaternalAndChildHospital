package com.example.maternalandchildhospital.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.activity.HomeActivity;
import com.example.maternalandchildhospital.activity.MsgListActivity;
import com.example.maternalandchildhospital.async.HomePageAsync;
import com.example.maternalandchildhospital.bean.HomePageInfo;
import com.example.maternalandchildhospital.fragment.home.FragmentNotebook;
import com.example.maternalandchildhospital.fragment.home.FragmentRemind;
import com.example.maternalandchildhospital.interfaces.MenuListener;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.EnterDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         首页
 */
public class FragmentHome extends Fragment implements OnClickListener {

	private View viewMain;

	private LinearLayout llReturn;
	private TextView tvRemind;
	private TextView tvNotebook;

	private RelativeLayout rlMsg;
	private TextView tvMsgNum;

	private MenuListener menu;

	private String userId = "";
	private String userSessionId = "";

	private FragmentNotebook fragmentNotebook;
	private FragmentRemind fragmentRemind;
	private FragmentManager fm;
	private FragmentTransaction ft;

	private LinearLayout llAppointment;
	private LinearLayout llChatting;
	private LinearLayout llReport;

	private RelativeLayout rlSearch;
	private EditText etSearch;
	private ImageView ivDelSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewMain = inflater.inflate(R.layout.layout_fragment_home, null);
		menu = (MenuListener) getActivity();

		userId = Utils.readLocalInfo(getActivity(), GlobalInfo.SETTING, GlobalInfo.USERID);
		userSessionId = Utils.readLocalInfo(getActivity(), GlobalInfo.SETTING, GlobalInfo.USERSESSIONID);

		fm = getActivity().getSupportFragmentManager();
		initView();

		return viewMain;
	}

	private void initView() {
		llReturn = (LinearLayout) viewMain.findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);

		rlSearch = (RelativeLayout) viewMain.findViewById(R.id.rl_search_frag_home);
		rlSearch.setOnClickListener(this);
		// 搜索功能暂时未开发
		ivDelSearch = (ImageView) viewMain.findViewById(R.id.iv_del_search_frag_home);
		etSearch = (EditText) viewMain.findViewById(R.id.et_search_frag_home);
		etSearch.setEnabled(false);
		etSearch.setOnClickListener(this);
		// etSearch.addTextChangedListener(Utils.getTextWatcher(etSearch,
		// ivDelSearch));

		tvRemind = (TextView) viewMain.findViewById(R.id.tv_remind_frag_home);
		tvRemind.setOnClickListener(this);
		tvNotebook = (TextView) viewMain.findViewById(R.id.tv_notebook_frag_home);
		tvNotebook.setOnClickListener(this);

		rlMsg = (RelativeLayout) viewMain.findViewById(R.id.title_rl_email);
		rlMsg.setOnClickListener(this);
		tvMsgNum = (TextView) viewMain.findViewById(R.id.tv_msg_num);

		llAppointment = (LinearLayout) viewMain.findViewById(R.id.ll_appointment_frag_home);
		llAppointment.setOnClickListener(this);
		llChatting = (LinearLayout) viewMain.findViewById(R.id.ll_chatting_frag_home);
		llChatting.setOnClickListener(this);
		llReport = (LinearLayout) viewMain.findViewById(R.id.ll_report_frag_home);
		llReport.setOnClickListener(this);

		fragmentNotebook = new FragmentNotebook();
		fragmentRemind = new FragmentRemind();

		if (GlobalInfo.userInfo != null && !Utils.strNullMeans(GlobalInfo.userInfo.getYunId())) {

			new HomePageAsync(userId, userSessionId, GlobalInfo.userInfo.getYunId(), getActivity(), new UpdateUi() {

				@Override
				public void updateUI(Object ob) {

					HomePageInfo homePageInfo = (HomePageInfo) ob;
					if (homePageInfo != null) {
						updateUnReadNum();
						initTab();
					}

				}
			}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}

	}

	protected void updateUnReadNum() {

		if (Utils.strNullMeans(GlobalInfo.homePageInfo.getMsgCount()) || "0".equals(GlobalInfo.homePageInfo.getMsgCount())) {
			tvMsgNum.setVisibility(View.GONE);
		} else {
			tvMsgNum.setVisibility(View.VISIBLE);
			tvMsgNum.setText(GlobalInfo.homePageInfo.getMsgCount());
		}

	}

	private void initTab() {

		changeUI(0);

	}

	private void changeUI(int i) {

		if (GlobalInfo.homePageInfo != null) {
			ft = fm.beginTransaction();
			if (i == 0) {
				tvNotebook.setTextColor(getResources().getColor(R.color.color_txt_dark_grey));
				tvRemind.setTextColor(getResources().getColor(R.color.color_white));
				ft.replace(R.id.fl_frag_home, fragmentRemind);
			} else if (i == 1) {
				tvRemind.setTextColor(getResources().getColor(R.color.color_txt_dark_grey));
				tvNotebook.setTextColor(getResources().getColor(R.color.color_white));
				ft.replace(R.id.fl_frag_home, fragmentNotebook);
			}
			ft.commit();

		} else { // 没数据 只变换tag
			if (i == 0) {
				tvNotebook.setTextColor(getResources().getColor(R.color.color_txt_dark_grey));
				tvRemind.setTextColor(getResources().getColor(R.color.color_white));
			} else if (i == 1) {
				tvRemind.setTextColor(getResources().getColor(R.color.color_txt_dark_grey));
				tvNotebook.setTextColor(getResources().getColor(R.color.color_white));
			}
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FragmentHome");

		//查看消息后，更新未读消息数量
		if (GlobalInfo.homePageInfo != null) {
			updateUnReadNum();
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("FragmentHome");
	}

	@Override
	public void onClick(View v) {
		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == llReturn) {
			menu.getMenuListener(0);
		} else if (((HomeActivity) getActivity()).isMenuOpen()) {// 侧滑栏打开的时候，禁止主界面点击
			return;
		} else if (v == tvRemind) {
			changeUI(0);
		} else if (v == tvNotebook) {
			changeUI(1);
		} else if (v == llAppointment) {
            ShowAppointmentMethod(v);
			//Utils.ShowEnterDialog(getActivity(), "敬请期待", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
		} else if (v == llChatting) {
			ShowOnlineChatMethod(v);
//			Utils.ShowEnterDialog(getActivity(), "敬请期待", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
		} else if (v == llReport) {
			ShowWiseServicesMethod(v);
//			Utils.ShowEnterDialog(getActivity(), "敬请期待", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
		} else if (v == rlMsg) {
			Intent intent = new Intent(getActivity(), MsgListActivity.class);
			startActivity(intent);
		} else if (v == rlSearch || v == etSearch) {
			Utils.ShowEnterDialog(getActivity(), "敬请期待", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
		}
	}
	/*
	    预约就诊 弹出框
	 */
	private void ShowAppointmentMethod(View view){
		PopupMenu popupMenu = new PopupMenu(getActivity(),view);
		MenuInflater inflater = popupMenu.getMenuInflater();
		//添加单击事件
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				switch (menuItem.getItemId()){
					case R.id.item_appointment:
						Utils.ShowEnterDialog(getActivity(), "预约就诊", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
						break;
					case R.id.item_42day:
						Utils.ShowEnterDialog(getActivity(), "产后42天", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
						break;
					case R.id.item_children:
						Utils.ShowEnterDialog(getActivity(), "0-6岁儿童体检", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
						break;
					case R.id.item_admission:
						Utils.ShowEnterDialog(getActivity(), "入托体检预约", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
						break;
				}
				return false;
			}
		});
		inflater.inflate(R.menu.appointment,popupMenu.getMenu());
		popupMenu.show();
	}

	/*
	   	在线问诊
	 */
	private void ShowOnlineChatMethod(View view){
		PopupMenu popupMenu = new PopupMenu(getActivity(),view);
		MenuInflater inflater = popupMenu.getMenuInflater();
		//添加单击事件
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				switch (menuItem.getItemId()){
					case R.id.item_onlinechat:
						Utils.ShowEnterDialog(getActivity(), "在线问诊", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
						break;
				}
				return false;
			}
		});
		inflater.inflate(R.menu.onlinechat,popupMenu.getMenu());
		popupMenu.show();
	}

	/*
   		智慧服务
 	*/
	private void ShowWiseServicesMethod(View view){
		PopupMenu popupMenu = new PopupMenu(getActivity(),view);
		MenuInflater inflater = popupMenu.getMenuInflater();
		//添加单击事件
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				switch (menuItem.getItemId()){
					case R.id.item_wiseservice1:
						Utils.ShowEnterDialog(getActivity(), "智慧服务1", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
						break;
					case R.id.item_wiseservice2:
						Utils.ShowEnterDialog(getActivity(), "智慧服务2", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
						break;
				}
				return false;
			}
		});
		inflater.inflate(R.menu.wiseservice,popupMenu.getMenu());
		popupMenu.show();
	}
}
