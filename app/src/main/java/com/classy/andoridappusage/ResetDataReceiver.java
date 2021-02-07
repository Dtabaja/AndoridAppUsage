package com.classy.andoridappusage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ResetDataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        InternalDataBase internalDataBase = new InternalDataBase(context);
        internalDataBase.resetAllIsUsageExceeded();
        internalDataBase.close();
    }
}
