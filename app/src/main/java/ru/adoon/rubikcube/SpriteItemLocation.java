package ru.adoon.rubikcube;

import android.opengl.GLES20;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;

/**
 * Created by Лукшин on 02.11.2017.
 */

public class SpriteItemLocation {
    public static int aPositionLocation;
    public static int aTextureLocation;
    public static int uMatrixLocation;
    public static int uTextureUnitLocation;

    static void GetLocations(int programId) {
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        aTextureLocation = glGetAttribLocation(programId, "a_Texture");
        uTextureUnitLocation = glGetUniformLocation(programId, "u_TextureUnit");
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
    }
}
