package com.phuphuc.appbanthietbididong.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.phuphuc.appbanthietbididong.R;
import com.phuphuc.appbanthietbididong.adapter.DienThoaiAdapter;
import com.phuphuc.appbanthietbididong.model.SanPhamMoi;
import com.phuphuc.appbanthietbididong.retrofit.ApiBanHang;
import com.phuphuc.appbanthietbididong.retrofit.RetrofitClient;
import com.phuphuc.appbanthietbididong.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoaiActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    DienThoaiAdapter dienThoaiAdapter;
    List<SanPhamMoi> sanPhamList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai", 1);
        AnhXa();
        ActionToolbar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamList.size()-1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                sanPhamList.add(null);
                dienThoaiAdapter.notifyItemInserted(sanPhamList.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sanPhamList.remove(sanPhamList.size() - 1);
                dienThoaiAdapter.notifyItemRemoved(sanPhamList.size());
                ++page;
                getData(page);
                dienThoaiAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page, loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                if (dienThoaiAdapter == null) {
                                    sanPhamList = sanPhamMoiModel.getResult();
                                    dienThoaiAdapter = new DienThoaiAdapter(getApplicationContext(), sanPhamList);
                                    recyclerView.setAdapter(dienThoaiAdapter);
                                }
                                else {
                                    int vitri = sanPhamList.size()-1;
                                    int soluongadd = sanPhamMoiModel.getResult().size();
                                    for (int i = 0; i < soluongadd; i++){
                                        sanPhamList.add(sanPhamMoiModel.getResult().get(i));
                                    }
                                    dienThoaiAdapter.notifyItemRangeInserted(vitri, soluongadd);
                                    isLoading = false;
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Đã hết dữ liệu!", Toast.LENGTH_SHORT).show();
                                isLoading = true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không kết nối được tới server!\n" + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
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
        String title = "";
        switch (loai) {
            case 1:
                title = "Điện thoại";
                break;
            case 2:
                title = "Laptop";
                break;
        }
        toolbar.setTitle(title);
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbardienthoai);
        recyclerView = findViewById(R.id.recyclerviewdienthoai);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}