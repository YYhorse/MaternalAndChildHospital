package com.example.maternalandchildhospital.fragment.home;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.adapter.NoteBookAdapter;
import com.example.maternalandchildhospital.bean.ProductionInspectionManual;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author hxc
 *         <p>
 *         产检手册
 */
public class FragmentNotebook extends Fragment implements OnClickListener {

	private View viewMain;

	private ListView lv;
	private NoteBookAdapter adapter;
	private List<ProductionInspectionManual> list = new ArrayList<ProductionInspectionManual>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		viewMain = inflater.inflate(R.layout.layout_fragment_note_book, null);

		initView();

		return viewMain;
	}

	private void initView() {

		lv = (ListView) viewMain.findViewById(R.id.lv_note_book);
		if (GlobalInfo.homePageInfo != null && GlobalInfo.homePageInfo.getpIMList() != null && GlobalInfo.homePageInfo.getpIMList().size() > 0) {
			list.clear();
			list.addAll(GlobalInfo.homePageInfo.getpIMList());
		}
		adapter = new NoteBookAdapter(list, getActivity());
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				ProductionInspectionManual pimDate = list.get(position);
				if (pimDate != null) {
					Utils.ShowNoteBookDialog(getActivity(), pimDate, new UpdateUi() {

						@Override
						public void updateUI(Object ob) {

						}
					});
				}

			}
		});

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("FragmentNotebook");

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("FragmentNotebook");
	}

	@Override
	public void onClick(View v) {
		if (!Utils.isFastDoubleClick()) {
			return;
		}
	}

}
