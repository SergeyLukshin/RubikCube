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
    public static final int menu_do_main_no_exit = 6;
    public static final int menu_do_complete_no_time = 7;
    public static final int menu_do_size_cube = 8;
    public static final int menu_do_size_pyramid = 9;
    public static final int menu_do_size_dcube = 10;
    public static final int menu_do_size_empty_cube = 11;

    public static final int menu_none = -1;

    public static final int menu_do_main_new_game = 0;
    public static final int menu_do_main_timer = 1;
    public static final int menu_do_main_figure = 2;
    public static final int menu_do_main_rotate = 3;
    public static final int menu_do_main_exit = 4;

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

    public static final int menu_do_rotate_rotate_type = 0;
    public static final int menu_do_rotate_rotate1 = 1;
    public static final int menu_do_rotate_rotate2 = 2;
    public static final int menu_do_rotate_double_tap = 3;


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

    public void SetContext(Context context) {
        mBackground.SetContext(context);

        for (int i = 0 ; i < menu.size(); i++) {
            for (int j = 0; j < menu.get(i).size(); j++)
                menu.get(i).get(j).SetContext(context);
        }

        if (mTime != null) mTime.SetContext(context);
        if (mStepCnt != null) mStepCnt.SetContext(context);
        if (mStepCnt2 != null) mStepCnt2.SetContext(context);
    }

    Menu(Context ctx)
    {
        //list.add(new MenuItemSprite(ctx, 0, 4, 1f * 422f / 460f, R.drawable.menu_settings, 0));
        ArrayList<MenuItemSprite> list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_main_new_game, 5, 1f * 422f / 460f, R.drawable.menu_new_game, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_main_timer, 5, 1f * 400f / 460f, R.drawable.menu_timer_off, R.drawable.menu_timer_on, true));
            list.add(new MenuItemSprite(ctx, menu_do_main_figure, 5, 1f * 420f / 460f, R.drawable.menu_figure, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_main_rotate, 5, 1f * 460f / 460f, R.drawable.menu_rotate, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_main_exit, 5, 1f * 260f / 460f, R.drawable.menu_exit2, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_shuffle_shuffle, 3, 1f * 470f / 460f, R.drawable.menu_shuffle, 0, false));
            list.add(new MenuItemSprite(ctx, menu_do_shuffle_yes, 3, 1f * 210f / 460f, R.drawable.menu_yes, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_shuffle_no, 3, 1f * 210f / 460f, R.drawable.menu_no, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_exit_exit, 3, 1f * 470f / 460f, R.drawable.menu_exit, 0, false));
            list.add(new MenuItemSprite(ctx, menu_do_exit_yes, 3, 1f * 210f / 460f, R.drawable.menu_yes, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_exit_no, 3, 1f * 210f / 460f, R.drawable.menu_no, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_complete_complete, 5, 1f * 701f / 460f, R.drawable.menu_complete, 0, false));
            list.add(new MenuItemSprite(ctx, menu_do_complete_time, 5, 1f * 440f / 460f, R.drawable.menu_time, 0, false));
            list.add(new MenuItemSprite(ctx, menu_do_complete_time_val, 5, 0, 0, 0, false));
            list.add(new MenuItemSprite(ctx, menu_do_complete_cnt_steps, 5, 1f * 440f / 460f, R.drawable.menu_cnt_steps, 0, false));
            list.add(new MenuItemSprite(ctx, menu_do_complete_cnt_steps_val, 5, 0, 0, 0, false));
            //list.add(new MenuItemSprite(ctx, menu_new_game2, 7, 1f * 422f / 460f, R.drawable.menu_new_game, 0, true));
            //list.add(new MenuItemSprite(ctx, menu_close2, 7, 1f * 350f / 460f, R.drawable.menu_close, 0, true));

            mTime = new TimeInfo(ctx);
            mStepCnt = new StepsInfo(ctx);
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_figure_cube, 4, 1f * 210f / 460f, R.drawable.menu_cube, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_figure_pyramid, 4, 1f * 370f / 460f, R.drawable.menu_pyramid, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_figure_domino_cube, 4, 1f * 420f / 460f, R.drawable.menu_dcube, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_figure_empty_cube, 4, 1f * 360f / 460f, R.drawable.menu_empty_cube, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_rotate_rotate_type, 4, 1f * 300f / 460f, R.drawable.menu_rotate_type, 0, false));
            list.add(new MenuItemSprite(ctx, menu_do_rotate_rotate1, 4, 1f * 760f / 460f, R.drawable.menu_rotate1, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_rotate_rotate2, 4, 1f * 520f / 460f, R.drawable.menu_rotate2, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_rotate_double_tap, 4, 1f * 520f / 460f, R.drawable.menu_double_tap_off, R.drawable.menu_double_tap_on, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_main_new_game, 4, 1f * 422f / 460f, R.drawable.menu_new_game, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_main_timer, 4, 1f * 400f / 460f, R.drawable.menu_timer_off, R.drawable.menu_timer_on, true));
            list.add(new MenuItemSprite(ctx, menu_do_main_figure, 4, 1f * 420f / 460f, R.drawable.menu_figure, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_main_rotate, 4, 1f * 460f / 460f, R.drawable.menu_rotate, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_complete_no_time_complete, 3, 1f * 701f / 460f, R.drawable.menu_complete, 0, false));
            list.add(new MenuItemSprite(ctx, menu_do_complete_no_time_cnt_steps, 3, 1f * 440f / 460f, R.drawable.menu_cnt_steps, 0, false));
            list.add(new MenuItemSprite(ctx, menu_do_complete_no_time_cnt_steps_val, 3, 0, 0, 0, false));
            //list.add(new MenuItemSprite(ctx, menu_new_game3, 3, 1f * 422f / 460f, R.drawable.menu_new_game, 0, true));
            //list.add(new MenuItemSprite(ctx, menu_close3, 3, 1f * 350f / 460f, R.drawable.menu_close, 0, true));
            mStepCnt2 = new StepsInfo(ctx);
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_size_cube_222, 4, 1f * 240f / 460f, R.drawable.menu_size222, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_cube_333, 4, 1f * 240f / 460f, R.drawable.menu_size333, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_cube_444, 4, 1f * 240f / 460f, R.drawable.menu_size444, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_cube_555, 4, 1f * 240f / 460f, R.drawable.menu_size555, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_size_pyramid_222, 4, 1f * 240f / 460f, R.drawable.menu_size222, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_pyramid_333, 4, 1f * 240f / 460f, R.drawable.menu_size333, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_pyramid_444, 4, 1f * 240f / 460f, R.drawable.menu_size444, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_pyramid_555, 4, 1f * 240f / 460f, R.drawable.menu_size555, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_size_dcube_233, 6, 1f * 240f / 460f, R.drawable.menu_size233, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_dcube_244, 6, 1f * 240f / 460f, R.drawable.menu_size244, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_dcube_344, 6, 1f * 240f / 460f, R.drawable.menu_size344, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_dcube_255, 6, 1f * 240f / 460f, R.drawable.menu_size255, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_dcube_355, 6, 1f * 240f / 460f, R.drawable.menu_size355, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_dcube_455, 6, 1f * 240f / 460f, R.drawable.menu_size455, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_do_size_empty_cube_333, 2, 1f * 240f / 460f, R.drawable.menu_size333, 0, true));
            list.add(new MenuItemSprite(ctx, menu_do_size_empty_cube_555, 2, 1f * 240f / 460f, R.drawable.menu_size555, 0, true));
        }
        menu.add(list);

        mBackground = new Background(ctx, true);
    }

    public void SetRatio(float parentWidth, float parentHeight,
         float ratioX, float ratioY)
    {
        for (int i = 0 ; i < menu.size(); i++) {
            for (int j = 0; j < menu.get(i).size(); j++) {
                menu.get(i).get(j).SetRatio(parentWidth, parentHeight, ratioX, ratioY);
                menu.get(i).get(j).SetPos(dist);
            }
        }

        if (mTime != null) {
            mTime.SetY(menu.get(menu_do_complete).get(menu_do_complete_time_val).GetPosY());
            mTime.SetRatio(parentWidth, parentHeight, ratioX, ratioY);
        }

        if (mStepCnt != null) {
            mStepCnt.SetY(menu.get(menu_do_complete).get(menu_do_complete_cnt_steps_val).GetPosY());
            mStepCnt.SetRatio(parentWidth, parentHeight, ratioX, ratioY);
        }

        if (mStepCnt2 != null) {
            mStepCnt2.SetY(menu.get(menu_do_complete_no_time).get(menu_do_complete_no_time_cnt_steps_val).GetPosY());
            mStepCnt2.SetRatio(parentWidth, parentHeight, ratioX, ratioY);
        }
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
        if (mActiveMenuIndex < 0) return Menu.menu_none;
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

        if (activeMenuIndex == menu_do_main || activeMenuIndex == menu_do_main_no_exit)
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
