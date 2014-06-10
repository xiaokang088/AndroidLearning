package com.example.smapleviewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TourActivity extends Activity implements android.view.View.OnClickListener{
	
	ViewPager viewPager;
	CustomPageAdapter pagerAdapter;
	int currentIndex = 0;
	List<View> views;
	TextView txtShow,txtNumber;	
	String[] tourStrings = new String[]{"Choose from the cloud options available and login",
			"Select type of files that you want to backup",
			"Select the drive that you want to backup to "};
	int[] tourImages = new int[]{R.drawable.tour_cloud,R.drawable.tour_files,R.drawable.tour_target};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tour);
		
		Intent intent =  this.getIntent() ;
		int backuptype =  intent.getExtras().getInt("backupType");
		
		loadControls();
		loadData();
	}
	
	
	void initButton(int btnID){
		Button btn = (Button)this.findViewById(btnID);
		btn.setOnClickListener(this);
	}
	
	void loadControls(){
		initButton(R.id.btnSkip);
		initButton(R.id.btnBack);
		initButton(R.id.btnConinue);
		  
		viewPager =  (ViewPager) this.findViewById(R.id.viewpager);
		
		views = new ArrayList<View>();
		LayoutInflater mInflater = this.getLayoutInflater();
		views.add(mInflater.inflate(R.layout.img_tour,null));
		views.add(mInflater.inflate(R.layout.img_tour,null));
		views.add(mInflater.inflate(R.layout.img_tour,null));
		
		pagerAdapter = new CustomPageAdapter(views);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new CustomOnPageChangeListener());
		
		txtShow =  (TextView)this.findViewById(R.id.txtShow);
		txtNumber =  (TextView)this.findViewById(R.id.txtNumber);
	}
	
	
	void loadData(){
		View currentView = views.get(currentIndex);
 
		ImageView img  = (ImageView)currentView.findViewById(R.id.img);
		img.setImageResource(tourImages[currentIndex]);
		
		String str =  Integer.toString(currentIndex + 1);
		txtNumber.setText(str);
		
		txtShow.setText(tourStrings[currentIndex]);
	}
	
	public class CustomOnPageChangeListener implements OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			currentIndex = arg0;
			loadData();
		}
	}
	
	public class CustomPageAdapter extends PagerAdapter{

		List<View> _views;
		
		public CustomPageAdapter(List<View> views) {
			_views = views;
		}

		@Override
		public int getCount() {
			return _views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		@Override
		public void destroyItem(android.view.View container, int position,
				java.lang.Object object) {
			((ViewPager) container).removeView(_views.get(position));
		}

		@Override
		public java.lang.Object instantiateItem(android.view.View container,
				int position) {
			((ViewPager) container).addView(_views.get(position), 0);
			return _views.get(position);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.btnBack:
			if (currentIndex == 0) return;
			int index = currentIndex-1;
			viewPager.setCurrentItem(index);
			break;
		case R.id.btnConinue:
			if (currentIndex == 2){
				this.finish();
				return ;
			}
			int nextIndex = currentIndex+1;
			viewPager.setCurrentItem(nextIndex);
			break;
		case R.id.btnSkip:
			this.finish();
			break;
		}
	}

}
