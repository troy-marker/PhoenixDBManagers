package com.phoenixhosman.phoenixdbmanagers;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.inflate;
import static java.util.Objects.requireNonNull;



/**
 * Code for the main activity
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public class ActivityMain extends FragmentActivity implements InterfaceDataPasser {
    private RecyclerView mRecyclerView;
    private RecyclerView sRecyclerView;
    private Adapter sAdapter;
    private final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private final LinearLayoutManager sLayoutManager = new LinearLayoutManager(this);
    private String strCoName;
    private String strApiUrl;
    private Integer intRecord;
    private String strGradename;
    private String strMenuName;
    private String strSubMenuName;
    private Button btnExitButton;
    private Bundle args = new Bundle();
    private final ArrayList<ObjectMenu> MenuList = new ArrayList<>();
    private final ArrayList<ObjectSubMenu> objSubMenu = new ArrayList<>();
    private final ArrayList<ObjectMenu> SubmenuList = new ArrayList<>();
    private final FragmentBlank blank1 = new FragmentBlank();
    private final FragmentBlank blank2 = new FragmentBlank();

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
                cursor.moveToNext();
            }
        } else {
            Error("\nRequired setting missing.\nPlease (re)run Phoenix Install App", false);
        }
        cursor = getContentResolver().query(Uri.parse("content://com.phoenixhosman.launcher.ProviderUser/acl"), null, null, null, null, null);
        assert cursor != null;
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                strGradename = cursor.getString(cursor.getColumnIndex("gradename"));
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
        args.putString("CoName", strCoName);
        args.putString("ApiUrl", strApiUrl);
        Adapter mAdapter = new MenuAdapter(MenuList);
        sAdapter = new SubmenuAdapter(SubmenuList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        sRecyclerView.setLayoutManager(sLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        sRecyclerView.setAdapter(sAdapter);
        btnExitButton = findViewById(R.id.btnExitButton);
        btnExitButton.setOnClickListener(v -> finishAndRemoveTask());
        if (findViewById(R.id.topFrame) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getSupportFragmentManager().beginTransaction().add(R.id.topFrame, blank1).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.bottomFrame, blank2).commit();
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
    public void Error(String strError, Boolean exit) {
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
                getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blank1).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, blank2).commit();
                if (holder.btnMenuItem.isSelected()) {
                    strSubMenuName = null;
                    holder.btnMenuItem.setSelected(false);
                    resetSubMenu(sRecyclerView);
                    SubmenuList.clear();
                } else {
                    resetMainMenu(mRecyclerView);
                    resetSubMenu(sRecyclerView);
                    holder.btnMenuItem.setSelected(true);
                    strMenuName = currentMenu.getName();
                    GetSubMenu(currentMenu.getName());
                }
                sAdapter.notifyDataSetChanged();
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blank1).commit();
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, blank2).commit();
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

        FragmentBlank blankFragment = new FragmentBlank();
        FragmentUserList userlistFragment = new FragmentUserList();
        FragmentUserAdd useraddFragment = new FragmentUserAdd();

        switch (strMenuName) {
            case "Users":
                switch (strSubMenuName) {
                    case "List":
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
                        args.putBoolean("update", false);
                        args.putBoolean("remove", false);
                        userlistFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, userlistFragment).commit();
                        break;
                    case "Add":
                        useraddFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, useraddFragment).commit();
                        args.putBoolean("update", false);
                        args.putBoolean("remove", false);
                        userlistFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, userlistFragment).commit();
                        break;
                    case "Update":
                        args.putBoolean("update", true);
                        args.putBoolean("remove", false);
                        userlistFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, userlistFragment).commit();
                        break;
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
        }
    }

    public void ClearTopFrame() {
        FragmentBlank blankFragment = new FragmentBlank();
        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
        resetSubMenu(sRecyclerView);
    }

    public void LoadUserList() {
        FragmentUserList userlistFragment = new FragmentUserList();
        userlistFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, userlistFragment).commit();
    }

    @Override
    public void onUpdate(int id) {
        FragmentUserUpdate userupdateFragment = new FragmentUserUpdate();
        args.putString("ApiUrl", strApiUrl);
        args.putString("CoName", strCoName);
        args.putInt("record", id);
        userupdateFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, userupdateFragment).commit();
    }

    @Override
    public void onRemove(int id) {

    }


}