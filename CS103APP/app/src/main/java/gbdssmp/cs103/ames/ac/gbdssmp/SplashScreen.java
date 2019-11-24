package gbdssmp.cs103.ames.ac.gbdssmp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class SplashScreen extends MainActivity {

    /** Duration of wait **/
//    private final int SLASH_DISPLAY_LENGTH = 1000;

    //1: Declare variables
    public static Boolean started = false;
    private FirebaseAuth mAuth;
    private Button mLogin, mRegistration;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splashscreen_activity);


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this,RegistrationActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, 2800);
    }
}