package com.example.nhom4_tranduc_ntbp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {
//https://www.youtube.com/watch?v=YJL7iRDoDrY
    ImageView img_bell , imgTranslate;

    EditText txtTaiKhoan, txtMatKhau;
    int quyenTruyCap = 0;

    Button btnDangNhap, btnDangKi;
    TextView txtQuenMatKhau;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgTranslate = (ImageView) findViewById(R.id.imageViewlogo);

        img_bell =(ImageView) findViewById(R.id.imageViewbell);


        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);

        imgTranslate.startAnimation(animTranslate);
        img_bell.startAnimation(animation);



        xuLySaoChepCSDLTuAsssetsVaoHeThongMobile();
        addControls();
        addEvents();

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

    private void addControls() {
        txtTaiKhoan = (EditText) findViewById(R.id.txtTaiKhoan);
        txtMatKhau = (EditText) findViewById(R.id.etMatKhau);
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        btnDangKi = (Button) findViewById(R.id.btnDangKi1);
        txtQuenMatKhau = (TextView) findViewById(R.id.txtQuenMatKhau);

    }

    private void addEvents() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDangNhap();
            }
        });
        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDangKi();
            }
        });
        txtQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyQuenMatKhau();
            }
        });

    }

    private void xuLyQuenMatKhau() {
        Intent i = new Intent(this, activity_quen_mat_khau.class);
        startActivity(i);
    }

    private void xuLyDangKi() {
        Intent i = new Intent(this, activity_dang_ki_tai_khoan.class);
        startActivity(i);
    }

    private void xuLyDangNhap() {
        if (txtTaiKhoan.getText().toString().equals("admin") && txtMatKhau.getText().toString().equals("1234")) {
            Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            quyenTruyCap = 1;
            Intent i = new Intent(MainActivity.this, activity_menu.class);



            i.putExtra("MSNV", txtTaiKhoan.getText().toString());
            i.putExtra("QUYEN_TRUY_CAP", quyenTruyCap);
            startActivity(i);
        } else {
            boolean check = false;
            check = kiemTraDangNhap();
            if (check == true) {
                Intent i = new Intent(MainActivity.this, activity_menu.class);
                i.putExtra("MSNV", txtTaiKhoan.getText().toString());
                i.putExtra("QUYEN_TRUY_CAP", quyenTruyCap);
                startActivity(i);
            } else {
                Toast.makeText(this, "Sai mật khẩu hoặc tên đăng nhập", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private boolean kiemTraDangNhap() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from TaiKhoanNhanVien", null);
            while (cursor.moveToNext()) {
                String tenDangNhap = String.valueOf(cursor.getInt(0));
                String matKhau = cursor.getString(1);
                if (tenDangNhap.equals(txtTaiKhoan.getText().toString()) && matKhau.equals(txtMatKhau.getText().toString())) {
                    return true;
                }
            }

        } catch (Exception ex) {

        }
        return false;
    }
}
