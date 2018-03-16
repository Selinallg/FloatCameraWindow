package rht.com.floatview;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Camera;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


import rht.com.floatview.gl.SurfaceView;

import static rht.com.floatview.MyWindowManager.createCameraWindow;


public class FloatWindowService extends Service {


	private Handler handler = new Handler();
	CameraOperator mCamera;

	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	protected SurfaceView mView;

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.e("FLoat","oncreate");

		mView = MyWindowManager.createCameraWindow(getApplicationContext()).getCameraView();

		initCamera();


	}

	protected void initCamera()
	{
		new Thread (new Runnable(){
			@Override
			public void run(){
				if(mCamera == null) {
					mCamera = new CameraOperator(mView.getHolder(), null);
					mCamera.startPreview();
				}
			}
		}).start();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		if(timer != null)
		{
		timer.cancel();
		}
		timer = null;
		removeAllWindow();
	}

	public void removeAllWindow()
	{
		MyWindowManager.removeCameraWindow(getApplicationContext());
	}
	class RefreshTask extends TimerTask {

		@Override
		public void run() {
			if( !MyWindowManager.isWindowShowing())			{
				handler.post(new Runnable() {
					@Override
					public void run() {
						createCameraWindow(getApplicationContext());
					}
				});
			}


		}

	}

}
