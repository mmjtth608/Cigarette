package com.tb.cigarette.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tb.cigarette.impl.CigatetteService;
import com.tb.cigarette.model.Cigarette;
import com.tb.cigarette.model.SearchParams;
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
	private static String FIELD_SHARE = "share";

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

	@SuppressWarnings("static-access")
	@Override
	public void updateCigarette(Cigarette cigarette) {
		// TODO Auto-generated method stub
		// String sql = "update from " + DbManager.TABLE_CIGARETTE + " where "
		// + FIELD_ID + " = " + cigarette.getId();
		// dbManager.databaseExeSQL(sql);

		ContentValues values = getContentValues(cigarette);
		dbManager.getDbManager(mContext).db.update(DbManager.TABLE_CIGARETTE,
				values, FIELD_ID + "='" + cigarette.getId() + "'", null);
	}

	@Override
	public void deleteCigarette(Cigarette cigarette) {
		// TODO Auto-generated method stub
		String sql = "delete from " + DbManager.TABLE_CIGARETTE + " where "
				+ FIELD_ID + " = " + cigarette.getId();
		dbManager.databaseExeSQL(sql);
	}

	private ContentValues getContentValues(Cigarette cigarette) {
		ContentValues values = new ContentValues();
		values.put(FIELD_CHANDI, cigarette.getChandi());
		values.put(FIELD_NAME, cigarette.getName());
		values.put(FIELD_CHANGJIA, cigarette.getChangjia());
		values.put(FIELD_DANGCI, cigarette.getDangci());
		values.put(FIELD_GUIGE, cigarette.getGuige());
		values.put(FIELD_IMG, cigarette.getImg());
		values.put(FIELD_LEIXING, cigarette.getLeixing());
		values.put(FIELD_PINPAI, cigarette.getPinpai());
		values.put(FIELD_SHARE, cigarette.getShare());
		values.put(FIELD_SHOUJIA, cigarette.getShoujia());
		return values;
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
		return cigarettes;
	}

	private Cigarette getFromCursor(Cursor cursor) {
		Cigarette mCigarette = new Cigarette();
		mCigarette.setId(cursor.getInt(cursor.getColumnIndex(FIELD_ID)));
		mCigarette.setName(cursor.getString(cursor.getColumnIndex(FIELD_NAME)));
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
		mCigarette.setShare(cursor.getInt(cursor.getColumnIndex(FIELD_SHARE)));
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

	@Override
	public ArrayList<String> loadPinpai() {
		ArrayList<String> cigarettes = new ArrayList<String>();
		String sql = "select DISTINCT " + FIELD_PINPAI + " from "
				+ DbManager.TABLE_CIGARETTE;
		SQLiteDatabase db = DbManager.getDbManager(mContext).getDb();
		Cursor mCursor = db.rawQuery(sql, null);
		while (mCursor.moveToNext()) {
			cigarettes.add(mCursor.getString(mCursor
					.getColumnIndex(FIELD_PINPAI)));
		}
		return cigarettes;
	}

	@Override
	public ArrayList<String> loadDangci() {
		ArrayList<String> cigarettes = new ArrayList<String>();
		String sql = "select DISTINCT " + FIELD_DANGCI + " from "
				+ DbManager.TABLE_CIGARETTE;
		SQLiteDatabase db = DbManager.getDbManager(mContext).getDb();
		Cursor mCursor = db.rawQuery(sql, null);
		while (mCursor.moveToNext()) {
			cigarettes.add(mCursor.getString(mCursor
					.getColumnIndex(FIELD_DANGCI)));
		}
		return cigarettes;
	}

	@Override
	public ArrayList<String> loadChandi() {
		ArrayList<String> cigarettes = new ArrayList<String>();
		String sql = "select DISTINCT " + FIELD_CHANDI + " from "
				+ DbManager.TABLE_CIGARETTE;
		SQLiteDatabase db = DbManager.getDbManager(mContext).getDb();
		Cursor mCursor = db.rawQuery(sql, null);
		while (mCursor.moveToNext()) {
			cigarettes.add(mCursor.getString(mCursor
					.getColumnIndex(FIELD_CHANDI)));
		}
		return cigarettes;
	}

	@Override
	public ArrayList<Cigarette> loadSearchCigarette(SearchParams searchParams) {
		// TODO Auto-generated method stub
		String sql = "select * from " + DbManager.TABLE_CIGARETTE;
		if (searchParams.getKey() != null) {
			sql = "select * from " + DbManager.TABLE_CIGARETTE + " where "
					+ FIELD_CHANDI + " like \'%" + searchParams.getKey()
					+ "%\' or " + FIELD_NAME + " like \'%"
					+ searchParams.getKey() + "%\' or " + FIELD_DANGCI
					+ " like \'%" + searchParams.getKey() + "%\' or "
					+ FIELD_PINPAI + " like \'%" + searchParams.getKey()
					+ "%\' or " + FIELD_LEIXING + " like \'%"
					+ searchParams.getKey() + "%\' or " + FIELD_GUIGE
					+ " like \'%" + searchParams.getKey() + "%\' or "
					+ FIELD_SHOUJIA + " like \'%" + searchParams.getKey()
					+ "%\'";
		} else if (searchParams.getPinpai() != null) {
			sql = "select * from " + DbManager.TABLE_CIGARETTE + " where "
					+ FIELD_PINPAI + " = \'" + searchParams.getPinpai() + "\'";
		} else if (searchParams.getDangci() != null) {
			sql = "select * from " + DbManager.TABLE_CIGARETTE + " where "
					+ FIELD_DANGCI + " = \'" + searchParams.getDangci() + "\'";
		} else if (searchParams.getChandi() != null) {
			sql = "select * from " + DbManager.TABLE_CIGARETTE + " where "
					+ FIELD_CHANDI + " = \'" + searchParams.getChandi() + "\'";
		}
		ArrayList<Cigarette> cigarettes = new ArrayList<Cigarette>();
		SQLiteDatabase db = DbManager.getDbManager(mContext).getDb();
		Cursor mCursor = db.rawQuery(sql, null);
		while (mCursor.moveToNext()) {
			Cigarette mCigarette = getFromCursor(mCursor);
			cigarettes.add(mCigarette);
		}
		return cigarettes;
	}

}
