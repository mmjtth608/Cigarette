package com.tb.cigarette.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tb.cigarette.adapter.BaseFragmentAdapter;
import com.tb.cigarette.db.CigaretteDao;
import com.tb.cigarette.model.Cigarette;
import com.tb.cigarette.widget.DirectionalViewPager;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

public class LoadingActivity extends FragmentActivity {
	private ImageView[] loadingItems;
	private DirectionalViewPager pager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		loadingItems = new ImageView[3];
		for (int i = 0; i < loadingItems.length; i++) {
			loadingItems[i] = new ImageView(this);
		}
		loadingItems[0].setImageResource(R.drawable.loading1);
		loadingItems[1].setImageResource(R.drawable.loading2);
		loadingItems[2].setImageResource(R.drawable.loading3);
		pager = (DirectionalViewPager) findViewById(R.id.vp_loading);
		pager.setAdapter(new BaseFragmentAdapter(getSupportFragmentManager(),
				loadingItems));
		pager.setOrientation(DirectionalViewPager.VERTICAL);
		// pager.setOrientation(DirectionalViewPager.HORIZONTAL);
		// InputStream s = getResources().openRawResource(R.raw.ca);
		// String ssString = readStream(s);
		// List<Cigarette> list = new LinkedList<Cigarette>();
		// try {
		// JSONArray jsonArray = new JSONObject(ssString).getJSONObject(
		// "alltobacco").getJSONArray("tobacco");
		// for (int i = 0; i < jsonArray.length(); i++) {
		// JSONObject jsonObject = jsonArray.getJSONObject(i);
		// Cigarette cigarette = new Cigarette();
		// cigarette.setId(Integer.parseInt(jsonObject.getString("-id")));
		// cigarette.setName(jsonObject.getString("name"));
		// cigarette.setDangci(jsonObject.getString("dangci"));
		// cigarette.setPinpai(jsonObject.getString("pinpai"));
		// cigarette.setChandi(jsonObject.getString("chandi"));
		// cigarette.setLeixing(jsonObject.getString("leixing"));
		// cigarette.setGuige(jsonObject.getString("guige"));
		// cigarette.setShoujia(jsonObject.getString("shoujia"));
		// cigarette.setChangjia(jsonObject.getString("changjia"));
		// cigarette.setImg(jsonObject.getString("img"));
		// list.add(cigarette);
		// }
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// CigaretteDao.getInstance(this).insertCigarettes(list);
	}

	private String readStream(InputStream is) {
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

}
