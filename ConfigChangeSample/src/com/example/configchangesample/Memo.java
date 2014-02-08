package com.example.configchangesample;

public class Memo {
	 /* 一般在AndroidManifest.xml文件中都没有使用到android:configChanges=
	 * "keyboardHidden|orientation"配置，当然还是很有用的哈
	 * 就是如果配置了这个属性，当我们横竖屏切换的时候会直接调用onCreate方法中的onConfigurationChanged方法，
	 * 而不会重新执行onCreate方法，那当然如果不配置这个属性的话就会重新调用onCreate方法了，下面是测试
	 * http://www.cnblogs.com/and_he/archive/2011/05/24/2055087.html
	 * http://developer.android.com/guide/topics/manifest/activity-element.html
	 *横竖屏切换时候activity的生命周期 android:configChanges
	 *http://my.eoe.cn/niunaixiaoshu/archive/1514.html
	 *Activity中ConfigChanges属性的用法
	 *http://www.cnblogs.com/adamzuocy/archive/2009/10/15/1583670.html
*/
	
/*    android:targetSdkVersion="18" 
    android:configChanges="keyboardHidden|orientation|screenSize" 
	02-08 10:16:44.318: I/configchangesample(30051): onConfigurationChanged:2
	02-08 10:16:47.915: I/configchangesample(30051): onConfigurationChanged:1
	02-08 10:16:50.549: I/configchangesample(30051): onConfigurationChanged:2
*/
	
    /*android:targetSdkVersion="18"
    android:configChanges="orientation|screenSize"
	02-08 10:17:52.684: I/configchangesample(30313): onConfigurationChanged:2
	02-08 10:17:56.386: I/configchangesample(30313): onConfigurationChanged:1
	02-08 10:18:01.379: I/configchangesample(30313): onConfigurationChanged:2
	02-08 10:18:07.006: I/configchangesample(30313): onConfigurationChanged:1
*/
	
	/*  android:configChanges="orientation|screenSize"
			  02-08 10:20:24.167: I/configchangesample(30463): onConfigurationChanged:1
			  02-08 10:20:26.519: I/configchangesample(30463): onConfigurationChanged:2
			  02-08 10:20:33.290: I/configchangesample(30463): onConfigurationChanged:1
*/
	
}
