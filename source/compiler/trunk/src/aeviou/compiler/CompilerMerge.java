
package aeviou.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * The function of this program is to merge
 * two files. One contains word and pinyin.
 * The other contains word and freqency.
 */

public class CompilerMerge {
    public static final String FILE_WORD_AND_PINYIN = "E:\\word_pinyin.txt";
    public static final String FILE_WORD_AND_FREQENCY = "E:\\word_freqency.txt";
    
    public static final String FILE_OUTPUT_FILE = "E:\\word_list.txt";

    public static HashMap<String, Integer> freqencyMap;

    /*
     * Please run this program very carefully, it may destory FILE_OUTPUT_FILE
     */
    public static void main2(String[] args) {
        freqencyMap = new HashMap<String, Integer>();
        int characterCount = 0;
        int wordCount = 0;
        try{
            File outFile = new File(FILE_OUTPUT_FILE);
            if (outFile.exists()){
                outFile.delete();
            }

            Scanner pinyinFile = new Scanner
                    (new File(FILE_WORD_AND_PINYIN));
            Scanner freqencyFile = new Scanner
                    (new File(FILE_WORD_AND_FREQENCY));
            FileOutputStream outputFile = new FileOutputStream(FILE_OUTPUT_FILE);

            System.out.println("Read " + FILE_WORD_AND_FREQENCY + ", please wait...");
            while (freqencyFile.hasNext()){
                String line = freqencyFile.nextLine();
                String[] splitedLine = line.split("\t");
                if (splitedLine.length == 2){
                    freqencyMap.put(splitedLine[0], Integer.parseInt(splitedLine[1]));
                }else{
                    System.out.println("Warning: Cannot handle line in: "
                                        + FILE_WORD_AND_FREQENCY + ":" + line);
                }
            }


            System.out.println("Read " + FILE_WORD_AND_PINYIN + ", please wait...");
            while (pinyinFile.hasNext()){
                String line = pinyinFile.nextLine();
                String[] splitedLine = line.split(" ");
                if (splitedLine.length >= 3){
                    String word = splitedLine[0];
                    int j = 2;
                    for (j = 2; j < splitedLine.length; j++){
                        String pinyin = splitedLine[j];
                        if (pinyin.contains(":")){
                            pinyin = pinyin.split(":")[0];
                        }
                        String pinyinSplited[] = pinyin.split("\'");
                        if (pinyinSplited.length == word.length()){
                            if (pinyinSplited.length == 1){
                                characterCount++;
                            }else{
                                wordCount++;
                            }
                            Integer iFreqency = freqencyMap.get(word);
                            int frequency = 0;
                            if (iFreqency != null){
                                frequency = iFreqency.intValue();
                            }
                            outputFile.write(
                                    (word + " " + pinyin + " "
                                    + String.valueOf(frequency) + "\r\n")
                                    .getBytes());
                        }else{
                            System.out.println("Warning: pinyinLen != wordLen in: "
                                                + FILE_WORD_AND_PINYIN + ":" + line);
                        }
                    }
                }else{
                    System.out.println("Warning: Cannot handle line in: "
                                        + FILE_WORD_AND_PINYIN + ":" + line);
                }
            }

            System.out.println("Character count:" + String.valueOf(characterCount));
            System.out.println("Word count:" + String.valueOf(wordCount));
            System.out.println("Merge job finished");
            pinyinFile.close();
            freqencyFile.close();
            outputFile.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
