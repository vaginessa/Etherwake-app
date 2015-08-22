package org.schabi.etherwake;


import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.view.View;
import android.widget.ListView;

/**
 * Copyright (C) Christian Schabesberger 2015 <chris.schabesberger@mailbox.org>
 * MainActivityFragment.java is part of Etherwake App.
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

public class MainActivityFragment extends ListFragment {

    private CursorAdapter ca;
    private HostDBOpenHelper dbHandler;

    public MainActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ca = new HostsAdapter(getActivity());
        setListAdapter(ca);
        dbHandler = ((MainActivity) getActivity()).getDbHandler();
        updateList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dbHandler.close();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ItemDialog dialog = new ItemDialog();
        dialog.setItemData(
                id,dbHandler.getStringValueOf(id, HostDBOpenHelper.MAC),
                dbHandler.getStringValueOf(id, HostDBOpenHelper.PWD),
                dbHandler.getBoolValueOf(id, HostDBOpenHelper.BROADCAST));
        dialog.show(getActivity().getSupportFragmentManager(), "itemDialog");
    }

    public void updateList() {
        ca.changeCursor(dbHandler.query());
    }
}
