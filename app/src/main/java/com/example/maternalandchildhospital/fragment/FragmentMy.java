package com.example.maternalandchildhospital.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.activity.HomeActivity;
import com.example.maternalandchildhospital.async.GetMyInfoAsync;
import com.example.maternalandchildhospital.bean.MyInfo;
import com.example.maternalandchildhospital.interfaces.MenuListener;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         个人信息
 */
public class FragmentMy extends Fragment implements OnClickListener {

	private View viewMain;
	private LinearLayout llReturn;
	private ImageView ivBack;

	private TextView tvTitle;

	private MenuListener menu;

	private TextView userName;
	private TextView dueDate;
	private TextView idCard;
	private TextView community;
	private LinearLayout isRealName;
	private LinearLayout eDCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewMain = inflater.inflate(R.layout.layout_fragment_my, null);
		menu = (MenuListener) getActivity();
		initView();
		return viewMain;
	}

	private void initView() {
		llReturn = (LinearLayout) viewMain.findViewById(R.id.title_ll_back);
		llReturn.setOnClickListener(this);
		ivBack = (ImageView) viewMain.findViewById(R.id.title_iv_back);
		ivBack.setImageResource(R.drawable.btn_menu2);

		tvTitle = (TextView) viewMain.findViewById(R.id.title_tv_name);
		tvTitle.setText("我的");

		userName = (TextView) viewMain.findViewById(R.id.frag_my_tv_username);
		dueDate = (TextView) viewMain.findViewById(R.id.frag_my_tv_due_date);
		idCard = (TextView) viewMain.findViewById(R.id.frag_my_tv_idcard);
		community = (TextView) viewMain.findViewById(R.id.frag_my_tv_community);
		isRealName = (LinearLayout) viewMain.findViewById(R.id.frag_my_ll_isRealName);
		isRealName.setOnClickListener(this);
		eDCode = (LinearLayout) viewMain.findViewById(R.id.frag_my_ll_eDCode);
		eDCode.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		new GetMyInfoAsync(true, GlobalInfo.userInfo.getUserId(), GlobalInfo.userInfo.getUserSessionId(), GlobalInfo.userInfo.getYunId(), getActivity(), new UpdateUi() {

			@Override
			public void updateUI(Object ob) {
				if (ob != null) {
					MyInfo myInfo = (MyInfo) ob;
					userName.setText("" + myInfo.getUserName());
					dueDate.setText("" + myInfo.getDueDate());
					idCard.setText("" + Utils.getXingstring(myInfo.getIdCard(), 6, 4));
					community.setText("" + myInfo.getCommunity());
				}
			}
		}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FragmentMy");

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("FragmentMy");
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
		} else if (v == isRealName) {
			Utils.Toast(getActivity(), "敬请期待!");
		} else if (v == eDCode) {
			Utils.Toast(getActivity(), "敬请期待!");
		}
	}

}
