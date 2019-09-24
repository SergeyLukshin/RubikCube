package ru.adoon.rubikcube;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Лукшин on 10.11.2017.
 */

public class MainData {
    public Context context;

    public Background mBackground = null;
    public SettingsSprite mSettingsSprite = null;
    public BackSprite mBackSprite = null;
    public LockSprite mLockSprite = null;
    public SoundSprite mSoundSprite = null;
    public Clock mClock = null;
    public Moves mMoves = null;
    public Menu mMenu = null;
    public Figure mFigure = null;
    public int mRotateState = Structures.ROTATE_ONLY_AXES;
    public int mRotateBlockType = Structures.ROTATE_BLOCK_NONE;
    public int mLanguage = 0;
    public boolean mDoubleTap = false;
    public int mVisibleSides = 1;
    public int mCntSteps = 0;
    //public boolean mRotateType = false;

    public SoundPool mSoundPool;
    public MediaPlayer mediaPlayer;
    public AssetManager mAssetManager;

    public int mClickSound;

    MainData(Context ctx) {

        context = ctx;

        mSettingsSprite = new SettingsSprite(ctx);
        mLockSprite = new LockSprite(ctx);
        mSoundSprite = new SoundSprite(ctx);
        mBackSprite = new BackSprite(ctx);
        mBackground = new Background(ctx, false);
        mClock = new Clock(ctx);
        mMoves = new Moves(ctx);
        mFigure = new Figure(ctx);
        //mFigure.FigureInit();
        LoadData();

        //if (mRotateState == Structures.ROTATE_ONLY_AXES)
        //    Camera.mFirstStart = true;

        //mVisibleSides = 2;

        mMenu = new Menu(ctx, mLanguage);
    }

    public void SetContext(Context ctx) {
        context = ctx;

        mFigure.SetContext(ctx);
        mSettingsSprite.SetContext(ctx);
        mLockSprite.SetContext(ctx);
        mSoundSprite.SetContext(ctx);
        mBackSprite.SetContext(ctx);
        mBackground.SetContext(ctx);
        mClock.SetContext(ctx);
        mMoves.SetContext(ctx);
        mMenu.SetContext(ctx);
    }

    public int loadSound(String fileName) {
        AssetFileDescriptor afd;
        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Не могу загрузить файл " + fileName,
                    Toast.LENGTH_SHORT).show();
            return -1;
        }
        return mSoundPool.load(afd, 1);
    }

    public void playWin() {
        mediaPlayer.stop();
        if (mSoundSprite.GetType() != 0) {
            mediaPlayer = MediaPlayer.create(context, R.raw.win);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(0.5f, 0.5f);
            mediaPlayer.start();
        }
    }

    public void Vibrate(int duration) {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            //deprecated in API 26
            v.vibrate(duration);
        }
    }

    public void playSound(int sound, float volume, int loop, int priority) {
        if (sound > 0 && mSoundSprite.GetType() != 0) {
            mSoundPool.play(sound, volume, volume, priority + 1, loop, 1);
            //Vibrate(30);
        }
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
            ed.putFloat("Camera_Rotation_Pos_" + String.valueOf(i), Camera.mAccumulatedRotation[i]);
        }

        ed.putFloat("Camera_Scale", Camera.mScale);
        ed.putInt("Camera_Direction", Camera.GetDirection());
        ed.putFloat("Camera_Speed", Camera.mSpeed);

        if (mClock != null) {
            ed.putLong("Clock_TimeStart", mClock.mTimeStart);
            ed.putLong("Clock_Duration", mClock.mDuration);
            ed.putBoolean("Clock_Pause", mClock.mPause);
        }

        if (mLockSprite != null) {
            ed.putLong("Lock_Type", mLockSprite.GetType());
        }

        if (mSoundSprite != null) {
            ed.putLong("Sound", mSoundSprite.GetType());
        }

        if (mFigure != null) {
            ed.putInt("Figure_Type_", mFigure.mFigureType);
            ed.putInt("Figure_SubType", mFigure.mFigureSubType);
        }

        ed.putInt("Rotate_BlockType", mRotateBlockType);
        ed.putInt("Rotate_State", mRotateState);
        ed.putInt("Language", mLanguage);
        ed.putBoolean("Double_Tap", mDoubleTap);
        ed.putInt("Visible_Sides", mVisibleSides);
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

        if (mFigure != null) {
            mFigure.SetFigure(sPref.getInt("Figure_Type_", Structures.CUBE), sPref.getInt("Figure_SubType", 1));
        }

        Camera.mFirstStart = true;
        for (int i = 0; i < Camera.mAccumulatedRotation.length; i++) {
            Camera.mAccumulatedRotation[i] = sPref.getFloat("Camera_Rotation_Pos_" + String.valueOf(i), -1);
            if (Camera.mAccumulatedRotation[i] != -1) {
                Camera.mScaling = true;
                Camera.mFirstStart = false;
            }
        }
        Camera.mScale  = sPref.getFloat("Camera_Scale", 1);
        Camera.SetDirection(sPref.getInt("Camera_Direction", Structures.CAMERA_NONE));
        Camera.mSpeed = sPref.getFloat("Camera_Speed", 1);

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

        if (mSoundSprite != null) {
            mSoundSprite.SetType((int)sPref.getLong("Sound", 1));
        }

        boolean isEnabled =  sPref.getBoolean("Menu_IsEnable", false);

        mRotateBlockType = sPref.getInt("Rotate_BlockType", Structures.ROTATE_BLOCK_ALL_FIGURE);
        mRotateState = sPref.getInt("Rotate_State", Structures.ROTATE_ALL_DIRECTION);
        mLanguage = sPref.getInt("Language", 0);
        mDoubleTap = sPref.getBoolean("Double_Tap", false);
        mVisibleSides = sPref.getInt("Visible_Sides", 1);
        mCntSteps = sPref.getInt("Cnt_Steps", 0);

        if (mMoves != null)
            mMoves.SetVal(mCntSteps);

        if (mClock != null && mMenu != null) {
            if (isEnabled && !mMenu.MenuIsEnable()) {
                mMenu.MenuInit();
                mMenu.MenuShow(Menu.menu_do_resume);
                if (!mClock.IsEnable()) {
                    //mMenu.SetTexture(Menu.menu_do_main_timer, 0);
                    mMenu.SetTexture(Menu.menu_do_resume_timer, 0);
                }
                else {
                    //mMenu.SetTexture(Menu.menu_do_main_timer, 1);
                    mMenu.SetTexture(Menu.menu_do_resume_timer, 1);
                }
            }
            if (!isEnabled && mMenu.MenuIsEnable()) mMenu.MenuClose();

            if (mClock.IsEnable() && !mMenu.MenuIsEnable()) {
                //mClock.Pause();
                mMenu.MenuInit();
                mMenu.MenuShow(Menu.menu_do_resume);

                if (!mClock.IsEnable()) {
                    //mMenu.SetTexture(Menu.menu_do_main_timer, 0);
                    mMenu.SetTexture(Menu.menu_do_resume_timer, 0);
                }
                else {
                    //mMenu.SetTexture(Menu.menu_do_main_timer, 1);
                    mMenu.SetTexture(Menu.menu_do_resume_timer, 1);
                }
            }
        }

        if (mFigure != null)
            mFigure.LoadPos(sPref);
    }
}
