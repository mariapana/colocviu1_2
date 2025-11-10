package ro.pub.cs.systems.eim.colocviu1_2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Colocviu1_2SecondaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int sum = -1;

        Intent intent = getIntent();
        if (intent != null) {
            String allTermsString = intent.getStringExtra(Constants.ALL_TERMS_STRING);
            sum = 0;

            String[] terms = allTermsString.split("\\+");
            for (String term : terms) {
                sum += Integer.parseInt(term);
            }
        }
        Log.d("COLOCVIU", "Sum is: " + sum);


        // Send result to main activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.SUM, sum);

        // Return to main activity
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
