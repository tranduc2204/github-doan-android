package com.example.nhom4_tranduc_ntbp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

//Activity AppCompatActivity
public class activity_bieu_do_nhan_vien extends AppCompatActivity {

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    ImageButton btnBack, btnOptionMenu;
    TextView txtTitle;
    EditText txtThuocTinh;

    String thuocTinh="PHONGBAN";

    private static String TAG = "MainActivity";
    private float[] yData;
    private String[] xData;
    PieChart pieChartx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bieu_do_nhan_vien);

        xuLySaoChepCSDLTuAsssetsVaoHeThongMobile();
        addControls();
        addEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getParent().getMenuInflater();
        inflater.inflate(R.menu.menu_ve_bieu_do, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        xuLyOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);
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
        setTitle("BIỂU ĐỒ NHÂN VIÊN");
        bieuDoPhongBan();
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnOptionMenu = (ImageButton) findViewById(R.id.btnOptionMenu2);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
    }

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

    private void xuLyBack() {
        finish();

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

    private void bieuDoPhongBan() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        try {
            Cursor cTong = database.rawQuery("select * from NhanVien", null);
            float tong = cTong.getCount();
            xData = new String[]{"Tài Chính", "Kỹ Thuật", "Nhân Sự", "Kinh Doanh"};
            Cursor cTaiChinh = database.rawQuery("select * from NhanVien where PhongBan='Phòng Tài Chính'", null);
            Cursor cKyThuat = database.rawQuery("select * from NhanVien where PhongBan='Phòng Kỹ Thuật'", null);
            Cursor cNhanSu = database.rawQuery("select * from NhanVien where PhongBan='Phòng Nhân Sự'", null);
            Cursor cKinhDoanh = database.rawQuery("select * from NhanVien where PhongBan='Phòng Kinh Doanh'", null);
            float phanTramTaiChinh = (((float) cTaiChinh.getCount() / tong) * 100);
            float phanTramKyThuat = (((float) cKyThuat.getCount() / tong) * 100);
            float phanTramNhanSu = (((float) cNhanSu.getCount() / tong) * 100);
            float phanTramKinhDoanh = (((float) cKinhDoanh.getCount() / tong) * 100);

            yData = new float[]{phanTramTaiChinh, phanTramKyThuat, phanTramNhanSu, phanTramKinhDoanh};
        } catch (Exception ex) {
        }

        Log.d(TAG, "onCreate: starting on create chart");
        pieChartx = (PieChart) findViewById(R.id.idPieChart1);
        pieChartx.setDescription("Biểu đồ nhân viên");
        pieChartx.setRotationEnabled(true);
        pieChartx.setHoleRadius(35f);//25
        pieChartx.setTransparentCircleAlpha(0);
        pieChartx.setCenterText("PHÒNG BAN");
        pieChartx.setCenterTextSize(20);//10
        pieChartx.setDrawEntryLabels(true);
        addDataSet();
        pieChartx.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                int pos1 = e.toString().indexOf("(sum): ");
                String sales = e.toString().substring(pos1 + 7);

                for (int i = 0; i < yData.length; i++) {
                    if (yData[i] == Float.parseFloat(sales)) {
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1];
                Toast.makeText(activity_bieu_do_nhan_vien.this, "Phòng " + employee + "\n" + "Chiếm: " + sales + "% tổng số nhân viên", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });


    }

    private void addDataSet() {
        Log.d(TAG, "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntrys.add(new PieEntry(yData[i], xData[i]));
        }

        for (int i = 0; i < xData.length; i++) {
            xEntrys.add(new String(xData[i]));
        }

        PieDataSet pieDataSet = new PieDataSet(yEntrys, "CHÚ THÍCH");
        pieDataSet.setSliceSpace(5);//2
        pieDataSet.setValueTextSize(30);//12

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);//RED
        colors.add(Color.BLUE);//BLUE
        colors.add(Color.GREEN);//GREEN
        colors.add(Color.MAGENTA);//MAGENTA
        colors.add(Color.YELLOW);//YELLOW
        colors.add(Color.CYAN);//CYAN
        colors.add(Color.GRAY);//GRAY

        pieDataSet.setColors(colors);

        Legend legend = pieChartx.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(pieDataSet);
        pieChartx.setData(pieData);
        pieChartx.invalidate();
    }

    private void xuLyOptionsItemSelected(MenuItem item) {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        try {
            Cursor cTong = database.rawQuery("select * from NhanVien", null);
            float tong = cTong.getCount();
            if (item.getItemId() == R.id.mnuPhongBan) {
                xData = new String[]{"Tài Chính", "Kỹ Thuật", "Nhân Sự", "Kinh Doanh"};
                Cursor cTaiChinh = database.rawQuery("select * from NhanVien where PhongBan='Phòng Tài Chính'", null);
                Cursor cKyThuat = database.rawQuery("select * from NhanVien where PhongBan='Phòng Kỹ Thuật'", null);
                Cursor cNhanSu = database.rawQuery("select * from NhanVien where PhongBan='Phòng Nhân Sự'", null);
                Cursor cKinhDoanh = database.rawQuery("select * from NhanVien where PhongBan='Phòng Kinh Doanh'", null);
                float phanTramTaiChinh = ((float) cTaiChinh.getCount() / tong) * 100;
                float phanTramKyThuat = ((float) cKyThuat.getCount() / tong) * 100;
                float phanTramNhanSu = ((float) cNhanSu.getCount() / tong) * 100;
                float phanTramKinhDoanh = ((float) cKinhDoanh.getCount() / tong) * 100;

                yData = new float[]{phanTramTaiChinh, phanTramKyThuat, phanTramNhanSu, phanTramKinhDoanh};
                pieChartx.setCenterText("PHÒNG BAN");
                txtTitle.setText("THỐNG KÊ THEO PHÒNG BAN");


            } else if (item.getItemId() == R.id.mnuGioiTinh) {
                xData = new String[]{"Nam", "Nữ"};
                Cursor cNam = database.rawQuery("Select * from NhanVien where GioiTinh='Nam'", null);
                Cursor cNu = database.rawQuery("Select * from NhanVien where GioiTinh='Nữ'", null);
                float phanTramNam = ((float) cNam.getCount() / tong) * 100;
                float phanTramNu = ((float) cNu.getCount() / tong) * 100;

                yData = new float[]{phanTramNam, phanTramNu};
                pieChartx.setCenterText("GIỚI TÍNH");
                txtTitle.setText("THỐNG KÊ THEO GIỚI TÍNH");

            } else if (item.getItemId() == R.id.mnuHonNhan) {
                xData = new String[]{"Đã kết hôn", "Chưa kết hôn", "Không rõ"};
                Cursor cDaKetHon = database.rawQuery("Select * from NhanVien where HonNhan='Đã kết hôn'", null);
                Cursor cChuaKetHon = database.rawQuery("Select * from NhanVien where HonNhan='Chưa kết hôn'", null);
                Cursor cKhongRo = database.rawQuery("Select * from NhanVien where HonNhan='Không rõ'", null);

                float phanTramDaKetHon = ((float) cDaKetHon.getCount() / tong) * 100;
                float phanTramChuaKetHon = ((float) cChuaKetHon.getCount() / tong) * 100;
                float phanTramKhongRo = ((float) cKhongRo.getCount() / tong) * 100;

                yData = new float[]{phanTramDaKetHon, phanTramChuaKetHon, phanTramKhongRo};
                pieChartx.setCenterText("HÔN NHÂN");
                txtTitle.setText("THỐNG KÊ THEO HÔN NHÂN");
            }
        } catch (Exception ex) {
        }

        Log.d(TAG, "onCreate: starting on create chart");
        pieChartx = (PieChart) findViewById(R.id.idPieChart1);
        pieChartx.setDescription("Biểu đồ nhân viên");
        pieChartx.setRotationEnabled(true);
        pieChartx.setHoleRadius(35f);
        pieChartx.setTransparentCircleAlpha(0);
        pieChartx.setCenterTextSize(20);
        pieChartx.setDrawEntryLabels(true);
        addDataSet();
        pieChartx.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, "onValueSelected: Value select from chart.");
                Log.d(TAG, "onValueSelected: " + e.toString());
                Log.d(TAG, "onValueSelected: " + h.toString());

                int pos1 = e.toString().indexOf("(sum): ");
                String sales = e.toString().substring(pos1 + 7);

                for (int i = 0; i < yData.length; i++) {
                    if (yData[i] == Float.parseFloat(sales)) {
                        pos1 = i;
                        break;
                    }
                }
                String employee = xData[pos1];
                Toast.makeText(activity_bieu_do_nhan_vien.this, "Phòng " + employee + "\n" + "Chiếm: " + sales + "% tổng số nhân viên", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });


    }
}