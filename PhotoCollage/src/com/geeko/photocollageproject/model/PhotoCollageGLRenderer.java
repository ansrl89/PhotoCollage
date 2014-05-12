package com.geeko.photocollageproject.model;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

public class PhotoCollageGLRenderer implements GLSurfaceView.Renderer {

	private static final String TAG = "PhotoCollageGLRenderer";

	public GL11 gl11;
	protected int surface_width;
	protected int surface_height;
	private float left = -1, right = 1, top = 1, bottom = -1, near = 0.0001f,
			far = 10000.0f;
	private float eyeX = 0, eyeY = 0, eyeZ = 1, centerX = 0, centerY = 0,
			centerZ = 0, upX = 0, upY = 1, upZ = 0;
	private float projectionMatrix[] = new float[16];
	private float modelViewMatrix[] = new float[16];
	private int viewport[] = new int[4];
	private float ratio;
	private float bmpRatio;
	private final float zoomRatio = 1.05f;
	private int zoomCnt = 0;

	private final int sizeNum = 64;
	private final int degree = 1;
	
	
	// quad
	private float[] quad = new float[] {
			-0.5f, 0.5f, 0.0f, // left top
			0.5f, 0.5f, 0.0f, // right top
			-0.5f, -0.5f, 0.0f, // left bottom
			0.5f, -0.5f, 0.0f, // right bottom
	};
	private float[] white_quad = new float[] { -0.5f, 0.5f, 0.0f, // left top
			0.5f, 0.5f, 0.0f, // right top
			-0.5f, -0.6f, 0.0f, // left bottom
			0.5f, -0.6f, 0.0f, // right bottom
	};
	private float[] line_quad = new float[] { -0.5f, 0.5f, 0.0f, // left top
			-0.5f, -0.6f, 0.0f, // left bottom
			0.5f, -0.6f, 0.0f, // right bottom
			0.5f, 0.5f, 0.0f, // right top
	};
	private FloatBuffer white_quad_buffer;
	private FloatBuffer line_quad_buffer;
	private FloatBuffer quad_buffer;
	private int mode;

	// texture
	private float texcoords[] = { 0, 0, 1, 0, 0, 1, 1, 1 };
	private FloatBuffer texcoord_buffer;

	private int[] tex_ids = new int[4];
	private int pixels[];
	private int sizeOfPixels;
	private int bmpWidth;
	private int bmpheight;
	private int mapInfoArray[];


	private int BNWphotoSrcIdx[];
	private int COLORphotoSrcIdx[];
	private float BNWscalingV[];
	private float BNWrotatingV[];
	private int randomRatioV[];
	private int randomRatioV2[];
	private float depthV[];
	
	private final int vectorSize = new Random().nextInt(1000) + 200;

	// texture image
	private boolean is_bitmap_ready = false;
	private Bitmap bmp = null;
	private Bitmap scaled_bmp = null;


	private float[] clear_color = new float[3];

	private final int BNW_Src_Cnt = 50;
	private final int COLOR_Src_Cnt = 36;
	
	private final int srcImageSize = 80;
	private int[] imgBuffer = new int[srcImageSize*srcImageSize];
	private int[] whiteGroup = new int[COLOR_Src_Cnt * srcImageSize*srcImageSize];
	private int[] blackGroup = new int[COLOR_Src_Cnt * srcImageSize*srcImageSize];
	private int[] redGroup = new int[COLOR_Src_Cnt * srcImageSize*srcImageSize];
	private int[] blueGroup = new int[COLOR_Src_Cnt * srcImageSize*srcImageSize];
	private int[] normalGroup = new int[BNW_Src_Cnt * srcImageSize*srcImageSize];
	
	
	// animation
	private boolean is_play_anim = false;
	private long time_prev = 0;
	private float angle = 0;

	public void toggleAnimation() {
		is_play_anim = !is_play_anim;
		time_prev = System.currentTimeMillis();
	}

