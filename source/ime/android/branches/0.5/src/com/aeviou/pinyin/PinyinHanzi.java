
package com.aeviou.pinyin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class PinyinHanzi {
    public static final int MAX_CACHE_SIZE = 100;
    HashMap<Integer, String>firstCache;

    String filename;
    RandomAccessFile file = null;
    byte[] readBuffer = new byte[4];

    public void clearCache(){
        firstCache.clear();
    }

    public void fileOpen(){
        try {
            file = new RandomAccessFile(new File(filename), "r");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void fileClose(){
        if (file == null) return;
        try {
            file.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        file = null;
    }

    PinyinHanzi(String filename) {
        this.filename = filename;
        firstCache = new HashMap<Integer, String>();
    }

    String getFirst(int offset) {
        String value = firstCache.get(offset);
        if (value != null){
            return value;
        }
        
        try{
            file.seek(offset);
            file.read(readBuffer);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        int length = PinyinUtils.getChar(readBuffer, 0);

        byte[] listBuffer = new byte[length + length + 4];
        try{
            file.read(listBuffer);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        StringBuffer sb = new StringBuffer();
        char c;
        for (int i = 0; i < length; i++){
            c = PinyinUtils.getChar(listBuffer, i + i);
            sb.append(c);
        }

        String ret = sb.toString();
        if (firstCache.size() < MAX_CACHE_SIZE){
            firstCache.put(offset, ret);
        }
        return ret;
    }

    void getAll(int offset, ArrayList<CandidateType> list){
        try{
            file.seek(offset);
            file.read(readBuffer);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        int length = PinyinUtils.getChar(readBuffer, 0);
        int size = PinyinUtils.getChar(readBuffer, 2);

        byte[] listBuffer = new byte[size * (length + length + 4)];
        try{
            file.read(listBuffer);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        StringBuffer sb = new StringBuffer();
        char c;
        int address = 0;
        for (int k = 0; k < size; k++){
            sb.delete(0, length);
            for (int i = 0; i < length; i++){
                c = PinyinUtils.getChar(listBuffer, address + i + i);
                sb.append(c);
            }
            int freqency = PinyinUtils.getInteger(listBuffer, address + length + length);
            CandidateType candidate = new CandidateType();
            candidate.word = sb.toString();
            candidate.frequency = freqency;
            candidate.length = length;
            list.add(candidate);
            address += length + length + 4;
        }
    }
}
