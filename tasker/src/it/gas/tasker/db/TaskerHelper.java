package it.gas.tasker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskerHelper extends SQLiteOpenHelper {
	private static final String DBNAME = "tasker.db";
	private static final int DBVERSION = 1;
	
	
	public TaskerHelper(Context cx) {
		super(cx, DBNAME, null, DBVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
		// TODO Auto-generated method stub

	}

}
