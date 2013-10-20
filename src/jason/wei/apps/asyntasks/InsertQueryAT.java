package jason.wei.apps.asyntasks;

import jason.wei.apps.MainActivity;
import jason.wei.apps.R;
import jason.wei.apps.notes.Note;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.TextView;

public class InsertQueryAT extends AsyncTask<Void, Void, Cursor> {
	
	private MainActivity mainActivity;
	private ContentValues insertValues;
	private Uri insertUri;
	private String[] queryProjection;
	
	public InsertQueryAT(MainActivity mainActivity, ContentValues cv, Uri uri, String[] projection) {
		this.mainActivity = mainActivity;
		insertValues = cv;
		insertUri    = uri;
		queryProjection = projection;
	}

	@Override
	protected Cursor doInBackground(Void... params) {
		
		Uri insertURI = mainActivity.getContentResolver().insert(insertUri, insertValues);		
		Cursor cursor = mainActivity.getContentResolver().query(insertURI, queryProjection, "", null, "_id DESC");
				
		return cursor;
	}
	
	@Override
	protected void onPostExecute(Cursor cursor) {
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			TextView tv1 = (TextView) mainActivity.findViewById(R.id.tv1);
			tv1.setText(cursor.getString(MainActivity.COLUMN_INDEX_TEXT));
			
			cursor.moveToNext();
		}
	}

}
