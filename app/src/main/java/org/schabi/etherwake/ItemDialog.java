package org.schabi.etherwake;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

/**
 * Created by Christian Schabesberger on 27.07.15.
 *
 * Copyright (C) Christian Schabesberger 2015 <chris.schabesberger@mailbox.org>
 * ItemDialog.java is part of Etherwake App.
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

public class ItemDialog extends DialogFragment {

    private static final String TAG = DialogFragment.class.getSimpleName();

    private long itemId = -1;
    private String macAddress = "";
    private String password = "";
    private boolean broadcast;

    private SharedPreferences prefs;

    public void setItemData(long id, String mac, String pwd, boolean br){
        itemId = id;
        macAddress = mac;
        password = pwd;
        broadcast = br;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        prefs = getActivity().getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.itemDialogTitle)
                .setItems(R.array.itemDialogItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case 0:
                                try {
                                    Etherwake.getEtherwake().executeEtherwake(getActivity(),
                                            prefs.getString(MainActivity.INTERFACE_PREF, "wlan0"),
                                            macAddress, password, broadcast);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case 1:
                                ((MainActivity) getActivity()).onEditItem(itemId);
                                break;
                            case 2:
                                DeleteItemDialog deleteDialog= new DeleteItemDialog();
                                deleteDialog.setItemId(itemId);
                                deleteDialog.show(getActivity().getSupportFragmentManager(), "deleteDialog");
                            case 3:
                                dialog.cancel();
                                break;
                            default:
                                Log.e(TAG, "Motherfucker");
                        }
                    }
                });
        return builder.create();
    }
}
