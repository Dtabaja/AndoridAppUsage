package com.classy.andoridappusage;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

        private InternalDataBase internalDataBase;
        private ListView appListView;
        private static int LAUNCH_SETTINGS_ACTIVITY = 1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            internalDataBase = new InternalDataBase(this);
            appListView = findViewById(R.id.app_list);

            Notifications.resetIsUsageExceededData(getApplicationContext());
            startBackgroundService();
            if(!Utils.isUsageAccessAllowed(this)) {
                openUsageDialog();
            }
        }

        @Override
        protected void onResume() {
            super.onResume();
            showAppListAndSetClickListener();
        }

        private List<AppInfo> getAppInfoList() {
            PackageManager packageManager = getPackageManager();
            List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
            List<AppInfo> appList = new ArrayList<>();

            for (int i = 0; i < packageInfoList.size(); i++) {
                PackageInfo packageInfo = packageInfoList.get(i);
                if (packageManager.getLaunchIntentForPackage(packageInfo.packageName) != null) {
                    String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
                    Drawable appIcon = packageInfo.applicationInfo.loadIcon(packageManager);
                    String packageName = packageInfo.packageName;

                    TrackedInfo trackedAppInfo = internalDataBase.getRow(packageName);
                    if(trackedAppInfo != null) {
                        boolean isUsageExceeded = trackedAppInfo.getIsUsageExceeded() == 1;
                        appList.add(new AppInfo(appName,
                                appIcon, packageName, true, isUsageExceeded));
                    }
                    else {
                        appList.add(new AppInfo(appName,
                                appIcon, packageName, false, false));
                    }
                }
            }
            Collections.sort(appList);
            return appList;
        }

        private void showAppListAndSetClickListener() {
            final List<AppInfo>appInfoList = getAppInfoList();
            AppListAdapter appListAdapter = new AppListAdapter(MainActivity.this, appInfoList);
            appListView.setAdapter(appListAdapter);

            appListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    Intent intent = new Intent(MainActivity.this, AppInfoActivity.class);
                    intent.putExtra("packageName", appInfoList.get(i).getPackageName());
                    intent.putExtra("appName", appInfoList.get(i).getAppName());
                    startActivity(intent);
                }
            });
        }

        private void startBackgroundService() {
            if(Utils.isUsageAccessAllowed(this)) {
                Notifications.scheduleNotification(getApplicationContext());
            }
        }
    private void openUsageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Usage Access Needed :(")
                .setMessage("You need to give usage access to this app to see usage data of your apps. " +
                        "Click \"Go To Settings\" and then give the access :)")
                .setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent usageAccessIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(usageAccessIntent, LAUNCH_SETTINGS_ACTIVITY);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_SETTINGS_ACTIVITY) {
            if(Utils.isUsageAccessAllowed(this)) {
                Notifications.scheduleNotification(this);
            }
        }
    }
        @Override
        protected void onDestroy() {
            super.onDestroy();
            internalDataBase.close();

        }
}