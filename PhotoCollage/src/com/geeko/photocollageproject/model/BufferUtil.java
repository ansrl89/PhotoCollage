package com.geeko.photocollageproject.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class BufferUtil {
	
	public static FloatBuffer makeFloatBuffer(float[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}
	
	public static IntBuffer makeIntBuffer(int[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
		bb.order(ByteOrder.nativeOrder());
		IntBuffer Ib = bb.asIntBuffer();
		Ib.put(arr);
		Ib.position(0);
		return Ib;
	}
	
	public static ShortBuffer makeShortBuffer(short[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*2);
		bb.order(ByteOrder.nativeOrder());
		ShortBuffer Ib = bb.asShortBuffer();
		Ib.put(arr);
		Ib.position(0);
		return Ib;
	}
	
	public static ByteBuffer makeByteBuffer(byte[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length);
		bb.order(ByteOrder.nativeOrder());
		bb.put(arr);
		bb.position(0);
		return bb;
	}
		
	public static FloatBuffer makeFloatBuffer(int size) {
		ByteBuffer bb = ByteBuffer.allocateDirect(size*4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.position(0);
		return fb;
	}
		
	public static IntBuffer makeIntBuffer(int size) {
		ByteBuffer bb = ByteBuffer.allocateDirect(size*4);
		bb.order(ByteOrder.nativeOrder());
		IntBuffer Ib = bb.asIntBuffer();
		Ib.position(0);
		return Ib;
	}
	
	public static ShortBuffer makeShortBuffer(int size) {
		ByteBuffer bb = ByteBuffer.allocateDirect(size*2);
		bb.order(ByteOrder.nativeOrder());
		ShortBuffer Ib = bb.asShortBuffer();
		Ib.position(0);
		return Ib;
	}
	
	public static ByteBuffer makeByteBuffer(int size) {
		ByteBuffer bb = ByteBuffer.allocateDirect(size);
		bb.position(0);
		return bb;
	}	
}
