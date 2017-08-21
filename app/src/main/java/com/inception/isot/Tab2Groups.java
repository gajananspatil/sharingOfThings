package com.inception.isot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Gajanan on 06-08-2017.
 */

public class Tab2Groups extends Fragment {

    private static final String TAG = "Tab2Group";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance)
    {
        View view = inflater.inflate(R.layout.tab_groups,container,false);
        return view;
    }
}
