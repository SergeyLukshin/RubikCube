package ru.adoon.rubikcube;

import java.util.ArrayList;

/**
 * Created by Лукшин on 09.11.2017.
 */

public class ActionList {
    volatile ArrayList<Action> list = new ArrayList<Action>();
    volatile int mPause = -1;

    public synchronized boolean ActionIsEnable() {

        if (mPause > 0) return true;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(0).ActionIsEnable())
                return true;
        }
        return false;
    }

    public synchronized void ActionExec() {
        if (list.size() > 0) {
            list.get(0).ActionExec();
        }
    }

    public synchronized Action GetCurAction() {
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public synchronized void RemoveCurAction() {
        if (list.size() > 0) {
            list.remove(0);
            mPause = -1;
        }
    }

    public synchronized void ActionStart() {
        if (list.size() > 0) {
            list.get(0).ActionStart();
        }
    }

    /*public float[] CheckItemInAction(int posX, int posY, int posZ) {
        if (list.size() > 0) {
            return list.get(0).CheckItemInAction(posX, posY, posZ);
        }
        return null;
    }

    public float GetAngle() {
        if (list.size() > 0) {
            return list.get(0).mAngle;
        }
        return 0;
    }*/

    public synchronized Action CheckActionStop(int limit) {
        Action a = null;

        if (mPause > 0) mPause --;
        if (mPause == 0) ActionStart();

        if (list.size() > 0) {
            if (list.get(0).CheckActionStop(limit)) {
                a = list.get(0).clone();
                list.remove(0);

                if (list.size() > 0) mPause = 5;
                else mPause = -1;
                //ActionStart();
            }
        }
        return a;
    }

    public synchronized void Add(Action a) {
        list.add(a);
        ActionStart();
    }

    public synchronized void AddNoStart(Action a) {
        list.add(a);
    }
}
