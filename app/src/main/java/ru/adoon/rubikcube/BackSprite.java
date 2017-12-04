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
 * Created by Лукшин on 01.11.2017.
 */

public class BackSprite {
    public FloatBuffer vertexData;
    private  float[] mModelMatrix = new float[16];
    private int texture;
    private Context context;

    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT
            + TEXTURE_COUNT) * 4;

    private float mSizeGL = 0.3f;

    private float mHeight;
    private float mWidth;

    private float mTop;
    private float mLeft;

    boolean IsPressed(float x, float y) {
        if ( y <= mTop + mHeight && y >= mTop && x <= mLeft + mWidth && x >= mLeft)
            return true;
        else
            return false;
    }

    BackSprite(Context context) {

        texture = TextureUtils.loadTexture(context, R.drawable.back);
    }

    public void SetContext(Context context) {
        TextureUtils.deleteTexture(texture);
        texture = TextureUtils.loadTexture(context, R.drawable.back);
    }

    public void SetRatio(float parentWidth, float parentHeight, float ratioX, float ratioY) {

        float Height = mSizeGL / ratioY;
        float Width = mSizeGL / ratioX;
        float[] vertices = {
                1 - Width,  1, -1,   0, 0,
                1 - Width, 1 - Height, -1,   0, 1,
                1, 1, -1,   1, 0,
                1, 1 - Height, -1,   1, 1,
        };

        mHeight = Structures.GetRealSizeFromGL(parentHeight, Height);
        mWidth = Structures.GetRealSizeFromGL(parentWidth, Width);

        mTop = 0;
        mLeft = parentWidth - mWidth;

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

        Matrix.setIdentityM(mModelMatrix, 0);
    }

    public void Draw() {
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
