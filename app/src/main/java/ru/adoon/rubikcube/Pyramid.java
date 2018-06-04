package ru.adoon.rubikcube;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Лукшин on 22.11.2017.
 */

public class Pyramid {
    public volatile ArrayList<ArrayList<ArrayList<PyramidItem>>> mItems = null;//new ArrayList<ArrayList<ArrayList<CubeItemOld>>>();
    public static int mTexture;

    public static final int TRIANGLE_COUNT_PYRAMID = 4;
    public static final int TRIANGLE_COUNT_PYRAMID2 = 8;

    public Set<Integer> mLSide = new HashSet<>();
    public Set<Integer> mRSide = new HashSet<>();
    public Set<Integer> mBSide = new HashSet<>();
    public Set<Integer> mFSide = new HashSet<>();

    public int mPyramidDim = 3;
    public float mScale = 2.7f;

    public static final int GREEN = 0;
    public static final int RED = 1;
    public static final int BLUE = 2;
    public static final int YELLOW = 3;
    public static final int BLACK = 4;

    public static float h = (float)Math.sqrt(2 / 3f);
    public static float h2 = (float)Math.sqrt(3f) / 2f;
    public static float h2_3 = h2 / 3f;
    public static float radius = (3 * h * h + 1) / (6 * h);//(float)Math.sqrt(6f) / 4f;
    //public static float H = 3 * (float)Math.sqrt(2 / 3f);
    //public static float Radius = (3 * H * H + 1) / (6 * H); // радиус большой окружности описанной около всей пирамиды
    public static float angle = (float)Math.asin(2 * Math.sqrt(2) / 3);
    //public static float shift_y = radius - h / 2;

    int[][][] rotateL = {
            {{111, 101, 100, 101,   2, 101}, {202}},
            {{222, 212, 211, 201, 200, 201, 102, 103,   4, 103, 113, 212}, {313, 303, 302, 303, 204, 303}, {404}},
            {{333, 323, 322, 312, 311, 301, 300, 301, 202, 203, 104, 105,   6, 105, 115, 214, 224, 323}, {424, 414, 413, 403, 402, 403, 304, 305, 206, 305, 315, 414}, {515, 505, 504, 505, 406, 505}, {606}}
    };
    int[][][] rotateR = {
            {{111, 101, 100, 101, 202, 101}, {  2}},
            {{222, 212, 211, 201, 200, 201, 302, 303, 404, 303, 313, 212}, {113, 103, 102, 103, 204, 103}, {  4}},
            {{333, 323, 322, 312, 311, 301, 300, 301, 402, 403, 504, 505, 606, 505, 515, 414, 424, 323}, {224, 214, 213, 203, 202, 203, 304, 305, 406, 305, 315, 214}, {115, 105, 104, 105, 206, 105}, {  6}}
    };
    int[][][] rotateB = {
            {{100, 101, 202, 101,   2, 101}, {111}},
            {{200, 201, 302, 303, 404, 303, 204, 103,   4, 103, 102, 201}, {211, 212, 313, 212, 113, 212}, {222}},
            {{300, 301, 402, 403, 504, 505, 606, 505, 406, 305, 206, 105,   6, 105, 104, 203, 202, 301}, {311, 312, 413, 414, 515, 414, 315, 214, 115, 214, 213, 312}, {322, 323, 424, 323, 224, 323}, {333}}
    };
    int[][][] rotateF = {
            {{111, 101, 202, 101,   2, 101}, {100}},
            {{222, 212, 313, 303, 404, 303, 204, 103,   4, 103, 113, 212}, {211, 201, 302, 201, 102, 201}, {200}},
            {{333, 323, 424, 414, 515, 505, 606, 505, 406, 305, 206, 105,   6, 105, 115, 214, 224, 323}, {322, 312, 413, 403, 504, 403, 304, 203, 104, 203, 213, 312}, {311, 301, 402, 301, 202, 301}, {300}}
    };

    public static float[] vertex = {
            0, h / 2, 0,
            -1 / 2f, - h / 2, h2 / 3f,
            1 / 2f, - h / 2, h2 / 3f,
            0, - h / 2, -2 * h2 / 3,
    };

    public static float[] vertex2 = {
            // ур = 2, инд = 0
            1 / 2f, h / 2, h2 / 3f, // 1
            -1 / 2f, h / 2, h2 / 3f, // 2
            0, - h / 2, 2 * h2 / 3f, // 3
            0, h / 2, -2 * h2 / 3f, // 4
            1 / 2f, - h / 2, -h2 / 3f, // 5
            -1 / 2f, - h / 2, -h2 / 3f, // 6
    };

