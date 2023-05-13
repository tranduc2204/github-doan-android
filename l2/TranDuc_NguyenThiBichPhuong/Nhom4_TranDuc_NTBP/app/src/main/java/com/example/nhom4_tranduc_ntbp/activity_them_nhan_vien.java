package com.example.nhom4_tranduc_ntbp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class activity_them_nhan_vien extends Activity {//Activity

    ImageButton back;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ImageView imgAvatar;
    int RESULT_LOAD_IMG = 1;

    Button btnChange, btnThem;
    EditText txtMaNhanVienx, txtHoTen, txtCMNDx, txtHoKhau, txtNguyenQuan, txtTonGiao, txtPhone, txtEmail;

    AutoCompleteTextView autotxtDanToc;
    String[] arrDanToc;
    ArrayAdapter<String> adapterDanToc;

    Spinner spPhongBan;
    ArrayList<String> dsPhongBan;
    ArrayAdapter<String> adapterPhongBan;
    int viTriSpPhongBan = -1;

    Spinner spNgaySinh;
    ArrayList<String> dsNgaySinh;
    ArrayAdapter<String> adapterNgaySinh;
    int viTriSpNgaySinh = -1;

    Spinner spThangSinh;
    ArrayList<String> dsThangSinh;
    ArrayAdapter<String> adapterThangSinh;
    int viTriSpThangSinh = -1;

    Spinner spNamSinh;
    ArrayList<String> dsNamSinh;
    ArrayAdapter<String> adapterNamSinh;
    int viTriSpNamSinh = -1;

    Spinner spHonNhan;
    ArrayList<String> dsHonNhan;
    ArrayAdapter<String> adapterHonNhan;
    int viTriSpHonNhan = -1;


    Spinner spGioiTinh;
    ArrayList<String> dsGioiTinh;
    ArrayAdapter<String> adapterGioiTinh;
    int viTriSpGioiTinh = -1;


    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);

        xuLySaoChepCSDLTuAssetsVaoHeThongMobile();
        addControls();
        addEvents();

    }
    private void xuLySaoChepCSDLTuAssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAcsset();
                Toast.makeText(this, "thanh cong", Toast.LENGTH_LONG);
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

            }
        }

    }

    private void CopyDataBaseFromAcsset() {
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

    private void addEvents() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChange();

            }

        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThem();
            }
        });

        spNgaySinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpNgaySinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spThangSinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpThangSinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spNamSinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpNamSinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spHonNhan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpHonNhan = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPhongBan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpPhongBan = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spGioiTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpGioiTinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void xuLyChange() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imgAvatar.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(activity_them_nhan_vien.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(activity_them_nhan_vien.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }

    }


    @SuppressLint("Range")
    private void xuLyThem() {

        txtMaNhanVienx = (EditText) findViewById(R.id.etmanv) ;
        txtCMNDx = (EditText) findViewById(R.id.etcmnd);
        String tmp = txtMaNhanVienx.getText().toString();

        if (tmp.equals("")==true){
            Toast.makeText(activity_them_nhan_vien.this, "Vui lòng nhập mã nhân viên", Toast.LENGTH_LONG).show();
        }else {
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("select MSNV,CMND,Phone,Email from NhanVien", null);

            int mSNVx, cMNDx, ngaySinh, thangSinh, namSinh;
            float heSoLuong;
            Bitmap bm = ((BitmapDrawable) imgAvatar.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] hinhAvatar = stream.toByteArray();
            String hoTen, gioiTinh, phone, email, danToc, nguyenQuan, hoKhau, tonGiao, honNhan, phongBan;

            mSNVx = Integer.parseInt(txtMaNhanVienx.getText().toString());
            cMNDx = Integer.parseInt(txtCMNDx.getText().toString());
            ngaySinh = Integer.parseInt(dsNgaySinh.get(viTriSpNgaySinh));
            thangSinh = Integer.parseInt(dsThangSinh.get(viTriSpThangSinh));
            namSinh = Integer.parseInt(dsNamSinh.get(viTriSpNamSinh));

            hoTen = txtHoTen.getText().toString();
            gioiTinh = dsGioiTinh.get(viTriSpGioiTinh);
            phone = txtPhone.getText().toString();
            email = txtEmail.getText().toString();
            danToc = autotxtDanToc.getText().toString();
            nguyenQuan = txtNguyenQuan.getText().toString();
            hoKhau = txtHoKhau.getText().toString();
            tonGiao = txtTonGiao.getText().toString();
            honNhan = dsHonNhan.get(viTriSpHonNhan);
            phongBan = dsPhongBan.get(viTriSpPhongBan);

            if (phongBan.equals("Phòng Kỹ Thuật")) {
                heSoLuong = (float) 6.5;
            } else if (phongBan.equals("Phòng Tài Chính")) {
                heSoLuong = (float) 5;
            } else if (phone.equals("Phòng Kinh Doanh")) {
                heSoLuong = (float) 5.5;
            } else {
                heSoLuong = (float) 4.5;
            }

            int check = 0;

            if (mSNVx < 1 || mSNVx > 7049999) {
                Toast.makeText(this, "Mã số nhân viên không hợp lệ", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndex("MSNV")) == mSNVx) {
                        check++;
                        Toast.makeText(this, "Đã tồn tại mã số nhân viên có mã số " + String.valueOf(mSNVx), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (cursor.getInt(cursor.getColumnIndex("CMND")) == cMNDx) {
                        check++;
                        Toast.makeText(this, "Đã tồn tại nhân viên có CMND " + String.valueOf(cMNDx), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (cursor.getString(cursor.getColumnIndex("Phone")).equals(phone)) {
                        check++;
                        Toast.makeText(this, "Đã tồn tại nhân viên có số phone " + phone, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (cursor.getString(cursor.getColumnIndex("Email")).equals(email)) {
                        check++;
                        Toast.makeText(this, "Đã tồn tại nhân viên có email " + email, Toast.LENGTH_SHORT).show();
                        break;
                    }

                }
                if (check == 0) {
                    ContentValues values = new ContentValues();
                    values.put("MSNV", mSNVx);
                    values.put("HoTen", hoTen);
                    values.put("NgaySinh", String.valueOf(ngaySinh) + "/" + String.valueOf(thangSinh) + "/"
                            + String.valueOf(namSinh));
                    values.put("GioiTinh", gioiTinh);
                    values.put("CMND", cMNDx);
                    values.put("Phone", phone);
                    values.put("Email", email);
                    values.put("DanToc", danToc);
                    values.put("NguyenQuan", nguyenQuan);
                    values.put("HoKhau", hoKhau);
                    values.put("TonGiao", tonGiao);
                    values.put("HonNhan", honNhan);
                    values.put("PhongBan", phongBan);
                    values.put("Hinh", hinhAvatar);
                    long add = database.insert("NhanVien", null, values);
                    if (add == -1) {
                        Toast.makeText(this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Thêm thành công dòng thứ " + add, Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }
        }
    }


    private void addControls() {
        btnChange = (Button) findViewById(R.id.btnChange2);
        btnThem = (Button) findViewById(R.id.btnThem2);

        back = (ImageButton) findViewById(R.id.btnBack);

        txtCMNDx = (EditText) findViewById(R.id.etcmnd);
        txtMaNhanVienx = (EditText) findViewById(R.id.etmanv);

        txtHoTen = (EditText) findViewById(R.id.txtHoTen2);

        txtHoKhau = (EditText) findViewById(R.id.txtHoKhau2);
        txtNguyenQuan = (EditText) findViewById(R.id.txtNguyenQuan2);
        txtTonGiao = (EditText) findViewById(R.id.txtTonGiao2);
        txtPhone = (EditText) findViewById(R.id.txtPhone2);
        txtEmail = (EditText) findViewById(R.id.txtEmail2);

        autotxtDanToc = (AutoCompleteTextView) findViewById(R.id.autotxtDanToc2);
        arrDanToc = getResources().getStringArray(R.array.arrDanTocc);
        adapterDanToc = new ArrayAdapter<String>(activity_them_nhan_vien.this, android.R.layout.simple_list_item_1, arrDanToc);
        autotxtDanToc.setAdapter(adapterDanToc);


        spGioiTinh = (Spinner) findViewById(R.id.spGioiTinh2);
        spNgaySinh = (Spinner) findViewById(R.id.spNgaySinh2);
        spThangSinh = (Spinner) findViewById(R.id.spThangSinh2);
        spNamSinh = (Spinner) findViewById(R.id.spNamSinh2);
        spPhongBan = (Spinner) findViewById(R.id.spPhongBan2);
        spHonNhan = (Spinner) findViewById(R.id.spHonNhan2);

        addDSNgaySinh();
        adapterNgaySinh = new ArrayAdapter<String>(activity_them_nhan_vien.this, android.R.layout.simple_spinner_item
                , dsNgaySinh);
        adapterNgaySinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNgaySinh.setAdapter(adapterNgaySinh);

        addDSTThangSinh();
        adapterThangSinh = new ArrayAdapter<String>(activity_them_nhan_vien.this, android.R.layout.simple_spinner_item
                , dsThangSinh);
        adapterThangSinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThangSinh.setAdapter(adapterThangSinh);

        addDSNamSinh();
        adapterNamSinh = new ArrayAdapter<String>(activity_them_nhan_vien.this, android.R.layout.simple_spinner_item
                , dsNamSinh);
        adapterNamSinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNamSinh.setAdapter(adapterNamSinh);

        addDSPhongBan();
        adapterPhongBan = new ArrayAdapter<String>(activity_them_nhan_vien.this, android.R.layout.simple_list_item_checked
                , dsPhongBan);
        adapterPhongBan.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spPhongBan.setAdapter(adapterPhongBan);

        addDSHonNhan();
        adapterHonNhan = new ArrayAdapter<String>(activity_them_nhan_vien.this, android.R.layout.simple_list_item_checked
                , dsHonNhan);
        adapterHonNhan.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spHonNhan.setAdapter(adapterHonNhan);

        addDSGioiTinh();
        adapterGioiTinh = new ArrayAdapter<String>(activity_them_nhan_vien.this, android.R.layout.simple_list_item_checked,
                dsGioiTinh);
        adapterGioiTinh.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spGioiTinh.setAdapter(adapterGioiTinh);

        imgAvatar = (ImageView) findViewById(R.id.imgAvatar2);
    }

    private void addDSGioiTinh() {
        dsGioiTinh = new ArrayList<>();
        dsGioiTinh.add("Nam");
        dsGioiTinh.add("Nữ");
    }

    private void addDSHonNhan() {
        dsHonNhan = new ArrayList<>();
        dsHonNhan.add("Chưa kết hôn");
        dsHonNhan.add("Đã kết hôn");
        dsHonNhan.add("Không rõ");
    }


    private void addDSPhongBan() {
        dsPhongBan = new ArrayList<>();
        dsPhongBan.add("Phòng Tài Chính");
        dsPhongBan.add("Phòng Kỹ Thuật");
        dsPhongBan.add("Phòng Nhân Sự");
        dsPhongBan.add("Phòng Kinh Doanh");
    }

    private void addDSNgaySinh() {
        dsNgaySinh = new ArrayList<>();

        for (int i = 0; i < 31; i++)
            dsNgaySinh.add(String.valueOf((i + 1)));
    }

    private void addDSNamSinh() {
        dsNamSinh = new ArrayList<>();
        for (int i = 1949; i < 2022; i++)
            dsNamSinh.add(String.valueOf((i + 1)));

    }

    private void addDSTThangSinh() {
        dsThangSinh = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            dsThangSinh.add(String.valueOf((i + 1)));

    }
}