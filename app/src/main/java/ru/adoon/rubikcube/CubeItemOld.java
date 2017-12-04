package ru.adoon.rubikcube;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

public class CubeItemOld {
    public FloatBuffer vertexData;

    private float[] mMatrix = new float[16];
    private  float[] mModelMatrix = new float[16];
    private  float[] mPos = null;
    float mScale = 1f;

    public int mPosX;
    public int mPosY;
    public int mPosZ;

    public boolean m_bSelect = false;

    public int[] verge_color_index = new int[6];
    //private float[] vecRotate = new float[4];

    float[] vertices = new float[Structures.TRIANGLE_COUNT /*всего треугольников*/ * 3 * (Structures.POSITION_COUNT /*вершины*/ + Structures.NORMAL_COUNT /*нормали*/ + Structures.COLOR_COUNT /*цвет*/)];
    float[] vertices_fake = new float[Cube.TRIANGLE_COUNT_CUBE * 3 * Structures.POSITION_COUNT];

    public int GetTriangleCount() {
        return Cube.TRIANGLE_COUNT_CUBE;
    }

    public float[] GetNormal(int index_triangle) {
        float[] normal = new float[3];
        normal[0] = vertices[index_triangle * 3 * (Structures.POSITION_COUNT + Structures.NORMAL_COUNT + Structures.COLOR_COUNT) + Structures.POSITION_COUNT];
        normal[1] = vertices[index_triangle * 3 * (Structures.POSITION_COUNT + Structures.NORMAL_COUNT + Structures.COLOR_COUNT) + Structures.POSITION_COUNT + 1];
        normal[2] = vertices[index_triangle * 3 * (Structures.POSITION_COUNT + Structures.NORMAL_COUNT + Structures.COLOR_COUNT) + Structures.POSITION_COUNT + 2];
        return normal;
    }

    public float[] GetPoint(int index_triangle, int index) {
        float[] point = new float[3];
        point[0] = vertices_fake[index_triangle * 3 * Structures.POSITION_COUNT + index * Structures.POSITION_COUNT];
        point[1] = vertices_fake[index_triangle * 3 * Structures.POSITION_COUNT + index * Structures.POSITION_COUNT + 1];
        point[2] = vertices_fake[index_triangle * 3 * Structures.POSITION_COUNT + index * Structures.POSITION_COUNT + 2];
        return point;
    }

    private int SetTriangleFake(float[] vertices, int cur_index, float[] vertex,
                                int ind1, int ind2, int ind3) {
        ind1--;
        ind2--;
        ind3--;

        float difX = 2 * mPosX * mScale;
        float difY = 2 * mPosY * mScale;
        float difZ = 2 * mPosZ * mScale;

        if (mPos != null) {
            difX += mPos[0];
            difY += mPos[1];
            difZ += mPos[2];
        }

        float x1 = vertex[ind1 * 3] * mScale + difX;
        float y1 = vertex[ind1 * 3 + 1] * mScale + difY;
        float z1 = vertex[ind1 * 3 + 2] * mScale + difZ;

        float x2 = vertex[ind2 * 3] * mScale + difX;
        float y2 = vertex[ind2 * 3 + 1] * mScale + difY;
        float z2 = vertex[ind2 * 3 + 2] * mScale + difZ;

        float x3 = vertex[ind3 * 3] * mScale + difX;
        float y3 = vertex[ind3 * 3 + 1] * mScale + difY;
        float z3 = vertex[ind3 * 3 + 2] * mScale + difZ;

        vertices[cur_index++] = x1;
        vertices[cur_index++] = y1;
        vertices[cur_index++] = z1;

        vertices[cur_index++] = x2;
        vertices[cur_index++] = y2;
        vertices[cur_index++] = z2;

        vertices[cur_index++] = x3;
        vertices[cur_index++] = y3;
        vertices[cur_index++] = z3;

        return cur_index;
    }