    Pyramid(Context context) {

        mItems = new ArrayList<ArrayList<ArrayList<PyramidItem>>>();
        mTexture = TextureUtils.loadTexture(context, R.drawable.pyramid);

        // для размера 3
        // y = 2
        //      0   1   2   3   4
        //  0   .   .   .   .   .
        //  1   .   .   .   .   .
        //  2   .   .   x   .   .
        //  3   .   .   .   .   .
        //  4   .   .   .   .   .

        // y = 1
        //      0   1   2   3   4
        //  0   .   .   .   .   .
        //  1   .   .   x   .   .
        //  2   .   .   o   .   .
        //  3   .   x   .   x   .
        //  4   .   .   .   .   .

        // y = 0
        //      0   1   2   3   4
        //  0   .   .   x   .   .
        //  1   .   .   o   .   .
        //  2   .   x   .   x   .
        //  3   .   o   .   o   .
        //  4   x   .   x   .   x

        // для размера 4
        // y = 3
        //      0   1   2   3   4   5   6
        //  0   .   .   .   .   .   .   .
        //  1   .   .   .   .   .   .   .
        //  2   .   .   .   .   .   .   .
        //  3   .   .   .   x   .   .   .
        //  4   .   .   .   .   .   .   .
        //  5   .   .   .   .   .   .   .
        //  6   .   .   .   .   .   .   .

        // y = 2
        //      0   1   2   3   4   5   6
        //  0   .   .   .   .   .   .   .
        //  1   .   .   .   .   .   .   .
        //  2   .   .   .   x   .   .   .
        //  3   .   .   .   o   .   .   .
        //  4   .   .   x   .   x   .   .
        //  5   .   .   .   .   .   .   .
        //  6   .   .   .   .   .   .   .

        // y = 1
        //      0   1   2   3   4   5   6
        //  0   .   .   .   .   .   .   .
        //  1   .   .   .   x   .   .   .
        //  2   .   .   .   o   .   .   .
        //  3   .   .   x   .   x   .   .
        //  4   .   .   o   .   o   .   .
        //  5   .   x   .   x   .   x   .
        //  6   .   .   .   .   .   .   .

        // y = 0
        //      0   1   2   3   4   5   6
        //  0   .   .   .   x   .   .   .
        //  1   .   .   .   o   .   .   .
        //  2   .   .   x   .   x   .   .
        //  3   .   .   o   .   o   .   .
        //  4   .   x   .   x   .   x   .
        //  5   .   o   .   o   .   o   .
        //  6   x   .   x   .   x   .   x

        // для размера 5
        // y = 4
        //      0   1   2   3   4   5   6   7   8
        //  0   .   .   .   .   .   .   .   .   .
        //  1   .   .   .   .   .   .   .   .   .
        //  2   .   .   .   .   .   .   .   .   .
        //  3   .   .   .   .   .   .   .   .   .
        //  4   .   .   .   .   x   .   .   .   .
        //  5   .   .   .   .   .   .   .   .   .
        //  6   .   .   .   .   .   .   .   .   .
        //  7   .   .   .   .   .   .   .   .   .
        //  8   .   .   .   .   .   .   .   .   .

        // y = 3
        //      0   1   2   3   4   5   6   7   8
        //  0   .   .   .   .   .   .   .   .   .
        //  1   .   .   .   .   .   .   .   .   .
        //  2   .   .   .   .   .   .   .   .   .
        //  3   .   .   .   .   x   .   .   .   .
        //  4   .   .   .   .   o   .   .   .   .
        //  5   .   .   .   x   .   x   .   .   .
        //  6   .   .   .   .   .   .   .   .   .
        //  7   .   .   .   .   .   .   .   .   .
        //  8   .   .   .   .   .   .   .   .   .

        // y = 2
        //      0   1   2   3   4   5   6   7   8
        //  0   .   .   .   .   .   .   .   .   .
        //  1   .   .   .   .   .   .   .   .   .
        //  2   .   .   .   .   x   .   .   .   .
        //  3   .   .   .   .   o   .   .   .   .
        //  4   .   .   .   x   .   x   .   .   .
        //  5   .   .   .   o   .   o   .   .   .
        //  6   .   .   x   .   x   .   x   .   .
        //  7   .   .   .   .   .   .   .   .   .
        //  8   .   .   .   .   .   .   .   .   .

        // y = 1
        //      0   1   2   3   4   5   6   7   8
        //  0   .   .   .   .   .   .   .   .   .
        //  1   .   .   .   .   x   .   .   .   .
        //  2   .   .   .   .   o   .   .   .   .
        //  3   .   .   .   x   .   x   .   .   .
        //  4   .   .   .   o   .   o   .   .   .
        //  5   .   .   x   .   x   .   x   .   .
        //  6   .   .   o   .   o   .   o   .   .
        //  7   .   x   .   x   .   x   .   x   .
        //  8   .   .   .   .   .   .   .   .   .

        // y = 0
        //      0   1   2   3   4   5   6   7   8
        //  0   .   .   .   .   x   .   .   .   .
        //  1   .   .   .   .   o   .   .   .   .
        //  2   .   .   .   x   .   x   .   .   .
        //  3   .   .   .   o   .   o   .   .   .
        //  4   .   .   x   .   x   .   x   .   .
        //  5   .   .   o   .   o   .   o   .   .
        //  6   .   x   .   x   .   x   .   x   .
        //  7   .   o   .   o   .   o   .   o   .
        //  8   x   .   x   .   x   .   x   .   x

        // xyzr - r - ряд
    }

    public void SetContext(Context context) {
        TextureUtils.deleteTexture(mTexture);
        mTexture = TextureUtils.loadTexture(context, R.drawable.pyramid);
    }

    public float[] CheckItemInAction(Action a, int posX, int posY, int posZ) {
        if (a.ActionIsEnable()) {
            int val = posX * 1000 + posY * 100 + posZ * 10 + a.m_ActionPosRotate;
            if (a.m_ActionAxisRotate == Structures.AXE_B && mBSide.contains(val)) return new float[]{0, 1, 0};
            if (a.m_ActionAxisRotate == Structures.AXE_L && mLSide.contains(val)) {
                float angleY = 30;
                float angleZ = 90 - (float) Math.toDegrees(Math.atan(Math.sqrt(2) * 2));
                return new float[]{1, angleY, angleZ};
            }
            if (a.m_ActionAxisRotate == Structures.AXE_R && mRSide.contains(val)) {
                float angleY = -30;
                float angleZ = -90 + (float) Math.toDegrees(Math.atan(Math.sqrt(2) * 2));
                return new float[]{1, angleY, angleZ};
            }
            if (a.m_ActionAxisRotate == Structures.AXE_F && mFSide.contains(val)) {
                float angleX = 90 - (float) Math.toDegrees(Math.atan(Math.sqrt(2) * 2));
                return new float[]{angleX, 0, 1};
            }
            if (mPyramidDim > 2 && a.m_ActionPosRotate == mPyramidDim - 2) {
                val = posX * 1000 + posY * 100 + posZ * 10 + mPyramidDim - 1;
                if (a.m_ActionAxisRotate == Structures.AXE_B && mBSide.contains(val)) return new float[]{0, 1, 0};
                if (a.m_ActionAxisRotate == Structures.AXE_L && mLSide.contains(val)) {
                    float angleY = 30;
                    float angleZ = 90 - (float) Math.toDegrees(Math.atan(Math.sqrt(2) * 2));
                    return new float[]{1, angleY, angleZ};
                }
                if (a.m_ActionAxisRotate == Structures.AXE_R && mRSide.contains(val)) {
                    float angleY = -30;
                    float angleZ = -90 + (float) Math.toDegrees(Math.atan(Math.sqrt(2) * 2));
                    return new float[]{1, angleY, angleZ};
                }
                if (a.m_ActionAxisRotate == Structures.AXE_F && mFSide.contains(val)) {
                    float angleX = 90 - (float) Math.toDegrees(Math.atan(Math.sqrt(2) * 2));
                    return new float[]{angleX, 0, 1};
                }
            }
        }
        return null;
    }

