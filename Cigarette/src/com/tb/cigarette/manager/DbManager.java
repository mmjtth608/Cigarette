package com.tb.cigarette.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DbManager {
	public static DbManager dbManager = null;
	public SQLiteDatabase db = null;
	public static final String DB_NAME = "cigarette.db";// 数据库文件
	public static final String TABLE_CIGARETTE = "cigarette";// 景点表

	public String path = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + File.separator
			+ "com.tb.cigarette.activity" + File.separator + DB_NAME;

	public Context mContext = null;

	public DbManager(Context context) {
		this.mContext = context;
		copyDbFile();
		this.db = getDb();
	}

	public static DbManager getDbManager(Context context) {
		if (dbManager == null) {
			dbManager = new DbManager(context);
		}
		return dbManager;
	}

	public void copyDbFile() {
		File db = new File(path);
		if (!db.exists()) {
			try {
				InputStream is = mContext.getAssets().open(DB_NAME);
				FileOutputStream fos = new FileOutputStream(db);
				int len = -1;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
					fos.flush();
				}
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	public SQLiteDatabase getDb() {
		if (db == null) {
			db = mContext
					.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
		}
		return db;
	}

	/**
	 * 数据库操作一条SQL语句
	 */
	public void databaseExeSQL(String sql) {
		synchronized (db) {
			this.db.execSQL(sql);
		}
	}
}
