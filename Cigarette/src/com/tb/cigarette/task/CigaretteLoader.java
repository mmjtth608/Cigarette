package com.tb.cigarette.task;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.tb.cigarette.manager.CigaretteManager;
import com.tb.cigarette.model.Cigarette;

public class CigaretteLoader extends AsyncTaskLoader<ArrayList<Cigarette>> {

	@SuppressWarnings("unused")
	private Context mContext;
	private CigaretteManager mCigaretteManager = null;
	private ArrayList<Cigarette> mData = null;

	public CigaretteLoader(Context mContext) {
		super(mContext);
		this.mContext = mContext;
		mCigaretteManager = CigaretteManager.getInstance(mContext);
	}

	@Override
	public ArrayList<Cigarette> loadInBackground() {
		ArrayList<Cigarette> cigarettes = new ArrayList<Cigarette>();
		cigarettes = mCigaretteManager.loadAllCigarette();
		return cigarettes;
	}

	@Override
	public void onCanceled(ArrayList<Cigarette> data) {
		super.onCanceled(data);
		onReleaseResources(mData);
	}

	@Override
	public void deliverResult(ArrayList<Cigarette> data) {
		super.deliverResult(data);
		if (isReset()) {
			onReleaseResources(mData);
			return;
		}
		ArrayList<Cigarette> oldData = mData;
		mData = data;
		if (isStarted()) {
			super.deliverResult(data);
		}
		if (oldData != null && oldData != data) {
			onReleaseResources(oldData);
		}
	}

	@Override
	protected void onReset() {
		onStopLoading();

		if (mData != null) {
			onReleaseResources(mData);
			mData = null;
		}
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		if (mData != null)
			deliverResult(mData);

		if (takeContentChanged() || mData == null)
			forceLoad();
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	protected void onReleaseResources(ArrayList<Cigarette> data) {

	}

}
