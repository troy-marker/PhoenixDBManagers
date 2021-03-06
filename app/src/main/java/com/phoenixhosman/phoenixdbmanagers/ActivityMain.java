/*
    The Phoenix Hospitality Management System
    Database Manager App
    Main Activity Code File
    Copyright (c) 2020 By Troy Marker Enterprises
    All Rights Under Copyright Reserved

    The code in this file was created for use with the Phoenix Hospitality Management System (PHMS).
    Use of this code outside the PHMS is strictly prohibited.
 */
package com.phoenixhosman.phoenixdbmanagers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.phoenixhosman.phoenixapi.ObjectMenu;
import com.phoenixhosman.phoenixapi.ObjectSubMenu;
import com.phoenixhosman.phoenixlib.ActivityPhoenixLib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static androidx.recyclerview.widget.RecyclerView.Adapter;

import static java.util.Objects.requireNonNull;

/**
 * Code for the main activity
 * @author Troy Marker
 * @version 1.0.0
 */
public class
ActivityMain extends FragmentActivity implements InterfaceDataPasser, View.OnClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerView sRecyclerView;
    private Adapter sAdapter;
    private final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
    private final LinearLayoutManager sLayoutManager = new LinearLayoutManager(this);
    private String strCoName;
    private String strApiUrl;
    private String strGradename = "";
    private String strMenuName;
    private String strSubMenuName;
    private int intSupplier;
    private int intCategory;
    private int intSubcategory;
    private final ActivityPhoenixLib Phoenix = new ActivityPhoenixLib();
    private final Bundle args = new Bundle();
    private final ArrayList<ObjectMenu> MenuList = new ArrayList<>();
    private final ArrayList<ObjectSubMenu> objSubMenu = new ArrayList<>();
    private final ArrayList<ObjectMenu> SubmenuList = new ArrayList<>();
    private final FragmentBlank blank1 = new FragmentBlank();
    private final FragmentBlank blank2 = new FragmentBlank();


    /**
     * Override of the parent onCreate method
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
            Phoenix.Error(this, getResources().getString(R.string.settingmissing), false);
        }
        cursor = getContentResolver().query(Uri.parse("content://com.phoenixhosman.launcher.ProviderUser/acl"), null, null, null, null, null);
        assert cursor != null;
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                strGradename = cursor.getString(cursor.getColumnIndex("gradename"));
                cursor.moveToNext();
            }
        } else {
            Phoenix.Error(this, getResources().getString(R.string.nologged), true);
        }
        if (!strGradename.contains("Administrator")) {
            Phoenix.Error(this, getResources().getString(R.string.adminreq), true);
        }
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recyclerViewMainMenu);
        sRecyclerView = findViewById(R.id.recyclerViewSubMenu);
        Button btnExitButton = findViewById(R.id.btnExitButton);
        Button btnRefreshButton = findViewById(R.id.btnRefreshButton);
        args.putString("CoName", strCoName);
        args.putString("ApiUrl", strApiUrl);
        Adapter mAdapter = new MenuAdapter(MenuList);
        sAdapter = new SubmenuAdapter(SubmenuList);
        mRecyclerView.setHasFixedSize(true);
        sRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        sRecyclerView.setLayoutManager(sLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        sRecyclerView.setAdapter(sAdapter);
        btnExitButton.setOnClickListener(this);
        btnRefreshButton.setOnClickListener(this);
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
     * Method to define the main and sub menus
     */
    public void BuildMenu() {
        MenuList.add(new ObjectMenu("Users"));
        MenuList.add(new ObjectMenu("Grades"));
        MenuList.add(new ObjectMenu("Departments"));
        MenuList.add(new ObjectMenu("Inventory"));
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

    @Override
    public void onClick(View v) {
        Button button = (Button)v;
        String buttonText = button.getText().toString();
        if (buttonText.equals(getResources().getString(R.string.refresh))) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        } else if (buttonText.equals(getResources().getString(R.string.exit))) {
            finishAffinity();
        } else {
            throw new IllegalStateException(getResources().getString(R.string.unexpected) + buttonText);
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
                    if (strMenuName.equals("Inventory")) {
                        CallFragment();
                        SubmenuList.clear();
                    } else {
                        GetSubMenu(currentMenu.getName());
                    }
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
        FragmentGradeList gradelistFragment = new FragmentGradeList();
        FragmentDepartmentList departmentlistFragment = new FragmentDepartmentList();
        FragmentUserAdd useraddFragment = new FragmentUserAdd();
        FragmentGradeAdd gradeaddFragment = new FragmentGradeAdd();
        FragmentDepartmentAdd departmentaddFragment = new FragmentDepartmentAdd();
        FragmentInventoryBottom inventoryBottomFrame = new FragmentInventoryBottom();
        FragmentInventoryTop inventoryTopFrame = new FragmentInventoryTop();
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, userlistFragment).commit();
                        break;
                    case "Remove":
                        args.putBoolean("update", false);
                        args.putBoolean("remove", true);
                        userlistFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, userlistFragment).commit();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + strSubMenuName);                       
                }
                break;
            case "Grades":
                switch (strSubMenuName) {
                    case "List":
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
                        args.putBoolean("update", false);
                        args.putBoolean("remove", false);
                        gradelistFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, gradelistFragment).commit();
                        break;
                    case "Add":
                        args.putBoolean("update", false);
                        args.putBoolean("remove", false);
                        gradelistFragment.setArguments(args);
                        gradeaddFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, gradeaddFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, gradelistFragment).commit();
                        break;
                    case "Update":
                        args.putBoolean("update", true);
                        args.putBoolean("remove", false);
                        gradelistFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, gradelistFragment).commit();
                        break;
                    case "Remove":
                        args.putBoolean("update", false);
                        args.putBoolean("remove", true);
                        gradelistFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, gradelistFragment).commit();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + strSubMenuName);
                }
                break;
            case "Departments":
                switch (strSubMenuName) {
                    case "List":
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
                        args.putBoolean("update", false);
                        args.putBoolean("remove", false);
                        departmentlistFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, departmentlistFragment).commit();
                        break;
                    case "Add":
                        args.putBoolean("update", false);
                        args.putBoolean("remove", false);
                        departmentlistFragment.setArguments(args);
                        departmentaddFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, departmentaddFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, departmentlistFragment).commit();
                        break;
                    case "Update":
                        args.putBoolean("update", true);
                        args.putBoolean("remove", false);
                        departmentlistFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, departmentlistFragment).commit();
                        break;
                    case "Remove":
                        args.putBoolean("update", false);
                        args.putBoolean("remove", true);
                        departmentlistFragment.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
                        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, departmentlistFragment).commit();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + strSubMenuName);
                }
                break;
            case "Inventory":
                args.putString("CoName", strCoName);
                args.putString("ApiUrl", strApiUrl);
                inventoryBottomFrame.setArguments(args);
                inventoryTopFrame.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, inventoryTopFrame).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, inventoryBottomFrame).commit();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + strMenuName);
        }
    }

    public void ClearTopFrame() {
        FragmentBlank blankFragment = new FragmentBlank();
        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, blankFragment).commit();
    }

    public void LoadUserList() {
        FragmentUserList userlistFragment = new FragmentUserList();
        userlistFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, userlistFragment).commit();
    }

    public void LoadGradeList() {
        FragmentGradeList gradelistFragment = new FragmentGradeList();
        gradelistFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame, gradelistFragment).commit();
    }

    public void LoadDepartmentList() {
        FragmentDepartmentList departmentlistFragment = new FragmentDepartmentList();
        departmentlistFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomFrame,departmentlistFragment).commit();
    }

    @Override
    public void onUserUpdate(int id) {
        if (id <= 1) {
            Phoenix.Error(this, "Can not update built-in Administrator", false);
        } else {
            FragmentUserUpdate userupdateFragment = new FragmentUserUpdate();
            args.putString("ApiUrl", strApiUrl);
            args.putString("CoName", strCoName);
            args.putInt("record", id);
            userupdateFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, userupdateFragment).commit();
        }
    }

    @Override
    public void onUserRemove(int id) {
        if (id <= 1) {
            Phoenix.Error(this, "Can not remove built-in Administrator", false);
        } else {
            FragmentUserRemove userremoveFragment = new FragmentUserRemove();
            args.putString("ApiUrl", strApiUrl);
            args.putString("CoName", strCoName);
            args.putInt("record", id);
            userremoveFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, userremoveFragment).commit();
        }
    }

    @Override
    public void onGradeUpdate(int id) {
        if (id <= 4) {
            Phoenix.Error(this, "Can not change a built-in grade", false);
        } else {
            FragmentGradeUpdate gradeupdateFragment = new FragmentGradeUpdate();
            args.putString("ApiUrl", strApiUrl);
            args.putString("CoName", strCoName);
            args.putInt("record", id);
            gradeupdateFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, gradeupdateFragment).commit();
        }
    }

    @Override
    public void onGradeRemove(int id) {
        if (id <= 4) {
            Phoenix.Error(this, "Can not remove a built-in grade", false);
        } else {
            FragmentGradeRemove graderemoveFragment = new FragmentGradeRemove();
            args.putString("ApiUrl", strApiUrl);
            args.putString("CoName", strCoName);
            args.putInt("record", id);
            graderemoveFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, graderemoveFragment).commit();
        }
    }

    @Override
    public void onDepartmentUpdate(int id) {
        if (id <= 6) {
            Phoenix.Error(this, "Can not change a built-in department", false);
        } else {
            FragmentDepartmentUpdate departmentupdateFragment;
            departmentupdateFragment = new FragmentDepartmentUpdate();
            args.putString("ApiUrl", strApiUrl);
            args.putString("CoName", strCoName);
            args.putInt("record", id);
            departmentupdateFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, departmentupdateFragment).commit();
        }
    }

    @Override
    public void onDepartmentRemove(int id) {
        if (id <= 6) {
            Phoenix.Error(this, "Can not remove a built-in department", false);
        } else {
            FragmentDepartmentRemove departmentremoveFragment = new FragmentDepartmentRemove();
            args.putString("ApiUrl", strApiUrl);
            args.putString("CoName", strCoName);
            args.putInt("record", id);
            departmentremoveFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, departmentremoveFragment).commit();
        }
    }

    @Override
    public void onSupplierAdd() {
        FragmentInventorySupplier fragmentInventorySupplier = new FragmentInventorySupplier();
        FragmentInventorySelector fragmentInventorySelector = new FragmentInventorySelector();
        FragmentBlank fragmentBlank1 = new FragmentBlank();
        FragmentBlank fragmentBlank2 = new FragmentBlank();
        args.putString("ApiUrl", strApiUrl);
        args.putString("CoName", strCoName);
        fragmentInventorySupplier.setArguments(args);
        fragmentInventorySelector.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, fragmentInventorySupplier).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomRightFrame, fragmentBlank1).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomLeftFrame, fragmentBlank2).commit();
    }

    @Override
    public void onItemList(Bundle data) {
        FragmentInventoryList fragmentInventoryList = new FragmentInventoryList();
        FragmentInventoryTop fragmentInventoryTop = new FragmentInventoryTop();
        args.putString("ApiUrl", strApiUrl);
        args.putString("CoName", strCoName);
        intSupplier = data.getInt("supplier");
        intCategory = data.getInt("category");
        intSubcategory = data.getInt("subcategory");
        args.putInt("supplier", intSupplier);
        args.putInt("category", intCategory);
        args.putInt("subcategory", intSubcategory);
        fragmentInventoryList.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomRightFrame, fragmentInventoryList).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, fragmentInventoryTop).commit();
    }

    @Override
    public void onItemInfo(Bundle data) {
        FragmentInventoryDetails fragmentInventoryDetails = new FragmentInventoryDetails();
        args.putString("ApiUrl", strApiUrl);
        args.putString("CoName", strCoName);
        args.putInt("supplier", intSupplier);
        args.putInt("category", intCategory);
        args.putInt("subcategory", intSubcategory);
        args.putString("item", data.getString("item"));
        args.putString("description", data.getString("description"));
        args.putInt("casecount", data.getInt("casecount"));
        args.putInt("count", data.getInt("count"));
        fragmentInventoryDetails.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.topLeftFrame, fragmentInventoryDetails).commit();
    }

    @Override
    public void onItemPrice(Bundle data) {
        FragmentInventoryPricing fragmentInventoryPricing = new FragmentInventoryPricing();
        args.putString("ApiUrl", strApiUrl);
        args.putString("CoName", strCoName);
        args.putInt("supplier", intSupplier);
        args.putInt("category", intCategory);
        args.putInt("subcategory", intSubcategory);
        args.putString("item", data.getString("item"));
        args.putInt("tier1count", data.getInt("tier1count"));
        args.putDouble("tier1cost", data.getDouble("tier1cost"));
        args.putInt("tier2count", data.getInt("tier2count"));
        args.putDouble("tier2cost", data.getDouble("tier2cost"));
        args.putInt("tier3count", data.getInt("tier3count"));
        args.putDouble("tier3cost", data.getDouble("tier3cost"));
        fragmentInventoryPricing.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.topRightFrame, fragmentInventoryPricing).commit();

    }

    @Override
    public void onCloseSupplier() {
        FragmentInventoryTop fragmentInventoryTop = new FragmentInventoryTop();
        FragmentInventorySelector fragmentInventorySelector = new FragmentInventorySelector();
        args.putString("ApiUrl", strApiUrl);
        args.putString("CoName", strCoName);
        fragmentInventorySelector.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.topFrame, fragmentInventoryTop).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomLeftFrame, fragmentInventorySelector).commit();
    }
}