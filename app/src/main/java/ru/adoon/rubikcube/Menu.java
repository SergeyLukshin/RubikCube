package ru.adoon.rubikcube;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Лукшин on 08.11.2017.
 */

public class Menu {

    public static final int menu_do_main = 0;
    public static final int menu_do_shuffle = 1;
    public static final int menu_do_exit = 2;
    public static final int menu_do_complete = 3;
    public static final int menu_do_figure = 4;
    public static final int menu_do_rotate = 5;
    public static final int menu_do_resume = 6;
    public static final int menu_do_complete_no_time = 7;
    public static final int menu_do_size_cube = 8;
    public static final int menu_do_size_pyramid = 9;
    public static final int menu_do_size_dcube = 10;
    public static final int menu_do_size_empty_cube = 11;
    public static final int menu_do_size_floppy_cube = 12;
    public static final int menu_do_language = 13;
    public static final int menu_do_rotate_axes = 14;
    public static final int menu_do_rotate_block = 15;
    public static final int menu_do_camera_speed = 16;

    public static final int menu_none = -1;

    public static final int menu_do_main_language = 0;
    public static final int menu_do_main_new_game = 1;
    public static final int menu_do_main_timer = 2;
    public static final int menu_do_main_figure = 3;
    public static final int menu_do_main_rotate = 4;
    public static final int menu_do_main_speed = 5;
    public static final int menu_do_main_exit = 6;

    public static final int menu_do_resume_new_game = 0;
    public static final int menu_do_resume_timer = 1;
    public static final int menu_do_resume_figure = 2;
    public static final int menu_do_resume_rotate = 3;
    public static final int menu_do_resume_speed = 4;

    public static final int menu_do_exit_exit = 0;
    public static final int menu_do_exit_yes = 1;
    public static final int menu_do_exit_no = 2;

    public static final int menu_do_shuffle_shuffle = 0;
    public static final int menu_do_shuffle_yes = 1;
    public static final int menu_do_shuffle_no = 2;

    public static final int menu_do_complete_complete = 0;
    public static final int menu_do_complete_time = 1;
    public static final int menu_do_complete_time_val = 2;
    public static final int menu_do_complete_cnt_steps = 3;
    public static final int menu_do_complete_cnt_steps_val = 4;

    public static final int menu_do_complete_no_time_complete = 0;
    public static final int menu_do_complete_no_time_cnt_steps = 1;
    public static final int menu_do_complete_no_time_cnt_steps_val = 2;
    //public static final int menu_new_game2 = 5;
    //public static final int menu_close2 = 6;

    //public static final int menu_figure_type = 0;
    public static final int menu_do_figure_cube = 0;
    public static final int menu_do_figure_pyramid = 1;
    public static final int menu_do_figure_domino_cube = 2;
    public static final int menu_do_figure_empty_cube = 3;
    public static final int menu_do_figure_floppy_cube = 4;

    public static final int menu_do_size_cube_222 = 0;
    public static final int menu_do_size_cube_333 = 1;
    public static final int menu_do_size_cube_444 = 2;
    public static final int menu_do_size_cube_555 = 3;

    public static final int menu_do_size_pyramid_222 = 0;
    public static final int menu_do_size_pyramid_333 = 1;
    public static final int menu_do_size_pyramid_444 = 2;
    public static final int menu_do_size_pyramid_555 = 3;

    public static final int menu_do_size_dcube_233 = 0;
    public static final int menu_do_size_dcube_244 = 1;
    public static final int menu_do_size_dcube_344 = 2;
    public static final int menu_do_size_dcube_255 = 3;
    public static final int menu_do_size_dcube_355 = 4;
    public static final int menu_do_size_dcube_455 = 5;

    public static final int menu_do_size_empty_cube_333 = 0;
    public static final int menu_do_size_empty_cube_555 = 1;

    public static final int menu_do_size_floppy_cube_122 = 0;
    public static final int menu_do_size_floppy_cube_133 = 1;
    public static final int menu_do_size_floppy_cube_144 = 2;
    public static final int menu_do_size_floppy_cube_155 = 3;

    public static final int menu_do_rotate_axes_type = 0;
    public static final int menu_do_rotate_any_type = 1;
    public static final int menu_do_rotate_block_set = 2;

    /*public static final int menu_do_rotate_rotate_type = 0;
    public static final int menu_do_rotate_rotate1 = 1;
    public static final int menu_do_rotate_rotate2 = 2;
    public static final int menu_do_rotate_double_tap = 3;*/

