package rht.com.floatview;


import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;



import rht.com.floatview.FloatWindow.FloatWindowCamera;


public class MyWindowManager {

	private static FloatWindowCamera mCameraWindow;
	private static LayoutParams mCameraWindowParams;
	private static WindowManager mWindowManager;


	public static FloatWindowCamera createCameraWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (mCameraWindow == null) {
			mCameraWindow = new FloatWindowCamera(context);
			if (mCameraWindowParams == null) {
				mCameraWindowParams = new LayoutParams();
				mCameraWindowParams.type = LayoutParams.TYPE_TOAST ;
				mCameraWindowParams.format = PixelFormat.RGBA_8888;
				mCameraWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				mCameraWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				mCameraWindowParams.width = FloatWindowCamera.viewWidth;
				mCameraWindowParams.height = FloatWindowCamera.viewHeight;
				mCameraWindowParams.x = screenWidth;
				mCameraWindowParams.y = screenHeight / 2;
			}
			mCameraWindow.setParams(mCameraWindowParams);
			windowManager.addView(mCameraWindow, mCameraWindowParams);
			Log.e("FLOAT","create small success");
		}
		return mCameraWindow;
	}


	public static void removeCameraWindow(Context context){
		if(mCameraWindow != null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(mCameraWindow);
			mCameraWindow = null;
		}
	}



	public static boolean isWindowShowing() {
		return  mCameraWindow != null;
	}


	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}




	public static String getUsedPercentValue(Context context) {

		return "Hello";
	}



}
