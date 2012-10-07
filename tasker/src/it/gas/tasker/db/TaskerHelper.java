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
		StringBuilder build = new StringBuilder();
		build.append("CREATE TABLE TASK (");
		build.append(TaskerColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
		build.append(TaskerColumns.TITLE + " VARCHAR(30) NOT NULL,");
		build.append(TaskerColumns.DESCRIPTION + " VARCHAR(255),");
		build.append(TaskerColumns.COMPLETE + " BOOLEAN DEFAULT 0,");
		build.append(TaskerColumns.CREATED + " DATETIME DEFAULT CURRENT_TIMESTAMP,");
		build.append(TaskerColumns.MODIFIED + " DATETIME DEFAULT CURRENT_TIMESTAMP");
		build.append(");");
		db.execSQL(build.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
	}

}
