package com.example.nhom4_tranduc_ntbp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class activity_ma_xac_nhan extends Activity {

    ImageButton back;
    EditText txtMaXacNhan;
    Button btnXacNhan;
    String maNhanVien;
    String matKhau;
    int maXN = 1000;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_xac_nhan);

        xuLySaoChepCSDLTuAsssetsVaoHeThongMobile();

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
        xuLyGuiMaXacNhan();
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyXacNhan();
            }
        });
    }

    private void xuLyGuiMaXacNhan() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("select Phone from NhanVien where MSNV=" + maNhanVien, null);
        String soDienThoaiNhanVien = null;
        while (cursor.moveToNext())
            soDienThoaiNhanVien = cursor.getString(0);

        Random r = new Random();
        maXN = (int) (1000 + r.nextInt(8999));

        //lấy mặc định SmsManager
        final SmsManager sms = SmsManager.getDefault();
        Intent msgSent = new Intent("ACTION_MSG_SENT");
        //Khai báo pendingintent để kiểm tra kết quả
        final PendingIntent pendingMsgSent =
                PendingIntent.getBroadcast(this, 0, msgSent, 0);
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();
                String msg = "Vui lòng vào ứng dụng tin nhắn để nhận";
                if (result != Activity.RESULT_OK) {
                    msg = "Send failed";
                }
                Toast.makeText(activity_ma_xac_nhan.this, msg, Toast.LENGTH_LONG).show();

            }
        }, new IntentFilter("ACTION_MSG_SENT"));
        //Gọi hàm gửi tin nhắn đi
        sms.sendTextMessage(soDienThoaiNhanVien, null, ("Mã xác nhận của bạn là: ").toString() + String.valueOf(maXN).toString() + "",
                pendingMsgSent, null);
    }

    private void xuLyXacNhan() {

        txtMaXacNhan = (EditText) findViewById(R.id.txtMaXacNhan1) ;
        String maXacNhanTuPhone = "";
        try {
            maXacNhanTuPhone = txtMaXacNhan.getText().toString();
        } catch (Exception ex) {

        }
        int tmp;
        tmp = Integer.parseInt(txtMaXacNhan.getText().toString());
        if (maXN == tmp) {
            ContentValues values = new ContentValues();
            values.put("MSNV_TK", Integer.parseInt(maNhanVien));
            values.put("MatKhau", matKhau);
            values.put("QuyenTruyCap", 0);
            long add = database.insert("TaiKhoanNhanVien", null, values);
            if (add == -1) {
                Toast.makeText(this, "Đăng kí không thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Toast.makeText(this, maXacNhanTuPhone, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "Mã xác nhận không chính xác", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    @SuppressLint("ObsoleteSdkInt")
    private void addControls() {
        back = (ImageButton) findViewById(R.id.btnBack);
        txtMaXacNhan = (EditText) findViewById(R.id.txtMaXacNhan1);
        btnXacNhan = (Button) findViewById(R.id.btnXacNhan1);
        Intent i = getIntent();
        maNhanVien = i.getStringExtra("MA_NHAN_VIEN");
        matKhau = i.getStringExtra("MAT_KHAU");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }

    }



    private void xuLySaoChepCSDLTuAsssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAcssets();
                Toast.makeText(this, "Sao chep database thanh cong", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void copyDataBaseFromAcssets() {
        try {

            InputStream myInput = getAssets().open(DATABASE_NAME);
            String outFileName = layDuongDanLuuTru();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);

            if (!f.exists()) {
                f.mkdir();
            }
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();

        } catch (Exception ex) {
            Log.e("Loi sao chep", ex.toString());

        }
    }

    private String layDuongDanLuuTru() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
}