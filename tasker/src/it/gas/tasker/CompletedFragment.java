package it.gas.tasker;

import it.gas.tasker.db.TaskerColumns;
import it.gas.tasker.db.TaskerProvider;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.SimpleCursorAdapter;

public class CompletedFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String[] FROM = { TaskerColumns.TITLE };
		final int[] TO = { android.R.id.text1 };
		adapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_1, null, FROM, TO, 0);
		setListAdapter(adapter);

		getActivity().getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.activity_completed_fragment, container);
	}

	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String selection = TaskerColumns.COMPLETE + " = 1";
		String[] projection = { TaskerColumns._ID, TaskerColumns.TITLE };
		return new CursorLoader(getActivity(), TaskerProvider.CONTENT_URI,
				projection, selection, null, null);
	}

	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		adapter.swapCursor(arg1);
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

}
