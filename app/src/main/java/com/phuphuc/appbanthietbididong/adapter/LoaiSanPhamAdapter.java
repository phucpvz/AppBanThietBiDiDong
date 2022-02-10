package com.phuphuc.appbanthietbididong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.phuphuc.appbanthietbididong.R;
import com.phuphuc.appbanthietbididong.model.LoaiSanPham;

import java.util.List;

public class LoaiSanPhamAdapter extends BaseAdapter {

    Context context;
    List<LoaiSanPham> loaiSanPhamList;

    public LoaiSanPhamAdapter(Context context, List<LoaiSanPham> loaiSanPhamList) {
        this.context = context;
        this.loaiSanPhamList = loaiSanPhamList;
    }

    @Override
    public int getCount() {
        return loaiSanPhamList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder {
        ImageView imgHinhAnhLoaiSP;
        TextView txtTenLoaiSP;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_sanpham, null);
            viewHolder = new ViewHolder();
            viewHolder.txtTenLoaiSP = view.findViewById(R.id.textviewtenloaisp);
            viewHolder.imgHinhAnhLoaiSP = view.findViewById(R.id.imageviewhinhloaisp);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.txtTenLoaiSP.setText(loaiSanPhamList.get(i).getTensanpham());
        Glide.with(context).load(loaiSanPhamList.get(i).getHinhanh()).into(viewHolder.imgHinhAnhLoaiSP);
        return view;
    }
}
