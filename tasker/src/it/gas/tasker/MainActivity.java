package it.gas.tasker;

import it.gas.tasker.db.TaskerColumns;
import it.gas.tasker.db.TaskerProvider;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.app.LoaderManager;

public class MainActivity extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private SimpleCursorAdapter adapter;
	private AlertDialog dialog;
	private SharedPreferences pref;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		getLoaderManager().initLoader(0, null, this);
		final String[] FROM = { TaskerColumns.TITLE, TaskerColumns.DESCRIPTION };
		final int[] TO = { android.R.id.text1, android.R.id.text2 };
		adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, null, FROM, TO, 0);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add:
			addPopup(item);
			return true;
		case R.id.menu_settings:
			Intent i = new Intent(this, PrefActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void addPopup(MenuItem mi) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle(R.string.menu_add);
		b.setCancelable(true);
		b.setView(getLayoutInflater().inflate(R.layout.activity_dialog_add,
				null));
		b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		b.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				save();
			}
		});
		dialog = b.create();
		dialog.show();
	}

	private void save() {
		ContentValues values = new ContentValues();
		TextView v = (TextView) dialog.findViewById(R.id.addTitle);
		String s = v.getText().toString();
		values.put(TaskerColumns.TITLE, s);
		v = (TextView) dialog.findViewById(R.id.addDescription);
		s = v.getText().toString();
		values.put(TaskerColumns.DESCRIPTION, s);
		getContentResolver().insert(TaskerProvider.CONTENT_URI, values);
		getContentResolver().notifyChange(TaskerProvider.CONTENT_URI, null);
	}

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		//show completed pref		
		String selection = null;
		boolean b = pref.getBoolean(PrefActivity.PREF_COMPLETED, true);
		Log.d(this.getLocalClassName(), "pref_completed: " + b);
		if(! b)
			selection = TaskerColumns.COMPLETE + " = 'FALSE'";
		//execute
		return new CursorLoader(this, TaskerProvider.CONTENT_URI, null,
				selection, null, null);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

}
