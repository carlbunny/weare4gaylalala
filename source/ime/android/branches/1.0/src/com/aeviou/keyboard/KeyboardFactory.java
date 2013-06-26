package com.aeviou.keyboard;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.aeviou.R;

import com.aeviou.candidate.CandidateBar;
import com.aeviou.key.AbstractKey;
import com.aeviou.key.HexKey;
import com.aeviou.key.SettingKey;
import com.aeviou.key.SettingKeyBoard;
import com.aeviou.key.SquareKey;
import com.aeviou.key.SettingKey.SwitchKeyEnum;
import com.aeviou.pinyin.PinyinContext;
import com.aeviou.utils.AeviouConstants;

public class KeyboardFactory {
	public static final int KEYBOARD_MODE_FULL = 1;
	public static final int KEYBOARD_MODE_SIMPLE = 0;

	private static KeyboardFactory instance;

	public static KeyboardFactory getInstance() {
		if (instance == null) {
			instance = new KeyboardFactory();
		}
		return instance;
	}
	
	final int[][] offsetsOdd = { { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 },
			{ 1, 0 }, { 1, 1 } };
	final int[][] offsetsEven = { { -1, -1 }, { -1, 0 }, { 0, -1 }, { 0, 1 },
			{ 1, -1 }, { 1, 0 } };

	public AbstractKeyboard getKeyboardEntity(int file, int keyboardMode) {
		Scanner keyboardFile;
		AbstractKeyboard keyboard = null;
		String[] line;
		keyboardFile = new Scanner(new InputStreamReader(
				AeviouConstants.inputMethodService.getResources()
						.openRawResource(file), Charset.forName("utf-8")));
		if (keyboardFile.hasNextLine()) {
			line = keyboardFile.nextLine().split(" ");
			if (line[0].equals("hexboard")) {
				keyboard = createHexKeyboard(keyboardFile,
						Integer.parseInt(line[1]), keyboardMode);
//			} else if (line[0].equals("squareboard")) {
//				keyboard = createSquareKeyboard(keyboardFile,
//						Integer.parseInt(line[1]), keyboardMode);
			} else if (line[0].equals("9grid")) {
				keyboard = createNumberKeyboard(keyboardFile,
						Integer.parseInt(line[1]), keyboardMode);
			}
		}
		keyboardFile.close();
		return keyboard;
	}

	protected char getKeyName(String name) {
		if (name.length() == 1) {
			return name.charAt(0);
		}
		if (name.equals("'hide")) {
			return AeviouConstants.KEYCODE_NULL;
		} else if (name.equals("'enter")) {
			return AeviouConstants.KEYCODE_ENTER;
		} else if (name.equals("'space")) {
			return AeviouConstants.KEYCODE_SPACE;
		} else if (name.equals("'backspace")) {
			return AeviouConstants.KEYCODE_BACKSPACE;
		} else if (name.equals("'aoe")) {
			return AeviouConstants.KEYCODE_AOE;
		} else if (name.equals("'shift")) {
			return AeviouConstants.KEYCODE_SHIFT;
		} else if (name.equals("'caps")) {
			return AeviouConstants.KEYCODE_CAPS;
		} else if (name.equals("'switch")) {
			return AeviouConstants.KEYCODE_SWITCH;
		} else if(name.equals("'symbolcenter")){
			return AeviouConstants.KEYCODE_SYMBOLCENTER;
		}else {
				return name.charAt(0);
			}
	}

	protected int getKeyType(String type) {
		if (type.equals("letter")) {
			return AbstractKey.TYPE_KEY_LETTER;
		} else if (type.equals("symbol")) {
			return AbstractKey.TYPE_KEY_SYMBOL;
		} else if (type.equals("enter")) {
			return AbstractKey.TYPE_KEY_ENTER;
		} else if (type.equals("space")) {
			return AbstractKey.TYPE_KEY_SPACE;
		} else if (type.equals("backspace")) {
			return AbstractKey.TYPE_KEY_BACKSPACE;
		} else if (type.equals("shift")) {
			return AbstractKey.TYPE_KEY_SHIFT;
		} else if (type.equals("caps")) {
			return AbstractKey.TYPE_KEY_CAPS;
		} else if (type.equals("switch")) {
			return AbstractKey.TYPE_KEY_SWITCH;
		} else {
			return HexKey.TYPE_KEY_HIDE;
		}
	}

