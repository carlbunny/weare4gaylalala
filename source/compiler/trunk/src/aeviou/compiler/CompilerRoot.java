package aeviou.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CompilerRoot {
    private int[] rootsIndex;
    private CompilerNode tree;

    public CompilerRoot(CompilerNode tree){
        this.tree = tree;
        rootsIndex = new int[CompilerUtils.getPinyinCount()];
        for (int i = 0; i < rootsIndex.length; i++){
            rootsIndex[i] = -1;
        }
    }

    int getIndex(String pinyin) {
        char cPinyin = CompilerUtils.pinyinToChar(pinyin);
        if (cPinyin == 65535){
            System.out.println("Error: pinyin not found :" + pinyin);
        }
        int value = rootsIndex[cPinyin];
        if (value == -1){
            value = tree.createNewRoot(pinyin);
            rootsIndex[cPinyin] = value;
        }
        return value;
    }

    void saveToFile(String filename) {
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

        for (int i = 0; i < rootsIndex.length; i++){
            int address = -1;
            if (rootsIndex[i] != -1){
                address = tree.getRootPosition(rootsIndex[i]);
            }else{
                System.out.println("Warning: there is no hanzi for pinyin, index:" +
                                   String.valueOf(i));
            }
            CompilerUtils.writeInteger(address, file);
        }
        try {
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
