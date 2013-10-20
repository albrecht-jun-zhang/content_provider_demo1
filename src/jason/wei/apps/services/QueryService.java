package jason.wei.apps.services;

import java.net.MalformedURLException;
import java.net.URL;

import jason.wei.apps.asyntasks.InsertQueryAT;
import jason.wei.apps.asyntasks.QueryAT;
import jason.wei.apps.asyntasks.UpdateAT;
import jason.wei.apps.constants.Consts;
import jason.wei.apps.notes.Note.Notes;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class QueryService extends AbsService{

	private static String TAG = "QueryService";
	
	public void onCreate() {
				
	}
	
	@Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
		int _id = intent.getExtras().getInt(Notes._ID);
		QueryAT queryAT = new QueryAT(this, _id);
		try {
			queryAT.execute(new URL(Consts.SIGN_IN_QUERY));
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