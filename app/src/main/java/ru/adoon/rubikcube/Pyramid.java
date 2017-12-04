package ru.adoon.rubikcube;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import java.util.ArrayList;
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

    public static Set<Integer> mLSide = new HashSet<>();
    public static Set<Integer> mRSide = new HashSet<>();
    public static Set<Integer> mBSide = new HashSet<>();
    public static Set<Integer> mFSide = new HashSet<>();

    public static final int GREEN = 0;
    public static final int RED = 1;
    public static final int BLUE = 2;
    public static final int YELLOW = 3;
    public static final int BLACK = 4;

    public static float h = (float)Math.sqrt(2 / 3f);
    public static float h2 = (float)Math.sqrt(3f) / 2f;
    public static float h2_3 = h2 / 3f;
    public static float radius = (3 * h * h + 1) / (6 * h);//(float)Math.sqrt(6f) / 4f;
    public static float H = 3 * (float)Math.sqrt(2 / 3f);
    public static float Radius = (3 * H * H + 1) / (6 * H); // радиус большой окружности описанной около всей пирамиды
    public static float angle = (float)Math.asin(2 * Math.sqrt(2) / 3);
    //public static float shift_y = radius - h / 2;

    int[][] rotateL = {{222,212,211,201,200,201,102,103,4,  103,113,212},{313,303,302,303,204,303}, {404}};
    int[][] rotateR = {{222,212,211,201,200,201,302,303,404,303,313,212},{113,103,102,103,204,103}, {4}};
    int[][] rotateB = {{200,201,302,303,404,303,204,103,4,  103,102,201},{211,212,313,212,113,212}, {222}};
    int[][] rotateF = {{222,212,313,303,404,303,204,103,4,  103,113,212},{211,201,302,201,102,201}, {200}};

    /*int[][] rotateL = {{2221,2121,2111,2011,2001,2003,2013,1023,1033,43,  40,  1030,1130,2120,2220},{3131,3031,3021,3023,3033,2043,2040,3030,3130},{4041,4043,4040}};
    int[][] rotateR = {{2222,2122,2112,2012,2002,2003,2013,3023,3033,4043,4040,3030,3130,2120,2220},{1132,1032,1022,1023,1033,2043,2040,1030,1130},{42,  43,  40}};
    int[][] rotateB = {{2001,2011,3021,3031,4041,4040,3030,2040,1030,40  ,42  ,1032,1022,2012,2002},{2111,2121,3131,3130,2120,1130,1132,2122,2112},{2222,2221,2220}};
    int[][] rotateF = {{2221,2121,3131,3031,4041,4043,3033,2043,1033,43  ,42  ,1032,1132,2122,2222},{2111,2011,3021,3023,2013,1023,1022,2012,2112},{2002,2001,2003}};
    */

    /*int[] pyrFaceL = {0, 1, 3, 0};
    int[] pyrFaceR = {0, 2, 3, 0};
    int[] pyrFaceB = {0, 1, 2, 0};
    int[] pyrFaceF = {1, 2, 3, 1};*/

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

        // xyzr - r - ряд
    }

    public void SetContext(Context context) {
        TextureUtils.deleteTexture(mTexture);
        mTexture = TextureUtils.loadTexture(context, R.drawable.pyramid);
    }

    public float[] CheckItemInAction(Action a, int posX, int posY, int posZ) {
        if (a.ActionIsEnable()) {
            int val = posX * 1000 + posY * 100 + posZ * 10 + a.m_ActionPosRotate;
            if (a.m_ActionAxisRotate == Structures.AXE_B && Pyramid.mBSide.contains(val)) return new float[]{0, 1, 0};
            if (a.m_ActionAxisRotate == Structures.AXE_L && Pyramid.mLSide.contains(val)) {
                float angleY = 30;
                float angleZ = 90 - (float) Math.toDegrees(Math.atan(Math.sqrt(2) * 2));
                return new float[]{1, angleY, angleZ};
            }
            if (a.m_ActionAxisRotate == Structures.AXE_R && Pyramid.mRSide.contains(val)) {
                float angleY = -30;
                float angleZ = -90 + (float) Math.toDegrees(Math.atan(Math.sqrt(2) * 2));
                return new float[]{1, angleY, angleZ};
            }
            if (a.m_ActionAxisRotate == Structures.AXE_F && Pyramid.mFSide.contains(val)) {
                float angleX = 90 - (float) Math.toDegrees(Math.atan(Math.sqrt(2) * 2));
                return new float[]{angleX, 0, 1};
            }
            if (a.m_ActionPosRotate == 1) {
                val = posX * 1000 + posY * 100 + posZ * 10 + 2;
                if (a.m_ActionAxisRotate == Structures.AXE_B && Pyramid.mBSide.contains(val)) return new float[]{0, 1, 0};
                if (a.m_ActionAxisRotate == Structures.AXE_L && Pyramid.mLSide.contains(val)) {
                    float angleY = 30;
                    float angleZ = 90 - (float) Math.toDegrees(Math.atan(Math.sqrt(2) * 2));
                    return new float[]{1, angleY, angleZ};
                }
                if (a.m_ActionAxisRotate == Structures.AXE_R && Pyramid.mRSide.contains(val)) {
                    float angleY = -30;
                    float angleZ = -90 + (float) Math.toDegrees(Math.atan(Math.sqrt(2) * 2));
                    return new float[]{1, angleY, angleZ};
                }
                if (a.m_ActionAxisRotate == Structures.AXE_F && Pyramid.mFSide.contains(val)) {
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

    public synchronized void PyramidInit() {
        if (mItems.size() == 0) {
            for (int x = 0; x <= 5; x++) {
                mItems.add(new ArrayList<ArrayList<PyramidItem>>());
                for (int y = 0; y <= 3; y++) {
                    mItems.get(x).add(new ArrayList<PyramidItem>());
                    for (int z = 0; z <= 5; z++) {
                        mItems.get(x).get(y).add(null);
                    }
                }
            }
        }

        //mItems.get(2).get(2).set(2, new PyramidItem(2, 1, 2, 1, 0, 0, 2, 0, null));

        mItems.get(2).get(2).set(2, new PyramidItem(2, 2, 2, 0, 0, 0, 2, 0, null));

        mItems.get(2).get(1).set(1, new PyramidItem(2, 1, 1, 0, 0, 0, 1, 1, null));
        mItems.get(1).get(1).set(3, new PyramidItem(1, 1, 3, 0, 0, 1, 1, 0, null));
        mItems.get(3).get(1).set(3, new PyramidItem(3, 1, 3, 0, 1, 0, 1, 0, null));
        mItems.get(2).get(1).set(2, new PyramidItem(2, 1, 2, 1, 0, 0, 1, 0, null));

        mItems.get(2).get(0).set(0, new PyramidItem(2, 0, 0, 0, 0, 0, 0, 2, null));
        mItems.get(1).get(0).set(2, new PyramidItem(1, 0, 2, 0, 0, 1, 0, 1, null));
        mItems.get(3).get(0).set(2, new PyramidItem(3, 0, 2, 0, 1, 0, 0, 1, null));
        mItems.get(0).get(0).set(4, new PyramidItem(0, 0, 4, 0, 0, 2, 0, 0, null));
        mItems.get(2).get(0).set(4, new PyramidItem(2, 0, 4, 0, 1, 1, 0, 0, null));
        mItems.get(4).get(0).set(4, new PyramidItem(4, 0, 4, 0, 2, 0, 0, 0, null));
        mItems.get(2).get(0).set(1, new PyramidItem(2, 0, 1, 1, 0, 0, 0, 1, null));
        mItems.get(1).get(0).set(3, new PyramidItem(1, 0, 3, 1, 0, 1, 0, 0, null));
        mItems.get(3).get(0).set(3, new PyramidItem(3, 0, 3, 1, 1, 0, 0, 0, null));
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

        int[] res = {-1, -1, -1, -1};
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

                    for (int i = 0; i < 3; i ++) {
                        int val = item[0] * 1000 + item[1] * 100 + item[2] * 10 + i;

                        switch (ti.m_AxisRotate) {
                            case Structures.AXE_L:
                                if (Pyramid.mLSide.contains(val)) {
                                    a.m_ActionPosRotate = i;
                                    return a;
                                }
                                break;
                            case Structures.AXE_R:
                                if (Pyramid.mRSide.contains(val)) {
                                    a.m_ActionPosRotate = i;
                                    return a;
                                }
                                break;
                            case Structures.AXE_B:
                                if (Pyramid.mBSide.contains(val)) {
                                    a.m_ActionPosRotate = i;
                                    return a;
                                }
                                break;
                            case Structures.AXE_F:
                                if (Pyramid.mFSide.contains(val)) {
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
                        ed.putString("PyramidVertexColor" + String.valueOf(x) + String.valueOf(y) + String.valueOf(z), str);
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
                        String str = sPref.getString("PyramidVertexColor" + String.valueOf(x) + String.valueOf(y) + String.valueOf(z), "");
                        if (str != "") {
                            int[] verge_color_indexes_ = {Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK};
                            for (int i = 0; i < str.length(); i++)
                                verge_color_indexes_[i] = Integer.valueOf(str.substring(i, i + 1));
                            mItems.get(x).get(y).set(z, new PyramidItem(pi, verge_color_indexes_));
                        }
                    }
                }
            }
        }
    }

    public void ExchangeColors(int[][] rotate, Action a) {

        if (a.m_ActionPosRotate == 2) {
            int x = rotate[a.m_ActionPosRotate][0] / 100;
            int y = (rotate[a.m_ActionPosRotate][0] % 100) / 10;
            int z = (rotate[a.m_ActionPosRotate][0] % 10);

            PyramidItem pi = mItems.get(x).get(y).get(z);
            int[] verge_color_indexes_  = pi.verge_color_index.clone();
            PyramidItem.Rotate(verge_color_indexes_, a.m_ActionAxisRotate, a.m_ActionDirectRotate, pi.mType);
            mItems.get(x).get(y).set(z, new PyramidItem(pi, verge_color_indexes_));
        }
        else {

            int next_ind;
            HashMap<Integer, int[]> mapColors = new HashMap<>();

            for (int i = 0; i < rotate[a.m_ActionPosRotate].length; i++) {
                int x = rotate[a.m_ActionPosRotate][i] / 100;
                int y = (rotate[a.m_ActionPosRotate][i] % 100) / 10;
                int z = (rotate[a.m_ActionPosRotate][i] % 10);

                PyramidItem pi = mItems.get(x).get(y).get(z);

                if (a.m_ActionDirectRotate == Structures.DIRECT_LEFT) {
                    next_ind = i + (a.m_ActionPosRotate == 0 ? 4 : 2);
                    if (next_ind >= rotate[a.m_ActionPosRotate].length)
                        next_ind -= rotate[a.m_ActionPosRotate].length;
                } else {
                    next_ind = i - (a.m_ActionPosRotate == 0 ? 4 : 2);
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
                mItems.get(x).get(y).set(z, new PyramidItem(pi, verge_color_indexes_));
            }
        }
    }

    public boolean StoreItemPosition(Action a) {

        if (a == null) return false;
        if (a.m_ActionDirectRotate == Structures.DIRECT_NONE) return false;

        if (a.m_ActionAxisRotate == Structures.AXE_L) {
            ExchangeColors(rotateL, a);
            if (a.m_ActionPosRotate == 1) {
                a.m_ActionPosRotate = 2;
                ExchangeColors(rotateL, a);
            }
        }
        if (a.m_ActionAxisRotate == Structures.AXE_R) {
            ExchangeColors(rotateR, a);
            if (a.m_ActionPosRotate == 1) {
                a.m_ActionPosRotate = 2;
                ExchangeColors(rotateR, a);
            }
        }
        if (a.m_ActionAxisRotate == Structures.AXE_B) {
            ExchangeColors(rotateB, a);
            if (a.m_ActionPosRotate == 1) {
                a.m_ActionPosRotate = 2;
                ExchangeColors(rotateB, a);
            }
        }
        if (a.m_ActionAxisRotate == Structures.AXE_F) {
            ExchangeColors(rotateF, a);
            if (a.m_ActionPosRotate == 1) {
                a.m_ActionPosRotate = 2;
                ExchangeColors(rotateF, a);
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
