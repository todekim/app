package com.narangdesign.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyFirebaseMsgService";

    private FragmentManager fragmentManager;
    private FragmentHome fragment_home;
    private FragmentWork fragment_work;
    private FragmentCal fragment_cal;
    private FragmentMap fragment_map;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_fragment();
        init_firebase();
    }

    private void init_fragment(){
        fragmentManager = getSupportFragmentManager();
        fragment_home = new FragmentHome();
        fragment_work = new FragmentWork();
        fragment_cal = new FragmentCal();
        fragment_map = new FragmentMap();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment_home).commitAllowingStateLoss();
    }

    private void init_firebase(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                    }
                });
    }

    public void clickHandler(View view)
    {
        transaction = fragmentManager.beginTransaction();
        switch(view.getId())
        {
            case R.id.btn_fragment_home:
                transaction.replace(R.id.frameLayout, fragment_home).commitAllowingStateLoss();
                break;
            case R.id.btn_fragment_work:
                transaction.replace(R.id.frameLayout, fragment_work).commitAllowingStateLoss();
                break;
            case R.id.btn_fragment_cal:
                transaction.replace(R.id.frameLayout, fragment_cal).commitAllowingStateLoss();
                break;
            case R.id.btn_fragment_map:
                transaction.replace(R.id.frameLayout, fragment_map).commitAllowingStateLoss();
                break;
        }
    }
}