	protected SwitchKeyEnum getSettingType(String type) {
		SwitchKeyEnum enumType = null;
		if (type.equals("'Number")) {
			enumType = SettingKey.SwitchKeyEnum.Number;
		} else if (type.equals("'Chinese")) {
			enumType = SettingKey.SwitchKeyEnum.Chinese;
		} else if (type.equals("'English")) {
			enumType = SettingKey.SwitchKeyEnum.English;
		} else if (type.equals("'Symbol")) {
			enumType = SettingKey.SwitchKeyEnum.Symbol;
		} else if (type.equals("'Setting")) {
			enumType = SettingKey.SwitchKeyEnum.Setting;
		}
		return enumType;
	}

	public NumberKeyboard createNumberKeyboard(Scanner keyboardFile,
			int keyCount, int keyboardMode) {
		NumberKeyboard keyboard;
		SettingKeyBoard settingkeyBoard = null;
		AbstractKey[] keys = new AbstractKey[keyCount];
		String line[];

		line = keyboardFile.nextLine().split(" ");
		int gridX = Integer.parseInt(line[0]), gridY = Integer
				.parseInt(line[1]);
		int keyWidth = AeviouConstants.NORMAL_IME_VIEW_WIDTH / gridX;
		int keyHeight = AeviouConstants.NORMAL_IME_VIEW_HEIGHT / gridY;

		for (int i = 0; i < keyCount; i++) {
			String rawLine = keyboardFile.nextLine();
			// skype comment
			while (rawLine.length() == 0 || rawLine.startsWith("//"))
				rawLine = keyboardFile.nextLine();

			line = rawLine.split(" ");
			int type = getKeyType(line[2]);

			AbstractKey key = null;
			if (type == AbstractKey.TYPE_KEY_SWITCH) {
				key = settingkeyBoard = createSettingKeyBoard();
			} else {
				key = new SquareKey();
				key.setSize(keyWidth, keyHeight);
			}
			keys[i] = key;

			key.id = i;
			key.name = getKeyName(line[1]);
			key.type = getKeyType(line[2]);

			key.x = Integer.parseInt(line[4]) * keyWidth;
			if (keyboardMode == KEYBOARD_MODE_FULL) {
				key.x *= 1.51;
			}
			key.y = AeviouConstants.NORMAL_SQUAREKEYBOARD_TOP_MARGIN
					+ Integer.parseInt(line[3]) * keyHeight;
			// if (Integer.parseInt(line[7]) == 1){
			// key.x += AeviouConstants.NORMAL_IME_SQUAREKEY_WIDTH / 2;
			// }
			// key.widthStep = Float.parseFloat(line[6]);
			key.widthStep = 1;
			if (keyboardMode == KEYBOARD_MODE_FULL) {
				key.widthStep *= 1.5;
			}
			key.calCenter();

		}
		keyboard = new NumberKeyboard(keys);

		// set the the lowleft corner
		int settingKeyWidth = (int) (keyWidth / 1.5f), settingKeyHeight = keyHeight;
		settingkeyBoard.setSize(settingKeyWidth, settingKeyHeight);

		settingkeyBoard.x = (int) ((1.0f / 3) * keyWidth);
		settingkeyBoard.y = AeviouConstants.NORMAL_IME_VIEW_HEIGHT
				- settingKeyHeight;
		settingkeyBoard.calCenter();
		keyboard.settingkeyBoard = settingkeyBoard;
		return keyboard;
	}

//	public SquareKeyboard createSquareKeyboard(Scanner keyboardFile,
//			int keyCount, int keyboardMode) {
//		SquareKeyboard keyboard;
//		SquareKey[] keys = new SquareKey[keyCount];
//		String line[];
//		for (int i = 0; i < keyCount; i++) {
//			SquareKey key = new SquareKey();
//			line = keyboardFile.nextLine().split(" ");
//			keys[i] = key;
//
//			key.id = i;
//			key.name = getKeyName(line[1]);
//			key.capName = getKeyName(line[2]);
//			key.type = getKeyType(line[3]);
//			key.x = Integer.parseInt(line[5])
//					* AeviouConstants.NORMAL_IME_SQUAREKEY_WIDTH;
//			if (keyboardMode == KEYBOARD_MODE_FULL) {
//				key.x *= 1.51;
//			}
//			key.y = AeviouConstants.NORMAL_BAR_HEIGHT
//					+ AeviouConstants.NORMAL_SQUAREKEYBOARD_TOP_MARGIN
//					+ Integer.parseInt(line[4])
//					* AeviouConstants.NORMAL_IME_SQUAREKEY_HEIGHT;
//			if (Integer.parseInt(line[7]) == 1) {
//				key.x += AeviouConstants.NORMAL_IME_SQUAREKEY_WIDTH / 2;
//			}
//			key.widthStep = Float.parseFloat(line[6]);
//			if (keyboardMode == KEYBOARD_MODE_FULL) {
//				key.widthStep *= 1.5;
//			}
//			key.centerX = (int) (key.x + AeviouConstants.NORMAL_IME_SQUAREKEY_WIDTH
//					* key.widthStep / 2);
//			key.centerY = key.y + AeviouConstants.NORMAL_IME_SQUAREKEY_HEIGHT
//					/ 2;
//
//			keys[i] = key;
//		}
//		keyboard = new SquareKeyboard(keys);
//		keyboard.settingkeyBoard = AeviouConstants.settingKeyBoard;
//		return keyboard;
//	}
	


