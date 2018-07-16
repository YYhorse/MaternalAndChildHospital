package com.example.maternalandchildhospital.bean;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;

public class ValidateButton {

	private MyThread myThread;
	private MyHandler myHandler;
	private Button currentValidateButton = null;

	private long StartTime;

	public class MyThread implements Runnable {
		private Thread thread;

		private boolean stopThread = false;

		private int time;

		@Override
		public void run() {
			thread = Thread.currentThread();
			// int num = 0;
			while (!stopThread) {
				if (GlobalInfo.validataButtonTag) {
					break;
				}
				long EndTime = System.currentTimeMillis();

				Message msg = new Message();
				Bundle bd = new Bundle();
				// time = 60 - num;
				long time = EndTime - StartTime;
//				Utils.log("time=" + time);
				if (time >= 60000) {
					GlobalInfo.validataButtonTag = true;
					bd.putString("time", "获取验证码");
					bd.putString("flag", "0");
					msg.setData(bd);
					myHandler.sendMessage(msg);
					GlobalInfo.remainTime = 60;
					break;
				}
				GlobalInfo.remainTime = (int) (60 - time / 1000);
				bd.putString("time", "获取验证码（" + (60 - time / 1000)  + "）");
				bd.putString("flag", "1");
				msg.setData(bd);
				myHandler.sendMessage(msg);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// num++;
			}
			GlobalInfo.validataButtonTag = true;
		}

		public void stopThread() {
			stopThread = true;
			if (thread != null) {
				thread.interrupt();
			}
		}

		public int getTime() {
			return time;
		}
	}

	public class MyHandler extends Handler {

		public MyHandler() {

		}

		public MyHandler(Looper l) {
			super(l);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bd = msg.getData();
			String time = bd.getString("time");
			String flag = bd.getString("flag");
			if (currentValidateButton != null) {
				currentValidateButton.setText(time);
				if("0".equals(flag)){
					currentValidateButton.setBackgroundResource(R.drawable.corner_btn_code_bg);
				} else if("1".equals(flag)){
					currentValidateButton.setBackgroundResource(R.drawable.corner_grey_btn);
				}
			}
		}
	}

	public void setButton(Button button) {
		currentValidateButton = button;
	}

	public int getTime() {
		return myThread.getTime();
	}

	public void stopButtonThread() {
		if (myThread != null) {
			myThread.stopThread = true;
			GlobalInfo.validataButtonTag = true;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void getRunTime() {

		StartTime = System.currentTimeMillis();
		GlobalInfo.validataButtonTag = false;
		// setButton(button);
		currentValidateButton = null;
		if (myThread != null) {
			myThread.stopThread();
			myThread = null;
		}
		myHandler = new MyHandler();
		myThread = new MyThread();
		new Thread(myThread).start();
	}
}
