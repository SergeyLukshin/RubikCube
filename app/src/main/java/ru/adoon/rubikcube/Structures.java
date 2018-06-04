package ru.adoon.rubikcube;

/**
 * Created by Лукшин on 27.10.2017.
 */

public class Structures {

    public static final int CUBE = 0;
    public static final int PYRAMID = 1;
    public static final int DOMINO_CUBE = 2;

    public final static int POSITION_COUNT = 3;
    public static final int NORMAL_COUNT = 3;
    public static final int COLOR_COUNT = 3;
    public static final int TEXTURE_COUNT = 2;
    public static final int STRIDE = (POSITION_COUNT + NORMAL_COUNT + COLOR_COUNT) * 4;
    public static final int STRIDE2 = (POSITION_COUNT + NORMAL_COUNT + TEXTURE_COUNT) * 4;
    public static final int TRIANGLE_COUNT = 12 + 16 + 8 + 8;

    public static final int ACTION_NONE = 0;
    public static final int ACTION_SCALE = 1;
    public static final int ACTION_MOVE = 2;
    public static final int ACTION_DO = 3;
    //public static final int ACTION_DOWN = 4;

    public static final int AXE_X = 0;
    public static final int AXE_Y = 1;
    public static final int AXE_Z = 2;

    public static final int AXE_L = 0;
    public static final int AXE_R = 1;
    public static final int AXE_B = 2;
    public static final int AXE_F = 3 ;

    public static final int DIRECT_LEFT = -1;
    public static final int DIRECT_RIGHT = 1;
    public static final int DIRECT_NONE = 0;

    public static final int ROTATE_ALL_FIGURE = 0;
    public static final int ROTATE_CAMERA_FIGURE = 1;

    public static final int LOCK_CAMERA = 0;
    public static final int UNLOCK = 1;

    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_LEFT = 2;

    public static float[] Light = new float[4];
    public static float[] CameraEye = new float[4];
    public static float[] CameraUp = new float[4];
    public static float[] PointView = {12f, 12f, 12f, 0};

    public static final float CameraEyeDistance = 30;
    public static final float[] LightInit = {12f, 12f, 12f, 0};
    public static final float[] CameraEyeInit = {0f, 0f, 1f, 1};
    public static final float[] CameraUpInit = {0f, 1.0f, 0, 1};
    public static final float[] CameraCenter = {0, 0, 0};

    public static final float[] text_rt = {1f, 0f};
    public static final float[] text_lt = {0f, 0f};
    public static final float[] text_rb = {1f, 1f};
    public static final float[] text_lb = {0f, 1f};


    static final float h = 0.08f;
    public static final float[] vertex = {
            1 - h, 1 - h , 1,
            -(1 - h), 1 - h, 1,
            -(1 - h), -(1 - h), 1,
            1 - h, -(1 - h), 1,

            1, 1 - h, -(1 - h),
            1, 1 - h, 1 - h,
            1, -(1 - h), 1 - h,
            1, -(1 - h), -(1 - h),

            -(1 - h), 1 - h, -1,
            1 - h, 1 - h, -1,
            1 - h, -(1 - h), -1,
            -(1 - h), -(1 - h), -1,

            -1, 1 - h, 1 - h,
            -1, 1 - h, -(1 - h),
            -1, -(1 - h), -(1 - h),
            -1, -(1 - h), 1 - h,

            -(1 - h), 1, -(1 - h),
            -(1 - h), 1, 1 - h,
            1 - h, 1, 1 - h,
            1 - h, 1, -(1 - h),

            1 - h, -1, -(1 - h),
            1 - h, -1, 1 - h,
            -(1 - h), -1, 1 - h,
            -(1 - h), -1, -(1 - h),
    };



    public static final float[] black = {0, 0, 0};
    public static final float[] white = {1, 1, 1};
    public static final float[] green = {0, 1, 0};
    public static final float[] red = {1, 0, 0};
    public static final float[] blue = {0, 0, 1};
    public static final float[] yellow = {1, 1, 0};
    public static final float[] orange = {1, 0.5f, 0};

    public static float GetRealSizeFromGL(float full_size, float sizeGL) {
        return full_size * sizeGL / 2;
    }
}
