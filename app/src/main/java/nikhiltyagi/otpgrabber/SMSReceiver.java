package nikhiltyagi.otpgrabber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nikhil Tyagi on 13-12-2015.
 */
public abstract class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            Object[] pdus = (Object[]) bundle.get("pdus");

            //Get all the SMSes
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }

            //Processing each SMS one by one.
            for (SmsMessage message : messages) {
                String msg = message.getMessageBody();
                String from = message.getOriginatingAddress();
                if(from.equals("5555")){
                    Pattern pattern = Pattern.compile("([1-9][0-9]{5})");
                    Matcher matches = pattern.matcher(msg);
                    if(matches.find()){
                        otpReceived(matches.group(0));
                    }
                }
            }
        }
    }

    public abstract void otpReceived(String otp);
}
