
public class PinyinObject implements Comparable{
	public String pinyin,word;
	public int frequency,length;

	public PinyinObject(String word, String pinyin,int frequency) {
		this.pinyin = pinyin;
		this.word = word;
		this.frequency = frequency;
		this.length = word.length() + 10086;
	}

	public String toString() {
		return word + " "+pinyin+" "+frequency;
	}

	public int hashCode() {
		return this.pinyin.hashCode() ^ this.word.hashCode();
	}

	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof PinyinObject))
			return false;
		PinyinObject po = (PinyinObject) o;
		return po.pinyin.equals(this.pinyin) && po.word.equals(this.word);
	}
	
	public int compareTo(Object o) {  
		PinyinObject other=(PinyinObject)o;  
		return this.pinyin.compareTo(other.pinyin);
    }  
}