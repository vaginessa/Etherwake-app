package org.schabi.etherwake;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Christian Schabesberger on 26.07.15.
 *
 * Copyright (C) Christian Schabesberger 2015 <chris.schabesberger@mailbox.org>
 * HostAdapter.java is part of Etherwake App.
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

public class HostsAdapter extends CursorAdapter {

    private LayoutInflater inflater;

    public HostsAdapter(Context context) {
        super(context, null, 0);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int ciHost = cursor.getColumnIndex(HostDBOpenHelper.HOST);
        int ciMac = cursor.getColumnIndex(HostDBOpenHelper.MAC);

        TextView hostLbl = (TextView) view.findViewById(R.id.hostLbl);
        TextView macLbl = (TextView) view.findViewById(R.id.macLbl);

        hostLbl.setText(cursor.getString(ciHost));
        macLbl.setText(cursor.getString(ciMac));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.host_item, null);
    }
}
