package ru.adoon.rubikcube;

import android.content.Context;
import android.os.SystemClock;

import java.util.ArrayList;

/**
 * Created by Лукшин on 10.11.2017.
 */

public class Clock {
    private int texture;

    private float mParentHeight;
    private float mParentWidth;

    private float mRatioX;
    private float mRatioY;

    public volatile long mTimeStart = -1; // необходимо сохранять
    public volatile long mDuration = 0; // необходимо сохранять
    private int mSec = -1;
    private int mMin = -1;
    private int mHour = -1;
    private int mDay = -1;
    public volatile boolean mPause = false; // необходимо сохранять

    ArrayList<DigitSprite> mDigits = new ArrayList<DigitSprite>();

    /*public void Reset() {
        mTimeStart = -1;
        mDuration = 0;
        mSec = -1;
        mMin = -1;
        mHour = -1;
        mDay = -1;
        mPause = false;
    }*/

    public synchronized void Reset () {
        Stop();
        Start();
        Pause();
    }


    public synchronized void Stop() {
        mTimeStart = -1;
        mDuration = 0;
        mSec = -1;
        mMin = -1;
        mHour = -1;
        mDay = -1;
        mPause = false;
    }

    public synchronized void Start() {
        mTimeStart = SystemClock.uptimeMillis();
    }

    public synchronized void Pause() {
        mPause = true;
    }

    public synchronized void Resume() {
        mPause = false;
    }

    /*public void setContext(Context context) {
        TextureUtils.deleteTexture(texture);
        texture = TextureUtils.loadTexture(context, R.drawable.digits2);
    }*/

    Clock(Context context) {

        mTimeStart = SystemClock.uptimeMillis();
        texture = TextureUtils.loadTexture(context, R.drawable.digits2);

        for (int i = 0; i < 11; i++) {
            DigitSprite ds = new DigitSprite();
            ds.SetMarginTop(0.03f);
            ds.SetAlign(Structures.ALIGN_CENTER);
            ds.SetZ(-0.8f);
            ds.SetDigitsLength(11);
            ds.SetDigitsCnt(11);
            mDigits.add(ds);
        }

        mDigits.get(2).SetDigit(2, 10);
        mDigits.get(5).SetDigit(5, 10);
        mDigits.get(8).SetDigit(8, 10);
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

    public synchronized boolean IsEnable() {
        return mTimeStart >= 0;
    }

    public void Draw(boolean bMenuIsEnable) {
        if (mTimeStart < 0) return;

        if (mPause || bMenuIsEnable) {
            mTimeStart = SystemClock.uptimeMillis() - mDuration;
        }
        else {
            mDuration = SystemClock.uptimeMillis() - mTimeStart;
        }
        long Duration = mDuration / 1000;
        int Sec = (int)(Duration % 60);
        int Min = (int)((Duration / 60) % 60);
        int Hour = (int)((Duration / (60 * 60)) % 60);
        int Day = (int)((Duration / (60 * 60 * 24)));

        if (mSec != Sec) {
            mDigits.get(9).SetDigit(9, Sec / 10);
            mDigits.get(10).SetDigit(10, Sec % 10);
        }
        if (mMin != Min) {
            mDigits.get(6).SetDigit(6, Min / 10);
            mDigits.get(7).SetDigit(7, Min % 10);
        }
        if (mHour != Hour) {
            mDigits.get(3).SetDigit(3, Hour / 10);
            mDigits.get(4).SetDigit(4, Hour % 10);
        }
        if (mDay != Day) {
            mDigits.get(0).SetDigit(0, Day / 10);
            mDigits.get(1).SetDigit(1, Day % 10);
        }

        mSec = Sec;
        mMin = Min;
        mHour = Hour;
        mDay = Day;

        if (!bMenuIsEnable) {
            for (int i = 0; i < mDigits.size(); i++) {
                mDigits.get(i).Draw(texture);
            }
        }
    }
}
