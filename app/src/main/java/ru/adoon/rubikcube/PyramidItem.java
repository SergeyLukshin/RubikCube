package ru.adoon.rubikcube;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

public class PyramidItem {
    public FloatBuffer vertexData;

    public volatile HashMap<Integer, ArrayList<TriangleItem>> mNearItems = null;//new ArrayList<ArrayList<ArrayList<CubeItemOld>>>();

    private float[] mMatrix = new float[16];
    private  float[] mModelMatrix = new float[16];
    //private  float[] mPos = null;
    private float mScale = 2.7f;

    public int mPosX;
    public int mPosY;
    public int mPosZ;
    public int mType; // 0 - pyramid, 1 - other

    public boolean m_bSelect = false;

    public int[] verge_color_index;// = new int[6];
    //private float[] vecRotate = new float[4];
    Pyramid mParent;

    float[] vertices;


    // = new float[Pyramid.TRIANGLE_COUNT_PYRAMID /*всего треугольников*/ * 3 * (Structures.POSITION_COUNT /*вершины*/ + Structures.NORMAL_COUNT /*нормали*/ + Structures.TEXTURE_COUNT /*цвет*/) +];
    //float[] vertices_fake = new float[Structures.TRIANGLE_COUNT_CUBE * 3 * Structures.POSITION_COUNT];

