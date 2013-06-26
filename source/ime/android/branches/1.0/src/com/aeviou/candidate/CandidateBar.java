package com.aeviou.candidate;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.aeviou.key.SquareKey;
import com.aeviou.keyboard.KeyboardFactory;
import com.aeviou.pinyin.CustomPinyin;
import com.aeviou.pinyin.PinyinContext;
import com.aeviou.utils.AeviouConstants;
import com.aeviou.utils.BitmapUtils;

public class CandidateBar extends SquareKey {
	private static int candidateCount = 6;
	private static int maxPageCount = 30;

	public static boolean invalidated = true;
	public PinyinContext pinyinContext;
	private CandidateKey[] keys;
	private Paint paint;
	private String sentence;
	private int page;
	private int endCandidate;
	private int startCandidate;
	private boolean lastPage;
	private boolean touched, hided, inLianxiang, deletePrevious;
	private int[] stack;
	private int[] occupyCount;
	private int keyboardMode;
	private int lastPinyinLength;
	private SentenceView sentenceView;

	private int touchedPointX;

	public boolean isOpen = false;

	public CandidateBar(PinyinContext pinyinContext, int keyboardMode) {
		this.keyboardMode = keyboardMode;
		if (this.keyboardMode == KeyboardFactory.KEYBOARD_MODE_FULL) {
			candidateCount = 9;
			AeviouConstants.NORMAL_CANDIDATE_LEFT_MARGIN = AeviouConstants.NORMAL_IME_HEXKEY_WIDTH;
			AeviouConstants.NORMAL_CANDIDATE_RARROW_LEFT_MARGIN = AeviouConstants.NORMAL_CANDIDATE_RARROW_LEFT_MARGIN_FULL;
		} else {
			candidateCount = 6;
			AeviouConstants.NORMAL_CANDIDATE_LEFT_MARGIN = AeviouConstants.NORMAL_IME_HEXKEY_WIDTH / 2;
			AeviouConstants.NORMAL_CANDIDATE_RARROW_LEFT_MARGIN = AeviouConstants.NORMAL_CANDIDATE_RARROW_LEFT_MARGIN_SIMPLE;
		}
		paint = new Paint();
		paint.setColor(Color.WHITE);
		this.pinyinContext = pinyinContext;
		this.keys = new CandidateKey[candidateCount];

		sentenceView = new SentenceView(
				AeviouConstants.keyboardView.getContext());

		hided = false;
		deletePrevious = false;

		for (int i = 0; i < candidateCount; i++) {
			keys[i] = new CandidateKey();
			keys[i].y = AeviouConstants.NORMAL_CANDIDATE_TOP_MARGIN;
			keys[i].x = AeviouConstants.NORMAL_CANDIDATE_LEFT_MARGIN + i
					* AeviouConstants.NORMAL_CANDIDATE_KEY_WIDTH;
		}
		page = 0;
		stack = new int[maxPageCount];
		touched = false;
		lastPage = true;
		inLianxiang = false;
		occupyCount = new int[PinyinContext.MAX_SETENCE_LENGTH + 1];
		occupyCount[0] = 0;
		occupyCount[1] = 1;
		occupyCount[2] = 1;
		occupyCount[3] = 2;
		occupyCount[4] = 2;
		occupyCount[5] = 3;
		occupyCount[6] = 3;
		occupyCount[7] = 3;
		occupyCount[8] = 4;
		occupyCount[9] = 4;
		occupyCount[10] = 4;
		lastPinyinLength = 0;
	}

	public void updatePinyin() {
		page = 0;
		lastPage = false;
		inLianxiang = false;
		updateContext();
	}

	private void nextPage() {
		if (page == maxPageCount)
			return;
		if (lastPage)
			return;
		stack[page] = endCandidate;
		startCandidate = endCandidate;
		page++;
		updateContext();
	}

	private void previousPage() {
		if (page <= 0)
			return;
		page--;
		if (page > 0)
			startCandidate = stack[page - 1];
		lastPage = false;
		updateContext();
	}

	public void clearCandidateKey() {
		for (int i = 0; i < candidateCount; i++) {
			keys[i].type = CandidateKey.TYPE_SINGLE;
			keys[i].word = null;
			keys[i].candidateId = -1;
		}
	}

	public void setDeletePrevious() {
		deletePrevious = true;
		lastPinyinLength = pinyinContext.getSize();
	}

