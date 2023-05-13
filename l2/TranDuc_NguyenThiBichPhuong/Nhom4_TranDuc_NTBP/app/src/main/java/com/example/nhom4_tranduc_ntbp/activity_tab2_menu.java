package com.example.nhom4_tranduc_ntbp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import Model.IconChucNang;
import Model.IconChucNangAdapter;

public class activity_tab2_menu extends AppCompatActivity {

    GridView gridViewTinhCong;
    ArrayList<IconChucNang> dsChucNang;
    IconChucNangAdapter iconChucNangAdapter;

    int quyenTruyCap = 1;
    String MSNV = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab2_menu);

        addControls();
        addEvents();
    }
    private void addEvents() {
        gridViewTinhCong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    xuLyTinhCong();
                } else if (position == 1) {
                    if (quyenTruyCap == 1) {
                        xuLyNhapCong();
                    }
                } else if (position == 2) {
                    xuLyHopDong();
                }

            }
        });
    }

    private void xuLyHopDong() {
        Intent i = new Intent(this, activity_hop_dong.class);
        i.putExtra("QUYEN_TRUY_CAP", quyenTruyCap);
        startActivity(i);
    }

    private void xuLyNhapCong() {
        Intent i = new Intent(this, activity_nhap_cong.class);
        startActivity(i);
    }

    private void xuLyTinhCong() {
        Intent i = new Intent(this, activity_tinh_cong.class);
        i.putExtra("QUYEN_TRUY_CAP", quyenTruyCap);
        i.putExtra("MSNV", MSNV);
        startActivity(i);
    }

    private void addControls() {
        quyenTruyCap = getIntent().getIntExtra("QUYEN_TRUY_CAP", 1);
        MSNV = getIntent().getStringExtra("MSNV");
        gridViewTinhCong = (GridView) findViewById(R.id.gridViewTinhCong);
        addDSChucNang();
        gridViewTinhCong.setAdapter(new IconChucNangAdapter(this, R.layout.activity_item_gridview, dsChucNang));
    }

    private void addDSChucNang() {
        dsChucNang = new ArrayList<>();
        dsChucNang.add(new IconChucNang(R.drawable.abacus, "TÍNH CÔNG NHÂN VIÊN"));
        dsChucNang.add(new IconChucNang(R.drawable.imgnhapcong, "NHẬP CÔNG NHÂN VIÊN"));
        dsChucNang.add(new IconChucNang(R.drawable.imghopdong, "HỢP ĐỒNG LÀM VIỆC"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        return;
    }
}