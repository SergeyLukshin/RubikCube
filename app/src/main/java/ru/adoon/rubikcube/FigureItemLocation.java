package ru.adoon.rubikcube;

import android.opengl.GLES20;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;

/**
 * Created by Лукшин on 02.11.2017.
 */

public class FigureItemLocation {
    public static int aPositionLocation;
    public static int aColorLocation;
    public static int aTextureLocation;
    public static int uTextureUnitLocation;
    public static int uMatrixLocation;
    public static int uModelMatrixLocation;
    public static int uLightLocation;
    public static int aNormalLocation;
    public static int uCameraLocation;
    public static int uSelectLocation;

    static void GetLocations(int programId) {
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        aColorLocation = glGetAttribLocation(programId, "a_Color");
        aTextureLocation = glGetAttribLocation(programId, "a_Texture");
        uTextureUnitLocation = glGetUniformLocation(programId, "u_TextureUnit");
        aNormalLocation = GLES20.glGetAttribLocation(programId, "a_Normal");
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
        uModelMatrixLocation = glGetUniformLocation(programId, "u_ModelMatrix");
        uLightLocation = GLES20.glGetUniformLocation(programId, "u_Light");
        uCameraLocation = GLES20.glGetUniformLocation(programId, "u_Camera");
        uSelectLocation = GLES20.glGetUniformLocation(programId, "u_Select");
    }
}
