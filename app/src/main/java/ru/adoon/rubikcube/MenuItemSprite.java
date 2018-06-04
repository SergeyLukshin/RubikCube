package ru.adoon.rubikcube;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Лукшин on 08.11.2017.
 */

public class MenuItemSprite {

    ArrayList<MenuItemSprite> list = new ArrayList<MenuItemSprite>();

    public FloatBuffer vertexData;
    private  float[] mModelMatrix = new float[16];
    private int texture;
    private int texture2;
    //private Context context;
    float mSize;
    float mMarginGL;

    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT
            + TEXTURE_COUNT) * 4;

    private float mDistGL = 0.1f;
    private float mHeightGL = 0.3f;
    private float mWidthGL;

    private float mParentHeight;
    private float mParentWidth;

    private float mRatioX;
    private float mRatioY;

    private float mHeight;
    private float mWidth;

    private float mTop;
    private float mLeft;
    private boolean mIsEnabled = true;

    int mIndex;
    int mCntPos;
    int mTextureIndex = 0;
    int mTextureID = 0;
    int mTextureID2 = 0;

    public boolean IsEnabled() {
        return mIsEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        mIsEnabled = isEnabled;
    }

    public void SetContext(Context context) {
        if (mTextureID > 0)
            TextureUtils.deleteTexture(texture);
        if (mTextureID2 > 0)
            TextureUtils.deleteTexture(texture2);

        if (mTextureID > 0)
            texture = TextureUtils.loadTexture(context, mTextureID);
        if (mTextureID2 > 0)
            texture2 = TextureUtils.loadTexture(context, mTextureID2);

        for (int i = 0 ; i < list.size(); i++) {
            list.get(i).SetContext(context);
        }
    }

    public void SetRatio(float parentWidth, float parentHeight,
                   float ratioX, float ratioY) {
        //this.context = context;

        mHeightGL = 0.3f / ratioY;
        mDistGL = 0.03f / ratioY;
        mWidthGL = mSize / ratioX;
        mMarginGL = 1 - (mCntPos * mHeightGL + (mCntPos - 1) * mDistGL) / 2;

        mHeight = Structures.GetRealSizeFromGL(parentHeight, mHeightGL);
        mWidth = Structures.GetRealSizeFromGL(parentWidth, mWidthGL);

        float margin = Structures.GetRealSizeFromGL(parentHeight, mMarginGL);
        float dist = Structures.GetRealSizeFromGL(parentHeight, mDistGL);
        mTop = margin + mIndex * (mHeight + dist);
        //1 - (mMarginGL + mIndex * (mHeightGL + mDistGL))
        //mLeft = parentWidth - mWidth;

        mParentHeight = parentHeight;
        mParentWidth = parentWidth;

        mRatioX = ratioX;
        mRatioY = ratioY;
    }

    MenuItemSprite(Context context, int index, int cnt_pos, float size, int textureID, int textureID2, boolean isEnabled) {
        //this.context = context;
        mSize = size;
        mTextureID = textureID;
        mTextureID2 = textureID2;

        if (textureID > 0)
            texture = TextureUtils.loadTexture(context, textureID);
        if (textureID2 > 0)
            texture2 = TextureUtils.loadTexture(context, textureID2);
        mIndex = index;

        mCntPos = cnt_pos;
        mIsEnabled = isEnabled;

        Matrix.setIdentityM(mModelMatrix, 0);
    }

    boolean IsPressed(float x, float y) {
        if ( y <= mTop + mHeight && y >= mTop && x <= mLeft + mWidth && x >= mLeft)
            return true;
        else
            return false;
    }

    public void SetTexture(int pos) {
        mTextureIndex = pos;
    }

    public float GetPosY() {
        return 1 - (mMarginGL + mHeightGL / 2 + mIndex * (mHeightGL + mDistGL));
    }

    public void SetPos(float move_dist) {

        move_dist = move_dist / mRatioX;

        float[] vertices = {
                1 - mWidthGL + move_dist, 1 - (mMarginGL + mIndex * (mHeightGL + mDistGL)), -1, 0, 0,
                1 - mWidthGL + move_dist, 1 - (mMarginGL + mHeightGL + mIndex * (mHeightGL + mDistGL)), -1, 0, 1,
                1f + move_dist, 1 - (mMarginGL + mIndex * (mHeightGL + mDistGL)), -1, 1, 0,
                1f + move_dist, 1 - (mMarginGL + mHeightGL + mIndex * (mHeightGL + mDistGL)), -1, 1, 1,
        };

        mLeft = mParentWidth - mWidth + Structures.GetRealSizeFromGL(mParentWidth, move_dist);

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }

    public void Draw() {

        if (mTextureID <= 0) return;

        vertexData.position(0);
        glVertexAttribPointer(SpriteItemLocation.aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(SpriteItemLocation.aPositionLocation);

        // координаты текстур
        vertexData.position(POSITION_COUNT);
        glVertexAttribPointer(SpriteItemLocation.aTextureLocation, TEXTURE_COUNT, GL_FLOAT,
                false, STRIDE, vertexData);
        glEnableVertexAttribArray(SpriteItemLocation.aTextureLocation);

        // помещаем текстуру в target 2D юнита 0
        glActiveTexture(GL_TEXTURE0);
        if (mTextureIndex == 0)
            glBindTexture(GL_TEXTURE_2D, texture);
        else
            glBindTexture(GL_TEXTURE_2D, texture2);

        // юнит текстуры
        glUniform1i(SpriteItemLocation.uTextureUnitLocation, 0);
        glUniformMatrix4fv(SpriteItemLocation.uMatrixLocation, 1, false, mModelMatrix, 0);

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(SpriteItemLocation.aPositionLocation);
        GLES20.glDisableVertexAttribArray(SpriteItemLocation.aTextureLocation);
    }
}
