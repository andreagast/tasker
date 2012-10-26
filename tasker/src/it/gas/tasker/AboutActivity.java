package it.gas.tasker;

import android.annotation.TargetApi;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class AboutActivity extends FragmentActivity {

    @TargetApi(11)
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        	getActionBar().setDisplayHomeAsUpEnabled(true);
        TextView v = (TextView) findViewById(R.id.about_title);
        try {
        	//retrieve the package version
        	PackageManager pacMan = getPackageManager();
			PackageInfo info = pacMan.getPackageInfo(getPackageName(), 0);
			v.setText(getString(R.string.app_name) + " v" + info.versionName);
		} catch (NameNotFoundException e) {
			Log.w(getLocalClassName(), e.getMessage());
			v.setText(R.string.app_name);
		}
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

}
