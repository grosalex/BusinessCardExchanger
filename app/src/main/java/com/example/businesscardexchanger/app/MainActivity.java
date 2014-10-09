package com.example.businesscardexchanger.app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle="BusinessCardExchanger";
    private NfcAdapter mNfcAdapter;
    private Uri[] mFileUris = new Uri[10];
    private FileUriCallback mFileUriCallback;
    private Uri fileUri;
    private Intent mIntent;
    private File mParentPath;


    private class FileUriCallback implements NfcAdapter.CreateBeamUrisCallback {
        public FileUriCallback() {
            String transferFile = getString(R.string.my_info);
            File extDir = getExternalFilesDir(null);
            File requestFile = new File(extDir, transferFile);
            requestFile.setReadable(true, false);
            // Get a URI for the File and add it to the list of URIs
            fileUri = Uri.fromFile(requestFile);
            if (fileUri != null) {
                mFileUris[0] = fileUri;
            } else {
                Log.e("My Activity", "No File URI available for file.");
            }
        }
        /**
         * Create content URIs as needed to share with another device
         */
        @Override
        public Uri[] createBeamUris(NfcEvent event) {
            return mFileUris;
        }
    }
    public String handleFileUri(Uri beamUri) {
        // Get the path part of the URI
        String fileName = beamUri.getPath();
        // Create a File object for this filename
        File copiedFile = new File(fileName);
        // Get a string containing the file's parent directory
        return copiedFile.getParent();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,new String[]{
                "Board",
                "My contacts",
                "Settings",
        }));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_drawer,R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Navigation");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        /*
         * Instantiate a new FileUriCallback to handle requests for
         * URIs
         */
        mFileUriCallback = new FileUriCallback();
        // Set the dynamic callback for URI requests.
        mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback,this);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.container, new PlaceholderFragment(),"register")
                    .commit();
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = null;
        Bundle args = new Bundle();

        // Insert the fragment by replacing any existing fragment

        switch (position){
            case 0:
                fragment=new BoardFragment();
                break;
            case 1:
                fragment=new MyContactsFragment();
                break;
            case 2:
                fragment=new SettingsFragment();
                break;

            default:
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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

    private void handleViewIntent() {
        // Get the Intent action
        mIntent = getIntent();
        String action = mIntent.getAction();
        /*
         * For ACTION_VIEW, the Activity is being asked to display data.
         * Get the URI.
         */
        if (TextUtils.equals(action, Intent.ACTION_VIEW)) {
            // Get the URI from the Intent
            Uri beamUri = mIntent.getData();
            /*
             * Test for the type of URI, by getting its scheme value
             */
            if (TextUtils.equals(beamUri.getScheme(), "file")) {
               // mParentPath = handleFileUri(beamUri);
            } else if (TextUtils.equals(
                    beamUri.getScheme(), "content")) {
                //mParentPath = handleContentUri(beamUri);
            }
        }
    }
    public void Share(View view) {

    }

    public void Modify(View view) {
        ModifyDialogFragment modifyDialogFragment = new ModifyDialogFragment();
        modifyDialogFragment.show(getFragmentManager(),"modify");
    }
}


