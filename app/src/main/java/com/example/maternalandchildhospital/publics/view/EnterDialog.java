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
 * @author 万凯
 * @Description
 * @date 2015年11月6日
 */
public class EnterDialog extends Dialog implements android.view.View.OnClickListener {

	public static final int MODE_DOUBLE_BUTTON = 1; // 两个按钮

	public static final int MODE_SINGLE_BUTTON = 2; // 一个按钮

	public static final int MODE_NOTITLE_DOUBlE = 3; // 没有标题，两个按钮

	public static final int MODE_NOTITLE_SINGLE = 4; // 没有标题，1个按钮

	private int type = MODE_DOUBLE_BUTTON;

	private String title = "提示"; // 标题

	private String leftButton = "取消";

	private String rightButton = "确定";

	private String leftMsg = ""; //

	private String rightMsg = "";

	private TextView mTextView_title; // 标题

	private TextView mTextView_leftMsg; // 左边信息

	private TextView mTextView_rightMsg; // 右边信息

	private TextView mTextView_leftBtn; // 左边按钮

	private TextView mTextView_rightBtn; // 右边按钮

	private View mView_line; // 线

	private Context mContext;

	private UpdateUi mUpdateUi;

	private int color = 0; // 内容颜色

	public EnterDialog(Context context, String leftMsg, String rightMsg, String leftButton, String rightButton, String title, int type, UpdateUi updateUi) {
		this(context, leftMsg, rightMsg, leftButton, rightButton, title, type, 0, updateUi);
	}

	public EnterDialog(Context context, String leftMsg, String rightMsg, String leftButton, String rightButton, String title, int type, int color, UpdateUi updateUi) {
		super(context, R.style.MyDialog);
		this.leftMsg = leftMsg;
		this.rightMsg = rightMsg;
		this.leftButton = leftButton;
		this.rightButton = rightButton;
		this.title = title;
		mContext = context;
		mUpdateUi = updateUi;
		this.type = type;
		this.color = color;
	}

	public EnterDialog(Context context) {
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
		if (type == MODE_DOUBLE_BUTTON) {
			this.setContentView(R.layout.layout_dialog_enter_cancel);
			initView();
		} else if (type == MODE_SINGLE_BUTTON) {
			this.setContentView(R.layout.layout_dialog_enter_cancel);
			initView();
			mTextView_rightBtn.setVisibility(View.GONE);
			mView_line.setVisibility(View.GONE);
		} else if (type == MODE_NOTITLE_DOUBlE) {
			this.setContentView(R.layout.layout_dialog_enter_cancel2);
			initView();

		} else if (type == MODE_NOTITLE_SINGLE) {
			this.setContentView(R.layout.layout_dialog_enter_cancel2);
			initView();
			mTextView_rightBtn.setVisibility(View.GONE);
			mView_line.setVisibility(View.GONE);
		}

	}

	private void initView() {
		mTextView_leftBtn = (TextView) findViewById(R.id.enterCancel_dialog_tv_left);
		mTextView_rightBtn = (TextView) findViewById(R.id.enterCancel_dialog_tv_right);
		mTextView_leftMsg = (TextView) findViewById(R.id.enterCancel_dialog_tv_infoLeft);
		mTextView_rightMsg = (TextView) findViewById(R.id.enterCancel_dialog_tv_infoRight);
		mView_line = findViewById(R.id.enterCancel_dialog_view_line);
		if (type == MODE_DOUBLE_BUTTON || type == MODE_SINGLE_BUTTON) {
			mTextView_title = (TextView) findViewById(R.id.enterCancel_dialog_tv_title);
			mTextView_title.setText(title);
		}
		setListener();

		mTextView_leftBtn.setText(leftButton);
		mTextView_rightBtn.setText(rightButton);
		mTextView_leftMsg.setText(leftMsg);
		mTextView_rightMsg.setText(rightMsg);
		
		
		if(Utils.strNullMeans(leftMsg)){
			mTextView_leftMsg.setVisibility(View.GONE);
		}
		if(Utils.strNullMeans(rightMsg)){
			mTextView_rightMsg.setVisibility(View.GONE);
		}
		
		if (color != 0) {
			mTextView_leftMsg.setTextColor(mContext.getResources().getColor(color));
		}
	}

	private void setListener() {
		mTextView_leftBtn.setOnClickListener(this);
		mTextView_rightBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (!Utils.isFastDoubleClick()) {
			return;
		}

		if (v.getId() == R.id.enterCancel_dialog_tv_left) {// 左边

			if (mUpdateUi != null) {
				mUpdateUi.updateUI(1);
			}

		} else if (v.getId() == R.id.enterCancel_dialog_tv_right) { // 右边
			if (mUpdateUi != null) {
				mUpdateUi.updateUI(2);
			}
		}

		Utils.CloseEnterDialog();

	}

}
