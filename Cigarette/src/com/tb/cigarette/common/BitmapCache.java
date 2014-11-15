package com.tb.cigarette.common;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import android.graphics.Bitmap;
import android.widget.ImageView;

/***
 * 图片缓存
 * 
 * (单利模式)
 * 
 * @author zhangjia
 * 
 */
public class BitmapCache {

	static private BitmapCache cache;
	// 软引用
	private HashMap<String, SoftReference<ImageView>> imageCache;

	public BitmapCache() {
		imageCache = new HashMap<String, SoftReference<ImageView>>();
	}

	/**
	 * 取得缓存器实例
	 */
	public static BitmapCache getInstance() {
		if (cache == null) {
			cache = new BitmapCache();
		}
		return cache;

	}

	/***
	 * 获取缓存图片
	 * 
	 * @param key
	 *            image name
	 * @return
	 */
	public ImageView getBitmap(String key) {
		if (imageCache.containsKey(key)) {
			SoftReference<ImageView> reference = imageCache.get(key);
			ImageView bitmap = reference.get();
			if (bitmap != null)
				return bitmap;
		}
		return null;

	}

	/***
	 * 将图片添加到软引用中
	 * 
	 * @param bitmap
	 * @param key
	 */
	public void putSoftReference(ImageView bitmap, String key) {
		imageCache.put(key, new SoftReference<ImageView>(bitmap));

	}

}
