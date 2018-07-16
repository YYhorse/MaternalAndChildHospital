package com.example.maternalandchildhospital.adapter;

import java.util.List;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.bean.Article;
import com.example.maternalandchildhospital.bean.MsgInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author hxc
 *         <p>
 *         消息 适配器
 */
public class MsgAdapter extends BaseAdapter {

	private List<MsgInfo> list;
	private Context mContext;

	private LayoutInflater inflater;

	public MsgAdapter(List<MsgInfo> list, Context mContext) {
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
			convertView = inflater.inflate(R.layout.layout_msg_item, null);
			vh.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon_msg_item);
			vh.tvTitle = (TextView) convertView.findViewById(R.id.tv_title_msg_item);
			vh.tvDate = (TextView) convertView.findViewById(R.id.tv_date_msg_item);
			vh.tvContent = (TextView) convertView.findViewById(R.id.tv_content_msg_item);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		if ("0".equals(list.get(position).getReadFlag())) {
			vh.ivIcon.setImageResource(R.drawable.icon_news_unread);
			vh.tvTitle.setTextColor(mContext.getResources().getColor(R.color.color_dark_orange));
		} else {
			vh.ivIcon.setImageResource(R.drawable.icon_news_read);
			vh.tvTitle.setTextColor(mContext.getResources().getColor(R.color.color_txt_dark_grey));
		}
		vh.tvTitle.setText(list.get(position).getType() + "消息");

		vh.tvDate.setText(list.get(position).getTime());
		vh.tvContent.setText(list.get(position).getMsgText());

		return convertView;
	}

	private class ViewHolder {

		ImageView ivIcon;
		TextView tvTitle;
		TextView tvContent;
		TextView tvDate;

	}

	public void setList(List<MsgInfo> list2) {
		this.list = list2;
		notifyDataSetChanged();
	}

}
