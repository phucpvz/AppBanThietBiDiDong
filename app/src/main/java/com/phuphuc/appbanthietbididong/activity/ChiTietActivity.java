package com.phuphuc.appbanthietbididong.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nex3z.notificationbadge.NotificationBadge;
import com.phuphuc.appbanthietbididong.R;
import com.phuphuc.appbanthietbididong.model.GioHang;
import com.phuphuc.appbanthietbididong.model.SanPhamMoi;
import com.phuphuc.appbanthietbididong.utils.Utils;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import static com.phuphuc.appbanthietbididong.utils.Utils.demSoLuongSanPhamGioHang;

public class ChiTietActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtTen, txtGia, txtMoTa;
    Button btnThemVaoGioHang;
    Spinner spinSoLuong;
    ImageView imgHinhAnh;
    SanPhamMoi sanPham;
    NotificationBadge notificationBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolbar();
        initData();
        initControl();
    }

    @Override
    protected void onResume() {
        super.onResume();
        notificationBadge.setText(String.valueOf(Utils.demSoLuongSanPhamGioHang(Utils.gioHangList)));
    }

    private void initControl() {
        btnThemVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if (Utils.gioHangList.size() > 0) {
            boolean isExist = false;
            for (int i = 0; i < Utils.gioHangList.size(); ++i) {
                if (Utils.gioHangList.get(i).getIdsp() == sanPham.getId()) {
                    int soLuong = Integer.parseInt(spinSoLuong.getSelectedItem().toString());
                    Utils.gioHangList.get(i).setSoluong(Utils.gioHangList.get(i).getSoluong() + soLuong);
                    long gia = sanPham.getGiasp() * Utils.gioHangList.get(i).getSoluong();
                    Utils.gioHangList.get(i).setGiasp(gia);
                    isExist = true;
                }
            }
            if (!isExist) {
                int soLuong = Integer.parseInt(spinSoLuong.getSelectedItem().toString());
                long gia = sanPham.getGiasp() * soLuong;
                GioHang gioHang = new GioHang();
                gioHang.setIdsp(sanPham.getId());
                gioHang.setTensp(sanPham.getTensp());
                gioHang.setHinhsp(sanPham.getHinhanh());
                gioHang.setSoluong(soLuong);
                gioHang.setGiasp(gia);
                Utils.gioHangList.add(gioHang);
            }
        } else {
            int soLuong = Integer.parseInt(spinSoLuong.getSelectedItem().toString());
            long gia = sanPham.getGiasp() * soLuong;
            GioHang gioHang = new GioHang();
            gioHang.setIdsp(sanPham.getId());
            gioHang.setTensp(sanPham.getTensp());
            gioHang.setHinhsp(sanPham.getHinhanh());
            gioHang.setSoluong(soLuong);
            gioHang.setGiasp(gia);
            Utils.gioHangList.add(gioHang);
        }
        notificationBadge.setText(String.valueOf(demSoLuongSanPhamGioHang(Utils.gioHangList)));
    }

    private void initData() {
        sanPham = (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        txtTen.setText(sanPham.getTensp().trim());
        txtMoTa.setText(sanPham.getMota());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtGia.setText("Giá: " + decimalFormat.format(sanPham.getGiasp()) + " đ");
        Picasso.get().load(sanPham.getHinhanh()).into(imgHinhAnh);
        Integer[] mangSoLuong = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, mangSoLuong);
        spinSoLuong.setAdapter(arrayAdapter);
    }

    private void initView() {
        txtTen = findViewById(R.id.txtTenSanPham);
        txtGia = findViewById(R.id.txtGiaSanPham);
        txtMoTa = findViewById(R.id.txtMoTaSanPham);
        btnThemVaoGioHang = findViewById(R.id.btnThemVaoGioHang);
        spinSoLuong = findViewById(R.id.spinSoLuong);
        imgHinhAnh = findViewById(R.id.imgHinhAnhSanPham);
        toolbar = findViewById(R.id.toolbarChiTietSanPham);
        notificationBadge = findViewById(R.id.notificationSoLuong);
        FrameLayout frameLayout = findViewById(R.id.frameGioHang);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
            }
        });
        if (Utils.gioHangList != null) {
            notificationBadge.setText(String.valueOf(Utils.demSoLuongSanPhamGioHang(Utils.gioHangList)));
        }
    }

    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}