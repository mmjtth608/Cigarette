package com.tb.cigarette.fragment;

import com.tb.cigarette.activity.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("NewApi")
public final class LoadingFragment extends Fragment {
	private static final String KEY_CONTENT = "LoadingFragment:Content";
	private int mContent;
	private View contentView = null;

	public static LoadingFragment newInstance(int content) {
		LoadingFragment fragment = new LoadingFragment();
		fragment.mContent = content;
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getInt(KEY_CONTENT);
		}

		if (contentView != null) {
			((ViewGroup) contentView.getParent()).removeView(contentView);
			return contentView;
		}
		contentView = inflater.inflate(R.layout.item_loading, null);
		LinearLayout layout = (LinearLayout) contentView
				.findViewById(R.id.ll_splash);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		layout.setGravity(Gravity.CENTER);
		layout.setBackground(getActivity().getResources().getDrawable(mContent));
		return contentView;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_CONTENT, mContent);
	}
}
