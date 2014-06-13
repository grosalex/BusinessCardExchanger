package com.example.businesscardexchanger.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(),"register")
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveProfile(View view) {
        String name = new String(((EditText)findViewById(R.id.editTextName)).getText().toString());
        String family = new String(((EditText)findViewById(R.id.editTextFamily)).getText().toString());
        String email = new String(((EditText)findViewById(R.id.editTextEmail)).getText().toString());
        String phone = new String(((EditText)findViewById(R.id.editTextPhone)).getText().toString());

        JSONObject save = new JSONObject();

        try {
            save.put("name",name);
            save.put("family",family);
            save.put("email",email);
            save.put("phone",phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        File file = new File(getFilesDir(),getString(R.string.my_info));
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(getString(R.string.my_info), Context.MODE_PRIVATE);
            outputStream.write(save.toString().getBytes());
            outputStream.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(),"Information Successfuly Recorded",Toast.LENGTH_SHORT).show();


        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        BoardFragment next = new BoardFragment();
        ft.replace(R.id.container,next,"register");
        ft.addToBackStack(null);
        ft.commit();

    }


}
