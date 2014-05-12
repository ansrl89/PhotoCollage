package com.geeko.photocollageproject.model;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class TouchEventHandler implements OnTouchListener {

	private static final String TAG = "TouchEventHandler";
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;
	private float distance;

	protected float[] start_pos;
	protected float[] prev_pos;
	protected float[] curr_pos;

	protected float[] start_pos2;
	protected float[] prev_pos2;
	protected float[] curr_pos2;

	protected boolean thrown;

	protected long delta_time;
	protected long last_time;

	protected PhotoCollageGLRenderer renderer;

	public TouchEventHandler(PhotoCollageGLRenderer renderer) {
		this.renderer = renderer;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		curr_pos = new float[2];
		curr_pos[0] = event.getX(0);
		curr_pos[1] = event.getY(0);

		curr_pos2 = new float[2];

		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			Log.i(TAG, "ACTION_DOWN (" + curr_pos[0] + ", " + curr_pos[1]);
			start_pos = curr_pos;
			prev_pos = curr_pos;
			mode = DRAG;
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			curr_pos2[0] = event.getX(1);
			curr_pos2[1] = event.getY(1);
			prev_pos2 = curr_pos2;
			Log.i(TAG, "ACTION_POINTER_DOWN (" + curr_pos[0] + ", "
					+ curr_pos[1]);
			Log.i(TAG, "ACTION_POINTER_DOWN (" + curr_pos2[0] + ", "
					+ curr_pos2[1]);
			mode = ZOOM;
			break;

		case MotionEvent.ACTION_UP:
			Log.i(TAG, "ACTION_UP (" + curr_pos[0] + ", " + curr_pos[1]);

			// example
			if (start_pos[0] == curr_pos[0] && start_pos[1] == curr_pos[1]) {
				// click event
				renderer.updateClearColor();
				
			}

			// flush event
			start_pos = null;
			prev_pos = null;
			curr_pos = null;
			break;

		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;

		case MotionEvent.ACTION_MOVE:
			Log.i(TAG, "ACTION_MOVE : " + prev_pos[0] + ", " + prev_pos[1]
					+ "->" + curr_pos[0] + ", " + curr_pos[1]);
			if (mode == DRAG) {
				Log.i(TAG, "DRAG_MODE : ");
				// prev_pos, curr_pos
				renderer.drag(prev_pos, curr_pos);

			} else if (mode == ZOOM) {
				Log.i(TAG, "ZOOM_MODE : ");
				curr_pos2[0] = event.getX(1);
				curr_pos2[1] = event.getY(1);

				renderer.zoom(prev_pos, prev_pos2, curr_pos, curr_pos2);

				prev_pos2 = curr_pos2;
			}
			// TODO
			prev_pos = curr_pos;

			break;

		default:
			return false;
		}
		return true;
	}
}
