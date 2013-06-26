package com.aeviou.pinyin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * File format: ================repeat===============
 * 
 * char pinyin 2 int wordAddress 4 int maxFreqency 4 char childrenSize 2 int
 * childrenAddress 4
 * 
 * ==============end repeat==============
 * 
 */
public class PinyinNode {
	public static final int NODE_SIZE = 16;

	String filename;
	RandomAccessFile file = null;
	byte[] readBuffer = new byte[NODE_SIZE];
	int lastCurrent;
	int lastAddress;
	int lastFreqency;

	public void fileOpen() {
		try {
			file = new RandomAccessFile(new File(filename), "r");
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	public void fileClose() {
		if (file == null)
			return;
		try {
			file.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		file = null;
	}

	PinyinNode(String filename) {
		this.filename = filename;
		lastCurrent = -1;
		lastAddress = -1;
	}

	int getChild(int current, char pinyin) {
		try {
			file.seek(current);
			file.read(readBuffer);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		char childrenSize = PinyinUtils.getChar(readBuffer, 10);
		int childrenAddress = PinyinUtils.getInteger(readBuffer, 12);

		if (childrenAddress == -1)
			return -1;

		int i = 0;
		int j = childrenSize - 1;
		int m = 0;
		int address;
		char value;

		byte[] childrenBuffer = new byte[NODE_SIZE * childrenSize];
		try {
			file.seek(childrenAddress);
			file.read(childrenBuffer);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		while (i <= j) {
			m = (i + j) / 2;
			address = m * NODE_SIZE;
			value = PinyinUtils.getChar(childrenBuffer, address);
			if (pinyin == value) {
				lastCurrent = childrenAddress + address;
				lastAddress = PinyinUtils.getInteger(childrenBuffer,
						address + 2);
				lastFreqency = PinyinUtils.getInteger(childrenBuffer,
						address + 6);
				return childrenAddress + address;
			} else if (pinyin > value) {
				i = m + 1;
			} else {
				j = m - 1;
			}
		}

		return -1;
	}

	int getHanziAddress(int current) {
		if (lastCurrent == current) {
			return lastAddress;
		}
		try {
			file.seek(current);
			file.read(readBuffer);
			lastCurrent = current;
			lastAddress = PinyinUtils.getInteger(readBuffer, 2);
			lastFreqency = PinyinUtils.getInteger(readBuffer, 6);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lastAddress;
	}

	int getMaxFreqency(int current, char pinyinId) {
		if (lastCurrent == current) {
			return lastFreqency;
		}
		try {
			file.seek(current);
			file.read(readBuffer);
			lastCurrent = current;
			lastAddress = PinyinUtils.getInteger(readBuffer, 2);
			lastFreqency = PinyinUtils.getInteger(readBuffer, 6);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lastFreqency;
	}
}
