package com.example.nhom4_tranduc_ntbp;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class activity_sms extends Activity {
    ImageButton back;
    TextView txtPhone;
    EditText txtNoiDung;
    Button btnGui;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        addControls();
        addEvents();
    }

    private void addEvents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyGui();
            }
        });
    }

    private void xuLyGui() {
        //lấy mặc định SmsManager
        final SmsManager sms = SmsManager.getDefault();
        Intent msgSent = new Intent("ACTION_MSG_SENT");
        //Khai báo pendingintent để kiểm tra kết quả
        final PendingIntent pendingMsgSent =
                PendingIntent.getBroadcast(this, 0, msgSent, 0);
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();
                String msg = "Đã gửi mật khẩu mới về số điện thoại của bạn";
                if (result != Activity.RESULT_OK) {
                    msg = "Send failed";
                }
                Toast.makeText(activity_sms.this, msg, Toast.LENGTH_LONG).show();

            }
        }, new IntentFilter("ACTION_MSG_SENT"));
        //Gọi hàm gửi tin nhắn đi
        sms.sendTextMessage(txtPhone.getText().toString(), null, txtNoiDung.getText() + "",
                pendingMsgSent, null);

        finish();
    }

    private void addControls() {
        back = (ImageButton) findViewById(R.id.btnBack);
        txtPhone = (TextView) findViewById(R.id.txtPhone1);
        txtNoiDung = (EditText) findViewById(R.id.txtNoiDung1);
        btnGui = (Button) findViewById(R.id.btnGui2);

        Intent i = getIntent();
        String soPhone = i.getStringExtra("SO_PHONE");
        txtPhone.setText(soPhone);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }
    }
}