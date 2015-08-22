package org.schabi.etherwake;

import android.app.Activity;
import android.content.Intent;
import android.drm.DrmStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Copyright (C) Christian Schabesberger 2015 <chris.schabesberger@mailbox.org>
 * HostSettingsActivityFragment.java is part of Etherwake App.
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

public class HostSettingsActivityFragment extends Fragment {

    private static final String TAG = HostSettingsActivityFragment.class.getSimpleName();

    public static final String ID = "id";
    public static final String HOST_VALUE = "host_value";
    public static final String MAC_VALUE = "mac_value";
    public static final String PWD_VALUE = "pwd_value";
    public static final String BROADCAST_VALUE = "broadcast_value";

    EditText hostEdit, macEdit, pwdEdit;
    CheckBox broadcastCheckBox;

    private long currentId;

    public long getCurrentId() {
        return currentId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_host_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        hostEdit = (EditText) getView().findViewById(R.id.settingsHostEdit);
        macEdit = (EditText) getView().findViewById(R.id.settingsMacEdit);
        pwdEdit = (EditText) getView().findViewById(R.id.settingsPwdEdit);
        broadcastCheckBox = (CheckBox) getView().findViewById(R.id.broadcastCheckBox);

        Intent intent = getActivity().getIntent();
        if(intent.getIntExtra(MainActivity.REQUEST_CODE, -1) == MainActivity.EDIT_HOST_REQUEST) {
            currentId = intent.getLongExtra(ID, -1);
            hostEdit.setText(intent.getStringExtra(HOST_VALUE));
            macEdit.setText(intent.getStringExtra(MAC_VALUE));
            pwdEdit.setText(intent.getStringExtra(PWD_VALUE));
            broadcastCheckBox.setChecked(intent.getBooleanExtra(BROADCAST_VALUE, false));
        }

        Button savedButton = (Button) getView().findViewById(R.id.saveButton);
        savedButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!hostEdit.getText().toString().isEmpty() &&
                        !macEdit.getText().toString().isEmpty()) {
                    Intent input = getActivity().getIntent();
                    Intent output = new Intent();
                    output.putExtra(MainActivity.REQUEST_CODE,
                            input.getIntExtra(MainActivity.REQUEST_CODE, -1));
                    output.putExtra(ID, input.getLongExtra(ID, -1));            //only needed for EDIT_HOST_REQUEST
                                                                                //will be discard for NEW_HOST_REQUEST
                    output.putExtra(HOST_VALUE, hostEdit.getText().toString());
                    output.putExtra(MAC_VALUE, macEdit.getText().toString());
                    output.putExtra(PWD_VALUE, pwdEdit.getText().toString());
                    output.putExtra(BROADCAST_VALUE, broadcastCheckBox.isChecked());
                    getActivity().setResult(Activity.RESULT_OK, output);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.onSaveWrongToast,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
