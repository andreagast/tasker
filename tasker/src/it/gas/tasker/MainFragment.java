package it.gas.tasker;

import it.gas.tasker.db.TaskerColumns;
import it.gas.tasker.db.TaskerProvider;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor>,
		AdapterView.OnItemLongClickListener {
	private SharedPreferences pref;
	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

		final String[] FROM = { TaskerColumns.TITLE, TaskerColumns.DESCRIPTION };
		final int[] TO = { android.R.id.text1, android.R.id.text2 };
		adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_2, null, FROM, TO, 0);
		setListAdapter(adapter);
		getActivity().getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_main_fragment, container);
		ListView l = (ListView) v.findViewById(android.R.id.list);
		l.setOnItemLongClickListener(this);
		return v;
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String sort = TaskerColumns._ID + " DESC";
		// show completed pref
		String selection = null;
		boolean b = pref.getBoolean(PrefActivity.PREF_COMPLETED, false);
		Log.d(getActivity().getLocalClassName(), "pref_completed: " + b);
		if (!b)
			selection = TaskerColumns.COMPLETE + " = 0";
		// execute
		String[] projection = { TaskerColumns._ID, TaskerColumns.TITLE,
				TaskerColumns.DESCRIPTION };
		return new CursorLoader(getActivity(), TaskerProvider.CONTENT_URI,
				projection, selection, null, sort);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, final long id) {
		Uri uri = Uri.withAppendedPath(TaskerProvider.CONTENT_URI, "" + id);
		Cursor c = getActivity().getContentResolver().query(uri, null, null,
				null, null);
		if (!c.moveToFirst()) {
			Log.w(getActivity().getLocalClassName(),
					"Cursor is empty... WTF???");
			return false;
		}
		AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
		build.setTitle(c.getString(c.getColumnIndex(TaskerColumns.TITLE)));
		build.setMessage(R.string.main_setcompleted_question);
		build.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						markTaskAsCompleted(id);
						dialog.dismiss();
					}
				});
		build.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		build.create().show();
		c.close();

		return true;
	}

	private void markTaskAsCompleted(long id) {
		Uri uri = Uri.withAppendedPath(TaskerProvider.CONTENT_URI, "" + id);
		ContentValues values = new ContentValues();
		values.put(TaskerColumns.COMPLETE, 1);
		int row = getActivity().getContentResolver().update(uri, values, null,
				null);
		if (row > 0)
			Toast.makeText(getActivity(), R.string.main_setcompleted_toast,
					Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getActivity(), R.string.main_setcompleted_error,
					Toast.LENGTH_SHORT).show();
	}

}
