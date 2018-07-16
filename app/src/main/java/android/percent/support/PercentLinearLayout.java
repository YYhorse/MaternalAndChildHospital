package android.percent.support;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by zhy on 15/6/30.
 */
public class PercentLinearLayout extends LinearLayout
{

//    private static final String TAG = "PercentLinearLayout";
    private PercentLayoutHelper mPercentLayoutHelper;

    public PercentLinearLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mPercentLayoutHelper = new PercentLayoutHelper(this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {

        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int tmpHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);

        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int tmpWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);

//        Log.d(TAG, MeasureSpec.toString(heightMeasureSpec));
        //fixed scrollview height problems
//        System.out.println("widthMode = " + widthMode);

//        System.out.println("heightMode = " + heightMode);
//        System.out.println("getParent() = " + getParent());
        if (heightMode == MeasureSpec.UNSPECIFIED && getParent() != null && (getParent() instanceof ScrollView))
        {
            int baseHeight = 0;
            Context context = getContext();
            if (context instanceof Activity)
            {
                Activity act = (Activity) context;
                int measuredHeight = act.findViewById(android.R.id.content).getMeasuredHeight();
                baseHeight = measuredHeight;
//                Log.d(TAG, "measuredHeight = " + measuredHeight);
            } else
            {
                baseHeight = getScreenHeight();
//                Log.d(TAG, "scHeight = " + baseHeight);
            }
//            System.out.println("baseHeight = " + baseHeight);
//            System.out.println("heightMode = " + heightMode);
            tmpHeightMeasureSpec = MeasureSpec.makeMeasureSpec(baseHeight, heightMode);
        }
//ggl add HerizontalScrollview
//        if (widthMode == MeasureSpec.UNSPECIFIED && getParent() != null && (getParent() instanceof HorizontalScrollView))
//        {
//            int baseWidth = 0;
//            Context context = getContext();
//            if (context instanceof Activity)
//            {
//                Activity act = (Activity) context;
//                int measuredWidth = act.findViewById(android.R.id.content).getMeasuredWidth();
//                baseWidth = measuredWidth;
//                Log.d(TAG, "measuredWidth = " + measuredWidth);
//            } else
//            {
//            	baseWidth = getScreenWidth();
//                Log.d(TAG, "scWidth = " + baseWidth);
//            }
//            System.out.println("baseWidth = " + baseWidth);
//            System.out.println("widthMode = " + widthMode);
//            tmpWidthMeasureSpec = MeasureSpec.makeMeasureSpec(baseWidth, widthMode);
//        }
//        System.out.println(tmpWidthMeasureSpec +" = = " + tmpHeightMeasureSpec);
        mPercentLayoutHelper.adjustChildren(tmpWidthMeasureSpec, tmpHeightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mPercentLayoutHelper.handleMeasuredStateTooSmall())
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private int getScreenHeight()
    {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
    //ggl add width
    private int getScreenWidth()
    {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        super.onLayout(changed, l, t, r, b);
        mPercentLayoutHelper.restoreOriginalParams();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new LayoutParams(getContext(), attrs);
    }


    public static class LayoutParams extends LinearLayout.LayoutParams
            implements PercentLayoutHelper.PercentLayoutParams
    {
        private PercentLayoutHelper.PercentLayoutInfo mPercentLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
            mPercentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs);
        }

        @Override
        public PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo()
        {
            return mPercentLayoutInfo;
        }

        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr)
        {
            PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr);
        }

        public LayoutParams(int width, int height)
        {
            super(width, height);
        }


        public LayoutParams(ViewGroup.LayoutParams source)
        {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source)
        {
            super(source);
        }

    }

}
