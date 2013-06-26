package com.aeviou.pinyin;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

import android.content.res.AssetManager;

import com.aeviou.R;
import com.aeviou.utils.AeviouConstants;

public class PinyinContext {

	public static final int MAX_SETENCE_LENGTH = 10;
	public static final int MAX_WORD_LENGTH = 4;

	private char[] pinyinList;
	private LinkedList<Integer>[][] searchMatrix;
	private LinkedList<Integer>[][] addressMatrix;
	private int[][] freqencyMatrix;
	private int[][] tFreqencyMatrix;
	private int[][] firstMatrix;
	int candidatesCacheFlag;

	private int pinyinListSize;
	private ArrayList<CandidateType> candidates;
	private HashMap<Character, String> lastChar;
	private StringBuilder sentence;
	private int correctPosition;

	private PinyinRoot root;
	private PinyinNode node;
	private PinyinHanzi hanzi;
	private PinyinLianxiang lianxiang;

	private char[] combiningChars;

	public PinyinContext() {
		pinyinList = new char[MAX_SETENCE_LENGTH];
		combiningChars = new char[MAX_SETENCE_LENGTH];
		pinyinListSize = 0;

		searchMatrix = new LinkedList[MAX_SETENCE_LENGTH][MAX_WORD_LENGTH];
		addressMatrix = new LinkedList[MAX_SETENCE_LENGTH][MAX_WORD_LENGTH];
		freqencyMatrix = new int[MAX_SETENCE_LENGTH][MAX_WORD_LENGTH];
		tFreqencyMatrix = new int[MAX_SETENCE_LENGTH][MAX_WORD_LENGTH];
		firstMatrix = new int[MAX_SETENCE_LENGTH][MAX_WORD_LENGTH];

		candidates = new ArrayList<CandidateType>();
		sentence = new StringBuilder();

		root = new PinyinRoot(PinyinFile.PINYIN_FILE_DIRECTORY
				+ PinyinFile.PINYIN_ROOT_FILENAME);
		node = new PinyinNode(PinyinFile.PINYIN_FILE_DIRECTORY
				+ PinyinFile.PINYIN_NODE_FILENAME);
		hanzi = new PinyinHanzi(PinyinFile.PINYIN_FILE_DIRECTORY
				+ PinyinFile.PINYIN_HANZI_FILENAME);
		lianxiang = new PinyinLianxiang(PinyinFile.PINYIN_FILE_DIRECTORY
				+ PinyinFile.PINYIN_LIANXIANG_FILENAME);

		Scanner lastCharScanner = new Scanner(new InputStreamReader(
				AeviouConstants.inputMethodService.getResources()
						.openRawResource(R.raw.last_char)));
		String[] line;
		lastChar = new HashMap<Character, String>();
		while (lastCharScanner.hasNext()) {
			line = lastCharScanner.nextLine().split(" ");
			lastChar.put(PinyinTree.getInstance().getPinyinId(line[0]), line[1]);
		}

		this.clearContext();

		System.gc();
	}

	private void updateMatrixElement(int col, int row, int searchIndex,
			char pinyinId) {
		if (searchMatrix[col][row] == null) {
			searchMatrix[col][row] = new LinkedList<Integer>();
		}
		searchMatrix[col][row].add(searchIndex);
		int freqency;
		int address = node.getHanziAddress(searchIndex);
		if (address != -1) {
			if (addressMatrix[col][row] == null) {
				addressMatrix[col][row] = new LinkedList<Integer>();
			}
			addressMatrix[col][row].add(address);
			freqency = node.getMaxFreqency(searchIndex, pinyinId);
			if (freqency > freqencyMatrix[col][row]) {
				freqencyMatrix[col][row] = freqency;
				firstMatrix[col][row] = address;
			}
		}
		if (candidatesCacheFlag == col) {
			candidatesCacheFlag = -1;
		}
	}

