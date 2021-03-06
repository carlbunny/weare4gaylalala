package com.aeviou.pinyin;

import java.util.HashMap;
import java.util.Stack;

public class PinyinLongestPath {

	private int MAX_SETENCE_LENGTH = 0;
	private int MAX_WORD_LENGTH = 0;
	private float[][] tFreqencyMatrix = null;
	private int[][] freqencyMatrix = null;
	private int[] pathMatrix = null;// store the next word pos
	private float[] maxValueStore = null;
	private int[] weightArr = null;
	private int pyLength = 0;

	private PinyinHanzi hanzi;
	private int[][] firstMatrix; // [index][length]
	private StringBuilder strBuilder;

	HashMap<Character, String> lastCharMap;
	private char[] pinyinList;

	public PinyinLongestPath(int MAX_SETENCE_LENGTH, int MAX_WORD_LENGTH,
			PinyinHanzi hanzi, int[][] firstMatrix, int[][] freqencyMatrix,
			HashMap<Character, String> lastChar, char[] pinyinList) {
		this.MAX_SETENCE_LENGTH = MAX_SETENCE_LENGTH;
		this.MAX_WORD_LENGTH = MAX_WORD_LENGTH;

		tFreqencyMatrix = new float[MAX_SETENCE_LENGTH][MAX_WORD_LENGTH];
		pathMatrix = new int[MAX_SETENCE_LENGTH];
		maxValueStore = new float[MAX_SETENCE_LENGTH + 1];
		maxValueStore[maxValueStore.length - 1] = 0;// edge case of DP

		weightArr = new int[MAX_WORD_LENGTH + 1];
		for (int i = 0; i < weightArr.length; i++) {
			weightArr[i] = 1 << (i * 5);
		}

		this.hanzi = hanzi;
		this.firstMatrix = firstMatrix;
		this.freqencyMatrix = freqencyMatrix;
		this.lastCharMap = lastChar;
		this.pinyinList = pinyinList;
		strBuilder = new StringBuilder();
	}

	private float searchNode(int startInMatrix) {
		// search this path
		if (pathMatrix[startInMatrix] != -1)
			return maxValueStore[startInMatrix];

		float maxValue = 0;
		for (int wordLength = 1; wordLength <= MAX_WORD_LENGTH
				&& startInMatrix + wordLength <= pyLength; wordLength++) {

			float valueInMatrix;
			if (startInMatrix == pyLength - 1
					&& lastCharMap.get(pinyinList[startInMatrix]) != null) {
				valueInMatrix = weightArr[wordLength + 1]
						* tFreqencyMatrix[startInMatrix][wordLength - 1];
			} else
				valueInMatrix = weightArr[wordLength]
						* tFreqencyMatrix[startInMatrix][wordLength - 1];

			if (valueInMatrix < 0) {
				continue;// cut
			} else

			if (startInMatrix + wordLength < pyLength)
				valueInMatrix = valueInMatrix
						+ searchNode(startInMatrix + wordLength);
			// faver the longer
			if (valueInMatrix >= maxValue) {
				maxValue = valueInMatrix;
				pathMatrix[startInMatrix] = startInMatrix + wordLength;
			}
		}
		maxValueStore[startInMatrix] = Math.max(maxValueStore[startInMatrix],
				maxValue);
		return maxValue;
	}

	public void searchIter(int startPos) {
		int currentPos = pyLength - 1;
		// init
		if (lastCharMap.containsKey((pinyinList[currentPos]))) {
			maxValueStore[currentPos] = weightArr[2];// 100% frequency
		} else {
			maxValueStore[currentPos] = weightArr[1]
					* tFreqencyMatrix[currentPos][0];
		}
		float tValue = -1;
		for (currentPos -= 1; currentPos >= startPos; currentPos--) {
			// partition
			for (int firstLength = 1; currentPos + firstLength <= pyLength
					&& firstLength <= MAX_WORD_LENGTH; firstLength++) {
				if (tFreqencyMatrix[currentPos][firstLength - 1] != -1) {
					tValue = weightArr[firstLength]
							* tFreqencyMatrix[currentPos][firstLength - 1]
							+ maxValueStore[currentPos + firstLength];
					if (tValue >= maxValueStore[currentPos]) {
						maxValueStore[currentPos] = tValue;
						pathMatrix[currentPos] = currentPos + firstLength;
					}
				}

			}
		}

	}

	private void Frequency2Weight() {
		for (int j = 0; j < MAX_WORD_LENGTH; j++) {
			float sum = 0;
			for (int i = 0; i < MAX_SETENCE_LENGTH; i++) {
				if (tFreqencyMatrix[i][j] >= 0) {
					sum += tFreqencyMatrix[i][j];
				}
			}
			for (int i = 0; i < MAX_SETENCE_LENGTH; i++) {
				if (tFreqencyMatrix[i][j] >= 0) {
					tFreqencyMatrix[i][j] /= sum;
				}

			}
		}
	}

	public String LongestTreeAlgorithm(int startPos, int pinyinListSize) {

		pyLength = pinyinListSize;

		for (int i = 0; i < MAX_SETENCE_LENGTH; i++) {
			for (int j = 0; j < MAX_WORD_LENGTH; j++) {
				tFreqencyMatrix[i][j] = freqencyMatrix[i][j];
				pathMatrix[i] = -1;
				maxValueStore[i] = -1;
			}
		}
		Frequency2Weight();

		// searchNode(startPos);
		searchIter(startPos);
		pathMatrix[pyLength - 1] = pyLength;

		strBuilder.delete(0, strBuilder.length());
		for (; startPos < pyLength;) {
			int wordEndIndex = pathMatrix[startPos] - 1;
			if (startPos == pyLength - 1
					&& lastCharMap.containsKey((pinyinList[startPos]))) {
				strBuilder.append(lastCharMap.get(pinyinList[startPos]));
			} else {
				strBuilder
						.append(hanzi
								.getFirst(firstMatrix[startPos][wordEndIndex
										- startPos]));
			}
			startPos = pathMatrix[startPos];
		}

		return strBuilder.toString();
	}
}
