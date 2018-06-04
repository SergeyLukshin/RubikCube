package ru.adoon.rubikcube;

import android.content.Context;
import android.os.SystemClock;

import java.util.ArrayList;

public class TimeInfo {
    private int texture;

    private float mParentHeight;
    private float mParentWidth;

    private float mRatioX;
    private float mRatioY;

    ArrayList<DigitSprite> mDigits = new ArrayList<DigitSprite>();

    public void SetTime(long Duration) {
        Duration = Duration / 1000;
        int Sec = (int)(Duration % 60);
        int Min = (int)((Duration / 60) % 60);
        int Hour = (int)((Duration / (60 * 60)) % 60);
        int Day = (int)((Duration / (60 * 60 * 24)));

        mDigits.get(9).SetDigit(9, Sec / 10);
        mDigits.get(10).SetDigit(10, Sec % 10);

        mDigits.get(6).SetDigit(6, Min / 10);
        mDigits.get(7).SetDigit(7, Min % 10);

        mDigits.get(3).SetDigit(3, Hour / 10);
        mDigits.get(4).SetDigit(4, Hour % 10);

        mDigits.get(0).SetDigit(0, Day / 10);
        mDigits.get(1).SetDigit(1, Day % 10);
    }

    TimeInfo(Context context) {

        texture = TextureUtils.loadTexture(context, R.drawable.digits2);

        for (int i = 0; i < 11; i++) {
            DigitSprite ds = new DigitSprite();
            ds.SetMarginTop(0);
            ds.SetMarginRight(0.1f);
            ds.SetAlign(Structures.ALIGN_RIGHT);
            ds.SetZ(-1f);
            ds.SetDigitsCnt(11);
            mDigits.add(ds);
        }

        mDigits.get(2).SetDigit(2, 10);
        mDigits.get(5).SetDigit(5, 10);
        mDigits.get(8).SetDigit(8, 10);
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
