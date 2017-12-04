package ru.adoon.rubikcube;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

public class MainActivity extends AppCompatActivity {

    private Surface glSurfaceView = null;
    private GLRenderer render = null;
    //SaveFragment saveFragment;
    private Handler mHandler = new Handler();
    private Boolean RPause = false;
    private int FPS = 30;  // кадров в секунду
    public static Activity activity;

    /*private float mPreviousX;
    private float mPreviousY;

    private float mDensity;*/

    @Override
    public void onBackPressed() {
        if (render.data != null)
            render.data.mMenu.MenuShow(Menu.menu_do_exit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!supportES2()) {
            Toast.makeText(this, "OpenGl ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        activity = this;
        glSurfaceView = new Surface(this);
        glSurfaceView.setEGLContextClientVersion(2);
        render = new GLRenderer(this);
        /*saveFragment = (SaveFragment) getFragmentManager().findFragmentByTag("SAVE_FRAGMENT");

        if (saveFragment != null) {
            render = saveFragment.getModel();
            render.setContext(this);
        }
        else {
            render = new GLRenderer(this);
            saveFragment = new SaveFragment();
            getFragmentManager().beginTransaction()
                    .add(saveFragment, "SAVE_FRAGMENT")
                    .commit();*/
        //}

        glSurfaceView.setRenderer(render);
        setContentView(glSurfaceView);
        glSurfaceView.setGLWrapper(new GLSurfaceView.GLWrapper() {
            public GL wrap(GL gl) {
                return new MatrixTrackingGL(gl);
            }
        });

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        glSurfaceView.mDensity = displayMetrics.density;
        glSurfaceView.mRender = render;
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private boolean supportES2() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

    void reqRend(){
        mHandler.removeCallbacks(mDrawRa);
        if(!RPause){
            mHandler.postDelayed(mDrawRa, 1000 / FPS); // отложенный вызов mDrawRa
            glSurfaceView.requestRender();
        }
    }

    private final Runnable mDrawRa = new Runnable() {
        public void run() {
            reqRend();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        //saveFragment.setModel(render);
        //render.mNeedRefreshTextures = true;
        glSurfaceView.onPause();
        if (render.data != null)
            render.data.SaveData();
        RPause = true; // флаг паузы
    }
    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        RPause = false;  // флаг паузы
        if (render.data != null) {
            if (render.data.mClock.IsEnable()) {
                render.data.ResumeClock();
                render.data.mClock.Pause();
                render.data.mMenu.MenuShow(Menu.menu_do_main);
            }
        }
        reqRend(); // запускаем рендеринг
    }

    @Override
    protected void onStop(){
        super.onStop();
        //RPause = true;
        //saveFragment.setModel(render);
        //this.finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //if (render.data != null)
        //    render.data.SaveData();
        //RPause = true;
        //saveFragment.setModel(render);
        //this.finish();
    }
}
