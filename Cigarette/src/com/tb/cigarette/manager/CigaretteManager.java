package com.tb.cigarette.manager;

import java.util.ArrayList;

import com.tb.cigarette.db.CigaretteDao;
import com.tb.cigarette.impl.CigatetteService;
import com.tb.cigarette.model.Cigarette;
import com.tb.cigarette.model.SearchParams;

import android.content.Context;

public class CigaretteManager {
	public static CigaretteManager mCigaretteManager = null;
	public Context mContext = null;
	public CigatetteService mCigaretteDao = null;

	public CigaretteManager(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		mCigaretteDao = CigaretteDao.getInstance(mContext);
	}

	public static CigaretteManager getInstance(Context mContext) {
		if (mCigaretteManager == null) {
			mCigaretteManager = new CigaretteManager(mContext);
		}
		return mCigaretteManager;
	}

	public ArrayList<Cigarette> loadAllCigarette() {
		return mCigaretteDao.loadAllCigarette();
	}

	public void updateCigarette(Cigarette cigarette) {
		mCigaretteDao.updateCigarette(cigarette);
	}

	public ArrayList<String> loadPinpai() {
		return mCigaretteDao.loadPinpai();
	}

	public ArrayList<String> loadDangci() {
		return mCigaretteDao.loadDangci();
	}

	public ArrayList<String> loadChandi() {
		return mCigaretteDao.loadChandi();
	}

	public ArrayList<Cigarette> loadSearchCigarette(SearchParams searchParams) {
		return mCigaretteDao.loadSearchCigarette(searchParams);
	}

}
