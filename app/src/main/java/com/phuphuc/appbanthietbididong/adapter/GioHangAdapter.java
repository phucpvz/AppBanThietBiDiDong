package com.phuphuc.appbanthietbididong.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phuphuc.appbanthietbididong.Interface.IImageClickListener;
import com.phuphuc.appbanthietbididong.Interface.ItemClickListener;
import com.phuphuc.appbanthietbididong.R;
import com.phuphuc.appbanthietbididong.activity.ChiTietActivity;
import com.phuphuc.appbanthietbididong.model.EventBus.TinhTongEvent;
import com.phuphuc.appbanthietbididong.model.GioHang;
import com.phuphuc.appbanthietbididong.model.SanPhamMoi;
import com.phuphuc.appbanthietbididong.utils.Utils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {

    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.txtTen.setText(gioHang.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGia.setText(decimalFormat.format(gioHang.getGiasp() / gioHang.getSoluong()));
        holder.txtSoLuong.setText(gioHang.getSoluong() + "");
        Picasso.get().load(gioHang.getHinhsp()).into(holder.imgHinh);
        long thanhTien = gioHang.getGiasp();
        holder.txtThanhTien.setText(decimalFormat.format(thanhTien));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (Utils.muaHangList.contains(gioHang)) return;
                    Utils.muaHangList.add(gioHang);
                }
                else {
                    Utils.muaHangList.remove(gioHang);
                }
                EventBus.getDefault().postSticky(new TinhTongEvent());
            }
        });
        holder.setImageClickListener(new IImageClickListener() {
            @Override
            public void onImageClick(View view, int position, int value) {
                int soLuongCu = gioHangList.get(position).getSoluong();
                int soLuongMoi = soLuongCu;
                if (value == 1) {
                    if (gioHangList.get(position).getSoluong() > 1) {
                        soLuongMoi = soLuongCu - 1;
                    }
                    else if (gioHangList.get(position).getSoluong() == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Xác nhận");
                        builder.setMessage("Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng không?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.gioHangList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                } else if (value == 2) {
                    if (gioHangList.get(position).getSoluong() < 10) {
                        soLuongMoi = soLuongCu + 1;
                    }
                }
                // Kiểm tra cho trường hợp xóa
                if (gioHangList.get(position) != null) {
                    gioHangList.get(position).setSoluong(soLuongMoi);
                    gioHangList.get(position).setGiasp(gioHangList.get(position).getGiasp() * soLuongMoi / soLuongCu);
                    holder.txtSoLuong.setText(gioHangList.get(position).getSoluong() + "");
                    long thanhTien = gioHangList.get(position).getGiasp();
                    holder.txtThanhTien.setText(decimalFormat.format(thanhTien));
                }
                EventBus.getDefault().postSticky(new TinhTongEvent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgHinh, imgTru, imgCong;
        TextView txtTen, txtGia, txtSoLuong, txtThanhTien;
        IImageClickListener imageClickListener;
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHinh = itemView.findViewById(R.id.item_giohang_hinhsp);
            txtTen = itemView.findViewById(R.id.item_giohang_tensp);
            txtGia = itemView.findViewById(R.id.item_giohang_giasp);
            txtSoLuong = itemView.findViewById(R.id.item_giohang_soluong);
            txtThanhTien = itemView.findViewById(R.id.item_giohang_thanhtien);
            imgTru = itemView.findViewById(R.id.item_giohang_tru);
            imgCong = itemView.findViewById(R.id.item_giohang_cong);
            imgCong.setOnClickListener(this);
            imgTru.setOnClickListener(this);
            checkBox = itemView.findViewById(R.id.item_giohang_check);
        }

        public void setImageClickListener(IImageClickListener imageClickListener) {
            this.imageClickListener = imageClickListener;
        }

        @Override
        public void onClick(View view) {
            if (view == imgTru) {
                imageClickListener.onImageClick(view, getAdapterPosition(), 1);
            }
            else if (view == imgCong){
                imageClickListener.onImageClick(view, getAdapterPosition(), 2);
            }
        }
    }
}
