package ru.adoon.rubikcube;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Лукшин on 01.11.2017.
 */

public class Line {
    public FloatBuffer vertexData;

    private float[] mMatrix = new float[16];
    private  float[] mModelMatrix = new float[16];

    Line(float[] start, float[] end) {
        float[] vertices = new float[6];
        vertices[0] = start[0];
        vertices[1] = start[1];
        vertices[2] = start[2];
        vertices[3] = end[0];
        vertices[4] = end[1];
        vertices[5] = end[2];

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

    }

    public void Draw() {

        Matrix.setIdentityM(mModelMatrix, 0);

        vertexData.position(0);
        glVertexAttribPointer(LineItemLocation.aPositionLocation, Structures.POSITION_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(LineItemLocation.aPositionLocation);

        // отрисовка
        Matrix.multiplyMM(mMatrix, 0, Camera.getViewMatrix(), 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, Camera.getProjMatrix(), 0, mMatrix, 0);

        glUniformMatrix4fv(LineItemLocation.uMatrixLocation, 1, false, mMatrix, 0);

        glLineWidth(10);
        glDrawArrays(GL_LINES, 0, 3);

        GLES20.glDisableVertexAttribArray(LineItemLocation.aPositionLocation);
    }
}