    public void Draw(Action a) {
        for (int x = 0; x < mItems.size(); x++) {
            for (int y = 0; y < mItems.get(x).size(); y++) {
                for (int z = 0; z < mItems.get(x).get(y).size(); z++) {
                    PyramidItem pi = mItems.get(x).get(y).get(z);
                    if (pi != null) {
                        pi.InitMatrix();
                        if (a != null) {
                            float[] rotateType = CheckItemInAction(a, pi.mPosX, pi.mPosY, pi.mPosZ);
                            pi.RotateMatrix(a.mAngle, rotateType);
                        }
                        /*if (pi.mNearItems != null) {
                            Set<Integer> keys = pi.mNearItems.keySet();
                            for(int key: keys) {
                                for (int j = 0; j < pi.mNearItems.get(key).size(); j++) {
                                    pi.mNearItems.get(key).get(j).InitMatrix();
                                    pi.mNearItems.get(key).get(j).Draw();
                                }
                            }
                        }
                        else*/
                            pi.Draw();

                    }
                }
            }
        }
    }

    public synchronized void PyramidInit(int PyramidDim) {

        mPyramidDim = PyramidDim;
        if (PyramidDim == 2) mScale = 2.7f * 1.2f;
        if (PyramidDim == 3) mScale = 2.7f * 1.0f;
        if (PyramidDim == 4) mScale = 2.7f * 0.8f;
        if (PyramidDim == 5) mScale = 2.7f * 0.6f;

        if (mItems.size() == 0) {
            for (int x = 0; x < mPyramidDim + mPyramidDim - 1; x++) {
                mItems.add(new ArrayList<ArrayList<PyramidItem>>());
                for (int y = 0; y < mPyramidDim; y++) {
                    mItems.get(x).add(new ArrayList<PyramidItem>());
                    for (int z = 0; z < mPyramidDim + mPyramidDim - 1; z++) {
                        mItems.get(x).get(y).add(null);
                    }
                }
            }
        }

        //mItems.get(2).get(2).set(2, new PyramidItem(2, 1, 2, 1, 0, 0, 2, 0, null));

        if (mPyramidDim == 2) {
            mItems.get(1).get(1).set(1, new PyramidItem(this, 1, 1, 1, 0, 0, 0, 1, 0, null, mPyramidDim, mScale));

            mItems.get(1).get(0).set(0, new PyramidItem(this, 1, 0, 0, 0, 0, 0, 0, 1, null, mPyramidDim, mScale));
            mItems.get(0).get(0).set(2, new PyramidItem(this, 0, 0, 2, 0, 0, 1, 0, 0, null, mPyramidDim, mScale));
            mItems.get(2).get(0).set(2, new PyramidItem(this, 2, 0, 2, 0, 1, 0, 0, 0, null, mPyramidDim, mScale));
            mItems.get(1).get(0).set(1, new PyramidItem(this, 1, 0, 1, 1, 0, 0, 0, 0, null, mPyramidDim, mScale));
        }

        if (mPyramidDim == 3) {
            mItems.get(2).get(2).set(2, new PyramidItem(this, 2, 2, 2, 0, 0, 0, 2, 0, null, mPyramidDim, mScale));

            mItems.get(2).get(1).set(1, new PyramidItem(this, 2, 1, 1, 0, 0, 0, 1, 1, null, mPyramidDim, mScale));
            mItems.get(1).get(1).set(3, new PyramidItem(this, 1, 1, 3, 0, 0, 1, 1, 0, null, mPyramidDim, mScale));
            mItems.get(3).get(1).set(3, new PyramidItem(this, 3, 1, 3, 0, 1, 0, 1, 0, null, mPyramidDim, mScale));
            mItems.get(2).get(1).set(2, new PyramidItem(this, 2, 1, 2, 1, 0, 0, 1, 0, null, mPyramidDim, mScale));

            mItems.get(2).get(0).set(0, new PyramidItem(this, 2, 0, 0, 0, 0, 0, 0, 2, null, mPyramidDim, mScale));
            mItems.get(1).get(0).set(2, new PyramidItem(this, 1, 0, 2, 0, 0, 1, 0, 1, null, mPyramidDim, mScale));
            mItems.get(3).get(0).set(2, new PyramidItem(this, 3, 0, 2, 0, 1, 0, 0, 1, null, mPyramidDim, mScale));
            mItems.get(0).get(0).set(4, new PyramidItem(this, 0, 0, 4, 0, 0, 2, 0, 0, null, mPyramidDim, mScale));
            mItems.get(2).get(0).set(4, new PyramidItem(this, 2, 0, 4, 0, 1, 1, 0, 0, null, mPyramidDim, mScale));
            mItems.get(4).get(0).set(4, new PyramidItem(this, 4, 0, 4, 0, 2, 0, 0, 0, null, mPyramidDim, mScale));
            mItems.get(2).get(0).set(1, new PyramidItem(this, 2, 0, 1, 1, 0, 0, 0, 1, null, mPyramidDim, mScale));
            mItems.get(1).get(0).set(3, new PyramidItem(this, 1, 0, 3, 1, 0, 1, 0, 0, null, mPyramidDim, mScale));
            mItems.get(3).get(0).set(3, new PyramidItem(this, 3, 0, 3, 1, 1, 0, 0, 0, null, mPyramidDim, mScale));
        }

        if (mPyramidDim == 4) {
            mItems.get(3).get(3).set(3, new PyramidItem(this, 3, 3, 3, 0, 0, 0, 3, 0, null, mPyramidDim, mScale));

            mItems.get(3).get(2).set(2, new PyramidItem(this, 3, 2, 2, 0, 0, 0, 2, 1, null, mPyramidDim, mScale));
            mItems.get(2).get(2).set(4, new PyramidItem(this, 2, 2, 4, 0, 0, 1, 2, 0, null, mPyramidDim, mScale));
            mItems.get(4).get(2).set(4, new PyramidItem(this, 4, 2, 4, 0, 1, 0, 2, 0, null, mPyramidDim, mScale));
            mItems.get(3).get(2).set(3, new PyramidItem(this, 3, 2, 3, 1, 0, 0, 2, 0, null, mPyramidDim, mScale));

            mItems.get(3).get(1).set(1, new PyramidItem(this, 3, 1, 1, 0, 0, 0, 1, 2, null, mPyramidDim, mScale));
            mItems.get(2).get(1).set(3, new PyramidItem(this, 2, 1, 3, 0, 0, 1, 1, 1, null, mPyramidDim, mScale));
            mItems.get(4).get(1).set(3, new PyramidItem(this, 4, 1, 3, 0, 1, 0, 1, 1, null, mPyramidDim, mScale));
            mItems.get(1).get(1).set(5, new PyramidItem(this, 1, 1, 5, 0, 0, 2, 1, 0, null, mPyramidDim, mScale));
            mItems.get(3).get(1).set(5, new PyramidItem(this, 3, 1, 5, 0, 1, 1, 1, 0, null, mPyramidDim, mScale));
            mItems.get(5).get(1).set(5, new PyramidItem(this, 5, 1, 5, 0, 2, 0, 1, 0, null, mPyramidDim, mScale));
            mItems.get(3).get(1).set(2, new PyramidItem(this, 3, 1, 2, 1, 0, 0, 1, 1, null, mPyramidDim, mScale));
            mItems.get(2).get(1).set(4, new PyramidItem(this, 2, 1, 4, 1, 0, 1, 1, 0, null, mPyramidDim, mScale));
            mItems.get(4).get(1).set(4, new PyramidItem(this, 4, 1, 4, 1, 1, 0, 1, 0, null, mPyramidDim, mScale));

            mItems.get(3).get(0).set(0, new PyramidItem(this, 3, 0, 0, 0, 0, 0, 0, 3, null, mPyramidDim, mScale));
            mItems.get(2).get(0).set(2, new PyramidItem(this, 2, 0, 2, 0, 0, 1, 0, 2, null, mPyramidDim, mScale));
            mItems.get(4).get(0).set(2, new PyramidItem(this, 4, 0, 2, 0, 1, 0, 0, 2, null, mPyramidDim, mScale));
            mItems.get(1).get(0).set(4, new PyramidItem(this, 1, 0, 4, 0, 0, 2, 0, 1, null, mPyramidDim, mScale));
            mItems.get(3).get(0).set(4, new PyramidItem(this, 3, 0, 4, 0, 1, 1, 0, 1, null, mPyramidDim, mScale));
            mItems.get(5).get(0).set(4, new PyramidItem(this, 5, 0, 4, 0, 2, 0, 0, 1, null, mPyramidDim, mScale));
            mItems.get(0).get(0).set(6, new PyramidItem(this, 0, 0, 6, 0, 0, 3, 0, 0, null, mPyramidDim, mScale));
            mItems.get(2).get(0).set(6, new PyramidItem(this, 2, 0, 6, 0, 1, 2, 0, 0, null, mPyramidDim, mScale));
            mItems.get(4).get(0).set(6, new PyramidItem(this, 4, 0, 6, 0, 2, 1, 0, 0, null, mPyramidDim, mScale));
            mItems.get(6).get(0).set(6, new PyramidItem(this, 6, 0, 6, 0, 3, 0, 0, 0, null, mPyramidDim, mScale));
            mItems.get(3).get(0).set(1, new PyramidItem(this, 3, 0, 1, 1, 0, 0, 0, 2, null, mPyramidDim, mScale));
            mItems.get(2).get(0).set(3, new PyramidItem(this, 2, 0, 3, 1, 0, 1, 0, 1, null, mPyramidDim, mScale));
            mItems.get(4).get(0).set(3, new PyramidItem(this, 4, 0, 3, 1, 1, 0, 0, 1, null, mPyramidDim, mScale));
            mItems.get(1).get(0).set(5, new PyramidItem(this, 1, 0, 5, 1, 0, 2, 0, 0, null, mPyramidDim, mScale));
            mItems.get(3).get(0).set(5, new PyramidItem(this, 3, 0, 5, 1, 1, 1, 0, 0, null, mPyramidDim, mScale));
            mItems.get(5).get(0).set(5, new PyramidItem(this, 5, 0, 5, 1, 2, 0, 0, 0, null, mPyramidDim, mScale));
        }

        if (mPyramidDim == 5) {
            mItems.get(4).get(4).set(4, new PyramidItem(this, 4, 4, 4, 0, 0, 0, 4, 0, null, mPyramidDim, mScale));

            mItems.get(4).get(3).set(3, new PyramidItem(this, 4, 3, 3, 0, 0, 0, 3, 1, null, mPyramidDim, mScale));
            mItems.get(3).get(3).set(5, new PyramidItem(this, 3, 3, 5, 0, 0, 1, 3, 0, null, mPyramidDim, mScale));
            mItems.get(5).get(3).set(5, new PyramidItem(this, 5, 3, 5, 0, 1, 0, 3, 0, null, mPyramidDim, mScale));
            mItems.get(4).get(3).set(4, new PyramidItem(this, 4, 3, 4, 1, 0, 0, 3, 0, null, mPyramidDim, mScale));

            mItems.get(4).get(2).set(2, new PyramidItem(this, 4, 2, 2, 0, 0, 0, 2, 2, null, mPyramidDim, mScale));
            mItems.get(3).get(2).set(4, new PyramidItem(this, 3, 2, 4, 0, 0, 1, 2, 1, null, mPyramidDim, mScale));
            mItems.get(5).get(2).set(4, new PyramidItem(this, 5, 2, 4, 0, 1, 0, 2, 1, null, mPyramidDim, mScale));
            mItems.get(2).get(2).set(6, new PyramidItem(this, 2, 2, 6, 0, 0, 2, 2, 0, null, mPyramidDim, mScale));
            mItems.get(4).get(2).set(6, new PyramidItem(this, 4, 2, 6, 0, 1, 1, 2, 0, null, mPyramidDim, mScale));
            mItems.get(6).get(2).set(6, new PyramidItem(this, 6, 2, 6, 0, 2, 0, 2, 0, null, mPyramidDim, mScale));
            mItems.get(4).get(2).set(3, new PyramidItem(this, 4, 2, 3, 1, 0, 0, 2, 1, null, mPyramidDim, mScale));
            mItems.get(3).get(2).set(5, new PyramidItem(this, 3, 2, 5, 1, 0, 1, 2, 0, null, mPyramidDim, mScale));
            mItems.get(5).get(2).set(5, new PyramidItem(this, 5, 2, 5, 1, 1, 0, 2, 0, null, mPyramidDim, mScale));

            mItems.get(4).get(1).set(1, new PyramidItem(this, 4, 1, 1, 0, 0, 0, 1, 3, null, mPyramidDim, mScale));
            mItems.get(3).get(1).set(3, new PyramidItem(this, 3, 1, 3, 0, 0, 1, 1, 2, null, mPyramidDim, mScale));
            mItems.get(5).get(1).set(3, new PyramidItem(this, 5, 1, 3, 0, 1, 0, 1, 2, null, mPyramidDim, mScale));
            mItems.get(2).get(1).set(5, new PyramidItem(this, 2, 1, 5, 0, 0, 2, 1, 1, null, mPyramidDim, mScale));
            mItems.get(4).get(1).set(5, new PyramidItem(this, 4, 1, 5, 0, 1, 1, 1, 1, null, mPyramidDim, mScale));
            mItems.get(6).get(1).set(5, new PyramidItem(this, 6, 1, 5, 0, 2, 0, 1, 1, null, mPyramidDim, mScale));
            mItems.get(1).get(1).set(7, new PyramidItem(this, 1, 1, 7, 0, 0, 3, 1, 0, null, mPyramidDim, mScale));
            mItems.get(3).get(1).set(7, new PyramidItem(this, 3, 1, 7, 0, 1, 2, 1, 0, null, mPyramidDim, mScale));
            mItems.get(5).get(1).set(7, new PyramidItem(this, 5, 1, 7, 0, 2, 1, 1, 0, null, mPyramidDim, mScale));
            mItems.get(7).get(1).set(7, new PyramidItem(this, 7, 1, 7, 0, 3, 0, 1, 0, null, mPyramidDim, mScale));
            mItems.get(4).get(1).set(2, new PyramidItem(this, 4, 1, 2, 1, 0, 0, 1, 2, null, mPyramidDim, mScale));
            mItems.get(3).get(1).set(4, new PyramidItem(this, 3, 1, 4, 1, 0, 1, 1, 1, null, mPyramidDim, mScale));
            mItems.get(5).get(1).set(4, new PyramidItem(this, 5, 1, 4, 1, 1, 0, 1, 1, null, mPyramidDim, mScale));
            mItems.get(2).get(1).set(6, new PyramidItem(this, 2, 1, 6, 1, 0, 2, 1, 0, null, mPyramidDim, mScale));
            mItems.get(4).get(1).set(6, new PyramidItem(this, 4, 1, 6, 1, 1, 1, 1, 0, null, mPyramidDim, mScale));
            mItems.get(6).get(1).set(6, new PyramidItem(this, 6, 1, 6, 1, 2, 0, 1, 0, null, mPyramidDim, mScale));

            mItems.get(4).get(0).set(0, new PyramidItem(this, 4, 0, 0, 0, 0, 0, 0, 4, null, mPyramidDim, mScale));
            mItems.get(3).get(0).set(2, new PyramidItem(this, 3, 0, 2, 0, 0, 1, 0, 3, null, mPyramidDim, mScale));
            mItems.get(5).get(0).set(2, new PyramidItem(this, 5, 0, 2, 0, 1, 0, 0, 3, null, mPyramidDim, mScale));
            mItems.get(2).get(0).set(4, new PyramidItem(this, 2, 0, 4, 0, 0, 2, 0, 2, null, mPyramidDim, mScale));
            mItems.get(4).get(0).set(4, new PyramidItem(this, 4, 0, 4, 0, 1, 1, 0, 2, null, mPyramidDim, mScale));
            mItems.get(6).get(0).set(4, new PyramidItem(this, 6, 0, 4, 0, 2, 0, 0, 2, null, mPyramidDim, mScale));
            mItems.get(1).get(0).set(6, new PyramidItem(this, 1, 0, 6, 0, 0, 3, 0, 1, null, mPyramidDim, mScale));
            mItems.get(3).get(0).set(6, new PyramidItem(this, 3, 0, 6, 0, 1, 2, 0, 1, null, mPyramidDim, mScale));
            mItems.get(5).get(0).set(6, new PyramidItem(this, 5, 0, 6, 0, 2, 1, 0, 1, null, mPyramidDim, mScale));
            mItems.get(7).get(0).set(6, new PyramidItem(this, 7, 0, 6, 0, 3, 0, 0, 1, null, mPyramidDim, mScale));
            mItems.get(0).get(0).set(8, new PyramidItem(this, 0, 0, 8, 0, 0, 4, 0, 0, null, mPyramidDim, mScale));
            mItems.get(2).get(0).set(8, new PyramidItem(this, 2, 0, 8, 0, 1, 3, 0, 0, null, mPyramidDim, mScale));
            mItems.get(4).get(0).set(8, new PyramidItem(this, 4, 0, 8, 0, 2, 2, 0, 0, null, mPyramidDim, mScale));
            mItems.get(6).get(0).set(8, new PyramidItem(this, 6, 0, 8, 0, 3, 1, 0, 0, null, mPyramidDim, mScale));
            mItems.get(8).get(0).set(8, new PyramidItem(this, 8, 0, 8, 0, 4, 0, 0, 0, null, mPyramidDim, mScale));
            mItems.get(4).get(0).set(1, new PyramidItem(this, 4, 0, 1, 1, 0, 0, 0, 3, null, mPyramidDim, mScale));
            mItems.get(3).get(0).set(3, new PyramidItem(this, 3, 0, 3, 1, 0, 1, 0, 2, null, mPyramidDim, mScale));
            mItems.get(5).get(0).set(3, new PyramidItem(this, 5, 0, 3, 1, 1, 0, 0, 2, null, mPyramidDim, mScale));
            mItems.get(2).get(0).set(5, new PyramidItem(this, 2, 0, 5, 1, 0, 2, 0, 1, null, mPyramidDim, mScale));
            mItems.get(4).get(0).set(5, new PyramidItem(this, 4, 0, 5, 1, 1, 1, 0, 1, null, mPyramidDim, mScale));
            mItems.get(6).get(0).set(5, new PyramidItem(this, 6, 0, 5, 1, 2, 0, 0, 1, null, mPyramidDim, mScale));
            mItems.get(1).get(0).set(7, new PyramidItem(this, 1, 0, 7, 1, 0, 3, 0, 0, null, mPyramidDim, mScale));
            mItems.get(3).get(0).set(7, new PyramidItem(this, 3, 0, 7, 1, 1, 2, 0, 0, null, mPyramidDim, mScale));
            mItems.get(5).get(0).set(7, new PyramidItem(this, 5, 0, 7, 1, 2, 1, 0, 0, null, mPyramidDim, mScale));
            mItems.get(7).get(0).set(7, new PyramidItem(this, 7, 0, 7, 1, 3, 0, 0, 0, null, mPyramidDim, mScale));
        }
    }

