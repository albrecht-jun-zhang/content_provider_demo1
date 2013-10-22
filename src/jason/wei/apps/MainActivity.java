package jason.wei.apps;



import jason.wei.apps.asyntasks.InsertQueryAT;
import jason.wei.apps.notes.Note.Notes;
import jason.wei.apps.services.CustomService;
import jason.wei.apps.services.QueryService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements OnClickListener, 
LoaderManager.LoaderCallbacks<Cursor> {

	public static String TAG = "MainActivity";
	public static final String[] PROJECTION = new String[] {
        Notes._ID, // 0
        Notes.TITLE, // 1
        Notes.TEXT // 2
	};
	public static final int COLUMN_INDEX_TEXT = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ContentValues cv = new ContentValues();
		cv.put(Notes.TEXT, "Text1");
		
		InsertQueryAT insertQueryAT = new InsertQueryAT(this, cv, Notes.CONTENT_URI, PROJECTION);
		insertQueryAT.execute();
		
		Button btn1 = (Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(this);
		
		Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
		btnRefresh.setOnClickListener(this);
		
		// Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getSupportLoaderManager().initLoader(0, null, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.btn1:
			ContentValues cv = new ContentValues();
			cv.put(Notes.TEXT, "Text2");
			String[] whereArgs = {"1"};
			int updatedRows = getContentResolver().update(Notes.CONTENT_URI, cv, Notes._ID + " = ?", whereArgs);
			Log.i(TAG, Integer.toString(updatedRows));
			
			Cursor cursor2 = getContentResolver().query(Notes.CONTENT_URI, PROJECTION, Notes._ID + " = ?", whereArgs, null);
			cursor2.moveToFirst();
			while(!cursor2.isAfterLast()) {
				TextView tv1 = (TextView) findViewById(R.id.tv1);
				tv1.setText(cursor2.getString(MainActivity.COLUMN_INDEX_TEXT));
				
				cursor2.moveToNext();
			}
			
			// Start service
			Intent intent = new Intent(this, CustomService.class);		
			intent.putExtra(Notes.TEXT, "Text2");
			startService(intent);
			
			break;
		case R.id.btnRefresh:
			String[] whereRefreshArgs = {"1"};			
			
			Cursor cursorRefresh = getContentResolver().query(Notes.CONTENT_URI, PROJECTION, Notes._ID + " = ?", whereRefreshArgs, null);
			cursorRefresh.moveToFirst();
			while(!cursorRefresh.isAfterLast()) {
				TextView tv1 = (TextView) findViewById(R.id.tv1);
				tv1.setText(cursorRefresh.getString(MainActivity.COLUMN_INDEX_TEXT));
				
				cursorRefresh.moveToNext();
			}
			
			// Start service
			Intent intentQuery = new Intent(this, QueryService.class);		
			intentQuery.putExtra(Notes._ID, 1);
			startService(intentQuery);
			break;
		}
		
	}

	
	@Override
	public android.support.v4.content.Loader<Cursor> onCreateLoader(int arg0,
			Bundle arg1) {
		String[] whereRefreshArgs = {"1"};					
		return new CursorLoader(this, Notes.CONTENT_URI, PROJECTION, Notes._ID + " = ?", whereRefreshArgs, null);
	}


	@Override
	public void onLoadFinished(android.support.v4.content.Loader<Cursor> arg0,
			Cursor cursor) {
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			TextView tv1 = (TextView) findViewById(R.id.tv1);
			tv1.setText(cursor.getString(MainActivity.COLUMN_INDEX_TEXT));
			
			cursor.moveToNext();
		}
		
	}

	@Override
	public void onLoaderReset(android.support.v4.content.Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}	

}
