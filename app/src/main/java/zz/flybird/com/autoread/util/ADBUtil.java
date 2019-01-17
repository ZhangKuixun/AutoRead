package zz.flybird.com.autoread.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Looper;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class ADBUtil {

    /**
     * 点击某个视图
     */
    public static void perforGlobalClick(AccessibilityNodeInfo info) {
        Rect rect = new Rect();
        info.getBoundsInScreen(rect);
        perforGlobalClick(rect.centerX(), rect.centerY());
    }

    public static void perforGlobalClick(int x, int y) {
        execShellCmd("input tap " + x + " " + y);
    }

    /**
     * 执行shell命令
     *
     * @param cmd
     */
    public static void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
//            process.waitFor();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    private static android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());

    /**
     * 全局滑动操作
     *
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     */
    public static void perforGlobalSwipe(int x0, int y0, int x1, int y1) {
        execShellCmd("input swipe " + x0 + " " + y0 + " " + x1 + " " + y1);
    }


    /**
     * 当要点击的View可能在屏幕外时
     *
     * @param info
     * @param context
     */
    public static void tryGlobalClickMaybeViewOutsideScreen(final AccessibilityNodeInfo info, final Context context, final Runnable afterScroll, final Runnable sucess) {
        Rect rect = new Rect();
        info.getBoundsInScreen(rect);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        LogUtils.d("info rect==>" + rect);
        LogUtils.d("window dm -->" + dm);
        long delay = 3000;
        if (rect.top < 0) {
            LogUtils.d("scroll down ↓↓↓↓");
            //下滑半屏
            perforGlobalSwipe(dm.widthPixels / 2, dm.heightPixels / 4, dm.widthPixels / 2, (int) (dm.heightPixels * 0.75));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    afterScroll.run();
                }
            }, delay);
        } else if (rect.bottom > dm.heightPixels) {
            LogUtils.d("scroll up ↑↑↑↑");
            //上滑半屏
            perforGlobalSwipe(dm.widthPixels / 2, (int) (dm.heightPixels * 0.75), dm.widthPixels / 2, dm.heightPixels / 4);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    afterScroll.run();
                }
            }, delay);
        } else {
            //
            LogUtils.d("scroll and find the clickable view in screen");

            execShellCmd("input tap " + rect.centerX() + " " + rect.centerY());
            handler.postDelayed(sucess, 2000);
        }

    }

    /**
     * 发送全局 Home键 事件
     *
     * @param delay 延迟时间
     */
    public static void perforGlobalHome(long delay) {
        if (delay <= 0) {
            execShellCmd("input keyevent " + KeyEvent.KEYCODE_HOME);
        } else
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    execShellCmd("input keyevent " + KeyEvent.KEYCODE_HOME);
                }
            }, delay);
    }


    /**
     * 发送全局 返回键 事件
     *
     * @param delay 延迟时间
     */
    public static void perforGlobalBack(long delay) {
        if (delay <= 0) {
            execShellCmd("input keyevent " + KeyEvent.KEYCODE_BACK);
        } else
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    execShellCmd("input keyevent " + KeyEvent.KEYCODE_BACK);
                }
            }, delay);
    }

    /**
     * 发送一段文字，该功能经实验，不好用
     */
    public static void sendString(String text) {
        StringBuilder sb = new StringBuilder();
        String[] split = text.split(" ");
        for (int i = 0; i < split.length; i++) {
            sb.append("input text " + split[i]).append("\n");
            sb.append("input keyevent " + KeyEvent.KEYCODE_SPACE).append("\n");
        }
        execShellCmd(sb.toString());
    }

    //自动为edittext粘贴上文字内容
    public static void sendTextForEditText(Context context, AccessibilityNodeInfo edittext, String text) {
        if (edittext != null) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("text", text);
            clipboard.setPrimaryClip(clip);
            //焦点（n是AccessibilityNodeInfo对象）
            edittext.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            ////粘贴进入内容
            edittext.performAction(AccessibilityNodeInfo.ACTION_PASTE);
            //发送
            //...
        }
    }

    /**
     * 点亮亮屏,点亮屏幕要求很高,不能有手势锁,密码锁,指纹锁,还不能有屏保
     */
    public static void unlock(Context context) {
        PowerManager mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        // 点亮亮屏
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock mWakeLock = mPowerManager.newWakeLock
                (PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "Tag");
        LogUtils.d("px", "mWakeLock is lock:" + mWakeLock.isHeld());
        mWakeLock.acquire();

    }

}
