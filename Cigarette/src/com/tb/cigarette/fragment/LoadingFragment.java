package com.tb.cigarette.fragment;

import com.tb.cigarette.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public final class LoadingFragment extends Fragment {
	// private static final String KEY_CONTENT = "LoadingFragment:Content";
	private View mContent;
	private View contentView;

	public static LoadingFragment newInstance(View content) {
		LoadingFragment fragment = new LoadingFragment();
		fragment.mContent = content;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// if ((savedInstanceState != null)
		// && savedInstanceState.containsKey(KEY_CONTENT)) {
		// mContent = savedInstanceState.getString(KEY_CONTENT);
		// }

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
		// layout.addView(mContent);
		layout.setBackground(((ImageView) mContent).getDrawable());
		return contentView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// outState.putString(KEY_CONTENT, mContent);
	}
}
