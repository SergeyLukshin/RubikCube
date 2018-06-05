package ru.adoon.rubikcube;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Лукшин on 10.11.2017.
 */

public class Cube {
    public volatile ArrayList<ArrayList<ArrayList<CubeItem>>> mItems = null;//new ArrayList<ArrayList<ArrayList<CubeItemOld>>>();
    public static int mTexture;
    public static final int TRIANGLE_COUNT_CUBE = 12;

    public static final int GREEN = 0;
    public static final int RED = 1;
    public static final int BLUE = 2;
    public static final int ORANGE = 3;
    public static final int WHITE = 4;
    public static final int YELLOW = 5;
    public static final int BLACK = 6;

    public int mCubeDimX = 3;
    public int mCubeDimY = 3;
    public int mCubeDimZ = 3;
    public float mScale = 0.8f;

    public boolean mEmpty = false;

    /*int[] rotateY = {0, 01, 02, 12, 22, 21, 20, 10, 0, 01};
    int[] rotateX = {0, 10, 20, 21, 22, 12, 02, 01, 0, 10};
    int[] rotateZ = {0, 10, 20, 21, 22, 12, 02, 01, 0, 10};

    int[] cubeFaceY = {0, 1, 2, 3, 0};
    int[] cubeFaceX = {0, 5, 2, 4, 0};
    int[] cubeFaceZ = {5, 1, 4, 3, 5};*/

    static final float h2 = 0f;
    public static final float[] vertex = {
            1 - h2, 1 - h2 , 1,
            -(1 - h2), 1 - h2, 1,
            -(1 - h2), -(1 - h2), 1,
            1 - h2, -(1 - h2), 1,

            1, 1 - h2, -(1 - h2),
            1, 1 - h2, 1 - h2,
            1, -(1 - h2), 1 - h2,
            1, -(1 - h2), -(1 - h2),

            -(1 - h2), 1 - h2, -1,
            1 - h2, 1 - h2, -1,
            1 - h2, -(1 - h2), -1,
            -(1 - h2), -(1 - h2), -1,

            -1, 1 - h2, 1 - h2,
            -1, 1 - h2, -(1 - h2),
            -1, -(1 - h2), -(1 - h2),
            -1, -(1 - h2), 1 - h2,

            -(1 - h2), 1, -(1 - h2),
            -(1 - h2), 1, 1 - h2,
            1 - h2, 1, 1 - h2,
            1 - h2, 1, -(1 - h2),

            1 - h2, -1, -(1 - h2),
            1 - h2, -1, 1 - h2,
            -(1 - h2), -1, 1 - h2,
            -(1 - h2), -1, -(1 - h2),
    };

    Cube(Context context) {

        mItems = new ArrayList<ArrayList<ArrayList<CubeItem>>>();
        mTexture = TextureUtils.loadTexture(context, R.drawable.cube);
    }

    public void SetContext(Context context) {
        TextureUtils.deleteTexture(mTexture);
        mTexture = TextureUtils.loadTexture(context, R.drawable.cube);
    }

