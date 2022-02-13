package com.phuphuc.appbanthietbididong.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    EditText edtSearch;
    DienThoaiAdapter dienThoaiAdapter;
    List<SanPhamMoi> sanPhamList;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        ActionToolbar();
    }

    private void initView() {
        sanPhamList = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        edtSearch = findViewById(R.id.edtSearch);
        toolbar = findViewById(R.id.toolbarSearch);
        recyclerView = findViewById(R.id.recyclerview_search);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sanPhamList.clear();
                if (charSequence.length() == 0) {
                    dienThoaiAdapter = new DienThoaiAdapter(getApplicationContext(), sanPhamList);
                    recyclerView.setAdapter(dienThoaiAdapter);
                }
                else {
                    getDataSearch(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getDataSearch(String search) {
        compositeDisposable.add(apiBanHang.timKiem(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if (sanPhamMoiModel.isSuccess()) {
                                sanPhamList = sanPhamMoiModel.getResult();
                            }
                            dienThoaiAdapter = new DienThoaiAdapter(getApplicationContext(), sanPhamList);
                            recyclerView.setAdapter(dienThoaiAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
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
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}