package com.aeviou.keyboard;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;
import android.view.inputmethod.EditorInfo;


import com.aeviou.R ;
public class AeviouKeyboard extends Keyboard {
    private Key mEnterKey;
    public AeviouKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    public AeviouKeyboard(Context context, int layoutTemplateResId, 
            CharSequence characters, int columns, int horizontalPadding) {
        super(context, layoutTemplateResId, characters, columns, horizontalPadding);
    }
    
    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y, 
            XmlResourceParser parser) {
        Key key = new Key(res, parent, x, y, parser);
        if (key.codes[0] == 10) {
            mEnterKey = key;
        }
        return key;
    }
    @Override
    protected Row createRowFromXml(Resources res, XmlResourceParser parser) {
    	Row temp=new Row(res, this, parser);
        return temp ;
    }
    /**
     * 根据输入状态，设置enter按键的显示内容。
     */
    public void setImeOptions(Resources res, int options) {
        if (mEnterKey == null) {
            return;
        }
        switch (options&(EditorInfo.IME_MASK_ACTION|EditorInfo.IME_FLAG_NO_ENTER_ACTION)) {
            case EditorInfo.IME_ACTION_GO:
                mEnterKey.iconPreview = null;
                mEnterKey.icon = null;
                mEnterKey.label = res.getText(R.string.label_go_key);
                break;
            case EditorInfo.IME_ACTION_NEXT:
                mEnterKey.iconPreview = null;
                mEnterKey.icon = null;
                mEnterKey.label = res.getText(R.string.label_next_key);
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                mEnterKey.icon = res.getDrawable(
                        R.drawable.sym_keyboard_search);
                mEnterKey.label = null;
                break;
            case EditorInfo.IME_ACTION_SEND:
                mEnterKey.iconPreview = null;
                mEnterKey.icon = null;
                mEnterKey.label = res.getText(R.string.label_send_key);
                break;
            default:
                mEnterKey.icon = res.getDrawable(
                        R.drawable.sym_keyboard_return);
                mEnterKey.label = null;
                break;
        }
    }

}
