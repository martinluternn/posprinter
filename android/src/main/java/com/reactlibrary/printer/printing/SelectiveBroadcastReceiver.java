package com.reactlibrary.printer.printing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class SelectiveBroadcastReceiver extends BroadcastReceiver {

    private String actionName;

    public SelectiveBroadcastReceiver(String actionName){
        this.actionName = actionName;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(actionName)) {
            onReceiveFilteredIntent(context, intent);
        }

    }
    public abstract void onReceiveFilteredIntent(Context context, Intent intent);
}
