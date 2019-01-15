package zz.flybird.com.autoread.util;

import android.util.Log;

/**
 */
public class LogUtils {
    private LogUtils() {
    }

    private static final String tag = "日志";
    public static boolean isDebug = true;

    /**
     * 截断输出日志
     *
     * @param msg
     */
    public static void logE(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.e(tag, msg);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.e(tag, logContent);
            }
            Log.e(tag, msg);// 打印剩余日志
        }
    }


    public static void e(String tag, String text) {
        if (isDebug) {
            Log.e(tag, text);
        }
    }

    public static void e(String text) {
        if (isDebug) {
            Log.e(tag, text);
        }
    }

    public static void d(String tag, String text) {
        if (isDebug) {
            Log.d(tag, text);
        }
    }

    public static void d(String text) {
        if (isDebug) {
            Log.d(tag, text);
        }
    }

    public static void i(String tag, String text) {
        if (isDebug) {
            Log.i(tag, text);
        }
    }

    public static void i(String text) {
        if (isDebug) {
            Log.i(tag, text);
        }
    }


}
