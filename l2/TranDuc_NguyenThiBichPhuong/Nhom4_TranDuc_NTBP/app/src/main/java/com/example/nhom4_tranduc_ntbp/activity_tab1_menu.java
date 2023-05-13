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


public class activity_tab1_menu extends AppCompatActivity {

    GridView gridViewNhanVien;
    ArrayList<IconChucNang> dsIconChucNang;
    IconChucNangAdapter iconChucNangAdapter;

    int quyenTruyCap = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab1_menu);

        addControls();
        addEvents();

    }

    private void addEvents() {

        gridViewNhanVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    xuLyTimKiem();
                } else if (position == 1) {
                    if (quyenTruyCap == 1)
                        xuLyThemNhanVien();
                } else if (position == 2) {
                    xuLyXemDanhSachNhanVien();
                } else if (position == 3) {
                    xuLyBieuDoPhanBo();
                }
            }
        });
    }

    private void xuLyBieuDoPhanBo() {
        Intent i = new Intent(this, activity_bieu_do_nhan_vien.class);

        startActivity(i);
    }

    private void xuLyXemDanhSachNhanVien() {
        Intent i = new Intent(this, activity_danh_sach_nhan_vien.class);
        i.putExtra("QUYEN_TRUY_CAP", quyenTruyCap);
        startActivity(i);

    }

    private void xuLyThemNhanVien() {
        Intent i = new Intent(this, activity_them_nhan_vien.class);
        startActivity(i);
    }

    private void xuLyTimKiem() {
        Intent i = new Intent(this, activity_tim_kiem.class);
        i.putExtra("QUYEN_TRUY_CAP", quyenTruyCap);
        startActivity(i);
    }


    private void addControls() {
        quyenTruyCap = getIntent().getIntExtra("QUYEN_TRUY_CAP", 1);
        gridViewNhanVien = (GridView) findViewById(R.id.gridViewNhanVien);
        addDSIconChucNang();
        iconChucNangAdapter = new IconChucNangAdapter(this, R.layout.activity_item_gridview, dsIconChucNang);
        gridViewNhanVien.setAdapter(new IconChucNangAdapter(this, R.layout.activity_item_gridview, dsIconChucNang));
    }

    private void addDSIconChucNang() {
        dsIconChucNang = new ArrayList<>();
        dsIconChucNang.add(new IconChucNang(R.drawable.cv, "TÌM KIẾM NHÂN VIÊN"));
        dsIconChucNang.add(new IconChucNang(R.drawable.employee, "THÊM NHÂN VIÊN MỚI"));
        dsIconChucNang.add(new IconChucNang(R.drawable.accountable, "DS NHÂN VIÊN"));
        dsIconChucNang.add(new IconChucNang(R.drawable.piechart, "THỐNG KÊ"));

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