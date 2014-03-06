package com.example.adapterexample;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlanAdapter extends ArrayAdapter<PlanItem> {

	public PlanAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}

 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(R.layout.list_plan_item, null);
		}

		TextView tvFrom = (TextView) convertView.findViewById(R.id.tvFrom);
		TextView tvTo = (TextView) convertView.findViewById(R.id.tvTo);
		TextView tvCreated = (TextView) convertView
				.findViewById(R.id.tvCreated);

		PlanItem item = (PlanItem) this.getItem(position);

		tvFrom.setText(item.from);
		tvTo.setText(item.to);

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"hh:mm:ss yyyy/MM/dd ");
		tvCreated.setText(dateFormat.format(item.created));

		return convertView;
	}
}
