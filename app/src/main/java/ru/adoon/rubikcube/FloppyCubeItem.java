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

public class FloppyCubeItem {
    public FloatBuffer vertexData;

    private float[] mMatrix = new float[16];
    private  float[] mModelMatrix = new float[16];
    private  float[] mPos = null;
    float mScale = 1f;
    public volatile HashMap<Integer, ArrayList<TriangleItem>> mNearItems = null;

    public int mPosX;
    public int mPosY;
    public int mPosZ;

    public int mCubeDimX;
    public int mCubeDimY;
    public int mCubeDimZ;

    public boolean m_bSelect = false;
    public boolean m_bVisible = true;

    public int[] verge_color_index = new int[6];
    //private float[] vecRotate = new float[4];

    float[] vertices = new float[Cube.TRIANGLE_COUNT_CUBE /*всего треугольников*/ * 3 * (Structures.POSITION_COUNT /*вершины*/ + Structures.NORMAL_COUNT /*нормали*/ + Structures.TEXTURE_COUNT /*цвет*/)];
    //float[] vertices_fake = new float[Structures.TRIANGLE_COUNT_CUBE * 3 * Structures.POSITION_COUNT];

    public int GetTriangleCount() {
        return Cube.TRIANGLE_COUNT_CUBE;
    }

    public float[] GetNormal(int index_triangle) {
        float[] normal = new float[3];
        normal[0] = vertices[index_triangle * 3 * (Structures.POSITION_COUNT + Structures.NORMAL_COUNT + Structures.TEXTURE_COUNT) + Structures.POSITION_COUNT];
        normal[1] = vertices[index_triangle * 3 * (Structures.POSITION_COUNT + Structures.NORMAL_COUNT + Structures.TEXTURE_COUNT) + Structures.POSITION_COUNT + 1];
        normal[2] = vertices[index_triangle * 3 * (Structures.POSITION_COUNT + Structures.NORMAL_COUNT + Structures.TEXTURE_COUNT) + Structures.POSITION_COUNT + 2];
        return normal;
    }

    public float[] GetPoint(int index_triangle, int index) {
        float[] point = new float[3];

        int cnt = Structures.POSITION_COUNT + Structures.NORMAL_COUNT + Structures.TEXTURE_COUNT;
        point[0] = vertices[index_triangle * 3 * cnt + index * cnt];
        point[1] = vertices[index_triangle * 3 * cnt + index * cnt + 1];
        point[2] = vertices[index_triangle * 3 * cnt + index * cnt + 2];
        return point;
    }

    /*private int SetTriangleFake(float[] vertices, int cur_index, float[] vertex,
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
    }*/

