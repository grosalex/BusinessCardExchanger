package com.example.businesscardexchanger.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by grosalex on 14/06/14.
 */
public class ModifyDialogFragment extends DialogFragment {
    private JSONObject myself = null;

    View view;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_main, null);
        builder.setTitle("Modify my informations");
        builder.setView(view);

        ((Button)view.findViewById(R.id.buttonSave)).setVisibility(View.INVISIBLE);
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

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = new String(((EditText)view.findViewById(R.id.editTextName)).getText().toString());
                String family = new String(((EditText)view.findViewById(R.id.editTextFamily)).getText().toString());
                String email = new String(((EditText)view.findViewById(R.id.editTextEmail)).getText().toString());
                String phone = new String(((EditText)view.findViewById(R.id.editTextPhone)).getText().toString());

                try {
                    myself.put("name", name);
                    myself.put("family", family);
                    myself.put("email", email);
                    myself.put("phone", phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                File file = new File(getActivity().getFilesDir(),getString(R.string.my_info));
                FileOutputStream outputStream;

                try {
                    outputStream = getActivity().openFileOutput(getString(R.string.my_info), Context.MODE_PRIVATE);
                    outputStream.write(myself.toString().getBytes());
                    outputStream.close();

                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        });
        return builder.create();

    }
    public void setMyInfoInText(){
        try {
            ((EditText)view.findViewById(R.id.editTextName)).setHint(myself.getString("name"));
            ((EditText)view.findViewById(R.id.editTextFamily)).setHint(myself.getString("family"));
            ((EditText)view.findViewById(R.id.editTextEmail)).setHint(myself.getString("email"));
            ((EditText)view.findViewById(R.id.editTextPhone)).setHint(myself.getString("phone"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
