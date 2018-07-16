package com.example.maternalandchildhospital.fragment.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.activity.H5Activity;
import com.example.maternalandchildhospital.adapter.ArticleAdapter;
import com.example.maternalandchildhospital.bean.Article;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.EnterDialog;
import com.example.maternalandchildhospital.publics.view.SpringProgressView;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         产检提醒
 */
public class FragmentRemind extends Fragment implements OnClickListener {

	private View viewMain;
	/** 产检提醒 */

	private LinearLayout llRemind;
	private TextView tvDays;
	private SpringProgressView spv;
	private TextView tvYunWeek;
	private TextView tvRemindDay;
	private TextView tvDate;
	private TextView tvCheck;
	private TextView tvYuYue;

	private TextView tvDanWei;
	/**************************************/

	/** 产检完成 */
	private LinearLayout llComplete;
	private ListView lv;
	ArticleAdapter adapter;
	private List<Article> list = new ArrayList<Article>();

	/**************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewMain = inflater.inflate(R.layout.layout_fragment_remind, null);
		InitView();
		return viewMain;
	}

	private void InitView() {

		/** 产检提醒 */

		llRemind = (LinearLayout) viewMain.findViewById(R.id.ll_production_inspection_remind);
		tvDays = (TextView) viewMain.findViewById(R.id.tv_days_frag_remind);
		tvYunWeek = (TextView) viewMain.findViewById(R.id.tv_yunweek_frag_remind);
		tvRemindDay = (TextView) viewMain.findViewById(R.id.tv_remindday_frag_remind);
		tvDate = (TextView) viewMain.findViewById(R.id.tv_date_frag_remind);
		tvDanWei = (TextView) viewMain.findViewById(R.id.tv_reminddw_frag_remind);
		tvCheck = (TextView) viewMain.findViewById(R.id.tv_check_frag_remind);
		tvYuYue = (TextView) viewMain.findViewById(R.id.tv_yuyue_frag_remind);
		tvYuYue.setOnClickListener(this);

		spv = (SpringProgressView) viewMain.findViewById(R.id.spv_frag_remind);

		/**************************************/
		/** 产检完成 */
		llComplete = (LinearLayout) viewMain.findViewById(R.id.ll_production_inspection_complete);
		lv = (ListView) viewMain.findViewById(R.id.lv_remind);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Article article = list.get(position);
				if (article != null) {
					Intent intent = new Intent(getActivity(), H5Activity.class);
					Bundle bundle = new Bundle();
					bundle.putString("title", article.getArticleTitle());
					bundle.putString("url", "http://121.42.28.104/fubaoyuan/html5/fby-news/index.html#article?id=" + article.getArticleId());
					intent.putExtras(bundle);
					startActivity(intent);

				}

			}
		});

		/**************************************/

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FragmentRemind");

		if (GlobalInfo.homePageInfo != null) {
			tvDays.setText("距离预产期" + GlobalInfo.homePageInfo.getDoctorCount() + "天");
			tvYunWeek.setText(GlobalInfo.homePageInfo.getYunWeeks());
			if ("0".equals(GlobalInfo.homePageInfo.getDays())) {
				tvRemindDay.setText("今");
				tvDanWei.setText("日");
			} else {
				tvRemindDay.setText(GlobalInfo.homePageInfo.getDays());
				tvDanWei.setText("日后");
			}
			tvDate.setText(GlobalInfo.homePageInfo.getNextYunTest() + ",怀孕第" + GlobalInfo.homePageInfo.getYunTestSerial() + "次产检");
			tvCheck.setText(GlobalInfo.homePageInfo.getYunTestContent());

			int current = 280 - Integer.parseInt(Utils.strNullMeans(GlobalInfo.homePageInfo.getDoctorCount()) ? "0" : GlobalInfo.homePageInfo.getDoctorCount());

			spv.setInitData(280, 0, BitmapFactory.decodeResource(getResources(), R.drawable.icon_love), "今天", 0xff6cd9d4);
			spv.setCurrentCount(current);

			if (GlobalInfo.homePageInfo != null && GlobalInfo.homePageInfo.getArticleList() != null && GlobalInfo.homePageInfo.getArticleList().size() > 0) {
				list.clear();
				list.addAll(GlobalInfo.homePageInfo.getArticleList());
			}
			adapter = new ArticleAdapter(list, getActivity());
			lv.setAdapter(adapter);

			// 产检已完成
			if (GlobalInfo.homePageInfo.getpIMList() != null && GlobalInfo.homePageInfo.getpIMList().size() > 0 && !"0".equals(GlobalInfo.homePageInfo.getpIMList().get(GlobalInfo.homePageInfo.getpIMList().size() - 1).getStatus())) {
				llComplete.setVisibility(View.VISIBLE);
				llRemind.setVisibility(View.GONE);
			} else {
				llComplete.setVisibility(View.GONE);
				llRemind.setVisibility(View.VISIBLE);
			}
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("FragmentRemind");
	}

	@Override
	public void onClick(View v) {
		if (!Utils.isFastDoubleClick()) {
			return;
		} else if (v == tvYuYue) {
			Utils.ShowEnterDialog(getActivity(), "敬请期待", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
		}
	}

}
