package it.gas.tasker;

import it.gas.tasker.db.TaskerColumns;
import it.gas.tasker.db.TaskerProvider;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.widget.SimpleCursorAdapter;

public class CompletedFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor>,
		AdapterView.OnItemLongClickListener {
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
		View v = inflater.inflate(R.layout.activity_completed_fragment,
				container);
		ListView l = (ListView) v.findViewById(android.R.id.list);
		l.setOnItemLongClickListener(this);
		return v;
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

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, final long id) {
		Uri uri = Uri.withAppendedPath(TaskerProvider.CONTENT_URI,
				Long.toString(id));
		Cursor c = getActivity().getContentResolver().query(uri, null, null,
				null, null);
		if (!c.moveToFirst()) {
			Log.w(getActivity().getLocalClassName(),
					"Cursor is empty... WTF???");
			return false;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(c.getString(c.getColumnIndex(TaskerColumns.TITLE)));
		builder.setMessage(R.string.completed_delete);
		builder.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						deleteId(id);
					}
				});
		builder.setNegativeButton(android.R.string.no, null);
		builder.create().show();
		return true;
	}

	public void deleteId(long id) {
		Uri delUri = Uri.withAppendedPath(TaskerProvider.CONTENT_URI,
				Long.toString(id));
		int row = getActivity().getContentResolver().delete(delUri, null, null);
		if (row == 1)
			Toast.makeText(getActivity(), R.string.completed_delete_toast,
					Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getActivity(), R.string.main_setcompleted_error,
					Toast.LENGTH_SHORT).show();
	}

}
