package gbdssmp.cs103.ames.ac.gbdssmp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
public class RegistrationActivity extends AppCompatActivity {


    private Button mRegistration, mLogin;
    private EditText mEmail, mPassword, mName, loginEmail, loginPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener, loginFirebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
        loginFirebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    return;
                }
            }

        };

        mAuth = FirebaseAuth.getInstance();



        mRegistration = findViewById(R.id.registration);
        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        mLogin = findViewById(R.id.login);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);


        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
             vibrate.vibrate(120);

             MediaPlayer.create(getApplicationContext(),R.raw.open).start();

             final String name = mName.getText().toString();
             final String email = mEmail.getText().toString();
             final String password = mPassword.getText().toString();

             if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {

                 Toast.makeText(getApplication(), "Please enter all your name, email and password",
                         Toast.LENGTH_SHORT).show();
                 return;
             }

             mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                 RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (!task.isSuccessful()) {

                             Toast.makeText(getApplication(), "ERROR. EMAIL IS INVALID",
                                     Toast.LENGTH_SHORT).show();
                         } else {

                             FirebaseUser user = mAuth.getCurrentUser();
                             UserProfileChangeRequest profileUpdates = new
                                     UserProfileChangeRequest.Builder().setDisplayName(name).build();
                             user.updateProfile(profileUpdates);

                             String userId = mAuth.getCurrentUser().getUid();
                             DatabaseReference currentUserDb = FirebaseDatabase
                                     .getInstance()
                                     .getReference()
                                     .child("users")
                                     .child(name);

                             Map userInfo = new HashMap();
                             userInfo.put("email", email);
                             userInfo.put("name", name);
                             userInfo.put("profileImageUrl", "default");

                             currentUserDb.updateChildren(userInfo);

                             Toast.makeText(getApplication(), "REGISTERED SUCCESSFULLY",
                                     Toast.LENGTH_SHORT).show();
                         }
                     }
                 });
             }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrate.vibrate(120);

                MediaPlayer.create(getApplicationContext(),R.raw.open).start();

                final String email = loginEmail.getText().toString();
                final String password = loginPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {

                    Toast.makeText(getApplication(), "Please enter all your email and password",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            Toast.makeText(RegistrationActivity.this,
                                    "SIGN IN ERROR. WRONG EMAIL OR PASSWORD",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
