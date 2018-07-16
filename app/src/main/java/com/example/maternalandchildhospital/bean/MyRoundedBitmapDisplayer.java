package com.example.maternalandchildhospital.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public class MyRoundedBitmapDisplayer implements BitmapDisplayer {
	protected final int cornerRadius;
	protected final int margin;

	public MyRoundedBitmapDisplayer(int cornerRadiusPixels) {
		this(cornerRadiusPixels, 0);
	}

	public MyRoundedBitmapDisplayer(int cornerRadiusPixels, int marginPixels) {
		this.cornerRadius = cornerRadiusPixels;
		this.margin = marginPixels;
	}

	public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
		if (!(imageAware instanceof ImageViewAware)) {
			throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
		}

		imageAware.setImageDrawable(new RoundedDrawable(bitmap, this.cornerRadius, this.margin));
	}

	public static class RoundedDrawable extends Drawable {
		protected final float cornerRadius;
		protected final int margin;
		protected final RectF mRect = new RectF();
		protected final RectF mBitmapRect;
		protected final BitmapShader bitmapShader;
		protected final Paint paint;

		public RoundedDrawable(Bitmap bitmap, int cornerRadius, int margin) {
			this.cornerRadius = cornerRadius;
			this.margin = margin;

			this.bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			int widthMargin = margin;
			int heightMargin = margin;
			if (width > height) {
				widthMargin += height - width / 2;
			} else if (height > width) {
				height = width;
			}
			this.mBitmapRect = new RectF(widthMargin, heightMargin, width - widthMargin, height - heightMargin);

			this.paint = new Paint();
			this.paint.setAntiAlias(true);
			this.paint.setShader(this.bitmapShader);
		}

		protected void onBoundsChange(Rect bounds) {
			super.onBoundsChange(bounds);
			this.mRect.set(this.margin, this.margin, bounds.width() - this.margin, bounds.height() - this.margin);

			Matrix shaderMatrix = new Matrix();
			shaderMatrix.setRectToRect(this.mBitmapRect, this.mRect, Matrix.ScaleToFit.FILL);
			this.bitmapShader.setLocalMatrix(shaderMatrix);
		}

		public void draw(Canvas canvas) {
			canvas.drawRoundRect(this.mRect, this.cornerRadius, this.cornerRadius, this.paint);
		}

		public int getOpacity() {
			return -3;
		}

		public void setAlpha(int alpha) {
			this.paint.setAlpha(alpha);
		}

		public void setColorFilter(ColorFilter cf) {
			this.paint.setColorFilter(cf);
		}
	}
}
