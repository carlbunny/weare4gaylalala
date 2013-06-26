package com.aeviou.pinyin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.widget.Toast;

import com.aeviou.R;
import com.aeviou.utils.ALog;
import com.aeviou.utils.AeviouConstants;

public class CustomPinyinJSON extends CustomPinyin {

	private static CustomPinyin instance;

	private String dir = Environment.getExternalStorageDirectory().getPath()
			+ File.separator;
	private String file = "aeviou" + File.separator + "customPinyin.dat";

	private String fileContent;
	private JSONObject json;
	private JSONArray array;

	private StringBuilder pinyin;

	private static int recordCounter = 0;
	private static int recordInterval = 3;

	private static final int DEFAULT_FREQUENCY = 99;

	private HashMap<String, HashMap<String, PinyinObject>> pinyinHash;
	private int lastPinyinLength;
	private boolean hasSD;

	public static CustomPinyin getInstance() {
		if (CustomPinyinJSON.instance == null) {
			CustomPinyinJSON.instance = new CustomPinyinJSON();
		}
		return CustomPinyinJSON.instance;
	}

	public boolean isSdCardExist() {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// �ж��Ƿ��Ѿ�����
			return true;
		}
		return false;
	}

	private CustomPinyinJSON() {
		if (hasSD = isSdCardExist()) {
			initFile();
		} else {
			Toast.makeText(AeviouConstants.inputMethodService,
					R.string.dict_needSD, Toast.LENGTH_SHORT).show();
			this.fileContent = "{\"customPinyin\":[[gaoji,搞基,100]]}";
		}

		try {
			this.json = new JSONObject(fileContent);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.pinyinHash = new HashMap<String, HashMap<String, PinyinObject>>();
		this.pinyin = new StringBuilder();
		initPinyinMap();
	}

	private void initFile() {
		// TODO Auto-generated method stub

		File CustomPinyinFile = new File(this.dir + this.file);
		if (!CustomPinyinFile.exists()) {
			this.writeFileSdcardFile(this.dir + this.file,
					"{\"customPinyin\":[[gaoji,搞基,100]]}");
		}
		this.fileContent = readFileSdcardFile(dir + file);

	}

	private void initPinyinMap() {
		// TODO Auto-generated method stub
		try {
			this.array = this.json.getJSONArray("customPinyin");
			int length = this.array.length();
			// insert json to the hashmap
			for (int i = 0; i < length; i++) {

				JSONArray entry = this.array.getJSONArray(i);

				String pinyin = entry.getString(0);
				String hanzi = entry.getString(1);
				int frequency = entry.getInt(2);

				PinyinObject pinyinObject = new PinyinObject(pinyin, hanzi,
						frequency);

				HashMap<String, PinyinObject> miniMap = this.pinyinHash
						.get(pinyin);
				if (miniMap == null) {
					miniMap = new HashMap<String, PinyinObject>();
					this.pinyinHash.put(pinyin, miniMap);
					miniMap.put(hanzi, pinyinObject);
				} else {
					PinyinObject oldObj = miniMap.get(hanzi);
					if (oldObj == null) {
						miniMap.put(hanzi, pinyinObject);
					} else {
						if (oldObj.frequency < pinyinObject.frequency) {
							miniMap.put(hanzi, pinyinObject);
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// after find sd card
	private void updatePinyinMap() {
		initFile();
		try {
			this.array = this.json.getJSONArray("customPinyin");
			int length = this.array.length();
			// insert json to the hashmap
			for (int i = 0; i < length; i++) {

				JSONArray entry = this.array.getJSONArray(i);

				String pinyin = entry.getString(0);
				String hanzi = entry.getString(1);
				int frequency = entry.getInt(2);

				PinyinObject pinyinObject = new PinyinObject(pinyin, hanzi,
						frequency);

				HashMap<String, PinyinObject> miniMap = this.pinyinHash
						.get(pinyin);
				if (miniMap == null) {
					miniMap = new HashMap<String, PinyinObject>();
					this.pinyinHash.put(pinyin, miniMap);
					miniMap.put(hanzi, pinyinObject);
				} else {
					PinyinObject oldObj = miniMap.get(hanzi);
					if (oldObj == null) {
						miniMap.put(hanzi, pinyinObject);
					} else {
						if (oldObj.frequency < pinyinObject.frequency) {
							miniMap.put(hanzi, pinyinObject);
						}
					}
				}
			}
			JSONArray organizedArray = new JSONArray();

			for (String pinyin : this.pinyinHash.keySet()) {
				HashMap<String, PinyinObject> miniMap = this.pinyinHash
						.get(pinyin);
				for (String hanzi : miniMap.keySet()) {
					PinyinObject pinyinObject = miniMap.get(hanzi);
					JSONArray entry = new JSONArray();
					entry.put(pinyin);
					entry.put(hanzi);
					entry.put(pinyinObject.frequency);
					organizedArray.put(entry);
				}
			}

			this.array = organizedArray;
			this.json.put("customPinyin", this.array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeFileSdcardFile(String fileName, String write_str) {

		try {
			File file = new File(fileName);
			File dir = file.getParentFile();

			if (!dir.exists()) {
				dir.mkdir();
			}

			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = write_str.getBytes();

			fout.write(bytes);
			fout.close();
		}

		catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String readFileSdcardFile(String fileName) {
		String res = null;
		try {
			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte[] buffer = new byte[length];
			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");

			buffer = null;
			fin.close();
		}

		catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aeviou.pinyin.CustomPinyin#clearPinyin()
	 */
	@Override
	public void clearPinyin() {
		this.pinyin.delete(0, this.pinyin.length());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aeviou.pinyin.CustomPinyin#addPinyin(java.lang.String)
	 */
	@Override
	public void addPinyin(String pinyin) {
		ALog.v("addpinyin:" + pinyin);

		this.pinyin.append(pinyin);
		ALog.v("this.pinyin:" + this.pinyin);
		this.lastPinyinLength = pinyin.length();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aeviou.pinyin.CustomPinyin#removePinyin()
	 */
	@Override
	public void removePinyin() {
		this.pinyin.delete(this.pinyin.length() - this.lastPinyinLength,
				this.pinyin.length());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aeviou.pinyin.CustomPinyin#getPinyin()
	 */
	@Override
	public Collection<PinyinObject> getPinyin() {
		HashMap<String, PinyinObject> miniMap = this.pinyinHash.get(this.pinyin
				.toString());
		if (miniMap == null) {
			return null;
		} else {
			return miniMap.values();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aeviou.pinyin.CustomPinyin#debug()
	 */
	@Override
	public void debug() {
		try {
			ALog.v(this.json.getJSONArray("customPinyin"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String pinyin : this.pinyinHash.keySet()) {
			ALog.v(pinyin + this.pinyinHash.get(pinyin));
		}
		ALog.v("this.pinyin:" + this.pinyin.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aeviou.pinyin.CustomPinyin#testCustomExist(java.lang.String)
	 */
	@Override
	public boolean testCustomExist(String word) {
		HashMap<String, PinyinObject> wordSet = this.pinyinHash.get(this.pinyin
				.toString());
		if (wordSet == null) {
			return false;
		} else {
			return wordSet.containsKey(word);
		}
	}

	@Override
	public void recordPinyin(String word) {
		// TODO Auto-generated method stub
		HashMap<String, PinyinObject> miniMap = this.pinyinHash.get(this.pinyin
				.toString());
		if (miniMap == null) {
			miniMap = new HashMap<String, PinyinObject>();
			this.pinyinHash.put(this.pinyin.toString(), miniMap);
		}

		PinyinObject po = miniMap.get(word);
		boolean isNew = false;
		if (po == null) {
			isNew = true;
			po = new PinyinObject(pinyin.toString(), word,
					CustomPinyinJSON.DEFAULT_FREQUENCY);
		}

		po.frequency++;
		if (po.frequency < 0) {
			po.frequency = Integer.MAX_VALUE / 2;
		}

		miniMap.put(word, po);

		if (isNew) {
			JSONArray newEntry = new JSONArray();
			ALog.v(po);
			newEntry.put(this.pinyin.toString());
			newEntry.put(word);
			newEntry.put(po.frequency);
			this.array.put(newEntry);
		}
		if (CustomPinyinJSON.recordCounter++ % CustomPinyinJSON.recordInterval == 0) {
			record2SD();
		}
	}

	public void record2SD() {

		boolean hasSDNow = isSdCardExist();
		if (hasSDNow) {
			if (hasSD) {
				writeFileSdcardFile(this.dir + this.file, this.json.toString());
			} else {
				initFile();
				try {
					this.json = new JSONObject(fileContent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updatePinyinMap();
				writeFileSdcardFile(this.dir + this.file, this.json.toString());
			}
		}
		// no sd card, do nothing
		hasSD = hasSDNow;
	}

	public void onDestory() {
		record2SD();
	}
}

class PinyinObject extends CandidateType {
	public String pinyin;

	public PinyinObject(String pinyin, String word, int frequency) {
		this.pinyin = pinyin;
		this.word = word;
		this.frequency = frequency;
		this.length = word.length() + 10086;
	}

	public String toString() {
		return pinyin + word + frequency;
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
}
