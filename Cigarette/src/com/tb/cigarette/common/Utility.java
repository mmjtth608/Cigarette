package com.tb.cigarette.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.tb.cigarette.activity.R;

public class Utility {
	public static Utility utility = null;

	public static Utility getInstance() {
		if (utility == null) {
			utility = new Utility();
		}
		return utility;
	}

	/**
	 * 根据流转换字符串
	 * 
	 * @param is
	 *            输入流
	 * @return 输入流转换完毕的字符串
	 */
	public String readStream(InputStream is) {
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			int i = is.read();
			while (i != -1) {
				bo.write(i);
				i = is.read();
			}
			return bo.toString();
		} catch (IOException e) {
			return "";
		}
	}
	/**
     * 
     * @return
     */
    public static DisplayImageOptions getUserIconDisplayOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_user_icon)
                .showImageForEmptyUri(R.drawable.ic_user_icon)
                .showImageOnFail(R.drawable.ic_user_icon)
                .bitmapConfig(Bitmap.Config.RGB_565)
                // .cacheInMemory()
                .cacheOnDisc().displayer(new SimpleBitmapDisplayer())
                .imageScaleType(ImageScaleType.EXACTLY).build();
        return options;
    }

    /**
     * 
     * @return
     */
    public static DisplayImageOptions getImageDisplayOption() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_image_loading)
                .showImageForEmptyUri(R.drawable.ic_image_loading)
                .showImageOnFail(R.drawable.ic_image_loading)
                .bitmapConfig(Bitmap.Config.RGB_565)
                // .cacheInMemory()
                .cacheOnDisc().displayer(new SimpleBitmapDisplayer())
                .imageScaleType(ImageScaleType.EXACTLY).build();
        return options;
    }
}