    public static final int menu_do_russian = 0;
    public static final int menu_do_english = 1;

    public static final int menu_do_rotate_axes_visibility = 0;
    public static final int menu_do_rotate_axes_1_side = 1;
    public static final int menu_do_rotate_axes_2_side = 2;
    public static final int menu_do_rotate_axes_3_side = 3;

    public static final int menu_do_rotate_block_info = 0;
    public static final int menu_do_rotate_block_none = 1;
    public static final int menu_do_rotate_block_rotate1 = 2;
    public static final int menu_do_rotate_block_rotate2 = 3;
    public static final int menu_do_rotate_block_double_tap = 4;

    public static final int menu_do_1_camera_speed = 0;
    public static final int menu_do_1_5_camera_speed = 1;
    public static final int menu_do_2_camera_speed = 2;

    //public static final int menu_new_game3 = 1;
    //public static final int menu_close3 = 2;

    boolean m_bMenuIsEnable = false; // необходимо сохранять
    boolean m_bMenuIsLoad = false;
    boolean m_bMenuIsUnload = false;
    ArrayList<ArrayList<MenuItemSprite>> menu = new ArrayList<ArrayList<MenuItemSprite>>();
    Background mBackground;
    float dist = 0.5f;
    int mActiveMenuIndex = -1;
    //int mPrevActiveMenuIndex = -1;

    TimeInfo mTime = null;
    StepsInfo mStepCnt = null;
    StepsInfo mStepCnt2 = null;

    ArrayList<Integer> menuHistory = new ArrayList<Integer>();

    //Context mCtx;
    float mParentWidth;
    float mParentHeight;
    float mRatioX;
    float mRatioY;

    //int mLanguage;

    public void SetContext(Context context) {

        //mCtx = context;
        mBackground.SetContext(context);

        for (int i = 0 ; i < menu.size(); i++) {
            for (int j = 0; j < menu.get(i).size(); j++)
                menu.get(i).get(j).SetContext(context);
        }

        if (mTime != null) mTime.SetContext(context);
        if (mStepCnt != null) mStepCnt.SetContext(context);
        if (mStepCnt2 != null) mStepCnt2.SetContext(context);
    }

