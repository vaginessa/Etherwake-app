package org.schabi.etherwake;

import android.app.Dialog;
import android.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Christian Schabesberger on 29.07.15.
 *
 * Copyright (C) Christian Schabesberger 2015 <chris.schabesberger@mailbox.org>
 * DeleteItemDialog.java is part of Etherwake App.
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

public class DeleteItemDialog extends DialogFragment {

    private long itemId = -1;

    public void setItemId(long id) {
        itemId = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceBundle) {
        super.onCreateDialog(savedInstanceBundle);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.deleteDialogMessage)
                .setPositiveButton(R.string.deleteYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity) getActivity()).onDeleteItem(itemId);
                    }
                })
                .setNegativeButton(R.string.deleteNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
