package com.example.kotlinsampleapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

public class PlayReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public PlayReceiver(Handler handler) {
        super(handler);
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);

        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }

//        Log.d("Play Receiver result:", resultData.getString("progress").toString());

//        switch (resultCode) {
//            case 1:
//                System.out.println("test:"+ resultData.getString("progress"));
//                break;
//            case 2:
//                System.out.println("delete+delete+delete+delete+delete+delete");
//                break;
//            case 3:
//                System.out.println("error+error+error+error+error+error+");
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + resultCode);
//        }
    }
}
