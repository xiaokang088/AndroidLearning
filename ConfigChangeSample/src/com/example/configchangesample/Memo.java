package com.example.configchangesample;

public class Memo {
	 /* һ����AndroidManifest.xml�ļ��ж�û��ʹ�õ�android:configChanges=
	 * "keyboardHidden|orientation"���ã���Ȼ���Ǻ����õĹ�
	 * �������������������ԣ������Ǻ������л���ʱ���ֱ�ӵ���onCreate�����е�onConfigurationChanged������
	 * ����������ִ��onCreate�������ǵ�Ȼ���������������ԵĻ��ͻ����µ���onCreate�����ˣ������ǲ���
	 * http://www.cnblogs.com/and_he/archive/2011/05/24/2055087.html
	 * http://developer.android.com/guide/topics/manifest/activity-element.html
	 *�������л�ʱ��activity���������� android:configChanges
	 *http://my.eoe.cn/niunaixiaoshu/archive/1514.html
	 *Activity��ConfigChanges���Ե��÷�
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
