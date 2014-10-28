package com.tb.cigarette.adapter;

import com.tb.cigarette.fragment.LoadingFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class BaseFragmentAdapter extends FragmentPagerAdapter {
	protected View[] loadingViews;

	public BaseFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public BaseFragmentAdapter(FragmentManager fm, View[] loadingViews) {
		super(fm);
		this.loadingViews = loadingViews;
	}

	public void setViews(View[] loadingViews) {
		this.loadingViews = loadingViews;
	}

	@Override
	public Fragment getItem(int position) {
		return LoadingFragment.newInstance(loadingViews[position]);
	}

	@Override
	public int getCount() {
		return loadingViews.length;
	}
}