package com.example.imageshowsample;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity  implements android.view.View.OnClickListener{

	//http://www.cnblogs.com/over140/archive/2011/06/08/2075054.html
 
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
	
	@Override
	public void onClick(View v) {
		setImageProperty();
	}

	int currentIndex = 0;
	void setImageTest(){
		currentIndex++;
		// 1.set images resource
		// imgView.setImageResource(R.drawable.a_10 + currentIndex);

		// 2. set image bitmap.
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL picURL = new URL("http://www.baidu.com/img/bdlogo.gif");
					/*final Bitmap pngBM = BitmapFactory.decodeStream(picURL
							.openStream());*/
					final Bitmap  pngBM = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/Screenshots/a.png");
					
					runOnUiThread(new Runnable(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							imgView.setImageBitmap(pngBM);
						}
					});
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	 
		}).start();
		
		// 3. set image uri
		/*ContentResolver resolver = this.getContentResolver();
		String columns[] = new String[] { MediaStore.Images.ImageColumns.DATA };
		Cursor myCursor =  resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				columns, null, null, null);
		
		myCursor.moveToFirst();
		String url = null;
		while(myCursor.moveToNext()){
			  url = myCursor.getString(0);
			  if (url!=null)
				  break;
		}
		
		File file = new File(url);
		Uri uri = Uri.fromFile(file);*/
		/*Uri uri = Uri.parse("/storage/emulated/0/Pictures/Screenshots/a.png");
		imgView.setImageURI(uri);*/
		
		//4. set image drawable
		/*Drawable drawable = this.getResources().getDrawable(R.drawable.a_17);
		imgView.setImageDrawable(drawable);*/
	}

	int a=0;
	void setImageProperty(){
		/*1.setImageState
		 * http://stackoverflow.com/questions/20348057/what-does-the-imageview-method-setimagestateint-state-boolean-merge-do
		 * So it looks like if you set merge to false in setImageState(state, merge) 
		 * then next time the view system calls onCreateDrawableState() 
		 * it will return just the state you supplied in setImageState(state, merge).
				If however you set merge to true in setImageState(state, merge) then
				 next time the view system calls onCreateDrawableState() 
				 it will return the existing state merged with the state 
				 you supplied in setImageState(state, merge).
				In other words, Yes the merge parameter
				 determines whether the state you supply should be combined with 
				 or replace the current states of the ImageView next time
				  the view system calls onCreateDrawableState().

				I still believe some explanation of the parameters 
				should be added to the documentation. 
				I've not come across any other public methods without any documentation so far.
				*/
		//imgView.setImageState(state, merge)
		
		imgView.setImageResource(R.drawable.weathericon);
		
		imgView.setImageLevel(a);
		a= (a == 1 ? 0 : 1);
	}
}