	public void addPinyin(String pinyin) {
		if (this.getSize() >= MAX_SETENCE_LENGTH)
			return;

		node.fileOpen();
		hanzi.fileOpen();

		pinyinList[pinyinListSize] = PinyinTree.getInstance().getPinyinId(
				pinyin);
		char[] possibleIds = PinyinTree.getInstance().getPinyinPossibleId(
				pinyin);

		int currentIndex;

		CustomPinyin.getInstance().addPinyin(pinyin);

		for (char pinyinId : possibleIds) {
			currentIndex = root.getIndex(pinyinId);
			updateMatrixElement(pinyinListSize, 0, currentIndex, pinyinId);

			for (int i = 1; i < MAX_WORD_LENGTH; i++) {
				if (pinyinListSize - i < 0)
					break;
				if (searchMatrix[pinyinListSize - i][i - 1] != null) {
					for (int previousIndex : searchMatrix[pinyinListSize - i][i - 1]) {
						currentIndex = node.getChild(previousIndex, pinyinId);
						if (currentIndex != -1) {
							updateMatrixElement(pinyinListSize - i, i,
									currentIndex, pinyinId);
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
		if (pinyinListSize == 0)
			return;
		if (pinyinListSize == 1) {
			clearContext();
			return;
		}

		node.fileOpen();
		hanzi.fileOpen();

		CustomPinyin.getInstance().removePinyin();

		pinyinListSize--;
		for (int i = 0; i < MAX_WORD_LENGTH; i++) {
			if (pinyinListSize - i < 0)
				break;
			searchMatrix[pinyinListSize - i][i] = null;
			addressMatrix[pinyinListSize - i][i] = null;
			freqencyMatrix[pinyinListSize - i][i] = -1;
			firstMatrix[pinyinListSize - i][i] = -1;
		}
		if (correctPosition >= pinyinListSize) {
			correctPosition = pinyinListSize - 1;
			updateSentence();
		} else {
			sentence.deleteCharAt(pinyinListSize);
		}
		candidatesCacheFlag = -1;
		updateCandidate();

		node.fileClose();
		hanzi.fileClose();
	}

	public String getSentence() {
		return sentence.toString();
	}

	public String getRemainPinyin() {
		if (pinyinListSize == 0 || correctPosition >= pinyinListSize) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(PinyinTree.getInstance().getPinyinString(
				pinyinList[correctPosition]));
		for (int i = correctPosition + 1; i < pinyinListSize; i++) {
			sb.append(' ');
			sb.append(PinyinTree.getInstance().getPinyinString(pinyinList[i]));
		}
		return sb.toString();
	}

	public String getChoosedSentence() {
		if (correctPosition == 0) {
			return "";
		}
		return sentence.substring(0, correctPosition);
	}

	public String getRemainSentence() {
		return sentence.substring(correctPosition);
	}

	public void correctWord(int index) {
		hanzi.fileOpen();

		if (index < 0 || index >= pinyinListSize)
			return;
		if (correctPosition == index)
			return;
		correctPosition = index;
		updateCandidate();
		String candidate = getCandidate(0);
		int size = candidate.length();
		sentence.delete(correctPosition, correctPosition + size);
		sentence.insert(correctPosition, candidate);

		hanzi.fileClose();
	}

	public String getCandidate(int index) {
		if (index < 0 || index >= candidates.size()) {
			return null;
		}
		return candidates.get(index).word;
	}

	public void chooseCandidate(int index) {
		node.fileOpen();
		hanzi.fileOpen();

		String candidate = getCandidate(index);
		if (candidate == null)
			return;
		int size = candidate.length();
		sentence.delete(correctPosition, correctPosition + size);
		sentence.insert(correctPosition, candidate);
		correctPosition += size;
		updateSentence();
		updateCandidate();

		node.fileClose();
		hanzi.fileClose();
	}

	public void clearContext() {

		sentence.delete(0, sentence.length());
		pinyinListSize = 0;
		candidates.clear();
		correctPosition = 0;

		for (int i = 0; i < MAX_SETENCE_LENGTH; i++) {
			for (int j = 0; j < MAX_WORD_LENGTH; j++) {
				searchMatrix[i][j] = null;
				addressMatrix[i][j] = null;
				freqencyMatrix[i][j] = -1;
				firstMatrix[i][j] = -1;
			}
		}

		candidatesCacheFlag = -1;
		hanzi.clearCache();
		CustomPinyin.getInstance().clearPinyin();
		System.gc();
	}

	private PinyinSentence GreedyAlgorithm(int orgl) {
		StringBuffer res = new StringBuffer();
		int wordCountWeight = 0;
		int charCountWeight = 0;
		int l = orgl;
		while (l < pinyinListSize) {
			for (int i = MAX_WORD_LENGTH - 1; i >= 0; i--) {
				if (firstMatrix[l][i] != -1) {
					String tempWord;
					boolean flag = false;
					if (pinyinListSize > 1 && l == pinyinListSize - 1 && i == 0) {
						if ((tempWord = lastChar.get(pinyinList[l])) != null) {
							wordCountWeight += (1 << (i + i));
							charCountWeight += freqencyMatrix[l][i];
							res.append(tempWord);
							break;
						}
					}

					for (int j = 2; j > 0; j--) {
						if (l - j - orgl > 0 && i + j < MAX_WORD_LENGTH) {
							if (firstMatrix[l - j][i + j] != -1) {
								tempWord = hanzi.getFirst(firstMatrix[l - j][i
										+ j]);
								if (tempWord.substring(0, j).equals(
										res.substring(l - orgl - j, l - orgl))) {
									wordCountWeight += (1 << (i + i));
									charCountWeight += freqencyMatrix[l][i] + 1;
									res.append(tempWord.substring(j, i + j + 1));
									flag = true;
									break;
								}
							}
						}
					}
					if (flag)
						break;

					wordCountWeight += (1 << (i + i));
					charCountWeight += freqencyMatrix[l][i];
					res.append(hanzi.getFirst(firstMatrix[l][i]));
					break;
				}
			}
			l = orgl + res.length();
		}
		return new PinyinSentence(res.toString(), wordCountWeight,
				charCountWeight);
	}

	private PinyinSentence LongestAlgorithm(int l) {
		int wordCountWeight = 0;
		int charCountWeight = 0;

		for (int i = 0; i < MAX_SETENCE_LENGTH; i++) {
			combiningChars[i] = '\0';
		}

		int combinedCount = 0;
		int maxFreqency, maxPosition;
		for (int i = 0; i < MAX_SETENCE_LENGTH; i++) {
			for (int j = 0; j < MAX_WORD_LENGTH; j++) {
				tFreqencyMatrix[i][j] = freqencyMatrix[i][j];
			}
		}

		int i = MAX_WORD_LENGTH - 1;
		while (pinyinListSize - l > combinedCount) {
			maxFreqency = -1;
			maxPosition = -1;
			for (int j = l; j < pinyinListSize - i; j++) {
				if (tFreqencyMatrix[j][i] > maxFreqency) {
					maxFreqency = tFreqencyMatrix[j][i];
					maxPosition = j;
				}
			}
			if (maxPosition == -1) {
				i--;
			} else {
				String word;
				if (pinyinListSize > 1 && maxPosition == pinyinListSize - 1
						&& i == 0) {
					word = lastChar.get(pinyinList[maxPosition]);
					if (word == null) {
						word = hanzi.getFirst(firstMatrix[maxPosition][i]);
					}
				} else {
					word = hanzi.getFirst(firstMatrix[maxPosition][i]);
				}
				wordCountWeight += (1 << (i + i));
				charCountWeight += freqencyMatrix[maxPosition][i];
				for (int j = 0; j < i + 1; j++) {
					combiningChars[maxPosition - l + j] = word.charAt(j);
				}
				for (int j = 0; j < i + 1; j++) {
					for (int k = 0; k < MAX_WORD_LENGTH; k++) {
						tFreqencyMatrix[maxPosition + j][k] = -1;
					}
				}
				for (int j = maxPosition - 1; j >= 0; j--) {
					for (int k = maxPosition - j; k < MAX_WORD_LENGTH; k++) {
						tFreqencyMatrix[j][k] = -1;
					}
				}
				combinedCount += word.length();
			}
		}

		return new PinyinSentence(new String(combiningChars, 0, combinedCount),
				wordCountWeight, charCountWeight);
	}

	private void updateSentence() {
		int l = sentence.length();
		if (correctPosition < l) {
			sentence.delete(correctPosition, l);
			l = correctPosition;
		}
		PinyinSentence greedy = GreedyAlgorithm(l);
		PinyinSentence longest = LongestAlgorithm(l);
		if (greedy.isBetterThan(longest)) {
			sentence.append(greedy.sentence);
		} else {
			sentence.append(longest.sentence);
		}
	}

	private void updateCandidate() {
		if (candidatesCacheFlag == correctPosition) {
			// return;
		}

		candidates.clear();
		if (CustomPinyin.getInstance().getPinyin() != null) {
			candidates.addAll(CustomPinyin.getInstance().getPinyin());
		}

		if (correctPosition >= MAX_SETENCE_LENGTH) {
			return;
		}

		for (int i = MAX_WORD_LENGTH - 1; i >= 0; i--) {
			if (addressMatrix[correctPosition][i] != null) {
				for (int address : addressMatrix[correctPosition][i]) {
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
		if (pinyinListSize == 0) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		sb.append(PinyinTree.getInstance().getPinyinString(pinyinList[0]));
		for (int i = 1; i < pinyinListSize; i++) {
			sb.append('\'');
			sb.append(PinyinTree.getInstance().getPinyinString(pinyinList[i]));
		}
		return sb.toString();
	}

	public void updateLianXiang(char lastHanzi) {
		lianxiang.fileOpen();
		if (sentence != null) {
			if (sentence.length() != 0)
				sentence.delete(0, sentence.length() - 1);
		}
		candidates.clear();
		lianxiang.getAll(lastHanzi, candidates);
		lianxiang.fileClose();
	}

	/*
	 * public static void main(String[] args) { PinyinUtils.PINYIN_LIST_FILE =
	 * "E:\\pinyin_list.txt"; PinyinContext context = new PinyinContext();
	 * 
	 * context.addPinyin("ce"); context.addPinyin("shi");
	 * context.addPinyin("ping"); context.addPinyin("ying");
	 * context.addPinyin("shu"); context.addPinyin("ru");
	 * context.addPinyin("fa");
	 * 
	 * context.chooseCandidate(0); System.out.println(context.getSentence());
	 * 
	 * context.removePinyin(); System.out.println(context.getSentence());
	 * 
	 * context.addPinyin("fa"); System.out.println(context.getSentence());
	 * 
	 * context.correctWord(1); context.chooseCandidate(1);
	 * System.out.println(context.getSentence());
	 * 
	 * context.removePinyin(); System.out.println(context.getSentence());
	 * 
	 * context.addPinyin("fa"); System.out.println(context.getSentence());
	 * 
	 * context.clearContext(); System.out.println(context.getSentence());
	 * 
	 * context.addPinyin("ce"); context.removePinyin();
	 * 
	 * context.addPinyin("ce"); context.removePinyin();
	 * 
	 * 
	 * context.addPinyin("li"); context.addPinyin("shi");
	 * context.addPinyin("shei"); System.out.println(context.getSentence());
	 * 
	 * context.clearContext();
	 * 
	 * context.updateLianXiang(); for (int i = 0; i < 100; i++){
	 * System.out.println(context.getCandidate(i)); } }
	 */
}
