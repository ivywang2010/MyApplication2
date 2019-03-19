package androidlabs.example.com.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final String USER_PREFS_NAME = "PREFERENCE_KEY";
    private static final String USER_EMAIL="USER_EMAIL_KEY";
    EditText EmailText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

        //setContentView(R.layout.actvity_main_linear);
        //setContentView(R.layout.activity_main_grid);
        //setContentView(R.layout.activity_main_relative);

        SharedPreferences prefs;
        prefs = getSharedPreferences(USER_PREFS_NAME,MODE_PRIVATE);
        EmailText = findViewById((R.id.editText));
        String restoredText = prefs.getString(USER_EMAIL,null);

       if(restoredText != null){
           if(EmailText != null){
               EmailText.setText(restoredText);
           }
       }

       Button button = findViewById(R.id.buttonLogin);
       button.setOnClickListener(new View.OnClickListener(){
           public void onClick(View v){
               Intent intent = new Intent();
               intent.setClass(MainActivity.this,activityProfile.class);
               if(EmailText != null){
                   String emailText = EmailText.getText().toString();
                   intent.putExtra("email",emailText);
               }
               startActivity(intent);
           }
       });

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(EmailText != null){
            String emailText = EmailText.getText().toString();
            SharedPreferences.Editor editor = getSharedPreferences(USER_PREFS_NAME,MODE_PRIVATE).edit();
            editor.putString(USER_EMAIL,emailText);
            editor.commit();
        }
    }
}

