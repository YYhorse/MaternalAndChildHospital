package com.example.maternalandchildhospital.publics.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.publics.util.Utils;

public class LoadDialog extends Dialog {
	private Context context;
	private ImageView ivProgress;

	public LoadDialog(Context context) {
		super(context, R.style.MyDialog);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_dialog_load);
		ivProgress = (ImageView) findViewById(R.id.iv_progress_load);
		// 加载动画
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
		// 使用ImageView显示动画
		ivProgress.startAnimation(hyperspaceJumpAnimation);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Utils.CloseLoadingDialog();
		}
		return false;
	}

}
