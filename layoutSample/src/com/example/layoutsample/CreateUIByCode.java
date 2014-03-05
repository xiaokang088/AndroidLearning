package com.example.layoutsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class CreateUIByCode extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Button btn  =  new Button(this);
		btn.setText("one");
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.RIGHT;
		btn.setLayoutParams(params);
		btn.setGravity(Gravity.CENTER);
		
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(linearLayout.VERTICAL);
		
		linearLayout.addView(btn);
		setContentView(linearLayout);
	}

}
