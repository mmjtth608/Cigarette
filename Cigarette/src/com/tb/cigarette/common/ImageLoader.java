package com.tb.cigarette.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

/**
 * 双缓存图片异步加载
 * 
 * @author thinkpad
 * 
 */
public class ImageLoader {
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	// 线程池
	ExecutorService executorService;
	private Context mContext = null;

	// 当进入listview时默认的图片，可换成你自己的默认图片
	private int stub_id;
	private int threadPools = 5;// 线程池数目
	@SuppressWarnings("unused")
	private int connectTimeout = 30000;// 连接超时时间
	@SuppressWarnings("unused")
	private int readTimeout = 30000;// 读取超时时间
	private String fileName = "LazyList";
	private boolean all = false;

	public ImageLoader(int defaultImg, Context context, boolean all) {
		this.mContext = context;
		this.stub_id = defaultImg;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(threadPools);
		this.all = all;
	}

	// 最主要的方法
	public void DisplayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		// 先从内存缓存中查找

		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bb(bitmap));
			imageView.setImageBitmap(bitmap);
		} else {
			// 若没有的话则开启新线程加载图片
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// 先从文件缓存中查找是否有
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// 最后从指定的url中下载图片
		try {
			@SuppressWarnings("unused")
			Bitmap bitmap = null;
			// URL imageUrl = new URL(url);
			// HttpURLConnection conn = (HttpURLConnection) imageUrl
			// .openConnection();
			// conn.setConnectTimeout(connectTimeout);
			// conn.setReadTimeout(readTimeout);
			// conn.setInstanceFollowRedirects(true);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;// 这里只返回bitmap的大小
			InputStream is = mContext.getAssets().open(url);
			b = BitmapFactory.decodeStream(is, null, options);
			// InputStream is = conn.getInputStream();
			// OutputStream os = new FileOutputStream(f);
			// CopyStream(is, os);
			// os.close();
			// bitmap = decodeFile(f);
			return b;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			if (all) {
				return BitmapFactory.decodeStream(new FileInputStream(f), null,
						null);
			} else {
				return BitmapFactory.decodeStream(new FileInputStream(f), null,
						o2);
			}
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * 防止图片错位
	 * 
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// 用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				photoToLoad.imageView.setImageBitmap(bb(bitmap));
				photoToLoad.imageView.setImageBitmap(bitmap);
			} else
				photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public class MemoryCache {
		// 放入缓存时是个同步操作
		// LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU
		// 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
		private Map<String, Bitmap> cache = Collections
				.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f,
						true));
		// 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
		private long size = 0;// current allocated size
		// 缓存只能占用的最大堆内存
		private long limit = 1000000;// max memory in bytes

		public MemoryCache() {
			// use 25% of available heap size
			setLimit(Runtime.getRuntime().maxMemory() / 4);
		}

		public void setLimit(long new_limit) {
			limit = new_limit;
			// DebugUtil.logV(null, "MemoryCache will use up to " + limit /
			// 1024.
			// / 1024. + "MB");
		}

		public Bitmap get(String id) {
			try {
				if (!cache.containsKey(id))
					return null;
				return cache.get(id);
			} catch (NullPointerException ex) {
				return null;
			}
		}

		public void put(String id, Bitmap bitmap) {
			try {
				if (cache.containsKey(id))
					size -= getSizeInBytes(cache.get(id));
				cache.put(id, bitmap);
				size += getSizeInBytes(bitmap);
				checkSize();
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}

		/**
		 * 严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
		 * 
		 */
		private void checkSize() {
			// DebugUtil.logV(null,
			// "cache size=" + size + " length=" + cache.size());
			if (size > limit) {
				// 先遍历最近最少使用的元素
				Iterator<Entry<String, Bitmap>> iter = cache.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Entry<String, Bitmap> entry = iter.next();
					size -= getSizeInBytes(entry.getValue());
					iter.remove();
					if (size <= limit)
						break;
				}
				// DebugUtil.logV(null, "Clean cache. New size " +
				// cache.size());
			}
		}

		public void clear() {
			cache.clear();
		}

		/**
		 * 图片占用的内存
		 * 
		 * @param bitmap
		 * @return
		 */
		long getSizeInBytes(Bitmap bitmap) {
			if (bitmap == null)
				return 0;
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	}

	public class FileCache {

		private File cacheDir;

		public FileCache(Context context) {
			// 如果有SD卡则在SD卡中建一个LazyList的目录存放缓存的图片
			// 没有SD卡就放在系统的缓存目录中
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED))
				cacheDir = new File(
						android.os.Environment.getExternalStorageDirectory(),
						fileName);
			else
				cacheDir = context.getCacheDir();
			if (!cacheDir.exists())
				cacheDir.mkdirs();
		}

		public File getFile(String url) {
			// 将url的hashCode作为缓存的文件名
			String filename = String.valueOf(url.hashCode());
			// Another possible solution
			// String filename = URLEncoder.encode(url);
			File f = new File(cacheDir, filename);
			return f;

		}

		public void clear() {
			File[] files = cacheDir.listFiles();
			if (files == null)
				return;
			for (File f : files)
				f.delete();
		}

	}

	public Bitmap bb(Bitmap bitmap) {
		// int width = bitmap.getWidth();
		// int height = bitmap.getHeight();
		// // 设置想要的大小
		// // 计算缩放比例
		// float scaleWidth = ((float) width) / width;
		// float scaleHeight = ((float) height) / height;
		// // 取得想要缩放的matrix参数
		// Matrix matrix = new Matrix();
		// matrix.postScale(scaleWidth, scaleHeight);
		// // 得到新的图片
		// bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
		// true);
		return bitmap;
	}
}