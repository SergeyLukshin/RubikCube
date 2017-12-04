package ru.adoon.rubikcube;

import android.content.Context;
import android.graphics.Color;

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


    public static final int menu_none = -1;

    public static final int menu_new_game = 0;
    public static final int menu_timer = 1;
    public static final int menu_figure = 2;
    public static final int menu_close = 3;

    public static final int menu_shuffle = 0;
    public static final int menu_exit = 0;
    public static final int menu_yes = 1;
    public static final int menu_no = 2;

    public static final int menu_complete = 0;
    public static final int menu_new_game2 = 1;
    public static final int menu_close2 = 2;

    public static final int menu_cube = 0;
    public static final int menu_pyramid = 1;

    boolean m_bMenuIsEnable = false; // необходимо сохранять
    boolean m_bMenuIsLoad = false;
    boolean m_bMenuIsUnload = false;
    ArrayList<ArrayList<MenuItemSprite>> menu = new ArrayList<ArrayList<MenuItemSprite>>();
    Background mBackground;
    float dist = 0.5f;
    int mActiveMenuIndex = 0;

    public void SetContext(Context context) {
        mBackground.SetContext(context);

        for (int i = 0 ; i < menu.size(); i++) {
            for (int j = 0; j < menu.get(i).size(); j++)
                menu.get(i).get(j).SetContext(context);
        }
    }

    Menu(Context ctx)
    {
        //list.add(new MenuItemSprite(ctx, 0, 4, 1f * 422f / 460f, R.drawable.menu_settings, 0));
        ArrayList<MenuItemSprite> list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_new_game, 4, 1f * 422f / 460f, R.drawable.menu_new_game, 0, true));
            list.add(new MenuItemSprite(ctx, menu_timer, 4, 1f * 400f / 460f, R.drawable.menu_timer_off, R.drawable.menu_timer_on, true));
            list.add(new MenuItemSprite(ctx, menu_figure, 4, 1f * 420f / 460f, R.drawable.menu_figure, 0, true));
            list.add(new MenuItemSprite(ctx, menu_close, 4, 1f * 350f / 460f, R.drawable.menu_close, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_shuffle, 3, 1f * 470f / 460f, R.drawable.menu_shuffle, 0, false));
            list.add(new MenuItemSprite(ctx, menu_yes, 3, 1f * 210f / 460f, R.drawable.menu_yes, 0, true));
            list.add(new MenuItemSprite(ctx, menu_no, 3, 1f * 210f / 460f, R.drawable.menu_no, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_exit, 3, 1f * 470f / 460f, R.drawable.menu_exit, 0, false));
            list.add(new MenuItemSprite(ctx, menu_yes, 3, 1f * 210f / 460f, R.drawable.menu_yes, 0, true));
            list.add(new MenuItemSprite(ctx, menu_no, 3, 1f * 210f / 460f, R.drawable.menu_no, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_complete, 3, 1f * 701f / 460f, R.drawable.menu_complete, 0, false));
            list.add(new MenuItemSprite(ctx, menu_new_game2, 3, 1f * 422f / 460f, R.drawable.menu_new_game, 0, true));
            list.add(new MenuItemSprite(ctx, menu_close2, 3, 1f * 350f / 460f, R.drawable.menu_close, 0, true));
        }
        menu.add(list);

        list = new ArrayList<MenuItemSprite>();
        {
            list.add(new MenuItemSprite(ctx, menu_cube, 2, 1f * 210f / 460f, R.drawable.menu_cube, 0, true));
            list.add(new MenuItemSprite(ctx, menu_pyramid, 2, 1f * 370f / 460f, R.drawable.menu_pyramid, 0, true));
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
    }

    public void SetTexture(int index, int pos) {
        if (index >= 0 && index < menu.get(mActiveMenuIndex).size())
            menu.get(mActiveMenuIndex).get(index).SetTexture(pos);
    }

    public int GetActiveMenuIndex() {
        return mActiveMenuIndex;
    }

    public int GetMenuPressed(float x, float y) {

        for (int i = 0; i < menu.get(mActiveMenuIndex).size(); i++) {
            if (menu.get(mActiveMenuIndex).get(i).IsEnabled() && menu.get(mActiveMenuIndex).get(i).IsPressed(x, y))
                return i;
        }

        return Menu.menu_none;
    }

    public boolean MenuIsEnable() {
        return m_bMenuIsEnable || m_bMenuIsLoad || m_bMenuIsUnload;
    }

    public void MenuShow(int activeMenuIndex) {
        MenuEndUnload();
        mActiveMenuIndex = activeMenuIndex;
        m_bMenuIsLoad = true;
        dist = 0.5f;
    }

    public void MenuEndLoad() {
        m_bMenuIsLoad = false;
        m_bMenuIsEnable = true;
    }

    public void MenuEndUnload() {
        m_bMenuIsUnload = false;
        m_bMenuIsEnable = false;
        //mActiveMenuIndex = -1;
    }

    public void MenuClose() {
        m_bMenuIsUnload = true;
    }

    public void Draw() {

        if (!MenuIsEnable()) return;

        mBackground.Draw();

        if (m_bMenuIsLoad) {
            dist -= 0.1f;
            if (dist <= 0) {
                dist = 0;
                MenuEndLoad();
            }

            for (int i = 0; i < menu.get(mActiveMenuIndex).size(); i++) {
                menu.get(mActiveMenuIndex).get(i).SetPos(dist);
            }
        }

        if (m_bMenuIsUnload) {
            dist += 0.1f;
            if (dist >= 0.5f) {
                dist = 0.5f;
                MenuEndUnload();
            }

            for (int i = 0; i < menu.get(mActiveMenuIndex).size(); i++) {
                menu.get(mActiveMenuIndex).get(i).SetPos(dist);
            }
        }

        for (int i = 0; i < menu.get(mActiveMenuIndex).size(); i++) {
            menu.get(mActiveMenuIndex).get(i).Draw();
        }
    }
}
