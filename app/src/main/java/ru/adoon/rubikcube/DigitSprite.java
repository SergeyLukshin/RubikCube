package ru.adoon.rubikcube;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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
 * Created by Лукшин on 10.11.2017.
 */

public class DigitSprite {
    public FloatBuffer vertexData;
    private  float[] mModelMatrix = new float[16];

    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT
            + TEXTURE_COUNT) * 4;

    private float mHeightGL = 0.19f;
    private float mWidthGL = 0.10f;
    float marginTop = 0.03f;
    private int mCntDigits = 11;

    private float mRatioX = 1;
    private float mRatioY = 1;
    private int mDigit = 0;
    private int mPos = 0;

    DigitSprite() {
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    public void SetRatio(float ratioX, float ratioY) {
        mRatioX = ratioX;
        mRatioY = ratioY;

        RecalcSize();
    }
    public void SetDigit(int pos, int digit) {

        mPos = pos;
        mDigit = digit;

        RecalcSize();
    }

    public void RecalcSize() {

        float x1_digit = 1f / mCntDigits * mDigit;
        float x2_digit = 1f / mCntDigits * (1 + mDigit);

        float Height = mHeightGL / mRatioY;
        float Width = mWidthGL / mRatioX;

        float marginLeft = 1 - mCntDigits * Width / 2;

        float[] vertices = {
                -1 + marginLeft + mPos * Width, 1 - marginTop,                   -0.8f, x1_digit, 0,
                -1 + marginLeft + mPos * Width, 1 - marginTop - Height,          -0.8f, x1_digit, 1,
                -1 + marginLeft + (mPos + 1) * Width, 1 - marginTop,             -0.8f, x2_digit, 0,
                -1 + marginLeft + (mPos + 1) * Width, 1 - marginTop - Height,    -0.8f, x2_digit, 1,
        };

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }

    public void Draw(int texture) {
        //glEnable(GL_BLEND);

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
        glBindTexture(GL_TEXTURE_2D, texture);

        // юнит текстуры
        glUniform1i(SpriteItemLocation.uTextureUnitLocation, 0);
        glUniformMatrix4fv(SpriteItemLocation.uMatrixLocation, 1, false, mModelMatrix, 0);

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(SpriteItemLocation.aPositionLocation);
        GLES20.glDisableVertexAttribArray(SpriteItemLocation.aTextureLocation);

        //glDisable(GL_BLEND);
    }
}
