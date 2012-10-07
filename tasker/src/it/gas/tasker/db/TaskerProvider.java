package it.gas.tasker.db;

import android.content.ContentProvider;
import android.content.ContentResolver;
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
		case 0: // all the db
			break;
		case 1: // only a specified row
			selection = "_ID = ?";
			selectionArgs = new String[1];
			selectionArgs[0] = uri.getLastPathSegment();
			break;
		default:
			return 0;
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		int del = db.delete(TABLE, selection, selectionArgs);
		db.close();
		getContext().getContentResolver().notifyChange(uri, null);
		return del;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case 0:
			return ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd." + AUTHORITY
					+ "." + TABLE;
		case 1:
			return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + AUTHORITY
					+ "." + TABLE;
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
			getContext().getContentResolver().notifyChange(uri, null);
			return Uri.withAppendedPath(CONTENT_URI, Long.toString(id));
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
			break;
		case 1:
			selection = "_ID = ?";
			selectionArgs = new String[1];
			selectionArgs[0] = uri.getLastPathSegment();
			break;
		default:
			return null;
		}
		Cursor c = db.query(TABLE, projection, selection, selectionArgs,
				null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		switch (uriMatcher.match(uri)) {
		case 0:
			break;
		case 1:
			selection = "_ID = ?";
			selectionArgs = new String[1];
			selectionArgs[0] = uri.getLastPathSegment();
			break;
		default:
			return 0;
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		int row = db.update(TABLE, values, selection, selectionArgs);
		db.close();
		getContext().getContentResolver().notifyChange(uri, null);
		return row;
	}

}
