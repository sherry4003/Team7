package com.example.cody.team7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Cody on 11/1/2016.
 */

public class SmsReceiver extends BroadcastReceiver {
    private String TAG = SmsReceiver.class.getSimpleName();

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the data (SMS data) bound to intent

        if (validate(context)) {
            Bundle bundle = intent.getExtras();

            SmsMessage[] msgs = null;

            String str = "";

            if (bundle != null) {
                // Retrieve the SMS Messages received
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];

                // For every SMS message received
                for (int i=0; i < msgs.length; i++) {
                    // Convert Object array
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    // Sender's phone number
                    str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
                    // Fetch the text message
                    str += msgs[i].getMessageBody().toString();
                    str += "\n";
                }

                // Display the entire SMS Message
                Log.d(TAG, str);
            }
        }


    }

    private Boolean validate(Context context) {
        Calendar c = Calendar.getInstance();
        String day = Methods.getDay(c.get(Calendar.DAY_OF_WEEK));
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (preferences.getString(day,"").equals("true")) {
            String currTime = Methods.getTime();
            String beginTime = preferences.getString("begin", "");
            String endTime = preferences.getString("end", "");

            // if currTime is later than beginTime and before endTime
            if (Methods.compareTimes(currTime, beginTime) >= 0 && Methods.compareTimes(currTime, endTime) <= 0) {
                return true;
            }
        }

        return false;
    }


}