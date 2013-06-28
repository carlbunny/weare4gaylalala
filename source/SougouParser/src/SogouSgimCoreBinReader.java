/*  Copyright (c) 2010 Xiaoyun Zhu
 * 
 *  Permission is hereby granted, free of charge, to any person obtaining a copy  
 *  of this software and associated documentation files (the "Software"), to deal  
 *  in the Software without restriction, including without limitation the rights  
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell  
 *  copies of the Software, and to permit persons to whom the Software is  
 *  furnished to do so, subject to the following conditions:
 *  
 *  The above copyright notice and this permission notice shall be included in  
 *  all copies or substantial portions of the Software.
 *  
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR  
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,  
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE  
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER  
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,  
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN  
 *  THE SOFTWARE.  
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Sogou sgim_core.bin Reader
 * 
 * 
 * <pre>
 * 地址：
 * 0x0C：单词数量
 * ????：单词长度（byte），单词（编码：UTF-16LE）
 * 
 * For files like sgim_eng.bin etc., the implementation has to be slightly modified.
 * </pre>
 * 
 * @author keke
 */
public class SogouSgimCoreBinReader {
	static String polyphonePrefix="";
	
	public static void main(final String[] args) throws IOException {
		final String binFile = "res\\sgim_core.bin";
		final int[] searchKey = { 0x02, 0x00, 0x4A, 0x55 }; // 6.2.0.7817
		//the searchkey is the length of bytes 2 and the character"啊“
		
		// load correct pinyin from aeviou, if not exist, use the pinyin4j
		HashMap<String, PinyinObject> worldlistFromAeviou = new HashMap<String, PinyinObject>();
		HashMap<String, String> pyFromAeviou = new HashMap<String, String>();
		buildHZTable(worldlistFromAeviou);
		buildPYTable(pyFromAeviou);

		// read scel into byte array
		final ByteBuffer bb;
		RandomAccessFile file = new RandomAccessFile(binFile, "r");
		final FileChannel fChannel = file.getChannel();
		bb = ByteBuffer.allocate((int) fChannel.size());
		fChannel.read(bb);

		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.rewind();

		int words = bb.getInt(0xC);
		System.out.println("读入文件: " + binFile + "，单词：" + words);

		int idx = 0;
		int i;
		int startPos = -1;
		while (bb.hasRemaining()) {
			i = 0xff & bb.get();
			if (i == searchKey[idx]) {
				idx++;
				if (idx == searchKey.length) {
					startPos = bb.position() - searchKey.length;
					break;
				}
			} else {
				idx = 0;
			}
		}
		 System.out.println("单词起始位置：0x" +		 Integer.toHexString(startPos));
		 
		 ArrayList<PinyinObject> polyphoneArr=new ArrayList<PinyinObject>();
		 char startChar='啊';
		 
		try {

			PrintWriter pw;
			pw = new PrintWriter(new FileWriter("res\\sougou_list.txt"));
			if (startPos != -1) {
				short s;
				int counter = 0;
				final ByteBuffer buffer = ByteBuffer.allocate(Short.MAX_VALUE);

				bb.position(startPos);
				
				
				while (bb.hasRemaining() && (words-- > 0)) {
					s = bb.getShort();
					bb.get(buffer.array(), 0, s);
					String word = new String(buffer.array(), 0, s, "UTF-16LE");
					String pinyin = converterToSpell(word, worldlistFromAeviou,
							pyFromAeviou);

					if (pinyin != null && !word.contains("^?")) {
						counter++;
						if(word.charAt(0)==startChar){
							polyphoneArr.add(new PinyinObject(word,pinyin,1));
						}else{
							wirghtArrBack(polyphoneArr,pw);
							startChar=word.charAt(0);
							polyphonePrefix="";
							polyphoneArr.add(new PinyinObject(word,pinyin,1));
						}
					}else{
//						System.out.println("Error: word: "+word+" "+pinyin);
					}
				}
				wirghtArrBack(polyphoneArr,pw);
				System.out.println("add word: "+counter);
			} else {
				System.err.println("文件版本已更新！");
			}
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done");
	}

	public static void wirghtArrBack(ArrayList<PinyinObject> polyphoneArr,PrintWriter pw){
		if(polyphonePrefix.equals("")==true){
			for(PinyinObject pyOBJ:polyphoneArr){
				pw.println(pyOBJ.word + " " + pyOBJ.pinyin);
			}
		}else{
			for(PinyinObject pyOBJ:polyphoneArr){
				String newPy=pyOBJ.pinyin.replaceFirst(".*?'",polyphonePrefix+"'");
				pw.println(pyOBJ.word + " " + newPy);
			}
		}
		polyphoneArr.clear();
	}
	
	public static void buildHZTable(HashMap<String, PinyinObject> map) {
		try {
			Scanner scanner = new Scanner(new FileReader("res\\word_list.txt"));
			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split(" ");
				if (line.length != 3) {
					break;
				}
				//find the highest freq to the default multiphone
				int newFreq=Integer.valueOf(line[2]);
				if(map.containsKey(line[0])){
					PinyinObject pObj=map.get(line[0]);
					
					if(newFreq>pObj.frequency){
						pObj.pinyin=line[1];
						pObj.frequency=newFreq;
					}
				}else{
					map.put(line[0], new PinyinObject(line[0],line[1],newFreq));
				}
			}
			System.out.println("load worl list:" + map.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void buildPYTable(HashMap<String, String> map) {
		try {
			Scanner scanner = new Scanner(
					new FileReader("res\\pinyin_list.txt"));
			while (scanner.hasNextLine()) {
				String py = scanner.nextLine();
				if (py.startsWith("'")) {
					py = py.substring(1, py.length());
				}
				map.put(py, py);

			}
			System.out.println("load worl list:" + map.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String converterToSpell(String chines,
			HashMap<String, PinyinObject> worldlistFromAeviou,
			HashMap<String, String> pyFromAeviou) {
		//skip single hz
		if(chines.length()==1)return null;
		
		// live polyphone to be phrased in ListMerger
		
		if (worldlistFromAeviou.containsKey(chines)) {
			String resultPY=worldlistFromAeviou.get(chines).pinyin;
			polyphonePrefix=resultPY.split("'")[0];
			return resultPY;
		}

		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {

				if (i > 0)
					pinyinName += "'";
				if (worldlistFromAeviou
						.containsKey(String.valueOf(nameChar[i]))) {
					pinyinName += worldlistFromAeviou.get(String.valueOf(nameChar[i])).pinyin;
				} else {
					try {
						String singlePY = PinyinHelper
								.toHanyuPinyinStringArray(nameChar[i],
										defaultFormat)[0];
						if (singlePY != null
								&& pyFromAeviou.containsKey(singlePY)) {
							pinyinName += singlePY;
						}
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					} catch (NullPointerException e){
						return null;
					}
				}

			} else {
				return null;
			}
		}
		return pinyinName;
	}
}
