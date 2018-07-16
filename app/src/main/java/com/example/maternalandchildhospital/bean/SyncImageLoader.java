package com.example.maternalandchildhospital.bean;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.example.maternalandchildhospital.publics.util.Md5Util;
import com.example.maternalandchildhospital.publics.util.Utils;

public class SyncImageLoader {

	private Object lock = new Object();

	private boolean mAllowLoad = true;

	private boolean firstLoad = true;

	private int mStartLoadLimit = 0;

	private int mStopLoadLimit = 20;

	// private int mTag = 0;

	final Handler handler = new Handler();

	private HashMap<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();

	public interface OnImageLoadListener {
		public void onImageLoad(ImageView view, Integer t, Drawable drawable, int tag);

		public void onMyScrollLayout(Integer t, List<Drawable> drawables);

		public void onError(ImageView view, Integer t, int tag);
	}

	public void setLoadLimit(int startLoadLimit, int stopLoadLimit) {
		if (startLoadLimit > stopLoadLimit) {
			return;
		}
		mStartLoadLimit = startLoadLimit;
		mStopLoadLimit = stopLoadLimit;
	}

	public void restore() {
		mAllowLoad = true;
		firstLoad = true;
	}

	public void lock() {
		mAllowLoad = false;
		firstLoad = false;
	}

	public void unlock() {
		mAllowLoad = true;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public void loadImage(final Context context, final ImageView view, final int tag, final Integer t, final List<String> imageUrl, final OnImageLoadListener listener) {

		if (tag < 100) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (!mAllowLoad) {
						synchronized (lock) {
							try {
								lock.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					if (mAllowLoad && firstLoad) {
						loadImage(view, imageUrl.get(0), t, tag, listener);
					} else if (mAllowLoad && t <= mStopLoadLimit && t >= mStartLoadLimit) {
						loadImage(view, imageUrl.get(0), t, tag, listener);
					}
				}

			}).start();
		} else {

			new Thread(new Runnable() {
				List<Drawable> drawable = new ArrayList<Drawable>();

				@Override
				public void run() {

					for (final String str : imageUrl) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								if (!mAllowLoad) {
									synchronized (lock) {
										try {
											lock.wait();
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
								// Utils.Log("run = "+t +"=" +mStartLoadLimit
								// +"|"+mStopLoadLimit);
								if (mAllowLoad && t <= mStopLoadLimit && t >= mStartLoadLimit) {
									if (imageCache.containsKey(str)) {
										// Utils.Log("imageCache");
										SoftReference<Drawable> softReference = imageCache.get(str);
										final Drawable d = softReference.get();
										// Utils.Log("d = " + d);
										if (d != null) {
											handler.post(new Runnable() {
												@Override
												public void run() {
													if (mAllowLoad) {
														drawable.add(d);
													}
												}
											});
											return;
										}
									}
									try {
										final Drawable d = loadImageFromUrl(str, tag);
										if (d != null) {
											imageCache.put(str, new SoftReference<Drawable>(d));
										}
										handler.post(new Runnable() {
											@Override
											public void run() {
												if (mAllowLoad) {
													drawable.add(d);
												}
											}
										});
									} catch (IOException e) {
										Utils.Log("IOException = ");
										handler.post(new Runnable() {
											@Override
											public void run() {
												drawable.add(new BitmapDrawable());
											}
										});
										e.printStackTrace();
									}
								}
							}

						}).start();
					}
					while (true) {
						// Utils.Log("mAllowLoad = " + mAllowLoad +
						// "|"+imageUrl.size() + "|" + drawable.size());
						if (drawable.size() == imageUrl.size() && mAllowLoad) {

							listener.onMyScrollLayout(t, drawable);
							return;
						}
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			}).start();

		}
	}

	private void loadImage(final ImageView view, final String mImageUrl, final Integer mt, final int mTag, final OnImageLoadListener mListener) {
		// Utils.Log(mt + "|" + mImageUrl);
		if (imageCache.containsKey(mImageUrl)) {
			// Utils.Log("imageCache");
			SoftReference<Drawable> softReference = imageCache.get(mImageUrl);
			final Drawable d = softReference.get();
			// Utils.Log("d = " + d);
			if (d != null) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (mAllowLoad) {
							mListener.onImageLoad(view, mt, d, mTag);
						}
					}
				});
				return;
			}
		}
		try {
			final Drawable d = loadImageFromUrl(mImageUrl, mTag);
			if (d != null) {
				imageCache.put(mImageUrl, new SoftReference<Drawable>(d));
			}
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (mAllowLoad) {
						mListener.onImageLoad(view, mt, d, mTag);
					}
				}
			});
		} catch (IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mListener.onError(view, mt, mTag);
				}
			});
			e.printStackTrace();
		}
	}

	public static Drawable loadImageFromUrl(String url, int tag) throws IOException {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File f;
			File file;
			// if (tag == 99) {
			// f = new File(url);
			// // file = new File(url);
			// } else {
			f = new File(Environment.getExternalStorageDirectory() + "/CETC/" + Md5Util.getMD5ByString(url));
			file = new File(Environment.getExternalStorageDirectory() + "/CETC/");

			// }

			Log.i("123", "model_------>" + Build.MODEL);

			if (f.exists()) {
				FileInputStream fis = new FileInputStream(f);
				Drawable d = Drawable.createFromStream(fis, "src");
				return d;
			}
			if (!file.exists()) {
				file.mkdirs();
			}
			file.createNewFile();
			URL m = new URL(url);
			InputStream i = (InputStream) m.getContent();
			DataInputStream in = new DataInputStream(i);
			FileOutputStream out = new FileOutputStream(f);
			byte[] buffer = new byte[1024];
			int byteread = 0;
			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			in.close();
			out.close();
			// Drawable d = Drawable.createFromStream(i, "src");
			return loadImageFromUrl(url, tag);
		} else {
			URL m = new URL(url);
			InputStream i = (InputStream) m.getContent();
			Drawable d = Drawable.createFromStream(i, "src");
			return d;
		}

	}

//	public static String getPathLoadImage(String url) {
//		String path = FileAccessor.IMESSAGE_RICH_TEXT + "/" + DemoUtils.md5(url) + ".jpg";
//		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			File f = new File(path);
//			if (f.exists()) {
//				return path;
//			} else {
//
//				File isf = new File(FileAccessor.IMESSAGE_RICH_TEXT+ "/");
//				if (!isf.exists()) {
//					isf.mkdirs();
//				}
//				try {
//					isf.createNewFile();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//
//			URL m;
//			DataInputStream in = null;
//			FileOutputStream out = null;
//			try {
//				m = new URL(url);
//				InputStream i = (InputStream) m.getContent();
//				in = new DataInputStream(i);
//				out = new FileOutputStream(f);
//				byte[] buffer = new byte[1024];
//				int byteread = 0;
//				while ((byteread = in.read(buffer)) != -1) {
//					out.write(buffer, 0, byteread);
//				}
//				in.close();
//				out.close();
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			return getPathLoadImage(url);
//		} else {
//			return "no sdcard";
//		}
//
//	}

}
