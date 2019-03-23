package androidlabs.example.com.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.widget.Toast;
import android.util.Log;



public class ChatRoomActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "activity_chat_room";
    protected EditText chatText;
    protected static final int SENDING_MESSAGE = 1;
    protected static final int RECEIVING_MESSAGE = 2;
    protected MessageAdapter myAdapter;
    private List<Message> chatMessage = new ArrayList<>();
    protected static MyOpener mo;
    Cursor results;
    protected static SQLiteDatabase db;
    String[] arguments = new String[] { mo.COL_ID, mo.COL_MESSAGE,mo.COL_TYPE};

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_TYPE = "TYPE";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //instantiate an object of myopener and get a database
         mo = new MyOpener(this);
        db = mo.getWritableDatabase();

        //query all the results from the database
        String [] columns = {mo.COL_ID, mo.COL_MESSAGE,mo.COL_TYPE};
        results = db.query(false, mo.TABLE_NAME, columns, null, null, null, null, null, null);

       //  printCursor(results);

        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(mo.COL_MESSAGE);
        int idColIndex = results.getColumnIndex(mo.COL_ID);
        int idColType = results.getColumnIndex(mo.COL_TYPE);

        //iterate over the results, return true if there is a next item
        while(results.moveToNext())
        {
            String msg = results.getString(messageColumnIndex);

            long id = results.getLong(idColIndex);

            int type = results.getInt(idColType);


            //add the new Contact to the array list:
            chatMessage.add(new Message(msg, id,type));
        }

        printCursor(results);
         //getting the chatText field from the screen
          ListView listView = findViewById(R.id.MessagelistView);
          chatText = findViewById(R.id.editChat);


        myAdapter = new MessageAdapter(this);
        listView.setAdapter(myAdapter);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;
        //set sending button onclick listener
        Button btSend = findViewById(R.id.btnSend);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adding message and message type to arrayList
                chatMessage.add(new Message(chatText.getText().toString(), SENDING_MESSAGE));
                //repeat the process of getCount() and getView()
                myAdapter.notifyDataSetChanged();
                ContentValues cv = new ContentValues();
                cv.put(mo.COL_MESSAGE,chatText.getText().toString());
                cv.put(mo.COL_TYPE,SENDING_MESSAGE);
                db.insert(mo.TABLE_NAME,"NULLCOLUMN",cv);

                chatText.setText("");
                results = db.query(false, mo.TABLE_NAME,
                        arguments, null, null, null, null, null, null);
                 chatText.setText("");
            }
        });

        //set receiving button onclick listener
        Button btReceive = findViewById(R.id.btnReceive);

        btReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adding message and message type to arrayList
                chatMessage.add(new Message(chatText.getText().toString(), RECEIVING_MESSAGE));
                //repeat the process of getCount() and getView()
                myAdapter.notifyDataSetChanged();
                ContentValues cv = new ContentValues();
                cv.put(mo.COL_MESSAGE,chatText.getText().toString());
                cv.put(mo.COL_TYPE,RECEIVING_MESSAGE);
                db.insert(mo.TABLE_NAME,"NULLCOLUMN",cv);
                chatText.setText("");
                results = db.query(false, mo.TABLE_NAME,
                        arguments, null, null, null, null, null, null);
                chatText.setText("");
            }
        });

     //This listens for items being clicked in the list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Context context = view.getContext();
                TextView textViewItem = (view.findViewById(R.id.chatMessage));
                String listItemText = textViewItem.getText().toString();

                //ab 8 based on class example
                Bundle dataToPass = new Bundle();
                dataToPass.putString(ITEM_SELECTED, chatMessage.get(position).getMessages());
                dataToPass.putInt(ITEM_TYPE,chatMessage.get(position).getMsgType());
                dataToPass.putInt(ITEM_POSITION, position);
                dataToPass.putLong(ITEM_ID, chatMessage.get(position).getDbId());

                if(isTablet)
                {
                    FragmentDetail dFragment = new FragmentDetail(); //add a DetailFragment
                    dFragment.setArguments( dataToPass ); //pass it a bundle for information
                    dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                            .addToBackStack("AnyName") //make the back button undo the transaction
                            .commit(); //actually load the fragment.
                }
                else //isPhone
                {
                    Intent nextActivity = new Intent(ChatRoomActivity.this, empty_fragment.class);
                    nextActivity.putExtras(dataToPass); //send data to next activity
                    startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
                }
            }

            //end lab 8 part

        });
    }

    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(ITEM_ID, 0);
                deleteMessageId((int)id);
            }
        }
    }

    public void deleteMessageId(int id)
    {
        Log.i("Delete this message:" , " id="+id);
        chatMessage.remove(id);
        myAdapter.notifyDataSetChanged();
    }

    private void printCursor(Cursor c) {

        Log.i(ACTIVITY_NAME, "version: " + db.getVersion()+
        ", number of cursor: " + c.getColumnCount() +
        ", name of cursor: " + c.getColumnNames() +
        ",number of results: " + c.getCount());

        c.moveToFirst();

        while(!c.isAfterLast()){

            Log.i(ACTIVITY_NAME,"MESSAGE: "+ c.getString(c.getColumnIndex(mo.COL_MESSAGE)));
            c.moveToNext();

        }
    }


    //inner class MessageAdapter which extends array adapter taking Message object as parameter
    public class MessageAdapter extends ArrayAdapter<Message>// {
    {

        public MessageAdapter(Context ctx) {

            super(ctx, 0);
        }

        //return the size of Message arraylist
        public int getCount() {

            return chatMessage.size();
        }

        //get the position
        public long getItemId(int position) {
            return position;

        }

        //get the message item  from the list with specified index
        public Message getItem(int position) {

            return chatMessage.get(position);
        }

        //get the view
        public View getView(int position, View oldView, ViewGroup parent) {
            LayoutInflater inflater = ChatRoomActivity.this.getLayoutInflater();
            View result = null;

            if (getItem(position).getMsgType() == RECEIVING_MESSAGE) {
                result = inflater.inflate(R.layout.activity_single_row_receiving, null);
            }
            if (getItem(position).getMsgType() == SENDING_MESSAGE) {
                result = inflater.inflate(R.layout.activity_single_row_sending, null);
            }

            TextView message = (TextView) result.findViewById(R.id.chatMessage);

            message.setText(getItem(position).getMessages());

            return result;
        }



    }
}


 // class message to store the information of messages received and sent
class Message {
    private String messages;
    private int msgType;
    private long dbID;

    public Message(){
       messages = null;
       msgType = 0;
    }

    public Message(String m, int type) {
        setMessages(m);
        setMsgType(type);


    }

    public Message(String m, long id){
        setMessages(m);
        setDbId(id);
    }

     public Message(String m, long id, int type){
         setMessages(m);
         setDbId(id);
         setMsgType(type);
     }

    String getMessages() {
        return messages;
    }

    private void setMessages(String messages) {
        this.messages = messages;
    }

    int getMsgType() {
        return msgType;
    }

    private void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    long getDbId(){
        return dbID;
    }

    private void setDbId(long id){
        this.dbID=id;
    }



}