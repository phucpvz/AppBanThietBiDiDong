package com.phuphuc.appbanthietbididong.utils;

import com.phuphuc.appbanthietbididong.model.GioHang;
import com.phuphuc.appbanthietbididong.model.User;

import java.util.List;

public class Utils {
    public static final String BASE_URL = "http://192.168.1.9/appbanthietbididong/";
    public static List<GioHang> gioHangList;
    public static User current_user = new User();

    public static int demSoLuongSanPhamGioHang() {
        int total = 0;
        for (int i = 0; i < Utils.gioHangList.size(); ++i) {
            total += Utils.gioHangList.get(i).getSoluong();
        }
        return total;
    }
}
