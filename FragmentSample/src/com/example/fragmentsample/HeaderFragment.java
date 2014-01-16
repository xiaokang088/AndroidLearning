package com.example.fragmentsample;

 
import android.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
 
public class HeaderFragment extends Fragment {

	TextView tvShow;
	
	@Override
	public  View onCreateView( LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState){
		View content =inflater.inflate(com.example.fragmentsample.R.layout.header_fragment, null);
		tvShow = (TextView)content.findViewById(com.example.fragmentsample.R.id.tvShow);
		
		return content;
	}
	
	@Override
	public void onAttach(android.app.Activity activity){
		super.onAttach(activity);
		
		 
	}
}
