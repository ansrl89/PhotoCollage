package com.geeko.photocollageproject;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	public static final String TAG = MyApplication.class.getSimpleName();
	static Context context;

	private Calendar appCalendar;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		initialize();
	}

	public static Context getContext() {
		return context;
	}

	private void initialize() {

		TimeZone reference = TimeZone.getTimeZone("GMT");
		appCalendar = Calendar.getInstance(reference);
		TimeZone.setDefault(reference);
	}

	public Calendar getAppCalendar() {
		return appCalendar;
	}

}
