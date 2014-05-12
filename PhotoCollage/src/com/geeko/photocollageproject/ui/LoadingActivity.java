package com.geeko.photocollageproject.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Window;
import android.widget.TextView;

import com.example.photocollage.R;

public class LoadingActivity extends Activity {

	public static final String TAG = LoadingActivity.class.getSimpleName();
	public static List<Bitmap> mBitmapList;

	public String id;
	public Bitmap bitmap;
	public BitmapFactory.Options opions;
	public Uri uri;
	public Cursor cursor;

	private TextView lobbyTitle;
	private TextView status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loadingactivity_layout);

		/*
		 * lobbyTitle = (TextView) findViewById(R.id.textView2); status =
		 * (TextView) findViewById(R.id.textView1);
		 * 
		 * Typeface face = Typeface.createFromAsset(getAssets(),
		 * "fonts/Eternal Call.ttf");
		 * 
		 * Typeface face2 = Typeface.createFromAsset(getAssets(),
		 * "fonts/Confetti Stream.ttf");
		 * 
		 * lobbyTitle.setTypeface(face); lobbyTitle.setText("Photo Collage");
		 * lobbyTitle.setTextSize(100); status.setTypeface(face2); //
		 * status.setScaleX(1.1f); status.setText("Loading.....");
		 * status.setTextSize(40);
		 */

		mBitmapList = new ArrayList<Bitmap>();
		cursor = getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);

		new AsyncImageLoad().execute(null, null);

	}

	public class AsyncImageLoad extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			cursor.moveToNext();

			do {
				id = cursor.getString(0);

				uri = Uri.withAppendedPath(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

				bitmap = MediaStore.Images.Thumbnails.getThumbnail(
						getContentResolver(), Integer.parseInt(id),
						MediaStore.Images.Thumbnails.MICRO_KIND, null);

				mBitmapList.add(bitmap);
			} while (cursor.moveToNext());

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			new Handler() {

				@Override
				public void handleMessage(Message msg) {
					LoadingActivity.this.startActivity(new Intent(
							LoadingActivity.this, LobbyActivity.class));
					overridePendingTransition(android.R.anim.fade_in,
							android.R.anim.fade_out);
					LoadingActivity.this.finish();
				}

			}.sendEmptyMessageDelayed(0, 1500);
		}

	}
}