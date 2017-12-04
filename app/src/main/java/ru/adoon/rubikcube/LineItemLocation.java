package ru.adoon.rubikcube;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;

/**
 * Created by Лукшин on 02.11.2017.
 */

public class LineItemLocation {

    public static int aPositionLocation;
    public static int uMatrixLocation;

    static void GetLocations(int programId) {
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
    }
}
