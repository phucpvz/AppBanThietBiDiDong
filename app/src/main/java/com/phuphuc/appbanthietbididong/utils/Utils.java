package com.phuphuc.appbanthietbididong.utils;

import com.phuphuc.appbanthietbididong.model.GioHang;
import com.phuphuc.appbanthietbididong.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL = "http://192.168.1.8/appbanthietbididong/";
    public static List<GioHang> gioHangList;
    public static List<GioHang> muaHangList = new ArrayList<>();
    public static User current_user = new User();

    public static int demSoLuongSanPhamGioHang(List<GioHang> list) {
        int total = 0;
        for (int i = 0; i < list.size(); ++i) {
            total += list.get(i).getSoluong();
        }
        return total;
    }
}
