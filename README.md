# redpacket_AccessibilityClick
模拟微信抢红包插件
&nbsp; &nbsp; &nbsp;&nbsp;逢年过节大家都少不了发微信红包，通过微信红包来表达祝福。同时，微信还有拼手气群红包。各种群好友群，亲戚群，工作群逢年过节常常会有红包可抢。抢红包的口诀是：“网速要好，手速要快”。抢到红包固然欣喜，抢不到红包的失落和遗憾的感觉却让人非常不爽。有时等红包等了很久，明明看到了一点开就被抢完。还有人等了很久，刚花了几分钟时间去做其他事，就有人发红包。损失了一个亿啊有木有……自从有了微信红包插件，再也不用担心抢不到红包了。就是单独发给我的红包，我也不用自己点开，可以自动拆红包，一下子就解放了双手。这里就不得不提到Android系统的辅助功能，又叫无障碍功能。
	&nbsp; &nbsp; &nbsp;&nbsp;Google为了让更多的用户可以使用Android系统，也为了可以帮助身体上有障碍的人士，给Android系统开发了辅助功能。辅助功能可以做很多事情，比如读出屏幕上的内容，模拟真实的用户点击等。
&nbsp; &nbsp; &nbsp;&nbsp;下面我们就来模拟实现一下抢红包功能，这里实现两个应用程序，一个用来模拟微信红包，另一个模拟红包插件。
&nbsp; &nbsp; &nbsp;&nbsp;先来看一下模拟红包的这个应用。新建一个工程，命名为RedPacket。RedPacket里定义了一个Button，默认文本为“查看红包”。当点击一次会显示未“200元”，再次点击又重新变为“查看红包”。 布局文件activity_main.xml如下：

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="com.gucheng.redpacket.MainActivity">

 <Button
     android:layout_height="wrap_content"
     android:layout_width="wrap_content"
     android:id= "@+id/hongbao"
     android:text="查看红包"
     android:background="#c8ff0000"
     />
</RelativeLayout>
```

&nbsp; &nbsp; &nbsp;&nbsp;在文件MainActivity.java文件中，给按钮添加响应事件。点击一次文本变为“200元”；再点击又变回“查看红包”。这样我们的模拟微信红包的软件就写好了。
&nbsp; &nbsp; &nbsp;&nbsp;接下来，我们再来写一个应用模拟微信抢红包插件。新建一个工程，命名为AccessibilityClick（辅助点击）。使用Android的辅助功能来实现自动点击操作。这里要用到Android的一个类AccessibilityService。

**实现一个类继承自AccessibilityService**

```
public class MyAccessibilityService extends AccessibilityService
```

**在AndroidManifest.xml文件中注册**

```
 <service android:name=".MyAccessibilityService"
                android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">
<intent-filter>
<action android:name="android.accessibilityservice.AccessibilityService" />
</intent-filter>
<meta-data android:name="android.accessibilityservice" android:resource="@xml/accessibilityservice" />
</service>
```

&nbsp;注意上面注册过程中请求了一个权限android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE"

&nbsp; &nbsp; &nbsp;&nbsp;同时这里有一个accessibilityservice.xml文件，里面有对于该辅助功能的一些配置。该文件是我们自己新建的，放置在xml目录下。如果没有该目录，应新建一个xml目录。这里我们的配置如下：

```
<?xml version="1.0" encoding="utf-8"?>
<accessibility-service xmlns:android="http://schemas.android.com/apk/res/android"
    android:accessibilityEventTypes="typeAllMask"
    android:accessibilityFeedbackType="feedbackGeneric"
    android:accessibilityFlags="flagRequestFilterKeyEvents"
    android:canRetrieveWindowContent="true"
    android:canRequestFilterKeyEvents="true"
    android:description="@string/description"
    android:packageNames= "com.gucheng.redpacket">
</accessibility-service>
```

上面的description属性是是对该辅助功能的描述，在设置里看到的就是这个。

```
 <string name="description">模拟点击</string>
```

<div align=center> 
<img src="http://img.blog.csdn.net/20170604115542156" height="50%" width="60%"  />

&nbsp; &nbsp; &nbsp;&nbsp;这里加上packageName的属性是为了区分接收哪个应用的消息，如果没有写这个属性的话，默认接收所有应用的消息。

**重写onAccessibilityEvent方法**
&nbsp; &nbsp; &nbsp;&nbsp;在实现的MyAccessibilityService类中，我们必须要重写两个方法。一个是onInterrupt，另一个是onAccessibilityEvent。如下图;

```
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

```

&nbsp; &nbsp; &nbsp;&nbsp;上图中，显示通过getRootInActiveWindow()获取了根视图，再从根视图中找到含有“查看红包”字样的控件，用performAction(AccessibilityNodeInfo.ACTION_CLICK)来模拟点击事件。 
最后实现的效果如下：
&nbsp; &nbsp; &nbsp;&nbsp;在设置中打开AccessibilityClick的开关后，再进入RedPacket应用，自动就把“红包”拆开了。每次点击“200元”字样后，按钮字样刚变为“查看红包”就会立刻变为“200元”。这个变化的速度非常快以致于看不清楚“查看红包”这几个字。只是一闪而过。

<div align=center> 
![这里写图片描述](http://img.blog.csdn.net/20170604115808174?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvZ3VjaGVuZzMxMTY=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

最后，总结一下实现安卓辅助功能需要以下几步：
	&nbsp; &nbsp; &nbsp;&nbsp;a. 实现一个类继承自AccessibilityService
	&nbsp; &nbsp; &nbsp;&nbsp;b. 在AndroidManifest.xml文件中注册
	&nbsp; &nbsp; &nbsp;&nbsp;c. 重写onAccessibilityEvent方法，在该方法中接收应用变化的消息，并进行相应处理。


贴上本文中写的两个模拟的小程序地址：
模拟红包地址：
&nbsp; &nbsp; &nbsp;&nbsp;https://github.com/gucheng3116/redpacket
模拟抢红包插件地址：
&nbsp; &nbsp; &nbsp;&nbsp;https://github.com/gucheng3116/redpacket_AccessibilityClick

**最后贴上亲测可用的微信抢红包插件源码地址：**
&nbsp; &nbsp; &nbsp;&nbsp;https://github.com/geeeeeeeeek/WeChatLuckyMoney