    public void CreateExVertices(int[] item, float[] point) {
        for (int x = 0; x < mItems.size(); x++) {
            for (int y = 0; y < mItems.get(x).size(); y++) {
                for (int z = 0; z < mItems.get(x).get(y).size(); z++) {
                    PyramidItem pi = mItems.get(x).get(y).get(z);
                    if (pi == null) continue;
                    pi.mNearItems = null;

                    if (x == item[0] && y == item[1] && z == item[2]) {
                        pi.CreateExVertices(point, item[3]);
                    }
                }
            }
        }
    }

    public Pair<int[], float[]> GetSelectItem(float[] start, float[] end/*, boolean bSelect*/)
    {
        float[] ray;
        float[] intersection;
        //float[] min_intersection = new float[4];
        float cosang, dist, lamda;
        //int surface_index = -1;

        ray = Surface.Sub(end, start);
        float min_dist = Float.MAX_VALUE;

        int[] res = {-1, -1, -1, -1};
        float[] min_intersection = {0, 0, 0};

        for (int x = 0; x < mItems.size(); x++) {
            for (int y = 0; y < mItems.get(x).size(); y++) {
                for (int z = 0; z < mItems.get(x).get(y).size(); z++) {

                    PyramidItem pi = mItems.get(x).get(y).get(z);
                    if (pi == null) continue;
                    //if (bSelect)
                    //    ci.m_bSelect = false;

                    for (int k = 0; k < pi.GetTriangleCount(); k++) {

                        float[] normal = pi.GetNormal(k);
                        float[] point1 = pi.GetPoint(k, 0);
                        float[] point2 = pi.GetPoint(k, 1);
                        float[] point3 = pi.GetPoint(k, 2);

                        cosang = Surface.Dot(ray, normal);
                        if (cosang > -0.0001 && cosang < 0.0001)
                            continue; // Determine if ray paralle to a plane.

                        float D = -Surface.Dot(normal, point1);
                        dist = Surface.Dot(start, normal);

                        lamda = -(D + dist) / cosang;
                        if (lamda < 0 || lamda > 1)
                            continue;

                        intersection = Surface.Add(start, Surface.Mul(ray, lamda));

                        if (Surface.InPolygon(point1, point2, point3, normal, intersection)) {
                            dist = Surface.Dist(intersection, Structures.CameraEye);
                            if (dist < min_dist) {
                                /*min_intersection[0] = intersection[0];
                                min_intersection[1] = intersection[1];
                                min_intersection[2] = intersection[2];*/
                                min_dist = dist;
                                res[0] = x;
                                res[1] = y;
                                res[2] = z;
                                res[3] = k; // индекс грани, т.к. каждая грань состоит из двух треугольников

                                min_intersection = Surface.Equal(intersection);
                            }
                        }
                    }
                }
            }
        }

        return new Pair<>(res, min_intersection);
    }