	public HexKeyboard createHexKeyboard(Scanner keyboardFile, int keyCount,
			int keyboardMode) {
		HexKeyboard keyboard;
		SettingKeyBoard settingKeyBoard = null;
		HexKey[] keys = new HexKey[keyCount];
		String line[];
		for (int i = 0; i < keyCount; i++) {
			String rawLine = keyboardFile.nextLine();
			// skype comment
			if (rawLine.length() == 0 || rawLine.startsWith("//"))
				continue;
			line = rawLine.split(" ");

			int type = getKeyType(line[2]);
			HexKey key;

			if (type == AbstractKey.TYPE_KEY_SWITCH) {
				key = settingKeyBoard = createSettingKeyBoard();
			} else
				key = new HexKey();

			keys[i] = key;

			key.id = i;

			key.name = getKeyName(line[1]);
			key.type = getKeyType(line[2]);

			int row = Integer.parseInt(line[3]);
			key.y = AeviouConstants.NORMAL_KEYBOARD_TOP_MARGIN
					+ (int) (AeviouConstants.NORMAL_IME_HEXKEY_HEIGHT * row * 0.75);
			if (row % 2 == keyboardMode) {
				key.x = (int) (AeviouConstants.NORMAL_IME_HEXKEY_WIDTH * (Integer
						.parseInt(line[4]) + 0.5f));
			} else {
				key.x = (int) (AeviouConstants.NORMAL_IME_HEXKEY_WIDTH * (Integer
						.parseInt(line[4])));
			}
			key.calCenter();
		}

		for (int i = 0; i < keyCount; i++) {
			HexKey key = keys[i];
			line = keyboardFile.nextLine().split(" ");
			key.neighbours = new HexKey[6];
			for (int j = 0; j < 6; j++) {
				if (line[j + 1].equals("null")) {
					key.neighbours[j] = null;
				} else {
					key.neighbours[j] = keys[Integer.parseInt(line[j + 1])];
				}
			}
		}

		for (int i = 0; i < keyCount; i++) {
			line = keyboardFile.nextLine().split(" ");
			int dynamicCount = Integer.parseInt(line[1]);
			if (dynamicCount != 0) {
				line = keyboardFile.nextLine().split(" ");
				int[] ids = new int[dynamicCount];
				char[] names = new char[dynamicCount];
				for (int j = 0; j < dynamicCount; j++) {
					ids[j] = Integer.parseInt(line[j * 2]);
					names[j] = line[j * 2 + 1].charAt(0);
				}
				keys[i].dynamicKeyIds = ids;
				keys[i].dynamicKeyNames = names;
			} else {
				if (keyboardFile.hasNextLine()) {
					keyboardFile.nextLine();
				}
			}
		}
		CandidateBar candidateBar = new CandidateBar(new PinyinContext(),keyboardMode);
		candidateBar.setPos(0, 0);
//		+candidateBar.setSize(AeviouConstants.IME_VIEW_WIDTH, AeviouConstants.NORMAL_BAR_HEIGHT);
		
		keyboard = new HexKeyboard(keys,candidateBar);

		keyboard.settingkeyBoard = settingKeyBoard;

		return keyboard;
	}

