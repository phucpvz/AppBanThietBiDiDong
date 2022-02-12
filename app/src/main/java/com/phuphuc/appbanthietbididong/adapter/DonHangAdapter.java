package com.phuphuc.appbanthietbididong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phuphuc.appbanthietbididong.R;
import com.phuphuc.appbanthietbididong.model.DonHang;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> donHangList;

    public DonHangAdapter(Context context, List<DonHang> donHangList) {
        this.context = context;
        this.donHangList = donHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = donHangList.get(position);
        holder.txtIdDonHang.setText("Đơn hàng: " + donHang.getId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rvChiTiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());

        ChiTietAdapter chiTietAdapter = new ChiTietAdapter(context, donHang.getItem());
        holder.rvChiTiet.setLayoutManager(layoutManager);
        holder.rvChiTiet.setAdapter(chiTietAdapter);
        holder.rvChiTiet.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return donHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtIdDonHang;
        RecyclerView rvChiTiet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdDonHang = itemView.findViewById(R.id.txtiddonhang);
            rvChiTiet = itemView.findViewById(R.id.recyclerview_chitiet);
        }
    }
}
