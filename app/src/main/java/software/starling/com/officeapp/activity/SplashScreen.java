package software.starling.com.officeapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import software.starling.com.officeapp.R;
import software.starling.com.officeapp.constant.Helper;
import software.starling.com.officeapp.constant.MyPrecfence;

public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    String id, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //check login values
      /*  SharedPreferences sharedPreferences = getSharedPreferences("LogIn", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        pass = sharedPreferences.getString("pass", "");*/
        setContentView(R.layout.activity_splash_screen);
//        MyPrecfence.getActiveInstance(PettyCash.this).getProjectId()
        if (Helper.isDataConnected(this) || Helper.isWifiConnected(this)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                        Intent mainIntent = new Intent(SplashScreen.this, LogIn.class);
                        startActivity(mainIntent);
                        finish();


                }
            }, 2000);
        } else {
            Helper.ShowToast(this, "No Internet Connection");
        }

    }
}