    public Action GetAction(int[] item, float[] start, float[] end/*, boolean bSelect*/)
    {
        float[] ray;
        float[] intersection;
        //float[] min_intersection = new float[4];
        float cosang, dist, lamda;
        //int surface_index = -1;

        Action a = new Action(true);

        ray = Surface.Sub(end, start);
        float min_dist = Float.MAX_VALUE;

        //int[] res = {-1, -1, -1, -1};

        if (item.length != 4) return null;
        if (item[0] < 0 || mItems.size() <= item[0])  return null;
        if (item[1] < 0 || mItems.get(item[0]).size() <= item[1])  return null;
        if (item[2] < 0 || mItems.get(item[0]).get(item[1]).size() <= item[2])  return null;

        PyramidItem pi = mItems.get(item[0]).get(item[1]).get(item[2]);
        if (pi == null || pi.mNearItems == null || !pi.mNearItems.containsKey(item[3])) return null;

        for (int k = 0; k < pi.mNearItems.get(item[3]).size(); k++) {
            TriangleItem ti = pi.mNearItems.get(item[3]).get(k);

            float[] normal = ti.GetNormal();
            float[] point1 = ti.GetPoint(0);
            float[] point2 = ti.GetPoint(1);
            float[] point3 = ti.GetPoint(2);

            cosang = Surface.Dot(ray, normal);
            if (cosang > -0.0001 && cosang < 0.0001)
                continue; // Determine if ray paralle to a plane.

            float D = -Surface.Dot(normal, point1);
            dist = Surface.Dot(start, normal);

            lamda = -(D + dist) / cosang;
            if (lamda < 0 || lamda > 1)
                continue;

            intersection = Surface.Add(start, Surface.Mul(ray, lamda));

            if (Surface.InPolygon(point1, point2, point3, normal, intersection)) {
                dist = Surface.Dist(intersection, Structures.CameraEye);
                if (dist < min_dist) {

                    min_dist = dist;
                    a.m_ActionAxisRotate = ti.m_AxisRotate;
                    a.m_ActionDirectRotate = ti.m_DirectRotate;

                    for (int i = 0; i < mPyramidDim; i ++) {
                        int val = item[0] * 1000 + item[1] * 100 + item[2] * 10 + i;

                        switch (ti.m_AxisRotate) {
                            case Structures.AXE_L:
                                if (mLSide.contains(val)) {
                                    a.m_ActionPosRotate = i;
                                    return a;
                                }
                                break;
                            case Structures.AXE_R:
                                if (mRSide.contains(val)) {
                                    a.m_ActionPosRotate = i;
                                    return a;
                                }
                                break;
                            case Structures.AXE_B:
                                if (mBSide.contains(val)) {
                                    a.m_ActionPosRotate = i;
                                    return a;
                                }
                                break;
                            case Structures.AXE_F:
                                if (mFSide.contains(val)) {
                                    a.m_ActionPosRotate = i;
                                    return a;
                                }
                                break;
                        }
                    }
                }
            }
        }

        return a;
    }

