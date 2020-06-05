package com.phoenixhosman.phoenixdbmanagers;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import static android.view.View.inflate;

public class ActivityMain extends FragmentActivity {
    private String strCoName;
    private String strApiUrl;
    private String strLockPass;
    private String strName;
    private Integer intGrade;
    private String strGradename;
    private Integer intDepartment;
    private String strDepartmentname;


    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Cursor cursor = getContentResolver().query(Uri.parse("content://com.phoenixhosman.installer.ProviderSettings/settings"), null, null, null, null, null);
        assert cursor != null;
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                strCoName = cursor.getString(cursor.getColumnIndex("coname"));
                strApiUrl = cursor.getString(cursor.getColumnIndex("apiurl"));
                strLockPass = cursor.getString(cursor.getColumnIndex("lockpass"));
                cursor.moveToNext();
            }
        } else {
            Error("\nRequired setting missing.\nPlease (re)run Phoenix Install App", false);
        }
        cursor = getContentResolver().query(Uri.parse("content://com.phoenixhosman.launcher.ProviderUser/acl"), null, null, null, null, null);
        assert cursor != null;
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                strName = cursor.getString(cursor.getColumnIndex("name"));
                intGrade = Integer.valueOf(cursor.getString(cursor.getColumnIndex("grade")));
                strGradename = cursor.getString(cursor.getColumnIndex("gradename"));
                intDepartment = Integer.valueOf(cursor.getString(cursor.getColumnIndex("department")));
                strDepartmentname =cursor.getString(cursor.getColumnIndex("departmentname"));
                cursor.moveToNext();
            }
        } else {
            Error("\nNo Logged User.\nPlease launch app from the Phoenix Launcher", true);
        }
    }

    /**
     * The error display method
     *
     * This method displays a dialog box with an error message and a close button.
     *
     * @param strError the error message to display
     */
    @SuppressWarnings ("SameParameterValue")
    private void Error(String strError, Boolean exit) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View view = inflate(this, R.layout.dialog_error, null);
        Button btnExit = view.findViewById(R.id.buttonExitButton);
        Button btnError = view.findViewById(R.id.btnErrorMessage);
        btnError.setText(getString(R.string.error, strError ));
        mBuilder.setView(view);
        AlertDialog dialog = mBuilder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        btnExit.setOnClickListener(v -> {
            dialog.dismiss();
            if (exit) {
                finishAffinity();
            }
        });
    }
}