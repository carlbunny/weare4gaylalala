
package com.aeviou.pinyin;

import java.util.ArrayList;
import java.util.LinkedList;

public class PinyinContext{

    private static final int MAX_SETENCE_LENGTH = 13;
    private static final int MAX_WORD_LENGTH = 6;

    private char[] pinyinList;
    private LinkedList<Integer>[][] searchMatrix;
    private LinkedList<Integer>[][] addressMatrix;
    private int[][] freqencyMatrix;
    private int[][] firstMatrix;
    int candidatesCacheFlag;

    private int pinyinListSize;
    private ArrayList<CandidateType> candidates;
    private StringBuilder sentence;
    private int correctPosition;

    private PinyinRoot root;
    private PinyinNode node;
    private PinyinHanzi hanzi;
    private PinyinLianxiang lianxiang;

    public PinyinContext(){
        pinyinList = new char[MAX_SETENCE_LENGTH];
        pinyinListSize = 0;

        searchMatrix = new LinkedList[MAX_SETENCE_LENGTH][MAX_WORD_LENGTH];
        addressMatrix = new LinkedList[MAX_SETENCE_LENGTH][MAX_WORD_LENGTH];
        freqencyMatrix = new int [MAX_SETENCE_LENGTH][MAX_WORD_LENGTH];
        firstMatrix = new int [MAX_SETENCE_LENGTH][MAX_WORD_LENGTH];

        candidates = new ArrayList<CandidateType>();
        sentence = new StringBuilder();

        root = new PinyinRoot(PinyinFile.PINYIN_FILE_DIRECTORY + PinyinFile.PINYIN_ROOT_FILENAME);
        node = new PinyinNode(PinyinFile.PINYIN_FILE_DIRECTORY + PinyinFile.PINYIN_NODE_FILENAME);
        hanzi = new PinyinHanzi(PinyinFile.PINYIN_FILE_DIRECTORY + PinyinFile.PINYIN_HANZI_FILENAME);
        lianxiang = new PinyinLianxiang(PinyinFile.PINYIN_FILE_DIRECTORY + PinyinFile.PINYIN_LIANXIANG_FILENAME);
        
        this.clearContext();

        System.gc();
    }

    private void updateMatrixElement(int col, int row, int searchIndex){
        if (searchMatrix[col][row] == null){
            searchMatrix[col][row] = new LinkedList<Integer>();
        }
        searchMatrix[col][row].add(searchIndex);
        int freqency;
        int address = node.getHanziAddress(searchIndex);
        if (address != -1){
            if (addressMatrix[col][row] == null){
                addressMatrix[col][row] = new LinkedList<Integer>();
            }
            addressMatrix[col][row].add(address);
            freqency = node.getMaxFreqency(searchIndex);
            if (freqency > freqencyMatrix[col][row]){
                freqencyMatrix[col][row] = freqency;
                firstMatrix[col][row] = address;
            }
        }
        if (candidatesCacheFlag == col){
            candidatesCacheFlag = -1;
        }
    }

    public void addPinyin(String pinyin) {
        node.fileOpen();
        hanzi.fileOpen();

        pinyinList[pinyinListSize] = PinyinTree.getInstance().getPinyinId(pinyin);
        char[] possibleIds = PinyinTree.getInstance().getPinyinPossibleId(pinyin);

        int currentIndex;

        for (char pinyinId : possibleIds){
            currentIndex = root.getIndex(pinyinId);
            updateMatrixElement(pinyinListSize, 0, currentIndex);

            for (int i = 1; i < MAX_WORD_LENGTH; i++){
                if (pinyinListSize - i < 0) break;
                if (searchMatrix[pinyinListSize - i][i - 1] != null){
                    for (int previousIndex : searchMatrix[pinyinListSize - i][i - 1]){
                        currentIndex = node.getChild(previousIndex, pinyinId);
                        if (currentIndex != -1){
                            updateMatrixElement(pinyinListSize - i, i, currentIndex);
                        }
                    }
                }
            }

        }

        pinyinListSize++;
        updateSentence();
        updateCandidate();

        node.fileClose();
        hanzi.fileClose();
    }

    public void removePinyin() {
        if (pinyinListSize == 0) return;
        if (pinyinListSize == 1){
            clearContext();
            return;
        }

        node.fileOpen();
        hanzi.fileOpen();

        pinyinListSize--;
        for (int i = 0; i < MAX_WORD_LENGTH; i++){
            if (pinyinListSize - i < 0) break;
            searchMatrix[pinyinListSize - i][i] = null;
            addressMatrix[pinyinListSize - i][i] = null;
            freqencyMatrix[pinyinListSize - i][i] = -1;
            firstMatrix[pinyinListSize - i][i] = -1;
        }
        if (correctPosition >= pinyinListSize){
            correctPosition = pinyinListSize - 1;
                updateSentence();
        }else{
            sentence.deleteCharAt(pinyinListSize);
        }

        updateCandidate();

        node.fileClose();
        hanzi.fileClose();
    }