	public SymbolKeyboard createSymbolKeyboard(){
		Scanner keyboardFile = new Scanner(new InputStreamReader(
				AeviouConstants.inputMethodService.getResources()
						.openRawResource(R.raw.symbol_simple),
				Charset.forName("utf-8")));
		
		SymbolKeyboard keyboard;
		SettingKeyBoard settingKeyBoard = null;

		String rawLine = keyboardFile.nextLine();
		String lineElement[];

		rawLine = skipComment(keyboardFile);

		lineElement = rawLine.split(" ");

		int gridX = Integer.parseInt(lineElement[0]), gridY = Integer
				.parseInt(lineElement[1]);
		int keyWidth = AeviouConstants.NORMAL_IME_VIEW_WIDTH / gridX;
		int keyHeight = (int) (AeviouConstants.NORMAL_IME_VIEW_HEIGHT / ((gridY - 1) * 0.75f + 1));
		HexKey keyGrid[][] = new HexKey[keyHeight][keyWidth];
		
		rawLine = skipComment(keyboardFile);
		int num = Integer.valueOf(rawLine);
		int symbolNum=0;
		HexKey keys[] = new HexKey[num];
		
		for (int i = 0; i < num; i++) {

			rawLine = skipComment(keyboardFile);
			lineElement = rawLine.split(" ");

			int type = getKeyType(lineElement[2]);
			HexKey key;

			if (type == AbstractKey.TYPE_KEY_SWITCH) {
				key = settingKeyBoard = createSettingKeyBoard();
			} else{
				key = new HexKey();
			}
			key.id = i;
			key.name = getKeyName(lineElement[1]);
			key.type =type;
			key.neighbours=new HexKey[0];
			
			if(type==AbstractKey.TYPE_KEY_SYMBOL){
				symbolNum++;}
//				key.name=AeviouConstants.KEYCODE_SYMBOL;
//				key.drawPic=AeviouConstants.PIC_ID.valueOf("symbol_"+i);
//			}else key.drawPic=AeviouConstants.PIC_ID.NULL;
			
			key.setSize(keyWidth, keyHeight);

			
			int row = Integer.parseInt(lineElement[3]);
			int col = Integer.parseInt(lineElement[4]);
			key.setRowCol(row, col);
			key.y = (int) (keyHeight * row * 0.75);
			if (Math.abs(row % 2) == 1) {
				key.x = (int) (keyWidth * (col + 0.5f));
			} else {
				key.x = (int) (keyWidth * col);
			}
			key.calCenter();

			keyGrid[row][col]=keys[i]=key;
		}
		
		// for neighbour
		char[] sixNeightbour=new char[6];
		StringBuilder strBuilder=new StringBuilder();
		for(int i=0;i<symbolNum;i++){
			rawLine = skipComment(keyboardFile);
			int id=Integer.valueOf(rawLine);
			
			rawLine = skipComment(keyboardFile);
			lineElement = rawLine.split(" ");
			
			HexKey key = keys[id];
			key.dynamicKeyIds=new int[lineElement.length];
			key.dynamicKeyNames=new char[lineElement.length];
			
			key.hexText=new String[3];
			Arrays.fill(sixNeightbour, ' ');
			
			int row = key.row, col = key.col;
			int[][] offsets=(Math.abs(row%2)==1)?offsetsOdd:offsetsEven;
			int n_index=0;
			int textIndex=0;
			for (int[] offset : offsets) {
				int newRow = row + offset[0];
				if (newRow < 0 || newRow >= gridY){
					sixNeightbour[textIndex++]=' ';
					continue;
				}
				int newCol = col + offset[1];
				if (newCol < 0 || newCol >= gridX){
					sixNeightbour[textIndex++]=' ';
					continue;
				}
				HexKey neighbourKey=keyGrid[newRow][newCol];
				if (neighbourKey!= null){
					key.dynamicKeyIds[n_index]=neighbourKey.id;
					key.dynamicKeyNames[n_index]=lineElement[n_index].charAt(0);
					sixNeightbour[textIndex++]=key.dynamicKeyNames[n_index];
					n_index++;
					if(n_index==lineElement.length)
						break;
				}
			}
			//fill the hexText
			strBuilder.delete( 0, strBuilder.length() );
			strBuilder.append(sixNeightbour[0]).append(' ').append(sixNeightbour[1]);
			key.hexText[0]=strBuilder.toString();
			strBuilder.delete( 0, strBuilder.length() );
			strBuilder.append(sixNeightbour[2]).append("     ").append(sixNeightbour[3]);
			key.hexText[1]=strBuilder.toString();
			strBuilder.delete( 0, strBuilder.length() );
			strBuilder.append(sixNeightbour[4]).append(' ').append(sixNeightbour[5]);
			key.hexText[2]=strBuilder.toString();
		}
		keyboard = new SymbolKeyboard(keys);

		keyboard.settingkeyBoard = settingKeyBoard;

		return keyboard;
	}
	
