package cookmap.cookandroid.com.myapplication;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SMSActivity extends AppCompatActivity {

    protected EditText etNum, etMsg;
    protected Button btSms;
    protected MyReceiverSendSms myReceiverSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

            setContentView(R.layout.activity_main);
            etNum = (EditText) findViewById(R.id.etNum);
            etMsg = (EditText) findViewById(R.id.etMsg);
            btSms = (Button) findViewById(R.id.btnSms);


            btSms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sNum = etNum.getText().toString();
                    String sMsg = etMsg.getText().toString();
                    PendingIntent piSent = PendingIntent.getBroadcast(getApplicationContext(),0,new Intent("SMS_SENT"), 0);
                    SmsManager.getDefault().sendTextMessage(sNum, null, sMsg, piSent, null);
                }
            });

            myReceiverSMS = new MyReceiverSendSms();
        }

        @Override
        protected void onResume() {
            super.onResume();
            registerReceiver(myReceiverSMS, new IntentFilter("SMS_SENT"));
        }

        @Override
        protected void onPause() {
            unregisterReceiver(myReceiverSMS);
            super.onPause();
        }

    }


