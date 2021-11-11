package com.example.kotlinsampleapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class PlayReceiver extends ResultReceiver {
    Handler hd  = null;
    Context context = null;

    public PlayReceiver( Context context,Handler handler) {
        super(handler);
        this.context = context;
        this.hd = handler;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);

        switch (resultCode) {
            case 1:
                System.out.println("test:"+ resultData.getString("progress"));
                hd.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("PlayReceiver", "hd post");
                        ((MediaActivity)context).getTv().setText(resultData.getString("progress"));
                    }
                });
                break;
            case 2:
                System.out.println("delete+delete+delete+delete+delete+delete");
                break;
            case 3:
                System.out.println("error+error+error+error+error+error+");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + resultCode);
        }
    }
}