    public String getSentence() {
        return sentence.toString();
    }

    public void correctWord(int index) {
        hanzi.fileOpen();

        if (index <0 || index >= pinyinListSize) return;
        if (correctPosition == index) return;
        correctPosition = index;
        updateCandidate();
        String candidate = getCandidate(0);
        int size = candidate.length();
        sentence.delete(correctPosition, correctPosition + size);
        sentence.insert(correctPosition, candidate);

        hanzi.fileClose();
    }

    public String getCandidate(int index) {
        if (index < 0 || index >= candidates.size()){
            return null;
        }
        return candidates.get(index).word;
    }

    public void chooseCandidate(int index) {
        node.fileOpen();
        hanzi.fileOpen();

        String candidate = getCandidate(index);
        if (candidate == null) return;
        int size = candidate.length();
        sentence.delete(correctPosition, correctPosition + size);
        sentence.insert(correctPosition, candidate);
        correctPosition += size;
        updateCandidate();

        node.fileClose();
        hanzi.fileClose();
    }

    public void clearContext() {
    	
        sentence.delete(0, sentence.length());
        pinyinListSize = 0;
        candidates.clear();
        correctPosition = 0;
        
        for (int i = 0; i < MAX_SETENCE_LENGTH; i++){
            for (int j = 0; j < MAX_WORD_LENGTH; j++){
                searchMatrix[i][j] = null;
                addressMatrix[i][j] = null;
                freqencyMatrix[i][j] = -1;
                firstMatrix[i][j] = -1;
            }
        }

        candidatesCacheFlag = -1;
        hanzi.clearCache();
        System.gc();
    }

    private void updateSentence(){
        int l = sentence.length();
        if (correctPosition < l){
            sentence.delete(correctPosition, l);
            l = correctPosition;
        }
        while(l < pinyinListSize){
            for (int i = MAX_WORD_LENGTH - 1; i >= 0; i--){
                if (firstMatrix[l][i] != -1){
                    sentence.append(hanzi.getFirst(firstMatrix[l][i]));
                    break;
                }
            }
            l = sentence.length();
        }
    }

    private void updateCandidate(){
        if (candidatesCacheFlag == correctPosition){
            return;
        }

        candidates.clear();

        for (int i = MAX_WORD_LENGTH - 1; i >= 0; i--){
            if (addressMatrix[correctPosition][i] != null){
                for (int address : addressMatrix[correctPosition][i]){
                    hanzi.getAll(address, candidates);
                }
            }
        }

        java.util.Collections.sort(candidates);

        candidatesCacheFlag = correctPosition;
    }

    public int getCorrectPosition() {
        return correctPosition;
    }

    public int getSize() {
        return pinyinListSize;
    }

    public String getPinyin() {
        if (pinyinListSize == 0){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(PinyinTree.getInstance().getPinyinString(pinyinList[0]));
        for (int i = 1; i < pinyinListSize; i++){
            sb.append('\'');
            sb.append(PinyinTree.getInstance().getPinyinString(pinyinList[i]));
        }
        return sb.toString();
    }

    public void updateLianXiang(char lastHanzi){
        lianxiang.fileOpen();
        if(sentence!=null){
        	if(sentence.length()!=0)
        	sentence.delete(0, sentence.length()-1 ) ;
        }
        candidates.clear();
        lianxiang.getAll(lastHanzi, candidates);

        lianxiang.fileClose();
    }

    /*
    public static void main(String[] args) {
        PinyinUtils.PINYIN_LIST_FILE = "E:\\pinyin_list.txt";
        PinyinContext context = new PinyinContext();
        
        context.addPinyin("ce");
        context.addPinyin("shi");
        context.addPinyin("ping");
        context.addPinyin("ying");
        context.addPinyin("shu");
        context.addPinyin("ru");
        context.addPinyin("fa");

        context.chooseCandidate(0);
        System.out.println(context.getSentence());

        context.removePinyin();
        System.out.println(context.getSentence());

        context.addPinyin("fa");
        System.out.println(context.getSentence());

        context.correctWord(1);
        context.chooseCandidate(1);
        System.out.println(context.getSentence());

        context.removePinyin();
        System.out.println(context.getSentence());

        context.addPinyin("fa");
        System.out.println(context.getSentence());

        context.clearContext();
        System.out.println(context.getSentence());

        context.addPinyin("ce");
        context.removePinyin();

        context.addPinyin("ce");
        context.removePinyin();

        
        context.addPinyin("li");
        context.addPinyin("shi");
        context.addPinyin("shei");
        System.out.println(context.getSentence());

        context.clearContext();
        
        context.updateLianXiang();
        for (int i = 0; i < 100; i++){
            System.out.println(context.getCandidate(i));
        }
    }
    */
}
