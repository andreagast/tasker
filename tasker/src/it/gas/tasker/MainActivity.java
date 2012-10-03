package it.gas.tasker;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

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

	public void addPopup(MenuItem mi) {
    	AlertDialog.Builder b = new AlertDialog.Builder(this);
    	b.setTitle(R.string.app_name);
    	b.setCancelable(false);
    	b.setMessage("WIP (work in progress)");
    	b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
    	b.create().show();
    }
    
}
