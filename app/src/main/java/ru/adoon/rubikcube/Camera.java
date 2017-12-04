package ru.adoon.rubikcube;

import android.opengl.Matrix;

/**
 * Created with IntelliJ IDEA.
 * User: Navi
 * Date: 04.04.13
 * Time: 12:02
 * To change this template use File | Settings | File Templates.
 */
public class Camera {
    static float[] ProjMatrix = new float[16];
    static float[] ViewMatrix = new float[16];
    public static float mDeltaX = 0;
    public static float mDeltaY = 0;
    public static float mDeltaZ = 0;
    public static float mDeltaXInertia = 0;
    public static float mDeltaYInertia = 0;
    public static float mDeltaZInertia = 0;
    public static float mScale = 1;
    public static boolean mFirstStart = true;
    public static boolean mScaling = false;

    public static float[] mAccumulatedRotation = new float[16];
    private static float[] mCurrentRotation = new float[16];

    //private static float[] mAccumulatedRotationLight = new float[16];
    //private static float[] mCurrentRotationLight = new float[16];

    public static void setfrustumMProj(float left, float right, float bottom, float top, float near, float far){
        Matrix.frustumM(ProjMatrix, 0, left,right, bottom, top, near, far);
        //ProjMatrix[15] = 20;
    }
    public static void setPerspectiveM( float fovy, float aspect, float zNear, float zFar){
        Matrix.perspectiveM(ProjMatrix, 0, fovy, aspect, zNear, zFar);
    }

    private static float[] Normalize(float[] vect) {
        float len = (float)Math.sqrt(vect[0] * vect[0] + vect[1] * vect[1] + vect[2] * vect[2]);
        vect[0] /= len;
        vect[1] /= len;
        vect[2] /= len;

        return vect;
    }

    private static void SetCameraEyeDistance(float distance) {

        //Structures.CameraEye = Normalize(Structures.CameraEye);

        Structures.CameraEye[0] *= distance;
        Structures.CameraEye[1] *= distance;
        Structures.CameraEye[2] *= distance;
    }

