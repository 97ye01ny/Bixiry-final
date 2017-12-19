package cookmap.cookandroid.com.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    protected Button btRecog, btTts, button;
    protected EditText etTts;
    protected TextToSpeech tts;
    protected ArrayList<String> arName, arPhoneNum;
    protected final int nNameSize = 3;
    protected static final int RECOG_CODE = 1234;
    private static final int CODE_CONTACT = 1234;



    protected String getPhoneNumFormFromName(boolean 임예은) {
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode("임예은"));
        String[] arProjection = new String[]{ContactsContract.Contacts._ID};
        Cursor cursor = getContentResolver().query(uri, arProjection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String sId = cursor.getString(0);
            String[] arProjNum = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
            String sWhereNum = ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?";
            String[] sWhereNumParam = new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, sId};
            Cursor cursorNum = getContentResolver().query(ContactsContract.Data.CONTENT_URI, arProjNum, sWhereNum, sWhereNumParam, null);
            if (cursorNum != null && cursorNum.moveToFirst()) {
                String sNum = cursorNum.getString(0);
            }
            cursorNum.close();
        }
        cursor.close();
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_CONTACT && resultCode == RESULT_OK) {
            String sFilter[] = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};



            if (requestCode == RECOG_CODE) {
                ArrayList<String> arStr = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String sRecog = arStr.get(0);
                Toast.makeText(getApplicationContext(), sRecog, Toast.LENGTH_SHORT).show();
                int nPos = arName.indexOf(sRecog);
                if (nPos == -1) {
                    tts.speak(sRecog + "는 없는 이름입니다", TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                  /*  String sPhoneNum = arPhoneNum.get(nPos);
                    Toast.makeText(getApplicationContext(), sRecog, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sPhoneNum));
                    startActivity(intent); */

                    show(requestCode, resultCode, data);

                }

            }
        }


    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btRecog = (Button) findViewById(R.id.btRecog);
        btRecog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "이름을 찾는 중...");
                startActivityForResult(intent, RECOG_CODE);
            }
        });



        arName = new ArrayList<String>(nNameSize);
        arPhoneNum = new ArrayList<String>(nNameSize);
        arName.add("임예은");
        arPhoneNum.add("01084114050");



        btTts = (Button) findViewById(R.id.btTts);
        btTts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = etTts.getText().toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(str, TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }
        });

        etTts = (EditText) findViewById(R.id.etTts);

        tts = new TextToSpeech(this, this);

    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.KOREAN);
            tts.setPitch(0.5f);
            tts.setSpeechRate(1.0f);
        }

    }


    void show(int requestCode, int resultCode, final Intent data) {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("전화 걸기");
        ListItems.add("메시지 보내기");
        ListItems.add("연락처 찾기");
        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        final List SelectedItems = new ArrayList();
        int defaultItem = 0;
        SelectedItems.add(defaultItem);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hello, Bixiry!");
        builder.setSingleChoiceItems(items, defaultItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg = "";
                        int index = (int) SelectedItems.get(0);
                        msg = ListItems.get(index);


                        if (msg.equals("전화 걸기")) {
                            Toast.makeText(getApplicationContext(), "전화 걸기가 제대료ㅗ 눌린다", Toast.LENGTH_LONG).show();


                            ArrayList<String> arStr = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            String sRecog = arStr.get(0);
                            Toast.makeText(getApplicationContext(), sRecog, Toast.LENGTH_SHORT).show();
                            int nPos = arName.indexOf(sRecog);


                            String sPhoneNum = arPhoneNum.get(nPos);
                            Toast.makeText(getApplicationContext(), sRecog, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sPhoneNum));
                            startActivity(intent);


                        }

                        else if (msg.equals("메시지 보내기")) {
                            Intent intent = new Intent(getApplicationContext(), SMSActivity.class);
                            startActivity(intent);
                        }


                        else if (msg.equals("연락처 찾기")) {

                            /* ArrayList<String> arStr = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            String sRecog = arStr.get(0);
                            Toast.makeText(getApplicationContext(), sRecog, Toast.LENGTH_SHORT).show();
                            int nPos = arName.indexOf(sRecog);

                            String sPhoneNum = arPhoneNum.get(nPos);
                            Toast.makeText(getApplicationContext(), sPhoneNum, Toast.LENGTH_SHORT).show(); */

                            String sPhoneNum = getPhoneNumFormFromName(msg.equals("임예은"));
                            Toast.makeText(getApplicationContext(), sPhoneNum, Toast.LENGTH_SHORT).show();
                        }


                        //     Toast.makeText(getApplicationContext(), msg , Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }






}