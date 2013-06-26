
package com.aeviou.pinyin;

public class CandidateType implements Comparable{
    public String word;
    public int length;
    public int frequency;

    public int compareTo(Object o) {
        CandidateType node1 = (CandidateType)this;
        CandidateType node2 = (CandidateType)o;

        if (node1.length < node2.length){
            return 1;
        }else if(node1.length > node2.length){
            return -1;
        }else{
            if (node1.frequency < node2.frequency){
                return 1;
            }else if(node1.frequency > node2.frequency){
                return -1;
            }else{
                return 0;
            }
        }
    }
}
