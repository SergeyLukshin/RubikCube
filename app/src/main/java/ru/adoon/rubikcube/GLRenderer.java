package ru.adoon.rubikcube;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glViewport;

public class GLRenderer implements Renderer {

    //private final static long TIME = 10000L;

    public Context context;
    //public boolean mTimer = false;
    public int m_width = 0;
    public int m_height = 0;
    public float m_ratio = 1;

    private int programIdCube;
    public int programIdLine;
    private int programIdSprite;

    private volatile boolean mPause = false;

    /*public ArrayList<ArrayList<ArrayList<CubeItemOld>>> mItems = null;//new ArrayList<ArrayList<ArrayList<CubeItemOld>>>();
    Background mBackground = null;
    SettingsSprite mSettingsSprite = null;
    BackSprite mBackSprite = null;
    Clock mClock = null;
    Menu mMenu = null;
    boolean mNeedRefreshTextures = false;*/
    static volatile MainData data = null;

    //Line mLine = null;
    //CubeItemOld mPoint = null;
    //CubeItemOld LiteCube;


    public void setContext(Context context) {
        this.context = context;
        //mNeedRefreshTextures = true;
    }

    public GLRenderer(Context context) {
        this.context = context;
        //data = new MainData(context);
    }

    volatile ActionList mActions = new ActionList();
    static ArrayList<Action> mHistory = new ArrayList<Action>();

    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
        glClearColor(0f, 0f, 0f, 1f);
        glEnable(GL_DEPTH_TEST);

        createAndUseProgram();
        prepareData();

        /*if (mNeedRefreshTextures) {
            mBackground.setContext(context);//.setContext(context);
            mClock.setContext(context);
            mMenu.setContext(context);
            mNeedRefreshTextures = false;
        }*/
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        glViewport(0, 0, width, height);
        createProjectionMatrix(width, height);
        m_width = width;
        m_height = height;

        //data = new MainData(context);
        data.SetContext(context);

