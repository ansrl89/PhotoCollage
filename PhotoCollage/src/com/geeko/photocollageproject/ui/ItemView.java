package com.geeko.photocollageproject.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.photocollage.R;
import com.geeko.photocollageproject.Data;

public class ItemView extends LinearLayout {

	ImageView icon;
	TextView title, sub;
	Data mData;

	public ItemView(Context context) {
		super(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.itemview_layout, this);
		icon = (ImageView) findViewById(R.id.listViewImage);
		title = (TextView) findViewById(R.id.listViewTitle);
		sub = (TextView) findViewById(R.id.listViewSub);
	}

	public void setData(Data data) {
		mData = data;
		icon.setImageResource(data.getImageId());
		title.setText(data.getTitle());
		sub.setText(data.getsub());
	}

}
