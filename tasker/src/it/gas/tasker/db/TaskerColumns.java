package it.gas.tasker.db;

import android.provider.BaseColumns;

public interface TaskerColumns extends BaseColumns {
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "descr";
	public static final String COMPLETE = "complete";
	public static final String CREATED = "createtime";
	public static final String MODIFIED = "modifytime";
}
