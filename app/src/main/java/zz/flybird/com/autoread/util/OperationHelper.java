package zz.flybird.com.autoread.util;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * @Author admin
 * Created on 2019/1/15 11:39.
 * Desc:AutoRead
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class OperationHelper {


    public static List<AccessibilityNodeInfo> findViewById(AccessibilityNodeInfo rootNodeInfo, String home_recyclerview_id) {
        List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId = rootNodeInfo.findAccessibilityNodeInfosByViewId(home_recyclerview_id);
        return accessibilityNodeInfosByViewId;
//        if (accessibilityNodeInfosByViewId != null && !accessibilityNodeInfosByViewId.isEmpty()) {
//        }
    }

    /**
     * 遍历新闻列表 阅读
     */
    public static void findViewByIdAndClick(AccessibilityNodeInfo rootNodeInfo, String viewId) {
        List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId = rootNodeInfo.findAccessibilityNodeInfosByViewId(viewId);
        if (accessibilityNodeInfosByViewId != null && !accessibilityNodeInfosByViewId.isEmpty()) {
            for (AccessibilityNodeInfo info : accessibilityNodeInfosByViewId) {
                info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }
}
