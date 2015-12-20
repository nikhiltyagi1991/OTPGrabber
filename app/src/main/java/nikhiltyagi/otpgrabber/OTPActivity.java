package nikhiltyagi.otpgrabber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OTPActivity extends AppCompatActivity {

    private SMSReceiver smsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        smsReceiver = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void sendOTP(View v){
        EditText phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        TextView status = (TextView)findViewById(R.id.status);
        phoneNumber.setEnabled(false);
        v.setVisibility(View.GONE);
        status.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        RegisterPhone registerPhone = new RegisterPhone(this,phoneNumber.getText().toString(),smsReceiver);
        registerPhone.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(smsReceiver!=null)
            unregisterReceiver(smsReceiver);
    }
}
