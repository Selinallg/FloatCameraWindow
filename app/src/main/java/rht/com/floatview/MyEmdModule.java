package rht.com.floatview;

/**
 * Created by rht on 2018/1/25.
 */

public class MyEmdModule {
    public final static String TAG = "EmdDeviceCamera";


    /** Get the current line number.
     * @return int - Current line number.
     */
    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[3].getLineNumber();
    }

    public final static boolean DEBUG = true;
    public static String LogHead() {
        if (DEBUG) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            return  ("["+className + "." + methodName + "():" + lineNumber+"]") ;
        }
        return "";
    }

}
