package com.classy.andoridappusage;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class AppInfoActivity extends AppCompatActivity {
    private static final String TAG = "AppInfoActivity";
    private InternalDataBase iniInternalDataBase;
    private String packageName;
    private String appName;
    private TextView appNameView;
    private ImageView appIcon;
    private TextView hour;
    private TextView minute;
    private TextView second;
    private Button chartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);



        packageName = getIntent().getStringExtra("packageName");
        appName = getIntent().getStringExtra("appName");
        iniInternalDataBase = new InternalDataBase(this);
        appNameView = findViewById(R.id.chart_app_name);
        appIcon = findViewById(R.id.list_app_icon);
        hour = findViewById(R.id.hour);
        minute = findViewById(R.id.minute);
        second = findViewById(R.id.second);
        chartButton = findViewById(R.id.show_chart);
        setAppNameAndImage();




        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppInfoActivity.this, ChartActivity.class);
                intent.putExtra("packageName", packageName);
                intent.putExtra("appName", appName);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int timeSpent = getTimeSpent();
        showTimeSpent(timeSpent);
    }



    private void setAppNameAndImage() {
        appNameView.setText(appName);
        PackageManager packageManager = getPackageManager();
        try {
            appIcon.setImageDrawable(packageManager.getApplicationIcon(packageName));
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "package name not found");
        }
    }



    private void showTimeSpent(int timeSpent) {
        int[] timesAllowed = Utils.reverseProcessTime(timeSpent);
        hour.setText(String.valueOf(timesAllowed[0]));
        minute.setText(String.valueOf(timesAllowed[1]));
        second.setText(String.valueOf(timesAllowed[2]));
    }

    private int getTimeSpent() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long beginTime = calendar.getTimeInMillis();
        long endTime = beginTime + Utils.DAY_IN_MILLIS;
        HashMap<String, Integer> appUsageMap = Utils.getTimeSpent(this, packageName, beginTime, endTime);
        Integer usageTime = appUsageMap.get(packageName);
        if (usageTime == null) usageTime = 0;
        return usageTime;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        iniInternalDataBase.close();
    }
}
