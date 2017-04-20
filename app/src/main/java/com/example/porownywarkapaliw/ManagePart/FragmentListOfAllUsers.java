package com.example.porownywarkapaliw.ManagePart;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.porownywarkapaliw.ListViewPopulateHelper;
import com.example.porownywarkapaliw.R;
import com.example.porownywarkapaliw.SQLDataBase.DBAdapter;
import com.example.porownywarkapaliw.ShowLogs;

public class FragmentListOfAllUsers extends Fragment implements AdapterView.OnItemClickListener {
    private View view;
    private ListView lvALOAU_listOfUsers;
    private DBAdapter dbAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list_of_all_users,container,false);

        lvALOAU_listOfUsers = (ListView) view.findViewById(R.id.lvALOAU_listOfUsers);
        lvALOAU_listOfUsers.setOnItemClickListener(this);

        dbAdapter = new DBAdapter(view.getContext());
        dbAdapter.openDB();

        populateUsersListView();
        return view;
    }

    private void populateUsersListView(){
        ShowLogs.i("FragmentListOfAllUsers populateUsersListView");
        ListViewPopulateHelper listViewPopulateHelper = new ListViewPopulateHelper(view.getContext(),dbAdapter.GetAllRows(),
                R.layout.custom_list_users_table_activity);

        lvALOAU_listOfUsers.setAdapter(listViewPopulateHelper);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(view.getContext(),"position :" + position,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        dbAdapter.closeDB();
    }
}
