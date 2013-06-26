
package aeviou.compiler;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Scanner;

public class CompilerUtils {
    public static String PINYIN_LIST_FILE;
    public static HashMap<String, Character> pinyinMap;

    public static void writeInteger(int value, RandomAccessFile file){
        try{
            file.writeByte(value >> 24);
            file.writeByte((value & 0x00FFFFFF) >> 16);
            file.writeByte((value & 0x0000FFFF) >> 8);
            file.writeByte(value & 0x000000FF);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void writeChar(char value, RandomAccessFile file){
        try{
            file.writeByte((value & 0x0000FFFF) >> 8);
            file.writeByte(value & 0x000000FF);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static char pinyinToChar(String pinyin){
        if (pinyinMap == null){
            importPinyin();
        }
        Character value = pinyinMap.get(pinyin);
        if (value == null){
            return (char)-1;
        }
        return value.charValue();
    }

    public static int getPinyinCount(){
        if (pinyinMap == null){
            importPinyin();
        }
        return pinyinMap.size();
    }

    private static void importPinyin(){
        String[] line;
        pinyinMap = new HashMap<String, Character>();
        try{
            Scanner pinyinFile = new Scanner
                    (new File(PINYIN_LIST_FILE));
            while (pinyinFile.hasNext()){
                line = pinyinFile.nextLine().split(" ");
                String pinyin = line[0];
                if (pinyin.charAt(0) == '\''){
                    pinyin = pinyin.substring(1);
                }
                pinyinMap.put(pinyin, (char)pinyinMap.size());
            }
            pinyinFile.close();
       }catch(Exception ex){
           ex.printStackTrace();
       }
    }
}
