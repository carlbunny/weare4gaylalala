
package aeviou.compiler;

public class CompilerManager {
    CompilerRoot root;
    CompilerNode node;
    CompilerHanzi hanzi;
    CompilerLianxiang lianxiang;

    public CompilerManager(){
        hanzi = new CompilerHanzi();
        node = new CompilerNode(hanzi);
        root = new CompilerRoot(node);
        lianxiang = new CompilerLianxiang();
    }

    public void updateFrequency(String word, int frequency){
        hanzi.updateFrequency(word, frequency);
    }

    public void insertWord(String[] pinyin, String word, int frequency){
        int rootIndex = root.getIndex(pinyin[0]);
        node.insertWord(rootIndex, pinyin, word, frequency);
        lianxiang.insertWord(word, frequency);
    }

    public void saveToFile(String hanziFile, String nodeFile, String rootFile, String lianxiangFile){
        System.out.println("Write " + hanziFile + ", please wait...");
        hanzi.saveToFile(hanziFile);
        System.out.println("Write " + nodeFile + ", please wait...");
        node.saveToFile(nodeFile);
        System.out.println("Write " + rootFile + ", please wait...");
        root.saveToFile(rootFile);
        System.out.println("Write " + lianxiangFile + ", please wait...");
        lianxiang.saveToFile(lianxiangFile);
    }
}
