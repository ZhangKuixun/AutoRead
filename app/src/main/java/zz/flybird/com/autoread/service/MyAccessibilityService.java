package zz.flybird.com.autoread.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.List;

import zz.flybird.com.autoread.ViewIdConfig;
import zz.flybird.com.autoread.util.ADBUtil;
import zz.flybird.com.autoread.util.LogUtils;
import zz.flybird.com.autoread.util.OperationHelper;
import zz.flybird.com.autoread.util.UIUtil;

/**
 * @Author admin
 * Created on 2019/1/15 10:18.
 * Desc:AutoRead
 */
public class MyAccessibilityService extends AccessibilityService {
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
        LogUtils.e("onAccessibilityEvent eventTypeToString:" + "\neventType:" + s);

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
            if (rootNodeInfo != null) {
                LogUtils.e("rootNodeInfo不为空");
                if (className.contains("com.sohu.quicknews.homeModel")) {
                    //找到新闻列表的recyclerView
                    List<AccessibilityNodeInfo> recyclerviewByIds = OperationHelper.findViewById(rootNodeInfo, ViewIdConfig.home_recyclerview_id);
                    LogUtils.e("rootNodeInfo recyclerView " + recyclerviewByIds.size());
                    if (!recyclerviewByIds.isEmpty()) {
                        AccessibilityNodeInfo accessibilityNodeInfo = recyclerviewByIds.get(0);
                        CharSequence className1 = accessibilityNodeInfo.getClassName();
                        LogUtils.e("recyclerView className1 " + className1);
                        //找到 recyclerView Item 中的文章标题的textView
                        List<AccessibilityNodeInfo> itemTitles = OperationHelper.findViewById(accessibilityNodeInfo, ViewIdConfig.home_recyclerview_item_title);
                        if (!itemTitles.isEmpty()) {
                            LogUtils.e("标题" + itemTitles.get(0).getClassName() + "是否可点击" + itemTitles.get(0).isClickable());
                            // 标题textView clickable为False  获取其parent LinearLayout clickable 为true
                            // 点击进入详情页面 TODO
                            //setItemClickAction(itemTitles.get(0));
                        } else {
                            LogUtils.e("标题列表为空");
                        }
                    }
                } else if (className.contains("com.sohu.quicknews.articleModel.activity.DetailActivity")) {
                    LogUtils.e("新闻详情");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    AccessibilityNodeInfo detailInfo = getRootInActiveWindow();
                    //找到id为 body的LinearLayout
//                    findBodyLayout(detailInfo);
                    //查找webView
                    findWebView(detailInfo);


                    //find CoordinatorLayout
//                    findCoordinatorLayout(detailInfo);


                } else if (className.contains("com.jifen.qukan")) {
                    AccessibilityNodeInfo detailInfo = getRootInActiveWindow();

                    List<AccessibilityNodeInfo> scrollNodes = detailInfo.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/aqz");
                    if (scrollNodes != null && !scrollNodes.isEmpty()) {
                        LogUtils.e("趣头条 详情" + scrollNodes.size());
                    } else {
                        LogUtils.e("趣头条 详情 scrollNode 为空");
                    }

//                    ADBUtil.perforGlobalSwipe(0, 0, 300, 300);
                    //从一个点 滑动到另一个点

                    ScrollScreen(1500);
//                    ADBUtil.perforGlobalSwipe(300, 300, 300, 700);

                }

            } else {
                LogUtils.e("rootNodeInfo为null");

            }
        }
    }

    private void ScrollScreen(long interval) {
        int screenHeight = UIUtil.getScreenHeight(this);
        int screenWidth = UIUtil.getScreenWidth(this);
        LogUtils.e("趣头条 详情 screenHeight" + screenHeight + "screenWidth:" + screenWidth);
//        ADBUtil.perforGlobalSwipe(0, screenHeight, 0, 300);
//        LogUtils.e("趣头条 详情 滑动1");

//        for (; ; ) {

//            ADBUtil.perforGlobalSwipe(0, 500, 0, 0);
//            LogUtils.e("趣头条 详情 滑动2");
//        }

        ADBUtil.perforGlobalSwipe(0, 500, 0, 0);
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ADBUtil.perforGlobalSwipe(0, 500, 0, 0);
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ADBUtil.perforGlobalSwipe(0, 500, 0, 0);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void findWebView(AccessibilityNodeInfo detailInfo) {
        List<AccessibilityNodeInfo> webViews = detailInfo.findAccessibilityNodeInfosByViewId(ViewIdConfig.detail_webview);
        if (webViews != null && !webViews.isEmpty()) {
            LogUtils.e("webViews不为空" + webViews.size());
            AccessibilityNodeInfo webViewInfo = webViews.get(0);
            if (webViewInfo.getChildCount() > 0) {
                AccessibilityNodeInfo childWebViewInfo = webViewInfo.getChild(0);
                LogUtils.e("childWebViewInfo" + childWebViewInfo.getClassName() + childWebViewInfo.isScrollable());
                if (childWebViewInfo.isScrollable()) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    LogUtils.e("执行滑动代码");
                    childWebViewInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    childWebViewInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                }

            }

        } else {
            LogUtils.e("webViews为空");
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

}
