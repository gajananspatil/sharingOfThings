package com.inception.isot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class fragment_add_item extends Fragment {

    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance)
    {
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        Log.d("add item","add fragment");

        return view;
    }
}