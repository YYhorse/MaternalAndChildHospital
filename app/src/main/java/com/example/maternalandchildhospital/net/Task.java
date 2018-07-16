package com.example.maternalandchildhospital.net;

import java.util.concurrent.Callable;

public abstract class Task<V> implements Callable<V>, Runnable {
	protected TaskListener listener;
	public boolean cancelConnect;
	public boolean appCanNetWork = true;

	public Task(TaskListener listener) {
		this.listener = listener;
	}

	public abstract V get() throws Exception;

	public void run() {
		try {
			call();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public V call() throws Exception {
		V obj = null;
		try {
			if (listener != null) {
				listener.taskStarted(this);
			}
			if (appCanNetWork) {
				obj = get();
			} else {
				listener.taskFailed(this, appCanNetWork, cancelConnect, null);
			}
		} catch (Throwable ex) {
			if (listener != null) {
				listener.taskFailed(this, appCanNetWork, cancelConnect, ex);
			}
			return null;
		}
		if (listener != null && appCanNetWork) {
			listener.taskCompleted(this, obj);
		}
		return obj;
	}
}
