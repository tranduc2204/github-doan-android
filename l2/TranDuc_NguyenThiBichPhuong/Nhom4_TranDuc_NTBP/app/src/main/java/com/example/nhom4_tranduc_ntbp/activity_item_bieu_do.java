package com.example.nhom4_tranduc_ntbp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import Model.NhanVien;
import Model.NhanVienAdapter;

public class activity_item_bieu_do extends AppCompatActivity {

    ImageButton btnBack, btnOptionMenu;
    TextView txtTitle;
    EditText txtThuocTinh;

    String thuocTinh="PHONGBAN";

    ListView lvTimKiem;
    ArrayList<NhanVien> dsNhanVien;
    NhanVienAdapter nhanVienAdapter;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/database/";
    SQLiteDatabase database = null;

    int quyenTruyCap = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_bieu_do);
        addControls();
        addEvents();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        nhanVienAdapter = new NhanVienAdapter(this, R.layout.activity_bieu_do, dsNhanVien,quyenTruyCap);
//        nhanVienAdapter.notifyDataSetChanged();
//        lvTimKiem.setAdapter(nhanVienAdapter);
//    }

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyBack();
            }
        });
        btnOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyOptionMenu(v);
            }
        });

    }
    private void addControls() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnOptionMenu = (ImageButton) findViewById(R.id.btnOptionMenu2);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        quyenTruyCap = getIntent().getIntExtra("QUYEN_TRUY_CAP", 1);


        lvTimKiem = (ListView) findViewById(R.id.lvTimKiem2);
    }

    private void xuLyOptionMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.getMenuInflater().inflate(R.menu.menu_bieudo, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                xuLyOptionsItemSelected(item);
                return true;
            }
        });
        popup.show();
    }

    private void xuLyOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuPhongBan) {
            thuocTinh="PHONGBAN";
            txtTitle.setText("THỐNG KÊ THEO PHÒNG BAN");

            Intent i = new Intent(this, activity_bieu_do_nhan_vien.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.mnuGioiTinh) {
            thuocTinh="GioiTinh";
            txtTitle.setText("THỐNG KÊ THEO GIỚI TÍNH");

            Intent i = new Intent(this, activity_bieu_do_nhan_vien.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.mnuHonNhan) {
            thuocTinh="HonNhan";
            txtTitle.setText("THỐNG KÊ THEO HÔN NHÂN");

            Intent i = new Intent(this, activity_bieu_do_nhan_vien.class);
            startActivity(i);
        }
    }

    private void xuLyBack() {
        finish();
    }
}