package it.gas.tasker;

import it.gas.tasker.db.TaskerColumns;
import it.gas.tasker.db.TaskerProvider;
import android.os.Bundle;
import android.app.ListActivity;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
import android.support.v4.app.NavUtils;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;

public class CompletedActivity extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_completed);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		final String[] FROM = { TaskerColumns.TITLE };
		final int[] TO = { android.R.id.text1 };
		adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, null, FROM, TO, 0);
		setListAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String selection = TaskerColumns.COMPLETE + " = 'TRUE'";
		String[] projection = { TaskerColumns._ID, TaskerColumns.TITLE };
		return new CursorLoader(this, TaskerProvider.CONTENT_URI, projection,
				selection, null, null);
	}

	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		adapter.swapCursor(arg1);
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

}
