package cookmap.cookandroid.com.myapplication;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;



//Broadcast Receiver
public class MyReceiverSendSms extends BroadcastReceiver {
    public MyReceiverSendSms() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //       throw new UnsupportedOperationException("Not yet implemented");

        String sMsg;

        switch (getResultCode()) {
            case Activity.RESULT_OK:
                sMsg = "Success!";
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                sMsg = "Failed!";
                break;
            default:
                sMsg = "Unknown error.";
        }
        Toast.makeText(context, sMsg, Toast.LENGTH_SHORT).show();

    }

}