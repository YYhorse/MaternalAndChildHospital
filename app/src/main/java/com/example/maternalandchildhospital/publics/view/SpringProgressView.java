package com.example.maternalandchildhospital.publics.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.publics.util.Utils;

/**
 * @author Sherry
 * 
 *         自定义渐变进度条
 * 
 */
public class SpringProgressView extends View {

	/** 分段颜色 */
	private static final int[] SECTION_COLORS = { 0xfff3745f, 0xffffff00, 0xff6cd9d4 };
	/** 进度条最大值 */
	private float maxCount;
	/** 进度条当前值 */
	private float currentCount;
	private float minCount;
	/** 画笔 */
	private Paint mPaint;
	private Paint mPaintBg;
	private float mWidth, mHeight;
	private float bigRadius, smallRadius;
	private float round;
	private Bitmap mBitmap = null;
	private String content = "";
	private int txtColor = 0xff000000;
	private float progressHeight = 15;
	TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private float margin = Utils.dpToPx(getContext(), 12);// 左右预留空间
	private float circleSize = 5;//圆环大小
	private float spacing =  Utils.dpToPx(getContext(), 5);//上边文字与进度条的间隔

	public SpringProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	public SpringProgressView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public SpringProgressView(Context context) {
		this(context,null);
	}

	private void initView() {
//		mWidth = mWidth - 2 * margin;
		progressHeight = mHeight * 12 / 100;// 进度条占总高度的12%
		smallRadius = progressHeight / 2;
		bigRadius = smallRadius + circleSize;
		round = progressHeight / 2;

	}

	public void setInitData(float maxCount, float minCount, Bitmap bitmap, String content, int txtColor) {
		this.maxCount = maxCount;
		this.minCount = minCount;
		this.mBitmap = bitmap;
		this.content = content;
		this.txtColor = txtColor;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint = new Paint();
		mPaintBg = new Paint();
		mPaintBg.setAntiAlias(true);

		// mPaintBg.setStyle(Paint.Style.FILL);
		float section = currentCount / (maxCount-minCount);
		float effectWidth = mWidth-2*margin;
		mPaint.setAntiAlias(true);

		// 绘制进度条外侧边框
		mPaintBg.setColor(getResources().getColor(R.color.color_background_light));
		float progressRound = progressHeight * 2 / 3;
		RectF recfBg = new RectF();
		recfBg.set(margin, mHeight / 2 - progressHeight / 2, mWidth-margin, mHeight / 2 + progressHeight / 2);
		canvas.drawRoundRect(recfBg, progressRound, progressRound, mPaintBg);
		float bitmapWidth = mBitmap.getWidth();
		// 上边文字paint设置
		textPaint.setColor(txtColor);
		textPaint.setTextSize(Utils.dpToPx(getContext(), 14));
		float mTpDesiredWidth = Layout.getDesiredWidth("田", textPaint);
		// 绘制进度条
		LinearGradient shader = new LinearGradient(margin, 0, effectWidth * section+margin, mHeight, SECTION_COLORS, null, Shader.TileMode.CLAMP);
		mPaint.setShader(shader);
		if (mWidth * section > bigRadius * 2) {
			mPaint.setStyle(Paint.Style.FILL);
			// float pl = mWidth * section;
			RectF rectProgressBg = new RectF();
			rectProgressBg.set(margin, mHeight / 2 - progressHeight / 2, effectWidth* section+margin, mHeight / 2 + progressHeight / 2);
			canvas.drawRoundRect(rectProgressBg, progressRound, progressRound, mPaint);
			// 画滑块大圆
			canvas.drawCircle(effectWidth * section - bigRadius+margin, mHeight/2, bigRadius, mPaint);
			// 画滑块小圆
			mPaintBg.setColor(0xffffffff);
			canvas.drawCircle(effectWidth * section - bigRadius+margin,mHeight/2, smallRadius, mPaintBg);
			// 画心形图片
			canvas.drawBitmap(mBitmap, effectWidth * section - bitmapWidth / 2 - bigRadius+margin, mHeight/2 - bigRadius-mBitmap.getHeight()-spacing, mPaintBg);
			//画上边文字
			if (effectWidth * section > effectWidth / 2) {// 超过中间后
				textPaint.setTextAlign(Paint.Align.RIGHT);
				canvas.drawText(content, effectWidth * section - bitmapWidth / 2 - bigRadius+margin-spacing, mHeight/2-bigRadius-spacing-mBitmap.getHeight()/4, textPaint);
			} else {
				textPaint.setTextAlign(Paint.Align.LEFT);
				canvas.drawText(content, effectWidth * section + bitmapWidth / 2 - bigRadius+margin+spacing,mHeight/2-bigRadius-spacing-mBitmap.getHeight()/4, textPaint);
			}
		} else {
			// 画滑块大圆
			canvas.drawCircle(bigRadius+margin, mHeight/2, bigRadius, mPaint);
			// 画滑块小圆
			mPaintBg.setColor(0xffffffff);
			canvas.drawCircle(bigRadius+margin, mHeight/2, smallRadius, mPaintBg);
			// 画心形图片
			canvas.drawBitmap(mBitmap, bigRadius+margin- bitmapWidth / 2, mHeight/2 - bigRadius-mBitmap.getHeight()-spacing, mPaintBg);
			//画上边文字
			textPaint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText(content, bigRadius+margin+ bitmapWidth / 2+spacing,mHeight/2-bigRadius-spacing-mBitmap.getHeight()/4, textPaint);
		}
		textPaint.setTextSize(Utils.dpToPx(getContext(), 12));
		textPaint.setColor(getResources().getColor(R.color.color_txt_light_grey));
		textPaint.setTextAlign(Paint.Align.LEFT);
		canvas.drawText(String.valueOf((int)minCount), margin, mHeight/2+bigRadius+mTpDesiredWidth, textPaint);
		textPaint.setTextAlign(Paint.Align.RIGHT);
		canvas.drawText(String.valueOf((int)maxCount), mWidth-margin, mHeight/2+bigRadius+mTpDesiredWidth, textPaint);
	}

	private int dipToPx(int dip) {
		float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/***
	 * 设置最大的进度值
	 * 
	 * @param maxCount
	 */
	public void setMaxCount(float maxCount) {
		this.maxCount = maxCount;
	}

	/***
	 * 设置当前的进度值
	 * 
	 * @param currentCount
	 */
	public void setCurrentCount(float currentCount) {
		this.currentCount = currentCount > maxCount ? maxCount : currentCount;
		invalidate();
	}

	public float getMaxCount() {
		return maxCount;
	}

	public float getCurrentCount() {
		return currentCount;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
			mWidth = widthSpecSize;
		} else {
			mWidth = 0;
		}
		if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
			mHeight = dipToPx(15);
		} else {
			mHeight = heightSpecSize;
		}

		initView();
		setMeasuredDimension((int) mWidth, (int) mHeight);
	}

}
