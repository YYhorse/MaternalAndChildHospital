package com.example.maternalandchildhospital.publics.view.wheel;

import java.util.List;

import com.example.maternalandchildhospital.R;

import android.content.Context;

/**
 * The simple Array wheel adapter
 * 
 * @param <T>
 *            the element type
 */
public class ArrayWheelAdapter extends AbstractWheelTextAdapter {

	// items
	protected List<String> items;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the current context
	 * @param items
	 *            the items
	 */
	public ArrayWheelAdapter(Context context, List<String> items) {
		super(context);

		this.items = items;
		//设置字体颜色
		setTextColor(context.getResources().getColor(R.color.color_white));
	}

	@Override
	public CharSequence getItemText(int index) {
		if (index >= 0 && index < items.size()) {
			String item = items.get(index);
			if (item instanceof CharSequence) {
				return (CharSequence) item;
			}
			return item.toString();
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return items.size();
	}
}
