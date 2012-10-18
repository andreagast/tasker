package it.gas.tasker;

import it.gas.tasker.db.TaskerProvider;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class CompletedActivity extends FragmentActivity implements DialogInterface.OnClickListener {

	@TargetApi(11)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_completed);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_completed, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.deleteAll:
			deleteAll();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			int rows = getContentResolver().delete(TaskerProvider.CONTENT_URI, null, null);
			Toast.makeText(this, getString(R.string.completed_toast, rows), Toast.LENGTH_SHORT).show();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			//Toast.makeText(this, "negative", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void deleteAll() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.completed_deleteAll);
		builder.setMessage(R.string.completed_deleteAll_question);
		builder.setPositiveButton(android.R.string.yes, this);
		builder.setNegativeButton(android.R.string.no, this);
		builder.create().show();
	}

}
