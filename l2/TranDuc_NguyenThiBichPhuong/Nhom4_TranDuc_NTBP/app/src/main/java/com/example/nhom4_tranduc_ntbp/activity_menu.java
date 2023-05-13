package com.example.nhom4_tranduc_ntbp;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Model.photo;
import Model.photoAdapter;
import me.relex.circleindicator.CircleIndicator;

//TabActivity
@SuppressWarnings("deprecation")
public class activity_menu extends TabActivity {
    ViewPager viewpager;
    CircleIndicator circleIndicator;
    Model.photoAdapter photoAdapter;
    List<photo> mListPhoto;
    Timer mTimer;

    ImageView imgAvatar;
    TextView txtHoTen, txtMaNhanVien, txtPhongBan;
    LinearLayout lnlDoiMatKhau;
    ImageView imgPhongBan;
    ImageButton btnLogout;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    String maNhanVien = "";
    int quyenTruyCap = 1;

    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        viewpager = findViewById(R.id.viewpagee);
        circleIndicator = findViewById(R.id.circle_indicator);

        mListPhoto = getListPhoto();
        photoAdapter = new photoAdapter(this, mListPhoto);
        viewpager.setAdapter(photoAdapter);

        circleIndicator.setViewPager(viewpager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        autoslideimages();


        xuLySaoChepCSDLTuAssetsVaoHeThongMobile();
        addControls();
        addEvents();
//        txtgio = (TextView) findViewById(R.id.tvgio);
//        TimerTask time = new TimerTask() {
//            @Override
//            public void run() {
//                Date dt = new Date();
//                txtgio.setText(dt.toString().substring(11,19));
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(time, 0, 1000);
    }

    private List<photo> getListPhoto(){
        List<photo> list = new ArrayList<>();
        list.add(new photo(R.drawable.v2));
        list.add(new photo(R.drawable.x2));
        list.add(new photo(R.drawable.x3));
        list.add(new photo(R.drawable.v1));

        return list;
    }

    private void autoslideimages(){
        if (mListPhoto == null || mListPhoto.isEmpty() || viewpager == null) {
            return;
        }

        if (mTimer == null){
            mTimer = new Timer();
        }

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewpager.getCurrentItem();
                        int totalItem = mListPhoto.size() - 1;
                        if (currentItem < totalItem){
                            currentItem ++;
                            viewpager.setCurrentItem(currentItem);
                        }else {
                            viewpager.setCurrentItem(0);
                        }
                    }
                });
            }
        },  500, 3000 );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void xuLySaoChepCSDLTuAssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAcsset();
                Toast.makeText(this, "Sao chep database thanh cong", Toast.LENGTH_SHORT).show();

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

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i)
                            .setBackgroundColor(Color.parseColor("#E98C02")); // unselected Color.WHITE #ff5a01
                }
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                        .setBackgroundColor(Color.WHITE); // selected Color.parseColor("#ff5a01")
            }
        });
        lnlDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDoiMatKhau();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLogout();
            }
        });
    }

    private void xuLyLogout() {
        Intent i= new Intent(this,MainActivity.class);
        startActivity(i);
    }

    private void xuLyDoiMatKhau() {
        Intent i = new Intent(this, activity_doi_mat_khau.class);
        i.putExtra("MSNV", maNhanVien);
        startActivity(i);
    }

    private void addControls() {

        quyenTruyCap = getIntent().getIntExtra("QUYEN_TRUY_CAP", 1);
        maNhanVien = getIntent().getStringExtra("MSNV");

        Intent intent1 = new Intent(this, activity_tab1_menu.class);
        intent1.putExtra("QUYEN_TRUY_CAP", quyenTruyCap);

        Intent intent2 = new Intent(this, activity_tab2_menu.class);
        intent2.putExtra("QUYEN_TRUY_CAP", quyenTruyCap);
        intent2.putExtra("MSNV", maNhanVien);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec tab1 = tabHost.newTabSpec("NHÂN VIÊN");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("TÍNH CÔNG");

//imgemployess
        tab1.setIndicator("", getResources().getDrawable(R.drawable.aa));
        tab1.setContent(intent1);


        tab2.setIndicator("", getResources().getDrawable(R.drawable.aaa));
        tab2.setContent(intent2);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.WHITE);////////// mặc định ban đầu trắng nếu chọn
        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#E98C02"));//////////#ff5a01

        maNhanVien = getIntent().getStringExtra("MSNV");
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar2);
        txtHoTen = (TextView) findViewById(R.id.txtHoTen1);
        txtMaNhanVien = (TextView) findViewById(R.id.etmanv);
        txtPhongBan = (TextView) findViewById(R.id.txtPhongBan);
        lnlDoiMatKhau= (LinearLayout) findViewById(R.id.lnlDoiMatKhau);
        btnLogout= (ImageButton) findViewById(R.id.btnLogout);
        loadThongTinNhanVien();
    }

    private void loadThongTinNhanVien() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = null;
        try {
            String hoTen, phongBan;
            hoTen = phongBan = "";
            byte[] avatar = null;
            //cursor = database.rawQuery("select HoTen,PhongBan,Hinh from NhanVien where MSNV=" + maNhanVien,null);
            cursor = database.rawQuery(String.format("select HoTen,PhongBan,Hinh from NhanVien where MSNV=%s", maNhanVien),null);


            while (cursor.moveToNext()) {
                hoTen = cursor.getString(0);
                phongBan = cursor.getString(1);
                avatar = cursor.getBlob(2);
            }
            if (avatar == null)
                imgAvatar.setImageResource(R.drawable.imgavatar);
            else {
                Bitmap bmImage = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
                imgAvatar.setImageBitmap(bmImage);
            }
            txtHoTen.setText("  "+hoTen);
            txtMaNhanVien.setText(maNhanVien);
            txtPhongBan.setText("  "+phongBan);
            if(phongBan.equals("Tất Cả"))
                imgPhongBan.setImageResource(R.drawable.all_icon);
            else if(phongBan.equals("Phòng Tài Chính"))
                imgPhongBan.setImageResource(R.drawable.tai_chinh_icon);
            else if(phongBan.equals("Phòng Kỹ Thuật"))
                imgPhongBan.setImageResource(R.drawable.ky_thuat_icon);
            else if(phongBan.equals("Phòng Nhân Sự"))
                imgPhongBan.setImageResource(R.drawable.nhan_su_icon);
            else if(phongBan.equals("Phòng Kinh Doanh"))
                imgPhongBan.setImageResource(R.drawable.kinh_doanh_icon);
            cursor.close();
        } catch (Exception ex) {

        }
    }
}