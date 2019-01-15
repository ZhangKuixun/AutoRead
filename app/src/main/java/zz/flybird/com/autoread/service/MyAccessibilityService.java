package zz.flybird.com.autoread.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import zz.flybird.com.autoread.ViewIdConfig;
import zz.flybird.com.autoread.util.BaseAccessibilityService;
import zz.flybird.com.autoread.util.LogUtils;
import zz.flybird.com.autoread.util.OperationHelper;

/**
 * @Author admin
 * Created on 2019/1/15 10:18.
 * Desc:AutoRead
 */
public class MyAccessibilityService extends BaseAccessibilityService {
    @Override
    protected void onServiceConnected() {
        LogUtils.e("onServiceConnected");
    }


    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String className = event.getClassName().toString();
        String s = AccessibilityEvent.eventTypeToString(eventType);
        LogUtils.e("onAccessibilityEvent className:" + className);
//        LogUtils.e("onAccessibilityEvent eventTypeToString:" + "\neventType:" + s);
        AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (rootNodeInfo != null) {
                LogUtils.e("rootNodeInfo不为空");
                if (className.contains("com.sohu.quicknews.homeModel")) {
                    List<AccessibilityNodeInfo> recyclerviewByIds = OperationHelper.findViewById(rootNodeInfo, ViewIdConfig.home_recyclerview_id);
                    LogUtils.e("rootNodeInfo recyclerView " + recyclerviewByIds.size());
                    if (recyclerviewByIds != null && !recyclerviewByIds.isEmpty()) {
                        AccessibilityNodeInfo accessibilityNodeInfo = recyclerviewByIds.get(0);
                        CharSequence className1 = accessibilityNodeInfo.getClassName();
                        LogUtils.e("recyclerView className1 " + className1);
                        List<AccessibilityNodeInfo> itemTitles = OperationHelper.findViewById(accessibilityNodeInfo, ViewIdConfig.home_recyclerview_item_title);
                        if (!itemTitles.isEmpty()) {

                            LogUtils.e("标题列表不为空" + itemTitles.get(0).getClassName());

//                            itemTitles.get(0).getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            LogUtils.e("点击标题" + itemTitles.get(0).getText().toString());
//                            LogUtils.e("点击标题" + itemTitles.get(0).isClickable());
//                            AccessibilityNodeInfo parent = itemTitles.get(0).getParent();
//                            LogUtils.e("点击" + "类名" + parent.getClassName() + "是否可点击" + parent.isClickable());
//                            if (!parent.isClickable()) {
//                                AccessibilityNodeInfo superParent = parent.getParent();
//                                LogUtils.e(superParent.isClickable() + "" + superParent.getClassName());
//                            } else {
//                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                            }

//                            for (AccessibilityNodeInfo itemInfo : itemTitles) {
//                                setItemClickAction(itemInfo);
//                            }

                            setItemClickAction(itemTitles.get(0));
                        } else {
                            LogUtils.e("标题列表为空");
                        }

//                        accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);

                    }
                } else if (className.contains("com.sohu.quicknews.articleModel.activity.DetailActivity")) {
                    LogUtils.e("新闻详情");
                    List<AccessibilityNodeInfo> coordinatorLayouts = OperationHelper.findViewById(rootNodeInfo, ViewIdConfig.detail_coordinatorLayout);
                    if (coordinatorLayouts != null && !coordinatorLayouts.isEmpty()) {
                        LogUtils.e("详情" + coordinatorLayouts.size());
                        LogUtils.e("详情" + coordinatorLayouts.get(0).getClassName() + "是否可滚动" + coordinatorLayouts.get(0).isScrollable());

                        if (coordinatorLayouts.get(0).isScrollable()) {
                            coordinatorLayouts.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            coordinatorLayouts.get(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                        } else {
                            AccessibilityNodeInfo parent = coordinatorLayouts.get(0).getParent();
                            LogUtils.e("详情" + parent.getClassName() + "是否可滚动" + parent.isScrollable());
                        }

                    }
                }

            } else {
                LogUtils.e("rootNodeInfo为null");

            }
        }
    }


    @Override
    public void onInterrupt() {
        LogUtils.e("onInterrupt");
    }


    private void setItemClickAction(AccessibilityNodeInfo info) {
        LogUtils.e("设置点击" + info.getClassName() + "IsClick" + info.isClickable());
        if (info.isClickable() && info.getClassName().equals("android.widget.LinearLayout")) {
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            LogUtils.e(info.getClassName() + "不可点击,查找父布局");
            AccessibilityNodeInfo parentInfo = info.getParent();
            setItemClickAction(parentInfo);
        }
    }

    private void chooseScrollbleView(AccessibilityNodeInfo info) {

    }
}
