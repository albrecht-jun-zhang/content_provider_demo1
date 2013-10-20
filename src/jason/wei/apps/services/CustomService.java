package jason.wei.apps.services;

import java.net.MalformedURLException;
import java.net.URL;

import jason.wei.apps.asyntasks.InsertQueryAT;
import jason.wei.apps.asyntasks.UpdateAT;
import jason.wei.apps.constants.Consts;
import jason.wei.apps.notes.Note.Notes;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CustomService extends AbsService{

	private String text;
	private static String TAG = "CustomService";
	
	public void onCreate() {
				
	}
	
	@Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
		text = intent.getExtras().getString(Notes.TEXT);
		UpdateAT updateAT = new UpdateAT(this, text);
		try {
			updateAT.execute(new URL(Consts.SIGN_IN_UPDATE_TEXT));
		} catch (MalformedURLException e) {
			Log.e(TAG, "MalformedURLException: " + e.getMessage(), e);
		}
		
		
	    //TODO do something useful
	    return Service.START_REDELIVER_INTENT;
	  }

	  @Override
	  public IBinder onBind(Intent intent) {
	  //TODO for communication return IBinder implementation
	    return null;
	  }
	  
	  public boolean isNetworkAvailable() {
		  return super.isNetworkAvailable();
	  }
}