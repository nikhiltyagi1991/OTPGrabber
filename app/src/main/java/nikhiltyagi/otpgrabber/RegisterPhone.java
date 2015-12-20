package nikhiltyagi.otpgrabber;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Nikhil Tyagi on 13-12-2015.
 */
class RegisterPhone extends AsyncTask<Void, Void, String> {
    private String phoneNumber;
    private Context context;
    private SMSReceiver smsReceiver;

    public RegisterPhone(Context context,String phoneNumber, SMSReceiver smsReceiver){
        this.phoneNumber = phoneNumber;
        this.context = context;
        this.smsReceiver = smsReceiver;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            ContentValues postParams = new ContentValues();
            postParams.put("phone",phoneNumber);
            JSONObject jsonObject = new JSONObject(getContentFromWeb("http://192.168.0.8/rainylink/operationSuccessResponse.json",postParams));
            String status = jsonObject.getString("status");
            String result = jsonObject.getString("result");
            if(status.equals("ok")&&result.equals("success")) {
                String otp = jsonObject.getString("otp");
                return otp;
            }
        } catch (Exception e) {
            Log.wtf("Network Problem",e.getMessage());
        }
        return null;
    }

    private String getContentFromWeb(String url,ContentValues params) throws Exception{
        URL connectURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
        conn.setRequestMethod("POST");

        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.valueSet()) {
            String key = URLEncoder.encode(entry.getKey(), "UTF-8");
            String value = URLEncoder.encode(entry.getValue().toString(),"UTF-8");
            result.append(key+"="+value);
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
        writer.write(result.toString());

        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        in.close();
        return sb.toString();
    }

    @Override
    protected void onPostExecute(final String pass) {
        if(pass!=null) {
            smsReceiver = new SMSReceiver() {
                @Override
                public void otpReceived(String otp) {
                    if (otp.equals(pass)) {
                        //This operation is done inside the BroadcastReciever so only small synchronous operations are to be included.
                        // If you desire to do a long operation after recieving the sms consider making a service.
                        Intent mainScreen = new Intent(context, MainScreen.class);
                        context.startActivity(mainScreen);
                    }
                }
            };
            context.registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
        }
    }
}