	public void updateContext() {
		clearCandidateKey();
		if (lastPinyinLength != pinyinContext.getSize()) {
			if (deletePrevious && pinyinContext.getSize() != 0) {
				AeviouConstants.inputMethodService.getCurrentInputConnection()
						.deleteSurroundingText(1, 0);
			}
			deletePrevious = false;
		}
		sentence = pinyinContext.getChoosedSentence() + " "
				+ pinyinContext.getRemainPinyin();
		if (sentence.equals(" ") && !inLianxiang) {
			AeviouConstants.needRedraw = true;
			invalidated = false;
			sentence = null;
			AeviouConstants.keyboardView.updateHybridKeyboard(true);
			return;
		} else {
			AeviouConstants.keyboardView.updateHybridKeyboard(false);
		}

		int p = 0;
		int length;
		String remainSentence = null;
		if (page == 0) {
			startCandidate = 0;
			if (!inLianxiang) {
				remainSentence = pinyinContext.getRemainSentence();
				length = occupyCount[remainSentence.length()];
				for (int i = 0; i < length; i++) {
					if (i == 0 && i == length - 1) {
						keys[i].type = CandidateKey.TYPE_SINGLE;
						keys[i].word = remainSentence;
					} else if (i == 0) {
						keys[i].type = CandidateKey.TYPE_LEFT;
						keys[i].word = remainSentence;
					} else if (i == length - 1) {
						keys[i].type = CandidateKey.TYPE_RIGHT;
						keys[i].word = null;
					} else {
						keys[i].type = CandidateKey.TYPE_MID;
						keys[i].word = null;
					}
					keys[i].candidateId = 0;
				}
				p = length;
			}
		}

		endCandidate = startCandidate;

		while (true) {
			String word = pinyinContext.getCandidate(endCandidate);
			if (word == null) {
				lastPage = true;
				break;
			}
			if (!inLianxiang && word.equals(remainSentence)) {
				endCandidate++;
				continue;
			}
			length = occupyCount[word.length()];
			if (p + length <= candidateCount) {
				for (int i = 0; i < length; i++) {
					if (i == 0 && i == length - 1) {
						keys[p + i].type = CandidateKey.TYPE_SINGLE;
						keys[p + i].word = word;
					} else if (i == 0) {
						keys[p + i].type = CandidateKey.TYPE_LEFT;
						keys[p + i].word = word;
					} else if (i == length - 1) {
						keys[p + i].type = CandidateKey.TYPE_RIGHT;
						keys[p + i].word = null;
					} else {
						keys[p + i].type = CandidateKey.TYPE_MID;
						keys[p + i].word = null;
					}
					keys[p + i].candidateId = endCandidate + 1;
				}
				p += length;
				endCandidate++;
			} else {
				break;
			}
		}

		invalidated = false;
		AeviouConstants.needRedraw = true;
	}

	public void draw(Canvas canvas) {
		if (invalidated)
			updateContext();
		paint.setTextSize(AeviouConstants.NORMAL_CANDIDATE_WORD);
		paint.setColor(Color.BLACK);
		for (int i = 0; i < candidateCount; i++) {
			keys[i].drawBackground(canvas, paint);
		}
		for (int i = 0; i < candidateCount; i++) {
			keys[i].drawText(canvas, paint);
		}

		sentenceView.setSentence(sentence);
		sentenceView.commitUpdate();

		hided = false;

		if (page == 0) {
			if (inLianxiang) {
				BitmapUtils.drawBitmap(canvas, paint,
						AeviouConstants.PIC_ID.CLOSE,
						AeviouConstants.NORMAL_CANDIDATE_LARROW_LEFT_MARGIN,
						AeviouConstants.NORMAL_CANDIDATE_ARROW_TOP_MARGIN,
						AeviouConstants.NORMAL_CANDIDATE_ARROW_WIDTH,
						AeviouConstants.NORMAL_CANDIDATE_ARROW_HEIGHT);
			}
		} else {
			BitmapUtils.drawBitmap(canvas, paint,
					AeviouConstants.PIC_ID.CANDIDATE_LEFT_ARROW,
					AeviouConstants.NORMAL_CANDIDATE_LARROW_LEFT_MARGIN,
					AeviouConstants.NORMAL_CANDIDATE_ARROW_TOP_MARGIN,
					AeviouConstants.NORMAL_CANDIDATE_ARROW_WIDTH,
					AeviouConstants.NORMAL_CANDIDATE_ARROW_HEIGHT);
		}
		if (!lastPage) {
			BitmapUtils.drawBitmap(canvas, paint,
					AeviouConstants.PIC_ID.CANDIDATE_RIGHT_ARROW,
					AeviouConstants.NORMAL_CANDIDATE_RARROW_LEFT_MARGIN,
					AeviouConstants.NORMAL_CANDIDATE_ARROW_TOP_MARGIN,
					AeviouConstants.NORMAL_CANDIDATE_ARROW_WIDTH,
					AeviouConstants.NORMAL_CANDIDATE_ARROW_HEIGHT);
		}
	}