    public void SavePos(SharedPreferences.Editor ed) {
        for (int x = 0; x < mItems.size(); x++) {
            for (int y = 0; y < mItems.get(x).size(); y++) {
                for (int z = 0; z < mItems.get(x).get(y).size(); z++) {
                    PyramidItem pi = mItems.get(x).get(y).get(z);
                    if (pi != null) {
                        String str = "";
                        for (int i = 0; i < pi.verge_color_index.length; i++) {
                            str = str + String.valueOf(pi.verge_color_index[i]);
                        }
                        ed.putString("PyramidVertexColor" + String.valueOf(mPyramidDim) + String.valueOf(x) + String.valueOf(y) + String.valueOf(z), str);
                    }
                }
            }
        }
    }

    public void LoadPos(SharedPreferences sPref) {
        for (int x = 0; x < mItems.size(); x++) {
            for (int y = 0; y < mItems.get(x).size(); y++) {
                for (int z = 0; z < mItems.get(x).get(y).size(); z++) {
                    PyramidItem pi = mItems.get(x).get(y).get(z);
                    if (pi != null) {
                        String str = sPref.getString("PyramidVertexColor" + String.valueOf(mPyramidDim) + String.valueOf(x) + String.valueOf(y) + String.valueOf(z), "");
                        if (str != "") {
                            int[] verge_color_indexes_ = {Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK};
                            for (int i = 0; i < str.length(); i++)
                                verge_color_indexes_[i] = Integer.valueOf(str.substring(i, i + 1));
                            mItems.get(x).get(y).set(z, new PyramidItem(this, pi, verge_color_indexes_, mPyramidDim, mScale));
                        }
                    }
                }
            }
        }
    }

