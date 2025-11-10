package ro.pub.cs.systems.eim.colocviu1_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Colocviu1_2MainActivity extends AppCompatActivity {
    // Widgets
    private TextView allTerms;
    private EditText nextTerm;
    private Button addButton;
    private Button computeButton;

    private int sum;

    private int serviceStatus = Constants.SERVICE_STOPPED;
    private IntentFilter intentFilter = new IntentFilter();

    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: Extrage datele din intent
            String date = intent.getStringExtra("DATE");
//            String timestamp = intent.getStringExtra("TIMESTAMP");
            int receivedSum = intent.getIntExtra(Constants.SUM, -1);

            // TODO: Actualizeaza UI (textView, listView, etc)
            Log.d("COLOCVIU", "BroadcastReceiver - Primit: " + date + " " + receivedSum);
            Toast.makeText(context, "Broadcast received: " + date + " " + receivedSum,
                    Toast.LENGTH_LONG).show();
        }
    }

    // Create button listener class
    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // Define used variables here
            String allTermsString = allTerms.getText().toString();
            String nextTermString = nextTerm.getText().toString();

            // Button logic code goes here
            if (view.getId() == R.id.add_button) {
                if(allTermsString.equals("")) {
                    allTermsString = nextTermString;
                } else {
                    allTermsString += "+" + nextTermString;
                }
                allTerms.setText(allTermsString);
            } else if (view.getId() == R.id.navigate_to_secondary_activity_button) {
                Intent intent = new Intent(getApplicationContext(), Colocviu1_2SecondaryActivity.class);

                intent.putExtra(Constants.ALL_TERMS_STRING, allTermsString);
                // Start activity and wait for its result
                startActivityForResult(intent, Constants.SECONDARY_ACTIVITY_REQUEST_CODE);
            }


            // Check threshold condition
            if (sum > Constants.NUMBER_THRESHOLD
                    && serviceStatus == Constants.SERVICE_STOPPED) {
                Log.d("COLOCVIU", "Starting service");

                // Create intent to start service
                Intent intent = new Intent(getApplicationContext(), Colocviu1_2Service.class);
                intent.putExtra(Constants.SUM, sum);

                // Start service
                getApplicationContext().startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("COLOCVIU", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_2_main);

        // Initialize widgets by id
        allTerms = findViewById(R.id.all_terms);
        nextTerm = findViewById(R.id.next_term);
        addButton = findViewById(R.id.add_button);
        computeButton = findViewById(R.id.navigate_to_secondary_activity_button);

        // Assign listener to buttons
        addButton.setOnClickListener(buttonClickListener);
        computeButton.setOnClickListener(buttonClickListener);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Constants.SAVED_SUM)) {
                sum = savedInstanceState.getInt(Constants.SAVED_SUM);
            } else {
                sum = 0;
            }
        }
        intentFilter.addAction(Constants.ACTION_STRING);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("COLOCVIU", "onSaveInstanceState()");

        // Save current sum val
        outState.putInt(Constants.SAVED_SUM, sum);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from SecondaryActivity
        if (requestCode == Constants.SECONDARY_ACTIVITY_REQUEST_CODE) {
            // Show activity result code
            sum = intent.getIntExtra(Constants.SUM, -1);
            Toast.makeText(this, "Compute returned " + sum,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("COLOCVIU", "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(Constants.SAVED_SUM)) {
            sum = savedInstanceState.getInt(Constants.SAVED_SUM);
        } else {
            sum = 0;
        }

        Log.d("COLOCVIU", "Restored sum, it is: " + sum);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d("COLOCVIU", "onRestart()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("COLOCVIU", "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("COLOCVIU", "onResume()");
        registerReceiver(messageBroadcastReceiver, intentFilter, RECEIVER_EXPORTED);

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("COLOCVIU", "onPause()");
        unregisterReceiver(messageBroadcastReceiver);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("COLOCVIU", "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("COLOCVIU", "onDestroy()");
        // Stop service when activity is destroyed
        Intent intent = new Intent(this, Colocviu1_2Service.class);
        stopService(intent);
    }
}