	public boolean occupyBarView() {
		if (invalidated)
			updateContext();
		return keys[0].word != null;
	}

	public void onTouch(int x, int y) {
		touched = true;
		touchedPointX = x;
	}

	public void onMove(int x, int y) {
	}

	public void onRelease(int x, int y) {
		if (!touched)
			return;
		touched = false;

		final int MOVE_THRESHOLD = 50;
		if (Math.abs(x - this.touchedPointX) > MOVE_THRESHOLD) {
			if (x < this.touchedPointX) {
				nextPage();
			} else {
				previousPage();
			}
			return;
		}

		x -= AeviouConstants.NORMAL_CANDIDATE_LEFT_MARGIN;
		if (x < 0) {
			if (inLianxiang && page == 0) {
				pinyinContext.clearContext();
				inLianxiang = false;
				updatePinyin();
			} else {
				previousPage();
			}
			return;
		}
		int id = x / AeviouConstants.NORMAL_IME_HEXKEY_WIDTH;
		if (id >= candidateCount) {
			nextPage();
			return;
		}
		if (keys[id].candidateId == -1)
			return;

		if (inLianxiang) {
			String commitSentence = pinyinContext
					.getCandidate(keys[id].candidateId - 1);
			char lastHanzi = commitSentence.charAt(commitSentence.length() - 1);
			AeviouConstants.commitText(commitSentence);
			pinyinContext.clearContext();
			pinyinContext.updateLianXiang(lastHanzi);
			inLianxiang = true;
			page = 0;
			lastPage = false;
			updateContext();
		} else {
			if (keys[id].candidateId == 0) {
				String commitSentence = pinyinContext.getSentence();
				char lastHanzi = commitSentence
						.charAt(commitSentence.length() - 1);
				if (deletePrevious) {
					AeviouConstants.inputMethodService
							.getCurrentInputConnection().deleteSurroundingText(
									1, 0);
					deletePrevious = false;
				}
				if (pinyinContext.getCorrectPosition() > 0) {
					CustomPinyin.getInstance().recordPinyin(commitSentence);
				}
				AeviouConstants.commitText(commitSentence);
				pinyinContext.clearContext();
				pinyinContext.updateLianXiang(lastHanzi);
				inLianxiang = true;
				page = 0;
				lastPage = false;
				updateContext();
			} else {

				pinyinContext.chooseCandidate(keys[id].candidateId - 1);
				String commitSentence = pinyinContext.getSentence();
				char lastHanzi = commitSentence
						.charAt(commitSentence.length() - 1);
				if (pinyinContext.getCorrectPosition() >= pinyinContext
						.getSize()) {
					if (deletePrevious) {
						AeviouConstants.inputMethodService
								.getCurrentInputConnection()
								.deleteSurroundingText(1, 0);
						deletePrevious = false;
					}

					if (commitSentence.length() > 1) {
						CustomPinyin.getInstance().recordPinyin(commitSentence);
					}

					AeviouConstants.commitText(pinyinContext.getSentence());
					pinyinContext.clearContext();
					pinyinContext.updateLianXiang(lastHanzi);
					inLianxiang = true;

				}
				page = 0;
				lastPage = false;
				updateContext();
			}
		}
	}

	public void hideSentenceView() {
		if (!hided) {
			sentenceView.setVisibility(View.INVISIBLE);
			hided = true;
		}
	}

	public void reset() {
		sentenceView.setSentence(sentence);
		sentenceView.reset();
		sentenceView.commitUpdate();
		isOpen = false;
	}

	public void close() {
		pinyinContext.clearContext();
		updatePinyin();
		sentenceView.closeWindow();
	}

	public boolean inGeometry(int x, int y) {
		return y <= AeviouConstants.NORMAL_BAR_HEIGHT;
	}

}
