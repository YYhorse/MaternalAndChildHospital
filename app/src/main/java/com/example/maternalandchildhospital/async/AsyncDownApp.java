package com.example.maternalandchildhospital.async;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.maternalandchildhospital.activity.InitActivity;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Utils;

public class AsyncDownApp {

	// 是否更新
	private boolean updataTag = false;
	private String UPDATE_SERVERAPK = "maternalandchild.apk";

	private int fileSize = 0;
	private int downLoadFileSize = 0;

	private ProgressDialog pd = null;
	private boolean tag = false;
	private String updateTag = "";

	private boolean returnTag = false;
	private Context context;

	private UpdateUi updateUi;

	public AsyncDownApp(Context context, boolean tag, String str, UpdateUi uu) {
		updateUi = uu;
		updateTag = str;
		this.tag = tag;
		returnTag = false;
		this.context = context;
		showUpdataDialog(str);
	}

	private void updataApp() {
		if (updataTag) { // 更新
			pd = new ProgressDialog(context);
			pd.setTitle("正在下载");
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("请稍候...");
			pd.setOnCancelListener(cancelListener);
			pd.setCancelable(false);
			downFile(GlobalInfo.sysInfo.getDownloadUrl());
		}
	}

	private OnCancelListener cancelListener = new OnCancelListener() {

		@Override
		public void onCancel(DialogInterface dialog) {
		}
	};

	private void showUpdataDialog(String desc) {
		Utils.ShowUpdateDialog(context, desc, !tag, new UpdateUi() {

			@Override
			public void updateUI(Object ob) {
				int state = (Integer) ob;
				if (state == 1) { // 更新
					updataTag = true;
					updataApp();
				} else {
					updateUi.updateUI(state);
				}
			}
		});
	}

	private void showUpdataAppDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		if (!tag) {

			builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					updataTag = false;
					dialog.dismiss();
					if (!tag) {
						updateUi.updateUI(1);
					}
				}
			});
		}
		builder.setNeutralButton("更新", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				updataTag = true;
				updataApp();
				dialog.dismiss();
			}
		});
		builder.setOnCancelListener(cancelListener);
		builder.setCancelable(false);
		builder.show();
	}

	/**
	 * 下载apk
	 */
	private void downFile(final String url) {
		// Utils.Log("downapp = " + url);
		pd.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					fileSize = (int) entity.getContentLength();

					InputStream is = entity.getContent();

					sendMsg(0);
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = null;
						if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
							file = new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK);
							if (!file.exists()) {
								file.createNewFile();
							}
						} else {
							file = new File(context.getFilesDir(), UPDATE_SERVERAPK);
							if (!file.exists()) {
								file.createNewFile();
							}

						}
						fileOutputStream = new FileOutputStream(file);
						byte[] b = new byte[1024];
						int charb = -1;
						int count = 0;
						while ((charb = is.read(b)) != -1 && !returnTag) {
							fileOutputStream.write(b, 0, charb);
							count += charb;
							downLoadFileSize = count;
							sendMsg(1);

						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}

					if (returnTag) {
						return;
					}
					sendMsg(2);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

			// 定义一个Handler，用于处理下载线程与UI间通讯
			if (!Thread.currentThread().isInterrupted()) {
				switch (msg.what) {
				case 0:
					pd.setMax(100);
				case 1:
					int temp = fileSize / 100;

					pd.setProgress((downLoadFileSize / temp));
					break;
				case 2:
					pd.cancel();
					update();
					break;

				case -1:
					String error = msg.getData().getString("error");
					Toast.makeText(context, error, 1).show();
					break;
				}
			}

		}
	};

	/**
	 * 下载完成，通过handler将下载对话框取消
	 */
	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		handler.sendMessage(msg);
	}

	/**
	 * 安装应用
	 */
	private void update() {
		if (returnTag) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.setAction(Intent.ACTION_VIEW);

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), UPDATE_SERVERAPK)), "application/vnd.android.package-archive");

		} else {
			File file = new File(context.getFilesDir(), UPDATE_SERVERAPK);

			String cmd = "chmod 777 " + file.getAbsolutePath();
			try {
				Runtime.getRuntime().exec(cmd);

			} catch (Exception e) {

				e.printStackTrace();
			}

			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

			
		}
		context.startActivity(intent);
		if(!tag){
//			updateUi.updateUI(0);
		}else{
			CacheActivityManager.finishSingleActivityByClass(InitActivity.class);
		}
	}
}