    public synchronized void CubeInit(int CubeDimX, int CubeDimY, int CubeDimZ, boolean Empty) {
        mCubeDimX = CubeDimX;
        mCubeDimY = CubeDimY;
        mCubeDimZ = CubeDimZ;
        mEmpty = Empty;

        int MaxCubeDim = CubeDimX;
        if (CubeDimY >= CubeDimX && CubeDimY >= CubeDimZ) MaxCubeDim = CubeDimY;
        if (CubeDimZ >= CubeDimX && CubeDimZ >= CubeDimY) MaxCubeDim = CubeDimZ;

        if (MaxCubeDim == 2) mScale = 1.2f;
        if (MaxCubeDim == 3) mScale = 1.0f;
        if (MaxCubeDim == 4) mScale = 0.8f;
        if (MaxCubeDim == 5) mScale = 0.6f;

        if (mItems.size() == 0) {
            for (int x = 0; x <= mCubeDimX - 1; x++) {
                mItems.add(new ArrayList<ArrayList<CubeItem>>());
                for (int y = 0; y <= mCubeDimY - 1; y++) {
                    mItems.get(x).add(new ArrayList<CubeItem>());
                    for (int z = 0; z <= mCubeDimZ - 1; z++) {
                        if (x == 0 || x == mCubeDimX - 1 || y == 0 || y == mCubeDimY - 1 || z == 0 || z == mCubeDimZ - 1) // если скраю
                        {
                            if (mEmpty && ((x == 0 || x == mCubeDimX - 1) && (y + 1) % 2 == 0 && (z + 1) % 2 == 0 ||
                                    (y == 0 || y == mCubeDimX - 1) && (x + 1) % 2 == 0 && (z + 1) % 2 == 0 ||
                                    (z == 0 || z == mCubeDimX - 1) && (x + 1) % 2 == 0 && (y + 1) % 2 == 0))
                                mItems.get(x).get(y).add(new CubeItem(x/* - 1*/, y/* - 1*/, z/* - 1*/, null, mCubeDimX, mCubeDimY, mCubeDimZ, mScale, false));
                            else
                                mItems.get(x).get(y).add(new CubeItem(x/* - 1*/, y/* - 1*/, z/* - 1*/, null, mCubeDimX, mCubeDimY, mCubeDimZ, mScale, true));
                        }
                        else {
                            mItems.get(x).get(y).add(null);
                        }
                    }
                }
            }
        }
        else {
            for (int x = 0; x <= mCubeDimX - 1; x++) {
                mItems.add(new ArrayList<ArrayList<CubeItem>>());
                for (int y = 0; y <= mCubeDimY - 1; y++) {
                    mItems.get(x).add(new ArrayList<CubeItem>());
                    for (int z = 0; z <= mCubeDimZ - 1; z++) {
                        if (x == 0 || x == mCubeDimX - 1 || y == 0 || y == mCubeDimY - 1 || z == 0 || z == mCubeDimZ - 1) // если скраю
                        {
                            if (mEmpty && ((x == 0 || x == mCubeDimX - 1) && (y + 1) % 2 == 0 && (z + 1) % 2 == 0 ||
                                    (y == 0 || y == mCubeDimX - 1) && (x + 1) % 2 == 0 && (z + 1) % 2 == 0 ||
                                    (z == 0 || z == mCubeDimX - 1) && (x + 1) % 2 == 0 && (y + 1) % 2 == 0)) {
                                mItems.get(x).get(y).set(z, new CubeItem(x/* - 1*/, y/* - 1*/, z/* - 1*/, null, mCubeDimX, mCubeDimY, mCubeDimZ, mScale, false));
                            }
                            else
                                mItems.get(x).get(y).set(z, new CubeItem(x/* - 1*/, y/* - 1*/, z/* - 1*/, null, mCubeDimX, mCubeDimY, mCubeDimZ, mScale, true));
                        }
                    }
                }
            }
        }
    }

    public float[] CheckItemInAction(Action a, int posX, int posY, int posZ) {
        if (a.ActionIsEnable()) {
            if (a.m_ActionAxisRotate == Structures.AXE_X && a.m_ActionPosRotate == posX) return new float[]{1, 0, 0};
            if (a.m_ActionAxisRotate == Structures.AXE_Y && a.m_ActionPosRotate == posY) return new float[]{0, 1, 0};
            if (a.m_ActionAxisRotate == Structures.AXE_Z && a.m_ActionPosRotate == posZ) return new float[]{0, 0, 1};
        }
        return null;
    }

