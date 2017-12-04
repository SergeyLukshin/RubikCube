package ru.adoon.rubikcube;

/**
 * Created by Лукшин on 08.11.2017.
 */

public class Action implements Cloneable {
    private volatile boolean m_bActionIsEnable;

    public int m_ActionAxisRotate;
    public int m_ActionPosRotate;
    public int m_ActionDirectRotate;
    public float mAngle;
    public float mAngleDif;
    public boolean mFromUser;

    public Action(boolean fromUser) {
        m_bActionIsEnable = false;
        m_ActionAxisRotate = Structures.AXE_Z;
        m_ActionPosRotate = 0;
        m_ActionDirectRotate = Structures.DIRECT_NONE;
        mAngle = 0;
        mAngleDif = 10;
        mFromUser = fromUser;
    }

    public void setFromUser(boolean fromUser) {
        mFromUser = fromUser;
    }

    @Override
    protected Action clone() {
        try {
            return (Action)super.clone();
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }

    public synchronized boolean ActionIsEnable() {
        return m_bActionIsEnable;
    }

    public synchronized boolean CheckActionStop(int limit) {
        if (m_bActionIsEnable && (mAngle % limit) == 0) {
            m_bActionIsEnable = false;
            mAngle = 0;
            return true;
        }
        return false;
    }

    public synchronized void ActionExec() {
        if (m_bActionIsEnable) {
            if (m_ActionDirectRotate == Structures.DIRECT_LEFT) mAngle += mAngleDif;
            if (m_ActionDirectRotate == Structures.DIRECT_RIGHT) mAngle -= mAngleDif;
        }
    }

    public synchronized void ActionStart() {
        m_bActionIsEnable = true;
        mAngle = 0;
        ActionExec();
    }
}
