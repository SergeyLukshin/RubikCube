package ru.adoon.rubikcube;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class Surface extends GLSurfaceView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener
{
    private float mPreviousX;
    private float mPreviousY;
    public float mDensity;
    public GLRenderer mRender = null;
    //private MatrixGrabber mg = new MatrixGrabber();

    float mDeltaX = 0;
    float mDeltaY = 0;
    float mDeltaZ = 0;
    float mInitDistance = -1;
    float[] mPoint1 = new float[2];
    float[] mPoint2 = new float[2];
    int[] mSelectItem;
    float[] mTap = new float[2];

    private GestureDetector gesture;

    int mAction = Structures.ACTION_NONE;
    Context ctx;

    //public int nScreenWidth;
    //public int nScreenHeight;

    public Surface(Context context)
    {
        super(context);
        ctx = context;
        gesture = new GestureDetector(context, this);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        setSystemUiVisibility(getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LOW_PROFILE);

        // always return true to allow gestures to register
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        // this is handled already in onDown
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distX, float distY) {
        // ignore scrolling started over a button
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        // handle in onSingleTapConfirmed
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
        // ignore flings
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    public static float Dot(float[] p1, float[] p2) {
        return p1[0] * p2[0] + p1[1] * p2[1] + p1[2] * p2[2];
    }

    public static float Dot2D(float[] p1, float[] p2) {
        return p1[0] * p2[0] + p1[1] * p2[1];
    }

    public static float[] Sub(float[] p1, float[] p2) {
        float[] res = new float[3];
        res[0] = p1[0] - p2[0];
        res[1] = p1[1] - p2[1];
        res[2] = p1[2] - p2[2];
        return res;
    }

    public static float[] Sub2D(float[] p1, float[] p2) {
        float[] res = new float[2];
        res[0] = p1[0] - p2[0];
        res[1] = p1[1] - p2[1];
        return res;
    }

    public static float[] DivConst(float[] p, float digit) {
        float[] res = new float[3];
        res[0] = p[0] / digit;
        res[1] = p[1] / digit;
        res[2] = p[2] / digit;
        return res;
    }

    public static float[] Add(float[] p1, float[] p2) {
        float[] res = new float[3];
        res[0] = p1[0] + p2[0];
        res[1] = p1[1] + p2[1];
        res[2] = p1[2] + p2[2];
        return res;
    }

    public static float[] Mul(float[] p, float p2) {
        float[] res = new float[3];
        res[0] = p[0] * p2;
        res[1] = p[1] * p2;
        res[2] = p[2] * p2;
        return res;
    }

    public static float Dist(float[] p1, float[] p2) {
        return (float)Math.sqrt((p2[0] - p1[0]) * (p2[0] - p1[0]) + (p2[1] - p1[1]) * (p2[1] - p1[1]) + (p2[2] - p1[2]) * (p2[2] - p1[2]));
    }

    public static float[] Equal(float[] p) {
        float[] res = {0, 0, 0};
        for (int i = 0; i < res.length; i++)
        {
            res[i] = p[i];
        }
        return res;
    }

    float Dist2D(float[] p1, float[] p2) {
        if (p1 == null)
            return (float)Math.sqrt(p2[0] * p2[0] + p2[1] * p2[1]);
        else
            return (float)Math.sqrt((p2[0] - p1[0]) * (p2[0] - p1[0]) + (p2[1] - p1[1]) * (p2[1] - p1[1]));
    }

    public static int ClassifyPointLine(float[] s, float[] e, float[] p) {
        if (((p[0] - s[0]) * (e[1] - s[1]) - (p[1] - s[1]) * (e[0] - s[0])) >= 0.0)
            return 0;
        else
            return 1;
    }

    public static boolean InPolygon( float[] p1, float[] p2, float[] p3, float[] n, float[] p) {

        float[] v1 = new float[2];
        float[] v2 = new float[2];
        float[] v3 = new float[2];

        float[] pp = new float[2];

        int dir;

        if (Math.abs(n[2]) >= Math.abs(n[0]) && Math.abs(n[2]) >= Math.abs(n[1])) {
            v1[0] = p1[0];
            v1[1] = p1[1];
            v2[0] = p2[0];
            v2[1] = p2[1];
            v3[0] = p3[0];
            v3[1] = p3[1];

            pp[0] = p[0];
            pp[1] = p[1];

            if (n[2] < 0.0)
                dir = 0;
            else
                dir = 1;
        } else {
            if (Math.abs(n[0]) >= Math.abs(n[1]) && Math.abs(n[0]) >= Math.abs(n[2])) {
                // Oyz
                v1[0] = p1[1];
                v1[1] = p1[2];
                v2[0] = p2[1];
                v2[1] = p2[2];
                v3[0] = p3[1];
                v3[1] = p3[2];

                pp[0] = p[1];
                pp[1] = p[2];

                if (n[0] < 0.0)
                    dir = 0;
                else
                    dir = 1;
            } else {
                // Oxz
                v1[0] = p1[0];
                v1[1] = p1[2];
                v2[0] = p2[0];
                v2[1] = p2[2];
                v3[0] = p3[0];
                v3[1] = p3[2];

                pp[0] = p[0];
                pp[1] = p[2];

                if (n[1] < 0.0)
                    dir = 1;
                else
                    dir = 0;
            }
        }

        int dir_ = ClassifyPointLine( v1, v2, pp );
        if (dir_ != dir) return false;
        dir_ = ClassifyPointLine( v2, v3, pp );
        if (dir_ != dir) return false;
        dir_ = ClassifyPointLine( v3, v1, pp );
        if (dir_ != dir) return false;

        return true;
    }

    private static float[] Normalize2D(float[] vect) {
        float len = (float)Math.sqrt(vect[0] * vect[0] + vect[1] * vect[1]);
        vect[0] /= len;
        vect[1] /= len;

        return vect;
    }

    private static float[] Normalize(float[] vect) {
        float len = (float)Math.sqrt(vect[0] * vect[0] + vect[1] * vect[1] + vect[2] * vect[2]);
        vect[0] /= len;
        vect[1] /= len;
        vect[2] /= len;

        return vect;
    }

    private float[] getViewRay(float[] tap)
    {
        // view port
        int[] viewport = { 0, 0, mRender.m_width, mRender.m_height };

        // far eye point
        float[] eye = new float[4];
        GLU.gluUnProject(tap[0], mRender.m_height - tap[1], 0.9f, Camera.getViewMatrix(), 0, Camera.getProjMatrix(), 0, viewport, 0, eye, 0);

        // fix
        if (eye[3] != 0)
        {
            eye[0] = eye[0] / eye[3];
            eye[1] = eye[1] / eye[3];
            eye[2] = eye[2] / eye[3];
        }

        // ray vector
        float[] ray = { eye[0] /*- Structures.CameraEye[0]*/, eye[1] /*- Structures.CameraEye[1]*/, eye[2] /*- Structures.CameraEye[2]*/, 0.0f };

        ray[0] = ray[0] + 100 * (ray[0] - Structures.CameraEye[0]);
        ray[1] = ray[1] + 100 * (ray[1] - Structures.CameraEye[1]);
        ray[2] = ray[2] + 100 * (ray[2] - Structures.CameraEye[2]);

        /*Normalize(ray);
        ray[0] *= 100;
        ray[1] *= 100;
        ray[2] *= 100;*/

        return ray;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        // button click
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        // ignore intermediate events triggered in a double tap
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        if (mRender == null) return true;
        if (mRender.data == null) return true;

        if (mRender.data.mDoubleTap && mRender.data.mRotateBlockType != Structures.ROTATE_BLOCK_NONE)
            mRender.data.mLockSprite.SetNextType();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean res = gesture.onTouchEvent(event);

        if (mRender == null) return true;
        if (mRender.mActions == null) return true;
        if (mRender.data == null) return true;

        if (mAction == Structures.ACTION_AUTO_MOVE) {

            if (Camera.getStop()) {
                mAction = Structures.ACTION_NONE;
            }
            else {

                mPreviousX = -1;
                mPreviousY = -1;

                return true;
            }
        }

        if (mRender.mActions.ActionIsEnable()) {
            mAction = Structures.ACTION_NONE;

            mDeltaX = 0;
            mDeltaY = 0;
            mDeltaZ = 0;

            Camera.mDeltaXInertia = 0;
            Camera.mDeltaYInertia = 0;
            Camera.mDeltaZInertia = 0;

            mPreviousX = -1;
            mPreviousY = -1;

            return true;
        }

        int actionMask = event.getActionMasked();
        // индекс касания
        int pointerIndex = event.getActionIndex();
        // число касаний
        int pointerCount = event.getPointerCount();

        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        if (pointerCount < 2) mInitDistance = -1;

        if (actionMask == MotionEvent.ACTION_DOWN || actionMask == MotionEvent.ACTION_POINTER_DOWN) {
            mDeltaX = 0;
            mDeltaY = 0;
            mDeltaZ = 0;

            Camera.mDeltaXInertia = 0;
            Camera.mDeltaYInertia = 0;
            Camera.mDeltaZInertia = 0;
        }

        if (actionMask == MotionEvent.ACTION_DOWN) {
            mPreviousX = x;
            mPreviousY = y;
        }

        if (actionMask == MotionEvent.ACTION_POINTER_UP) {
            for (int i = 0; i < pointerCount; i++) {
                if (i != pointerIndex) {
                    mPreviousX = event.getX(i);
                    mPreviousY = event.getY(i);
                    break;
                }
            }
        }

        if (actionMask == MotionEvent.ACTION_DOWN) {

            if (mRender.data.mMenu.MenuIsEnable()) {

                int index = mRender.data.mMenu.GetMenuPressed(x, y);

                mDeltaX = 0;
                mDeltaY = 0;
                mDeltaZ = 0;

                Camera.mDeltaXInertia = 0;
                Camera.mDeltaYInertia = 0;
                Camera.mDeltaZInertia = 0;

                mPreviousX = -1;
                mPreviousY = -1;

                if (index != Menu.menu_none && mRender.data.mMenu.GetActiveMenuIndex() >= 0 && !mRender.data.mMenu.menu.get(mRender.data.mMenu.GetActiveMenuIndex()).get(index).IsEnabled())
                    return true;

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_main) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_main_new_game:
                            //mRender.ActionCubeMix(30);
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_main_figure:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_figure);
                            return true;
                        case Menu.menu_do_main_timer:
                            if (!mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Start();
                                mRender.data.mClock.Pause();
                            } else {
                                mRender.data.mClock.Stop();
                            }
                            break;
                        case Menu.menu_do_main_rotate:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_rotate);

                            /*if (!mRender.data.mDoubleTap)
                                mRender.data.mMenu.SetTexture(Menu.menu_do_rotate_double_tap, 0);
                            else
                                mRender.data.mMenu.SetTexture(Menu.menu_do_rotate_double_tap, 1);*/

                            return true;
                        case Menu.menu_do_main_speed:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_camera_speed);
                            return true;
                        case Menu.menu_do_main_language:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_language);
                            return true;
                        case Menu.menu_none:
                            break;
                        case Menu.menu_do_main_exit:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_exit);
                            return true;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_resume) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_resume_new_game:
                            //mRender.ActionCubeMix(30);
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_resume_figure:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_figure);
                            return true;
                        case Menu.menu_do_resume_timer:
                            if (!mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Start();
                                mRender.data.mClock.Pause();
                            } else {
                                mRender.data.mClock.Stop();
                            }
                            break;
                        case Menu.menu_do_resume_rotate:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_rotate);

                            /*if (!mRender.data.mDoubleTap)
                                mRender.data.mMenu.SetTexture(Menu.menu_do_rotate_double_tap, 0);
                            else
                                mRender.data.mMenu.SetTexture(Menu.menu_do_rotate_double_tap, 1);*/

                            return true;
                        case Menu.menu_do_resume_speed:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_camera_speed);
                            return true;
                        case Menu.menu_none:
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_shuffle) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_shuffle_yes:
                            mRender.ActionFigureMix(30);
                            break;
                        case Menu.menu_do_shuffle_no:
                            mRender.data.mFigure.FigureInit();
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_exit) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_exit_yes:
                            MainActivity.activity.finish();
                            break;
                        case Menu.menu_do_exit_no:
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_complete) {

                    mRender.data.mMenu.MenuClose();

                    if (mRender.data.mClock.IsEnable()) {
                        mRender.data.mClock.Reset();
                    }
                    mRender.ClearHistory();
                    mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                    return true;

                    /*switch (index) {
                        case Menu.menu_new_game2:
                            //mRender.ActionCubeMix(30);
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                    }*/
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_figure) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_figure_cube:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_size_cube);
                            return true;
                        case Menu.menu_do_figure_pyramid:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_size_pyramid);
                            return true;
                        case Menu.menu_do_figure_domino_cube:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_size_dcube);
                            return true;
                        case Menu.menu_do_figure_empty_cube:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_size_empty_cube);
                            return true;
                        case Menu.menu_do_figure_floppy_cube:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_size_floppy_cube);
                            return true;
                        case Menu.menu_none:
                            break;
                    }
                            /*if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(0);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);*/
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_size_cube) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_size_cube_222:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.CUBE, 0);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_cube_333:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.CUBE, 1);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_cube_444:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.CUBE, 2);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_cube_555:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.CUBE, 3);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_none:
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_size_pyramid) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_size_pyramid_222:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.PYRAMID, 0);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_pyramid_333:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.PYRAMID, 1);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_pyramid_444:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.PYRAMID, 2);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_pyramid_555:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.PYRAMID, 3);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_none:
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_size_dcube) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_size_dcube_233:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.DOMINO_CUBE, 0);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_dcube_244:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.DOMINO_CUBE, 1);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_dcube_344:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.DOMINO_CUBE, 2);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_dcube_255:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.DOMINO_CUBE, 3);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_dcube_355:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.DOMINO_CUBE, 4);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_dcube_455:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.DOMINO_CUBE, 5);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_none:
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_size_empty_cube) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_size_empty_cube_333:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.EMPTY_CUBE, 0);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_empty_cube_555:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.EMPTY_CUBE, 1);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_none:
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_size_floppy_cube) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_size_floppy_cube_122:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.FLOPPY_CUBE, 0);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_floppy_cube_133:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.FLOPPY_CUBE, 1);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_floppy_cube_144:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.FLOPPY_CUBE, 2);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_do_size_floppy_cube_155:
                            if (mRender.data.mClock.IsEnable()) {
                                mRender.data.mClock.Reset();
                            }
                            mRender.data.mFigure.SetFigure(Structures.FLOPPY_CUBE, 3);
                            mRender.ClearHistory();
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                        case Menu.menu_none:
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_rotate) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_rotate_axes_type:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_rotate_axes);
                            return true;
                        case Menu.menu_do_rotate_any_type:
                            //mRender.data.mRotateBlockType = Structures.ROTATE_BLOCK_CAMERA_FIGURE;
                            mRender.data.mRotateState = Structures.ROTATE_ALL_DIRECTION;
                            Camera.InitCamera();
                            break;
                        case Menu.menu_do_rotate_block_set:
                            mRender.data.mMenu.MenuShow(Menu.menu_do_rotate_block);

                            if (!mRender.data.mDoubleTap)
                                mRender.data.mMenu.SetTexture(Menu.menu_do_rotate_block_double_tap, 0);
                            else
                                mRender.data.mMenu.SetTexture(Menu.menu_do_rotate_block_double_tap, 1);
                            return true;
                        case Menu.menu_none:
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_rotate_axes) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_rotate_axes_1_side:
                            mRender.data.mVisibleSides = 1;
                            mRender.data.mRotateState = Structures.ROTATE_ONLY_AXES;
                            Camera.InitCamera();
                            break;
                        case Menu.menu_do_rotate_axes_2_side:
                            mRender.data.mVisibleSides = 2;
                            mRender.data.mRotateState = Structures.ROTATE_ONLY_AXES;
                            Camera.InitCamera();
                            break;
                        case Menu.menu_do_rotate_axes_3_side:
                            mRender.data.mVisibleSides = 3;
                            mRender.data.mRotateState = Structures.ROTATE_ONLY_AXES;
                            Camera.InitCamera();
                            break;
                        case Menu.menu_none:
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_rotate_block) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_rotate_block_none:
                            mRender.data.mRotateBlockType = Structures.ROTATE_BLOCK_NONE;
                            break;
                        case Menu.menu_do_rotate_block_rotate1:
                            mRender.data.mRotateBlockType = Structures.ROTATE_BLOCK_ALL_FIGURE;
                            break;
                        case Menu.menu_do_rotate_block_rotate2:
                            mRender.data.mRotateBlockType = Structures.ROTATE_BLOCK_CAMERA_FIGURE;
                            break;
                        case Menu.menu_do_rotate_block_double_tap:
                            mRender.data.mDoubleTap = !mRender.data.mDoubleTap;
                            break;
                        case Menu.menu_none:
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_complete_no_time) {

                    mRender.data.mMenu.MenuClose();

                    mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                    return true;

                    /*switch (index) {
                        case Menu.menu_new_game3:
                            //mRender.ActionCubeMix(30);
                            mRender.data.mMenu.MenuShow(Menu.menu_do_shuffle);
                            return true;
                    }*/
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_language) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_russian:
                            mRender.data.mLanguage = 0;
                            mRender.data.mMenu.SetLanguage(mRender.data.mLanguage);
                            break;
                        case Menu.menu_do_english:
                            mRender.data.mLanguage = 1;
                            mRender.data.mMenu.SetLanguage(mRender.data.mLanguage);
                            break;
                    }
                }

                if (mRender.data.mMenu.GetActiveMenuIndex() == Menu.menu_do_camera_speed) {

                    mRender.data.mMenu.MenuClose();

                    switch (index) {
                        case Menu.menu_do_1_camera_speed:
                            Camera.mSpeed = 1;
                            break;
                        case Menu.menu_do_1_5_camera_speed:
                            Camera.mSpeed = 1.5f;
                            break;
                        case Menu.menu_do_2_camera_speed:
                            Camera.mSpeed = 2f;
                            break;
                        case Menu.menu_none:
                            break;
                    }
                }

                return true;
            }

            float[] tap = new float[2];
            tap[0] = x;
            tap[1] = y;
            mTap = tap;

            if (mRender.mHistory.size() > 0 &&
                    mRender.data.mBackSprite.IsPressed(x, y)) {

                Action a = mRender.mHistory.get(mRender.mHistory.size() - 1).clone();
                a.m_ActionDirectRotate = -a.m_ActionDirectRotate;
                mRender.mActions.Add(a);
                mRender.RemoveLastHistory();
                //mRender.list.ActionStart();

                return true;
            }

            if (mRender.data.mSettingsSprite.IsPressed(x, y)) {
                mDeltaX = 0;
                mDeltaY = 0;
                mDeltaZ = 0;

                Camera.mDeltaXInertia = 0;
                Camera.mDeltaYInertia = 0;
                Camera.mDeltaZInertia = 0;

                mPreviousX = -1;
                mPreviousY = -1;

                mRender.data.mMenu.MenuShow(Menu.menu_do_main);

                if (!mRender.data.mClock.IsEnable()) {
                    mRender.data.mMenu.SetTexture(Menu.menu_do_main_timer, 0);
                    //mRender.data.mMenu.SetTexture(Menu.menu_do_resume_timer, 0);
                }
                else {
                    mRender.data.mMenu.SetTexture(Menu.menu_do_main_timer, 1);
                    //mRender.data.mMenu.SetTexture(Menu.menu_do_resume_timer, 1);
                }

                return true;
            }

            if (mRender.data.mRotateBlockType != Structures.ROTATE_BLOCK_NONE && mRender.data.mLockSprite.IsPressed(x, y)) {
                mRender.data.mLockSprite.SetNextType();

                /*mRender.data.mLockSprite.SetType(0);
                mDeltaX = 2;

                Camera.mDeltaX = mDeltaX;
                Camera.mDeltaXInertia = 0;

                mAction = Structures.ACTION_MOVE;*/

                return true;
            }

            if (mRender.data.mSoundSprite.IsPressed(x, y)) {
                mRender.data.mSoundSprite.SetNextType();
                return true;
            }

            if (mRender.data.mRotateBlockType == Structures.ROTATE_BLOCK_NONE
                    || mRender.data.mRotateBlockType == Structures.ROTATE_BLOCK_ALL_FIGURE
                    || mRender.data.mRotateBlockType == Structures.ROTATE_BLOCK_CAMERA_FIGURE &&
                    mRender.data.mLockSprite.GetType() == Structures.LOCK_CAMERA) {
                float[] ray;
                ray = getViewRay(tap);
                Structures.PointView = ray.clone();
                Pair<int[], float[]> pr = mRender.data.mFigure.GetSelectItem(ray, Structures.CameraEye);
                mSelectItem = pr.first;
                float[] intersect = pr.second;
                //mRender.mLine = new Line(Structures.CameraEye, Structures.PointView);

                if (mSelectItem != null) {
                    if (mSelectItem[0] >= 0 && mSelectItem[1] >= 0 && mSelectItem[2] >= 0) {
                        mAction = Structures.ACTION_DO;
                        mRender.data.mFigure.CreateExVertices(mSelectItem, intersect);
                    }
                }
            }
        }

        if (mRender.data.mMenu.MenuIsEnable()) return true;

        if (mRender.data.mLockSprite.GetType() == Structures.UNLOCK && event.getAction() == MotionEvent.ACTION_MOVE && pointerCount == 1 && mAction != Structures.ACTION_DO && mAction != Structures.ACTION_SCALE)
        {
            if (mPreviousX >= 0 && mPreviousY >= 0) {

                if (mRender.data.mRotateState == Structures.ROTATE_ALL_DIRECTION) {
                    mDeltaX = (x - mPreviousX) / mDensity / 2f;
                    mDeltaY = (y - mPreviousY) / mDensity / 2f;

                    if (Math.abs(mDeltaY) < Math.abs(mDeltaX / 2f)) mDeltaY = 0;
                    if (Math.abs(mDeltaX) < Math.abs(mDeltaY / 2f)) mDeltaX = 0;

                    Camera.mDeltaX = mDeltaX * Camera.mSpeed;
                    Camera.mDeltaY = mDeltaY * Camera.mSpeed;

                    mPreviousX = x;
                    mPreviousY = y;

                    mAction = Structures.ACTION_MOVE;
                }
                if (mRender.data.mRotateState == Structures.ROTATE_ONLY_AXES) {
                    double dist = Math.sqrt((x - mPreviousX) * (x - mPreviousX) + (y - mPreviousY) * (y - mPreviousY)) / mDensity / 2f;

                    float[] tap = new float[2];
                    tap[0] = x;
                    tap[1] = y;
                    if (Dist2D(mTap, tap) > Math.round(Math.min(mRender.m_height, mRender.m_width) / 50)) {
                        float distX = x - mTap[0];
                        float distY = y - mTap[1];

                        if (Math.abs(distX) >= Math.abs(distY)) {
                            if (x < mTap[0])
                                mDeltaX = -10;
                            else
                                mDeltaX = 10;
                            mDeltaY = 0;
                        }
                        else {
                            if (y < mTap[1])
                                mDeltaY = -10;
                            else
                                mDeltaY = 10;
                            mDeltaX = 0;

                            if (mRender.data.mVisibleSides == 3) {
                                if (mDeltaY < 0) {
                                    if (Camera.GetDirection() == Structures.CAMERA_DOWN) {
                                        mDeltaY = 0;
                                    }
                                    Camera.SetDirection(Structures.CAMERA_DOWN);
                                }
                                else {
                                    if (Camera.GetDirection() == Structures.CAMERA_UP) {
                                        mDeltaY = 0;
                                    }
                                    Camera.SetDirection(Structures.CAMERA_UP);
                                }
                            }
                            else {
                                if (mRender.data.mFigure.GetFigure() == Structures.PYRAMID) {
                                    int dir = Camera.GetDirection();
                                    if (dir == Structures.CAMERA_DOWN || dir == Structures.CAMERA_UP)
                                        Camera.SetDirection(Structures.CAMERA_NONE);
                                    else {
                                        if (mDeltaY < 0) Camera.SetDirection(Structures.CAMERA_DOWN);
                                        else Camera.SetDirection(Structures.CAMERA_UP);
                                    }
                                }
                            }
                        }
                        Camera.mDeltaY = 0;
                        Camera.mDeltaX = 0;
                        Camera.mTotalXZRotate = 0;
                        Camera.mTotalYRotate = 0;

                        if (mRender.data.mVisibleSides != 3
                                && mRender.data.mFigure.GetFigure() == Structures.PYRAMID
                                && Camera.GetDirection() != Structures.CAMERA_NONE) {
                            Camera.mDeltaXInertia = 0;
                            Camera.mDeltaYInertia = mDeltaY * Camera.mSpeed;
                            Camera.mDeltaZInertia = mDeltaX * Camera.mSpeed;
                        }
                        else {
                            Camera.mDeltaXInertia = mDeltaX * Camera.mSpeed;
                            Camera.mDeltaYInertia = mDeltaY * Camera.mSpeed;
                            Camera.mDeltaZInertia = 0;
                        }

                        mAction = Structures.ACTION_AUTO_MOVE;
                    }
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE && mAction == Structures.ACTION_DO)
        {
            // отслеживаем следующий кубик
            float[] tap = new float[2];
            tap[0] = x;
            tap[1] = y;

            if (Dist2D(mTap, tap) > Math.round(Math.min(mRender.m_height, mRender.m_width) / 50)) {
                float[] ray;
                ray = getViewRay(tap);

                if (!mRender.DoAction(mSelectItem, ray, Structures.CameraEye)) {
                    /*if (mRender.data.mLockSprite.GetType() == Structures.UNLOCK)
                    {
                        if (mPreviousX >= 0 && mPreviousY >= 0) {
                            mDeltaX = (x - mPreviousX) / mDensity / 2f;
                            mDeltaY = (y - mPreviousY) / mDensity / 2f;

                            Camera.mDeltaX = mDeltaX;
                            Camera.mDeltaY = mDeltaY;

                            mPreviousX = x;
                            mPreviousY = y;

                            mAction = Structures.ACTION_MOVE;
                        }
                    }*/
                }
            }
        }
        if ((mRender.data.mLockSprite.GetType() == Structures.UNLOCK || mRender.data.mRotateBlockType == Structures.ROTATE_BLOCK_NONE) && event.getAction() == MotionEvent.ACTION_MOVE && pointerCount == 2)
        {
            mPreviousX = -1;
            mPreviousY = -1;
            // zoom
            float x1 = event.getX(0);
            float y1 = event.getY(0);

            float x2 = event.getX(1);
            float y2 = event.getY(1);
            double distance = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

            if (mInitDistance <= 0) {
                mInitDistance = (float) distance / Camera.mScale;
                mPoint1[0] = x1;
                mPoint1[1] = y1;

                mPoint2[0] = x2;
                mPoint2[1] = y2;
            }
            else {
                Camera.mScale = (float)distance / mInitDistance;
                if (Camera.mScale < 0.8) { Camera.mScale = 0.8f; mInitDistance = (float)distance / Camera.mScale;}
                if (Camera.mScale > 1.2) { Camera.mScale = 1.2f; mInitDistance = (float)distance / Camera.mScale;}

                float[] point1 = {x1, y1};
                float[] point2 = {x2, y2};

                float[] mid_point = {(mPoint1[0] + mPoint2[0]) / 2, (mPoint1[1] + mPoint2[1]) / 2};

                //float shift = (float)Math.max(Dist2D(mPoint1, point1), Dist2D(mPoint2, point2));
                float[] vec11 = Sub2D(mPoint1, mid_point);
                float[] vec12 = Sub2D(point1, mid_point);

                float[] vec21 = Sub2D(mPoint2, mid_point);
                float[] vec22 = Sub2D(point2, mid_point);

                float acos1 = Dot2D(vec11, vec12) / (Dist2D(mid_point, mPoint1) * Dist2D(mid_point, point1));
                float acos2 = Dot2D(vec21, vec22) / (Dist2D(mid_point, mPoint2) * Dist2D(mid_point, point2));
                if (acos1 < -1) acos1 = -1;
                if (acos1 > 1) acos1 = 1;
                if (acos2 < -1) acos2 = -1;
                if (acos2 > 1) acos2 = 1;
                float angle1 = (float)Math.toDegrees(Math.acos(acos1));
                float angle2 = (float)Math.toDegrees(Math.acos(acos2));

                float val1 = vec11[0] * vec12[1] - vec12[0] * vec11[1];
                float val2 = vec21[0] * vec22[1] - vec22[0] * vec21[1];

                if (val1 <= 0 && val2 <= 0) mDeltaZ = Math.max(Math.abs(angle1), Math.abs(angle2));
                if (val1 >= 0 && val2 >= 0) mDeltaZ = -Math.max(Math.abs(angle1), Math.abs(angle2));
                Camera.mDeltaZ = mDeltaZ;

                mPoint1[0] = point1[0];
                mPoint1[1] = point1[1];

                mPoint2[0] = point2[0];
                mPoint2[1] = point2[1];

                if (mRender.data.mRotateState == Structures.ROTATE_ONLY_AXES) {
                    mDeltaZ = 0;
                    Camera.mDeltaZ = 0;
                }
            }
            Camera.mScaling = true;

            mAction = Structures.ACTION_SCALE;
        }

        if ((mRender.data.mLockSprite.GetType() == Structures.UNLOCK || mRender.data.mRotateBlockType == Structures.ROTATE_BLOCK_NONE) && event.getAction() == MotionEvent.ACTION_UP) {
            if (mAction == Structures.ACTION_AUTO_MOVE) {
                return res;
            }

            if (mAction == Structures.ACTION_MOVE) {
                Camera.mDeltaXInertia = mDeltaX;
                Camera.mDeltaYInertia = mDeltaY;
            }

            if (mAction == Structures.ACTION_SCALE) {
                Camera.mDeltaZInertia = mDeltaZ;
            }

            mAction = Structures.ACTION_NONE;
        }

        return res;
    }
}