    public void Draw(Action a) {
        for (int x = 0; x < mItems.size(); x++) {
            for (int y = 0; y < mItems.get(x).size(); y++) {
                for (int z = 0; z < mItems.get(x).get(y).size(); z++) {
                    CubeItem ci = mItems.get(x).get(y).get(z);
                    if (ci != null) {
                        ci.InitMatrix();
                        if (a != null) {
                            float[] rotateType = CheckItemInAction(a, ci.mPosX, ci.mPosY, ci.mPosZ);
                            ci.RotateMatrix(a.mAngle, rotateType);
                        }
                        /*if (ci.mNearItems != null) {
                            Set<Integer> keys = ci.mNearItems.keySet();
                            for(int key: keys) {
                                for (int j = 0; j < ci.mNearItems.get(key).size(); j++) {
                                    ci.mNearItems.get(key).get(j).InitMatrix();
                                    ci.mNearItems.get(key).get(j).Draw();
                                }
                            }
                        }
                        else*/
                            ci.Draw();
                    }
                }
            }
        }
    }

    public void CreateExVertices(int[] item, float[] point) {
        for (int x = 0; x < mItems.size(); x++) {
            for (int y = 0; y < mItems.get(x).size(); y++) {
                for (int z = 0; z < mItems.get(x).get(y).size(); z++) {
                    CubeItem ci = mItems.get(x).get(y).get(z);
                    if (ci == null) continue;
                    ci.mNearItems = null;

                    if (x == item[0] && y == item[1] && z == item[2]) {
                        ci.CreateExVertices(point, item[3]);
                    }
                }
            }
        }
    }

