package rht.com.floatview;

/**
 * Created by rht on 2018/3/13.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;

public class TopWindowService extends Service
{
    public static final String OPERATION = "operation";
    public static final int OPERATION_SHOW = 100;
    public static final int OPERATION_HIDE = 101;

    private static final int HANDLE_CHECK_ACTIVITY = 200;

    private boolean isAdded = false; // 是否已增加悬浮窗
    private static WindowManager wm;
    private static WindowManager.LayoutParams params;
    private Button btn_floatView;



    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
//        android.os.Debug.waitForDebugger();
        super.onCreate();
        Log.e("FLoat","oncreate");

        createFloatView();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    private void createFloatView()
    {

        btn_floatView = new Button(getApplicationContext());
        btn_floatView.setText("Hello");
        wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //in different sdk this type should change
        params.format = PixelFormat.RGBA_8888; //
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // 设置悬浮窗的长得宽
        params.width = 200;
        params.height = 100;

        // 设置悬浮窗的Touch监听
        btn_floatView.setOnTouchListener(new OnTouchListener()
        {
            int lastX, lastY;
            int paramX, paramY;

            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // 更新悬浮窗位置
                        wm.updateViewLayout(btn_floatView, params);
                        break;
                }
                return true;
            }
        });

        wm.addView(btn_floatView, params);
        isAdded = true;
    }




}
