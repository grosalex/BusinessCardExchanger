package com.example.businesscardexchanger.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by grosalex on 14/06/14.
 */
public class MyContactsFragment extends Fragment {
    public MyContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_contacts, container, false);
        return rootView;
    }
}