        if (m_width > m_height) {
            data.mSettingsSprite.SetRatio(m_width, m_height, m_ratio, 1);
            data.mBackSprite.SetRatio(m_width, m_height, m_ratio, 1);
            data.mMenu.SetRatio(m_width, m_height, m_ratio, 1);
            data.mClock.SetRatio(m_width, m_height, m_ratio, 1);
            data.mLockSprite.SetRatio(m_width, m_height, m_ratio, 1);
        } else {
            data.mSettingsSprite.SetRatio(m_width, m_height, 1, m_ratio);
            data.mBackSprite.SetRatio(m_width, m_height, 1, m_ratio);
            data.mMenu.SetRatio(m_width, m_height, 1, m_ratio);
            data.mClock.SetRatio(m_width, m_height, 1, m_ratio);
            data.mLockSprite.SetRatio(m_width, m_height, 1, m_ratio);
        }
    }

    public void AddHistory (Action ah) {
        mHistory.add(ah);
        if (data != null)
            data.mCntSteps ++;
    }

    public void RemoveLastHistory () {
        if (mHistory.size() > 0) {
            mHistory.remove(mHistory.size() - 1);
            if (data != null)
                data.mCntSteps--;
        }
    }

    public void ClearHistory() {
        mHistory.clear();
        if (data != null)
            data.mCntSteps = 0;
    }

    public void ActionFigureMix(int cnt) {

        mPause = true;

        ClearHistory();
        //data.mCube.CubeInit();

        Random r = new Random();
        for (int i = 0; i < cnt; i++) {
            Action a = new Action(false);
            a.mAngleDif = data.mFigure.GetLimitAngle();
            a.m_ActionAxisRotate = r.nextInt(data.mFigure.GetMaxAxisCnt());
            a.m_ActionPosRotate = data.mFigure.GetRndPosRotate(r, a.m_ActionAxisRotate);//data.mFigure.mType == 0 ? r.nextInt(data.mDim) : r.nextInt(3);
            a.m_ActionDirectRotate = r.nextInt(2);
            if (a.m_ActionDirectRotate == Structures.DIRECT_NONE) a.m_ActionDirectRotate = Structures.DIRECT_LEFT;
            mActions.AddNoStart(a);
        }
        mActions.ActionStart();

        if (data.mClock.IsEnable()) {
            data.mClock.Reset();
        }

        mPause = false;
    }

    private void prepareData() {

        // здесь создаем объекты

        /*if (mBackground == null) mBackground = new Background(context, false);
        if (mClock == null) mClock = new Clock(context);
        if (mMenu == null) mMenu = new Menu(context);*/
        //mLine = new Line(Structures.CameraEyeInit, Structures.PointView);
        /*float[] pos = new float[4];
        pos[0] = 0;
        pos[1] = 0;
        pos[2] = 0;
        mPoint = new CubeItemOld(pos, 0.1f);*/

        if (data == null) data = new MainData(context);
        //glBlendFunc(GL_SRC_ALPHA,GL_ONE);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        //LiteCube = new CubeItemOld(programId, Structures.Light);
    }

    private void createAndUseProgram() {
        int vertexShaderIdCube = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader_cube);
        int fragmentShaderIdCube = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader_cube);
        programIdCube = ShaderUtils.createProgram(vertexShaderIdCube, fragmentShaderIdCube);
        FigureItemLocation.GetLocations(programIdCube);

        int vertexShaderIdSprite = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader_sprite);
        int fragmentShaderIdSprite = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader_sprite);
        programIdSprite = ShaderUtils.createProgram(vertexShaderIdSprite, fragmentShaderIdSprite);
        SpriteItemLocation.GetLocations(programIdSprite);

        int vertexShaderIdLine = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader_line);
        int fragmentShaderIdLine = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader_line);
        programIdLine = ShaderUtils.createProgram(vertexShaderIdLine, fragmentShaderIdLine);
        LineItemLocation.GetLocations(programIdLine);
    }

    private void createProjectionMatrix(int width, int height) {
        m_ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 2;
        float far = 100000;
        if (width > height) {
            m_ratio = (float) width / height;
            left *= m_ratio;
            right *= m_ratio;
            Camera.setPerspectiveM(30 / m_ratio, m_ratio, near, far);
        } else {
            m_ratio = (float) height / width;
            bottom *= m_ratio;
            top *= m_ratio;
            Camera.setPerspectiveM(30, 1 / m_ratio, near, far);
        }

        //Camera.setfrustumMProj(left, right, bottom, top, near, far);
        if (Camera.mFirstStart) {
            Camera.mDeltaX = 45;
            Camera.setLookAtM();
            Camera.mDeltaY = 20;
            Camera.setLookAtM();
        }
        else
            Camera.setLookAtM();
    }

    @Override
    public void onDrawFrame(GL10 arg0) {

        if (mPause) return;

        if (data == null) return;
        if (data.mBackground == null) return;
        if (data.mClock == null) return;
        if (data.mMenu == null) return;
        if (data.mBackSprite == null) return;
        if (data.mLockSprite == null) return;

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        if (mActions.ActionIsEnable()) mActions.ActionExec();

        glUseProgram(programIdSprite);
        data.mBackground.Draw();
        data.mClock.Draw(data.mMenu.MenuIsEnable());
        if (!data.mMenu.MenuIsEnable()) {
            data.mSettingsSprite.Draw();
            if (mHistory.size() > 0)
                data.mBackSprite.Draw();
            data.mLockSprite.Draw();
        }

        Camera.setLookAtM();

        glDisable(GL_BLEND);
        glUseProgram(programIdCube);
        if (mActions.list != null && mActions.list.size() > 0) {
            Action a_ = mActions.list.get(0);
            if (a_ != null) {
                Action a = a_.clone();
                data.mFigure.Draw(a);
            }
        }
        else
            data.mFigure.Draw(null);
        glEnable(GL_BLEND);

        if (data.mMenu.MenuIsEnable()) {
            glUseProgram(programIdSprite);
            data.mMenu.Draw();
        }

        Action a = mActions.CheckActionStop(data.mFigure.GetLimitAngle());
        if (a != null) {
            boolean res = data.mFigure.StoreItemPosition(a);
            if (mActions.list.size() == 0)
                data.SaveData();
            if (res) {
                if (data.mClock.IsEnable())
                    data.mMenu.MenuShow(Menu.menu_do_complete);
                else
                    data.mMenu.MenuShow(Menu.menu_do_complete_no_time);
                ClearHistory();
            }
        }
    }

    public boolean DoAction(int[] item, float[] start, float[] end) {

        if (data.mClock.IsEnable())
            data.mClock.Resume();

        // вращение по оси Y
        Action a = data.mFigure.GetAction(item, start, end);
        if (a != null) {
            mActions.Add(a);
            Action ah = a.clone();
            ah.setFromUser(false);
            AddHistory(ah);
            return true;
        }
        return false;
    }

    /*public void DoAction(int[] CubeItem1, int[] CubeItem2, Bool bNeedRecalc) {

        if (data.mClock.IsEnable())
            data.mClock.Resume();

        // вращение по оси Y
        Action a = data.mFigure.GetAction(CubeItem1, CubeItem2, bNeedRecalc);
        if (a != null) {
            mActions.Add(a);
            Action ah = a.clone();
            ah.setFromUser(false);
            mHistory.add(ah);
        }
    }*/
}
