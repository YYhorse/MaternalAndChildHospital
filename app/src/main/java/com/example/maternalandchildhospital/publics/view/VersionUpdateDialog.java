package com.example.maternalandchildhospital.publics.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.Utils;

/**
 * 
 * @author wk
 * @Description 版本更新dialog
 * @date 2016年4月25日
 */
public class VersionUpdateDialog extends Dialog implements android.view.View.OnClickListener {

	private Context mContext;

	private UpdateUi uu;

	private String updateStrt; // 更行内容

	private TextView tvContent;

	private View viewCancel;

	private boolean isCancel = false;

	/**
	 * 
	 * @param context
	 * @param updateStr
	 *                更新内容
	 * @param uu
	 * @param isCancel
	 *                是否显示取消按钮
	 */
	public VersionUpdateDialog(Context context, String updateStr, boolean isCancel, UpdateUi uu) {
		super(context, R.style.MyDialog);
		mContext = context;
		this.uu = uu;
		this.updateStrt = updateStr;
		this.isCancel = isCancel;

	}

	public VersionUpdateDialog(Context context) {
		super(context);
		mContext = context;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Utils.CloseEnterDialog();
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_dialog_update);
		initView();

	}

	private void initView() {
		viewCancel = findViewById(R.id.updateDialog_tv_cancel);
		if (isCancel) {
			viewCancel.setVisibility(View.VISIBLE);
			viewCancel.setOnClickListener(this);
		} else {
			viewCancel.setVisibility(View.GONE);
		}

		findViewById(R.id.updateDialog_tv_update).setOnClickListener(this);
		tvContent = (TextView) findViewById(R.id.updateDialog_tv_content);
		tvContent.setText(updateStrt + "");
	}

	@Override
	public void onClick(View v) {

		if (!Utils.isFastDoubleClick()) {
			return;
		}
		switch (v.getId()) {
		case R.id.updateDialog_tv_cancel: // 取消
			if (uu != null) {
				uu.updateUI(0);
			}
			break;
		case R.id.updateDialog_tv_update: // 更新
			if (uu != null) {
				uu.updateUI(1);
			}
			break;

		default:
			break;
		}

		Utils.CloseUpdateDialog();

	}

}
