package com.example.inputcontrolssample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class tabtest extends Activity implements android.view.View.OnClickListener{

	private ViewPager mPager;
	private List<View> listViews;
	private ImageView cursor;
	
	private TextView t1,t2,t3;
	
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_control);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title_1);
		initTextView();
		InitViewPager();
		InitImageView();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.text1: {
			mPager.setCurrentItem(0);
			break;
		}
		case R.id.text2: {
			mPager.setCurrentItem(1);
			break;
		}
		case R.id.text3: {
			mPager.setCurrentItem(2);
			break;
		}
		}
	}	
	
	private void initTextView(){
		t1 = (TextView)this.findViewById(R.id.text1);	
		t2 = (TextView)this.findViewById(R.id.text2);
		t3 = (TextView)this.findViewById(R.id.text3);
		t1.setOnClickListener(this);
		t2.setOnClickListener(this);
		t3.setOnClickListener(this);
	}
	
	public void InitViewPager(){
		mPager = (ViewPager)this.findViewById(R.id.vPager);
		
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = this.getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.lay1,null));
		listViews.add(mInflater.inflate(R.layout.lay2,null));
		listViews.add(mInflater.inflate(R.layout.lay3,null));
		
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}
	
	public class MyPagerAdapter extends PagerAdapter{

		public List<View> mListViews;
		
		public MyPagerAdapter(List<View> listViews){
			this.mListViews = listViews;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(mListViews!=null)
			return mListViews.size();
			else 
				return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == (arg1);
		}
		
		@Override
		public void destroyItem(android.view.ViewGroup container, int position,
				java.lang.Object object) {
			((ViewPager)container).removeView(mListViews.get(position));
		}
		
		@Override
		public void finishUpdate(View arg0){
			
		}
		
		 @Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}
		 
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}
		
		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
		
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset*2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two =  one*2;// 页卡1 -> 页卡3 偏移量
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Animation animation = null;
			
			one = offset*2 + bmpW;
			two =  one*2;
			
			switch (arg0) {
			case 0: {
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			}
			case 1: {
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			}
			case 2: {
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			}
			
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}
		
	}
}
