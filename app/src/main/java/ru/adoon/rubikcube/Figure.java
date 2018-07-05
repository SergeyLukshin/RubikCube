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
    private ArrayList<Cube> mEmptyCubeList = null;
    private ArrayList<FloppyCube> mFloppyCubeList = null;
    public int mFigureType = Structures.CUBE;
    public int mFigureSubType = 1;
    public int mCntCubes = 4;
    public int mCntPyramides = 4;
    public int mCntDominoCubes = 6;
    public int mCntEmptyCubes = 2;
    public int mCntFloppyCubes = 2;

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

        mEmptyCubeList = new ArrayList<Cube>();
        for (int i = 0; i < mCntEmptyCubes; i++) {
            mEmptyCubeList.add(new Cube(context));
        }

        mFloppyCubeList = new ArrayList<FloppyCube>();
        for (int i = 0; i < mCntFloppyCubes; i++) {
            mFloppyCubeList.add(new FloppyCube(context));
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
        if (FigureType == Structures.EMPTY_CUBE) {
            switch (axis) {
                case Structures.AXE_X:
                    return mEmptyCubeList.get(mFigureSubType).mCubeDimX;
                case Structures.AXE_Y:
                    return mEmptyCubeList.get(mFigureSubType).mCubeDimY;
                case Structures.AXE_Z:
                    return mEmptyCubeList.get(mFigureSubType).mCubeDimZ;
            }
        }
        if (FigureType == Structures.FLOPPY_CUBE) {
            switch (axis) {
                case Structures.AXE_X:
                    return mFloppyCubeList.get(mFigureSubType).mCubeDimX;
                case Structures.AXE_Y:
                    return mFloppyCubeList.get(mFigureSubType).mCubeDimY;
                case Structures.AXE_Z:
                    return mFloppyCubeList.get(mFigureSubType).mCubeDimZ;
            }
        }
        return 0;
    }

    public void FigureInit() {
        if (mFigureType == Structures.CUBE) {
            //for (int i = 0; i < mCntCubes; i++)
                mCubeList.get(mFigureSubType).CubeInit(mFigureSubType + 2, mFigureSubType + 2, mFigureSubType + 2, false);
        }
        if (mFigureType == Structures.PYRAMID) {
            //for (int i = 0; i < mCntPyramides; i++)
                mPyramidList.get(mFigureSubType).PyramidInit(mFigureSubType + 2);
        }
        if (mFigureType == Structures.DOMINO_CUBE) {
            //for (int i = 0; i < mCntDominoCubes; i++)
                switch(mFigureSubType) {
                    case 0:
                        mDominoCubeList.get(mFigureSubType).CubeInit(3, 2, 3, false);
                        break;
                    case 1:
                        mDominoCubeList.get(mFigureSubType).CubeInit(4, 2, 4, false);
                        break;
                    case 2:
                        mDominoCubeList.get(mFigureSubType).CubeInit(4, 3, 4, false);
                        break;
                    case 3:
                        mDominoCubeList.get(mFigureSubType).CubeInit(5, 2, 5, false);
                        break;
                    case 4:
                        mDominoCubeList.get(mFigureSubType).CubeInit(5, 3, 5, false);
                        break;
                    case 5:
                        mDominoCubeList.get(mFigureSubType).CubeInit(5, 4, 5, false);
                        break;
                }
        }
        if (mFigureType == Structures.EMPTY_CUBE) {
            if (mFigureSubType == 0)
                mEmptyCubeList.get(mFigureSubType).CubeInit(3, 3, 3, true);
            if (mFigureSubType == 1)
                mEmptyCubeList.get(mFigureSubType).CubeInit(5, 5, 5, true);
        }
        if (mFigureType == Structures.FLOPPY_CUBE) {
            if (mFigureSubType == 0)
                mFloppyCubeList.get(mFigureSubType).CubeInit(3, 3, 3);
            if (mFigureSubType == 1)
                mFloppyCubeList.get(mFigureSubType).CubeInit(5, 5, 5);
        }
    }

    public void SetContext(Context context) {
        for (int i = 0; i < mCntCubes; i++)
            mCubeList.get(i).SetContext(context);
        for (int i = 0; i < mCntPyramides; i++)
            mPyramidList.get(i).SetContext(context);
        for (int i = 0; i < mCntDominoCubes; i++)
            mDominoCubeList.get(i).SetContext(context);
        for (int i = 0; i < mCntEmptyCubes; i++)
            mEmptyCubeList.get(i).SetContext(context);
        for (int i = 0; i < mCntFloppyCubes; i++)
            mFloppyCubeList.get(i).SetContext(context);
    }

    public void SavePos(SharedPreferences.Editor ed) {
        //for (int i = 0; i < mCntCubes; i++)
        if (mFigureType == Structures.CUBE)
            mCubeList.get(mFigureSubType).SavePos(ed);
        //for (int i = 0; i < mCntPyramides; i++)
        if (mFigureType == Structures.PYRAMID)
            mPyramidList.get(mFigureSubType).SavePos(ed);
        //for (int i = 0; i < mCntDominoCubes; i++)
        if (mFigureType == Structures.DOMINO_CUBE)
            mDominoCubeList.get(mFigureSubType).SavePos(ed);
        if (mFigureType == Structures.EMPTY_CUBE)
            mEmptyCubeList.get(mFigureSubType).SavePos(ed);
        if (mFigureType == Structures.FLOPPY_CUBE)
            mFloppyCubeList.get(mFigureSubType).SavePos(ed);
    }

    public void LoadPos(SharedPreferences sPref) {
        //for (int i = 0; i < mCntCubes; i++)
        if (mFigureType == Structures.CUBE)
            mCubeList.get(mFigureSubType).LoadPos(sPref);
        //for (int i = 0; i < mCntPyramides; i++)
        if (mFigureType == Structures.PYRAMID)
            mPyramidList.get(mFigureSubType).LoadPos(sPref);
        //for (int i = 0; i < mCntDominoCubes; i++)
        if (mFigureType == Structures.DOMINO_CUBE)
            mDominoCubeList.get(mFigureSubType).LoadPos(sPref);
        if (mFigureType == Structures.EMPTY_CUBE)
            mEmptyCubeList.get(mFigureSubType).LoadPos(sPref);
        if (mFigureType == Structures.FLOPPY_CUBE)
            mFloppyCubeList.get(mFigureSubType).LoadPos(sPref);
    }

    public int GetRndPosRotate(Random r, int axis) {
        return r.nextInt(GetDim(mFigureType, axis));
    }

    public int GetMaxAxisCnt() {
        if (mFigureType == Structures.CUBE || mFigureType == Structures.DOMINO_CUBE || mFigureType == Structures.EMPTY_CUBE || mFigureType == Structures.FLOPPY_CUBE)
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
        if (mFigureType == Structures.EMPTY_CUBE)
            return mEmptyCubeList.get(mFigureSubType).GetLimitAngle();
        if (mFigureType == Structures.FLOPPY_CUBE)
            return mFloppyCubeList.get(mFigureSubType).GetLimitAngle();

        return 0;
    }

    public Pair<int[], float[]> GetSelectItem(float[] start, float[] end/*, boolean bSelect*/) {
        if (mFigureType == Structures.CUBE)
            return mCubeList.get(mFigureSubType).GetSelectItem(start, end);
        if (mFigureType == Structures.PYRAMID)
            return mPyramidList.get(mFigureSubType).GetSelectItem(start, end);
        if (mFigureType == Structures.DOMINO_CUBE)
            return mDominoCubeList.get(mFigureSubType).GetSelectItem(start, end);
        if (mFigureType == Structures.EMPTY_CUBE)
            return mEmptyCubeList.get(mFigureSubType).GetSelectItem(start, end);
        if (mFigureType == Structures.FLOPPY_CUBE)
            return mFloppyCubeList.get(mFigureSubType).GetSelectItem(start, end);

        return null;
    }

    public Action GetAction(int[] item, float[] start, float[] end/*, boolean bSelect*/) {
        if (mFigureType == Structures.CUBE)
            return mCubeList.get(mFigureSubType).GetAction(item, start, end);
        if (mFigureType == Structures.PYRAMID)
            return mPyramidList.get(mFigureSubType).GetAction(item, start, end);
        if (mFigureType == Structures.DOMINO_CUBE)
            return mDominoCubeList.get(mFigureSubType).GetAction(item, start, end);
        if (mFigureType == Structures.EMPTY_CUBE)
            return mEmptyCubeList.get(mFigureSubType).GetAction(item, start, end);
        if (mFigureType == Structures.FLOPPY_CUBE)
            return mFloppyCubeList.get(mFigureSubType).GetAction(item, start, end);

        return null;
    }

    public void Draw(Action a) {
        if (mFigureType == Structures.CUBE)
            mCubeList.get(mFigureSubType).Draw(a);
        if (mFigureType == Structures.PYRAMID)
            mPyramidList.get(mFigureSubType).Draw(a);
        if (mFigureType == Structures.DOMINO_CUBE)
            mDominoCubeList.get(mFigureSubType).Draw(a);
        if (mFigureType == Structures.EMPTY_CUBE)
            mEmptyCubeList.get(mFigureSubType).Draw(a);
        if (mFigureType == Structures.FLOPPY_CUBE)
            mFloppyCubeList.get(mFigureSubType).Draw(a);
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
        if (mFigureType == Structures.EMPTY_CUBE)
            return mEmptyCubeList.get(mFigureSubType).StoreItemPosition(a);
        if (mFigureType == Structures.FLOPPY_CUBE)
            return mFloppyCubeList.get(mFigureSubType).StoreItemPosition(a);

        return false;
    }

    public void CreateExVertices(int[] item, float[] point) {
        if (mFigureType == Structures.CUBE)
            mCubeList.get(mFigureSubType).CreateExVertices(item, point);
        if (mFigureType == Structures.PYRAMID)
            mPyramidList.get(mFigureSubType).CreateExVertices(item, point);
        if (mFigureType == Structures.DOMINO_CUBE)
            mDominoCubeList.get(mFigureSubType).CreateExVertices(item, point);
        if (mFigureType == Structures.EMPTY_CUBE)
            mEmptyCubeList.get(mFigureSubType).CreateExVertices(item, point);
        if (mFigureType == Structures.FLOPPY_CUBE)
            mFloppyCubeList.get(mFigureSubType).CreateExVertices(item, point);
    }
}
