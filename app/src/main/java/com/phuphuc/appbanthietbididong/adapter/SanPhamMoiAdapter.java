package com.phuphuc.appbanthietbididong.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.phuphuc.appbanthietbididong.Interface.ItemClickListener;
import com.phuphuc.appbanthietbididong.R;
import com.phuphuc.appbanthietbididong.activity.ChiTietActivity;
import com.phuphuc.appbanthietbididong.model.SanPhamMoi;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {

    Context context;
    List<SanPhamMoi> sanPhamMoiList;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> sanPhamMoiList) {
        this.context = context;
        this.sanPhamMoiList = sanPhamMoiList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanphammoi, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = sanPhamMoiList.get(position);
        holder.txtTenSanPhamMoi.setText(sanPhamMoi.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGiaSanPhamMoi.setText("Giá: " + decimalFormat.format(sanPhamMoi.getGiasp()) + " đ");
//        Glide.with(context).load(sanPhamMoi.getHinhanh()).dontAnimate().into(holder.imgHinhAnhSanPhamMoi);
        Picasso.get().load(sanPhamMoi.getHinhanh()).into(holder.imgHinhAnhSanPhamMoi);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick){
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet", sanPhamMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sanPhamMoiList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTenSanPhamMoi, txtGiaSanPhamMoi;
        ImageView imgHinhAnhSanPhamMoi;

        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenSanPhamMoi = itemView.findViewById(R.id.textviewtenspmoi);
            txtGiaSanPhamMoi = itemView.findViewById(R.id.textviewgiaspmoi);
            imgHinhAnhSanPhamMoi = itemView.findViewById(R.id.imageviewhinhspmoi);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
