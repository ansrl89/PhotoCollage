package com.geeko.photocollageproject.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.photocollage.R;
import com.geeko.photocollageproject.Data;
import com.geeko.photocollageproject.model.Adapter;

public class Fragment2 extends Fragment {

	ListView listView;
	ArrayList<Data> mDataList = new ArrayList<Data>();
	Adapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_layout2, container, false);
		if (mDataList.isEmpty()) {
			initData();
		}

		listView = (ListView) v.findViewById(R.id.ListView1);
		adapter = new Adapter(getActivity(), mDataList);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("MODE", 0);
				intent.putExtra("IMAGE_ID", mDataList.get(position)
						.getImageId());
				intent.setClass(getActivity(), PhotoCollageActivity.class);
				startActivity(intent);
			}

		});
		return v;

	}

	void initData() {
		mDataList.add(new Data(R.drawable.cat01, "�����", "���"));
		mDataList.add(new Data(R.drawable.invlena, "����", "���"));
		mDataList.add(new Data(R.drawable.symbolgray, "���δ� �ɺ�", "���"));
		mDataList.add(new Data(R.drawable.bnw1, "I��U", "���"));
		mDataList.add(new Data(R.drawable.bnw3, "I��U2", "���"));
		mDataList.add(new Data(R.drawable.bnw7, "�迬��", "���"));
		mDataList.add(new Data(R.drawable.bnw2, "�׸���1", "���"));
		mDataList.add(new Data(R.drawable.bnw4, "����", "���"));
		mDataList.add(new Data(R.drawable.bnw5, "õ��� �Ǹ��� ����", "���"));
		mDataList.add(new Data(R.drawable.bnw6, "����", "���"));
	}
}
