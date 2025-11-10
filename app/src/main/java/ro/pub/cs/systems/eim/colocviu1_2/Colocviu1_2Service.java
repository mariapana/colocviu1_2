package ro.pub.cs.systems.eim.colocviu1_2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
public class Colocviu1_2Service extends Service {
    // Background thread instance
    private ProcessingThread processingThread = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Extract data sent via Intent
        int sum = intent.getIntExtra(Constants.SUM, -1);

        // Start the background processing thread
        processingThread = new ProcessingThread(this, sum);
        processingThread.start();

        // Ensures the intent is redelivered if system restarts the service
        return Service.START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Not a bound service -> return null
        return null;
    }

    @Override
    public void onDestroy() {
        // Stop background thread before service is destroyed
        processingThread.stopThread();
    }

}