    private int SetTriangle(float[] vertices, int cur_index, float[] vertex,
                            int ind1, int ind2, int ind3,
                            float n1, float n2, float n3,
                            int verge_color_index, int verge_index) {
        ind1--;
        ind2--;
        ind3--;

        float[] texture1 = new float[Structures.TEXTURE_COUNT];
        float[] texture2 = new float[Structures.TEXTURE_COUNT];
        float[] texture3 = new float[Structures.TEXTURE_COUNT];

        if (verge_index == 0) {
            texture1[0] = (verge_color_index + 1) / 7f;
            texture1[1] = 0;
            texture2[0] = verge_color_index / 7f;
            texture2[1] = 0;
            texture3[0] = verge_color_index / 7f;
            texture3[1] = 1;

        }
        else {
            texture1[0] = (verge_color_index + 1) / 7f;
            texture1[1] = 0;
            texture2[0] = verge_color_index / 7f;
            texture2[1] = 1;
            texture3[0] = (verge_color_index + 1) / 7f;
            texture3[1] = 1;
        }

        float difX = 2 * (mPosX - (mCubeDimX - 1) / 2f);//2 * mPosX * mScale;
        float difY = 2 * (mPosY - (mCubeDimY - 1) / 2f);//2 * mPosY * mScale;
        float difZ = 2 * (mPosZ - (mCubeDimZ - 1) / 2f);//2 * mPosZ * mScale;

        if (mPos != null) {
            difX += mPos[0];
            difY += mPos[1];
            difZ += mPos[2];
        }

        float x1 = (vertex[ind1 * 3] + difX) * mScale;// + difX;
        float y1 = (vertex[ind1 * 3 + 1] + difY) * mScale;// + difY;
        float z1 = (vertex[ind1 * 3 + 2] + difZ) * mScale;// + difZ;

        float x2 = (vertex[ind2 * 3] + difX) * mScale;// + difX;
        float y2 = (vertex[ind2 * 3 + 1] + difY) * mScale;// + difY;
        float z2 = (vertex[ind2 * 3 + 2] + difZ) * mScale;// + difZ;

        float x3 = (vertex[ind3 * 3] + difX) * mScale;// + difX;
        float y3 = (vertex[ind3 * 3 + 1] + difY) * mScale;// + difY;
        float z3 = (vertex[ind3 * 3 + 2] + difZ) * mScale;// + difZ;

        vertices[cur_index++] = x1;
        vertices[cur_index++] = y1;
        vertices[cur_index++] = z1;
        vertices[cur_index++] = n1;
        vertices[cur_index++] = n2;
        vertices[cur_index++] = n3;
        vertices[cur_index++] = texture1[0];
        vertices[cur_index++] = texture1[1];
        //vertices[cur_index++] = color[2];

        vertices[cur_index++] = x2;
        vertices[cur_index++] = y2;
        vertices[cur_index++] = z2;
        vertices[cur_index++] = n1;
        vertices[cur_index++] = n2;
        vertices[cur_index++] = n3;
        vertices[cur_index++] = texture2[0];
        vertices[cur_index++] = texture2[1];
        //vertices[cur_index++] = color[2];

        vertices[cur_index++] = x3;
        vertices[cur_index++] = y3;
        vertices[cur_index++] = z3;
        vertices[cur_index++] = n1;
        vertices[cur_index++] = n2;
        vertices[cur_index++] = n3;
        vertices[cur_index++] = texture3[0];
        vertices[cur_index++] = texture3[1];
        //vertices[cur_index++] = color[2];

        return cur_index;
    }

