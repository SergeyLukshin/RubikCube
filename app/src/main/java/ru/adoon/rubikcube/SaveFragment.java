package ru.adoon.rubikcube;

import android.app.Fragment;
import android.os.Bundle;

public class SaveFragment extends Fragment {
    private  GLRenderer mRender;


    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    public GLRenderer getModel(){
        return  mRender;
    }

    public  void setModel(GLRenderer model){
        mRender = model;
    }
}
