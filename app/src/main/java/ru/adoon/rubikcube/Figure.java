package ru.adoon.rubikcube;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Лукшин on 22.11.2017.
 */

public class Figure {
    private ArrayList<Cube> mCubeList = null;
    private ArrayList<Pyramid> mPyramidList = null;
    private ArrayList<Cube> mDominoCubeList = null;
    public int mFigureType = Structures.CUBE;
    public int mFigureSubType = 1;
    public int mCntCubes = 4;
    public int mCntPyramides = 3;
    public int mCntDominoCubes = 6;

    Figure(Context context) {
        //mCube = new Cube(context);
        mCubeList = new ArrayList<Cube>();
        for (int i = 0; i < mCntCubes; i++) {
            mCubeList.add(new Cube(context));
        }

        mPyramidList = new ArrayList<Pyramid>();
        for (int i = 0; i < mCntPyramides; i++) {
            mPyramidList.add(new Pyramid(context));
        }

        mDominoCubeList = new ArrayList<Cube>();
        for (int i = 0; i < mCntDominoCubes; i++) {
            mDominoCubeList.add(new Cube(context));
        }
    }

    public void SetFigure(int type, int sub_type) {
        mFigureType = type;
        mFigureSubType = sub_type;
        FigureInit();
    }

    public int GetDim(int FigureType, int axis) {
        if (FigureType == Structures.CUBE) {
            switch (axis) {
                case Structures.AXE_X:
                    return mCubeList.get(mFigureSubType).mCubeDimX;
                case Structures.AXE_Y:
                    return mCubeList.get(mFigureSubType).mCubeDimY;
                case Structures.AXE_Z:
                    return mCubeList.get(mFigureSubType).mCubeDimZ;
            }
        }
        if (FigureType == Structures.PYRAMID) {
                return mPyramidList.get(mFigureSubType).mPyramidDim;
        }
        if (FigureType == Structures.DOMINO_CUBE) {
            switch (axis) {
                case Structures.AXE_X:
                    return mDominoCubeList.get(mFigureSubType).mCubeDimX;
                case Structures.AXE_Y:
                    return mDominoCubeList.get(mFigureSubType).mCubeDimY;
                case Structures.AXE_Z:
                    return mDominoCubeList.get(mFigureSubType).mCubeDimZ;
            }
        }
        return 0;
    }

    public void FigureInit() {
        if (mFigureType == Structures.CUBE) {
            for (int i = 0; i < mCntCubes; i++)
                mCubeList.get(i).CubeInit(i + 2, i + 2, i + 2);
        }
        if (mFigureType == Structures.PYRAMID) {
            for (int i = 0; i < mCntPyramides; i++)
                mPyramidList.get(i).PyramidInit(i + 2);
        }
        if (mFigureType == Structures.DOMINO_CUBE) {
            for (int i = 0; i < mCntDominoCubes; i++)
                switch(i) {
                    case 0:
                        mDominoCubeList.get(i).CubeInit(3, 2, 3);
                        break;
                    case 1:
                        mDominoCubeList.get(i).CubeInit(4, 2, 4);
                        break;
                    case 2:
                        mDominoCubeList.get(i).CubeInit(4, 3, 4);
                        break;
                    case 3:
                        mDominoCubeList.get(i).CubeInit(5, 2, 5);
                        break;
                    case 4:
                        mDominoCubeList.get(i).CubeInit(5, 3, 5);
                        break;
                    case 5:
                        mDominoCubeList.get(i).CubeInit(5, 4, 5);
                        break;
                }
        }
    }

    public void SetContext(Context context) {
        for (int i = 0; i < mCntCubes; i++)
            mCubeList.get(i).SetContext(context);
        for (int i = 0; i < mCntPyramides; i++)
            mPyramidList.get(i).SetContext(context);
        for (int i = 0; i < mCntDominoCubes; i++)
            mDominoCubeList.get(i).SetContext(context);
    }

