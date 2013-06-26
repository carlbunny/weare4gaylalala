
package aeviou.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * File format:
 * ================repeat===============
 *
 * char     pinyin           2
 * int      wordAddress      4
 * int      maxFreqency      4
 * char     childrenSize     2
 * int      childrenAddress  4
 *
 * ==============end repeat==============
 *
 */
public class CompilerNode {

    private class TreeNode{
        public ArrayList<TreeNode> children;
        public char pinyin;
        public int listIndex;
        public int filePosition;
    }

    private class NodeComparator implements Comparator{
        public int compare(Object o1, Object o2) {
            TreeNode node1 = (TreeNode)o1;
            TreeNode node2 = (TreeNode)o2;

            if (node1.pinyin > node2.pinyin){
                return 1;
            }else if(node1.pinyin == node2.pinyin){
                return 0;
            }else{
                return -1;
            }
        }
    }

    private NodeComparator nodeComparator = new NodeComparator();
    private ArrayList<TreeNode> roots;
    private CompilerHanzi hanzi;

    public CompilerNode(CompilerHanzi hanzi){
        this.hanzi = hanzi;
        roots = new ArrayList<TreeNode>();
    }

    int getRootPosition(int index){
        return roots.get(index).filePosition;
    }

    int createNewRoot(String pinyin) {
        TreeNode node = new TreeNode();
        node.children = null;
        node.pinyin = CompilerUtils.pinyinToChar(pinyin);
        node.listIndex = -1;
        roots.add(node);
        return roots.size() - 1;
    }

    int insertWord(int rootIndex, String[] pinyin, String word, int freqency) {
        TreeNode node = roots.get(rootIndex);

        for (int i = 1; i < pinyin.length; i++){
            char current = CompilerUtils.pinyinToChar(pinyin[i]);
            if (current == (char)-1){
                System.out.println("Error: pinyin not found:" + pinyin[i]
                         + ", in word:" + word);
            }
            if (node.children == null){
                node.children = new ArrayList<TreeNode>();
            }
            boolean find = false;

            for (int j = 0; j < node.children.size(); j++){
                if (current == node.children.get(j).pinyin){
                    find = true;
                    node = node.children.get(j);
                    break;
                }
            }
            if (!find){
                node.children.add(new TreeNode());
                node = node.children.get(node.children.size() - 1);
                node.children = null;
                node.pinyin = current;
                node.listIndex = -1;
            }
        }

        if (node.listIndex == -1){
            node.listIndex = hanzi.createNewList();
        }

        hanzi.insertWord(node.listIndex, word, freqency);
        return 0;
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

        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        
        for (TreeNode node : roots){
            queue.addLast(node);
        }

        while (queue.size() != 0){
            TreeNode node = queue.removeFirst();
            nodes.add(node);
            if (node.children != null){
                java.util.Collections.sort(node.children, nodeComparator);
                for (int i = node.children.size() - 1; i >= 0; i--){
                    queue.addLast(node.children.get(i));
                }
            }
        }

        for (int i = nodes.size() - 1; i >= 0; i--){
            try{
                saveNode(nodes.get(i), file);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        try {
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void saveNode(TreeNode node, RandomAccessFile file) throws IOException{
        node.filePosition = (int)file.getFilePointer();

        CompilerUtils.writeChar(node.pinyin, file);

        int address = -1;
        int freqency = -1;
        if (node.listIndex != -1){
            address = hanzi.getListFilePosition(node.listIndex);
            freqency = hanzi.getMaxFreqency(node.listIndex);
        }

        CompilerUtils.writeInteger(address, file);
        CompilerUtils.writeInteger(freqency, file);

        if (node.children != null){
            CompilerUtils.writeChar((char)node.children.size(), file);
        }else{
            CompilerUtils.writeChar((char)-1, file);
        }

        address = -1;
        if (node.children != null && node.children.size() > 0){
            address = node.children.get(0).filePosition;
        }
        CompilerUtils.writeInteger(address, file);
    }
}
