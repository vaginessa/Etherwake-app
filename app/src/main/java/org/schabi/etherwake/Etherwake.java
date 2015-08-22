package org.schabi.etherwake;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by Christian Schabesberger on 31.07.15.
 *
 * Copyright (C) Christian Schabesberger 2015 <chris.schabesberger@mailbox.org>
 * Etherwake.java is part of Etherwake App.
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

public class Etherwake {

    private static final String TAG = Etherwake.class.getName();

    private static final Etherwake etherwake = new Etherwake();

    public static final String COMMAND_DIR_ARM = "command_arm";
    public static final String COMMAND_DIR_I686 = "command_i686";
    public static final String COMMAND_DIR_MIPS = "command_mips";

    public static final String CMD = "etherwake";

    public static Etherwake getEtherwake() {
        return etherwake;
    }

    private static void installCmd(Context context, String dir, int resourceId) {
        File cmdDir = context.getDir(dir, Context.MODE_PRIVATE);
        InputStream cmd_in = context.getResources().openRawResource(resourceId);
        File cmd_file = new File(cmdDir, CMD);
        try {
            FileOutputStream cmd_out = new FileOutputStream(cmd_file);
            IOUtils.copy(cmd_in, cmd_out);
            cmd_in.close();
            cmd_out.close();
            Runtime.getRuntime().exec("chmod -R 555 " + cmd_file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isInstalled() {
        return (new File(COMMAND_DIR_ARM)).exists();
    }

    public static void installToHomeFolder(Context context) {
        installCmd(context, COMMAND_DIR_ARM, R.raw.etherwake_arm);
        installCmd(context, COMMAND_DIR_I686, R.raw.etherwake_i686);
        installCmd(context, COMMAND_DIR_MIPS, R.raw.etherwake_mips);
    }

    public void executeEtherwake(Context context, String iface, String mac, String pwd, boolean broadcast) {
        String arch = System.getProperty("os.arch");
        String cmdDirS;
        Log.v(TAG,"System arch: " + arch );

        switch(arch) {
            case "armv7l":
                cmdDirS = COMMAND_DIR_ARM;
                break;
            case "arch64":
                cmdDirS = COMMAND_DIR_ARM;
                break;
            case "i686":
                cmdDirS = COMMAND_DIR_I686;
                break;
            case "x86_64":
                cmdDirS = COMMAND_DIR_I686;
                break;
            case "mips":
                cmdDirS = COMMAND_DIR_MIPS;
                break;
            case "mips64":
                cmdDirS = COMMAND_DIR_MIPS;
                break;
            default:
                cmdDirS = COMMAND_DIR_ARM;
                Log.e(TAG, "Architecture not known");
        }
        File cmdDir = context.getDir(cmdDirS, Context.MODE_PRIVATE);
        String command = cmdDir.getAbsolutePath() + "/" + CMD + " -i " + iface
                + (broadcast ? " -b " : "")
                + (pwd.isEmpty() ? "" : " -p " + pwd + " ")
                + " " + mac;
        Log.v(TAG, "EXECUTE as root: " + command);
        new Thread(new EthThread(context, new Handler(), command)).start();
    }

    private class EthThread implements Runnable {
        private Context context;
        private Handler handler;
        private String arguments;

        public EthThread(Context c, Handler h, String arg) {
            context = c;
            handler = h;
            arguments = arg;
        }

        @Override
        public void run() {
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"su", "-c", arguments});
                BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                String outLine;
                String errLine;

                //the folloing is ugly code but what ever :P
                while((errLine = err.readLine()) != null) {
                    Log.e(TAG, errLine);
                    ToastThreat msg = new ToastThreat(context, errLine);
                    handler.post(msg);
                }

                while((outLine = out.readLine()) != null) {
                    Log.v(TAG, outLine);
                    ToastThreat msg = new ToastThreat(context, outLine);
                    handler.post(msg);
                }

            } catch (Exception e) {
                e.getStackTrace();
            }
        }

    }

    private class ToastThreat implements Runnable {
        private Context context;
        private String message;

        public ToastThreat(Context c, String msg) {
            context = c;
            message = msg;
        }

        @Override
        public void run() {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void installToBin(Context context) {
        if(!isInstalled()) {
            installToHomeFolder(context);
        }

        Log.e(TAG, "INSTALL FUNKTION NOT WRITTEN YET!!!");
    }
}
