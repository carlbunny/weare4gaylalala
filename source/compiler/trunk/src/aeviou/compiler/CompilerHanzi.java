
package aeviou.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class CompilerHanzi {

    private class HanziType implements Comparable{
        String word;
        int frequency;

        public int compareTo(Object o) {
            HanziType node1 = (HanziType)this;
            HanziType node2 = (HanziType)o;

            if (node1.frequency < node2.frequency){
                return 1;
            }else if(node1.frequency == node2.frequency){
                return 0;
            }else{
                return -1;
            }
        }
    }

    private class HanziList{
        public ArrayList<HanziType> hanzis;
        int filePosition;
        int hanziLength;
    }

    private HashMap<String, HanziType> hanziMap;
    private ArrayList<HanziList> lists;

    public CompilerHanzi(){
        lists = new ArrayList<HanziList>();
        hanziMap = new HashMap<String, HanziType>();
    }

    public int createNewList() {
        HanziList node = new HanziList();
        lists.add(node);
        return lists.size() - 1;
    }

    public void updateFrequency(String word, int frequency){
        HanziType hanziType = hanziMap.get(word);
        if (hanziType != null){
            hanziType.frequency = frequency;
        }
    }

    void insertWord(int listIndex, String word, int freq) {
        HanziList list = lists.get(listIndex);
        if (list.hanzis == null){
            list.hanzis = new ArrayList<HanziType>();
            list.hanziLength = word.length();
        }
        HanziType hanziType = new HanziType();
        hanziType.word = word;
        hanziType.frequency = freq;
        list.hanzis.add(hanziType);
        hanziMap.put(word, hanziType);
    }

    public void saveToFile(String filename){
        RandomAccessFile file = null;

        File outFile = new File(filename);
        if (outFile.exists()){
            outFile.delete();
        }

        try {
            file = new RandomAccessFile(new File(filename), "rw");
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        for (HanziList list : lists){
            try {
                list.filePosition = (int)file.getFilePointer();

                CompilerUtils.writeChar((char)list.hanziLength, file);
                CompilerUtils.writeChar((char)list.hanzis.size(), file);

                java.util.Collections.sort(list.hanzis);

                for (HanziType wordType : list.hanzis){
                    for (int i = 0; i < list.hanziLength; i++){
                        char character = wordType.word.charAt(i);
                        CompilerUtils.writeChar(character, file);
                    }
                    CompilerUtils.writeInteger(wordType.frequency, file);
                }

                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    int getListFilePosition(int index){
        return lists.get(index).filePosition;
    }


    int getMaxFreqency(int index) {
        HanziList list = lists.get(index);
        if (list.hanzis != null && list.hanzis.size() > 0){
            return list.hanzis.get(0).frequency;
        }
        return -1;
    }
}
