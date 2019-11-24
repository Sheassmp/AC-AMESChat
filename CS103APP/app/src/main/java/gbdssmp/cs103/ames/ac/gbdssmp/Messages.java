package gbdssmp.cs103.ames.ac.gbdssmp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class Messages extends AppCompatActivity {

    private FloatingActionButton sendBtn;
    public EditText input;

    private FirebaseListAdapter<ChatMessage> adapter, adapterReceive;
    private ListView listOfMessages;
    private TextView selected_program;

    private String program_name, name, currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        program_name = getIntent().getExtras().getString("program_name");


        selected_program = (TextView) findViewById(R.id.selected_program);
        sendBtn = (FloatingActionButton) findViewById(R.id.sendBtn);
        input = (EditText) findViewById(R.id.input);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrate.vibrate(120);

                MediaPlayer.create(getApplicationContext(),R.raw.open).start();

                if (!input.getText().toString().isEmpty()) {

                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Messaging")
                            .child(program_name = getIntent()
                                    .getExtras()
                                    .getString("program_name") + "_" + currentUser)
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName()
                            )
                    );

                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Messaging")
                            .child( currentUser + "_" + getIntent()
                                    .getExtras()
                                    .getString( "program_name"))
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(), currentUser));

                    input.setText("");
                }
            }
        });

        listOfMessages = (ListView) findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this,
                ChatMessage.class,
                R.layout.message,
                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Messaging")
                        .child(program_name = getIntent()
                                .getExtras()
                                .getString("program_name") + "_" + currentUser)
        ) {
            @Override
            protected void populateView(View view, ChatMessage model, int position) {

                TextView messageText = (TextView) view.findViewById(R.id.message_text);
                TextView messageUser = (TextView) view.findViewById(R.id.message_user);
                TextView messageTime = (TextView) view.findViewById(R.id.message_time);

                TextView userName = (TextView) findViewById(R.id.userName);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                userName.setText(model.getMessageUser());

                messageTime.setText(
                        android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);

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
        Intent intent = new Intent(Messages.this, RegistrationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        finish();
    }
}