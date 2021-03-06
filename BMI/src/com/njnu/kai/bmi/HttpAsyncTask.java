/**
 * @(#)HttpAsyncTask.java		2012-10-31
 *
 */
package com.njnu.kai.bmi;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 *
 * @version 1.0.0
 * @since 2012-10-31
 */

interface HttpAsyncTaskNotify {
	public void notifyResult(int id, String result);
}

public class HttpAsyncTask extends AsyncTask<Object, Void, String> {
	private static final String LOG_TAG = "HttpAsyncTask";
	public static final String TEST_URL_BLUE = "http://wap.baidu.com";
	public static final String TEST_URL_YELLOW = "http://picdown.ttpod.cn/picsearch?artist=SHE";
	public static final String TEST_URL_PINK = "http://lrc.ttpod.com/search?artist=SHE";
	public static final String TEST_URL_GRAY = "http://v1.ard.h.itlily.com/plaza/newest/50";
	public static final String TEST_URL_GREEN = "http://v1.ard.q.itlily.com/share/celebrities";
	private HttpAsyncTaskNotify mNotify;
	private int mId;
	private Context mContext;


	public HttpAsyncTask(Context context, HttpAsyncTaskNotify notify) {
		super();
		mContext = context;
		mNotify = notify;
	}

	@Override
	protected String doInBackground(Object... params) {
		String result = "empty";
		mId = (Integer)params[0];
		switch (mId) {
		case R.id.iv_color_blue:
//			result = HttpUtility.WriteToFile(TEST_URL_BLUE, "/sdcard/downTestBlue.file");
            result = HttpUtility.GetUseAutoEncoding(TEST_URL_BLUE);
//			result = HttpConnectionUtility.WriteToFile(mContext, TEST_URL_BLUE, "/sdcard/downTestBlue.file");
			break;

		case R.id.iv_color_yellow:
//			result = HttpUtility.WriteToFile(TEST_URL_YELLOW, "/sdcard/downTestYellow.file");
            result = HttpUtility.GetUseAutoEncoding(TEST_URL_YELLOW);
//			result = HttpConnectionUtility.WriteToFile(mContext, TEST_URL_YELLOW, "/sdcard/downTestYellow.file");
			break;

		case R.id.iv_color_pink:
//			result = HttpUtility.WriteToFile(TEST_URL_PINK, "/sdcard/downTestPink.file");
            result = HttpUtility.GetUseAutoEncoding(TEST_URL_PINK);
//			result = HttpConnectionUtility.WriteToFile(mContext, TEST_URL_PINK, "/sdcard/downTestPink.file");
			break;

		case R.id.iv_color_gray:
//			result = HttpUtility.WriteToFile(TEST_URL_GRAY, "/sdcard/downTestGray.file");
//			result = HttpConnectionUtility.WriteToFile(mContext, TEST_URL_GRAY, "/sdcard/downTestGray.file");
            result = HttpUtility.GetUseAutoEncoding(TEST_URL_GRAY);
			break;
//			result = HttpSocketByGet.GetUseAutoEncoding(mContext, HttpUtility.TTLRCMIME);
//			break;

		case R.id.iv_color_green:
            result = HttpUtility.GetUseAutoEncoding(TEST_URL_GREEN);
//			result = HttpUtility.WriteToFile(TEST_URL_GREEN, "/sdcard/downTestGreen.file");
//			result = HttpConnectionUtility.WriteToFile(mContext, TEST_URL_GREEN, "/sdcard/downTestGreen.file");
			break;
//			result = HttpSocketByConnect.GetUseAutoEncoding(mContext, HttpUtility.TTLRCMIME);
//			break;

		case 1:
			result = new SendEmail2().sendEmail((String)params[1]);
			break;

		case 2:
//			testPictureDecord();
			break;

		default:
			break;
		}
		return result;
	}

//	private void testPictureDecord() {
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(result, options);
//		boolean check = (options.outMimeType != null);
//	}

	@Override
	protected void onPostExecute(String result) {
		mNotify.notifyResult(mId, result);
	}

}
