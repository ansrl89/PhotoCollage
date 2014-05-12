package com.geeko.photocollageproject.ui;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;

import com.example.photocollage.R;
import com.geeko.photocollageproject.model.PhotoCollageGLRenderer;
import com.geeko.photocollageproject.model.TouchEventHandler;

public class PhotoCollageActivity extends Activity {

	private static final String TAG = "PhotoCollageActivity";

	protected GLSurfaceView surface_view;
	protected GLSurfaceView surface_view2;
	protected PhotoCollageGLRenderer renderer;
	public ArrayList<Bitmap> bmpArr;
	public BitmapFactory.Options opt = new BitmapFactory.Options();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity);
		Intent intent = getIntent();
		int mode = intent.getIntExtra("MODE", 0);
		int imageId = intent.getIntExtra("IMAGE_ID", R.drawable.ic_launcher);
		
		
		Log.i(TAG, "onCreate");

		renderer = new PhotoCollageGLRenderer();
		surface_view = new GLSurfaceView(this);
		surface_view.setRenderer(renderer);
		surface_view.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		surface_view.setOnTouchListener(new TouchEventHandler(renderer));

		addContentView(surface_view, new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		
		
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), imageId);
		renderer.update_Background_Image(bmp, mode);
		
		opt.inSampleSize = 4;
		int size = LoadingActivity.mBitmapList.size();
		int hundred[] = new int[size];
		for(int i = 0; i < size; i++)
			hundred[i] = i;
		for(int i = 0; i < size; i++){
			int key = new Random().nextInt(size);
			
			int tmp = hundred[key];
			hundred[key] = hundred[i];
			hundred[i] = tmp;
		}
		
		if(mode == 0){
			for(int i=1; i<=50; i++){
				String uri = "drawable/"+ "normal"+ hundred[i];
				int imageResource = getResources().getIdentifier(uri, null, getPackageName());
//				Bitmap srcImg = BitmapFactory.decodeResource(getResources(), imageResource, opt);
				Bitmap srcImg = LoadingActivity.mBitmapList.get(hundred[i]);
				renderer.update_Src_Images(srcImg, 0, i);
			}
		}
		
		else if(mode == 1){
			for(int i=1; i<=36; i++){
				String uri = "drawable/"+ "white"+i;
				int imageResource = getResources().getIdentifier(uri, null, getPackageName());
				Bitmap srcImg = BitmapFactory.decodeResource(getResources(), imageResource, opt);
				renderer.update_Src_Images(srcImg, 1, i);
			}
			for(int i=1; i<=36; i++){
				String uri = "drawable/"+ "black"+i;
				int imageResource = getResources().getIdentifier(uri, null, getPackageName());
				Bitmap srcImg = BitmapFactory.decodeResource(getResources(), imageResource, opt);
				renderer.update_Src_Images(srcImg, 2, i);
			}
			for(int i=1; i<=36; i++){
				String uri = "drawable/"+ "red"+i;
				int imageResource = getResources().getIdentifier(uri, null, getPackageName());
				Bitmap srcImg = BitmapFactory.decodeResource(getResources(), imageResource, opt);
				renderer.update_Src_Images(srcImg, 3, i);
			}
			for(int i=1; i<=36; i++){
				String uri = "drawable/"+ "blue"+i;
				int imageResource = getResources().getIdentifier(uri, null, getPackageName());
				Bitmap srcImg = BitmapFactory.decodeResource(getResources(), imageResource, opt);
				renderer.update_Src_Images(srcImg, 4, i);
			}
		}
		
		renderer.setTextureImage();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i(TAG, "onResume()");
		surface_view.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		surface_view.onPause();
		Log.i(TAG, "onPause()");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_collage, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.animation:
			renderer.toggleAnimation();
			break;
		default:
			return false;
		}

		return false;
	}

}
