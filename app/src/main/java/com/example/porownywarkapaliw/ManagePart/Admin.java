package com.example.porownywarkapaliw.ManagePart;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.porownywarkapaliw.FragmentHelper;
import com.example.porownywarkapaliw.R;

public class Admin extends Fragment implements View.OnClickListener {

    private View view;
    private FragmentHelper fragmentHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_admin,container,false);
        fragmentHelper = new FragmentHelper(getActivity());
        return view;
    }





    @Override
    public void onClick(View v) {
        switch (view.getId()){
            case R.id.bAA_ShowListOfAllUsers:
                fragmentHelper.loadFragment(new FragmentListOfAllUsers());
            break;
        }

    }
}
