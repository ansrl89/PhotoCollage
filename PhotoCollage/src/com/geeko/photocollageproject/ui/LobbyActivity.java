package com.geeko.photocollageproject.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.widget.TabHost;

import com.example.photocollage.R;

public class LobbyActivity extends FragmentActivity {

	FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_collage);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		TabHost.TabSpec tabspec = mTabHost.newTabSpec("tab1");
		tabspec.setIndicator("흑백사진 photo collage");
		mTabHost.addTab(tabspec, Fragment2.class, null);

		tabspec = mTabHost.newTabSpec("tab2");
		tabspec.setIndicator("컬러사진 photo collage");
		mTabHost.addTab(tabspec, Fragment3.class, null);

		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_collage, menu);
		return true;
	}

}
