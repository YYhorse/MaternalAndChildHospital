package com.example.maternalandchildhospital.adapter;

import java.util.List;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.bean.Article;
import com.example.maternalandchildhospital.bean.ProductionInspectionManual;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.EnterDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author hxc
 *         <p>
 *         产检手册 适配器
 */
public class NoteBookAdapter extends BaseAdapter {

	private List<ProductionInspectionManual> list;
	private Context mContext;

	private LayoutInflater inflater;

	public NoteBookAdapter(List<ProductionInspectionManual> list, Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.layout_notebook_item, null);
			vh.tvYunWeek = (TextView) convertView.findViewById(R.id.tv_yunweek_notebook_item);
			vh.tvDate = (TextView) convertView.findViewById(R.id.tv_date_notebook_item);
			vh.isComplete = (ImageView) convertView.findViewById(R.id.iv_iscomplete_notebook_item);
			vh.tvContent = (TextView) convertView.findViewById(R.id.tv_content_notebook_item);
			vh.tvReport = (TextView) convertView.findViewById(R.id.tv_report_notebook_item);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.tvYunWeek.setText(list.get(position).getYunWeeks());
		vh.tvDate.setText(list.get(position).getDueDate() + "  " + list.get(position).getWeekName());
		if ("0".equals(list.get(position).getStatus())) {
			vh.isComplete.setImageResource(R.drawable.btn_check);
			vh.tvReport.setVisibility(View.GONE);
		} else {
			vh.isComplete.setImageResource(R.drawable.btn_check_finish);
			vh.tvReport.setVisibility(View.VISIBLE);
		}

		vh.tvContent.setText("第" + list.get(position).getSerialNoCN() + "次产检");
		vh.tvReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.ShowEnterDialog(mContext, "敬请期待", "", "确定", "", "", EnterDialog.MODE_NOTITLE_SINGLE, null);
			}
		});

		return convertView;
	}

	private class ViewHolder {

		TextView tvYunWeek;
		TextView tvDate;
		ImageView isComplete;
		TextView tvContent;
		TextView tvReport;

	}

}
