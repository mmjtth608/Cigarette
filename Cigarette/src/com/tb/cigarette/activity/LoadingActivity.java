package com.tb.cigarette.activity;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.baidu.frontia.Frontia;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tb.cigarette.common.Utility;
import com.tb.cigarette.db.CigaretteDao;
import com.tb.cigarette.model.Cigarette;

public class LoadingActivity extends FragmentActivity implements
		OnClickListener {
	private Button btn_gotomain = null;
	// private ActionBar actionBar = null;
	TranslateAnimation mShowAction = null;
	TranslateAnimation mHiddenAction = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// actionBar = this.getActionBar();
		setContentView(R.layout.activity_loading);
		init();
		initImageLoader(this);
		boolean isInit = Frontia.init(getApplicationContext(),
				"qavMGEsGGe0OaS68cSdUxCCI");
		if (isInit) {// Frontia is successfully initialized.
			// Use Frontia
		}
		bindEvents();
	}

	private void init() {
		btn_gotomain = (Button) findViewById(R.id.btn_gotomain);
		// pager.setOrientation(DirectionalViewPager.HORIZONTAL);//设置水平滑动
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-3.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(3000);
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-3.0f);
		mHiddenAction.setDuration(3000);
	}

	private void bindEvents() {
		// TODO Auto-generated method stub
		btn_gotomain.setOnClickListener(this);
		btn_gotomain.startAnimation(mShowAction);
	}

	// 前期录入数据库数据
	public void initData() {
		InputStream s = getResources().openRawResource(R.raw.ca);
		String ssString = Utility.getInstance().readStream(s);
		List<Cigarette> list = new LinkedList<Cigarette>();
		try {
			JSONArray jsonArray = new JSONObject(ssString).getJSONObject(
					"alltobacco").getJSONArray("tobacco");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Cigarette cigarette = new Cigarette();
				cigarette.setId(Integer.parseInt(jsonObject.getString("-id")));
				cigarette.setName(jsonObject.getString("name"));
				cigarette.setDangci(jsonObject.getString("dangci"));
				cigarette.setPinpai(jsonObject.getString("pinpai"));
				cigarette.setChandi(jsonObject.getString("chandi"));
				cigarette.setLeixing(jsonObject.getString("leixing"));
				cigarette.setGuige(jsonObject.getString("guige"));
				cigarette.setShoujia(jsonObject.getString("shoujia"));
				cigarette.setChangjia(jsonObject.getString("changjia"));
				cigarette.setImg(jsonObject.getString("img"));
				list.add(cigarette);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CigaretteDao.getInstance(this).insertCigarettes(list);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_gotomain:
			Intent intent = new Intent();
			intent.setClass(LoadingActivity.this, HomeActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.animation_right_in,
					R.anim.animation_left_out);
			finish();
			break;

		default:
			break;
		}
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .enableLogging()
				.imageDownloader(new BaseImageDownloader(context))
				// default
				.imageDecoder(new BaseImageDecoder(true))
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.threadPoolSize(1)
				// .discCache(
				// new UnlimitedDiscCache(new File(FileUtils
				// .getImagesPath())))// default
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

}
