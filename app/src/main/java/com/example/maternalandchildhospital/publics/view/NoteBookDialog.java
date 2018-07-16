package com.example.maternalandchildhospital.publics.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.bean.ProductionInspectionManual;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.Utils;

public class NoteBookDialog extends Dialog implements OnClickListener {
	private Context context;

	ProductionInspectionManual pimDate;

	private TextView tvNum;
	private TextView tvDate;
	private TextView tvYunWeek;
	private TextView tvCheck;
	private ImageView ivClose;

	private UpdateUi uu;

	public NoteBookDialog(Context context) {
		super(context);
		this.context = context;
	}

	public NoteBookDialog(Context context, ProductionInspectionManual pimDate, UpdateUi uu) {
		super(context, R.style.MyDialog);
		this.uu = uu;
		this.pimDate = pimDate;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_dialog_notebook);
		tvNum = (TextView) findViewById(R.id.tv_num_dialog_notebook);
		tvDate = (TextView) findViewById(R.id.tv_date_dialog_notebook);
		tvYunWeek = (TextView) findViewById(R.id.tv_yunweek_dialog_notebook);
		tvCheck = (TextView) findViewById(R.id.tv_check_dialog_notebook);
		ivClose = (ImageView) findViewById(R.id.iv_close_dialog_notebook);
		ivClose.setOnClickListener(this);

		if (pimDate != null) {
			tvNum.setText("第" + pimDate.getSerialNoCN() + "次产检");
			tvDate.setText(pimDate.getDueDate());
			tvYunWeek.setText("第" + pimDate.getYunWeeks() + "周");
			tvCheck.setText(pimDate.getYunTestContent());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Utils.CloseNoteBookDialog();
		}
		return false;
	}

	@Override
	public void onClick(View v) {

		if (!Utils.isFastDoubleClick()) {
			return;
		}
		if (v == ivClose) {

		}
		Utils.CloseNoteBookDialog();
	}

}
