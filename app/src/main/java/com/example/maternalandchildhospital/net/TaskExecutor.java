package com.example.maternalandchildhospital.net;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.net.ConnectivityManager;

public class TaskExecutor {
	private final int CORE_POOL_SIZE = 3;
	private final int MAX_POOL_SIZE = 5;
	private final int KEEP_ALIVE = 3;
	protected ConnectivityManager manager;
	protected Context ctx;
	private ThreadPoolExecutor executor;
	private BlockingQueue<Runnable> queue = new PriorityBlockingQueue<Runnable>(5);

	public TaskExecutor(Context _ctx) {
		executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
				KEEP_ALIVE, TimeUnit.SECONDS, queue,new ThreadPoolExecutor.DiscardOldestPolicy());
		ctx = _ctx;
	}

	public void execute(Task<?> task) {
		executor.execute(task);
	}
  
	public void shutdown() {
		executor.shutdownNow();
	}
}
