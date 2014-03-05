package com.example.layoutsample;

import android.app.Activity;
import android.os.Bundle;
import android.util.LayoutDirection;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class layoutDirection extends Activity implements
		android.view.View.OnClickListener {

	/*
	 * Note: To enable right-to-left layout features for your app, you must set
	 * supportsRtl to "true" and set targetSdkVersion to 17 or higher.
	 * http://book.51cto.com/art/201311/418549.htm
	 *   android:layout_marginStart：
	 *   如果在LTR布局模式下，该属性等同于android:layout_marginLeft。
	 *   如果在RTL布局模式下，该属性等同于android:layout_marginRight。
		android:layout_marginEnd：
		如果在LTR布局模式下，该属性等同于android:layout_marginRight。
		如果在RTL布局模式下，该属性等同于android:layout_marginLeft。
    
	 */
	Button btnDirection;
	LinearLayout testlayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_direction);

		btnDirection = (Button) this.findViewById(R.id.btnDirection);
		btnDirection.setOnClickListener(this);
		testlayout = (LinearLayout) this.findViewById(R.id.testlayout);
	}

	int layoutDirection = 0;
	
	@Override
	public void onClick(View v) {
		 
		if(layoutDirection == LayoutDirection.LTR)
			layoutDirection = LayoutDirection.RTL;
		else 
			layoutDirection = LayoutDirection.LTR;
		
		if (v.getId() == R.id.btnDirection){
			testlayout.setLayoutDirection(layoutDirection);
		}
		
	}
}