	HexEnglishKeyboard createSimpleKeyboard() {

		Scanner keyboardFile = new Scanner(new InputStreamReader(
				AeviouConstants.inputMethodService.getResources()
						.openRawResource(R.raw.english_hex),
				Charset.forName("utf-8")));

		SettingKeyBoard settingKeyBoard = null;

		String rawLine = keyboardFile.nextLine();
		String lineElement[];

		rawLine = skipComment(keyboardFile);

		lineElement = rawLine.split(" ");

		int gridX = Integer.parseInt(lineElement[0]), gridY = Integer
				.parseInt(lineElement[1]);
		int keyWidth = AeviouConstants.NORMAL_IME_VIEW_WIDTH / gridX;
		int keyHeight = (int) (AeviouConstants.NORMAL_IME_VIEW_HEIGHT / ((gridY - 1) * 0.75f + 1));
		HexKey keyGrid[][] = new HexKey[keyHeight][keyWidth];

		rawLine = skipComment(keyboardFile);
		int num = Integer.valueOf(rawLine);

		HexKey keys[] = new HexKey[num];
		boolean autoNeighborArr[] = new boolean[num];
		for (int i = 0; i < num; i++) {

			rawLine = skipComment(keyboardFile);
			lineElement = rawLine.split(" ");

			int type = getKeyType(lineElement[3]);
			HexKey key;

			if (type == AbstractKey.TYPE_KEY_SWITCH) {
				key = settingKeyBoard = createSettingKeyBoard();
			} else
				key = new HexKey();

			key.id = i;
			key.name = getKeyName(lineElement[1]);
			key.capName=getKeyName(lineElement[2]);
			key.type =type;

			key.setSize(keyWidth, keyHeight);

			
			int row = Integer.parseInt(lineElement[4]);
			int col = Integer.parseInt(lineElement[5]);
			key.setRowCol(row, col);
			key.y = (int) (keyHeight * row * 0.75);
			if (Math.abs(row % 2) == 1) {
				key.x = (int) (keyWidth * (col + 0.5f));
			} else {
				key.x = (int) (keyWidth * col);
			}
			key.calCenter();

			// for neighbour
			if (key.type == HexKey.TYPE_KEY_LETTER
					|| key.type == HexKey.TYPE_KEY_SYMBOL)
				keyGrid[row][col] = key;
			autoNeighborArr[i] = lineElement[6].equals("'auto");
			keys[i] = key;
		}
		// fill neighbour
		
		ArrayList<HexKey> neighborBuffer = new ArrayList<HexKey>();
		for (int i = 0; i < num; i++) {
			neighborBuffer.clear();
			if (autoNeighborArr[i] == false){
				keys[i].neighbours=new HexKey[0];
				continue;
			}
				
			HexKey key = keys[i];
			int row = key.row, col = key.col;
			int[][] offsets=(Math.abs(row%2)==1)?offsetsOdd:offsetsEven;
			for (int[] offset : offsets) {
				int newRow = row + offset[0];
				if (newRow < 0 || newRow >= gridY)
					continue;
				int newCol = col + offset[1];
				if (newCol < 0 || newCol >= gridX)
					continue;
				if (keyGrid[newRow][newCol] != null)
					neighborBuffer.add(keyGrid[newRow][newCol]);
			}
			key.neighbours=new HexKey[0];
			key.neighbours = (HexKey[]) neighborBuffer.toArray(key.neighbours);
			
			//fill left right
			if(col>0&&keyGrid[row][col-1] != null){
				key.leftChar=keyGrid[row][col-1].name;
			}
			if(col<gridX-1&&keyGrid[row][col+1] != null){
				key.rightChar=keyGrid[row][col+1].name;
			}
		}
		
		HexEnglishKeyboard keyboard = new HexEnglishKeyboard(keys);
		keyboard.settingkeyBoard = settingKeyBoard;
		keyboard.setGridResolution(gridX, gridY);
		
		return keyboard;
	}

