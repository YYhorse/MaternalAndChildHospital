package com.example.maternalandchildhospital.publics.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.wheel.ArrayWheelAdapter;
import com.example.maternalandchildhospital.publics.view.wheel.WheelView;

public class ChooseStrDialog extends Dialog implements OnClickListener {
	private Context context;

	private WheelView wv;
	private List<String> strData = new ArrayList<String>();

	private TextView tvTitle;
	private ImageView ivCancel;
	private ImageView ivComplete;

	private UpdateUi updateUi;

	private String title = "";
	private String strContent = "";

	public ChooseStrDialog(Context context, String title, List<String> strData, UpdateUi uu) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.updateUi = uu;
		this.strData = strData;
		this.title = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_dialog_choose_str);
		wv = (WheelView) findViewById(R.id.wv_str);
		wv.setViewAdapter(new ArrayWheelAdapter(context, strData));
		wv.setCurrentItem(0, false);
		wv.setVisibleItems(3);
		wv.setWheelBackground(context.getResources().getColor(R.color.transparent));
		wv.setCyclic(true);

		tvTitle = (TextView) findViewById(R.id.tv_title_str_dialog);
		tvTitle.setText(title);
		ivCancel = (ImageView) findViewById(R.id.iv_close_str_dialog);
		ivCancel.setOnClickListener(this);
		ivComplete = (ImageView) findViewById(R.id.iv_ok_str_dialog);
		ivComplete.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dismiss();
		}
		return false;
	}

	@Override
	public void onClick(View v) {

		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == ivCancel) {
			updateUi.updateUI(null);

		} else if (v == ivComplete) {

			ArrayWheelAdapter adapter = (ArrayWheelAdapter) wv.getViewAdapter();
			strContent = String.valueOf(adapter.getItemText(wv.getCurrentItem()));
			updateUi.updateUI(strContent);
		}

	}

}
