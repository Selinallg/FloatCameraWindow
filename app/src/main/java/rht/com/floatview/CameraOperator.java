package rht.com.floatview;

/**
 * Created by rht on 2018/1/22.
 */

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * Created by rht on 2018/1/22.
 */

class CameraOperator {
    Camera mCamera=null;
    int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    final String TAG = MyEmdModule.TAG;
    private Handler mMsgHandler;

    public CameraOperator(SurfaceHolder surfaceHolder, Handler msgHanlder)
    {
       ctreateCamera(surfaceHolder);
       mMsgHandler = msgHanlder;
    }

//    public void takePicture()
//    {
//        if(mCamera == null)
//        {
//            mCamera = getCameraInstance();
//        }
//
//        if(mCamera != null) {
//            mCamera.startPreview();
//            mCamera.takePicture(null, null, mPicture);
//        }
//        else
//        {
//            Log.e(TAG,MyEmdModule.LogHead()+"Camera is null");
//            mMsgHandler.obtainMessage(Messages.TAKE_PHOTO_FAILED,"Camera is null");
//        }
//    }

    private Camera getCameraInstance(SurfaceHolder holder) {
        if (mCamera == null) {
            try {
                mCamera = Camera.open(mCameraId);
                if(holder == null) {
                    SurfaceTexture st = new SurfaceTexture(0);
                    mCamera.setPreviewTexture(st);
                }else
                {
                mCamera.setPreviewDisplay(holder);
                }
            } catch (Exception e) {
                Log.e(TAG,e.getMessage());
                Log.d(TAG, MyEmdModule.LogHead()+"camera is not available");
            }
        }
        return mCamera;
    }
    private  int sync;


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = SaveFile.getOutputMediaFile(SaveFile.MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.e(TAG, MyEmdModule.LogHead()+"Error creating media file, check storage permissions: " );
                mMsgHandler.obtainMessage(Messages.TAKE_PHOTO_FAILED,"creating media file failed").sendToTarget();
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                Log.d(TAG,MyEmdModule.LogHead()+pictureFile.toString());
                fos.write(data);
                fos.close();
                mMsgHandler.obtainMessage(Messages.TAKE_PHOTO_OK,pictureFile.toString()).sendToTarget();

            } catch (FileNotFoundException e) {
                Log.e(TAG, MyEmdModule.LogHead()+"File not found: " + e.getMessage());
                mMsgHandler.obtainMessage(Messages.TAKE_PHOTO_FAILED,e.getMessage()).sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, MyEmdModule.LogHead()+"Error accessing file: " + e.getMessage());
                mMsgHandler.obtainMessage(Messages.TAKE_PHOTO_FAILED,e.getMessage()).sendToTarget();
            }
        }
    };

    private boolean ctreateCamera(SurfaceHolder surfaceHolder) {
        try {
            mCamera = getCameraInstance(surfaceHolder) ;
//            mCamera = Camera.open(mCameraId);
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();// 获取支持保存图片的尺寸
            Camera.Size pictureSize = supportedPictureSizes.get(0);// 从List取出Size
            Log.d(TAG,MyEmdModule.LogHead()+ pictureSize.width+"x"+pictureSize.height);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);//

            mCamera.setParameters(parameters);
            mCamera.autoFocus(null);
            return true;

        } catch (Exception e) {

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            destroyCamera();
            e.printStackTrace();
            Log.e(TAG,MyEmdModule.LogHead()+e.toString());

            return false;
        }
    }
    /**
     * 销毁Camera
     */
    protected synchronized void destroyCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            try {
                mCamera.release();
            } catch (Exception e) {

            }
            mCamera = null;
        }
    }

    public void startPreview(){
        if(mCamera != null)
        {
            mCamera.startPreview();
        }
        else
        {
            Log.e(TAG,"camera is null");
        }
    }
}