    public void ExchangeColors(int[][] rotate, Action a) {

        if (a.m_ActionPosRotate == mPyramidDim - 1) {
            int x = rotate[a.m_ActionPosRotate][0] / 100;
            int y = (rotate[a.m_ActionPosRotate][0] % 100) / 10;
            int z = (rotate[a.m_ActionPosRotate][0] % 10);

            PyramidItem pi = mItems.get(x).get(y).get(z);
            int[] verge_color_indexes_  = pi.verge_color_index.clone();
            PyramidItem.Rotate(verge_color_indexes_, a.m_ActionAxisRotate, a.m_ActionDirectRotate, pi.mType);
            mItems.get(x).get(y).set(z, new PyramidItem(this, pi, verge_color_indexes_, mPyramidDim, mScale));
        }
        else {

            int next_ind;
            HashMap<Integer, int[]> mapColors = new HashMap<>();

            for (int i = 0; i < rotate[a.m_ActionPosRotate].length; i++) {
                int x = rotate[a.m_ActionPosRotate][i] / 100;
                int y = (rotate[a.m_ActionPosRotate][i] % 100) / 10;
                int z = (rotate[a.m_ActionPosRotate][i] % 10);

                PyramidItem pi = mItems.get(x).get(y).get(z);

                int diff = (mPyramidDim - a.m_ActionPosRotate - 1) * 2;

                if (a.m_ActionDirectRotate == Structures.DIRECT_LEFT) {
                    next_ind = i + diff;
                    if (next_ind >= rotate[a.m_ActionPosRotate].length)
                        next_ind -= rotate[a.m_ActionPosRotate].length;
                } else {
                    next_ind = i - diff;
                    if (next_ind < 0) next_ind += rotate[a.m_ActionPosRotate].length;
                }

                int x2 = rotate[a.m_ActionPosRotate][next_ind] / 100;
                int y2 = (rotate[a.m_ActionPosRotate][next_ind] % 100) / 10;
                int z2 = (rotate[a.m_ActionPosRotate][next_ind] % 10);

                int[] verge_color_indexes_ = mItems.get(x2).get(y2).get(z2).verge_color_index.clone();
                if (mapColors.containsKey(x2 * 100 + y2 * 10 + z2)) {
                    verge_color_indexes_ = mapColors.get(x2 * 100 + y2 * 10 + z2).clone();
                }

                if (!mapColors.containsKey(x * 100 + y * 10 + z)) {
                    int[] verge_color_indexes = mItems.get(x).get(y).get(z).verge_color_index.clone();
                    mapColors.put(x * 100 + y * 10 + z, verge_color_indexes);
                }

                PyramidItem.Rotate(verge_color_indexes_, a.m_ActionAxisRotate, a.m_ActionDirectRotate, pi.mType);
                mItems.get(x).get(y).set(z, new PyramidItem(this, pi, verge_color_indexes_, mPyramidDim, mScale));
            }
        }
    }

