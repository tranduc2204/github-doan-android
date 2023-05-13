package Model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.nhom4_tranduc_ntbp.R;
import com.example.nhom4_tranduc_ntbp.activity_hien_thi_thong_tin;
import com.example.nhom4_tranduc_ntbp.activity_sua_chua_thong_tin;
import com.example.nhom4_tranduc_ntbp.activity_xoa_nhan_vien;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NhanVienAdapter extends ArrayAdapter<NhanVien> {
    Activity context;
    int resource;
    List<NhanVien> objects;
    ArrayList<NhanVien> dsNhanVien;
    int quyenTruyCap=1;

    public NhanVienAdapter(Activity context, int resource, List<NhanVien> objects, int quyenTruyCap) {
        super(context, resource, objects);

        this.context= context;
        this.resource= resource;
        this.objects= objects;
        this.dsNhanVien= new ArrayList<>();
        this.dsNhanVien.addAll(objects);
        this.quyenTruyCap=quyenTruyCap;
    }

    @NonNull
    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);

        ImageView imgAvatar= (ImageView) row.findViewById(R.id.imgAvatar2);
        TextView txtMsnv= (TextView) row.findViewById(R.id.txtMsnv1);
        TextView txtHoTen= (TextView) row.findViewById(R.id.txtHoTen1);
        ImageButton btnInfo= (ImageButton) row.findViewById(R.id.btnInfo1);
        ImageButton btnUpdate= (ImageButton) row.findViewById(R.id.btnUpdate1);
        final ImageButton btnDelete= (ImageButton) row.findViewById(R.id.btnDelete1);

        final NhanVien nhanVien=this.objects.get(position);
        txtHoTen.setText(nhanVien.getHoTen());
        txtMsnv.setText(nhanVien.getMaNhanVien());
        if(nhanVien.getHinhAnh()==null)
            imgAvatar.setImageResource(R.drawable.imgavatar);
        else {
            Bitmap bmImage = BitmapFactory.decodeByteArray(nhanVien.getHinhAnh(), 0, nhanVien.getHinhAnh().length);
            imgAvatar.setImageBitmap(bmImage);
        }
        if(quyenTruyCap!=1){
            btnUpdate.setBackgroundResource(R.drawable.item_blurry);
            btnDelete.setBackgroundResource(R.drawable.item_blurry);
        }

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getContext(), activity_hien_thi_thong_tin.class);
                i.putExtra("KIEU_NHAN_VIEN",nhanVien);
                getContext().startActivity(i);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quyenTruyCap==1) {
                    Intent i = new Intent(getContext(), activity_sua_chua_thong_tin.class);
                    i.putExtra("KIEU_NHAN_VIEN", nhanVien);
                    getContext().startActivity(i);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quyenTruyCap==1) {
                    Intent i = new Intent(getContext(), activity_xoa_nhan_vien.class);
                    i.putExtra("KIEU_NHAN_VIEN", nhanVien);
                    getContext().startActivity(i);
                }
            }
        });
        return row;
    }
    public void filter(String noiDungAuto){
        noiDungAuto= noiDungAuto.toLowerCase(Locale.getDefault());
        objects.clear();
        if (noiDungAuto.length()==0) {
            objects.addAll(dsNhanVien);
        }
        else {
            for (NhanVien nv: dsNhanVien){
                if (nv.getMaNhanVien().toLowerCase(Locale.getDefault()).contains(noiDungAuto) ||
                        nv.getHoTen().toLowerCase(Locale.getDefault()).contains(noiDungAuto))
                {
                    objects.add(nv);
                }
            }
        }
        notifyDataSetChanged();
    }
}
