package gbdssmp.cs103.ames.ac.gbdssmp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Message extends AppCompatActivity {


    public ListView lv;

    public DatabaseReference userDatabase;
    public TextView listUser;
    public String userdata;
    public String builtUpUsers;
    public ArrayList<String> your_array_list = new ArrayList<String>();

    public String[] topics = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // set Variables to their view asset
        lv = (ListView) findViewById(R.id.messagesList);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).

        ///////////////////////////////////////////////////////////////////////////////////////////
        //How to fetch or query data from Firebase database?
        //Step 1: Declare a database reference
        userDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        //Step 2: Set listener for database reference
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                //Write our functions, codes here
                //dataSnapShot is an object containing all the "users" information under "users" node
                //getValue() method is to collect all information
                userdata = dataSnapshot.getValue().toString();

                String allUsers = "";

                //Loop through all the users in the "users" database reference
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    //"datas" represent the each "user"
                    //Get username of each user
                    String userName = datas.getKey();

                    //Assign "username" to "builtupUsers"
                    builtUpUsers = userName;
                    allUsers = allUsers + userName + "\n";

                    //Add userName to the array list
                    your_array_list.add(builtUpUsers.toString());
                }

                //Display all userNames on the listView
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Message.this, android.R.layout.simple_list_item_1, your_array_list);
                //Insert arrayAdapter to listview
                lv.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //When database reference has error, not existing, ...
            }
        });

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);

        lv.setAdapter(arrayAdapter);

        // create action from onclick method to view each program
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrate.vibrate(120);

                MediaPlayer.create(getApplicationContext(),R.raw.open).start();

                // print out title of program
                TextView tvItemClicked = (TextView) itemClicked;

                // Explicitly use intent to open new Activity
                Intent intent = new Intent(Message.this, Messages.class);
                intent.putExtra("program_name", your_array_list.toArray(new String[0])[position]);
                startActivity(intent);
            }
        });
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Message.this, RegistrationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Bye Bye " , Toast.LENGTH_LONG).show();
        finish();
    }
}