    public boolean isComplete() {
        HashMap<Integer, Integer> mapColor = new HashMap<>();
        for (int x = 0; x < mItems.size(); x++) {
            for (int y = 0; y < mItems.get(x).size(); y++) {
                for (int z = 0; z < mItems.get(x).get(y).size(); z++) {
                    CubeItem ci = mItems.get(x).get(y).get(z);
                    if (ci != null && ci.m_bVisible) {
                        // проверка по оси z
                        int surface_x_index = -1;
                        int surface_y_index = -1;
                        int surface_z_index = -1;

                        if (z == mCubeDimZ - 1) surface_z_index = 0;
                        if (z == 0) surface_z_index = 2;

                        if (x == mCubeDimX - 1) surface_x_index = 1;
                        if (x == 0) surface_x_index = 3;

                        if (y == mCubeDimY - 1) surface_y_index = 4;
                        if (y == 0) surface_y_index = 5;

                        int color_x = -1;
                        int color_y = -1;
                        int color_z = -1;
                        if (surface_z_index >= 0) {
                            color_z = ci.verge_color_index[surface_z_index];
                            if (!mapColor.containsKey(surface_z_index)) mapColor.put(surface_z_index, color_z);
                            else {
                                if (mapColor.get(surface_z_index) != color_z) return false;
                            }
                        }
                        if (surface_x_index >= 0) {
                            color_x = ci.verge_color_index[surface_x_index];
                            if (!mapColor.containsKey(surface_x_index)) mapColor.put(surface_x_index, color_x);
                            else {
                                if (mapColor.get(surface_x_index) != color_x) return false;
                            }
                        }
                        if (surface_y_index >= 0) {
                            color_y = ci.verge_color_index[surface_y_index];
                            if (!mapColor.containsKey(surface_y_index)) mapColor.put(surface_y_index, color_y);
                            else {
                                if (mapColor.get(surface_y_index) != color_y) return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean isEqualDim() {
        return (mCubeDimX == mCubeDimY && mCubeDimY == mCubeDimZ);
    }

    public boolean StoreItemPosition(Action a) {

        if (a == null) return false;
        if (a.m_ActionDirectRotate == Structures.DIRECT_NONE) return false;

        int dimX = mCubeDimX;
        int dimY = mCubeDimY;
        int dimZ = mCubeDimZ;
        int iter = 0;
        while (true) {
            ArrayList<CubeItem> list = new ArrayList<CubeItem>();
            int x = 0;
            int y = 0;
            int z = 0;
            boolean bVisible;
            int dif = 0;

            if (a.m_ActionAxisRotate == Structures.AXE_X) {
                x = a.m_ActionPosRotate;// + 1;

                if (dimY <= 1 || dimZ <= 1) break;

                int cnt = dimY * dimZ - (dimY - 2) * (dimZ - 2);
                if (isEqualDim()) dif = dimX - 1;
                else dif = dimY - 1 + dimZ - 1;

                y = iter;
                z = mCubeDimZ - 1 - iter;

                for (int i = 0; i < cnt; i++) {

                    list.add(mItems.get(x).get(y).get(z));

                    if (i > 0 && y == iter) {
                        z++;
                        continue;
                    }
                    if (z == iter) {
                        y--;
                        continue;
                    }
                    if (y == mCubeDimY - 1 - iter) {
                        z--;
                        continue;
                    }
                    if (z == mCubeDimZ - 1 - iter) {
                        y++;
                    }
                }
            }

            if (a.m_ActionAxisRotate == Structures.AXE_Y) {
                y = a.m_ActionPosRotate;// + 1;

                if (dimX <= 1 || dimZ <= 1) break;

                int cnt = dimX * dimZ - (dimX - 2) * (dimZ - 2);
                if (isEqualDim()) dif = dimX - 1;
                else dif = dimX - 1 + dimZ - 1;

                x = mCubeDimX - 1 - iter;
                z = iter;

                for (int i = 0; i < cnt; i++) {

                    list.add(mItems.get(x).get(y).get(z));

                    if (i > 0 && z == iter) {
                        x++;
                        continue;
                    }
                    if (x == iter) {
                        z--;
                        continue;
                    }
                    if (z == mCubeDimZ - 1 - iter) {
                        x--;
                        continue;
                    }
                    if (x == mCubeDimX - 1 - iter) {
                        z++;
                    }
                }
            }

            if (a.m_ActionAxisRotate == Structures.AXE_Z) {
                z = a.m_ActionPosRotate;// + 1;

                if (dimX <= 1 || dimY <= 1) break;

                int cnt = dimX * dimY - (dimX - 2) * (dimY - 2);
                if (isEqualDim()) dif = dimX - 1;
                else dif = dimX - 1 + dimY - 1;

                x = iter;
                y = mCubeDimY - 1 - iter;

                for (int i = 0; i < cnt; i++) {

                    list.add(mItems.get(x).get(y).get(z));

                    if (i > 0 && x == iter) {
                        y++;
                        continue;
                    }
                    if (y == iter) {
                        x--;
                        continue;
                    }
                    if (x == mCubeDimX - 1 - iter) {
                        y--;
                        continue;
                    }
                    if (y == mCubeDimY - 1 - iter) {
                        x++;
                    }
                }
            }

            if (list.get(0) == null)
                break;

            int[] verge_color_indexes_first = list.get(0).verge_color_index.clone();
            int ind = 0;
            for (int i = 0; i < list.size(); i++) {

                if (list.get(i) == null)
                    continue;

                x = list.get(i).mPosX;// + 1;
                y = list.get(i).mPosY;// + 1;
                z = list.get(i).mPosZ;// + 1;
                bVisible = list.get(i).m_bVisible;// + 1;

                if (a.m_ActionDirectRotate == Structures.DIRECT_LEFT) {
                    ind = i + dif;
                    if (ind >= list.size()) ind -= list.size();
                }
                if (a.m_ActionDirectRotate == Structures.DIRECT_RIGHT) {
                    ind = i - dif;
                    if (ind < 0) ind += list.size();
                }

                int[] verge_color_indexes_ = list.get(ind).verge_color_index.clone();
                if (ind == 0) verge_color_indexes_ = verge_color_indexes_first;

                CubeItem.Rotate(verge_color_indexes_, a.m_ActionAxisRotate, a.m_ActionDirectRotate, isEqualDim());
                mItems.get(x).get(y).set(z, new CubeItem(x, y, z, verge_color_indexes_, mCubeDimX, mCubeDimY, mCubeDimZ, mScale, bVisible));
                //mItems.get(x).get(y).set(z, new CubeItem(x - 1, y - 1, z - 1, verge_color_indexes_, CubeDim));
            }

            //if (dim <= 3) break;
            dimX -= 2;
            dimY -= 2;
            dimZ -= 2;
            iter++;
        }

        // отдельно обрабатываем единичные элементы
        {
            ArrayList<CubeItem> list = new ArrayList<CubeItem>();
            if ((dimY > 1 || dimZ > 1) && a.m_ActionAxisRotate == Structures.AXE_X) {
                int x = a.m_ActionPosRotate;
                if (dimY == 1) {
                    for (int z = 0; z < dimZ; z++) {
                        list.add(mItems.get(x).get(iter).get(z + iter));
                    }
                }
                if (dimZ == 1) {
                    for (int y = 0; y < dimY; y++) {
                        list.add(mItems.get(x).get(y + iter).get(iter));
                    }
                }
            }
            if ((dimX > 1 || dimZ > 1) && a.m_ActionAxisRotate == Structures.AXE_Y) {
                int y = a.m_ActionPosRotate;
                if (dimX == 1) {
                    for (int z = 0; z < dimZ; z++) {
                        list.add(mItems.get(iter).get(y).get(z + iter));
                    }
                }
                if (dimZ == 1) {
                    for (int x = 0; x < dimX; x++) {
                        list.add(mItems.get(x + iter).get(y).get(iter));
                    }
                }
            }
            if ((dimX > 1 || dimY > 1) && a.m_ActionAxisRotate == Structures.AXE_Z) {
                int z = a.m_ActionPosRotate;
                if (dimX == 1) {
                    for (int y = 0; y < dimY; y++) {
                        list.add(mItems.get(iter).get(y + iter).get(z));
                    }
                }
                if (dimY == 1) {
                    for (int x = 0; x < dimX; x++) {
                        list.add(mItems.get(x + iter).get(iter).get(z));
                    }
                }
            }

            for (int i = 0; i < list.size() / 2; i++) {
                if (list.get(i) == null)
                    continue;

                int x1 = list.get(i).mPosX;// + 1;
                int y1 = list.get(i).mPosY;// + 1;
                int z1 = list.get(i).mPosZ;// + 1;
                boolean bVisible1 = list.get(i).m_bVisible;

                int x2 = list.get(list.size() - i - 1).mPosX;// + 1;
                int y2 = list.get(list.size() - i - 1).mPosY;// + 1;
                int z2 = list.get(list.size() - i - 1).mPosZ;// + 1;
                boolean bVisible2 = list.get(list.size() - i - 1).m_bVisible;

                int[] verge_color_indexes1_ = list.get(i).verge_color_index.clone();
                int[] verge_color_indexes2_ = list.get(list.size() - i - 1).verge_color_index.clone();

                CubeItem.Rotate(verge_color_indexes1_, a.m_ActionAxisRotate, a.m_ActionDirectRotate, isEqualDim());
                CubeItem.Rotate(verge_color_indexes2_, a.m_ActionAxisRotate, a.m_ActionDirectRotate, isEqualDim());

                mItems.get(x1).get(y1).set(z1, new CubeItem(x1, y1, z1, verge_color_indexes2_, mCubeDimX, mCubeDimY, mCubeDimZ, mScale, bVisible1));
                mItems.get(x2).get(y2).set(z2, new CubeItem(x2, y2, z2, verge_color_indexes1_, mCubeDimX, mCubeDimY, mCubeDimZ, mScale, bVisible2));
            }

            // средняя кнопка
            if ((list.size() / 2) * 2 != list.size()) {
                int index = list.size() / 2;
                int x = list.get(index).mPosX;// + 1;
                int y = list.get(index).mPosY;// + 1;
                int z = list.get(index).mPosZ;// + 1;
                boolean bVisible = list.get(index).m_bVisible;

                int[] verge_color_indexes_ = list.get(index).verge_color_index.clone();
                CubeItem.Rotate(verge_color_indexes_, a.m_ActionAxisRotate, a.m_ActionDirectRotate, isEqualDim());
                mItems.get(x).get(y).set(z, new CubeItem(x, y, z, verge_color_indexes_, mCubeDimX, mCubeDimY, mCubeDimZ, mScale, bVisible));
            }
        }

        if (a.mFromUser) {
            return isComplete();
        }

        return false;
    }

    /*public int CalcDirection(int[] rotate, int[] cubeFace, int[] CubeItem1, int[] CubeItem2, int ind1, int ind2) {

        if (CubeItem1[ind1] == CubeItem2[ind1] && CubeItem1[ind2] == CubeItem2[ind2]) {
            for (int i = 0; i < cubeFace.length - 1; i++) {
                if (CubeItem1[3] == cubeFace[i] && CubeItem2[3] == cubeFace[i + 1]) return Structures.DIRECT_LEFT;
            }
            for (int i = 1; i < cubeFace.length; i++) {
                if (CubeItem1[3] == cubeFace[i] && CubeItem2[3] == cubeFace[i - 1]) return Structures.DIRECT_RIGHT;
            }
        }
        else {
            for (int i = 0; i < rotate.length - 2; i++) {
                int val1 = CubeItem1[ind1] * 10 + CubeItem1[ind2];
                int val2 = CubeItem2[ind1] * 10 + CubeItem2[ind2];

                if (rotate[i] == val1 && (rotate[i + 1] == val2 || rotate[i + 2] == val2)) return Structures.DIRECT_LEFT;
            }

            for (int i = 2; i < rotate.length; i++) {
                int val1 = CubeItem1[ind1] * 10 + CubeItem1[ind2];
                int val2 = CubeItem2[ind1] * 10 + CubeItem2[ind2];

                if (rotate[i] == val1 && (rotate[i - 1] == val2 || rotate[i - 2] == val2)) return Structures.DIRECT_RIGHT;
            }
        }
        return Structures.DIRECT_NONE;
    }*/

    /*public Action GetAction(int[] CubeItem1, int[] CubeItem2) {
        if (CubeItem1[1] == CubeItem2[1] && CubeItem1[3] != 4  && CubeItem1[3] != 5 && CubeItem2[3] != 4  && CubeItem2[3] != 5) {
            Action a = new Action(true);
            a.m_ActionDirectRotate = Structures.DIRECT_NONE;
            a.m_ActionDirectRotate = CalcDirection(rotateY, cubeFaceY, CubeItem1, CubeItem2, 0, 2);

            if (a.m_ActionDirectRotate != Structures.DIRECT_NONE) {
                a.m_ActionAxisRotate = Structures.AXE_Y;
                a.m_ActionPosRotate = CubeItem1[1] - 1;

                return a;
            }
            return null;
        }

        if (CubeItem1[0] == CubeItem2[0] && CubeItem1[3] != 1  && CubeItem1[3] != 3 && CubeItem2[3] != 1  && CubeItem2[3] != 3) {
            Action a = new Action(true);
            a.m_ActionDirectRotate = Structures.DIRECT_NONE;
            a.m_ActionDirectRotate = CalcDirection(rotateX, cubeFaceX, CubeItem1, CubeItem2, 1, 2);

            if (a.m_ActionDirectRotate != Structures.DIRECT_NONE) {
                a.m_ActionAxisRotate = Structures.AXE_X;
                a.m_ActionPosRotate = CubeItem1[0] - 1;

                return a;
            }
            return null;
        }

        if (CubeItem1[2] == CubeItem2[2] && CubeItem1[3] != 0  && CubeItem1[3] != 2 && CubeItem2[3] != 0  && CubeItem2[3] != 2) {
            Action a = new Action(true);
            a.m_ActionDirectRotate = Structures.DIRECT_NONE;
            a.m_ActionDirectRotate = CalcDirection(rotateZ, cubeFaceZ, CubeItem1, CubeItem2, 0, 1);

            if (a.m_ActionDirectRotate != Structures.DIRECT_NONE) {
                a.m_ActionAxisRotate = Structures.AXE_Z;
                a.m_ActionPosRotate = CubeItem1[2] - 1;

                return a;
            }
            return null;
        }
        return null;
    }*/

    public int GetLimitAngle() {
        if (isEqualDim())
            return 90;
        else
            return 180;
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

        CubeItem pi = mItems.get(item[0]).get(item[1]).get(item[2]);
        int vertex = item[3] / 2;

        if (pi == null || pi.mNearItems == null || !pi.mNearItems.containsKey(vertex)) return null;
        for (int k = 0; k < pi.mNearItems.get(vertex).size(); k++) {
            TriangleItem ti = pi.mNearItems.get(vertex).get(k);

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

                    if (a.m_ActionAxisRotate == Structures.AXE_X) a.m_ActionPosRotate = item[0] /*- 1*/;
                    if (a.m_ActionAxisRotate == Structures.AXE_Y) a.m_ActionPosRotate = item[1] /*- 1*/;
                    if (a.m_ActionAxisRotate == Structures.AXE_Z) a.m_ActionPosRotate = item[2] /*- 1*/;
                }
            }
        }

        return a;
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

                    CubeItem ci = mItems.get(x).get(y).get(z);
                    if (ci == null) continue;
                    //if (bSelect)
                    //    ci.m_bSelect = false;

                    for (int k = 0; k < ci.GetTriangleCount(); k++) {

                        float[] normal = ci.GetNormal(k);
                        float[] point1 = ci.GetPoint(k, 0);
                        float[] point2 = ci.GetPoint(k, 1);
                        float[] point3 = ci.GetPoint(k, 2);

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
                                res[3] = k; // индекс треугольника

                                min_intersection = Surface.Equal(intersection);
                            }
                        }
                    }
                }
            }
        }

        /*if (bSelect) {
            if (res[0] >= 0 && res[1] >= 0 && res[2] >= 0) {
                CubeItemOld ci = mRender.data.mItems.get(res[0]).get(res[1]).get(res[2]);
                ci.m_bSelect = true;

                //mRender.mPoint = new CubeItemOld(min_intersection, 0.2f);
            }
        }*/

        return new Pair<>(res, min_intersection);
    }

