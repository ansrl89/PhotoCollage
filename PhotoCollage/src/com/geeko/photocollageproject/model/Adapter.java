package com.geeko.photocollageproject.model;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.geeko.photocollageproject.Data;
import com.geeko.photocollageproject.ui.ItemView;

public class Adapter extends BaseAdapter {

	ArrayList<Data> mList;
	Context mContext;

	public Adapter(Context context, ArrayList<Data> list) {
		mContext = context;
		mList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ItemView v;
		if (convertView == null) {
			v = new ItemView(mContext);
		} else {
			v = (ItemView) convertView;
		}
		Data item = mList.get(position);

		v.setData(item);
		return v;
	}

}
