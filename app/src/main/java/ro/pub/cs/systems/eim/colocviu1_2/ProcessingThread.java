package ro.pub.cs.systems.eim.colocviu1_2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Date;
import java.util.Random;

// ProcessingThread.java
public class ProcessingThread extends Thread {

    private Context context = null;
    private int sum = 0;
    private boolean isRunning = true;

    public ProcessingThread(Context context, int sum) {
        this.context = context;
        this.sum = sum;

    }

    @Override
    public void run() {
        Log.d("ProcessingThread", "Thread inceput");

        if (isRunning) {
            try {
                Thread.sleep(2000);

                sendBroadcast();
                Log.d("COLOCVIU", "Broadcast trimis");

            } catch (Exception e) {
                Log.e("COLOCVIU", "Eroare: " + e.getMessage());
            }
        }

        Log.d("COLOCVIU", "Thread oprit");
    }

    private void sendBroadcast() {
        Intent intent = new Intent();

        // TODO: Seteaza action-ul si defineste ACTION_STRING
        intent.setAction(Constants.ACTION_STRING);

        intent.putExtra("DATE", new Date(System.currentTimeMillis()).toString());
//        intent.putExtra("TIMESTAMP", System.currentTimeMillis());
        intent.putExtra(Constants.SUM, sum);

        // Trimite broadcast-ul
        context.sendBroadcast(intent);
    }

    public void stopThread() { isRunning = false; }
}
