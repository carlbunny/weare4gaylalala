package com.aeviou.pinyin;

public class PinyinUtils {
    public static String PINYIN_LIST_FILE;

    public static int getInteger(byte[] b, int offset){
        int ret = 0;
        int t;
        for (int i = 0; i < 4; i++){
            ret = ret << 8;
            t = b[offset + i];
            if (t < 0){
                t = 256 + t;
            }
            ret |= t;
        }
        return ret;
    }

    public static char getChar(byte[] b, int offset){
        int ret = 0;
        int t;
        for (int i = 0; i < 2; i++){
            ret = ret << 8;
            t = b[offset + i];
            if (t < 0){
                t = 256 + t;
            }
            ret |= t;
        }
        return (char)ret;
    }
}
