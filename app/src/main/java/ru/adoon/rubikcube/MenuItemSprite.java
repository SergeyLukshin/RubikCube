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
    private int texture_ru;
    private int texture2_ru;

    private int texture_en;
    private int texture2_en;
    //private Context context;
    float mSize_ru = 0;
    float mSize_en = 0;
    float mMarginGL;

    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT
            + TEXTURE_COUNT) * 4;

    private float mDistGL = 0.1f;
    private float mHeightGL = 0.3f;
    private float mWidthGL_ru;
    private float mWidthGL_en;

    private float mParentHeight;
    private float mParentWidth;

    private float mRatioX;
    private float mRatioY;

    private float mHeight;
    private float mWidth_ru;
    private float mWidth_en;

    private float mTop;
    private float mLeft;
    private boolean mIsEnabled = true;

    int mIndex;
    int mCntPos;
    int mTextureIndex = 0;
    int mTextureID_ru = 0;
    int mTextureID2_ru = 0;

    int mTextureID_en = 0;
    int mTextureID2_en = 0;
    int mLanguage = 0;

    public boolean IsEnabled() {
        return mIsEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        mIsEnabled = isEnabled;
    }

    public void SetLanguage(int language) {
        mLanguage = language;
    }

    public void DeleteTexture() {
        if (mTextureID_ru > 0)
            TextureUtils.deleteTexture(texture_ru);
        if (mTextureID2_ru > 0)
            TextureUtils.deleteTexture(texture2_ru);

        if (mTextureID_en > 0)
            TextureUtils.deleteTexture(texture_en);
        if (mTextureID2_en > 0)
            TextureUtils.deleteTexture(texture2_en);
    }

    public void SetContext(Context context) {
        if (texture_ru > 0)
            TextureUtils.deleteTexture(texture_ru);
        if (texture2_ru > 0)
            TextureUtils.deleteTexture(texture2_ru);

        if (texture_en > 0)
            TextureUtils.deleteTexture(texture_en);
        if (texture2_en > 0)
            TextureUtils.deleteTexture(texture2_en);

        int[] out = new int[1];

        if (mTextureID_ru > 0)
            texture_ru = TextureUtils.loadTexture(context, mTextureID_ru, out);
        if (mTextureID2_ru > 0)
            texture2_ru = TextureUtils.loadTexture(context, mTextureID2_ru, out);

        mSize_ru = out[0] / 460f;

        out = new int[1];
        if (mTextureID_en > 0)
            texture_en = TextureUtils.loadTexture(context, mTextureID_en, out);
        if (mTextureID2_en > 0)
            texture2_en = TextureUtils.loadTexture(context, mTextureID2_en, out);

        mSize_en = out[0] / 460f;

        for (int i = 0 ; i < list.size(); i++) {
            list.get(i).SetContext(context);
        }
    }

    public void SetRatio(float parentWidth, float parentHeight,
                   float ratioX, float ratioY) {
        //this.context = context;

        mHeightGL = 0.3f / ratioY;
        mDistGL = 0.03f / ratioY;
        mWidthGL_ru = mSize_ru / ratioX;
        mWidthGL_en = mSize_en / ratioX;
        mMarginGL = 1 - (mCntPos * mHeightGL + (mCntPos - 1) * mDistGL) / 2;

        mHeight = Structures.GetRealSizeFromGL(parentHeight, mHeightGL);
        mWidth_ru = Structures.GetRealSizeFromGL(parentWidth, mWidthGL_ru);
        mWidth_en = Structures.GetRealSizeFromGL(parentWidth, mWidthGL_en);

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

    MenuItemSprite(Context context, int index, int cnt_pos, int textureID_ru, int textureID2_ru, int textureID_en, int textureID2_en, boolean isEnabled) {
        //this.context = context;
        //mSize = size;
        mTextureID_ru = textureID_ru;
        mTextureID2_ru = textureID2_ru;

        mTextureID_en = textureID_en;
        mTextureID2_en = textureID2_en;

        texture_ru = 0;
        texture2_ru = 0;

        texture_en = 0;
        texture2_en = 0;

        /*if (mTextureID_ru > 0)
            texture_ru = TextureUtils.loadTexture(context, mTextureID_ru);
        if (mTextureID2_ru > 0)
            texture2_ru = TextureUtils.loadTexture(context, mTextureID2_ru);

        //if (out != null)
        //    mSize_ru = out[0] / 460f;

        if (mTextureID_en > 0)
            texture_en = TextureUtils.loadTexture(context, mTextureID_en);
        if (mTextureID2_en > 0)
            texture2_en = TextureUtils.loadTexture(context, mTextureID2_en);*/

        //if (out != null)
        //    mSize_en = out[0] / 460f;

        mIndex = index;

        mCntPos = cnt_pos;
        mIsEnabled = isEnabled;

        Matrix.setIdentityM(mModelMatrix, 0);
    }

    MenuItemSprite(Context context, int index, int cnt_pos, int textureID, int textureID2, boolean isEnabled) {
        //this.context = context;
        //mSize = size;
        mTextureID_ru = textureID;
        mTextureID2_ru = textureID2;

        mTextureID_en = -1;
        mTextureID2_en = -1;

        /*if (textureID_ru > 0)
            texture_ru = TextureUtils.loadTexture(context, textureID_ru);
        if (textureID2_ru > 0)
            texture2_ru = TextureUtils.loadTexture(context, textureID2_ru);

        if (textureID_en > 0)
            texture_en = TextureUtils.loadTexture(context, textureID_en);
        if (textureID2_en > 0)
            texture2_en = TextureUtils.loadTexture(context, textureID2_en);*/

        int[] out = null;

        if (mTextureID_ru > 0)
            texture_ru = TextureUtils.loadTexture(context, mTextureID_ru, out);
        if (mTextureID2_ru > 0)
            texture2_ru = TextureUtils.loadTexture(context, mTextureID2_ru, out);

        //if (out != null)
        //    mSize_en = out[0] / 460f;

        mIndex = index;

        mCntPos = cnt_pos;
        mIsEnabled = isEnabled;

        Matrix.setIdentityM(mModelMatrix, 0);
    }

    boolean IsPressed(float x, float y) {
        float mWidth = 0;
        if (mLanguage == 0) {
            mWidth = mWidth_ru;
        }
        else {
            if (mTextureID_en < 0) {
                mWidth = mWidth_ru;
            }
            else {
                mWidth = mWidth_en;
            }
        }

        if (y <= mTop + mHeight && y >= mTop && x <= mLeft + mWidth && x >= mLeft)
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

        float mWidthGL, mWidth;
        if (mLanguage == 0) {
            mWidthGL = mWidthGL_ru;
            mWidth = mWidth_ru;
        }
        else {
            if (mTextureID_en < 0) {
                mWidthGL = mWidthGL_ru;
                mWidth = mWidth_ru;
            }
            else {
                mWidthGL = mWidthGL_en;
                mWidth = mWidth_en;
            }
        }

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

        if (mTextureID_ru <= 0) return;

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
        if (mTextureIndex == 0) {
            if (mLanguage == 0) {
                if (texture_ru > 0)
                    glBindTexture(GL_TEXTURE_2D, texture_ru);
            }
            else {
                if (mTextureID_en < 0) {
                    if (texture_ru > 0)
                        glBindTexture(GL_TEXTURE_2D, texture_ru);
                }
                else {
                    if (texture_en > 0)
                        glBindTexture(GL_TEXTURE_2D, texture_en);
                }
            }
        }
        else {
            if (mLanguage == 0) {
                if (texture2_ru > 0)
                    glBindTexture(GL_TEXTURE_2D, texture2_ru);
            }
            else {
                if (mTextureID2_en < 0) {
                    if (texture2_ru > 0)
                        glBindTexture(GL_TEXTURE_2D, texture2_ru);
                }
                else {
                    if (texture2_en > 0)
                        glBindTexture(GL_TEXTURE_2D, texture2_en);
                }
            }
        }

        // юнит текстуры
        glUniform1i(SpriteItemLocation.uTextureUnitLocation, 0);
        glUniformMatrix4fv(SpriteItemLocation.uMatrixLocation, 1, false, mModelMatrix, 0);

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(SpriteItemLocation.aPositionLocation);
        GLES20.glDisableVertexAttribArray(SpriteItemLocation.aTextureLocation);
    }
}
