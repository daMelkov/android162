package com.astra.melkovhw162;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {
    private final static int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;
    private final static int MY_PERMISSIONS_REQUEST_SEND_SMS = 200;

    EditText edtPhoneNumber;
    EditText edtSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                    return;
                }
            case MY_PERMISSIONS_REQUEST_SEND_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSms();
                    return;
                }
        }

        finish();
    }

    private void initViews() {
        /* Call */
        edtPhoneNumber = findViewById(R.id.edtPhone);
        Button btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call();
            }
        });

        /* Send */
        edtSms = findViewById(R.id.edtSms);
        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms();
            }
        });
    }

    private void call() {
        final String permission = Manifest.permission.CALL_PHONE;

        String phone = edtPhoneNumber.getText().toString();
        if (phone.length() < 7) {
            return;
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                permission) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{permission}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }

    private void sendSms() {
        final String permission = Manifest.permission.SEND_SMS;

        String sms = edtSms.getText().toString();
        if (sms.length() < 1) {
            return;
        }

        if (ContextCompat.checkSelfPermission(
                MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{permission}, MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //intent.setData(Uri.parse("smsto:"));
            intent.setType("vnd.android-dir/mms-sms");
            intent.putExtra("address","+79213062903");
            intent.putExtra("sms_body", sms);
            startActivity(Intent.createChooser(intent,"Отправить смс с помощью"));
        }
    }
}