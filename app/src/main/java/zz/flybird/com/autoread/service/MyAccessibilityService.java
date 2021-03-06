package zz.flybird.com.autoread.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

import zz.flybird.com.autoread.ViewIdConfig;
import zz.flybird.com.autoread.util.LogUtils;
import zz.flybird.com.autoread.util.OperationHelper;
import zz.flybird.com.autoread.util.UIUtil;

/**
 * @Author admin
 * Created on 2019/1/15 10:18.
 * Desc:AutoRead
 */
public class MyAccessibilityService extends AccessibilityService {
    private List<String> sohuNewsHistory = new ArrayList<>();
    private List<String> ttddHistory = new ArrayList<>();
    private List<String> newsList = new ArrayList<>();

    @Override
    protected void onServiceConnected() {
        LogUtils.e("onServiceConnected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String className = event.getClassName().toString();
        String s = AccessibilityEvent.eventTypeToString(eventType);
        LogUtils.e("onAccessibilityEvent className:" + className);
        LogUtils.e("onAccessibilityEvent eventTypeToString:" + "\neventType:" + s);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            AccessibilityNodeInfo rootNodeInfo = getRootInActiveWindow();
            if (rootNodeInfo != null) {
                LogUtils.e("rootNodeInfo不为空");
                if (className.contains("com.sohu.quicknews.homeModel.activity.HomeActivity")) {
                    //搜狐新闻首页列表
                    LogUtils.e("搜狐新闻首页列表");
                    //找到新闻列表的recyclerView
                    List<AccessibilityNodeInfo> recyclerviewByIds = OperationHelper.findViewById(rootNodeInfo, ViewIdConfig.home_recyclerview_id);
//                    LogUtils.e("rootNodeInfo recyclerView " + recyclerviewByIds.size());
                    if (!recyclerviewByIds.isEmpty()) {
                        AccessibilityNodeInfo recyclerView = recyclerviewByIds.get(0);
//                        LogUtils.e("recyclerView className1 " + recyclerView.getClassName());
                        //找到 recyclerView Item 中的文章标题的textView
                        List<AccessibilityNodeInfo> itemText = OperationHelper.findViewById(recyclerView, ViewIdConfig.home_recyclerview_item_title);
                        if (!itemText.isEmpty()) {
//                            LogUtils.e("标题" + itemText.get(0).getClassName() + "是否可点击" + itemText.get(0).isClickable());
                            // 标题textView clickable为False  获取其parent LinearLayout clickable 为true
                            //遍历列表  点击进入
//                            setItemClickAction(itemTitles.get(0));
                            LogUtils.e("recyclerView Item Count " + itemText.size());
                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < itemText.size(); i++) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (!sohuNewsHistory.contains(itemText.get(i).getText().toString())) {
                                    LogUtils.e("添加阅读历史 文章标题:" + itemText.get(i).getText().toString());
                                    sohuNewsHistory.add(itemText.get(i).getText().toString());
                                    setItemClickAction(itemText.get(i));
                                    break;
                                } else {
                                    LogUtils.e("已阅读过" + "position" + i);
                                    if (i > 1) {
                                        recyclerView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                        setItemClickAction(itemText.get(i));
                                    }
//                                    if (i == itemText.size()) {
//                                        LogUtils.e("已到最后一条");
//                                        recyclerView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//                                        try {
//                                            Thread.sleep(1000);
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
//                                        setItemClickAction(itemText.get(i));
//                                    }
                                }
                                //到最后一个

                                LogUtils.e(itemText.get(i).getText().toString() + "'");
                            }

                        } else {
                            LogUtils.e("标题列表为空");
                        }
                    }
                } else if (className.contains("com.sohu.quicknews.articleModel.activity.DetailActivity")) {
                    LogUtils.e("搜狐资讯 详情");
                    ScrollScreen(2000, 20 * 1000);

                } else if (className.contains("com.jifen.qukan")) {

                    AccessibilityNodeInfo detailInfo = getRootInActiveWindow();

                    List<AccessibilityNodeInfo> scrollNodes = detailInfo.findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/aqz");
                    if (scrollNodes != null && !scrollNodes.isEmpty()) {
                        LogUtils.e("趣头条 详情" + scrollNodes.size());
                    } else {
                        LogUtils.e("趣头条 详情 scrollNode 为空");
                    }

                } else if (className.contains("com.songheng.eastfirst.business.newsdetail.view.activity.NewsDetailH5Activity")) {
                    LogUtils.e("东方头条 详情");

//                    ScrollScreen(200, 2 * 60 * 1000);
                } else if (className.contains(ViewIdConfig.htt_home_class_name)) {
                    LogUtils.e("惠头条 新闻列表");

                } else if (className.contains(ViewIdConfig.htt_detail_class_name)) {
                    LogUtils.e("惠头条 新闻详情");
                    ScrollScreen(2000, 20 * 1000);

                } else if (className.contains(ViewIdConfig.ttdd_news_list)) {
                    LogUtils.e("头条多多 新闻列表");
                    List<AccessibilityNodeInfo> recyclerViews = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.lite.infoflow.browser:id/pp");
                    LogUtils.e("头条多多 新闻列表 recyclerViews " + recyclerViews.size());
                    if (!recyclerViews.isEmpty()) {
                        for (int i = 0; i < recyclerViews.size(); i++) {
                            AccessibilityNodeInfo recyclerView = recyclerViews.get(i);
                            if (recyclerView == null)
                                continue;
                            if (recyclerView.getChildCount() < 2)
                                continue;
                            List<AccessibilityNodeInfo> linearLayout = recyclerView.findAccessibilityNodeInfosByViewId("com.lite.infoflow.browser:id/va");
                            LogUtils.e("头条多多 新闻列表 frameLayouts " + linearLayout.size());
                            List<AccessibilityNodeInfo> titleTexts = recyclerView.findAccessibilityNodeInfosByViewId("com.lite.infoflow.browser:id/vc");
                            LogUtils.e("头条多多 新闻列表 titleTexts " + titleTexts.size());
                            if (!titleTexts.isEmpty()) {
                                for (int j = 0; j < titleTexts.size(); j++) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (!ttddHistory.contains(titleTexts.get(j).getText().toString())) {
                                        LogUtils.e("添加 惠头条阅读历史 文章标题:" + titleTexts.get(j).getText().toString());
                                        ttddHistory.add(titleTexts.get(j).getText().toString());
                                        setItemClickAction(titleTexts.get(j));
                                        break;
                                    } else {
                                        LogUtils.e("已阅读过" + "position" + j);
                                        if (j > 1) {
                                            recyclerView.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                            setItemClickAction(titleTexts.get(j));
                                        }
                                    }
                                    //到最后一个
                                    LogUtils.e(titleTexts.get(j).getText().toString() + "'");
                                }

                            }
                        }
                    }

                } else if (className.contains(ViewIdConfig.ttdd_news_detail)) {
                    LogUtils.e("头条多多 新闻详情");

                } else {
                    LogUtils.e("className" + className);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                }

            } else {
                LogUtils.e("rootNodeInfo为null");
            }
        }
    }

    private void ScrollScreen(long interval, long duration) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long stayTime = 0;
        while (stayTime < duration) {
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            slideVertical(15, 10);
            stayTime = interval + stayTime;
        }
        LogUtils.e("关闭详情页");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }


    @Override
    public void onInterrupt() {
        LogUtils.e("onInterrupt");
    }


    @TargetApi(Build.VERSION_CODES.N)
    private void setItemClickAction(AccessibilityNodeInfo info) {
        if (info == null) {
            LogUtils.e("设置点击 info为空");
//            clickScreen(10, 10);
            return;
        }
        LogUtils.e("设置点击" + info.getClassName() + "IsClick" + info.isClickable());
        if (info.isClickable() && info.getClassName().equals("android.widget.LinearLayout")) {
            LogUtils.e("点击进入详情页");
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            LogUtils.e(info.getClassName() + "不可点击,查找父布局");
            AccessibilityNodeInfo parentInfo = info.getParent();
            setItemClickAction(parentInfo);
        }
    }


    /**
     * 滑动
     * 滑动比例 0~20
     */
    private void slideVertical(int startSlideRatio, int stopSlideRatio) {
        int screenHeight = UIUtil.getScreenHeight(getApplicationContext());
        int screenWidth = UIUtil.getScreenWidth(getApplicationContext());
        LogUtils.e("屏幕：" + (screenHeight - (screenHeight / 10)) + "/" +
                (screenHeight - (screenHeight - (screenHeight / 10))) + "/" + screenWidth / 2);
        Path path = new Path();
        int start = (screenHeight / 20) * startSlideRatio;
        int stop = (screenHeight / 20) * stopSlideRatio;
        path.moveTo(screenWidth / 2, start);//如果只是设置moveTo就是点击
        path.lineTo(screenWidth / 2, stop);//如果设置这句就是滑动
//        path.moveTo(30, 400);
//        path.lineTo(30, 100);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            GestureDescription.Builder builder = new GestureDescription.Builder();
            GestureDescription gestureDescription = builder
                    .addStroke(new GestureDescription.
                            StrokeDescription(path,
                            200,
                            200))
                    .build();
            dispatchGesture(gestureDescription, new GestureResultCallback() {
                @Override
                public void onCompleted(GestureDescription gestureDescription) {
                    super.onCompleted(gestureDescription);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        LogUtils.e("滑动结束" + gestureDescription.getStrokeCount());
                    }
                }

                @Override
                public void onCancelled(GestureDescription gestureDescription) {
                    super.onCancelled(gestureDescription);
                    LogUtils.e("滑动取消");
//                performGlobalAction(GESTURE_SWIPE_UP);
//                performGlobalAction(GESTURE_SWIPE_UP);
                }
            }, null);
        }
    }


    /**
     * 点击屏幕
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void clickScreen(int xRatio, int yRatio) {
        int screenHeight = UIUtil.getScreenHeight(getApplicationContext());
        int screenWidth = UIUtil.getScreenWidth(getApplicationContext());
        int y = (screenHeight / 20) * yRatio;
        int x = (screenWidth / 20) * xRatio;
        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder
                .addStroke(new GestureDescription.
                        StrokeDescription(path,
                        100,
                        50))
                .build();

        dispatchGesture(gestureDescription, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                LogUtils.e("点击结束" + gestureDescription.getStrokeCount());
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                LogUtils.e("点击取消");
            }
        }, null);
    }
}
