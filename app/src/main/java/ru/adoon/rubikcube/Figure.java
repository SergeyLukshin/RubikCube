package ru.adoon.rubikcube;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Лукшин on 22.11.2017.
 */

public class Figure {
    private Cube mCube = null;
    private Pyramid mPyramid = null;
    public int mType = 0;

    Figure(Context context) {
        mCube = new Cube(context);
        mPyramid = new Pyramid(context);
    }

    public void SetFigure(int type) {
        FigureInit();
        mType = type;
    }

    public void FigureInit() {
        mCube.CubeInit();
        mPyramid.PyramidInit();
    }

    public void SetContext(Context context) {
        mCube.SetContext(context);
        mPyramid.SetContext(context);
    }

    public void SavePos(SharedPreferences.Editor ed) {
        mCube.SavePos(ed);
        mPyramid.SavePos(ed);
    }

    public void LoadPos(SharedPreferences sPref) {
        mCube.LoadPos(sPref);
        mPyramid.LoadPos(sPref);
    }

    public int GetMaxAxisCnt() {
        return mType == 0? 3 : 4;
    }

    public int GetLimitAngle() {
        return mType == 0 ? 90 : 120;
    }

    public Pair<int[], float[]> GetSelectItem(float[] start, float[] end/*, boolean bSelect*/) {
        if (mType == 0)
            return mCube.GetSelectItem(start, end);
        else
            return mPyramid.GetSelectItem(start, end);
    }

    public Action GetAction(int[] item, float[] start, float[] end/*, boolean bSelect*/) {
        if (mType == 0)
            return mCube.GetAction(item, start, end);
        else
            return mPyramid.GetAction(item, start, end);
    }

    public void Draw(Action a) {
        if (mType == 0) mCube.Draw(a);
        else mPyramid.Draw(a);
    }

    /*public Action GetAction(int[] CubeItem1, int[] CubeItem2, Bool bNeedRecalc) {
        if (mType == 0) return mCube.GetAction(CubeItem1, CubeItem2);
        else return mPyramid.GetAction(CubeItem1, CubeItem2, bNeedRecalc);
    }*/

    public boolean StoreItemPosition(Action a) {
        if (mType == 0) return mCube.StoreItemPosition(a);
        else return mPyramid.StoreItemPosition(a);
    }

    public void CreateExVertices(int[] item, float[] point) {
        if (mType == 0)
            mCube.CreateExVertices(item, point);
        else
            mPyramid.CreateExVertices(item, point);
    }
}
