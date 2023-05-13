package com.example.nhom4_tranduc_ntbp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import Model.NhanVien;

public class activity_hien_thi_thong_tin extends Activity {

    ImageView imgAvatar;
    TextView txtMaNhanVien, txtHoTen, txtCMND, txtHoKhau, txtNguyenQuan, txtTonGiao, txtPhone, txtEmail;
    TextView txtDanToc, txtPhongBan, txtNgaySinh, txtHonNhan, txtGioiTinh;
    ImageButton btnCall, btnSMS;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hien_thi_thong_tin);

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

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyCall();
            }
        });
        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLySMS();
            }
        });
    }

    private void xuLySMS() {
        Intent i = new Intent(this, activity_sms.class);
        i.putExtra("SO_PHONE", txtPhone.getText().toString());
        startActivity(i);
    }

    private void xuLyCall() {
        Intent i = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + txtPhone.getText().toString());
        i.setData(uri);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(i);
    }

    private void addControls() {

        back = (ImageButton) findViewById(R.id.btnBack);
        txtDanToc = (TextView) findViewById(R.id.txtDanToc);
        txtPhongBan = (TextView) findViewById(R.id.txtPhongBan);
        txtNgaySinh = (TextView) findViewById(R.id.txtNgaySinh);
        txtHonNhan = (TextView) findViewById(R.id.txtHonNhan);
        txtGioiTinh = (TextView) findViewById(R.id.txtGioiTinh);
        txtPhone = (TextView) findViewById(R.id.txtPhone1);
        txtEmail = (TextView) findViewById(R.id.txtEmail1);
        txtTonGiao = (TextView) findViewById(R.id.txtTonGiao1);
        txtNguyenQuan = (TextView) findViewById(R.id.txtNguyenQuan1);
        txtHoKhau = (TextView) findViewById(R.id.txtHoKhau1);
        txtCMND = (TextView) findViewById(R.id.txtCMND1);
        txtHoTen = (TextView) findViewById(R.id.txtHoTen1);
        txtMaNhanVien = (TextView) findViewById(R.id.etmanv);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar2);


        Intent i = getIntent();
        NhanVien nhanVien = (NhanVien) i.getSerializableExtra("KIEU_NHAN_VIEN");

        txtMaNhanVien.setText(nhanVien.getMaNhanVien());
        txtHoTen.setText(nhanVien.getHoTen());
        txtCMND.setText(String.valueOf(nhanVien.getSoCMND()));
        txtHoKhau.setText(nhanVien.getHoKhau());
        txtNguyenQuan.setText(nhanVien.getNguyenQuan());
        txtTonGiao.setText(nhanVien.getTonGiao());
        txtPhone.setText(nhanVien.getSoPhone());
        txtEmail.setText(nhanVien.getEmail());
        txtDanToc.setText(nhanVien.getDanToc());
        txtPhongBan.setText(nhanVien.getPhongBanTrucThuoc());
        txtNgaySinh.setText(String.valueOf(nhanVien.getNgaySinh()) + "/" + String.valueOf(nhanVien.getThangSinh())
                + "/" + String.valueOf(nhanVien.getNamSinh()));
        txtHonNhan.setText(nhanVien.getTinhTrangHonNhan());
        txtGioiTinh.setText(nhanVien.getGioiTinh());

        if (nhanVien.getHinhAnh() == null)
            imgAvatar.setImageResource(R.drawable.imgavatar);
        else {
            Bitmap bmImage = BitmapFactory.decodeByteArray(nhanVien.getHinhAnh(), 0, nhanVien.getHinhAnh().length);
            imgAvatar.setImageBitmap(bmImage);
        }

        btnCall = (ImageButton) findViewById(R.id.btnCall);
        btnSMS = (ImageButton) findViewById(R.id.btnSMS);


    }
}