    public boolean StoreItemPosition(Action a) {

        if (a == null) return false;
        if (a.m_ActionDirectRotate == Structures.DIRECT_NONE) return false;

        if (a.m_ActionAxisRotate == Structures.AXE_L) {

            ExchangeColors(rotateL[mPyramidDim - 2], a);
            if (mPyramidDim > 2 && a.m_ActionPosRotate == mPyramidDim - 2) {
                a.m_ActionPosRotate = mPyramidDim - 1;
                ExchangeColors(rotateL[mPyramidDim - 2], a);
            }
        }
        if (a.m_ActionAxisRotate == Structures.AXE_R) {

            ExchangeColors(rotateR[mPyramidDim - 2], a);
            if (mPyramidDim > 2 && a.m_ActionPosRotate == mPyramidDim - 2) {
                a.m_ActionPosRotate = mPyramidDim - 1;
                ExchangeColors(rotateR[mPyramidDim - 2], a);
            }
        }
        if (a.m_ActionAxisRotate == Structures.AXE_B) {

            ExchangeColors(rotateB[mPyramidDim - 2], a);
            if (mPyramidDim > 2 && a.m_ActionPosRotate == mPyramidDim - 2) {
                a.m_ActionPosRotate = mPyramidDim - 1;
                ExchangeColors(rotateB[mPyramidDim - 2], a);
            }
        }
        if (a.m_ActionAxisRotate == Structures.AXE_F) {

            ExchangeColors(rotateF[mPyramidDim - 2], a);
            if (mPyramidDim > 2 && a.m_ActionPosRotate == mPyramidDim - 2) {
                a.m_ActionPosRotate = mPyramidDim - 1;
                ExchangeColors(rotateF[mPyramidDim - 2], a);
            }
        }

        if (a.mFromUser) {
            return isComplete();
        }

        return false;
    }

    public boolean isComplete() {
        HashMap<Integer, Integer> mapColor = new HashMap<>();
        int color = -1;
        int surface_index = -1;
        for (int x = 0; x < mItems.size(); x++) {
            for (int y = 0; y < mItems.get(x).size(); y++) {
                for (int z = 0; z < mItems.get(x).get(y).size(); z++) {
                    PyramidItem pi = mItems.get(x).get(y).get(z);
                    if (pi != null) {
                        int val = x * 1000 + y * 100 + z * 10 + 0;
                        if (mLSide.contains(val)) {
                            if (pi.mType == 0)
                                color = pi.verge_color_index[2];
                            else
                                color = pi.verge_color_index[4];

                            surface_index = Structures.AXE_L;

                            if (!mapColor.containsKey(surface_index))
                                mapColor.put(surface_index, color);
                            else {
                                if (mapColor.get(surface_index) != color) return false;
                            }
                        }
                        if (mRSide.contains(val)) {
                            if (pi.mType == 0)
                                color = pi.verge_color_index[1];
                            else
                                color = pi.verge_color_index[6];

                            surface_index = Structures.AXE_R;

                            if (!mapColor.containsKey(surface_index))
                                mapColor.put(surface_index, color);
                            else {
                                if (mapColor.get(surface_index) != color) return false;
                            }
                        }
                        if (mBSide.contains(val)) {
                            if (pi.mType == 0)
                                color = pi.verge_color_index[3];
                            else
                                color = pi.verge_color_index[1];

                            surface_index = Structures.AXE_B;

                            if (!mapColor.containsKey(surface_index))
                                mapColor.put(surface_index, color);
                            else {
                                if (mapColor.get(surface_index) != color) return false;
                            }
                        }
                        if (mFSide.contains(val)) {
                            if (pi.mType == 0)
                                color = pi.verge_color_index[0];
                            else
                                color = pi.verge_color_index[0];

                            surface_index = Structures.AXE_F;

                            if (!mapColor.containsKey(surface_index))
                                mapColor.put(surface_index, color);
                            else {
                                if (mapColor.get(surface_index) != color) return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