    static void Rotate(int[] verge_color_index_, int rotate_type, int direction, boolean isEqualDim) {
        if (rotate_type == Structures.AXE_X) {
            if (isEqualDim) {
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
            else {
                int ind = verge_color_index_[0];
                verge_color_index_[0] = verge_color_index_[2];
                verge_color_index_[2] = ind;
                ind = verge_color_index_[4];
                verge_color_index_[4] = verge_color_index_[5];
                verge_color_index_[5] = ind;
            }
        }
        if (rotate_type == Structures.AXE_Y) {
            if (isEqualDim) {
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
            else {
                int ind = verge_color_index_[0];
                verge_color_index_[0] = verge_color_index_[2];
                verge_color_index_[2] = ind;
                ind = verge_color_index_[1];
                verge_color_index_[1] = verge_color_index_[3];
                verge_color_index_[3] = ind;
            }
        }
        if (rotate_type == Structures.AXE_Z) {
            if (isEqualDim) {
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
            else {
                int ind = verge_color_index_[3];
                verge_color_index_[3] = verge_color_index_[1];
                verge_color_index_[1] = ind;
                ind = verge_color_index_[4];
                verge_color_index_[4] = verge_color_index_[5];
                verge_color_index_[5] = ind;
            }
        }
    }

    FloppyCubeItem(int posX, int posY, int posZ, int[] verge_color_index_, int CubeDimX, int CubeDimY, int CubeDimZ, float Scale, boolean bVisible) {

        mCubeDimX = CubeDimX;
        mCubeDimY = CubeDimY;
        mCubeDimZ = CubeDimZ;
        mScale = Scale;

        m_bVisible = bVisible;

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

            //if (posX == 0) verge_color_index[3] = Cube.GREEN;
            //if (posX == mCubeDimX - 1) verge_color_index[1] = Cube.BLUE;
            if (posY == (mCubeDimY - 1)/ 2) {
                if (posZ == mCubeDimZ - 1) verge_color_index[0] = Cube.RED;
                if (posX == mCubeDimX - 1) verge_color_index[1] = Cube.BLUE;
                if (posZ == 0) verge_color_index[2] = Cube.ORANGE;
                if (posX == 0) verge_color_index[3] = Cube.GREEN;

                verge_color_index[4] = Cube.WHITE;
                verge_color_index[5] = Cube.YELLOW;
            }
            //if (posZ == 0) verge_color_index[2] = Cube.ORANGE;
            //if (posZ == mCubeDimZ - 1) verge_color_index[0] = Cube.RED;
        }
        else {
            for (int i = 0; i < verge_color_index.length; i++)
                verge_color_index[i] = verge_color_index_[i];
        }

        /*int cur_index2 = 0;
        // основные грани
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 1, 2, 3);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 1, 3, 4);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 5, 6, 7);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 5, 7, 8);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 9, 10, 11);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 9, 11, 12);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 13, 14, 15);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 13, 15, 16);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 17, 18, 19);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 17, 19, 20);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 21, 22, 23);
        cur_index2 = SetTriangleFake(vertices_fake, cur_index2, Structures.vertex2, 21, 23, 24);*/

        int cur_index = 0;
        // основные грани
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 1, 2, 3, 0, 0, 1, verge_color_index[0], 0);
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 1, 3, 4, 0, 0, 1, verge_color_index[0], 1);
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 5, 6, 7, 1, 0, 0, verge_color_index[1], 0);
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 5, 7, 8, 1, 0, 0, verge_color_index[1], 1);
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 9, 10, 11, 0, 0, -1, verge_color_index[2], 0);
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 9, 11, 12, 0, 0, -1, verge_color_index[2], 1);
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 13, 14, 15, -1, 0, 0, verge_color_index[3], 0);
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 13, 15, 16, -1, 0, 0, verge_color_index[3], 1);
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 17, 18, 19, 0, 1, 0, verge_color_index[4], 0);
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 17, 19, 20, 0, 1, 0, verge_color_index[4], 1);
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 21, 22, 23, 0, -1, 0, verge_color_index[5], 0);
        cur_index = SetTriangle(vertices, cur_index, Cube.vertex, 21, 23, 24, 0, -1, 0, verge_color_index[5], 1);

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

    public void CreateExVertices(float[] point, int vertex_index) {

        mNearItems = new HashMap<>();

        float[] mid = {0, 0, 0};
        float koef = 10;

        //for (int i = 0; i < 4; i++){

        float[][] p = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        float[][] p2 = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

        float[][] new_p = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        float[][] new_p2 = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

        int vi = vertex_index / 2;
        int vertex1 = vi * 2;
        int vertex2 = vi * 2 + 1;

        mNearItems.put(vi, new ArrayList<TriangleItem>());

        for (int m = 0; m < p.length; m++) {
            p[m] = GetPoint(vertex1, m);
            p2[m] = GetPoint(vertex2, m);
        }
        float[] n = GetNormal(vertex_index);
        mid = Surface.Equal(p[0]);
        mid[0] = (p[0][0] + p[2][0]) / 2;
        mid[1] = (p[0][1] + p[2][1]) / 2;
        mid[2] = (p[0][2] + p[2][2]) / 2;

        float[] dif = Surface.Sub(point, mid);

        for (int m = 0; m < new_p.length; m++) {
            for (int k = 0; k < new_p[m].length; k++) {
                new_p[m][k] = (p[m][k] - mid[k]) * koef + mid[k];
                new_p[m][k] += dif[k];

                new_p2[m][k] = (p2[m][k] - mid[k]) * koef + mid[k];
                new_p2[m][k] += dif[k];
            }
        }
        mid = Surface.Add(mid, dif);

        if (vi == 0) {
            mNearItems.get(vi).add(new TriangleItem(new_p[0], new_p[1], mid, n, Structures.AXE_X, Structures.DIRECT_RIGHT, 0));
            mNearItems.get(vi).add(new TriangleItem(new_p[1], new_p[2], mid, n, Structures.AXE_Y, Structures.DIRECT_RIGHT, 1));
            mNearItems.get(vi).add(new TriangleItem(new_p[2], new_p2[2], mid, n, Structures.AXE_X, Structures.DIRECT_LEFT, 2));
            mNearItems.get(vi).add(new TriangleItem(new_p2[2], new_p[0], mid, n, Structures.AXE_Y, Structures.DIRECT_LEFT, 3));
        }
        if (vi == 1) {
            mNearItems.get(vi).add(new TriangleItem(new_p[0], new_p[1], mid, n, Structures.AXE_Z, Structures.DIRECT_LEFT, 0));
            mNearItems.get(vi).add(new TriangleItem(new_p[1], new_p[2], mid, n, Structures.AXE_Y, Structures.DIRECT_RIGHT, 1));
            mNearItems.get(vi).add(new TriangleItem(new_p[2], new_p2[2], mid, n, Structures.AXE_Z, Structures.DIRECT_RIGHT, 2));
            mNearItems.get(vi).add(new TriangleItem(new_p2[2], new_p[0], mid, n, Structures.AXE_Y, Structures.DIRECT_LEFT, 3));
        }
        if (vi == 2) {
            mNearItems.get(vi).add(new TriangleItem(new_p[0], new_p[1], mid, n, Structures.AXE_X, Structures.DIRECT_LEFT, 0));
            mNearItems.get(vi).add(new TriangleItem(new_p[1], new_p[2], mid, n, Structures.AXE_Y, Structures.DIRECT_RIGHT, 1));
            mNearItems.get(vi).add(new TriangleItem(new_p[2], new_p2[2], mid, n, Structures.AXE_X, Structures.DIRECT_RIGHT, 2));
            mNearItems.get(vi).add(new TriangleItem(new_p2[2], new_p[0], mid, n, Structures.AXE_Y, Structures.DIRECT_LEFT, 3));
        }
        if (vi == 3) {
            mNearItems.get(vi).add(new TriangleItem(new_p[0], new_p[1], mid, n, Structures.AXE_Z, Structures.DIRECT_RIGHT, 0));
            mNearItems.get(vi).add(new TriangleItem(new_p[1], new_p[2], mid, n, Structures.AXE_Y, Structures.DIRECT_RIGHT, 1));
            mNearItems.get(vi).add(new TriangleItem(new_p[2], new_p2[2], mid, n, Structures.AXE_Z, Structures.DIRECT_LEFT, 2));
            mNearItems.get(vi).add(new TriangleItem(new_p2[2], new_p[0], mid, n, Structures.AXE_Y, Structures.DIRECT_LEFT, 3));
        }
        if (vi == 4) {
            mNearItems.get(vi).add(new TriangleItem(new_p[0], new_p[1], mid, n, Structures.AXE_Z, Structures.DIRECT_LEFT, 0));
            mNearItems.get(vi).add(new TriangleItem(new_p[1], new_p[2], mid, n, Structures.AXE_X, Structures.DIRECT_LEFT, 1));
            mNearItems.get(vi).add(new TriangleItem(new_p[2], new_p2[2], mid, n, Structures.AXE_Z, Structures.DIRECT_RIGHT, 2));
            mNearItems.get(vi).add(new TriangleItem(new_p2[2], new_p[0], mid, n, Structures.AXE_X, Structures.DIRECT_RIGHT, 3));
        }
        if (vi == 5) {
            mNearItems.get(vi).add(new TriangleItem(new_p[0], new_p[1], mid, n, Structures.AXE_Z, Structures.DIRECT_LEFT, 0));
            mNearItems.get(vi).add(new TriangleItem(new_p[1], new_p[2], mid, n, Structures.AXE_X, Structures.DIRECT_RIGHT, 1));
            mNearItems.get(vi).add(new TriangleItem(new_p[2], new_p2[2], mid, n, Structures.AXE_Z, Structures.DIRECT_RIGHT, 2));
            mNearItems.get(vi).add(new TriangleItem(new_p2[2], new_p[0], mid, n, Structures.AXE_X, Structures.DIRECT_LEFT, 3));
        }

            /*float sign = 1;
            if (mType == 0) {
                mid = Surface.Equal(p[0]);
            }
            else {
                sign = -1;
                if (vi == 0)
                    mid = Surface.Equal(p[2]);
                if (vi == 1)
                    mid = Surface.Equal(p[0]);
                if (vi == 2)
                    mid = Surface.Equal(p[1]);
                if (vi == 3)
                    mid = Surface.Equal(p[0]);
            }

            if (vi == 0) {
                mid[1] -= sign * (Pyramid.h - dY) * mScale;
                mid[2] += sign * (Pyramid.h2_3 - dZ) * mScale;
            }
            if (vi == 1) {
                mid[0] += sign * (Pyramid.h2_3 - dZ) * mScale * Math.cos(Math.toRadians(30));
                mid[1] -= sign * (Pyramid.h - dY) * mScale;
                mid[2] -= sign * (Pyramid.h2_3 - dZ) * mScale * Math.sin(Math.toRadians(30));
            }
            if (vi == 2) {
                mid[0] -= sign * (Pyramid.h2_3 - dZ) * mScale * Math.cos(Math.toRadians(30));
                mid[1] -= sign * (Pyramid.h - dY) * mScale;
                mid[2] -= sign * (Pyramid.h2_3 - dZ) * mScale * Math.sin(Math.toRadians(30));
            }
            if (vi == 3) {
                mid[2] += sign * 2 * Pyramid.h2_3 * mScale;
            }

            float[] dif = Surface.Sub(point, mid);

            for (int j = 0; j < 3; j++) {

                for (int m = 0; m < new_p.length; m++) {
                    for (int k = 0; k < new_p[m].length; k++) {
                        new_p[m][k] = (p[m][k] - mid[k]) * koef + mid[k];
                        new_p[m][k] += dif[k];
                    }
                }

                new_p[j] = Surface.Add(Surface.Equal(mid), dif);
                if (j == 0) {
                    mid2 = Surface.DivConst(Surface.Add(new_p[1], new_p[2]), 2);
                    if (vi == 0) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], new_p[1], mid2, n, mType == 0? Structures.AXE_L : Structures.AXE_B, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_RIGHT, 0));
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], mid2, new_p[2], n, mType == 0? Structures.AXE_R : Structures.AXE_L, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_LEFT, 1));
                    }
                    if (vi == 1) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], new_p[1], mid2, n, mType == 0? Structures.AXE_F : Structures.AXE_F, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_LEFT, 0));
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], mid2, new_p[2], n, mType == 0? Structures.AXE_L : Structures.AXE_L, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_LEFT, 1));
                    }
                    if (vi == 2) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], new_p[1], mid2, n, mType == 0? Structures.AXE_R : Structures.AXE_F, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_LEFT, 0));
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], mid2, new_p[2], n, mType == 0? Structures.AXE_F : Structures.AXE_B, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_LEFT, 1));
                    }
                    if (vi == 3) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], new_p[1], mid2, n, mType == 0? Structures.AXE_R : Structures.AXE_R, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_LEFT, 0));
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], mid2, new_p[2], n, mType == 0? Structures.AXE_L : Structures.AXE_L, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_LEFT, 1));
                    }
                }
                if (j == 1) {
                    mid2 = Surface.DivConst(Surface.Add(new_p[0], new_p[2]), 2);
                    if (vi == 0) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], new_p[1], mid2, n, mType == 0? Structures.AXE_L : Structures.AXE_B, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_LEFT, 2));
                        mNearItems.get(vertex_index).add(new TriangleItem(mid2, new_p[1], new_p[2], n, mType == 0? Structures.AXE_B : Structures.AXE_R, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_LEFT, 3));
                    }
                    if (vi == 1) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], new_p[1], mid2, n, mType == 0? Structures.AXE_F : Structures.AXE_F, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_RIGHT, 2));
                        mNearItems.get(vertex_index).add(new TriangleItem(mid2, new_p[1], new_p[2], n, mType == 0? Structures.AXE_B : Structures.AXE_B, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_RIGHT, 3));
                    }
                    if (vi == 2) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], new_p[1], mid2, n, mType == 0? Structures.AXE_R : Structures.AXE_F, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_RIGHT, 2));
                        mNearItems.get(vertex_index).add(new TriangleItem(mid2, new_p[1], new_p[2], n, mType == 0? Structures.AXE_B : Structures.AXE_R, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_LEFT, 3));
                    }
                    if (vi == 3) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], new_p[1], mid2, n, mType == 0? Structures.AXE_R : Structures.AXE_R, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_RIGHT, 2));
                        mNearItems.get(vertex_index).add(new TriangleItem(mid2, new_p[1], new_p[2], n, mType == 0? Structures.AXE_F : Structures.AXE_F, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_LEFT, 3));
                    }
                }
                if (j == 2) {
                    mid2 = Surface.DivConst(Surface.Add(new_p[0], new_p[1]), 2);
                    if (vi == 0) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], mid2, new_p[2], n, mType == 0? Structures.AXE_R : Structures.AXE_L, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_RIGHT, 4));
                        mNearItems.get(vertex_index).add(new TriangleItem(mid2, new_p[1], new_p[2], n, mType == 0? Structures.AXE_B : Structures.AXE_R, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_RIGHT, 0));
                    }
                    if (vi == 1) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], mid2, new_p[2], n, mType == 0? Structures.AXE_L : Structures.AXE_L, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_RIGHT, 4));
                        mNearItems.get(vertex_index).add(new TriangleItem(mid2, new_p[1], new_p[2], n, mType == 0? Structures.AXE_B : Structures.AXE_B, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_LEFT, 0));
                    }
                    if (vi == 2) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], mid2, new_p[2], n, mType == 0? Structures.AXE_F : Structures.AXE_B, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_RIGHT, 4));
                        mNearItems.get(vertex_index).add(new TriangleItem(mid2, new_p[1], new_p[2], n, mType == 0? Structures.AXE_B : Structures.AXE_R, mType == 0? Structures.DIRECT_RIGHT : Structures.DIRECT_RIGHT, 0));
                    }
                    if (vi == 3) {
                        mNearItems.get(vertex_index).add(new TriangleItem(new_p[0], mid2, new_p[2], n, mType == 0? Structures.AXE_L : Structures.AXE_L, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_RIGHT, 4));
                        mNearItems.get(vertex_index).add(new TriangleItem(mid2, new_p[1], new_p[2], n, mType == 0? Structures.AXE_F : Structures.AXE_F, mType == 0? Structures.DIRECT_LEFT : Structures.DIRECT_RIGHT, 0));
                    }
                }
            }*/
    }

    public void Draw()
    {
        if (!m_bVisible) return;

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
        glBindTexture(GL_TEXTURE_2D, Cube.mTexture);

        glUniform1i(FigureItemLocation.uTextureUnitLocation, 0);

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

        glDrawArrays(GL_TRIANGLES, 0, Cube.TRIANGLE_COUNT_CUBE * Structures.POSITION_COUNT);

        GLES20.glDisableVertexAttribArray(FigureItemLocation.aPositionLocation);
        GLES20.glDisableVertexAttribArray(FigureItemLocation.aNormalLocation);
        GLES20.glDisableVertexAttribArray(FigureItemLocation.aTextureLocation);
    }
}
