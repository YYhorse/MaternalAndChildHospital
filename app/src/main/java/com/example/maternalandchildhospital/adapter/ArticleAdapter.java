package com.example.maternalandchildhospital.adapter;

import java.util.List;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.bean.Article;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author hxc<p>
 *   产后小贴士 适配器
 */
public class ArticleAdapter extends BaseAdapter {

	private List<Article> list;
	private Context mContext;

	private LayoutInflater inflater;

	public ArticleAdapter(List<Article> list, Context mContext) {
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
			convertView = inflater.inflate(R.layout.layout_article_item, null);
			vh.tvContent = (TextView) convertView.findViewById(R.id.tv_content_article_item);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.tvContent.setText(list.get(position).getArticleTitle());

		return convertView;
	}

	private class ViewHolder {

		TextView tvContent;

	}

}
