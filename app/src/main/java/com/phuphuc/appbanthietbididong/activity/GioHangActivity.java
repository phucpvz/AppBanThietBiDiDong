package com.phuphuc.appbanthietbididong.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.phuphuc.appbanthietbididong.R;
import com.phuphuc.appbanthietbididong.adapter.GioHangAdapter;
import com.phuphuc.appbanthietbididong.model.EventBus.TinhTongEvent;
import com.phuphuc.appbanthietbididong.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtGioHangTrong, txtTongTien;
    RecyclerView recyclerView;
    Button btnMuaHang;
    GioHangAdapter gioHangAdapter;
    long tongTien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        tinhTongTien();
    }

    private void tinhTongTien() {
        if (Utils.muaHangList == null) return;
        tongTien = 0;
        for (int i = 0; i < Utils.muaHangList.size(); ++i) {
            tongTien += Utils.muaHangList.get(i).getGiasp();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongTien));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (Utils.gioHangList.size() == 0) {
            txtGioHangTrong.setVisibility(View.VISIBLE);
        }
        else {
            gioHangAdapter = new GioHangAdapter(getApplicationContext(), Utils.gioHangList);
            recyclerView.setAdapter(gioHangAdapter);
        }

        btnMuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                intent.putExtra("tongtien", tongTien);
                Utils.gioHangList.clear();
                startActivity(intent);
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbarGioHang);
        txtGioHangTrong = findViewById(R.id.txtGioHangTrong);
        txtTongTien = findViewById(R.id.txtTongTien);
        recyclerView = findViewById(R.id.recyclerviewGioHang);
        btnMuaHang = findViewById(R.id.btnMuaHang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event) {
        if (event != null) {
            tinhTongTien();
        }
    }
}