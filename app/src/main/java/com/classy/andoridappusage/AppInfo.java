package com.classy.andoridappusage;

import android.graphics.drawable.Drawable;

public class AppInfo implements Comparable<AppInfo> {
    private String appName;
    private Drawable icon;
    private String packageName;


    AppInfo(String appName, Drawable icon, String packageName) {
        this.appName = appName;
        this.icon = icon;
        this.packageName = packageName;

    }

    String getAppName() {
        return appName;
    }

    Drawable getIcon() {
        return icon;
    }

    String getPackageName() {
        return packageName;
    }


    @Override
    public int compareTo(AppInfo appInfo) {
        return appName.compareTo(appInfo.getAppName());
    }

}