    public void SavePos(SharedPreferences.Editor ed) {
        for (int i = 0; i < mCntCubes; i++)
            mCubeList.get(i).SavePos(ed);
        for (int i = 0; i < mCntPyramides; i++)
            mPyramidList.get(i).SavePos(ed);
        for (int i = 0; i < mCntDominoCubes; i++)
            mDominoCubeList.get(i).SavePos(ed);
    }

    public void LoadPos(SharedPreferences sPref) {
        for (int i = 0; i < mCntCubes; i++)
            mCubeList.get(i).LoadPos(sPref);
        for (int i = 0; i < mCntPyramides; i++)
            mPyramidList.get(i).LoadPos(sPref);
        for (int i = 0; i < mCntDominoCubes; i++)
            mDominoCubeList.get(i).LoadPos(sPref);
    }

    public int GetRndPosRotate(Random r, int axis) {
        return r.nextInt(GetDim(mFigureType, axis));
    }

    public int GetMaxAxisCnt() {
        if (mFigureType == Structures.CUBE || mFigureType == Structures.DOMINO_CUBE)
            return 3;
        if (mFigureType == Structures.PYRAMID)
            return 4;

        return 0;
    }

    public int GetLimitAngle() {
        if (mFigureType == Structures.CUBE)
            return mCubeList.get(mFigureSubType).GetLimitAngle();
        if (mFigureType == Structures.PYRAMID)
            return 120;
        if (mFigureType == Structures.DOMINO_CUBE)
            return mDominoCubeList.get(mFigureSubType).GetLimitAngle();

        return 0;
    }

    public Pair<int[], float[]> GetSelectItem(float[] start, float[] end/*, boolean bSelect*/) {
        if (mFigureType == Structures.CUBE)
            return mCubeList.get(mFigureSubType).GetSelectItem(start, end);
        if (mFigureType == Structures.PYRAMID)
            return mPyramidList.get(mFigureSubType).GetSelectItem(start, end);
        if (mFigureType == Structures.DOMINO_CUBE)
            return mDominoCubeList.get(mFigureSubType).GetSelectItem(start, end);

        return null;
    }

    public Action GetAction(int[] item, float[] start, float[] end/*, boolean bSelect*/) {
        if (mFigureType == Structures.CUBE)
            return mCubeList.get(mFigureSubType).GetAction(item, start, end);
        if (mFigureType == Structures.PYRAMID)
            return mPyramidList.get(mFigureSubType).GetAction(item, start, end);
        if (mFigureType == Structures.DOMINO_CUBE)
            return mDominoCubeList.get(mFigureSubType).GetAction(item, start, end);

        return null;
    }

    public void Draw(Action a) {
        if (mFigureType == Structures.CUBE)
            mCubeList.get(mFigureSubType).Draw(a);
        if (mFigureType == Structures.PYRAMID)
            mPyramidList.get(mFigureSubType).Draw(a);
        if (mFigureType == Structures.DOMINO_CUBE)
            mDominoCubeList.get(mFigureSubType).Draw(a);
    }

    /*public Action GetAction(int[] CubeItem1, int[] CubeItem2, Bool bNeedRecalc) {
        if (mType == 0) return mCube.GetAction(CubeItem1, CubeItem2);
        else return mPyramid.GetAction(CubeItem1, CubeItem2, bNeedRecalc);
    }*/

    public boolean StoreItemPosition(Action a) {
        if (mFigureType == Structures.CUBE)
            return mCubeList.get(mFigureSubType).StoreItemPosition(a);
        if (mFigureType == Structures.PYRAMID)
            return mPyramidList.get(mFigureSubType).StoreItemPosition(a);
        if (mFigureType == Structures.DOMINO_CUBE)
            return mDominoCubeList.get(mFigureSubType).StoreItemPosition(a);

        return false;
    }

    public void CreateExVertices(int[] item, float[] point) {
        if (mFigureType == Structures.CUBE)
            mCubeList.get(mFigureSubType).CreateExVertices(item, point);
        if (mFigureType == Structures.PYRAMID)
            mPyramidList.get(mFigureSubType).CreateExVertices(item, point);
        if (mFigureType == Structures.DOMINO_CUBE)
            mDominoCubeList.get(mFigureSubType).CreateExVertices(item, point);
    }
}
