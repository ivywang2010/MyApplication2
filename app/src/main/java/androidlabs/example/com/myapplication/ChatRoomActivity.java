package androidlabs.example.com.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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



public class ChatRoomActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "activity_chat_room";
    protected EditText chatText;
    protected static final int SENDING_MESSAGE = 1;
    protected static final int RECEIVING_MESSAGE = 2;
    protected MessageAdapter myAdapter;
    private List<Message> chatMessage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //getting the chatText field from the screen
        ListView listView = findViewById(R.id.MessagelistView);
        chatText = findViewById(R.id.editChat);


        myAdapter = new MessageAdapter(this);
        listView.setAdapter(myAdapter);

        //instantiate an object of myopener and get a database
        MyOpener mo = new MyOpener(this);
        SQLiteDatabase db = mo.getWritableDatabase();

        //query all the results from the database
        String [] columns = {mo.COL_ID, mo.COL_MESSAGE};
        Cursor results = db.query(false, mo.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int MessageColumnIndex = results.getColumnIndex(mo.COL_MESSAGE);
        int idColIndex = results.getColumnIndex(mo.COL_ID);

        //iterate over the results, return true if there is a next item
        while(results.moveToNext())
        {
            String msg = results.getString(MessageColumnIndex);

            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            chatMessage.add(new Message(msg, id));
        }

        //set sending button onclick listener
        Button btSend = findViewById(R.id.btnSend);
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adding message and message type to arrayList
                chatMessage.add(new Message(chatText.getText().toString(), SENDING_MESSAGE));
                //repeat the process of getCount() and getView()
                myAdapter.notifyDataSetChanged();
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
                chatText.setText("");
            }
        });

        //This listens for items being clicked in the list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                TextView textViewItem = (view.findViewById(R.id.chatMessage));
                String listItemText = textViewItem.getText().toString();
                Log.d("messageListView", "onItemClick: " + i + " " + l);
            }
        });
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
        //setDbId(id);

    }

    public Message(String m, long id){
        setMessages(m);
        setDbId(id);
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