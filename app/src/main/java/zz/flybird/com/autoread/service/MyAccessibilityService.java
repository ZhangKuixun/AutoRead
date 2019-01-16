package zz.flybird.com.autoread.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import zz.flybird.com.autoread.ViewIdConfig;
import zz.flybird.com.autoread.util.LogUtils;
import zz.flybird.com.autoread.util.OperationHelper;

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
//        LogUtils.e("onAccessibilityEvent eventTypeToString:" + "\neventType:" + s);

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
                    List<AccessibilityNodeInfo> bodyNodeInfos = OperationHelper.findViewById(detailInfo, ViewIdConfig.detail_body);
                    if (bodyNodeInfos != null && !bodyNodeInfos.isEmpty()) {
                        LogUtils.e("详情" + bodyNodeInfos.size());
                        LogUtils.e("详情" + bodyNodeInfos.get(0).getClassName() + "是否可滚动" + bodyNodeInfos.get(0).isScrollable());
                        int childCount = bodyNodeInfos.get(0).getChildCount();
                        if (childCount <= 0) {
                            return;
                        }
                        //找到body下的可滚动的webView  即scrollable=true
                        for (int i = 0; i < childCount; i++) {
                            LogUtils.e("详情body" + bodyNodeInfos.get(0).getChild(i).getClassName() + "&&&" + bodyNodeInfos.get(0).getChild(i).isScrollable());
                            //找到外层的webView
                            if (bodyNodeInfos.get(0).getChild(i).getClassName().toString().contains("android.webkit.WebView")) {
                                LogUtils.e("外层是否可滑动" + bodyNodeInfos.get(0).getChild(i).getClassName() + "@@@" + bodyNodeInfos.get(0).getChild(i).isScrollable());
                                if (bodyNodeInfos.get(0).getChild(i).getChildCount() > 0) {
                                    //找到内层的webView
                                    CharSequence className1 = bodyNodeInfos.get(0).getChild(i).getChild(0).getClassName();
                                    boolean scrollable = bodyNodeInfos.get(0).getChild(i).getChild(0).isScrollable();
                                    LogUtils.e("内层" + className1 + ">>>" + scrollable);
                                    if (scrollable) {
                                        LogUtils.e("开始滑动");
                                        bodyNodeInfos.get(0).getChild(i).getChild(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        bodyNodeInfos.get(0).getChild(i).getChild(0).performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                    }

                                } else {
                                    LogUtils.e("未找到内层webview");
                                }

                                break;
                            }
                        }

                    } else {
                        LogUtils.e("body 为空");
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
        if (info.isScrollable()) {
            LogUtils.e("可以滑动:" + info.getClassName());
        } else {
            LogUtils.e("不可滑动:" + info.getClassName());
            if (info.getParent() != null) {
                AccessibilityNodeInfo parent = info.getParent();
                chooseScrollbleView(parent);
            }
        }

    }
}
