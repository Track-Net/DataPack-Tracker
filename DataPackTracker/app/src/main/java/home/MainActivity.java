package home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datapacktracker.MenuPage;
import com.example.datapacktracker.R;
import com.google.firebase.auth.FirebaseAuth;

import Emergency_DataBank.emergency_databank;
import dataUsage.dataUsage;
import user_registration.signup_page;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(MainActivity.this, MenuPage.class));
                    finish();
                }
            }
        }, 2000);
    }
}
