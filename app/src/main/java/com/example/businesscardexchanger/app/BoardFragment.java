package com.example.businesscardexchanger.app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;

/**
 * Created by grosalex on 10/06/14.
 */
public class BoardFragment extends Fragment {
    private JSONObject myself = null;
    private View rootView;
    public BoardFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.board_fragment, container, false);
        String inputString;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            FileInputStream myFile = getActivity().openFileInput(getString(R.string.my_info));
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(myFile));
            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
            }
            myself=new JSONObject(stringBuffer.toString());
            //((TextView)this.getView().findViewById(R.id.textViewMyName)).setText(myInfo.get("name").toString());
            setMyInfoInText();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }
    private void setMyInfoInText(){
        TextView name = (TextView)rootView.findViewById(R.id.textViewMyName);
        TextView familyName = (TextView)rootView.findViewById(R.id.textViewMyFamily);
        TextView phone = (TextView)rootView.findViewById(R.id.textViewMyPhone);
        TextView email = (TextView)rootView.findViewById(R.id.textViewMyEmail);

        try {
            name.setText(myself.get("name").toString());
            familyName.setText(myself.get("family").toString());
            phone.setText(myself.get("phone").toString());
            email.setText(myself.get("email").toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
