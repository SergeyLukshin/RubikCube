package ru.adoon.rubikcube;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Лукшин on 10.11.2017.
 */

public class MainData {
    private Context context;

    public Background mBackground = null;
    public SettingsSprite mSettingsSprite = null;
    public BackSprite mBackSprite = null;
    public LockSprite mLockSprite = null;
    public Clock mClock = null;
    public Menu mMenu = null;
    public Figure mFigure = null;
    public int mRotateType = 0;
    public boolean mDoubleTap = false;
    public int mCntSteps = 0;

    MainData(Context ctx) {

        context = ctx;

        mSettingsSprite = new SettingsSprite(ctx);
        mLockSprite = new LockSprite(ctx);
        mBackSprite = new BackSprite(ctx);
        mBackground = new Background(ctx, false);
        mClock = new Clock(ctx);
        mMenu = new Menu(ctx);
        mFigure = new Figure(ctx);
        //mFigure.FigureInit();
        LoadData();
    }

    public void SetContext(Context ctx) {
        context = ctx;

        mFigure.SetContext(ctx);
        mSettingsSprite.SetContext(ctx);
        mLockSprite.SetContext(ctx);
        mBackSprite.SetContext(ctx);
        mBackground.SetContext(ctx);
        mClock.SetContext(ctx);
        mMenu.SetContext(ctx);
    }

    public void SaveData() {
        SharedPreferences sPref = context.getSharedPreferences("RubiksCube", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();


        /*ed.putFloat("Camera_LightX", Structures.Light[0]);
        ed.putFloat("Camera_LightY", Structures.Light[1]);
        ed.putFloat("Camera_LightZ", Structures.Light[2]);

        ed.putFloat("Camera_CameraEyeX", Structures.CameraEye[0]);
        ed.putFloat("Camera_CameraEyeY", Structures.CameraEye[1]);
        ed.putFloat("Camera_CameraEyeZ", Structures.CameraEye[2]);

        ed.putFloat("Camera_CameraUpX", Structures.CameraUp[0]);
        ed.putFloat("Camera_CameraUpY", Structures.CameraUp[1]);
        ed.putFloat("Camera_CameraUpZ", Structures.CameraUp[2]);*/

        for (int i = 0; i < Camera.mAccumulatedRotation.length; i++) {
            ed.putFloat("Camera_Rotation" + String.valueOf(i), Camera.mAccumulatedRotation[i]);
        }

        ed.putFloat("Camera_Scale", Camera.mScale);

        if (mClock != null) {
            ed.putLong("Clock_TimeStart", mClock.mTimeStart);
            ed.putLong("Clock_Duration", mClock.mDuration);
            ed.putBoolean("Clock_Pause", mClock.mPause);
        }

        if (mLockSprite != null) {
            ed.putLong("Lock_Type", mLockSprite.GetType());
        }

        if (mFigure != null) {
            ed.putInt("Figure_Type_", mFigure.mFigureType);
            ed.putInt("Figure_SubType", mFigure.mFigureSubType);
        }

        ed.putInt("Rotation_Type", mRotateType);
        ed.putBoolean("Double_Tap", mDoubleTap);
        ed.putInt("Cnt_Steps", mCntSteps);

        if (mMenu != null)
            ed.putBoolean("Menu_IsEnable", mMenu.MenuIsEnable());

        if (mFigure != null)
            mFigure.SavePos(ed);

        ed.commit();
    }

    public void ResumeClock() {
        if (mClock.mTimeStart > 0) mClock.mTimeStart = SystemClock.uptimeMillis() - mClock.mDuration;
    }


    public void LoadData() {
        SharedPreferences sPref = context.getSharedPreferences("RubiksCube", MODE_PRIVATE);

        /*Structures.Light[0] = sPref.getFloat("Camera_LightX", Structures.Light[0]);
        Structures.Light[1] = sPref.getFloat("Camera_LightY", Structures.Light[1]);
        Structures.Light[2] = sPref.getFloat("Camera_LightZ", Structures.Light[2]);

        Structures.CameraEye[0] = sPref.getFloat("Camera_CameraEyeX", Structures.CameraEye[0]);
        Structures.CameraEye[1] = sPref.getFloat("Camera_CameraEyeY", Structures.CameraEye[1]);
        Structures.CameraEye[2] = sPref.getFloat("Camera_CameraEyeZ", Structures.CameraEye[2]);

        Structures.CameraUp[0] = sPref.getFloat("Camera_CameraUpX", Structures.CameraUp[0]);
        Structures.CameraUp[1] = sPref.getFloat("Camera_CameraUpY", Structures.CameraUp[1]);
        Structures.CameraUp[2] = sPref.getFloat("Camera_CameraUpZ", Structures.CameraUp[2]);*/

        Camera.mFirstStart = true;
        for (int i = 0; i < Camera.mAccumulatedRotation.length; i++) {
            Camera.mAccumulatedRotation[i] = sPref.getFloat("Camera_Rotation" + String.valueOf(i), -1);
            if (Camera.mAccumulatedRotation[i] != -1) {
                Camera.mScaling = true;
                Camera.mFirstStart = false;
            }
        }
        Camera.mScale  = sPref.getFloat("Camera_Scale", 1);

        if (mClock != null) {
            mClock.mTimeStart = sPref.getLong("Clock_TimeStart", -1);
            mClock.mDuration = sPref.getLong("Clock_Duration", 0);
            mClock.mPause = sPref.getBoolean("Clock_Pause", false);
            if (mClock.mTimeStart > 0)
                mClock.mTimeStart = SystemClock.uptimeMillis() - mClock.mDuration;
        }

        if (mLockSprite != null) {
            mLockSprite.SetType((int)sPref.getLong("Lock_Type", 1));
        }

        boolean isEnabled =  sPref.getBoolean("Menu_IsEnable", false);

        if (mFigure != null) {
            mFigure.SetFigure(sPref.getInt("Figure_Type_", Structures.CUBE), sPref.getInt("Figure_SubType", 1));
        }

        mRotateType = sPref.getInt("Rotation_Type", 0);
        mDoubleTap = sPref.getBoolean("Double_Tap", false);
        mCntSteps = sPref.getInt("Cnt_Steps", 0);

        if (mClock != null && mMenu != null) {
            if (isEnabled && !mMenu.MenuIsEnable()) {
                mMenu.MenuInit();
                mMenu.MenuShow(Menu.menu_do_main_no_exit);
                if (!mClock.IsEnable())
                    mMenu.SetTexture(Menu.menu_do_main_timer, 0);
                else
                    mMenu.SetTexture(Menu.menu_do_main_timer, 1);
            }
            if (!isEnabled && mMenu.MenuIsEnable()) mMenu.MenuClose();

            if (mClock.IsEnable() && !mMenu.MenuIsEnable()) {
                //mClock.Pause();
                mMenu.MenuInit();
                mMenu.MenuShow(Menu.menu_do_main_no_exit);

                if (!mClock.IsEnable())
                    mMenu.SetTexture(Menu.menu_do_main_timer, 0);
                else
                    mMenu.SetTexture(Menu.menu_do_main_timer, 1);
            }
        }

        if (mFigure != null)
            mFigure.LoadPos(sPref);
    }
}
