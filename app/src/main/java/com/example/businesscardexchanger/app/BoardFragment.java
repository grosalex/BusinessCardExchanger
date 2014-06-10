package com.example.businesscardexchanger.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;
import java.io.InputStreamReader;
import java.io.InputStream;

/**
 * Created by grosalex on 10/06/14.
 */
public class BoardFragment extends Fragment {
    private JSONObject myself = null;
    public BoardFragment(){

        //InputStream inputStream = openFileInput(getString(R.string.my_info));

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.board_fragment, container, false);
        return rootView;
    }
}