    private int SetTriangle(float[] vertices, int cur_index, float[] vertex,
                            int ind1, int ind2, int ind3,
                            float n1, float n2, float n3,
                            int verge_color_index) {
        ind1--;
        ind2--;
        ind3--;

        float[] color = new float[Structures.COLOR_COUNT];
        switch (verge_color_index) {
            case Cube.GREEN:
                color = Structures.green;
                break;
            case Cube.RED:
                color = Structures.red;
                break;
            case Cube.BLUE:
                color = Structures.blue;
                break;
            case Cube.ORANGE:
                color = Structures.orange;
                break;
            case Cube.WHITE:
                color = Structures.white;
                break;
            case Cube.YELLOW:
                color = Structures.yellow;
                break;
            case Cube.BLACK:
                color = Structures.black;
                break;
        }

        float difX = 2 * mPosX * mScale;
        float difY = 2 * mPosY * mScale;
        float difZ = 2 * mPosZ * mScale;

        if (mPos != null) {
            difX += mPos[0];
            difY += mPos[1];
            difZ += mPos[2];
        }

        float x1 = vertex[ind1 * 3] * mScale + difX;
        float y1 = vertex[ind1 * 3 + 1] * mScale + difY;
        float z1 = vertex[ind1 * 3 + 2] * mScale + difZ;

        float x2 = vertex[ind2 * 3] * mScale + difX;
        float y2 = vertex[ind2 * 3 + 1] * mScale + difY;
        float z2 = vertex[ind2 * 3 + 2] * mScale + difZ;

        float x3 = vertex[ind3 * 3] * mScale + difX;
        float y3 = vertex[ind3 * 3 + 1] * mScale + difY;
        float z3 = vertex[ind3 * 3 + 2] * mScale + difZ;

        vertices[cur_index++] = x1;
        vertices[cur_index++] = y1;
        vertices[cur_index++] = z1;
        vertices[cur_index++] = n1;
        vertices[cur_index++] = n2;
        vertices[cur_index++] = n3;
        vertices[cur_index++] = color[0];
        vertices[cur_index++] = color[1];
        vertices[cur_index++] = color[2];

        vertices[cur_index++] = x2;
        vertices[cur_index++] = y2;
        vertices[cur_index++] = z2;
        vertices[cur_index++] = n1;
        vertices[cur_index++] = n2;
        vertices[cur_index++] = n3;
        vertices[cur_index++] = color[0];
        vertices[cur_index++] = color[1];
        vertices[cur_index++] = color[2];

        vertices[cur_index++] = x3;
        vertices[cur_index++] = y3;
        vertices[cur_index++] = z3;
        vertices[cur_index++] = n1;
        vertices[cur_index++] = n2;
        vertices[cur_index++] = n3;
        vertices[cur_index++] = color[0];
        vertices[cur_index++] = color[1];
        vertices[cur_index++] = color[2];

        return cur_index;
    }

    static void Rotate(int[] verge_color_index_, int rotate_type, int direction) {
        if (rotate_type == Structures.AXE_X) {
            if (direction == Structures.DIRECT_LEFT) {
                int ind = verge_color_index_[0];
                verge_color_index_[0] = verge_color_index_[4];
                verge_color_index_[4] = verge_color_index_[2];
                verge_color_index_[2] = verge_color_index_[5];
                verge_color_index_[5] = ind;
            } else {
                int ind = verge_color_index_[0];
                verge_color_index_[0] = verge_color_index_[5];
                verge_color_index_[5] = verge_color_index_[2];
                verge_color_index_[2] = verge_color_index_[4];
                verge_color_index_[4] = ind;
            }
        }
        if (rotate_type == Structures.AXE_Y) {
            if (direction == Structures.DIRECT_LEFT) {
                int ind = verge_color_index_[0];
                verge_color_index_[0] = verge_color_index_[3];
                verge_color_index_[3] = verge_color_index_[2];
                verge_color_index_[2] = verge_color_index_[1];
                verge_color_index_[1] = ind;
            } else {
                int ind = verge_color_index_[0];
                verge_color_index_[0] = verge_color_index_[1];
                verge_color_index_[1] = verge_color_index_[2];
                verge_color_index_[2] = verge_color_index_[3];
                verge_color_index_[3] = ind;
            }
        }
        if (rotate_type == Structures.AXE_Z) {
            if (direction == Structures.DIRECT_LEFT) {
                int ind = verge_color_index_[3];
                verge_color_index_[3] = verge_color_index_[4];
                verge_color_index_[4] = verge_color_index_[1];
                verge_color_index_[1] = verge_color_index_[5];
                verge_color_index_[5] = ind;
            } else {
                int ind = verge_color_index_[3];
                verge_color_index_[3] = verge_color_index_[5];
                verge_color_index_[5] = verge_color_index_[1];
                verge_color_index_[1] = verge_color_index_[4];
                verge_color_index_[4] = ind;
            }
        }
    }

