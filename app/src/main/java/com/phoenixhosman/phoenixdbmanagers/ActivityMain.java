package com.phoenixhosman.phoenixdbmanagers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static android.view.View.inflate;
import static androidx.recyclerview.widget.RecyclerView.*;
import static java.util.Objects.requireNonNull;

/**
 * Code for the main activity
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public class ActivityMain extends FragmentActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView sRecyclerView;
    private Adapter mAdapter;
    private Adapter sAdapter;
    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private LinearLayoutManager sLayoutManager = new LinearLayoutManager(this);
    private String strCoName;
    private String strApiUrl;
    private String strLockPass;
    private String strName;
    private Integer intGrade;
    private String strGradename;
    private Integer intDepartment;
    private String strDepartmentname;
    private String strMenuName;
    private String strSubMenuName;
    private ArrayList<ObjectMenu> MenuList = new ArrayList<>();
    private ArrayList<ObjectSubMenu> objSubMenu = new ArrayList<>();
    private ArrayList<ObjectMenu> SubmenuList = new ArrayList<>();

    /**
     * Override of the parent onCreate method
     *
     * @param savedInstanceState The current instance state
     */
    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
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
        if (!strGradename.contains("Administrator")) {
            Error("\nAdministrator Grade Required.\nAdministrator Rights required to use this app", true);
        }
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerViewMainMenu);
        sRecyclerView = findViewById(R.id.recyclerViewSubMenu);
        mRecyclerView.setHasFixedSize(true);
        sRecyclerView.setHasFixedSize(true);
        mAdapter = new MenuAdapter(MenuList);
        sAdapter = new SubmenuAdapter(SubmenuList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        sRecyclerView.setLayoutManager(sLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        sRecyclerView.setAdapter(sAdapter);
        if (findViewById(R.id.topFrame) != null) {
            if (savedInstanceState != null) {
                return;
            }
            FragmentBlank blankFragment = new FragmentBlank();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.topFrame, blankFragment).commit();
        }
        if (findViewById(R.id.bottomFrame) != null) {
            if (savedInstanceState != null) {
                return;
            }
            FragmentBlank blankFragment = new FragmentBlank();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.bottomFrame, blankFragment).commit();
        }
        BuildMenu();
        mAdapter.notifyDataSetChanged();
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
            if (exit) finishAndRemoveTask();
        });
    }

    /**
     * Method to define the main and sub menus
     */
    public void BuildMenu() {
        MenuList.add(new ObjectMenu("Users"));
        MenuList.add(new ObjectMenu("Grades"));
        MenuList.add(new ObjectMenu("Departments"));
        objSubMenu.add(new ObjectSubMenu("Users", "List"));
        objSubMenu.add(new ObjectSubMenu("Users", "Add"));
        objSubMenu.add(new ObjectSubMenu("Users","Update"));
        objSubMenu.add(new ObjectSubMenu("Users", "Remove"));
        objSubMenu.add(new ObjectSubMenu("Grades", "List"));
        objSubMenu.add(new ObjectSubMenu("Grades", "Add"));
        objSubMenu.add(new ObjectSubMenu("Grades","Update"));
        objSubMenu.add(new ObjectSubMenu("Grades", "Remove"));
        objSubMenu.add(new ObjectSubMenu("Departments", "List"));
        objSubMenu.add(new ObjectSubMenu("Departments", "Add"));
        objSubMenu.add(new ObjectSubMenu("Departments","Update"));
        objSubMenu.add(new ObjectSubMenu("Departments", "Remove"));
    }

    /**
     * Method to return a sub menu
     * @param parent The parent of the submenu
     */
    public void GetSubMenu(String parent) {
        SubmenuList.clear();
        for (ObjectSubMenu subMenu : objSubMenu) {
            if (subMenu.getParent().equals(parent)) {
                SubmenuList.add(new ObjectMenu(subMenu.getName()));
            }
        }
    }

    /**
     * The Main Menu Recycler View Adapter
     */

    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
        final private List<ObjectMenu> mMenu;
        class MenuViewHolder extends RecyclerView.ViewHolder {
            final Button btnMenuItem;
            MenuViewHolder(View menuView) {
                super(menuView);
                btnMenuItem = menuView.findViewById(R.id.btnMainMenuItem);
            }
        }
        MenuAdapter(ArrayList<ObjectMenu> menu) {
            mMenu = menu;
        }
        @NonNull
        @Override
        public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mainmenu, parent,false);
            return new MenuViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull final MenuViewHolder holder, int position) {
            final ObjectMenu currentMenu = mMenu.get(position);
            holder.btnMenuItem.setText(currentMenu.getName());
            holder.btnMenuItem.setOnClickListener(v -> {
                if (holder.btnMenuItem.isSelected()) {
                    strSubMenuName = null;
                    SubmenuList.clear();
                    sAdapter.notifyDataSetChanged();
                } else {
                    resetMainMenu(mRecyclerView);
                    resetSubMenu(sRecyclerView);
                    FragmentBlank blankFragment = new FragmentBlank();
                    getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, blankFragment).commit();
                    holder.btnMenuItem.setSelected(true);
                    strMenuName = currentMenu.getName();
                    GetSubMenu(currentMenu.getName());
                    sAdapter.notifyDataSetChanged();
                }
            });
        }
        @Override
        public int getItemCount() {
            return mMenu.size();
        }
    }

    /**
     * The Submenu Recycler View Adapter
     */

    public class SubmenuAdapter extends RecyclerView.Adapter<SubmenuAdapter.SubmenuViewHolder> {
        final private List<ObjectMenu> mSubmenuList;
        class SubmenuViewHolder extends RecyclerView.ViewHolder {
            final Button btnSubmenuItem;
            SubmenuViewHolder(View submenuView) {
                super(submenuView);
                btnSubmenuItem = submenuView.findViewById(R.id.btnSubMenuItem);
            }
        }
        SubmenuAdapter(ArrayList<ObjectMenu> submenuList) {
            mSubmenuList = submenuList;
        }
        @NonNull
        @Override
        public SubmenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_submenu, parent, false);
            return new SubmenuViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull final SubmenuViewHolder holder, int position) {
            final ObjectMenu currentMenu = mSubmenuList.get(position);
            holder.btnSubmenuItem.setText(currentMenu.getName());
            holder.btnSubmenuItem.setOnClickListener(view -> {
                if (holder.btnSubmenuItem.isSelected()) {
                    resetSubMenu(sRecyclerView);
                    strSubMenuName = null;
                    FragmentBlank blankFragment = new FragmentBlank();
                    getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
                } else {
                    resetSubMenu(sRecyclerView);
                    holder.btnSubmenuItem.setSelected(true);
                    strSubMenuName = currentMenu.getName();
                    CallFragment();
                }
            });
        }
        @Override
        public int getItemCount() {
            return mSubmenuList.size();
        }
    }

    /**
     * Method to de-select all the menu buttons
     */

    private void resetMainMenu(RecyclerView rv) {
        for (AtomicInteger i = new AtomicInteger(); i.get() < rv.getChildCount(); i.getAndIncrement()) {
            MenuAdapter.MenuViewHolder holder = (MenuAdapter.MenuViewHolder) rv.findViewHolderForAdapterPosition(i.get());
            requireNonNull(holder).btnMenuItem.setSelected(false);
        }
    }

    /**
     * Method to de-select all the sub-menu buttons
     */

    private void resetSubMenu(RecyclerView rv) {
        for (AtomicInteger i = new AtomicInteger(); i.get() < rv.getChildCount(); i.getAndIncrement()) {
            SubmenuAdapter.SubmenuViewHolder holder = (SubmenuAdapter.SubmenuViewHolder) rv.findViewHolderForAdapterPosition(i.get());
            requireNonNull(holder).btnSubmenuItem.setSelected(false);
        }
    }

    /**
     * Method to load the requested menu option
     */

    private void CallFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("CoName", strCoName);
        bundle.putString("ApiUrl", strApiUrl);
        switch (strMenuName) {
            case "Users":
                switch (strSubMenuName) {
                    case "List":
                        FragmentUserList userlistFragment = new FragmentUserList();
                        userlistFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, userlistFragment).commit();
                        break;
                    case "Add":
                        //FragmentUserAdd useraddFragment = new FragmentUserAdd();
                        //useraddFragment.setArguments(bundle);
                        //getSupportFragmentManager().beginTransaction().replace(R.id.container, useraddFragment).commit();
                        break;
                    case "Update":
                    case "Remove":
                }
                break;
            case "Grades":
            case "Departments":
                switch (strSubMenuName) {
                    case "List":
                    case "Add":
                    case "Update":
                    case "Remove":
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + strSubMenuName);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + strMenuName);
        }
    }

}