	SettingKeyBoard createSettingKeyBoard() {
		Scanner keyboardFile = new Scanner(new InputStreamReader(
				AeviouConstants.inputMethodService.getResources()
						.openRawResource(R.raw.setting_hex),
				Charset.forName("utf-8")));

		String rawLine = keyboardFile.nextLine();
		String lineElement[];

		rawLine = skipComment(keyboardFile);

		int num = Integer.valueOf(rawLine);
		SettingKeyBoard keyBoard = new SettingKeyBoard();
		keyBoard.keys = new SettingKey[num];

		for (int i = 0; i < num; i++) {
			rawLine = skipComment(keyboardFile);
			lineElement = rawLine.split(" ");
			int index = Integer.valueOf(lineElement[0]);
			SwitchKeyEnum keyEnum = getSettingType(lineElement[1]);
			keyBoard.keys[index] = new SettingKey(keyEnum);
		}
		// load layout
		rawLine = skipComment(keyboardFile);
		num = Integer.valueOf(rawLine);
		for (int i = 0; i < num; i++) {
			rawLine = skipComment(keyboardFile);
			lineElement = rawLine.split(":");
			int startYIndex = Integer.valueOf(lineElement[0]);
			int startXIndex = Integer.valueOf(lineElement[1]);

			rawLine = skipComment(keyboardFile);
			lineElement = rawLine.split(" ");
			for (int j = 0; j < lineElement.length; j++) {
				int index = Integer.valueOf(lineElement[j]);
				keyBoard.keys[index].setIndex(startXIndex + j, startYIndex);
			}
		}
		return keyBoard;
	}

	private String skipComment(Scanner scanner) {
		String rawLine = null;
		while (scanner.hasNext()) {
			rawLine = scanner.nextLine();
			if (!(rawLine.length() == 0 || rawLine.startsWith("//")))
				break;
		}
		return rawLine;
	}

}
