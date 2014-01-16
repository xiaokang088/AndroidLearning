package com.example.fragmentsample;

import android.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class HeaderFragment extends Fragment {

	TextView tvShow;

	public interface ItemAddedListener {
		public void AddItem(ItemInfo item);
	}

	private ItemAddedListener ItemAdded;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View content = inflater.inflate(
				com.example.fragmentsample.R.layout.header_fragment, null);
		tvShow = (TextView) content
				.findViewById(com.example.fragmentsample.R.id.tvShow);

		final EditText et = (EditText) content
				.findViewById(com.example.fragmentsample.R.id.etInput);
		et.setOnKeyListener(new android.view.View.OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {

					if (arg1 == KeyEvent.KEYCODE_ENTER) {
						try {
							ItemInfo info = new ItemInfo(et.getText().toString());
							ItemAdded.AddItem(info);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						et.setText("");
						return true;
					}
				}

				return false;
			}
		});

		return content;
	}

	@Override
	public void onAttach(android.app.Activity activity) {
		super.onAttach(activity);
		
		try{
			ItemAdded = (ItemAddedListener)activity;
			
		} catch (Exception ex){
			
		}
	}
}
