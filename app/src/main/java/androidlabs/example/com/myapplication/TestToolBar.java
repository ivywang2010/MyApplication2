package androidlabs.example.com.myapplication;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolBar extends AppCompatActivity {

    private Toolbar myToolbar;
    private String s = "this is initial message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tool_bar);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem choice) {

        if(choice.getItemId()==R.id.menuItem1){
            Toast.makeText(this,s,Toast.LENGTH_LONG).show();

        }else if(choice.getItemId()==R.id.menuItem2){
            {
                View middle = getLayoutInflater().inflate(R.layout.activity_newmessage, null);

                final EditText text = (EditText)middle.findViewById(R.id.type_here);

                //btn.setOnClickListener( clk -> {et.setText("You clicked my button!");});

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Message")
                        .setPositiveButton("positive", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                s = text.getText().toString();
                            }
                        })
                        .setNegativeButton("negative", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).setView(middle);

                builder.create().show();
            }
        }
            else if(choice.getItemId()==R.id.menuItem3){

            Snackbar sb = Snackbar.make(myToolbar, "Hello world", Snackbar.LENGTH_LONG)
                    .setAction("finish", e ->finish());
            sb.show();

        }
                else if(choice.getItemId()==R.id.menuItem4){
            Toast.makeText(this,"you clicked on the overflow menu ",Toast.LENGTH_LONG).show();
        }
        return true;
    }

}


