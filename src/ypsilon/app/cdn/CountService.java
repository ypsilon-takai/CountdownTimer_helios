package ypsilon.app.cdn;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class CountService extends Service {

	private int remainTime;
	private int remainPreTime;

	private boolean counting;

	private Caller caller;
	private TickTick ticktick;


	public void onCreate () {
		remainTime = 0;
		counting = false;

		Log.d( "HLGT Debug", "CountService create()");

		caller = new Caller(this);

		ticktick = new TickTick() {
			public void onTick () {
				countDown();
			}
		};
	}

	@Override
	public IBinder onBind(Intent arg0) {

		Log.d( "HLGT Debug", "CountService onBind()");

		return csifImplement;
	}

	public CounterSvcIF.Stub csifImplement = new CounterSvcIF.Stub() {

		public boolean setTime(int time, int pretime) throws RemoteException {
			return CountService.this.setTime(time, pretime);
		}

		public void start(int time, int pretime) throws RemoteException {
			CountService.this.start(time, pretime);
		}

		public void stop() throws RemoteException {
			CountService.this.stop();
		}

		public void end() throws RemoteException {
			CountService.this.end();
		}

		public Bundle getState() throws RemoteException {
			return CountService.this.getState();
		}
	};

	private boolean setTime(int time, int pretime) {
		remainTime = time;
		remainPreTime = pretime;

		return true;
	}


	private void start(int time, int pretime) {
		this.setTime(time, pretime);
		counting = true;
		ticktick.start();
	}

	private void stop () {

		Log.d( "HLGT Debug", "CountService Stop()");

		ticktick.cancel();

		remainPreTime = -1;
		remainTime = -1;

		counting = false;
	}

	private void end () {

		Log.d( "HLGT Debug", "CountService end()");

		if (ticktick != null) {
			ticktick.cancel();
		}

		this.stopSelf();
	}

	private Bundle getState () {
		Bundle res = new Bundle();

		res.putBoolean("BUSY", counting);

		if (remainPreTime>0){
			res.putInt("REMAIN", remainPreTime);
		} else {
			res.putInt("REMAIN", remainTime);
		}

		return res;
	}

	public void countDown () {
		Intent message = new Intent ("YP_CDT_TIMECHANGE");



    	if (remainPreTime > 0) {
    		Log.d("HLGT Debug", "cd 01");
    		caller.say(remainPreTime);
    		// *** call controler
    		message.putExtra("STATE", counting);
    		message.putExtra("TIME", remainPreTime);
    		remainPreTime--;
    	} else if ( remainTime > 0){
    		Log.d("HLGT Debug", "cd 02");
        	caller.say(remainTime);
    		// *** call controler
    		message.putExtra("STATE", counting);
    		message.putExtra("TIME", remainTime);
       		remainTime--;
    	} else if ( remainTime <= 0) {
    		Log.d("HLGT Debug", "cd finish");
			caller.say("finished");
			ticktick.cancel();
			counting = false;
    		// *** call controler
    		message.putExtra("STATE", counting);
    	} else {
    		Log.d("HLGT Debug", "cd error");
        	caller.say(remainTime);
    		// *** call controler
    		message.putExtra("STATE", counting);
    		message.putExtra("TIME", remainTime);
    		remainTime--;
    	}

    	Log.d( "HLGT Debug", "CountService " + counting + " : rem=" + remainPreTime +  " rem=" + remainTime);

    	// send broadcast message
    	sendBroadcast(message);

    }


}
