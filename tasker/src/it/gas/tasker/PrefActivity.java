package it.gas.tasker;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

public class PrefActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	public static final String PREF_COMPLETED = "pref_completed";

	@SuppressWarnings("deprecation")
	@Override
	@TargetApi(11)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		addPreferencesFromResource(R.xml.pref_main);
		restoreSummary();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			/*Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);*/
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings("deprecation")
	public void onSharedPreferenceChanged(SharedPreferences sPref, String key) {
		if (key.equals(PREF_COMPLETED)) {
			Preference pref = findPreference(PREF_COMPLETED);
			if (sPref.getBoolean(PREF_COMPLETED, false)) {
				pref.setSummary(R.string.pref_completed_on);
			} else {
				pref.setSummary(R.string.pref_completed_off);
			}
			
		}
	}
	
	@SuppressWarnings("deprecation")
	private void restoreSummary() {
		SharedPreferences sPref = getPreferenceScreen().getSharedPreferences();
		Preference pref = findPreference(PREF_COMPLETED);
		if (sPref.getBoolean(PREF_COMPLETED, false)) {
			pref.setSummary(R.string.pref_completed_on);
		} else {
			pref.setSummary(R.string.pref_completed_off);
		}
	}

}
