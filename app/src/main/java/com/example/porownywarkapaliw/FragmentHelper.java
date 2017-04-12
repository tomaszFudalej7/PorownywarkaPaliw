package com.example.porownywarkapaliw;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

/**
 * Created by Администратор on 12.04.2017.
 */

public class FragmentHelper {
    private Context context;
    private Activity activity;
   public FragmentHelper(Context context,Activity activity){
       this.context = context;
       this.activity = activity;
   }
    public FragmentHelper(Activity activity){
        this.activity = activity;
    }


    public void loadFragment(Fragment fragment) {
        ShowLogs.i("FragmentHelper fragment :" + fragment.toString());
        android.app.FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
