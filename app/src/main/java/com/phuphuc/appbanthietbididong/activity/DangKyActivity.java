package com.phuphuc.appbanthietbididong.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.phuphuc.appbanthietbididong.R;
import com.phuphuc.appbanthietbididong.adapter.SanPhamMoiAdapter;
import com.phuphuc.appbanthietbididong.retrofit.ApiBanHang;
import com.phuphuc.appbanthietbididong.retrofit.RetrofitClient;
import com.phuphuc.appbanthietbididong.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {

    EditText txtEmail,txtUsername, txtPassword, txtRepass, txtMobile;
    AppCompatButton btnDangKy;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControl();
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtEmail = findViewById(R.id.txtemail);
        txtUsername = findViewById(R.id.txtusername);
        txtPassword = findViewById(R.id.txtpassword);
        txtRepass = findViewById(R.id.txtrepass);
        txtMobile = findViewById(R.id.txtmobile);
        btnDangKy = findViewById(R.id.btnDangKy);
    }

    private void initControl() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangKy();
            }
        });
    }

    private void dangKy() {
        String email = txtEmail.getText().toString().trim();
        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String repass = txtRepass.getText().toString().trim();
        String mobile = txtMobile.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Bạn chưa nhập email!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Bạn chưa nhập username!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Bạn chưa nhập password!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(repass)) {
            Toast.makeText(this, "Bạn chưa nhập repass!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "Bạn chưa nhập mobile!", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(repass)) {
            Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
        } else {
            compositeDisposable.add(apiBanHang.dangKy(email, password, username, mobile)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            userModel -> {
                                if (userModel.isSuccess()) {
                                    Toast.makeText(getApplicationContext(), "Đăng ký tài khoản thành công!", Toast.LENGTH_LONG).show();
                                    Utils.current_user.setEmail(email);
                                    Utils.current_user.setPassword(password);
                                    Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                    ));
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}