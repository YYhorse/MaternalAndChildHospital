package com.example.maternalandchildhospital.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.activity.OnlineConsultingActivity;
import com.example.maternalandchildhospital.bean.ExpertInfo;
import com.example.maternalandchildhospital.net.HttpxUtils.ImagexUtils;
import com.example.maternalandchildhospital.publics.util.PopMessageUtil;

import java.util.List;

public class ExpertInfoAdapter extends BaseAdapter{
    private OnlineConsultingActivity mContext = null;
    private List<ExpertInfo.Data.Ydata> ydata = null;
    public int NowPostion = 0;
    private boolean updataStatus = false;

    public ExpertInfoAdapter(OnlineConsultingActivity mactivity, List<ExpertInfo.Data.Ydata> yData){
        mContext = mactivity;
        ydata = yData;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != ydata) {
            count = ydata.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        ExpertInfo.Data.Ydata item = null;
        if (null != ydata) {
            item = ydata.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder{
        TextView Item_NickName,Item_Text,Item_Contecl;
        ImageView Item_FaceImage;
        Button Item_Online;
        LinearLayout Item_Expertinfo;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(null == convertView){
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.item_expertinfo, null);
            viewHolder.Item_Expertinfo = (LinearLayout) convertView.findViewById(R.id.Item_Expertinfo);
            viewHolder.Item_NickName = (TextView) convertView.findViewById(R.id.Item_NickName);
            viewHolder.Item_Text = (TextView) convertView.findViewById(R.id.Item_Text);
            viewHolder.Item_Contecl = (TextView) convertView.findViewById(R.id.Item_Contecl);
            viewHolder.Item_FaceImage = (ImageView) convertView.findViewById(R.id.Item_FaceImage);
            viewHolder.Item_Online = (Button) convertView.findViewById(R.id.Item_Online);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();
        //---------------获取最新数据--------------//
        final ExpertInfo.Data.Ydata ydataItem = (ExpertInfo.Data.Ydata) getItem(position);
        if(ydataItem.getDetail()!=null&&updataStatus==true){
            updataStatus = false;
            viewHolder.Item_NickName.setText(ydataItem.getDetail().getNick_name());
            viewHolder.Item_Text.setText(ydataItem.getDetail().getText());
            viewHolder.Item_Contecl.setText("QQ:"+ydataItem.getDetail().getQq());
            ImagexUtils.displayCircluarPicForUrl(viewHolder.Item_FaceImage, ydataItem.getUser_image_url());
        }
        //----------------点击事件-----------------//
        viewHolder.Item_Online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NowPostion = position;
                try {
                    PopMessageUtil.Log("跳转QQ");
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin="+ydata.get(NowPostion).getDetail().getQq();//uin是发送过去的qq号码
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {
                    e.printStackTrace();
                    PopMessageUtil.showToastLong("请检查是否安装QQ");
                }
            }
        });
        return convertView;
    }
    public void upDataData( List<ExpertInfo.Data.Ydata> zData){
        ydata = zData;
        updataStatus = true;
        notifyDataSetChanged();
    }
}
