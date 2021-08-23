package com.narangdesign.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    private AppUpdateManager appUpdateManager;
    private final String TAG = "mainActivity";
    private static final int MY_REQUEST_CODE = 100;

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

        appUpdateManager = AppUpdateManagerFactory.create(this);
        // Returns an intent object that you use to check for an update.
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try{
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.FLEXIBLE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            MY_REQUEST_CODE);
                    Log.d(TAG,"update now");
                }catch(IntentSender.SendIntentException e){
                    e.printStackTrace();
                }
            }
        });
        appUpdateManager.registerListener(installStateUpdatedListener);


        init_fragment();
        init_firebase();
    }

    private InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            // (Optional) Provide a download progress bar.
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                showCompletedUpdate();
            }
            // Log state or install the update.
        }
    };

    private void showCompletedUpdate() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "new app is ready", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("install", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
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

    @Override
    protected void onStop() {
        if(appUpdateManager!=null) appUpdateManager.unregisterListener(installStateUpdatedListener);
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MY_REQUEST_CODE && requestCode != RESULT_OK) {
            Toast.makeText(this,"cancle",Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("종료!")
                    .setMessage("종료하시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("아니오", null)
                    .show();
        }else{
            Toast.makeText(this,"fragment back",Toast.LENGTH_SHORT).show();
        }
    }
}