package ru.adoon.rubikcube;

import android.content.Context;
import android.os.SystemClock;

import java.util.ArrayList;

/**
 * Created by Лукшин on 10.11.2017.
 */

public class Moves {
    private int texture;

    private float mParentHeight;
    private float mParentWidth;

    private float mRatioX;
    private float mRatioY;

    private int mVal = 0;
    //private boolean mEnabled = true;
    ArrayList<DigitSprite> mDigits = new ArrayList<DigitSprite>();

    Moves(Context context) {

        mVal = 0;
        texture = TextureUtils.loadTexture(context, R.drawable.digits2);
    }

    public void SetContext(Context context) {
        TextureUtils.deleteTexture(texture);
        texture = TextureUtils.loadTexture(context, R.drawable.digits2);
    }

    public void SetRatio(float parentWidth, float parentHeight, float ratioX, float ratioY) {

        mParentHeight = parentHeight;
        mParentWidth = parentWidth;
        mRatioX = ratioX;
        mRatioY = ratioY;

        for (int i = 0; i < mDigits.size(); i++) {
            mDigits.get(i).SetMarginTop(0.03f + mDigits.get(i).mHeightGL / mRatioY);
            mDigits.get(i).SetRatio(mRatioX, mRatioY);
        }
    }

    /*public synchronized boolean IsEnable() {
        return mEnabled;
    }

    public void setEnable(boolean bEnable) {
        mEnabled = bEnable;
    }*/

    public void SetVal(int val) {
        mVal = val;
        mDigits.clear();
        String str = String.valueOf(val);
        for (int i = 0; i < str.length(); i++) {
            DigitSprite ds = new DigitSprite();
            if (mRatioY > 0)
                ds.SetMarginTop(0.03f + ds.mHeightGL / mRatioY);
            else
                ds.SetMarginTop(0.03f);
            ds.SetAlign(Structures.ALIGN_CENTER);
            ds.SetZ(-0.8f);
            ds.SetDigitsLength(str.length());
            ds.SetDigitsCnt(11);
            ds.SetRatio(mRatioX, mRatioY);
            ds.SetDigit(i, Integer.valueOf(String.valueOf(str.charAt(i))));
            mDigits.add(ds);
        }

        /*for (int i = 0; i < 11; i++) {
            DigitSprite ds = new DigitSprite();
            ds.SetMarginTop(0.03f);
            ds.SetAlign(Structures.ALIGN_CENTER);
            ds.SetZ(-0.8f);
            ds.SetDigitsCnt(11);
            ds.SetRatio(mRatioX, mRatioY);
            mDigits.add(ds);
        }

        mDigits.get(2).SetDigit(2, 10);
        mDigits.get(5).SetDigit(5, 10);
        mDigits.get(8).SetDigit(8, 10);
        mDigits.get(9).SetDigit(9, 0);
        mDigits.get(10).SetDigit(10, 1);
        mDigits.get(6).SetDigit(6, 2);
        mDigits.get(7).SetDigit(7, 3);
        mDigits.get(3).SetDigit(3, 4);
        mDigits.get(4).SetDigit(4, 5);
        mDigits.get(0).SetDigit(0, 6);
        mDigits.get(1).SetDigit(1, 7);*/
    }

    public void Draw(boolean bMenuIsEnable, boolean bClockEnable) {
        //if (!mEnabled) return;
        if (!bMenuIsEnable && bClockEnable) {
            for (int i = 0; i < mDigits.size(); i++) {
                mDigits.get(i).Draw(texture);
            }
        }
    }
}
