package jason.wei.apps.asyntasks;

import jason.wei.apps.MainActivity;
import jason.wei.apps.commons.RequestMethod;
import jason.wei.apps.notes.Note.Notes;
import jason.wei.apps.restclients.RestClient;
import jason.wei.apps.services.CustomService;
import jason.wei.apps.services.QueryService;

import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

public class QueryAT extends AsyncTask<URL, Void, String> {
	
	private QueryService serviceQuery;
	private int _id;
	private String url;
	private String TAG = "QueryAT";
	
	public QueryAT(QueryService serviceQuery, int _id) {
		this.serviceQuery = serviceQuery;
		this._id = _id;
	}

	@Override
	protected String doInBackground(URL... urls) {
		
		while(!serviceQuery.isNetworkAvailable()) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		url = urls[0].toString();
				
		RestClient client = new RestClient(url);
		client.AddParam(Notes._ID, Integer.toString(_id));
		// Add Headers
		client.AddHeader("deviceType","Android"); // Device type
		client.AddHeader("deviceVersion","2.0"); // Device OS version
		client.AddHeader("language", "es-es");	// Language of the Android client
		
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
		    Log.e(TAG, "exception", e);		    
		}
		
		String json = client.getResponse();
		Log.i(TAG, "Success: " + json);
		return json;
	}
	
	protected void onPostExecute(String jsonName) {				
		// Transform the String into a JSONObject
		try {
			JSONObject jsonObjName = new JSONObject(jsonName);
			boolean success = jsonObjName.getBoolean("success");
			if(!success) {
				String details = jsonObjName.get("details").toString();
				Log.i(TAG, details);
			} else {
				
				String text = jsonObjName.get("text").toString();
				
				// Update ContentProvider
				ContentValues cv = new ContentValues();
				cv.put(Notes.TEXT, text);
				String[] selectionArgs = {Integer.toString(_id)}; 
				serviceQuery.getContentResolver().update(Notes.CONTENT_URI, cv, Notes._ID + " = ?", selectionArgs);				
				
				serviceQuery.stopSelf();
				Log.i(TAG, "Stopped service");
			}
								
		} catch (JSONException e) {
			Log.e(TAG, "JSONException: " + e.getMessage(), e);
		}
	}

}
