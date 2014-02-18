package com.example.imageshowsample;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity  implements android.view.View.OnClickListener{

	ImageView imgView;
	Button btnTest;
	SeekBar seekBar1 ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imgView = (ImageView)this.findViewById(R.id.imgView);
		btnTest = (Button)this.findViewById(R.id.btnTest);
		btnTest.setOnClickListener(this);
		seekBar1 = (SeekBar)this.findViewById(R.id.seekBar1);
		seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				imgView.setAlpha(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	int currentIndex = 0;
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		currentIndex++;
		imgView.setImageResource(R.drawable.a_10 + currentIndex);
		 
	}

}
