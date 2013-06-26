
package aeviou.compiler;

import java.io.File;
import java.util.Scanner;

public class CompilerMain {
    public static final int MAX_WORD_LENGTH = 6;

    public static final String INPUT_WORD_FILE = "res\\merge.txt";
    public static final String INPUT_PINYIN_FILE = "res\\pinyin_list.txt";
    
    public static final String OUTPUT_ROOT_FILE = "proot.jpg";
    public static final String OUTPUT_NODE_FILE = "pnode.jpg";
    public static final String OUTPUT_HANZI_FILE = "phanzi.jpg";
    public static final String OUTPUT_LIANXIANG_FILE = "plx.jpg";

    public static void main(String[] args) {
        CompilerUtils.PINYIN_LIST_FILE = INPUT_PINYIN_FILE;
        
        CompilerManager manager = new CompilerManager();

        int characterCount = 0;
        int wordCount = 0;
        int droppedCount = 0;
        try{
            Scanner wordFile = new Scanner
                    (new File(INPUT_WORD_FILE));
            System.out.println("Read " + INPUT_WORD_FILE + ", please wait...");
            while (wordFile.hasNext()){
                String line = wordFile.nextLine();
                String[] splitedLine = line.split(" ");
                if (splitedLine.length == 3){
                    String word = splitedLine[0];
                    String freqency = splitedLine[2];
                    String pinyin[] = splitedLine[1].split("\'");
                    if (pinyin.length == word.length() && pinyin.length != 0){
                        if (pinyin.length <= MAX_WORD_LENGTH){
                            if (pinyin.length == 1){
                                characterCount++;
                            }else{
                                wordCount++;
                            }
                            manager.insertWord(pinyin, word, Integer.parseInt(freqency));
                        }else{
                            droppedCount++;
                        }
                    }else{
                        System.out.println("Warning: pinyinLen != wordLen: "
                                            + INPUT_WORD_FILE + ":" + line);
                    }
                }else{
                    System.out.println("Warning: Cannot handle line in: "
                                        + INPUT_WORD_FILE + ":" + line);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        manager.saveToFile(OUTPUT_HANZI_FILE,
                OUTPUT_NODE_FILE,
                OUTPUT_ROOT_FILE,
                OUTPUT_LIANXIANG_FILE);

        System.out.println("Character count:" + String.valueOf(characterCount));
        System.out.println("Word count:" + String.valueOf(wordCount));
        System.out.println("Dropped count:" + String.valueOf(droppedCount));
        System.out.println("Main job finished");
    }
}
