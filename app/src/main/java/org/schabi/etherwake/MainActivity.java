package org.schabi.etherwake;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;

/**
 * Copyright (C) Christian Schabesberger 2015 <chris.schabesberger@mailbox.org>
 * MainActivity.java is part of Etherwake App.
 *
 * Etherwake App is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Etherwake App is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Etherwake App.  If not, see <http://www.gnu.org/licenses/>.
 */

public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getName();

    public static final int NEW_HOST_REQUEST = 13;
    public static final int EDIT_HOST_REQUEST = 14;
    public static final String REQUEST_CODE = "requestCode";

    private HostDBOpenHelper dbHandler;

    public static final String PREFERENCES="ethernetPref";
    public static final String INTERFACE_PREF="interface";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new HostDBOpenHelper(this);

        //install ehterwake command if nececeary
        if(!Etherwake.isInstalled()) {
            Etherwake.installToHomeFolder(this);
        }

        Log.v(TAG, "App created");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_add_host) {
            Intent intent = new Intent();
            intent.setClass(this, HostSettingsActivity.class);
            intent.putExtra(REQUEST_CODE, NEW_HOST_REQUEST);
            startActivityForResult(intent, NEW_HOST_REQUEST);
        } else if(id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        if(resultCode == Activity.RESULT_OK && requestCode == NEW_HOST_REQUEST) {
            dbHandler.insert(data.getStringExtra(HostSettingsActivityFragment.HOST_VALUE),
                    data.getStringExtra(HostSettingsActivityFragment.MAC_VALUE),
                    data.getStringExtra(HostSettingsActivityFragment.PWD_VALUE),
                    data.getBooleanExtra(HostSettingsActivityFragment.BROADCAST_VALUE, false));
            fragment.updateList();
        } else if(resultCode == Activity.RESULT_OK && requestCode == EDIT_HOST_REQUEST) {
            dbHandler.update(
                    data.getLongExtra(HostSettingsActivityFragment.ID, -1),
                    data.getStringExtra(HostSettingsActivityFragment.HOST_VALUE),
                    data.getStringExtra(HostSettingsActivityFragment.MAC_VALUE),
                    data.getStringExtra(HostSettingsActivityFragment.PWD_VALUE),
                    data.getBooleanExtra(HostSettingsActivityFragment.BROADCAST_VALUE, false));
            fragment.updateList();
        }
    }

    public void onEditItem(long itemID) {
        Intent intent = new Intent();
        intent.setClass(this, HostSettingsActivity.class);
        intent.putExtra(REQUEST_CODE, EDIT_HOST_REQUEST);
        intent.putExtra(HostSettingsActivityFragment.ID, itemID);
        intent.putExtra(HostSettingsActivityFragment.HOST_VALUE,
                dbHandler.getStringValueOf(itemID, HostDBOpenHelper.HOST));
        intent.putExtra(HostSettingsActivityFragment.MAC_VALUE,
                dbHandler.getStringValueOf(itemID, HostDBOpenHelper.MAC));
        intent.putExtra(HostSettingsActivityFragment.PWD_VALUE,
                dbHandler.getStringValueOf(itemID, HostDBOpenHelper.PWD));
        intent.putExtra(HostSettingsActivityFragment.BROADCAST_VALUE,
                dbHandler.getBoolValueOf(itemID, HostDBOpenHelper.BROADCAST));
        startActivityForResult(intent, EDIT_HOST_REQUEST);
    }

    public void onDeleteItem(long itemId) {
        MainActivityFragment fragment = (MainActivityFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        dbHandler.delete(itemId);
        fragment.updateList();
    }

    public HostDBOpenHelper getDbHandler() {
        return dbHandler;
    }
}
