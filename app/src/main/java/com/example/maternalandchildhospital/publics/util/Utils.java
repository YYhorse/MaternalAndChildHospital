package com.example.maternalandchildhospital.publics.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.bean.MyRoundedBitmapDisplayer;
import com.example.maternalandchildhospital.bean.ProductionInspectionManual;
import com.example.maternalandchildhospital.bean.ValidateButton;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.view.ChooseDateDialog;
import com.example.maternalandchildhospital.publics.view.EnterDialog;
import com.example.maternalandchildhospital.publics.view.LoadDialog;
import com.example.maternalandchildhospital.publics.view.NoteBookDialog;
import com.example.maternalandchildhospital.publics.view.PromptDialog;
import com.example.maternalandchildhospital.publics.view.VersionUpdateDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

@SuppressLint("NewApi")
public class Utils {
	public static String getCurrentTime(String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss", new Date());
	}

	public static boolean showLog = true;

	public static void Log(String msg) {
		if (showLog) {
			android.util.Log.e("cetc", msg);
		}

	}

	public static void Toast(Context context, String test) {
		Toast.makeText(context, test, Toast.LENGTH_SHORT).show();
	}

	// 防暴力点击
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 300) {
			return false;// 不可点击
		}
		lastClickTime = time;
		return true;// 可以点击
	}

	public static Bitmap getSampleBitmap(String filePath, int width, int height) {
		BitmapFactory.Options op = new BitmapFactory.Options();
		// 这个设置是为了加载只加载图片属性，获取图片宽高（不会加载Bitmap，这样就不会占用内存，出现OMM）
		op.inJustDecodeBounds = true;

		// 获取图片的宽高，因为不加在Bitmap，所以返回的Bitmap是空值，图片的属性被储存在Option变量内
		BitmapFactory.decodeFile(filePath, op);
		int old_width = op.outWidth;
		int old_height = op.outHeight;
		// 缩放比例
		int sample_size = 0;
		// 判断宽高度各自的缩放大小，如果高度缩放大，就使用宽度计算缩放比；如果宽度缩放大，就使用高度计算缩放比
		// new
		// BigDecimal(float).setScale(i,j)方法是通过i和j判断获取int值，这里是取float的五舍六入的值
		// i是舍弃段的开始值，j是舍弃段的结束值
		if (Math.abs(old_width - width) < Math.abs(old_height - height))
			sample_size = new BigDecimal((float) old_width / (float) width).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue();
		else
			sample_size = new BigDecimal((float) old_height / (float) height).setScale(0, BigDecimal.ROUND_HALF_DOWN).intValue();

		// 修改获取方式为获取Bitmap
		op.inJustDecodeBounds = false;
		// 设置缩放比
		op.inSampleSize = sample_size;
		// 读取文件并转换为Bitmap
		return BitmapFactory.decodeFile(filePath, op);
	}

	public static String SendNetJson(JSONObject jsonData) {
		String returnStr = "";

		String data = jsonData.toString();
		try {
			data = data.replace("\r", "");
			data = data.replace("\n", "");
			data = data.replace("\r\n", "");
			JSONObject mainObject = new JSONObject();
			mainObject.put("check", Md5Util.getMD5ByString(data + "123456"));
			mainObject.put("json", data);
			returnStr = mainObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}

	public static String ReturnNetJsonForSys(String str) {
		str = Utils.toStrSys(str);
		String returnStr = "";
		try {
			JSONObject person = new JSONObject(str);
			String check = person.optString("check");
			String json = person.optString("json");
			returnStr = json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnStr;
	}

	public static String ReturnNetJson(String str) {
		str = Utils.toStr(str);
		String returnStr = "";
		try {
			JSONObject person = new JSONObject(str);
			String check = person.optString("check");
			String json = person.optString("json");
			returnStr = json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnStr;
	}

	public static JSONObject ReturnNetJsonThree(String str) {
		str = Utils.toStr(str);
		try {
			JSONObject person = new JSONObject(str);
			return person.optJSONObject("json");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static JSONObject ReturnNetJsonTwo(String str) {
		str = Utils.toStrXiongWei(str);
		try {
			JSONObject person = new JSONObject(str);
			return person.optJSONObject("json");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getNumberTo6(double number) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setRoundingMode(RoundingMode.HALF_UP);
		nf.setMaximumFractionDigits(6);
		return nf.format(number);
	}

	public static String getNumberTo2(double number) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setRoundingMode(RoundingMode.HALF_UP);
		nf.setMaximumFractionDigits(2);
		return nf.format(number);
	}

	public static String getNumberTo1(double number) {
		return String.format("%.1f", number);
	}

	public static String getDoubleString(String number) {
		if (strNullMeans(number)) {
			return "0";
		}
		String[] aa = number.split("\\.");
		if (aa.length > 1) {
			if (aa[1].length() > 6) {
				number = aa[0] + "." + aa[1].substring(0, 6);
			}
		}
		return number;
	}

	// 保存本地信息
	public static void saveLocalInfo(Context context, String databaseTag, String dataTag, String data) {
		SharedPreferences share = context.getSharedPreferences(databaseTag, 0);
		if (share != null) {
			share.edit().putString(dataTag, data).commit();
			share = null;
		}
	}

	// 清除本地信息
	public static void clearLocalInfo(Context context, String databaseTag, String dataTag) {
		SharedPreferences share = context.getSharedPreferences(databaseTag, 0);
		if (share != null) {
			share.edit().putString(dataTag, "").commit();
			share = null;
		}
	}

	// 读取本地信息
	public static String readLocalInfo(Context context, String databaseTag, String dataTag) {
		SharedPreferences share = context.getSharedPreferences(databaseTag, 0);
		String result = null;
		if (share != null) {
			result = share.getString(dataTag, "");
			share = null;
		}
		return result;
	}

	// 字符串是否为空 true 空 false 有内容
	public static boolean strNullMeans(String str) {
		if (str == null || str.equals("") || str.equals("null")) {
			return true;
		}
		return false;
	}

	public static boolean strLength(String str, int length) {
		if (str.length() != length) {
			return false;
		}
		return true;
	}

	/**
	 * @param 电话号码判断
	 * @return
	 */
	public static boolean telJudge(Context context, String tel) {
		if (Utils.strNullMeans(tel)) {
			Utils.Toast(context, "请输入手机号");
			return false;
		}

		if (!Utils.strLength(tel, 11)) {
			Utils.Toast(context, "请输入11位手机号码");
			return false;
		}

		String check = "^(1[3,4,5,8,7])\\d{9}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(tel);
		if (!matcher.matches()) {
			Utils.Toast(context, "请输入正确的手机号码");
			return false;
		}
		return true;
	}

	/**
	 * @param 密码判断
	 * @return
	 */
	public static boolean pswJudge(Context context, String psw) {
		if (Utils.strNullMeans(psw)) {
			Utils.Toast(context, "请输入密码");
			return false;
		}

		if (!Utils.pswJudge(psw)) {
			// Utils.Toast(context, "密码格式或长度不正确");
			Utils.ShowEnterDialog(context, "密码长度应为6~16个字符,由数字，字母或两者组合，请重新输入", "", "确定", "", "提示", EnterDialog.MODE_SINGLE_BUTTON, null);
			return false;
		}
		return true;
	}

	/**
	 * 检测邮箱合法性
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmailValid(String email) {
		if ((email == null) || (email.trim().length() == 0)) {
			return false;
		}
		String regEx = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(email.trim().toLowerCase());

		return m.find();
	}

	// 密码格式判断
	public static boolean pswJudge(String str) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9]{6,16}$");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static boolean strMinMaxLength(String str, int minLength, int maxLength) {
		if (str.length() < minLength || str.length() > maxLength) {
			return false;
		}
		return true;
	}

	// 保存图片

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		Bitmap bitmap = Bitmap.createBitmap(

		drawable.getIntrinsicWidth(),

		drawable.getIntrinsicHeight(),

		drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

		: Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

		drawable.draw(canvas);

		return bitmap;

	}

	public static void saveBitmapToFile(Bitmap bitmap, String _file) throws IOException {
		BufferedOutputStream os = null;
		try {
			File file = new File(_file);
			int end = _file.lastIndexOf(File.separator);
			String _filePath = _file.substring(0, end);
			File filePath = new File(_filePath);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			file.createNewFile();
			os = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {

				}
			}
		}
	}

	public static String toStr(String str) {
		str = str.replace("\\n", "");

		str = str.replace("\\r\\n", "");
		str = str.replace("\\", "");
		str = str.replace("\"{", "{");
		str = str.replace("}\"", "}");
		str = str.replace("\"[", "[");
		str = str.replace("]\"", "]");

		return str;
	}

	public static String toStrSys(String str) {
		/**
		 * 因为需要保留\n故先把 \n 替换成 ~enter
		 */

		str = str.replace("\\n", "~enter");
		str = toStr(str);
		/**
		 * 替换好后，把~enter替换成\n
		 */
		str = str.replace("~enter", "\n");

		return str;
	}

	public static String toStrHome(String str) {
		str = str.replace("\\", "");
		return str;
	}

	public static String toStrXiongWei(String str) {
		str = str.replace("\\r", "");
		str = str.replace("\\n", "");
		str = str.replace("\\t", "");
		str = str.replace("#", "");

		str = str.replace("\\r\\n", "");
		str = str.replace("\\", "");
		str = str.replace("\"{", "{");
		str = str.replace("}\"", "}");
		str = str.replace("\"[", "[");
		str = str.replace("]\"", "]");
		return str;
	}

	/**
	 * 判断字符串是否为日期格式
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isDateStringValid(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
		// 输入对象不为空
		try {
			sdf.parse(date);
			return true;
		} catch (java.text.ParseException e) {
			return false;
		}
	}

	public static Bitmap decodeByUIL(String pathName) {

		// 首先不加载图片,仅获取图片尺寸
		final BitmapFactory.Options options = new BitmapFactory.Options();
		// 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
		options.inJustDecodeBounds = true;
		// 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
		BitmapFactory.decodeFile(pathName, options);

		// 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
		options.inSampleSize = computeImageSampleSize(options.outWidth, options.outWidth, 200, 200, true);

		// 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
		options.inJustDecodeBounds = false;
		// 利用计算的比例值获取压缩后的图片对象
		return BitmapFactory.decodeFile(pathName, options);
	}

	public static int computeImageSampleSize(int width, int height, int limitWidth, int limitHeight, boolean powerOf2Scale) {

		int scale = 1;

		int widthScale = width / limitWidth;
		int heightScale = height / limitHeight;
		if (powerOf2Scale) {
			while (width / 2 >= limitWidth || height / 2 >= limitHeight) { // ||
				width /= 2;
				height /= 2;
				scale *= 2;
			}
		} else {
			scale = Math.max(widthScale, heightScale); // max
		}
		if (scale < 1) {
			scale = 1;
		}
		return scale;
	}

	public byte[] BitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
		return baos.toByteArray();
	}

	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	// 防止 bitmap OutOfMemoryError
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping
			// zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 检测网路是否连通
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnNet(Context context) {
		// 获得手机所有连接管理对象（包括对wi-fi等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获得网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已连接
					if (info.getState() == NetworkInfo.State.CONNECTED)

						return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	// 身份证号
	public static boolean IdentityJudge(String str) {
		if (str.replace("1", "").length() < 6) {
			return false;
		}

		if (str.length() == 18) {// \\d{1}
			String check = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-z])$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(str);
			if (!matcher.matches()) {
				return false;
			}
		} else if (str.length() == 15) {
			String check = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(str);
			if (!matcher.matches()) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static BitmapDrawable readBitmapDrawable(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;

		opt.inPurgeable = true;

		opt.inInputShareable = true;

		InputStream is = context.getResources().openRawResource(resId);

		Bitmap bm = BitmapFactory.decodeStream(is, null, opt);

		return new BitmapDrawable(context.getResources(), bm);

	}

	public static String getXingstring(String str, int start, int end) {

		if (str.length() <= (start + end)) {
			return str;
		}
		String xingStr = "";
		for (int i = 0; i < (str.length() - start - end); i++) {
			xingStr += "*";
		}

		String newStr = str.substring(0, start) + xingStr + str.substring(str.length() - end, str.length());

		return newStr;
	}

	/**
	 * 
	 * @param str
	 *            显示的文字
	 * @param start
	 *            星星开始的位置
	 * @param end
	 *            星星结束的位置
	 * @return
	 */
	public static String getFitStringByStar(String str, int start, int end) {
		StringBuilder fitStr = new StringBuilder("");
		if (!TextUtils.isEmpty(str)) {
			fitStr.append(str.substring(0, start));
			for (int i = start; i < end; i++) {
				fitStr.append("*");
			}
			fitStr.append(str.substring(end));

		}
		return fitStr.toString();

	}

	/** 日期判断 */
	public static boolean dateJudge(String str) {
		boolean state = false;
		if (str.length() != 8) {
			return false;
		}
		try {
			java.text.SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd");
			dFormat.setLenient(false);
			java.util.Date d = dFormat.parse(str);
			state = true;
		} catch (ParseException e) {
			e.printStackTrace();
			state = false;
		}
		return state;
	}

	/** 纯数字判断 */
	public static boolean numJudge(String str) {
		Pattern p = Pattern.compile("[0-9]*");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			// Toast.makeText(Main.this,"输入的是数字",
			// Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	/** 纯字母判断 */
	public static boolean letterJudge(String str) {
		Pattern p = Pattern.compile("[a-zA-Z]*");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			// Toast.makeText(Main.this,"输入的是字母",
			// Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	/** 纯中文判断 */
	public static boolean chineseJudge(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]*");
		Matcher m = p.matcher(str);
		if (m.matches()) {
			// Toast.makeText(Main.this,"输入的是汉字",
			// Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}

	public static TextWatcher getTextWatcher(final EditText et, final ImageView iv) {

		et.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && et.getText().toString().length() > 0) {
					iv.setVisibility(View.VISIBLE);
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated
							// method stub
							et.setText("");
						}
					});
				} else {
					iv.setVisibility(View.INVISIBLE);
				}
			}
		});

		TextWatcher watch = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (et.hasFocus() && s.length() > 0) {
					iv.setVisibility(View.VISIBLE);
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated
							// method stub
							et.setText("");
						}
					});
				} else {
					iv.setVisibility(View.INVISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		};

		return watch;
	}

	public static TextWatcher getTextWatcher(final EditText et, final View view) {

		et.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus && et.getText().toString().length() > 0) {
					view.setVisibility(View.VISIBLE);
					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated
							// method stub
							et.setText("");
						}
					});
				} else {
					view.setVisibility(View.INVISIBLE);
				}
			}
		});

		TextWatcher watch = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (et.hasFocus() && s.length() > 0) {
					view.setVisibility(View.VISIBLE);
					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated
							// method stub
							et.setText("");
						}
					});
				} else {
					view.setVisibility(View.INVISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		};

		return watch;
	}

	/**
	 * dp转px
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int dpToPx(Context context, int dp) {
		// 密度比dpi
		float scale = context.getResources().getDisplayMetrics().density;
		// 140dp转成px动态加载
		int px = (int) (scale * dp + 0.5f);
		return px;
	}

	public static boolean savePdfToFile(InputStream is, String _file) throws IOException {
		try {
			File file = new File(_file);
			int end = _file.lastIndexOf(File.separator);
			String _filePath = _file.substring(0, end);
			File filePath = new File(_filePath);
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			file.createNewFile();

			DataInputStream in = new DataInputStream(is);
			FileOutputStream out = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int byteread = 0;
			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread);
			}
			in.close();
			out.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utils.Log(path + "=" + degree);
		return degree;
	}

	/**
	 * 旋转图片
	 */
	public static Bitmap setBitmap(Bitmap bm, int degree, int w, int h) {
		if (bm == null || bm.getWidth() == 0 || bm.getHeight() == 0)
			return null;

		float Maxwidth = w;
		float Maxhight = h;
		if (bm.getWidth() > Maxwidth || bm.getHeight() > Maxhight) {
			float mx = Maxwidth / bm.getWidth() * 1.5f;
			float my = Maxwidth / bm.getHeight() * 1.5f;
			float sc = Math.min(mx, my);
			Matrix mMatrix = new Matrix();
			mMatrix.setScale(sc, sc);
			mMatrix.postRotate(degree);
			Bitmap scaleBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mMatrix, false);
			bm = scaleBitmap;

		} else if (bm.getWidth() < Maxwidth / 2 || bm.getHeight() < Maxhight / 2) {
			float mx = 2f;
			float my = 2f;
			float sc = Math.min(mx, my);
			Matrix mMatrix = new Matrix();
			mMatrix.setScale(sc, sc);
			Bitmap scaleBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mMatrix, false);
			bm = scaleBitmap;
		} else {
			float mx = Maxwidth / bm.getWidth() * 1.5f;
			float my = Maxwidth / bm.getHeight() * 1.5f;
			float sc = Math.min(mx, my);
			Matrix mMatrix = new Matrix();
			mMatrix.setScale(sc, sc);
			mMatrix.postRotate(degree);
			Bitmap scaleBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mMatrix, false);
			bm = scaleBitmap;
		}

		return bm;
	}

	// 压缩图片
	public static Bitmap getSmallBitmap(Bitmap image) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		newOpts.inPreferredConfig = Config.ARGB_8888;// 降低图片从ARGB888到RGB565
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
	}

	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 在二维码中间添加Logo图案
	 */
	private static Bitmap addLogo(Bitmap src, Bitmap logo) {
		if (src == null) {
			return null;
		}

		if (logo == null) {
			return src;
		}

		// 获取图片的宽高
		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();
		int logoWidth = logo.getWidth();
		int logoHeight = logo.getHeight();

		if (srcWidth == 0 || srcHeight == 0) {
			return null;
		}

		if (logoWidth == 0 || logoHeight == 0) {
			return src;
		}

		// logo大小为二维码整体大小的1/5
		float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
		Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
		try {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawBitmap(src, 0, 0, null);
			canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
			canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

			canvas.save(Canvas.ALL_SAVE_FLAG);
			canvas.restore();
		} catch (Exception e) {
			bitmap = null;
			e.getStackTrace();
		}

		return bitmap;
	}

	/**
	 * <p>
	 * Title: setListViewHeightBasedOnChildren
	 * </p>
	 * <p>
	 * Description: 设置竖直方向联动滑动
	 * </p>
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static void setListViewOnTouchAndScrollListener(final ListView listView1, final ListView listView2, final ImageView iv) {

		if (iv != null) {
			iv.setVisibility(View.GONE);
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!Utils.isFastDoubleClick()) {
						return;
					}
					listView1.setSelection(0);
					listView2.setSelection(0);
				}
			});
		}

		// 设置listview2列表的scroll监听，用于滑动过程中左右不同步时校正
		listView2.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 如果停止滑动
				if (scrollState == 0 || scrollState == 1) {
					// 获得第一个子view
					View subView = view.getChildAt(0);

					if (subView != null) {
						final int top = subView.getTop();
						final int top1 = listView1.getChildAt(0).getTop();
						final int position = view.getFirstVisiblePosition();

						// 如果两个首个显示的子view高度不等
						if (top != top1) {
							listView1.setSelectionFromTop(position, top);
						}
					}
				}

			}

			public void onScroll(AbsListView view, final int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				View subView = view.getChildAt(0);
				if (subView != null) {
					final int top = subView.getTop();

					// //如果两个首个显示的子view高度不等
					int top1 = listView1.getChildAt(0).getTop();
					if (!(top1 - 7 < top && top < top1 + 7)) {
						listView1.setSelectionFromTop(firstVisibleItem, top);
						listView2.setSelectionFromTop(firstVisibleItem, top);
					}

				}

				if (iv != null) {
					if (firstVisibleItem == 0) {
						iv.setVisibility(View.GONE);
					} else {
						iv.setVisibility(View.VISIBLE);
					}
				}
			}
		});

		// 设置listview1列表的scroll监听，用于滑动过程中左右不同步时校正
		listView1.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == 0 || scrollState == 1) {
					// 获得第一个子view
					View subView = view.getChildAt(0);

					if (subView != null) {
						final int top = subView.getTop();
						final int top1 = listView2.getChildAt(0).getTop();
						final int position = view.getFirstVisiblePosition();

						// 如果两个首个显示的子view高度不等
						if (top != top1) {
							listView1.setSelectionFromTop(position, top);
							listView2.setSelectionFromTop(position, top);
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, final int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				View subView = view.getChildAt(0);
				if (subView != null) {
					final int top = subView.getTop();
					listView1.setSelectionFromTop(firstVisibleItem, top);
					listView2.setSelectionFromTop(firstVisibleItem, top);

				}
			}
		});
	}

	public static String decryptAES(String sSrc, String sKey) throws Exception {
		try {

			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = Base64.decode(sSrc);
			// 先用base64解密
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original, "utf-8");
				return originalString;
			} catch (Exception e) {
				System.out.println(e.toString());
				return null;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
			return null;
		}
	}

	public static void dialPhone(Context context, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
		context.startActivity(intent);
	}

	public static void callPhone(Context context, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneNumber));
		context.startActivity(intent);
	}

	/**
	 * 得到资源color
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static int getResourcesColor(Context context, int resId) {
		return context.getResources().getColor(resId);
	}

	/**
	 * 传递给fragment
	 * 
	 * @param fm
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public static void onActivityResult(FragmentManager fm, int requestCode, int resultCode, Intent data) {
		int index = requestCode >> 16;
		if (index != 0) {
			index--;
			if (fm.getFragments() == null || index < 0 || index >= fm.getFragments().size()) {
				return;
			}
			Fragment frag = fm.getFragments().get(index);
			if (frag == null) {
			} else {
				handleResult(frag, requestCode, resultCode, data);
			}
		}
	};

	/**
	 * 递归传递所有的子fragment
	 * 
	 * @param frag
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	private static void handleResult(android.support.v4.app.Fragment frag, int requestCode, int resultCode, Intent data) {

		frag.onActivityResult(requestCode & 0xffff, resultCode, data);
		List<android.support.v4.app.Fragment> frags = frag.getChildFragmentManager().getFragments();
		if (frags != null) {
			for (android.support.v4.app.Fragment f : frags) {
				if (f != null)
					handleResult(f, requestCode, resultCode, data);
			}
		}
	}

	/**
	 * 得到根Fragment
	 * 
	 * @return
	 */
	public static Fragment getRootFragment(Fragment fragment) {
		Fragment _fragment = fragment;
		while (_fragment != null && _fragment.getParentFragment() != null) {
			_fragment = fragment.getParentFragment();
		}
		return _fragment;
	}

	/**
	 * return true 有更新 false 没有更新
	 * */
	public static boolean VersionJudge(String versionNew, String versionOld) {
		if (strNullMeans(versionNew) || strNullMeans(versionOld)) {
			return false;
		}
		List<Integer> listNew = new ArrayList<Integer>();
		List<Integer> listOld = new ArrayList<Integer>();
		String[] strNew = versionNew.split("\\.");
		String[] strOld = versionOld.split("\\.");

		for (String str : strNew) {
			listNew.add(Integer.parseInt(str));

		}
		for (String str : strOld) {
			listOld.add(Integer.parseInt(str));
		}

		for (int str : listNew) {
			System.out.println(str);

		}
		for (int str : listOld) {
			System.out.println(str);
		}

		int length = listNew.size() >= listOld.size() ? listNew.size() : listOld.size();
		if (listNew.size() < length) {
			for (int i = listNew.size(); i < length; i++) {
				listNew.add(0);
			}
		}
		if (listOld.size() < length) {
			for (int i = listOld.size(); i < length; i++) {
				listOld.add(0);
			}
		}
		/*
		 * for (int i = 0; i < length; i++) {
		 * System.out.println(listNew.get(i) + "|" + listOld.get(i)); }
		 */
		for (int i = 0; i < length; i++) {
			System.out.println(listNew.get(i) + "|" + listOld.get(i));
			if (listNew.get(i).floatValue() > listOld.get(i).floatValue()) {

				return true;
			} else if (listNew.get(i).floatValue() == listOld.get(i).floatValue()) {
				continue;
			} else {
				return false;
			}
		}
		return false;
	}

	public static int timeJudge(String time1, String time2) {
		Utils.Log("time1 = " + time1 + " time2 = " + time2);
		if (strNullMeans(time1) || strNullMeans(time2)) {
			return 0;
		}
		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		String[] str1 = time1.split(" ");
		String[] str2 = time2.split(" ");

		String[] str11 = str1[0].split("-");
		String[] str12 = str1[1].split(":");

		String[] str21 = str2[0].split("-");
		String[] str22 = str2[1].split(":");

		for (String str : str11) {
			list1.add(Integer.parseInt(str));
		}
		for (String str : str12) {
			list1.add(Integer.parseInt(str));
		}
		if (str12.length == 2) {
			list1.add(Integer.parseInt("00"));
		}

		for (String str : str21) {
			list2.add(Integer.parseInt(str));
		}
		for (String str : str22) {
			list2.add(Integer.parseInt(str));
		}
		if (str22.length == 2) {
			list2.add(Integer.parseInt("00"));
		}
		for (int i = 0; i < list1.size(); i++) {
			if ((int) list1.get(i) > (int) list2.get(i)) {
				return 1;
			} else if ((int) list1.get(i) == (int) list2.get(i)) {
				continue;
			} else {
				return -1;
			}
		}
		return 0;
	}

	// float型数据返回String 为整数时去除小数点后的0
	public static String formatData(float data) {
		int temp = (int) data;
		if (temp == data)
			return String.valueOf(temp);
		else
			return String.valueOf(data);
	}

	public static void setTextContent(TextView tv, String content) {
		if (tv != null) {
			tv.setText(strNullMeans(content) ? "" : content);
		}
	}

	public static String encryptAES(String sSrc, String sKey) throws Exception {
		String encodeSttr = "";
		if (!Utils.strNullMeans(sKey)) {
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			// "算法/模式/补码方式"
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
			encodeSttr = Base64.encode(encrypted);
			encodeSttr = encodeSttr.replace("\r", "");
			encodeSttr = encodeSttr.replace("\n", "");
			encodeSttr = encodeSttr.replace("\r\n", "");
			encodeSttr.trim();
		}
		return encodeSttr;
	}

	/**
	 * 判断结果是否合适
	 * 
	 * @param result
	 *            0.00
	 * @param reference
	 *            0.00~0.00 ; 0.00~~0.00; 0.00-0.00
	 * @return 0 1 -1; 0正常， 1高， -1低
	 */
	public static int isFit(String result, String reference) {
		try {
			String[] values = null;
			double content = Double.parseDouble(result);
			if (reference.contains("-")) {
				values = reference.split("-");
			}
			if (reference.contains("~")) {
				values = reference.split("~");
			}
			if (reference.contains("~~")) {
				values = reference.split("~~");
			}
			if (reference.contains("～")) {
				values = reference.split("～");
			}
			if (reference.contains("～～")) {
				values = reference.split("～～");
			}
			if (reference.contains("－")) {
				values = reference.split("－");
			}
			if (values != null) {
				if (values.length > 0) {
					double min = Double.parseDouble(values[0]);
					if (content < min) {
						return -1;
					}
					if (values.length > 1) {
						double max = Double.parseDouble(values[1]);
						if (content > max) {
							return 1;
						}
					}
				}

			}

			return 0;
		} catch (Exception e) {
			return 0;
		}

	}

	public static Dialog loadingDialog;

	public static void ShowLoadingDialog(Context context) {
		if (loadingDialog != null) {
			return;
		}
		loadingDialog = new LoadDialog(context);
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.show();
	}

	public static void CloseLoadingDialog() {
		if (loadingDialog != null && loadingDialog.isShowing()) {
			try {
				loadingDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		loadingDialog = null;
	}

	private static PromptDialog prompDialog;

	public static void ShowPromptDialog(Context context, int type, String strTitle, String strContent, String strBtn) {
		try {
			if (type == 0) {
				ShowLoadingDialog(context);
			} else {
				String temp[] = strContent.split("\\|");
				if (temp.length == 2) {
					if ("1003".equals(temp[0])) {
						return;
					}
				}
				//没有集成云通讯，无法测出异地登录，所以注释掉
//				if ("0001".equals(temp[0]) || "0002".equals(temp[0])) {
//					return;
//				}

				if (prompDialog != null && prompDialog.isShowing()) {
					// 当上次的重新登录dialog未关闭时，如果这次需要显示的也是重新登录时，直接返回
					if (1 == prompDialog.getType() && 1 == type && "0002".equals(temp[0]) && "0002".equals(prompDialog.getStrCode())) {
						return;
					}
					try {
						prompDialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
					prompDialog = null;
					return;
				}
				prompDialog = new PromptDialog(type, context, R.style.MyDialog, strTitle, strContent, strBtn);
				prompDialog.setCanceledOnTouchOutside(false);
				prompDialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void closePromptDialog() {
		CloseLoadingDialog();
		if (prompDialog != null && prompDialog.isShowing()) {
			// 如果当前dialog已弹出重新登录，并且未点击过 取消或者重新登录 就不关闭
			if (!prompDialog.getCanClose() && 1 == prompDialog.getType() && "0002".equals(prompDialog.getStrCode())) {
				return;
			}
			try {
				prompDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			}
			prompDialog = null;
		}

	}

	private static Dialog updateDialog;

	public static void ShowUpdateDialog(Context context, String updateStr, boolean isCancel, UpdateUi uu) {
		if (updateDialog != null) {
			return;
		}
		updateDialog = new VersionUpdateDialog(context, updateStr, isCancel, uu);
		updateDialog.setCanceledOnTouchOutside(false);
		updateDialog.show();
	}

	public static void CloseUpdateDialog() {
		if (updateDialog != null && updateDialog.isShowing()) {
			updateDialog.dismiss();
			updateDialog = null;
		}
	}

	private static Dialog enterDialog;

	/**
	 * 
	 * @param context
	 * @param leftMsg
	 * @param rightMsg
	 * @param leftButton
	 * @param rightButton
	 * @param title
	 * @param contentColor
	 *            内容颜色
	 * @param enterDialogMode
	 * @param updateUi
	 */
	public static void ShowEnterDialog(Context context, String leftMsg, String rightMsg, String leftButton, String rightButton, String title, int enterDialogMode, int contentColor, UpdateUi updateUi) {
		if (enterDialog != null) {
			if (enterDialog.isShowing()) {
				enterDialog.dismiss();
			}
			enterDialog = null;
			return;
		}
		enterDialog = new EnterDialog(context, leftMsg, rightMsg, leftButton, rightButton, title, enterDialogMode, contentColor, updateUi);
		enterDialog.setCanceledOnTouchOutside(false);
		enterDialog.show();
	}

	public static void ShowEnterDialog(Context context, String leftMsg, String rightMsg, String leftButton, String rightButton, String title, int enterDialogMode, UpdateUi updateUi) {
		if (enterDialog != null) {
			if (enterDialog.isShowing()) {
				enterDialog.dismiss();
			}
			enterDialog = null;
			return;
		}
		enterDialog = new EnterDialog(context, leftMsg, rightMsg, leftButton, rightButton, title, enterDialogMode, updateUi);
		enterDialog.setCanceledOnTouchOutside(false);
		enterDialog.show();
	}

	public static void CloseEnterDialog() {
		if (enterDialog != null && enterDialog.isShowing()) {
			enterDialog.dismiss();
			enterDialog = null;
		}
	}

	public static void newBtnNum() {
		if (GlobalInfo.vb != null) {
			GlobalInfo.vb.stopButtonThread();
			GlobalInfo.vb = null;
		}
		GlobalInfo.vb = new ValidateButton();
		GlobalInfo.vb.getRunTime();
	}

	private static Dialog chooseDateDialog;

	public static void ShowChooseDateDialog(int type, Context context, String title, String date, UpdateUi uu) {

		if (chooseDateDialog != null) {
			chooseDateDialog.dismiss();
			chooseDateDialog = null;
		}

		chooseDateDialog = new ChooseDateDialog(type, context, title, date, uu);
		chooseDateDialog.setCanceledOnTouchOutside(false);
		chooseDateDialog.show();

	}

	public static void closeChooseDateDialog() {
		if (chooseDateDialog != null && chooseDateDialog.isShowing()) {
			chooseDateDialog.dismiss();
			chooseDateDialog = null;
		}
	}

	private static Dialog NoteBookDialog;

	public static void ShowNoteBookDialog(Context context, ProductionInspectionManual pimDate, UpdateUi uu) {
		if (NoteBookDialog != null) {
			return;
		}
		NoteBookDialog = new NoteBookDialog(context, pimDate, uu);
		NoteBookDialog.setCanceledOnTouchOutside(false);
		NoteBookDialog.show();
	}

	public static void CloseNoteBookDialog() {
		if (NoteBookDialog != null && NoteBookDialog.isShowing()) {
			NoteBookDialog.dismiss();
			NoteBookDialog = null;
		}
	}

	public static void setIMHeadImage(String url, ImageView imageView) {
		ImageLoader.getInstance().displayImage(url, imageView, Utils.getImageLoadRoundOptions(R.drawable.icon_sidebar1, R.drawable.icon_sidebar1, R.drawable.icon_sidebar1));
	}

	public static DisplayImageOptions getImageLoadRoundOptions(int loadRes, int emptyRes, int errorRes) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(loadRes) // resource
				.showImageForEmptyUri(emptyRes) // resource or
				.showImageOnFail(errorRes) // resource or
				// drawable
				.resetViewBeforeLoading(false) // default
				.delayBeforeLoading(1000).cacheInMemory(true) // default
				.cacheOnDisk(true) // default
				.considerExifParams(false) // default
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
				.bitmapConfig(Bitmap.Config.ARGB_8888) // default
				.displayer(new MyRoundedBitmapDisplayer(360)) // default
				.handler(new Handler()) // default
				.build();
		return options;

	}

	public static DisplayImageOptions getImageLoadRoundOptions(int drawableRes) {
		return getImageLoadRoundOptions(drawableRes, drawableRes, drawableRes);
	}

	public static Dialog show(Context conetext, View view, int gravity, boolean isCancelable) {
		RelativeLayout.LayoutParams parames = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(parames);
		Dialog dialog = new Dialog(conetext, R.style.MyDialog);
		dialog.setContentView(view);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		Window window = dialog.getWindow();
		window.setAttributes(lp);
		window.setGravity(gravity);
		// window.setWindowAnimations(R.style.dialog_style_animation);
		if (!(conetext instanceof Activity)) {
			window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
			window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		}
		dialog.setCancelable(isCancelable);
		dialog.setCanceledOnTouchOutside(isCancelable);
		dialog.show();
		return dialog;
	}
	
	
	/**
	 * @param 按钮是否变亮手机号判断
	 * @return
	 */
	public static boolean showBtnTelJudge(Context context, String tel) {
		if (Utils.strNullMeans(tel)) {
			return false;
		}

		if (!Utils.strLength(tel, 11)) {
			return false;
		}

		String check = "^(1[3,4,5,8,7])\\d{9}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(tel);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * @param 按钮是否变亮密码判断
	 * @return
	 */
	public static boolean showBtnPswJudge(Context context, String psw) {
		if (Utils.strNullMeans(psw)) {
			return false;
		}

		if (!Utils.pswJudge(psw)) {
			return false;
		}
		return true;
	}
	
}
