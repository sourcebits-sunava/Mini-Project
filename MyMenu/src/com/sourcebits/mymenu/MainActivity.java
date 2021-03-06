package com.sourcebits.mymenu;


import java.util.ArrayList;
import java.util.List;





import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity 
{

	private PackageManager packageManager = null;
	private List<ApplicationInfo> applist = null;
	private ApplicationAdapter listadaptor = null;
	ListView listView,listViewMenu;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button button =(Button)findViewById(R.id.add);
		
		listViewMenu = (ListView) findViewById(R.id.appList);
		
		
	    
		
		packageManager = getPackageManager();
		new LoadApplications().execute();
		
		packageManager = getPackageManager();
		
		new LoadApplications().execute();
		
		button.setOnClickListener(new View.OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				Dialog dialog = new Dialog(getBaseContext());
				dialog.setContentView(R.layout.list);
 		        listView = (ListView ) dialog.findViewById(R.id.lv);
			    dialog.setCancelable(true);
			    dialog.setTitle("ListView");
			    dialog.show();
			    
				listView.setOnItemClickListener(new OnItemClickListener() 
				 {
					 	@Override
					 	public void onItemClick(AdapterView<?> listView, View view, int position, long id) 
					 	{
					 		ApplicationInfo app = applist.get(position);
							try {
								Intent intent = packageManager
										.getLaunchIntentForPackage(app.packageName);

								if (null != intent) {
									startActivity(intent);
								}
							} catch (ActivityNotFoundException e) {
								Toast.makeText(MainActivity.this, e.getMessage(),
										Toast.LENGTH_LONG).show();
							} catch (Exception e) {
								Toast.makeText(MainActivity.this, e.getMessage(),
										Toast.LENGTH_LONG).show();
							}

					 	}
					 	
				 });
				
				
				
				
			}
		});
				
		
		
		
		
		
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
		for (ApplicationInfo info : list) {
			try {
				if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
					applist.add(info);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return applist;
	}

	private class LoadApplications extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progress = null;

		@Override
		protected Void doInBackground(Void... params) {
			applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
			listadaptor = new ApplicationAdapter(MainActivity.this,
					R.layout.list_row, applist);
			

			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Void result) 
		{
			listView.setAdapter(listadaptor);
			progress.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(MainActivity.this, null,
					"Loading application info...");
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	
	}
}	
