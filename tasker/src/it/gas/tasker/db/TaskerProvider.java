package it.gas.tasker.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class TaskerProvider extends ContentProvider {
	public static final String AUTHORITY = "it.gas.tasker.provider";
	public static final String TABLE = "task";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + TABLE);
	private TaskerHelper helper;
	private static final UriMatcher uriMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);

	static {
		uriMatcher.addURI(AUTHORITY, TABLE, 0);
		uriMatcher.addURI(AUTHORITY, TABLE + "/#", 1);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case 0:
			SQLiteDatabase db = helper.getWritableDatabase();
			int del = db.delete(TABLE, selection, selectionArgs);
			return del;
		case 1:
			return 0;
		default:
			return 0;
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case 0:
			return "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + TABLE;
		case 1:
			return "vnd.android.cursor.item/vnd." + AUTHORITY + "." + TABLE;
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (uriMatcher.match(uri)) {
		case 0:
			SQLiteDatabase db = helper.getWritableDatabase();
			long id = db.insert(TABLE, null, values);
			return Uri.parse("content://" + AUTHORITY + "/" + TABLE + "/" + id);
		case 1:
		default:
			return null;
		}
	}

	@Override
	public boolean onCreate() {
		helper = new TaskerHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = helper.getWritableDatabase();
		switch (uriMatcher.match(uri)) {
		case 0:
		case 1:
			Cursor c = db.query(TABLE, projection, selection, selectionArgs,
					null, null, sortOrder);
			c.setNotificationUri(getContext().getContentResolver(), uri);
			return c;
		default:
			return null;
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case 0:
			SQLiteDatabase db = helper.getWritableDatabase();
			return db.update(TABLE, values, selection, selectionArgs);
		case 1:
		default:
			return 0;
		}
	}

}
