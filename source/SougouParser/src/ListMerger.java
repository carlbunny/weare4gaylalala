import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class ListMerger {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMap<String, ArrayList<PinyinObject>> map = new HashMap<String, ArrayList<PinyinObject>>();
		loadAeviou(map);
		loadSougou(map);
		writeAll(map);
	}

	static void writeAll(HashMap<String, ArrayList<PinyinObject>> map) {
		ArrayList<ArrayList<PinyinObject>> pinyinMerge = new ArrayList<ArrayList<PinyinObject>>(
				map.values());
		System.out.println("merge and get: " + pinyinMerge.size());
		Collections.sort(pinyinMerge,new Comparator(){

			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				PinyinObject p1=((ArrayList<PinyinObject>)o1).get(0),p2=((ArrayList<PinyinObject>)o2).get(0);
				return p1.compareTo(p2);
			}
			
		});
		String path = "res\\merge.txt";
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileWriter(path));
			for (ArrayList<PinyinObject> pObjArr : pinyinMerge) {
				for(PinyinObject pObj:pObjArr){
					//we don`t like zero
					pObj.frequency+=1;
					pw.println(pObj);
				}
			}
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// call the first;
	// word,pingying,frequence
	static void loadAeviou(HashMap<String, ArrayList<PinyinObject>> map) {
		try {
			Scanner scanner = new Scanner(new FileReader("res\\word_list.txt"));
			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split(" ");
				if (line.length != 3) {
					break;
				}
				PinyinObject pObj = new PinyinObject(line[0], line[1],
						Integer.valueOf(line[2]));
				ArrayList<PinyinObject> array = map.get(pObj.word);
				if (array == null) {
					array = new ArrayList<PinyinObject>();
					map.put(pObj.word, array);
				}
				array.add(pObj);
			}
			System.out.println("load worl list:" + map.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// call after Aeviou
	//
	static void loadSougou(HashMap<String, ArrayList<PinyinObject>> map) {
		try {
			Scanner scanner = new Scanner(
					new FileReader("res\\sougou_list.txt"));
			int countUpdate = 0, countAdd = 0;
			while (scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split(" ");
				PinyinObject pObj = null;
				if (map.containsKey(line[0])) {
					ArrayList<PinyinObject> array = map.get(line[0]);
					for (PinyinObject arrItem : array) {
						if (arrItem.word.equals(line[0])
								&& arrItem.pinyin.equals(line[1])) {
							pObj = arrItem;
							break;
						}
					}
					if (pObj != null) {
						pObj.frequency += 1;
						countUpdate++;
					} else {
						pObj = new PinyinObject(line[0], line[1], 1);
						array.add(pObj);
						countAdd++;
					}
				}else{
					ArrayList<PinyinObject> array=new ArrayList<PinyinObject>();
					array.add(new PinyinObject(line[0], line[1], 1));
					map.put(line[0], array);
					countAdd++;
				}
			}
			System.out.println("after merge:" + map.size() + "count: "
					+ countUpdate + " | " + countAdd);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
