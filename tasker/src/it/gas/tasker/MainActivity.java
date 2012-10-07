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
		LoaderManager.LoaderCallbacks<Cursor>, DialogInterface.OnClickListener {
	private SimpleCursorAdapter adapter;
	private AlertDialog dialog;
	private SharedPreferences pref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pref = PreferenceManager.getDefaultSharedPreferences(this);

		final String[] FROM = { TaskerColumns.TITLE, TaskerColumns.DESCRIPTION };
		final int[] TO = { android.R.id.text1, android.R.id.text2 };
		adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, null, FROM, TO, 0);
		setListAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = null;
		switch (item.getItemId()) {
		case R.id.menu_add:
			addPopup(item);
			return true;
		case R.id.menu_settings:
			i = new Intent(this, PrefActivity.class);
			startActivity(i);
			return true;
		case R.id.menu_completed:
			i = new Intent(this, CompletedActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void addPopup(MenuItem mi) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		// .setTitle(R.string.menu_add);
		b.setCancelable(true);
		b.setView(getLayoutInflater().inflate(R.layout.activity_dialog_add,
				null));
		b.setNegativeButton(getString(R.string.cancel), this);
		b.setPositiveButton(getString(R.string.ok), this);
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
		String sort = TaskerColumns._ID +  " DESC";
		// show completed pref
		String selection = null;
		boolean b = pref.getBoolean(PrefActivity.PREF_COMPLETED, false);
		Log.d(this.getLocalClassName(), "pref_completed: " + b);
		if (!b)
			selection = TaskerColumns.COMPLETE + " = 'FALSE'";
		// execute
		String[] projection = { TaskerColumns._ID, TaskerColumns.TITLE,
				TaskerColumns.DESCRIPTION };
		return new CursorLoader(this, TaskerProvider.CONTENT_URI, projection,
				selection, null, sort);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			dialog.dismiss();
			save();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			dialog.dismiss();
			break;
		}
	}

}