    public int GetTriangleCount() {
        if (mType == 0)
            return Pyramid.TRIANGLE_COUNT_PYRAMID;
        else
            return Pyramid.TRIANGLE_COUNT_PYRAMID2;
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

    private int SetTriangle(float[] vertices, int cur_index, float[] vertex,
                            int ind1, int ind2, int ind3,
                            float n1, float n2, float n3,
                            int verge_color_index, int PyramidDim) {
        ind1--;
        ind2--;
        ind3--;

        float[] texture1 = new float[Structures.TEXTURE_COUNT];
        float[] texture2 = new float[Structures.TEXTURE_COUNT];
        float[] texture3 = new float[Structures.TEXTURE_COUNT];

        texture1[0] = (verge_color_index + 0.5f) / 5f;
        texture1[1] = 0;
        texture2[0] = verge_color_index / 5f;
        texture2[1] = 1;
        texture3[0] = (verge_color_index + 1) / 5f;
        texture3[1] = 1;

        float H = Pyramid.h * PyramidDim;
        float a = PyramidDim;
        float Radius = (3 * H * H + a * a) / (6 * H); // радиус сферы, описанной около сборной пирамиды

        float difX = (mPosX - (PyramidDim - 1)) * 1 / 2f;
        float difY = - Pyramid.h * (PyramidDim - 1 - mPosY) - Pyramid.h / 2;//(mPosY - (PyramidDim - 1) / 2f) * Pyramid.h;
        float difZ = 0;//2 * mPosZ * mScale;
        if (mType == 0) {
            if (PyramidDim == 2) {
                if (mPosY == 0) {
                    if ((mPosZ - (PyramidDim - 1)) == -1) difZ = -2 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 1) difZ = Pyramid.h2_3;
                }
            }
            if (PyramidDim == 3) {
                if (mPosY == 1) {
                    if ((mPosZ - (PyramidDim - 1)) == -1) difZ = -2 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 1) difZ = Pyramid.h2_3;
                }
                if (mPosY == 0) {
                    if ((mPosZ - (PyramidDim - 1)) == -2) difZ = -4 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 0) difZ = -Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 2) difZ = 2 * Pyramid.h2_3;
                }
            }
            if (PyramidDim == 4) {
                if (mPosY == 2) {
                    if ((mPosZ - (PyramidDim - 1)) == -1) difZ = -2 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 1) difZ = Pyramid.h2_3;
                }
                if (mPosY == 1) {
                    if ((mPosZ - (PyramidDim - 1)) == -2) difZ = -4 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 0) difZ = -Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 2) difZ = 2 * Pyramid.h2_3;
                }
                if (mPosY == 0) {
                    if ((mPosZ - (PyramidDim - 1)) == -3) difZ = -6 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == -1) difZ = -3 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 1) difZ = 0;//Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 3) difZ = 3 * Pyramid.h2_3;
                }
            }
            if (PyramidDim == 5) {
                if (mPosY == 3) {
                    if ((mPosZ - (PyramidDim - 1)) == -1) difZ = -2 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 1) difZ = Pyramid.h2_3;
                }
                if (mPosY == 2) {
                    if ((mPosZ - (PyramidDim - 1)) == -2) difZ = -4 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 0) difZ = -Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 2) difZ = 2 * Pyramid.h2_3;
                }
                if (mPosY == 1) {
                    if ((mPosZ - (PyramidDim - 1)) == -3) difZ = -6 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == -1) difZ = -3 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 1) difZ = 0;//Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 3) difZ = 3 * Pyramid.h2_3;
                }
                if (mPosY == 0) {
                    if ((mPosZ - (PyramidDim - 1)) == -4) difZ = -8 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == -2) difZ = -5 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 0) difZ = -2 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 2) difZ = Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 4) difZ = 4 * Pyramid.h2_3;
                }
            }
        }
        else {
            if (PyramidDim == 3) {
                if ((mPosZ - (PyramidDim - 1)) == -1) difZ = -2 * Pyramid.h2_3;
                if ((mPosZ - (PyramidDim - 1)) == 1) difZ = Pyramid.h2_3;
            }
            if (PyramidDim == 4) {
                if (mPosY == 1) {
                    if ((mPosZ - (PyramidDim - 1)) == -1) difZ = -2 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 1) difZ = Pyramid.h2_3;
                }
                if (mPosY == 0) {
                    if ((mPosZ - (PyramidDim - 1)) == -2) difZ = -4 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 0) difZ = -1 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 2) difZ = 2 * Pyramid.h2_3;
                }
            }
            if (PyramidDim == 5) {
                if (mPosY == 2) {
                    if ((mPosZ - (PyramidDim - 1)) == -1) difZ = -2 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 1) difZ = Pyramid.h2_3;
                }
                if (mPosY == 1) {
                    if ((mPosZ - (PyramidDim - 1)) == -2) difZ = -4 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 0) difZ = -1 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 2) difZ = 2 * Pyramid.h2_3;
                }
                if (mPosY == 0) {
                    if ((mPosZ - (PyramidDim - 1)) == -3) difZ = -6 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == -1) difZ = -3 * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 1) difZ = 0;// * Pyramid.h2_3;
                    if ((mPosZ - (PyramidDim - 1)) == 3) difZ = 3 * Pyramid.h2_3;
                }
            }
        }

        difY += Radius;//Pyramid.radius;

        /*if (mPos != null) {
            difX += mPos[0];
            difY += mPos[1];
            difZ += mPos[2];
        }*/

        float x1 = (vertex[ind1 * 3] + difX) * mScale;
        float y1 = (vertex[ind1 * 3 + 1] + difY) * mScale;
        float z1 = (vertex[ind1 * 3 + 2] + difZ) * mScale;

        float x2 = (vertex[ind2 * 3] + difX) * mScale;
        float y2 = (vertex[ind2 * 3 + 1] + difY) * mScale;
        float z2 = (vertex[ind2 * 3 + 2] + difZ) * mScale;

        float x3 = (vertex[ind3 * 3] + difX) * mScale;
        float y3 = (vertex[ind3 * 3 + 1] + difY) * mScale;
        float z3 = (vertex[ind3 * 3 + 2] + difZ) * mScale;

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

    static void Rotate(int[] verge_color_index_, int rotate_type, int direction, int type) {
        if (rotate_type == Structures.AXE_B) {
            if (direction == Structures.DIRECT_LEFT) {
                int ind = verge_color_index_[0];

                if (type == 0) {
                    verge_color_index_[0] = verge_color_index_[2];
                    verge_color_index_[2] = verge_color_index_[1];
                    verge_color_index_[1] = ind;
                }
                else {
                    verge_color_index_[0] = verge_color_index_[4];
                    verge_color_index_[4] = verge_color_index_[6];
                    verge_color_index_[6] = ind;
                }
            } else {
                int ind = verge_color_index_[0];

                if (type == 0) {
                    verge_color_index_[0] = verge_color_index_[1];
                    verge_color_index_[1] = verge_color_index_[2];
                    verge_color_index_[2] = ind;
                }
                else {
                    verge_color_index_[0] = verge_color_index_[6];
                    verge_color_index_[6] = verge_color_index_[4];
                    verge_color_index_[4] = ind;
                }
            }
        }
        if (rotate_type == Structures.AXE_F) {
            if (direction == Structures.DIRECT_LEFT) {
                int ind = verge_color_index_[1];

                if (type == 0) {
                    verge_color_index_[1] = verge_color_index_[3];
                    verge_color_index_[3] = verge_color_index_[2];
                    verge_color_index_[2] = ind;
                }
                else {
                    verge_color_index_[1] = verge_color_index_[4];
                    verge_color_index_[4] = verge_color_index_[6];
                    verge_color_index_[6] = ind;
                }
            } else {
                int ind = verge_color_index_[1];

                if (type == 0) {
                    verge_color_index_[1] = verge_color_index_[2];
                    verge_color_index_[2] = verge_color_index_[3];
                    verge_color_index_[3] = ind;
                }
                else {
                    verge_color_index_[1] = verge_color_index_[6];
                    verge_color_index_[6] = verge_color_index_[4];
                    verge_color_index_[4] = ind;
                }
            }
        }

        if (rotate_type == Structures.AXE_L) {
            if (direction == Structures.DIRECT_LEFT) {
                int ind = verge_color_index_[0];

                if (type == 0) {
                    verge_color_index_[0] = verge_color_index_[1];
                    verge_color_index_[1] = verge_color_index_[3];
                    verge_color_index_[3] = ind;
                }
                else {
                    verge_color_index_[0] = verge_color_index_[6];
                    verge_color_index_[6] = verge_color_index_[1];
                    verge_color_index_[1] = ind;
                }
            } else {
                int ind = verge_color_index_[0];

                if (type == 0) {
                    verge_color_index_[0] = verge_color_index_[3];
                    verge_color_index_[3] = verge_color_index_[1];
                    verge_color_index_[1] = ind;
                }
                else {
                    verge_color_index_[0] = verge_color_index_[1];
                    verge_color_index_[1] = verge_color_index_[6];
                    verge_color_index_[6] = ind;
                }
            }
        }
        if (rotate_type == Structures.AXE_R) {
            if (direction == Structures.DIRECT_LEFT) {
                int ind = verge_color_index_[0];

                if (type == 0) {
                    verge_color_index_[0] = verge_color_index_[2];
                    verge_color_index_[2] = verge_color_index_[3];
                    verge_color_index_[3] = ind;
                }
                else {
                    verge_color_index_[0] = verge_color_index_[4];
                    verge_color_index_[4] = verge_color_index_[1];
                    verge_color_index_[1] = ind;
                }
            } else {
                int ind = verge_color_index_[0];

                if (type == 0) {
                    verge_color_index_[0] = verge_color_index_[3];
                    verge_color_index_[3] = verge_color_index_[2];
                    verge_color_index_[2] = ind;
                }
                else {
                    verge_color_index_[0] = verge_color_index_[1];
                    verge_color_index_[1] = verge_color_index_[4];
                    verge_color_index_[4] = ind;
                }
            }
        }
    }

    public void CreateExVertices(float[] point, int vertex_index) {
        if (mType == 0 ||
                mType == 1 && (vertex_index == 0 || vertex_index == 6 || vertex_index == 1 || vertex_index == 4)) {
            mNearItems = new HashMap<>();

            float dY = Pyramid.h2_3 * (float) Math.sin(Pyramid.angle);
            float dZ = Pyramid.h2_3 * (float) Math.cos(Pyramid.angle);

            float[] mid = {0, 0, 0};
            float[] mid2 = {0, 0, 0};
            float koef = 2;

            //for (int i = 0; i < 4; i++){
            int vi = vertex_index;

            if (mType == 1) {
                if (vi == 6) vi = 1;
                else {
                    if (vi == 1) vi = 3;
                    else {
                        if (vi == 4) vi = 2;
                    }
                }
            }

            mNearItems.put(vertex_index, new ArrayList<TriangleItem>());

            float[][] p = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
            float[][] new_p = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

            for (int m = 0; m < p.length; m++) {
                p[m] = GetPoint(vertex_index, m);
            }
            float[] n = GetNormal(vertex_index);

            float sign = 1;
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

                new_p[j] = Surface.Add(mid, dif);
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
            }
        }
    }

    PyramidItem(Pyramid parent, int posX, int posY, int posZ, int type, int rowL, int rowR, int rowB, int rowF, int[] verge_color_index_, int PyramidDim, float Scale) {

        mParent = parent;

        mPosX = posX;
        mPosY = posY;
        mPosZ = posZ;
        mType = type;

        mScale = Scale;

        if (mType == 0) {
            verge_color_index = new int[Pyramid.TRIANGLE_COUNT_PYRAMID];
            vertices = new float[Pyramid.TRIANGLE_COUNT_PYRAMID /*всего треугольников*/ * 3 * (Structures.POSITION_COUNT /*вершины*/ + Structures.NORMAL_COUNT /*нормали*/ + Structures.TEXTURE_COUNT /*цвет*/)];
        }
        else {
            verge_color_index = new int[Pyramid.TRIANGLE_COUNT_PYRAMID2];
            vertices = new float[Pyramid.TRIANGLE_COUNT_PYRAMID2 /*всего треугольников*/ * 3 * (Structures.POSITION_COUNT /*вершины*/ + Structures.NORMAL_COUNT /*нормали*/ + Structures.TEXTURE_COUNT /*цвет*/)];
        }

        if (verge_color_index_ == null) {
            int val = mPosX * 1000 + mPosY * 100 + mPosZ * 10 + 0;

            mParent.mLSide.add(val + rowL);
            mParent.mRSide.add(val + rowR);
            mParent.mBSide.add(val + rowB);
            mParent.mFSide.add(val + rowF);

            for (int i = 0; i < verge_color_index.length; i++)
                verge_color_index[i] = Pyramid.BLACK;

            if (mType == 0) {
                if (rowL == 0)
                    verge_color_index[2] = Pyramid.GREEN;
                if (rowR == 0)
                    verge_color_index[1] = Pyramid.BLUE;
                if (rowB == 0)
                    verge_color_index[3] = Pyramid.YELLOW;
                if (rowF == 0)
                    verge_color_index[0] = Pyramid.RED;
            }
            else
            {
                if (rowL == 0)
                    verge_color_index[4] = Pyramid.GREEN;
                if (rowR == 0)
                    verge_color_index[6] = Pyramid.BLUE;
                if (rowB == 0)
                    verge_color_index[1] = Pyramid.YELLOW;
                if (rowF == 0)
                    verge_color_index[0] = Pyramid.RED;
            }
        }
        else {
            for (int i = 0; i < verge_color_index.length; i++)
                verge_color_index[i] = verge_color_index_[i];
        }

        int cur_index = 0;
        // основные грани

        float sina = 2 * (float)Math.sqrt(2) / 3f;
        float a = 90 - (float)Math.toDegrees(Math.asin(sina));
        float y1 = (float)Math.sin(Math.toRadians(a));
        float z1 = (float)Math.cos(Math.toRadians(a));
        float zx = (float)Math.sqrt(1 - y1 * y1);
        float x2 = (float)Math.sqrt(3) / 2f * zx;
        float z2 = zx / 2;

        if (mType == 0) {
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex, 1, 2, 3, 0, y1, z1, verge_color_index[0], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex, 1, 3, 4, x2, y1, -z2, verge_color_index[1], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex, 1, 4, 2, -x2, y1, -z2, verge_color_index[2], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex, 4, 3, 2, 0, -1, 0, verge_color_index[3], PyramidDim);
        }
        else {
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 1, 2, 3, 0, y1, z1, verge_color_index[0], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 3, 6, 5, 0, -1, 0, verge_color_index[1], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 4, 2, 1, 0, 1, 0, verge_color_index[2], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 5, 6, 4, 0, -y1, -z1, verge_color_index[3], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 4, 6, 2, -x2, y1, -z2, verge_color_index[4], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 6, 3, 2, -x2, -y1, z2, verge_color_index[5], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 5, 4, 1, x2, y1, -z2, verge_color_index[6], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 5, 1, 3, x2, -y1, z2, verge_color_index[7], PyramidDim);
        }

        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertices);
    }

    PyramidItem(Pyramid parent, PyramidItem pi, int[] verge_color_index_, int PyramidDim, float Scale) {

        mParent = parent;

        mScale = Scale;

        mPosX = pi.mPosX;
        mPosY = pi.mPosY;
        mPosZ = pi.mPosZ;
        mType = pi.mType;

        if (mType == 0) {
            verge_color_index = new int[Pyramid.TRIANGLE_COUNT_PYRAMID];
            vertices = new float[Pyramid.TRIANGLE_COUNT_PYRAMID /*всего треугольников*/ * 3 * (Structures.POSITION_COUNT /*вершины*/ + Structures.NORMAL_COUNT /*нормали*/ + Structures.TEXTURE_COUNT /*цвет*/)];
        }
        else {
            verge_color_index = new int[Pyramid.TRIANGLE_COUNT_PYRAMID2];
            vertices = new float[Pyramid.TRIANGLE_COUNT_PYRAMID2 /*всего треугольников*/ * 3 * (Structures.POSITION_COUNT /*вершины*/ + Structures.NORMAL_COUNT /*нормали*/ + Structures.TEXTURE_COUNT /*цвет*/)];
        }

        if (verge_color_index_ != null) {
            for (int i = 0; i < verge_color_index.length; i++)
                verge_color_index[i] = verge_color_index_[i];
        }

        int cur_index = 0;
        // основные грани

        float sina = 2 * (float)Math.sqrt(2) / 3f;
        float a = 90 - (float)Math.toDegrees(Math.asin(sina));
        float y1 = (float)Math.sin(Math.toRadians(a));
        float z1 = (float)Math.cos(Math.toRadians(a));
        float zx = (float)Math.sqrt(1 - y1 * y1);
        float x2 = (float)Math.sqrt(3) / 2f * zx;
        float z2 = zx / 2;

        if (mType == 0) {
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex, 1, 2, 3, 0, y1, z1, verge_color_index[0], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex, 1, 3, 4, x2, y1, -z2, verge_color_index[1], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex, 1, 4, 2, -x2, y1, -z2, verge_color_index[2], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex, 4, 3, 2, 0, -1, 0, verge_color_index[3], PyramidDim);
        }
        else {
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 1, 2, 3, 0, y1, z1, verge_color_index[0], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 3, 6, 5, 0, -1, 0, verge_color_index[1], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 4, 2, 1, 0, 1, 0, verge_color_index[2], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 5, 6, 4, 0, -y1, -z1, verge_color_index[3], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 4, 6, 2, -x2, y1, -z2, verge_color_index[4], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 6, 3, 2, -x2, -y1, z2, verge_color_index[5], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 5, 4, 1, x2, y1, -z2, verge_color_index[6], PyramidDim);
            cur_index = SetTriangle(vertices, cur_index, Pyramid.vertex2, 5, 1, 3, x2, -y1, z2, verge_color_index[7], PyramidDim);
        }

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
        if (rotateType != null) {
            if (Math.abs(rotateType[0]) <= 1 && Math.abs(rotateType[1]) <= 1 && Math.abs(rotateType[2]) <= 1) {
                Matrix.rotateM(mModelMatrix, 0, angle, rotateType[0], rotateType[1], rotateType[2]);
            }
            else {
                if (Math.abs(rotateType[1]) > 1 && Math.abs(rotateType[2]) > 1) {
                    float[] mYZ = new float[16];
                    Matrix.setIdentityM(mYZ, 0);

                    float[] mY = new float[16];
                    Matrix.setIdentityM(mY, 0);
                    Matrix.rotateM(mY, 0, rotateType[1], 0, 1, 0);

                    float[] mZ = new float[16];
                    Matrix.setIdentityM(mZ, 0);
                    Matrix.rotateM(mZ, 0, rotateType[2], 0, 0, 1);
                    Matrix.multiplyMM(mYZ, 0, mZ, 0, mY, 0);

                    float[] mX = new float[16];
                    Matrix.setIdentityM(mX, 0);
                    Matrix.rotateM(mX, 0, angle, 1, 0, 0);

                    Matrix.multiplyMM(mModelMatrix, 0, mX, 0, mYZ, 0);

                    Matrix.setIdentityM(mZ, 0);
                    Matrix.rotateM(mZ, 0, -rotateType[2], 0, 0, 1);
                    Matrix.multiplyMM(mYZ, 0, mZ, 0, mModelMatrix, 0);

                    Matrix.setIdentityM(mY, 0);
                    Matrix.rotateM(mY, 0, -rotateType[1], 0, 1, 0);
                    Matrix.multiplyMM(mModelMatrix, 0, mY, 0, mYZ, 0);

                }
                if (Math.abs(rotateType[0]) > 1) {

                    float[] mX = new float[16];
                    Matrix.setIdentityM(mX, 0);
                    Matrix.rotateM(mX, 0, rotateType[0], 1, 0, 0);

                    float[] mZ = new float[16];
                    Matrix.setIdentityM(mZ, 0);
                    Matrix.rotateM(mZ, 0, angle, 0, 0, 1);

                    float[] mZX = new float[16];
                    Matrix.setIdentityM(mZX, 0);
                    Matrix.multiplyMM(mZX, 0, mZ, 0, mX, 0);

                    Matrix.setIdentityM(mX, 0);
                    Matrix.rotateM(mX, 0, -rotateType[0], 1, 0, 0);
                    Matrix.multiplyMM(mModelMatrix, 0, mX, 0, mZX, 0);

                }
            }
        }
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

        if (mType == 0)
            glDrawArrays(GL_TRIANGLES, 0, Pyramid.TRIANGLE_COUNT_PYRAMID * Structures.POSITION_COUNT);
        else
            glDrawArrays(GL_TRIANGLES, 0, Pyramid.TRIANGLE_COUNT_PYRAMID2 * Structures.POSITION_COUNT);

        GLES20.glDisableVertexAttribArray(FigureItemLocation.aPositionLocation);
        GLES20.glDisableVertexAttribArray(FigureItemLocation.aNormalLocation);
        GLES20.glDisableVertexAttribArray(FigureItemLocation.aTextureLocation);
    }
}
