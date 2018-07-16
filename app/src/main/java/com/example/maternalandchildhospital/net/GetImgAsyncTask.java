package com.example.maternalandchildhospital.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.example.maternalandchildhospital.publics.util.Utils;

public class GetImgAsyncTask extends AsyncTask<String, Integer, String> {

	private Drawable drawable;
	private ImageView iv;

	private String imgurl;

	private boolean saveTag = false;

	public GetImgAsyncTask(ImageView iv, String imgurl, boolean saveTag) {
		this.iv = iv;
		this.imgurl = imgurl;
		this.saveTag = saveTag;
	}

	@Override
	protected String doInBackground(String... params) {
//		Utils.Log("imgurl = " + imgurl);
		String temp[] = imgurl.split("/");
//		Utils.Log(temp[temp.length - 1]);
		if(temp[temp.length-1].length()>5&&"jpg".equals(temp[temp.length-1].substring(temp[temp.length-1].length()-3, temp[temp.length-1].length()))){
		    saveTag = true;
		}else {
		    saveTag = false;
		}

		URL url;
		try {
			url = new URL(imgurl);

			InputStream is = url.openStream();
			int readNum = 0;
			while (is.read() != -1) {
				readNum++;
			}

			if (readNum == 0) {
				is.close();
				return null;
			}
			is.close();
			is = url.openStream();
			if (drawable == null) {
				drawable = Drawable.createFromStream(is, temp[temp.length - 1]);
			} else {
				drawable = null;
				drawable = Drawable.createFromStream(is, temp[temp.length - 1]);
			}
//			Utils.Log("imgage = " + drawable);
			if (saveTag) {
				Utils.saveBitmapToFile(Utils.drawableToBitmap(drawable), Environment.getExternalStorageDirectory() + "/babypic/" + temp[temp.length - 1]);
			}
			return "ok";
		} catch (MalformedURLException e) {

			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 前台操作方法，该方法工作在UI线程
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (result != null && result.equals("ok")) {
			// Utils.Log("ok" );
			
//			iv.setImageDrawable(drawable);
			iv.setImageBitmap(Utils.toRoundBitmap(Utils.drawableToBitmap(drawable)));
		} else {
			Utils.Log("获取图片失败");
			// iv.setImageResource();
			// iv.setVisibility(View.VISIBLE);
		}
	}
}
