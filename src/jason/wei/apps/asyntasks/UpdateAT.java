package jason.wei.apps.asyntasks;

import jason.wei.apps.commons.RequestMethod;
import jason.wei.apps.notes.Note.Notes;
import jason.wei.apps.restclients.RestClient;
import jason.wei.apps.services.CustomService;

import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class UpdateAT extends AsyncTask<URL, Void, String> {
	
	private CustomService serviceUpdateText;
	private String text;
	private String url;
	private String TAG = "UpdateAT";
	
	public UpdateAT(CustomService serviceUpdateText, String text) {
		this.serviceUpdateText = serviceUpdateText;
		this.text = text;
	}

	@Override
	protected String doInBackground(URL... urls) {
		
		while(!serviceUpdateText.isNetworkAvailable()) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		url = urls[0].toString();
				
		RestClient client = new RestClient(url);
		client.AddParam(Notes.TEXT, text);
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
				serviceUpdateText.stopSelf();
				Log.i(TAG, "Stopped service");
			}
								
		} catch (JSONException e) {
			Log.e(TAG, "JSONException: " + e.getMessage(), e);
		}
	}

}
