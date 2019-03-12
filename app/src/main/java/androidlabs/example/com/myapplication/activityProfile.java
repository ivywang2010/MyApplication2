package androidlabs.example.com.myapplication;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class activityProfile extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String ACTIVITY_NAME = "PROFILE_ACTIVITY";
    private ImageButton mImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(ACTIVITY_NAME, "In the onCreate() event");
        setContentView(R.layout.activity_profile);
        final Intent intent = getIntent();

        if (intent != null) {
            String emailText = intent.getStringExtra("email");
            if (!TextUtils.isEmpty(emailText)) {
                EditText emailTextView = findViewById(R.id.editText2);
                emailTextView.setText(emailText);
            }
        }


        mImageButton = findViewById(R.id.imageButton);
        if (mImageButton != null) {
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }

                }
            });
        }

       Button chatButton = findViewById(R.id.btnGotoChat);
       chatButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                      Intent goToChat = new Intent();
                      goToChat.setClass(activityProfile.this,ChatRoomActivity.class);
                      startActivity(goToChat);
           }
    });

        Button button = findViewById(R.id.btnGotoToolbar);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent goToToolbar= new Intent();
                goToToolbar.setClass(activityProfile.this,TestToolBar.class);
                startActivity(goToToolbar);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(ACTIVITY_NAME, "In the onStart() event");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In the onResume() event");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(ACTIVITY_NAME, "In the onPause() event");
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ACTIVITY_NAME, "In the onDestroy() event");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(ACTIVITY_NAME, "In the onActivityResult() event");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageButton.setImageBitmap(imageBitmap);
        }
    }



}


