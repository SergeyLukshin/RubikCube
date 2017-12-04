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

public class Background {
    public FloatBuffer vertexData;
    private  float[] mModelMatrix = new float[16];
    private int texture;
    //private Context context;

    private final static int POSITION_COUNT = 3;
    private static final int TEXTURE_COUNT = 2;
    private static final int STRIDE = (POSITION_COUNT
            + TEXTURE_COUNT) * 4;
    private boolean mAlpha;

    public void SetContext(Context context) {
        //this.context = context;
        TextureUtils.deleteTexture(texture);
        if (mAlpha)
            texture = TextureUtils.loadTexture(context, R.drawable.background_alpha);
        else
            texture = TextureUtils.loadTexture(context, R.drawable.background);
    }

    Background(Context context, boolean bAlpha) {

        mAlpha = bAlpha;
        float z = 1;
        if (bAlpha) z = -0.9f;

        float[] vertices = {
                -1f,  1, z,   0, 0,
                -1f, -1, z,   0, 1,
                1f,  1, z,   1, 0,
                1f, -1, z,   1, 1,
        };

        //this.context = context;

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

        if (bAlpha)
            texture = TextureUtils.loadTexture(context, R.drawable.background_alpha);
        else
            texture = TextureUtils.loadTexture(context, R.drawable.background);

        Matrix.setIdentityM(mModelMatrix, 0);
    }

    public void Draw() {
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
    }
}
