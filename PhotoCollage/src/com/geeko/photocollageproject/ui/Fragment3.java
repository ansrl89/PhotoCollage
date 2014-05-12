package com.geeko.photocollageproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.photocollage.R;

public class Fragment3 extends Fragment {

	ImageView mImageView;
	Button btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_layout3, container, false);
		btn = (Button) v.findViewById(R.id.button1);
		mImageView = (ImageView) v.findViewById(R.id.imageView1);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("MODE", 1);
				intent.putExtra("IMAGE_ID", R.drawable.flag500);
				intent.setClass(getActivity(), PhotoCollageActivity.class);
				startActivity(intent);
			}
		});

		return v;
	}
}
