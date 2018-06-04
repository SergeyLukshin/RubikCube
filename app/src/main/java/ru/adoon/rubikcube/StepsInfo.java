package ru.adoon.rubikcube;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Лукшин on 29.05.2018.
 */

public class StepsInfo {
    private int texture;

    private float mParentHeight;
    private float mParentWidth;

    private float mRatioX;
    private float mRatioY;

    ArrayList<DigitSprite> mDigits = new ArrayList<DigitSprite>();

    public void SetStepsCnt(long steps) {
        int index = 10;
        while (true)
        {
            int digit = (int)(steps % 10);
            steps = steps / 10;
            mDigits.get(index).SetDigit(index, digit);
            index --;

            if (index < 0) break;
            if (steps == 0) break;
        }

        for (int i = 0; i <= index; i++) {
            mDigits.get(i).SetDigit(i, -1);
        }
    }

    StepsInfo(Context context) {

        texture = TextureUtils.loadTexture(context, R.drawable.digits2);

        for (int i = 0; i < 11; i++) {
            DigitSprite ds = new DigitSprite();
            ds.SetMarginTop(0);
            ds.SetMarginRight(0.1f);
            ds.SetAlign(Structures.ALIGN_RIGHT);
            ds.SetZ(-1f);
            ds.SetDigitsCnt(11);
            ds.SetDigit(i, 0);
            mDigits.add(ds);
        }
    }

    public void SetY(float Y) {
        for (int i = 0; i < mDigits.size(); i++) {
            mDigits.get(i).SetY(Y);
        }
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
            mDigits.get(i).SetRatio(mRatioX, mRatioY);
        }
    }

    public void Draw(/*boolean bMenuIsEnable*/) {

        for (int i = 0; i < mDigits.size(); i++) {
            mDigits.get(i).Draw(texture);
        }
    }
}
