package com.example.porownywarkapaliw.ManagePart;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.porownywarkapaliw.R;

public class FragmentListOfAllUsers extends Fragment {

private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list_of_all_users,container,false);

        return view;
    }
}