    public static void setLookAtM(){

        if (!mFirstStart && mDeltaX == 0 && mDeltaY == 0 && mDeltaZ == 0
                && mDeltaXInertia == 0 && mDeltaYInertia == 0 && mDeltaZInertia == 0 && !mScaling) return;

        if (mFirstStart) {
            Matrix.setIdentityM(mAccumulatedRotation, 0);
            mFirstStart = false;
        }

        Matrix.setIdentityM(mCurrentRotation, 0);
        float[] mTemporaryMatrix = new float[16];
        float[] mTemporaryMatrix2 = new float[16];
        if (mDeltaX != 0 || mDeltaY != 0 || mDeltaZ != 0) {
            Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 1.0f, 0.0f);
            Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 1.0f, 0.0f, 0.0f);
            Matrix.rotateM(mCurrentRotation, 0, mDeltaZ, 0.0f, 0.0f, 1.0f);

            Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
            System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);
        }
        else {
            if (mDeltaXInertia != 0 || mDeltaYInertia != 0 || mDeltaZInertia != 0) {
                Matrix.rotateM(mCurrentRotation, 0, mDeltaXInertia, 0.0f, 1.0f, 0.0f);
                Matrix.rotateM(mCurrentRotation, 0, mDeltaYInertia, 1.0f, 0.0f, 0.0f);
                Matrix.rotateM(mCurrentRotation, 0, mDeltaZInertia, 0.0f, 0.0f, 1.0f);

                Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
                System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);

                mDeltaXInertia /= 1.2;
                mDeltaYInertia /= 1.2;
                mDeltaZInertia /= 1.2;

                if (Math.abs(mDeltaXInertia) < 0.001) mDeltaXInertia = 0;
                if (Math.abs(mDeltaYInertia) < 0.001) mDeltaYInertia = 0;
                if (Math.abs(mDeltaZInertia) < 0.001) mDeltaZInertia = 0;
            }
        }

        for (int i = 0; i < 16; i ++) mTemporaryMatrix2[i] = 0;
        mTemporaryMatrix2[0] = Structures.CameraEyeInit[0];
        mTemporaryMatrix2[4] = Structures.CameraEyeInit[1];
        mTemporaryMatrix2[8] = Structures.CameraEyeInit[2];
        Matrix.multiplyMM(mTemporaryMatrix, 0, mTemporaryMatrix2, 0, mAccumulatedRotation, 0);
        Structures.CameraEye[0] = mTemporaryMatrix[0];
        Structures.CameraEye[1] = mTemporaryMatrix[4];
        Structures.CameraEye[2] = mTemporaryMatrix[8];

        SetCameraEyeDistance(Structures.CameraEyeDistance / mScale);

        for (int i = 0; i < 16; i ++) mTemporaryMatrix2[i] = 0;
        mTemporaryMatrix2[0] = Structures.CameraUpInit[0];
        mTemporaryMatrix2[4] = Structures.CameraUpInit[1];
        mTemporaryMatrix2[8] = Structures.CameraUpInit[2];
        Matrix.multiplyMM(mTemporaryMatrix, 0, mTemporaryMatrix2, 0, mAccumulatedRotation, 0);
        Structures.CameraUp[0] = mTemporaryMatrix[0];
        Structures.CameraUp[1] = mTemporaryMatrix[4];
        Structures.CameraUp[2] = mTemporaryMatrix[8];

        for (int i = 0; i < 16; i ++) mTemporaryMatrix2[i] = 0;
        mTemporaryMatrix2[0] = Structures.LightInit[0];
        mTemporaryMatrix2[4] = Structures.LightInit[1];
        mTemporaryMatrix2[8] = Structures.LightInit[2];
        Matrix.multiplyMM(mTemporaryMatrix, 0, mTemporaryMatrix2, 0, mAccumulatedRotation, 0);
        Structures.Light[0] = mTemporaryMatrix[0];
        Structures.Light[1] = mTemporaryMatrix[4];
        Structures.Light[2] = mTemporaryMatrix[8];


        Matrix.setLookAtM(ViewMatrix, 0, Structures.CameraEye[0], Structures.CameraEye[1], Structures.CameraEye[2],
                Structures.CameraCenter[0], Structures.CameraCenter[1], Structures.CameraCenter[2],
                Structures.CameraUp[0], Structures.CameraUp[1], Structures.CameraUp[2]);

        /*float[] mTemporaryMatrix = new float[16];
        if (mDeltaX != 0 || mDeltaY != 0) {
            Matrix.setIdentityM(mCurrentRotation, 0);
            Matrix.setIdentityM(mCurrentRotationLight, 0);

            Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 1.0f, 0.0f);
            Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 1.0f, 0.0f, 0.0f);

            Matrix.rotateM(mCurrentRotationLight, 0, mDeltaX, 0.0f, 1.0f, 0.0f);
            Matrix.rotateM(mCurrentRotationLight, 0, mDeltaY, 1.0f, 0.0f, 0.0f);

            Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
            System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);

            Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotationLight, 0, mAccumulatedRotationLight, 0);
            System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotationLight, 0, 16);
        }

        Matrix.multiplyMM(mTemporaryMatrix, 0, ViewMatrix, 0, mAccumulatedRotation, 0);
        System.arraycopy(mTemporaryMatrix, 0, ViewMatrix, 0, 16);

        Matrix.multiplyMV(Structures.Light, 0, mAccumulatedRotationLight, 0, Structures.LightInit, 0);*/

        mDeltaX  = 0.0f;
        mDeltaY = 0.0f;
        mDeltaZ = 0.0f;
        mScaling = false;
    }
    public static float[] getProjMatrix(){
        return ProjMatrix;
    }
    public static float[] getViewMatrix(){
        return ViewMatrix;
    }
}
