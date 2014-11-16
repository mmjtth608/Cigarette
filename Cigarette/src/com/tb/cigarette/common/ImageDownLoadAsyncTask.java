package com.tb.cigarette.common;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 异步下载图片
 * 
 * @author sy
 * 
 */
public class ImageDownLoadAsyncTask extends AsyncTask<Void, Void, Bitmap> {
	private String imagePath;
	private ImageView imageView;
	private Context context;
	private AssetManager assetManager;
	private int Image_width = 80;// 显示图片的宽度
	private LinearLayout progressbar;
	private TextView loadtext;

	/**
	 * 构造方法
	 * 
	 * @param context
	 * @param imagePath
	 * @param imageView
	 */
	public ImageDownLoadAsyncTask(Context context, String imagePath,
			ImageView imageView, int Image_width) {
		this.imagePath = imagePath;
		this.imageView = imageView;
		this.context = context;
		assetManager = this.context.getAssets();
		this.Image_width = Image_width;
	}

	public void setLoadtext(TextView loadtext) {
		this.loadtext = loadtext;
	}

	public void setProgressbar(LinearLayout progressbar) {
		this.progressbar = progressbar;
	}

	@Override
	protected Bitmap doInBackground(Void... params) {

		try {
			if (BitmapCache.getInstance().getBitmap(imagePath) == null) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = false;// 这里只返回bitmap的大小
				InputStream inputStream = assetManager.open(imagePath);
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null,
						options);
				BitmapCache.getInstance().putSoftReference(bitmap, imagePath);
				return bitmap;
			} else {
				return BitmapCache.getInstance().getBitmap(imagePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Bitmap drawable) {
		// TODO Auto-generated method stub
		super.onPostExecute(drawable);
		if (drawable != null) {
			LayoutParams layoutParams = imageView.getLayoutParams();
			int height = drawable.getHeight();// 获取图片的高度.
			int width = drawable.getWidth();// 获取图片的宽度
			layoutParams.height = (height * Image_width) / width;

			imageView.setLayoutParams(layoutParams);
			imageView.setImageBitmap(drawable);
		}
		if (progressbar != null && progressbar.isShown()
				|| (loadtext != null && loadtext.isShown())) {
			progressbar.setVisibility(View.GONE);
			loadtext.setVisibility(View.GONE);
		}

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (loadtext != null && !loadtext.isShown()) {
			loadtext.setVisibility(View.VISIBLE);
		}

	}
}