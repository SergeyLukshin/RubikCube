package ru.adoon.rubikcube;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Лукшин on 28.11.2017.
 */

public class TriangleItem {
    public FloatBuffer vertexData;

    private float[] mMatrix = new float[16];
    private  float[] mModelMatrix = new float[16];

    float[] vertices;
    public int m_AxisRotate;
    public int m_DirectRotate;



    // = new float[Pyramid.TRIANGLE_COUNT_PYRAMID /*всего треугольников*/ * 3 * (Structures.POSITION_COUNT /*вершины*/ + Structures.NORMAL_COUNT /*нормали*/ + Structures.TEXTURE_COUNT /*цвет*/) +];
    //float[] vertices_fake = new float[Structures.TRIANGLE_COUNT_CUBE * 3 * Structures.POSITION_COUNT];

    public int GetTriangleCount() {
        return 1;
    }

    public float[] GetNormal() {
        float[] normal = new float[3];
        normal[0] = vertices[Structures.POSITION_COUNT];
        normal[1] = vertices[Structures.POSITION_COUNT + 1];
        normal[2] = vertices[Structures.POSITION_COUNT + 2];
        return normal;
    }

    public float[] GetPoint(int index) {
        float[] point = new float[3];

        int cnt = Structures.POSITION_COUNT + Structures.NORMAL_COUNT + Structures.TEXTURE_COUNT;
        point[0] = vertices[index * cnt];
        point[1] = vertices[index * cnt + 1];
        point[2] = vertices[index * cnt + 2];
        return point;
    }

    TriangleItem(float[] point1, float[] point2, float[] point3, float[] n, int axisRotate, int directRotate, int col_index) {

        int cur_index = 0;
        m_AxisRotate = axisRotate;
        m_DirectRotate = directRotate;

        float[] texture1 = new float[Structures.TEXTURE_COUNT];
        float[] texture2 = new float[Structures.TEXTURE_COUNT];
        float[] texture3 = new float[Structures.TEXTURE_COUNT];

        vertices = new float[1 * 3 * (Structures.POSITION_COUNT /*вершины*/ + Structures.NORMAL_COUNT /*нормали*/ + Structures.TEXTURE_COUNT /*цвет*/)];

        int verge_color_index = col_index;
        texture1[0] = (verge_color_index + 0.5f) / 5f;
        texture1[1] = 0;
        texture2[0] = verge_color_index / 5f;
        texture2[1] = 1;
        texture3[0] = (verge_color_index + 1) / 5f;
        texture3[1] = 1;

        vertices[cur_index++] = point1[0];
        vertices[cur_index++] = point1[1];
        vertices[cur_index++] = point1[2];
        vertices[cur_index++] = n[0];
        vertices[cur_index++] = n[1];
        vertices[cur_index++] = n[2];
        vertices[cur_index++] = texture1[0];
        vertices[cur_index++] = texture1[1];
        //vertices[cur_index++] = color[2];

        vertices[cur_index++] = point2[0];
        vertices[cur_index++] = point2[1];
        vertices[cur_index++] = point2[2];
        vertices[cur_index++] = n[0];
        vertices[cur_index++] = n[1];
        vertices[cur_index++] = n[2];
        vertices[cur_index++] = texture2[0];
        vertices[cur_index++] = texture2[1];
        //vertices[cur_index++] = color[2];

        vertices[cur_index++] = point3[0];
        vertices[cur_index++] = point3[1];
        vertices[cur_index++] = point3[2];
        vertices[cur_index++] = n[0];
        vertices[cur_index++] = n[1];
        vertices[cur_index++] = n[2];
        vertices[cur_index++] = texture3[0];
        vertices[cur_index++] = texture3[1];

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }

    public void InitMatrix() {
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    public void Draw()
    {
        vertexData.position(0);
        glVertexAttribPointer(FigureItemLocation.aPositionLocation, Structures.POSITION_COUNT, GL_FLOAT, false, Structures.STRIDE2, vertexData);
        glEnableVertexAttribArray(FigureItemLocation.aPositionLocation);

        // нормали
        vertexData.position(Structures.POSITION_COUNT);
        glVertexAttribPointer(FigureItemLocation.aNormalLocation, Structures.NORMAL_COUNT, GL_FLOAT, false, Structures.STRIDE2, vertexData);
        glEnableVertexAttribArray(FigureItemLocation.aNormalLocation);

        vertexData.position(Structures.POSITION_COUNT + Structures.NORMAL_COUNT);
        glVertexAttribPointer(FigureItemLocation.aTextureLocation, Structures.TEXTURE_COUNT, GL_FLOAT, false, Structures.STRIDE2, vertexData);
        glEnableVertexAttribArray(FigureItemLocation.aTextureLocation);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, Pyramid.mTexture);

        glUniform1i(FigureItemLocation.uTextureUnitLocation, 0);

        // отрисовка
        Matrix.multiplyMM(mMatrix, 0, Camera.getViewMatrix(), 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, Camera.getProjMatrix(), 0, mMatrix, 0);
        //Matrix.multiplyMM(mMatrix, 0, WM, 0, mMatrix, 0);

        glUniformMatrix4fv(FigureItemLocation.uMatrixLocation, 1, false, mMatrix, 0);
        glUniformMatrix4fv(FigureItemLocation.uModelMatrixLocation, 1, false, mModelMatrix, 0);

        GLES20.glUniform3f(FigureItemLocation.uLightLocation, Structures.Light[0], Structures.Light[1], Structures.Light[2]);
        GLES20.glUniform3f(FigureItemLocation.uCameraLocation, Structures.CameraEye[0], Structures.CameraEye[1], Structures.CameraEye[2]);
        GLES20.glUniform1i(FigureItemLocation.uSelectLocation, 0);

        glDrawArrays(GL_TRIANGLES, 0, 1 * Structures.POSITION_COUNT);

        GLES20.glDisableVertexAttribArray(FigureItemLocation.aPositionLocation);
        GLES20.glDisableVertexAttribArray(FigureItemLocation.aNormalLocation);
        GLES20.glDisableVertexAttribArray(FigureItemLocation.aTextureLocation);
    }
}