    Menu(Context mCtx, int language)
    {
        //mCtx = ctx;
        //list.add(new MenuItemSprite(ctx, 0, 4, 1f * 422f / 460f, R.drawable.menu_settings, 0));
        //mLanguage = language;

        //SetLanguage(mCtx, language);

        mTime = new TimeInfo(mCtx);
        mStepCnt = new StepsInfo(mCtx);
        mStepCnt2 = new StepsInfo(mCtx);

        long origThreadID = Thread.currentThread().getId();

        //list.add(new MenuItemSprite(ctx, 0, 4, 1f * 422f / 460f, R.drawable.menu_settings, 0));
        /*for (int i = 0 ; i < menu.size(); i++) {
            for (int j = 0; j < menu.get(i).size(); j++)
                menu.get(i).get(j).DeleteTexture();
        }*/

        menu.clear();

        ArrayList<MenuItemSprite> list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_main_language, 7, R.drawable.menu_language, 0, R.drawable.menu_language, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_main_new_game, 7, R.drawable.menu_new_game, 0, R.drawable.menu_new_game_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_main_timer, 7, R.drawable.menu_timer_off, R.drawable.menu_timer_on, R.drawable.menu_timer_off_en, R.drawable.menu_timer_on_en, true));
            list.add(new MenuItemSprite(mCtx, menu_do_main_figure, 7, R.drawable.menu_figure, 0, R.drawable.menu_figure_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_main_rotate, 7, R.drawable.menu_rotate, 0, R.drawable.menu_rotate_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_main_speed, 7, R.drawable.menu_camera_speed, 0, R.drawable.menu_camera_speed_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_main_exit, 7, R.drawable.menu_exit2, 0, R.drawable.menu_exit2_en, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_shuffle_shuffle, 3, R.drawable.menu_shuffle, 0, R.drawable.menu_shuffle_en, 0, false));
            list.add(new MenuItemSprite(mCtx, menu_do_shuffle_yes, 3, R.drawable.menu_yes, 0, R.drawable.menu_yes_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_shuffle_no, 3, R.drawable.menu_no, 0, R.drawable.menu_no_en, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_exit_exit, 3, R.drawable.menu_exit, 0, R.drawable.menu_exit_en, 0, false));
            list.add(new MenuItemSprite(mCtx, menu_do_exit_yes, 3, R.drawable.menu_yes, 0, R.drawable.menu_yes_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_exit_no, 3, R.drawable.menu_no, 0, R.drawable.menu_no_en, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_complete_complete, 5, R.drawable.menu_complete, 0, R.drawable.menu_complete_en, 0, false));
            list.add(new MenuItemSprite(mCtx, menu_do_complete_time, 5, R.drawable.menu_time, 0, R.drawable.menu_time_en, 0, false));
            list.add(new MenuItemSprite(mCtx, menu_do_complete_time_val, 5, 0, 0, false));
            list.add(new MenuItemSprite(mCtx, menu_do_complete_cnt_steps, 5, R.drawable.menu_cnt_steps, 0, R.drawable.menu_cnt_steps_en, 0, false));
            list.add(new MenuItemSprite(mCtx, menu_do_complete_cnt_steps_val, 5, 0, 0, false));
            //list.add(new MenuItemSprite(ctx, menu_new_game2, 7, 1f * 422f / 460f, R.drawable.menu_new_game, 0, true));
            //list.add(new MenuItemSprite(ctx, menu_close2, 7, 1f * 350f / 460f, R.drawable.menu_close, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_figure_cube, 5, R.drawable.menu_cube, 0, R.drawable.menu_cube_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_figure_pyramid, 5, R.drawable.menu_pyramid, 0, R.drawable.menu_pyramid_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_figure_domino_cube, 5, R.drawable.menu_dcube, 0, R.drawable.menu_dcube_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_figure_empty_cube, 5, R.drawable.menu_empty_cube, 0, R.drawable.menu_empty_cube_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_figure_floppy_cube, 5, R.drawable.menu_fcube, 0, R.drawable.menu_fcube_en, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_axes_type, 3, R.drawable.menu_only_axes, 0, R.drawable.menu_only_axes_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_any_type, 3, R.drawable.menu_any, 0, R.drawable.menu_any_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_block_set, 3, R.drawable.menu_block, 0, R.drawable.menu_block_en, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_resume_new_game, 5, R.drawable.menu_new_game, 0, R.drawable.menu_new_game_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_resume_timer, 5, R.drawable.menu_timer_off, R.drawable.menu_timer_on, R.drawable.menu_timer_off_en, R.drawable.menu_timer_on_en, true));
            list.add(new MenuItemSprite(mCtx, menu_do_resume_figure, 5, R.drawable.menu_figure, 0, R.drawable.menu_figure_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_resume_rotate, 5, R.drawable.menu_rotate, 0, R.drawable.menu_rotate_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_resume_speed, 5, R.drawable.menu_camera_speed, 0, R.drawable.menu_camera_speed_en, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_complete_no_time_complete, 3, R.drawable.menu_complete, 0, R.drawable.menu_complete_en, 0, false));
            list.add(new MenuItemSprite(mCtx, menu_do_complete_no_time_cnt_steps, 3, R.drawable.menu_cnt_steps, 0, R.drawable.menu_cnt_steps_en, 0, false));
            list.add(new MenuItemSprite(mCtx, menu_do_complete_no_time_cnt_steps_val, 3, 0, 0, false));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_size_cube_222, 4, R.drawable.menu_size222, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_cube_333, 4, R.drawable.menu_size333, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_cube_444, 4, R.drawable.menu_size444, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_cube_555, 4, R.drawable.menu_size555, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_size_pyramid_222, 4, R.drawable.menu_size222, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_pyramid_333, 4, R.drawable.menu_size333, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_pyramid_444, 4, R.drawable.menu_size444, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_pyramid_555, 4, R.drawable.menu_size555, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_size_dcube_233, 6, R.drawable.menu_size233, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_dcube_244, 6, R.drawable.menu_size244, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_dcube_344, 6, R.drawable.menu_size344, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_dcube_255, 6, R.drawable.menu_size255, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_dcube_355, 6, R.drawable.menu_size355, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_dcube_455, 6, R.drawable.menu_size455, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_size_empty_cube_333, 2, R.drawable.menu_size333, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_empty_cube_555, 2, R.drawable.menu_size555, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_size_floppy_cube_122, 4, R.drawable.menu_size122, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_floppy_cube_133, 4, R.drawable.menu_size133, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_floppy_cube_144, 4, R.drawable.menu_size144, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_size_floppy_cube_155, 4, R.drawable.menu_size155, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_russian, 2, R.drawable.menu_russian, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_english, 2, R.drawable.menu_english, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_axes_visibility, 4, R.drawable.menu_visibility, 0, R.drawable.menu_visibility_en, 0, false));
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_axes_1_side, 4, R.drawable.menu_1_side, 0, R.drawable.menu_1_side_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_axes_2_side, 4, R.drawable.menu_2_side, 0, R.drawable.menu_2_side_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_axes_3_side, 4, R.drawable.menu_3_side, 0, R.drawable.menu_3_side_en, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_block_info, 5, R.drawable.menu_rotate_type, 0, R.drawable.menu_rotate_type, 0, false));
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_block_none, 5, R.drawable.menu_no, 0, R.drawable.menu_no_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_block_rotate1, 5, R.drawable.menu_rotate1, 0, R.drawable.menu_rotate1_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_block_rotate2, 5, R.drawable.menu_rotate2, 0, R.drawable.menu_rotate2_en, 0, true));
            list.add(new MenuItemSprite(mCtx, menu_do_rotate_block_double_tap, 5, R.drawable.menu_double_tap_off, R.drawable.menu_double_tap_on, R.drawable.menu_double_tap_off_en, R.drawable.menu_double_tap_on_en, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(mCtx, menu_do_1_camera_speed, 3, R.drawable.menu_1_speed, 0,true));
            list.add(new MenuItemSprite(mCtx, menu_do_1_5_camera_speed, 3, R.drawable.menu_1_5_speed, 0,true));
            list.add(new MenuItemSprite(mCtx, menu_do_2_camera_speed, 3, R.drawable.menu_2_speed, 0,true));
        }
        menu.add(list);

        SetLanguage(language);

        mBackground = new Background(mCtx, true);
    }

    public void SetLanguage(int language)
    {
        //mLanguage = language;

        for (int i = 0 ; i < menu.size(); i++) {
            for (int j = 0; j < menu.get(i).size(); j++)
                menu.get(i).get(j).SetLanguage(language);
        }
    }

    public void SetRatio() {
        for (int i = 0 ; i < menu.size(); i++) {
            for (int j = 0; j < menu.get(i).size(); j++) {
                menu.get(i).get(j).SetRatio(mParentWidth, mParentHeight, mRatioX, mRatioY);
                menu.get(i).get(j).SetPos(dist);
            }
        }

        if (mTime != null) {
            mTime.SetY(menu.get(menu_do_complete).get(menu_do_complete_time_val).GetPosY());
            mTime.SetRatio(mParentWidth, mParentHeight, mRatioX, mRatioY);
        }

        if (mStepCnt != null) {
            mStepCnt.SetY(menu.get(menu_do_complete).get(menu_do_complete_cnt_steps_val).GetPosY());
            mStepCnt.SetRatio(mParentWidth, mParentHeight, mRatioX, mRatioY);
        }

        if (mStepCnt2 != null) {
            mStepCnt2.SetY(menu.get(menu_do_complete_no_time).get(menu_do_complete_no_time_cnt_steps_val).GetPosY());
            mStepCnt2.SetRatio(mParentWidth, mParentHeight, mRatioX, mRatioY);
        }
    }

    public void SetRatio(float parentWidth, float parentHeight,
         float ratioX, float ratioY)
    {
        mParentWidth = parentWidth;
        mParentHeight = parentHeight;
        mRatioX = ratioX;
        mRatioY = ratioY;

        SetRatio();
    }

    public void SetTexture(int index, int pos) {
        if (mActiveMenuIndex < 0) return;
        if (index >= 0 && index < menu.get(mActiveMenuIndex).size())
            menu.get(mActiveMenuIndex).get(index).SetTexture(pos);
    }

    public int GetActiveMenuIndex() {
        return mActiveMenuIndex;
    }

    public int GetMenuPressed(float x, float y) {
        if (mActiveMenuIndex < 0 || mActiveMenuIndex >= menu.size()) return Menu.menu_none;
        for (int i = 0; i < menu.get(mActiveMenuIndex).size(); i++) {
            if (/*menu.get(mActiveMenuIndex).get(i).IsEnabled() &&*/ menu.get(mActiveMenuIndex).get(i).IsPressed(x, y))
                return i;
        }

        return Menu.menu_none;
    }

    public boolean MenuIsEnable() {
        return m_bMenuIsEnable || m_bMenuIsLoad || m_bMenuIsUnload;
    }

    public boolean MenuIsComplete() {
        return m_bMenuIsEnable && !m_bMenuIsLoad && !m_bMenuIsUnload;
    }

    public void PrevMenuShow() {
        if (menuHistory.size() > 1) {
            menuHistory.remove(menuHistory.size() - 1);
            int activeMenuIndex = menuHistory.get(menuHistory.size() - 1);
            menuHistory.remove(menuHistory.size() - 1);
            MenuShow(activeMenuIndex);
        }
    }

    public void MenuShow(int activeMenuIndex) {

        int iCntWait = 0;
        while (m_bMenuIsLoad || m_bMenuIsUnload) {
            try {
                Thread.sleep(10);
                iCntWait ++;
                if (iCntWait > 100) break;
            }catch (InterruptedException ex) {
            }
        }

        if (activeMenuIndex == menu_do_main || activeMenuIndex == menu_do_resume)
            menuHistory.clear();

        menuHistory.add(activeMenuIndex);

        //MenuEndUnload();
        mActiveMenuIndex = activeMenuIndex;
        //mPrevActiveMenuIndex = activeMenuIndex;

        m_bMenuIsLoad = true;
        dist = 0.5f;

        if (activeMenuIndex == menu_do_complete)
        {
            if (mTime != null && GLRenderer.data != null && GLRenderer.data.mClock != null) {
                mTime.SetTime(GLRenderer.data.mClock.mDuration);
            }

            if (mStepCnt != null && GLRenderer.data != null) {
                mStepCnt.SetStepsCnt(GLRenderer.data.mCntSteps);
            }
        }

        if (activeMenuIndex == menu_do_complete_no_time)
        {
            if (mStepCnt2 != null && GLRenderer.data != null) {
                mStepCnt2.SetStepsCnt(GLRenderer.data.mCntSteps);
            }
        }
    }

    public void MenuEndLoad() {
        m_bMenuIsLoad = false;
        m_bMenuIsEnable = true;
    }

    public void MenuEndUnload() {
        m_bMenuIsEnable = false;
        mActiveMenuIndex = -1;
        m_bMenuIsUnload = false;
    }

    public void MenuInit() {
        m_bMenuIsEnable = false;
        mActiveMenuIndex = -1;
        m_bMenuIsLoad = false;
        m_bMenuIsUnload = false;
    }

    public void MenuClose() {

        int iCntWait = 0;
        while (m_bMenuIsLoad || m_bMenuIsUnload) {
            try {
                Thread.sleep(10);
                iCntWait ++;
                if (iCntWait > 100) break;
            }catch (InterruptedException ex) {
            }
        }

        m_bMenuIsUnload = true;
    }

    public void Draw() {

        if (!MenuIsEnable()) return;

        int ActiveMenuIndex = mActiveMenuIndex;

        mBackground.Draw();

        if (m_bMenuIsLoad) {
            dist -= 0.1f;
            if (dist <= 0) {
                dist = 0;
                MenuEndLoad();
            }
            if (ActiveMenuIndex >= 0) {
                for (int i = 0; i < menu.get(ActiveMenuIndex).size(); i++) {
                    menu.get(ActiveMenuIndex).get(i).SetPos(dist);
                }
            }
        }

        if (m_bMenuIsUnload) {
            dist += 0.1f;
            if (dist >= 0.5f) {
                dist = 0.5f;
                MenuEndUnload();
                return;
            }

            if (ActiveMenuIndex >= 0) {
                for (int i = 0; i < menu.get(ActiveMenuIndex).size(); i++) {
                    menu.get(ActiveMenuIndex).get(i).SetPos(dist);
                }
            }
        }

        if (ActiveMenuIndex >= 0) {
            for (int i = 0; i < menu.get(ActiveMenuIndex).size(); i++) {
                if (ActiveMenuIndex == menu_do_complete && MenuIsComplete()) {
                    if (i == menu_do_complete_time_val) mTime.Draw();
                    if (i == menu_do_complete_cnt_steps_val) mStepCnt.Draw();
                }
                if (ActiveMenuIndex == menu_do_complete_no_time && MenuIsComplete()) {
                    if (i == menu_do_complete_no_time_cnt_steps_val) mStepCnt2.Draw();
                }
                menu.get(ActiveMenuIndex).get(i).Draw();
            }
        }
    }
}
