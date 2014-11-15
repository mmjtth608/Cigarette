package com.tb.cigarette.activity;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.tb.cigarette.adapter.BaseFragmentAdapter;
import com.tb.cigarette.common.Utility;
import com.tb.cigarette.db.CigaretteDao;
import com.tb.cigarette.model.Cigarette;
import com.tb.cigarette.widget.DirectionalViewPager;

public class LoadingActivity extends FragmentActivity {
	private int[] loadingItems = { R.drawable.loading1, R.drawable.loading2,
			R.drawable.loading3 };
	private DirectionalViewPager pager;
	private Button btn_gotomain = null;
//	private ActionBar actionBar = null;
	TranslateAnimation mShowAction = null;
	TranslateAnimation mHiddenAction = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		actionBar = this.getActionBar();
		setContentView(R.layout.activity_loading);
		init();
		bindEvents();
	}

	private void init() {
		btn_gotomain = (Button) findViewById(R.id.btn_gotomain);
		pager = (DirectionalViewPager) findViewById(R.id.vp_loading);
		pager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(),
				loadingItems));
		pager.setOrientation(DirectionalViewPager.VERTICAL);// 设置垂直滑动
		// pager.setOrientation(DirectionalViewPager.HORIZONTAL);//设置水平滑动
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-3.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(1000);
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-3.0f);
		mHiddenAction.setDuration(1000);
	}

	private void bindEvents() {
		// TODO Auto-generated method stub
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == loadingItems.length - 1) {
					btn_gotomain.startAnimation(mShowAction);
					btn_gotomain.setVisibility(View.VISIBLE);
				} else {
//					btn_gotomain.startAnimation(mHiddenAction);
					btn_gotomain.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
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

}
