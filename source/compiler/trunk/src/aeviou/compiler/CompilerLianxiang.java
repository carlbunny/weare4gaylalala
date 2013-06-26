
package aeviou.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

public class CompilerLianxiang {
    private static final int FREQENCY_THREHOLD = 1;
    private static final int SINGLE_FREQENCY_THREHOLD = 1500;
    private static final int INDEX_SIZE = 100;

    private class HanziType implements Comparable{
        String word;
        int freqency;

        public int compareTo(Object o) {
            HanziType node1 = (HanziType)this;
            HanziType node2 = (HanziType)o;

            if (node1.freqency < node2.freqency){
                return 1;
            }else if(node1.freqency == node2.freqency){
                return 0;
            }else{
                return -1;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final HanziType other = (HanziType) obj;
            if ((this.word == null) ? (other.word != null) : !this.word.equals(other.word)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + (this.word != null ? this.word.hashCode() : 0);
            return hash;
        }


    }

    private class HanziList{
        public ArrayList<HanziType> hanzis;
        int startPosition;
        int endPosition;
    }

    private class IndexType{
        ArrayList<Character> lists;
        int filePosition;
    }

    private HashMap<Character, HanziList> maps;
    private IndexType[] indexes;

    public CompilerLianxiang(){
        maps = new HashMap<Character, HanziList>();
        indexes = new IndexType[INDEX_SIZE];
        for (int i = 0; i < INDEX_SIZE; i++){
            indexes[i] = new IndexType();
            indexes[i].lists = new ArrayList<Character>();
        }
    }

    public void insertWord(String word, int freqency){
        if (freqency < FREQENCY_THREHOLD){
            return;
        }
        if (word == null){
            return;
        }
        if (word.length() == 0){
            return;
        }
        if (word.length() == 1){
            if (freqency < SINGLE_FREQENCY_THREHOLD){
                return;
            }
            HanziType hanzi = new HanziType();
            hanzi.word = word;
            hanzi.freqency = freqency;
            HanziList list= maps.get(' ');
            if (list == null){
                list = new HanziList();
                list.hanzis = new ArrayList<HanziType>();
                maps.put(' ', list);
                indexes[new Character(' ').hashCode() % INDEX_SIZE].lists.add(' ');
            }
            boolean find = false;
            for (int i = 0; i < list.hanzis.size(); i++){
                if (list.hanzis.get(i).equals(hanzi)){
                    find = true;
                    break;
                }
            }
            if (!find){
                list.hanzis.add(hanzi);
            }
        }else{
            Character key = word.charAt(0);
            String remain = word.substring(1);
            HanziType hanzi = new HanziType();
            hanzi.word = remain;
            hanzi.freqency = freqency;
            HanziList list= maps.get(key);
            if (list == null){
                list = new HanziList();
                list.hanzis = new ArrayList<HanziType>();
                maps.put(key, list);
                indexes[new Character(key).hashCode() % INDEX_SIZE].lists.add(key);
            }
            boolean find = false;
            for (int i = 0; i < list.hanzis.size(); i++){
                if (list.hanzis.get(i).equals(hanzi)){
                    find = true;
                    break;
                }
            }
            if (!find){
                list.hanzis.add(hanzi);
            }
        }
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

        try{
            CompilerUtils.writeInteger(0, file);

            for (HanziList list : maps.values()){
                list.startPosition = (int)file.getFilePointer();
                CompilerUtils.writeInteger(list.endPosition, file);

                java.util.Collections.sort(list.hanzis);

                for (HanziType wordType : list.hanzis){
                    for (int i = 0; i < wordType.word.length(); i++){
                        char character = wordType.word.charAt(i);
                        CompilerUtils.writeChar(character, file);
                    }
                    CompilerUtils.writeChar(' ', file);
                }
                
                list.endPosition = (int)file.getFilePointer();
                file.seek(list.startPosition);
                CompilerUtils.writeInteger(list.endPosition, file);
                file.seek(list.endPosition);
            }

            for (IndexType index : indexes){
                index.filePosition = (int)file.getFilePointer();
                CompilerUtils.writeInteger(index.lists.size(), file);
                java.util.Collections.sort(index.lists);
                for (Character hanzi : index.lists){
                    CompilerUtils.writeChar(hanzi, file);
                    CompilerUtils.writeInteger(maps.get(hanzi).startPosition, file);
                }
            }

            int filePosition = (int)file.getFilePointer();
            file.seek(0);
            CompilerUtils.writeInteger(filePosition, file);
            file.seek(filePosition);
            for (IndexType index : indexes){
                CompilerUtils.writeInteger(index.filePosition, file);
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }

        try {
            file.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