    CubeItemOld(float[] pos, float fScale) {

        /*float[][] verge_color = {Structures.black, Structures.black, Structures.black, Structures.black, Structures.black, Structures.black};
        if (posX == -1) verge_color[3] = Structures.green;
        if (posX == 1) verge_color[1] = Structures.blue;
        if (posY == -1) verge_color[5] = Structures.yellow;
        if (posY == 1) verge_color[4] = Structures.white;
        if (posZ == -1) verge_color[2] = Structures.orange;
        if (posZ == 1) verge_color[0] = Structures.red;*/

        mPos = new float[4];
        mScale = fScale;
        System.arraycopy(pos, 0, mPos, 0, 4);

        for (int i = 0; i < verge_color_index.length; i++)
            verge_color_index[i] = Cube.YELLOW;

        int cur_index_fake = 0;
        // основные грани
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 1, 2, 3);
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 1, 3, 4);
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 5, 6, 7);
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 5, 7, 8);
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 9, 10, 11);
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 9, 11, 12);
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 13, 14, 15);
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 13, 15, 16);
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 17, 18, 19);
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 17, 19, 20);
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 21, 22, 23);
        cur_index_fake = SetTriangleFake(vertices_fake, cur_index_fake, Cube.vertex, 21, 23, 24);

        int cur_index = 0;
        // основные грани
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 1, 2, 3, 0, 0, 1, verge_color_index[0]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 1, 3, 4, 0, 0, 1, verge_color_index[0]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 5, 6, 7, 1, 0, 0, verge_color_index[1]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 5, 7, 8, 1, 0, 0, verge_color_index[1]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 9, 10, 11, 0, 0, -1, verge_color_index[2]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 9, 11, 12, 0, 0, -1, verge_color_index[2]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 13, 14, 15, -1, 0, 0, verge_color_index[3]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 13, 15, 16, -1, 0, 0, verge_color_index[3]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 17, 18, 19, 0, 1, 0, verge_color_index[4]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 17, 19, 20, 0, 1, 0, verge_color_index[4]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 21, 22, 23, 0, -1, 0, verge_color_index[5]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 21, 23, 24, 0, -1, 0, verge_color_index[5]);

        // боковушки горизонтальные
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 19, 18, 2, 0, 1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 19, 2, 1, 0, 1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 4, 3, 23, 0, -1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 4, 23, 22, 0, -1, 1, Cube.BLACK);

        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 20, 19, 6, 1, 1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 20, 6, 5, 1, 1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 8, 7, 22, 1, -1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 8, 22, 21, 1, -1, 0, Cube.BLACK);

        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 17, 20, 10, 0, 1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 17, 10, 9, 0, 1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 12, 11, 21, 0, -1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 12, 21, 24, 0, -1, -1, Cube.BLACK);

        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 18, 17, 14, -1, 1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 18, 14, 13, -1, 1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 16, 15, 24, -1, -1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 16, 24, 23, -1, -1, 0, Cube.BLACK);

        // боковушки вертикальные
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 6, 1, 4, 1, 0, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 6, 4, 7, 1, 0, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 10, 5, 8, 1, 0, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 10, 8, 11, 1, 0, -1, Cube.BLACK);

        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 14, 9, 12, -1, 0, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 14, 12, 15, -1, 0, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 2, 13, 16, -1, 0, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 2, 16, 3, -1, 0, 1, Cube.BLACK);

        // уголки
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 19, 1, 6, 1, 1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 7, 4, 22, 1, -1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 20, 5, 10, 1, 1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 11, 8, 21, 1, -1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 17, 9, 14, -1, 1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 15, 12, 24, -1, -1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 18, 13, 2, -1, 1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 3, 16, 23, -1, -1, 1, Cube.BLACK);

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);

    }

    CubeItemOld(int posX, int posY, int posZ, int[] verge_color_index_) {

        mPosX = posX;
        mPosY = posY;
        mPosZ = posZ;

        /*float[][] verge_color = {Structures.black, Structures.black, Structures.black, Structures.black, Structures.black, Structures.black};
        if (posX == -1) verge_color[3] = Structures.green;
        if (posX == 1) verge_color[1] = Structures.blue;
        if (posY == -1) verge_color[5] = Structures.yellow;
        if (posY == 1) verge_color[4] = Structures.white;
        if (posZ == -1) verge_color[2] = Structures.orange;
        if (posZ == 1) verge_color[0] = Structures.red;*/

        if (verge_color_index_ == null) {
            for (int i = 0; i < verge_color_index.length; i++)
                verge_color_index[i] = Cube.BLACK;

            if (posX == -1) verge_color_index[3] = Cube.GREEN;
            if (posX == 1) verge_color_index[1] = Cube.BLUE;
            if (posY == -1) verge_color_index[5] = Cube.YELLOW;
            if (posY == 1) verge_color_index[4] = Cube.WHITE;
            if (posZ == -1) verge_color_index[2] = Cube.ORANGE;
            if (posZ == 1) verge_color_index[0] = Cube.RED;
        }
        else {
            for (int i = 0; i < verge_color_index.length; i++)
                verge_color_index[i] = verge_color_index_[i];
        }

        int cur_index2 = 0;
        // основные грани
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 1, 2, 3);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 1, 3, 4);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 5, 6, 7);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 5, 7, 8);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 9, 10, 11);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 9, 11, 12);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 13, 14, 15);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 13, 15, 16);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 17, 18, 19);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 17, 19, 20);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 21, 22, 23);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Cube.vertex, 21, 23, 24);

        int cur_index = 0;
        // основные грани
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 1, 2, 3, 0, 0, 1, verge_color_index[0]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 1, 3, 4, 0, 0, 1, verge_color_index[0]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 5, 6, 7, 1, 0, 0, verge_color_index[1]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 5, 7, 8, 1, 0, 0, verge_color_index[1]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 9, 10, 11, 0, 0, -1, verge_color_index[2]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 9, 11, 12, 0, 0, -1, verge_color_index[2]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 13, 14, 15, -1, 0, 0, verge_color_index[3]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 13, 15, 16, -1, 0, 0, verge_color_index[3]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 17, 18, 19, 0, 1, 0, verge_color_index[4]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 17, 19, 20, 0, 1, 0, verge_color_index[4]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 21, 22, 23, 0, -1, 0, verge_color_index[5]);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 21, 23, 24, 0, -1, 0, verge_color_index[5]);

        // боковушки горизонтальные
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 19, 18, 2, 0, 1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 19, 2, 1, 0, 1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 4, 3, 23, 0, -1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 4, 23, 22, 0, -1, 1, Cube.BLACK);

        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 20, 19, 6, 1, 1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 20, 6, 5, 1, 1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 8, 7, 22, 1, -1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 8, 22, 21, 1, -1, 0, Cube.BLACK);

        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 17, 20, 10, 0, 1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 17, 10, 9, 0, 1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 12, 11, 21, 0, -1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 12, 21, 24, 0, -1, -1, Cube.BLACK);

        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 18, 17, 14, -1, 1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 18, 14, 13, -1, 1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 16, 15, 24, -1, -1, 0, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 16, 24, 23, -1, -1, 0, Cube.BLACK);

        // боковушки вертикальные
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 6, 1, 4, 1, 0, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 6, 4, 7, 1, 0, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 10, 5, 8, 1, 0, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 10, 8, 11, 1, 0, -1, Cube.BLACK);

        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 14, 9, 12, -1, 0, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 14, 12, 15, -1, 0, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 2, 13, 16, -1, 0, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 2, 16, 3, -1, 0, 1, Cube.BLACK);

        // уголки
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 19, 1, 6, 1, 1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 7, 4, 22, 1, -1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 20, 5, 10, 1, 1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 11, 8, 21, 1, -1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 17, 9, 14, -1, 1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 15, 12, 24, -1, -1, -1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 18, 13, 2, -1, 1, 1, Cube.BLACK);
        cur_index = SetTriangle(vertices, cur_index, Structures.vertex, 3, 16, 23, -1, -1, 1, Cube.BLACK);

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }

    public void InitMatrix() {
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    public void RotateMatrix(float angle, float[] rotateType) {
        if (rotateType != null)
            Matrix.rotateM(mModelMatrix, 0, angle, rotateType[0], rotateType[1], rotateType[2]);
    }

    public void Draw()
    {
        vertexData.position(0);
        glVertexAttribPointer(FigureItemLocation.aPositionLocation, Structures.POSITION_COUNT, GL_FLOAT, false, Structures.STRIDE, vertexData);
        glEnableVertexAttribArray(FigureItemLocation.aPositionLocation);

        // нормали
        vertexData.position(Structures.POSITION_COUNT);
        glVertexAttribPointer(FigureItemLocation.aNormalLocation, Structures.NORMAL_COUNT, GL_FLOAT, false, Structures.STRIDE, vertexData);
        glEnableVertexAttribArray(FigureItemLocation.aNormalLocation);

        vertexData.position(Structures.POSITION_COUNT + Structures.NORMAL_COUNT);
        glVertexAttribPointer(FigureItemLocation.aColorLocation, Structures.COLOR_COUNT, GL_FLOAT, false, Structures.STRIDE, vertexData);
        glEnableVertexAttribArray(FigureItemLocation.aColorLocation);

        // отрисовка
        /*float[] WM = new float[16];
        Matrix.setIdentityM(WM, 0);
        WM[0] = 3 * Camera.mScale;
        WM[5] = 3 * Camera.mScale;
        WM[10] = 3 * Camera.mScale;
        WM[15] = 1;*/
        Matrix.multiplyMM(mMatrix, 0, Camera.getViewMatrix(), 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, Camera.getProjMatrix(), 0, mMatrix, 0);
        //Matrix.multiplyMM(mMatrix, 0, WM, 0, mMatrix, 0);

        glUniformMatrix4fv(FigureItemLocation.uMatrixLocation, 1, false, mMatrix, 0);
        glUniformMatrix4fv(FigureItemLocation.uModelMatrixLocation, 1, false, mModelMatrix, 0);

        GLES20.glUniform3f(FigureItemLocation.uLightLocation, Structures.Light[0], Structures.Light[1], Structures.Light[2]);
        GLES20.glUniform3f(FigureItemLocation.uCameraLocation, Structures.CameraEye[0], Structures.CameraEye[1], Structures.CameraEye[2]);
        GLES20.glUniform1i(FigureItemLocation.uSelectLocation, m_bSelect ? 1 : 0);

        glDrawArrays(GL_TRIANGLES, 0, Structures.TRIANGLE_COUNT * Structures.POSITION_COUNT);

        GLES20.glDisableVertexAttribArray(FigureItemLocation.aPositionLocation);
        GLES20.glDisableVertexAttribArray(FigureItemLocation.aNormalLocation);
        GLES20.glDisableVertexAttribArray(FigureItemLocation.aColorLocation);
    }
}