    public void SavePos(SharedPreferences.Editor ed) {
        for (int x = 0; x < mItems.size(); x++) {
            for (int y = 0; y < mItems.get(x).size(); y++) {
                for (int z = 0; z < mItems.get(x).get(y).size(); z++) {
                    CubeItem ci = mItems.get(x).get(y).get(z);
                    if (ci != null) {
                        String str = "";
                        for (int i = 0; i < ci.verge_color_index.length; i++) {
                            str = str + String.valueOf(ci.verge_color_index[i]);
                        }
                        ed.putString("CubeVertexColor" + String.valueOf(mEmpty) + String.valueOf(mCubeDimX) + String.valueOf(mCubeDimY) + String.valueOf(mCubeDimZ) + String.valueOf(x) + String.valueOf(y) + String.valueOf(z), str);
                    }
                }
            }
        }
    }

    public void LoadPos(SharedPreferences sPref) {
        for (int x = 0; x < mItems.size(); x++) {
            for (int y = 0; y < mItems.get(x).size(); y++) {
                for (int z = 0; z < mItems.get(x).get(y).size(); z++) {
                    CubeItem ci = mItems.get(x).get(y).get(z);
                    if (ci != null) {
                        String str = sPref.getString("CubeVertexColor" + String.valueOf(mEmpty) + String.valueOf(mCubeDimX) + String.valueOf(mCubeDimY) + String.valueOf(mCubeDimZ) + String.valueOf(x) + String.valueOf(y) + String.valueOf(z), "");
                        if (str != "") {
                            int[] verge_color_indexes_ = {Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK, Cube.BLACK};
                            for (int i = 0; i < str.length(); i++)
                                verge_color_indexes_[i] = Integer.valueOf(str.substring(i, i + 1));
                            mItems.get(x).get(y).set(z, new CubeItem(x/* - 1*/, y/* - 1*/, z/* - 1*/, verge_color_indexes_, mCubeDimX, mCubeDimY, mCubeDimZ, mScale, ci.m_bVisible));
                        }
                    }
                }
            }
        }
    }
}
