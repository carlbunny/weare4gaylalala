
package com.aeviou.pinyin;

import java.io.File;
import java.io.RandomAccessFile;

public class PinyinRoot {
    private int[] index;

    public PinyinRoot(String filename){
        try {
            RandomAccessFile file = new RandomAccessFile(new File(filename), "r");
            byte[] buffer = new byte[(int)file.length()];
            file.seek(0);
            file.read(buffer);
            file.close();
            index = new int [buffer.length / 4];
            int p = 0;
            for (int i = 0; i < index.length; i++){
                index[i] = PinyinUtils.getInteger(buffer, p);
                if (index[i] == -1){
                    System.out.println(i);
                }
                p += 4;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getIndex(char pinyin){
        if (pinyin < 0 || pinyin >= index.length){
            return -1;
        }
        return index[pinyin];
    }
}
