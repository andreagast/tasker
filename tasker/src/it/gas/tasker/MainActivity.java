package it.gas.tasker;

import it.gas.tasker.db.TaskerColumns;
import it.gas.tasker.db.TaskerProvider;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements
		DialogInterface.OnClickListener {
	private AlertDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		case R.id.menu_about:
			i = new Intent(this, AboutActivity.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			dialog.dismiss();
			saveNewTask();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			dialog.dismiss();
			break;
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

	private void saveNewTask() {
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

}
