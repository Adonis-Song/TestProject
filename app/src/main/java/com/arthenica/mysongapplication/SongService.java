package com.arthenica.mysongapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class SongService extends Service {
	private static final String TAG = "SongService";

	private String fileString;

	IServiceAndCilent.Stub myBinder = new IServiceAndCilent.Stub() {
		@Override
		public void getString(String string) throws RemoteException {
			fileString = string;
		}

		@Override
		public String returnString() throws RemoteException {
			return fileString;
		}
	};


	public SongService() {
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate: ");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand: ");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind: ");
		return myBinder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
