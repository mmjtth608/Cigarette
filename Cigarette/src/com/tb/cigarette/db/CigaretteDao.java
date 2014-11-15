package com.tb.cigarette.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tb.cigarette.impl.CigatetteService;
import com.tb.cigarette.model.Cigarette;
import com.tb.cigarette.manager.DbManager;

public class CigaretteDao implements CigatetteService {
	public static CigaretteDao cigaretteDao = null;
	public Context mContext = null;
	public DbManager dbManager = null;
	private static String FIELD_ID = "id";
	private static String FIELD_NAME = "name";
	private static String FIELD_DANGCI = "dangci";
	private static String FIELD_PINPAI = "pinpai";
	private static String FIELD_CHANDI = "chandi";
	private static String FIELD_LEIXING = "leixing";
	private static String FIELD_GUIGE = "guige";
	private static String FIELD_SHOUJIA = "shoujia";
	private static String FIELD_CHANGJIA = "changjia";
	private static String FIELD_IMG = "img";

	CigaretteDao(Context mContext) {
		this.mContext = mContext;
		this.dbManager = DbManager.getDbManager(mContext);
	}

	public static CigaretteDao getInstance(Context mContext) {
		if (cigaretteDao == null) {
			cigaretteDao = new CigaretteDao(mContext);
		}
		return cigaretteDao;
	}

	@Override
	public void insertCigarette(Cigarette cigarette) {
		String sql = "insert into " + DbManager.TABLE_CIGARETTE + " ("
				+ FIELD_NAME + "," + FIELD_DANGCI + "," + FIELD_PINPAI + ","
				+ FIELD_CHANDI + "," + FIELD_LEIXING + "," + FIELD_GUIGE + ","
				+ FIELD_SHOUJIA + "," + FIELD_CHANGJIA + "," + FIELD_IMG
				+ ") values (\'" + cigarette.getName() + "\',\'"
				+ cigarette.getDangci() + "\',\'" + cigarette.getPinpai()
				+ "\',\'" + cigarette.getChandi() + "\',\'"
				+ cigarette.getLeixing() + "\',\'" + cigarette.getGuige()
				+ "\',\'" + cigarette.getShoujia() + "\',\'"
				+ cigarette.getChangjia() + "\',\'" + cigarette.getImg()
				+ "\')";
		dbManager.databaseExeSQL(sql);
	}

	@Override
	public void insertCigarettes(List<Cigarette> cigarettes) {
		// TODO Auto-generated method stub
		for (Cigarette cigarette : cigarettes) {
			String sql = "insert into " + DbManager.TABLE_CIGARETTE + " ("
					+ FIELD_NAME + "," + FIELD_DANGCI + "," + FIELD_PINPAI
					+ "," + FIELD_CHANDI + "," + FIELD_LEIXING + ","
					+ FIELD_GUIGE + "," + FIELD_SHOUJIA + "," + FIELD_CHANGJIA
					+ "," + FIELD_IMG + ") values (\'" + cigarette.getName()
					+ "\',\'" + cigarette.getDangci() + "\',\'"
					+ cigarette.getPinpai() + "\',\'" + cigarette.getChandi()
					+ "\',\'" + cigarette.getLeixing() + "\',\'"
					+ cigarette.getGuige() + "\',\'" + cigarette.getShoujia()
					+ "\',\'" + cigarette.getChangjia() + "\',\'"
					+ cigarette.getImg() + "\')";
			dbManager.databaseExeSQL(sql);
		}
	}

	@Override
	public void updateCigarette(Cigarette cigarette) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteCigarette(Cigarette cigarette) {
		// TODO Auto-generated method stub
		String sql = "delete from " + DbManager.TABLE_CIGARETTE + " where "
				+ FIELD_ID + " = " + cigarette.getId();
		dbManager.databaseExeSQL(sql);
	}

	@Override
	public ArrayList<Cigarette> loadAllCigarette() {
		// TODO Auto-generated method stub
		ArrayList<Cigarette> cigarettes = new ArrayList<Cigarette>();
		String sql = "select * from " + DbManager.TABLE_CIGARETTE;
		SQLiteDatabase db = DbManager.getDbManager(mContext).getDb();
		Cursor mCursor = db.rawQuery(sql, null);
		while (mCursor.moveToNext()) {
			Cigarette mCigarette = getFromCursor(mCursor);
			cigarettes.add(mCigarette);
		}
		return null;
	}

	private Cigarette getFromCursor(Cursor cursor) {
		Cigarette mCigarette = new Cigarette();
		mCigarette.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
		mCigarette.setDangci(cursor.getString(cursor
				.getColumnIndex(FIELD_DANGCI)));
		mCigarette.setPinpai(cursor.getString(cursor
				.getColumnIndex(FIELD_PINPAI)));
		mCigarette.setChandi(cursor.getString(cursor
				.getColumnIndex(FIELD_CHANDI)));
		mCigarette.setLeixing(cursor.getString(cursor
				.getColumnIndex(FIELD_LEIXING)));
		mCigarette
				.setGuige(cursor.getString(cursor.getColumnIndex(FIELD_GUIGE)));
		mCigarette.setShoujia(cursor.getString(cursor
				.getColumnIndex(FIELD_SHOUJIA)));
		mCigarette.setChangjia(cursor.getString(cursor
				.getColumnIndex(FIELD_CHANGJIA)));
		mCigarette.setImg(cursor.getString(cursor.getColumnIndex(FIELD_IMG)));
		return mCigarette;
	}

	@Override
	public ArrayList<Cigarette> loadChandiCigarette() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Cigarette> loadChangjiaCigarette() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Cigarette> loadKeyCigarette(String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