	public void updateClearColor() {
		Random rand = new Random();

		clear_color[0] = rand.nextFloat();
		clear_color[1] = rand.nextFloat();
		clear_color[2] = rand.nextFloat();
		// clear_color[0] = 0f;
		// clear_color[1] = 0f;
		// clear_color[2] = 0f;
	}

	public void setTextureImage(){
		is_bitmap_ready = true;
	}

	private void updateTexture() {
		if(mode == 0){
			gl11.glGenTextures(1, tex_ids, 0);
			
			gl11.glBindTexture(GL11.GL_TEXTURE_2D, tex_ids[0]);

			Log.i(TAG, "gl.glTexParameterf");
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			
			Bitmap obj = Bitmap.createBitmap(normalGroup, srcImageSize*BNW_Src_Cnt, srcImageSize, Bitmap.Config.ARGB_8888);
			
			GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, obj, 0);
		}
		
		else if(mode == 1){
			gl11.glGenTextures(4, tex_ids, 0);
			
			gl11.glBindTexture(GL11.GL_TEXTURE_2D, tex_ids[0]);

			Log.i(TAG, "gl.glTexParameterf");
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			
			Bitmap obj0 = Bitmap.createBitmap(whiteGroup, srcImageSize*COLOR_Src_Cnt, srcImageSize, Bitmap.Config.ARGB_8888);

			GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, obj0, 0);
			
			gl11.glBindTexture(GL11.GL_TEXTURE_2D, tex_ids[1]);
			
			Log.i(TAG, "gl.glTexParameterf");
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			
			Bitmap obj1 = Bitmap.createBitmap(blackGroup, 0, srcImageSize*COLOR_Src_Cnt, srcImageSize*COLOR_Src_Cnt, srcImageSize, Bitmap.Config.ARGB_8888);
	
			GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, obj1, 0);
			
			gl11.glBindTexture(GL11.GL_TEXTURE_2D, tex_ids[2]);
			
			Log.i(TAG, "gl.glTexParameterf");
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			
			Bitmap obj2 = Bitmap.createBitmap(redGroup, srcImageSize*COLOR_Src_Cnt, srcImageSize, Bitmap.Config.ARGB_8888);

			GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, obj2, 0);
			
			gl11.glBindTexture(GL11.GL_TEXTURE_2D, tex_ids[3]);
			
			Log.i(TAG, "gl.glTexParameterf");
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			
			Bitmap obj3 = Bitmap.createBitmap(blueGroup, srcImageSize*COLOR_Src_Cnt, srcImageSize, Bitmap.Config.ARGB_8888);
	
			GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, obj3, 0);
		}

		is_bitmap_ready = false;
	}

	public void update_Background_Image(Bitmap bitmap, int m) {
		bmp = bitmap;
		mode = m;
		
		bmpRatio = (float) bmp.getHeight() / bmp.getWidth();
		scaled_bmp = Bitmap.createScaledBitmap(bmp, sizeNum, (int) (sizeNum), true);

		Log.i(TAG, "get_Background_Image_Info");
		get_Background_Image_Info();

		scaled_bmp.recycle(); // remove bmp
		bmp.recycle(); // remove bmp
	}

	private void get_Background_Image_Info() {
		sizeOfPixels = scaled_bmp.getWidth() * scaled_bmp.getHeight();
		bmpWidth = scaled_bmp.getWidth();
		bmpheight = scaled_bmp.getHeight();
		pixels = new int[sizeOfPixels];
		mapInfoArray = new int[sizeOfPixels];

		new Thread(new Runnable() {

			@Override
			public void run() {

				scaled_bmp.getPixels(pixels, 0, scaled_bmp.getWidth(), 0, 0,
						scaled_bmp.getWidth(), scaled_bmp.getHeight());

				for (int i = 0; i < scaled_bmp.getWidth(); i += degree) {
					for (int j = 0; j < scaled_bmp.getHeight(); j += degree) {

						int RGB[] = do_average(i, j);
						int red = RGB[0];
						int green = RGB[1];
						int blue = RGB[2];

						if (red < 80 && green < 80 && blue < 80) {
							mapInfoArray[bmpheight * i + j] = 1; // black
						} else if (red > 100 && green < 100 && blue < 100) {
							mapInfoArray[bmpheight * i + j] = 2; // red
						} else if (blue > 100 && red < 100 && green < 100) {
							mapInfoArray[bmpheight * i + j] = 3; // blue
						} else {
							mapInfoArray[bmpheight * i + j] = 0;
						}

					}

				}

			}
		}).run();

	}

	private int[] do_average(int row, int col){
		int RGB[] = new int[3];

		for (int i = 0; i < degree; i++) {
			for (int j = 0; j < degree; j++) {
				int argb = pixels[(bmpheight * (row + i) + (col + j))];

				RGB[0] += (argb & 0xFF0000) >> 16;
				RGB[1] += (argb & 0xFF00) >> 8;
				RGB[2] += (argb & 0xFF);
			}
		}

		RGB[0] /= degree * degree;
		RGB[1] /= degree * degree;
		RGB[2] /= degree * degree;

		return RGB;
	}
	
	public void update_Src_Images(Bitmap srcImg, int id, int num) {
		scaled_bmp = Bitmap.createScaledBitmap(srcImg, srcImageSize, srcImageSize, true);
		scaled_bmp.getPixels(imgBuffer, 0, scaled_bmp.getWidth(), 0, 0,
				scaled_bmp.getWidth(), scaled_bmp.getHeight());
		
		switch (id){
		case 0:	// normal
			myArraycopy(imgBuffer, 0, normalGroup, srcImageSize*(num-1), BNW_Src_Cnt);
			break;
		case 1:	// white
			myArraycopy(imgBuffer, 0, whiteGroup, srcImageSize*(num-1), COLOR_Src_Cnt);
			break;
		case 2:	// black
			myArraycopy(imgBuffer, 0, blackGroup, srcImageSize*(num-1), COLOR_Src_Cnt);
			break;
		case 3:	// red
			myArraycopy(imgBuffer, 0, redGroup, srcImageSize*(num-1), COLOR_Src_Cnt);
			break;
		case 4:	// blue
			myArraycopy(imgBuffer, 0, blueGroup, srcImageSize*(num-1), COLOR_Src_Cnt);
			break;
		}
		
		srcImg.recycle();
		scaled_bmp.recycle();
	}
	
	private void myArraycopy(int src[], int srcIdx, int des[], int desIdx, int cnt){
		for (int i = 0; i < srcImageSize; i++) {
			for (int j = 0; j < srcImageSize; j++) {
				des[(i*cnt*srcImageSize) + (desIdx + j)] = src[(i*srcImageSize) + (j + srcIdx)];
			}
		}
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		this.gl11 = (GL11) gl;
		if (is_bitmap_ready)
			updateTexture();

		// draw
		gl11.glClearColor(clear_color[0], clear_color[1], clear_color[2], 1.f);
		gl11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		gl11.glMatrixMode(GL11.GL_PROJECTION);
		gl11.glLoadIdentity();
		gl11.glOrthof(left, right, bottom, top, near, far);

		gl11.glMatrixMode(GL11.GL_MODELVIEW);
		gl11.glLoadIdentity();
		GLU.gluLookAt(gl11, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX,
				upY, upZ);

		gl11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		gl11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

		do_animation();

		switch (mode) {
		case 0:
			// make white box, 흰색 배경
			makeWhiteBox();
			drawBNW();
			break;
		case 1:
			drawCOLOR();
			break;
		default:
			break;
		}

		gl11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		gl11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

		gl11.glFlush();
	}

	private void do_animation() {
		if (is_play_anim) {
			Log.i(TAG, "play anim");
			long time_curr = System.currentTimeMillis();
			long time_step = time_curr - time_prev;

			Log.i(TAG, "time step :" + time_step);
			float coef = (float) ((float) time_step / 10.0);
			angle += coef;

			time_prev = time_curr;
			Log.i(TAG, "amgle :" + angle);
			gl11.glRotatef(angle, 0, 0, 1);
		}
	}

	private int plus_RandomKeyIdx(int i) {
		i++;
		if (i >= vectorSize)
			i = i % vectorSize;
		return i;
	}

	private void drawBNW() {

		float firstX = -0.5f, firstY = 0.5f;
		int randomKeyIdx = 0;
		for (int i = 0; i < sizeNum; i+=degree) {
			for (int j = 0; j < sizeNum; j+=degree) {
				if (mapInfoArray[(j * bmpWidth) + i] == 0) {
					continue;
				}

				gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, white_quad_buffer);

				gl11.glColor4f(1.f, 1.f, 1.f, 1.f);
				gl11.glPushMatrix();

				// basic positioning
				float transX = firstX + i * (1.f / sizeNum);
				float transY = firstY - j * (1.f / sizeNum);
				gl11.glTranslatef(transX, transY, depthV[randomKeyIdx]);
				gl11.glScalef(1.f*degree / sizeNum, 1.f*degree / sizeNum, 1);

				// each photo's specializing
				gl11.glScalef(BNWscalingV[randomKeyIdx],
						BNWscalingV[randomKeyIdx++], 1);
				randomKeyIdx = plus_RandomKeyIdx(randomKeyIdx);

				gl11.glRotatef(BNWrotatingV[randomKeyIdx++], 0, 0, 1);
				randomKeyIdx = plus_RandomKeyIdx(randomKeyIdx);

				// Transformation end
				gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

				gl11.glColor4f(0.f, 0.f, 0.f, 1.f);
				gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, line_quad_buffer);
				gl11.glDrawArrays(GL11.GL_LINE_LOOP, 0, 4);

				
				
				float coords[] = { 
						(1.f*(srcImageSize*randomRatioV[randomKeyIdx]) / (srcImageSize*100)), 0, 
						(1.f*(srcImageSize*(randomRatioV[randomKeyIdx]+1)) / (srcImageSize*100)), 0, 
						(1.f*(srcImageSize*randomRatioV[randomKeyIdx]) / (srcImageSize*100)), 1, 
						(1.f*(srcImageSize*(randomRatioV[randomKeyIdx]+1)) / (srcImageSize*100)), 1 };
				
				FloatBuffer tex_buffer;
				tex_buffer = BufferUtil.makeFloatBuffer(coords);

				// will change tex_buffer
				gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, tex_buffer);
				gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, quad_buffer);

				gl11.glColor4f(1.f, 1.f, 1.f, 1.f);

				gl11.glEnable(GL10.GL_TEXTURE_2D);
				gl11.glBindTexture(GL11.GL_TEXTURE_2D,
						tex_ids[0]);

				gl11.glColor4f(1.f, 1.f, 1.f, 1.f);
				gl11.glScalef(0.95f, 0.95f, 1);
				gl11.glTranslatef(0f, 0f, 0.2f);
				
				gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

				gl11.glDisable(GL11.GL_TEXTURE_2D);

				gl11.glPopMatrix();
			}
		}
	}

	private void makeWhiteBox() {
		gl11.glColor4f(1f, 1f, 1f, 1f);
		gl11.glPushMatrix();
		gl11.glTranslatef(0f,  0f, -20f);
		gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		gl11.glPopMatrix();
	}

	private void drawCOLOR() {
		float firstX = -0.5f, firstY = 0.5f;
		int randomKeyIdx = 0;
		for (int i = 0; i < sizeNum; i += degree) {
			for (int j = 0; j < sizeNum; j += degree) {
				float coords[] = {
					(1.f * (srcImageSize * randomRatioV2[randomKeyIdx]) / (srcImageSize * COLOR_Src_Cnt)), 0,
					(1.f * (srcImageSize * (randomRatioV2[randomKeyIdx] + 1)) / (srcImageSize * COLOR_Src_Cnt)), 0,
					(1.f * (srcImageSize * randomRatioV2[randomKeyIdx]) / (srcImageSize * COLOR_Src_Cnt)), 1,
					(1.f * (srcImageSize * (randomRatioV2[randomKeyIdx] + 1)) / (srcImageSize * COLOR_Src_Cnt)), 1 
				};

				FloatBuffer tex_buffer;
				tex_buffer = BufferUtil.makeFloatBuffer(coords);

				// will change tex_buffer
				gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, tex_buffer);
				gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, quad_buffer);

				int CORLOGroupIdx = 0;
				if (mapInfoArray[(j * bmpWidth) + i] == 0) {
					CORLOGroupIdx = 0;
				} else if (mapInfoArray[(j * bmpWidth) + i] == 2) {
					CORLOGroupIdx = 2;
				} else if (mapInfoArray[(j * bmpWidth) + i] == 3) {
					CORLOGroupIdx = 3;
				} else {
					CORLOGroupIdx = 1;
				}

				gl11.glEnable(GL11.GL_TEXTURE_2D);
				gl11.glBindTexture(GL11.GL_TEXTURE_2D, tex_ids[CORLOGroupIdx]);
				randomKeyIdx = plus_RandomKeyIdx(randomKeyIdx);

				gl11.glColor4f(1.f, 1.f, 1.f, 1.f);
				gl11.glPushMatrix();

				// basic positioning
				float transX = firstX + i * (1.f / sizeNum);
				float transY = firstY - j * (1.f / sizeNum);
				gl11.glTranslatef(transX, transY, 0);
				gl11.glScalef(1.f * degree / sizeNum, 1.f * degree / sizeNum, 1);

				// Transformation end
				gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
				gl11.glPopMatrix();

				gl11.glDisable(GL11.GL_TEXTURE_2D);
			}
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		Log.i(TAG, "onSurfaceChanged()");
		GL11 gl11 = (GL11) gl;
		surface_width = width;
		surface_height = height;

		ratio = (float) surface_height / surface_width;
		if (ratio >= 1) {
			top *= ratio;
			bottom *= ratio;
		} else {
			left *= 1 / ratio;
			right *= 1 / ratio;
		}

		gl11.glMatrixMode(GL11.GL_PROJECTION);
		gl11.glLoadIdentity();

		GLU.gluOrtho2D(gl11, left, right, bottom, top);
		// gl11.glOrthof(left, right, bottom, top, near, far);

		gl11.glViewport(0, 0, surface_width, surface_height);
		gl11.glGetIntegerv(GL11.GL_VIEWPORT, viewport, 0);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.i(TAG, "onSurfaceCreated()");

		gl11 = (GL11) gl;
		// gl.glClearColor(0, 0.5f, 0.5f, 0);
		updateClearColor();

		gl11.glClearDepthf(1f);
		
		gl11.glEnable(GL11.GL_DEPTH_TEST);

		
		
		gl11.glDisable(GL11.GL_LIGHTING);
		gl11.glDisable(GL11.GL_CULL_FACE);

		line_quad_buffer = BufferUtil.makeFloatBuffer(line_quad);
		white_quad_buffer = BufferUtil.makeFloatBuffer(white_quad);
		quad_buffer = BufferUtil.makeFloatBuffer(quad);
		texcoord_buffer = BufferUtil.makeFloatBuffer(texcoords);

		gl11.glMatrixMode(GL10.GL_MODELVIEW);
		gl11.glLoadIdentity();
		GLU.gluLookAt(gl11, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX,
				upY, upZ);
		gl11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix, 0);

		gl11.glMatrixMode(GL11.GL_PROJECTION);
		gl11.glLoadIdentity();

		GLU.gluOrtho2D(gl11, left, right, bottom, top);
		// gl11.glOrthof(left, right, bottom, top, near, far);
		gl11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projectionMatrix, 0);

		gl11.glGetIntegerv(GL11.GL_VIEWPORT, viewport, 0);

		// 난수 배열 최초 생성
		BNWphotoSrcIdx = new int[vectorSize];
		COLORphotoSrcIdx = new int[vectorSize];
		BNWscalingV = new float[vectorSize];
		BNWrotatingV = new float[vectorSize];
		randomRatioV = new int[vectorSize];
		randomRatioV2 = new int[vectorSize];
		depthV = new float[vectorSize];
		
		Random obj = new Random();
		for (int m = 0; m < vectorSize; m++) {
			BNWphotoSrcIdx[m] = obj.nextInt(50) % 1;
			COLORphotoSrcIdx[m] = obj.nextInt(200) % 4;
			randomRatioV[m] = obj.nextInt(300) % 100;
			randomRatioV2[m] = obj.nextInt(300) % COLOR_Src_Cnt;
			BNWscalingV[m] = obj.nextFloat() * 0.2f + 1.f;
			BNWrotatingV[m] = obj.nextFloat() + new Random().nextInt(31) - 15;
			depthV[m] = obj.nextFloat() - new Random().nextInt(5) - 1;
		}
	}

	public void drag(float[] prev_pos, float[] curr_pos) {
		float newCoord[] = new float[4];
		float preCoord[] = new float[4];

		gl11.glMatrixMode(GL10.GL_MODELVIEW);
		gl11.glLoadIdentity();
		GLU.gluLookAt(gl11, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX,
				upY, upZ);
		gl11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix, 0);

		gl11.glMatrixMode(GL11.GL_PROJECTION);
		gl11.glLoadIdentity();
		GLU.gluOrtho2D(gl11, left, right, bottom, top);
		// gl11.glOrthof(left, right, bottom, top, near, far);
		gl11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projectionMatrix, 0);

		gl11.glGetIntegerv(GL11.GL_VIEWPORT, viewport, 0);

		GLU.gluUnProject(prev_pos[0], prev_pos[1], 0, modelViewMatrix, 0,
				projectionMatrix, 0, viewport, 0, preCoord, 0);
		GLU.gluUnProject(curr_pos[0], curr_pos[1], 0, modelViewMatrix, 0,
				projectionMatrix, 0, viewport, 0, newCoord, 0);

		float dispX = newCoord[0] - preCoord[0];
		float dispY = newCoord[1] - preCoord[1];

		if (ratio <= 1)
			dispX *= 1 / ratio;
		else
			dispY *= ratio;

		if (zoomCnt >= 0) {
			dispX *= 1.f / Math.pow(zoomRatio, Math.abs(zoomCnt));
			dispY *= 1.f / Math.pow(zoomRatio, Math.abs(zoomCnt));
		} else {
			dispX *= Math.pow(zoomRatio, Math.abs(zoomCnt));
			dispY *= Math.pow(zoomRatio, Math.abs(zoomCnt));
		}
		eyeX -= dispX;
		centerX -= dispX;
		eyeY += dispY;
		centerY += dispY;

		prev_pos = curr_pos;
	}

	public void zoom(float[] prev_pos, float[] prev_pos2, float[] curr_pos,
			float[] curr_pos2) {
		float prevDistance = (prev_pos[0] - prev_pos2[0])
				* (prev_pos[0] - prev_pos2[0]) + (prev_pos[1] - prev_pos2[1])
				* (prev_pos[1] - prev_pos2[1]);
		float currDistance = (curr_pos[0] - curr_pos2[0])
				* (curr_pos[0] - curr_pos2[0]) + (curr_pos[1] - curr_pos2[1])
				* (curr_pos[1] - curr_pos2[1]);
		float direction = prevDistance >= currDistance ? 1 : -1;

		if (direction > 0) {
			left *= zoomRatio;
			right *= zoomRatio;
			bottom *= zoomRatio;
			top *= zoomRatio;

			zoomCnt--;
		} else {
			left *= 1 / zoomRatio;
			right *= 1 / zoomRatio;
			bottom *= 1 / zoomRatio;
			top *= 1 / zoomRatio;

			zoomCnt++;
		}

	}

}
