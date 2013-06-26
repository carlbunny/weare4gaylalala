
package com.aeviou.pinyin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class PinyinLianxiang {
    private static final int INDEX_SIZE = 100;

    String filename;
    RandomAccessFile file = null;
    private int[] indexes = null;
    private String[] defaultWord;
    private byte[] buffer;

    PinyinLianxiang(String filename) {
        buffer = new byte[4];

        this.filename = filename;
        indexes = new int[INDEX_SIZE];
        byte[] readBuffer = new byte[4 * INDEX_SIZE];
        fileOpen();
        try{
            file.read(readBuffer, 0, 4);
            int offset = PinyinUtils.getInteger(readBuffer, 0);
            file.seek(offset);
            file.read(readBuffer);
            for (int i = 0; i < INDEX_SIZE; i++){
                indexes[i] = PinyinUtils.getInteger(readBuffer, 4 * i);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        ArrayList<CandidateType> list = new ArrayList<CandidateType>();
        this.getAll(' ', list);
        defaultWord = new String[list.size()];
        for (int i = 0; i < defaultWord.length; i++){
            defaultWord[i] = list.get(i).word;
        }

        fileClose();
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

    public void getAll(Character lastHanzi, ArrayList<CandidateType> list){
        int wordOffset = getWordOffset(lastHanzi);
        int insertSize = 0;
        if (wordOffset != -1){
            try{
                file.seek(wordOffset);
                file.read(buffer);
                int endOffset = PinyinUtils.getInteger(buffer, 0);
                int size = endOffset - (wordOffset + 4);
                byte[] readBuffer = new byte[size];
                file.read(readBuffer);
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < size; i += 2){
                    char character = PinyinUtils.getChar(readBuffer, i);
                    if (character != ' '){
                        sb.append(character);
                    }else{
                        CandidateType candidate = new CandidateType();
                        candidate.word = sb.toString();
                        list.add(candidate);
                        insertSize++;
                        sb.delete(0, sb.length());
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        int i = 0;
        while (insertSize < 20){
            CandidateType candidate = new CandidateType();
            candidate.word = defaultWord[i];
            list.add(candidate);
            insertSize++;
            i++;
        }
    }

    private int getWordOffset(Character lastHanzi){
        int hanziOffset = indexes[lastHanzi.hashCode() % INDEX_SIZE];
        int wordOffset = -1;
        try{
            file.seek(hanziOffset);
            file.read(buffer);
            int size = PinyinUtils.getInteger(buffer, 0);
            byte[] readBuffer = new byte[size * (6)];
            file.read(readBuffer);
            int i = 0;
            int j = size - 1;
            int m;

            while (i <= j){
                m = (i + j) / 2;
                int offset = m * 6;
                char hanzi = PinyinUtils.getChar(readBuffer, offset);
                if (hanzi > lastHanzi.charValue()){
                    j = m - 1;
                }else if(hanzi < lastHanzi.charValue()){
                    i = m + 1;
                }else{
                    wordOffset = PinyinUtils.getInteger(readBuffer, offset + 2);
                    break;
                }
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }

        return wordOffset;
    }
}
