package com.gucheng.accessibilityclick;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by lenovo on 2017/5/22.
 */

public class MyAccessibilityService extends AccessibilityService {
    private static String TAG = "suolong";
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "event is " + event.getAction());
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText("查看红包");
            if (nodeInfos != null) {
                for (int i = 0; i < nodeInfos.size(); i++) {
                    nodeInfos.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }

        }


    }
    @Override
    public void onInterrupt() {

    }
}
