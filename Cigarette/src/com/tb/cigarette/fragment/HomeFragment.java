package com.tb.cigarette.fragment;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tb.cigarette.activity.MainActivity;
import com.tb.cigarette.activity.R;
import com.tb.cigarette.model.Cigarette;
import com.tb.cigarette.task.CigaretteLoader;
import com.tb.cigarette.widget.ResideMenu;

public class HomeFragment extends Fragment implements
		LoaderCallbacks<ArrayList<Cigarette>> {

	private View parentView;
	private ResideMenu resideMenu;
	private int LOADER_ID = 1001;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		parentView = inflater.inflate(R.layout.home, container, false);
		setUpViews();
		return parentView;
	}

	private void setUpViews() {
		MainActivity parentActivity = (MainActivity) getActivity();
		resideMenu = parentActivity.getResideMenu();
		reloadData();
		// add gesture operation's ignored views
		// FrameLayout ignored_view = (FrameLayout) parentView
		// .findViewById(R.id.ignored_view);
		// resideMenu.addIgnoredView(ignored_view);
	}

	@Override
	public Loader<ArrayList<Cigarette>> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new CigaretteLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Cigarette>> arg0,
			ArrayList<Cigarette> arg1) {
		// TODO Auto-generated method stub
		System.out.println(arg1.size() + "");
	}

	private void reloadData() {
		getLoaderManager().restartLoader(LOADER_ID, null, this);
	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Cigarette>> arg0) {
		// TODO Auto-generated method stub

	}

}
