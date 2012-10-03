package it.gas.tasker;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class PrefActivity extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	@TargetApi(11)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		addPreferencesFromResource(R.xml.pref_main);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
