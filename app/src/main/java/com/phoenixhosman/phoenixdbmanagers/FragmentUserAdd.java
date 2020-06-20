package com.phoenixhosman.phoenixdbmanagers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.R.layout.simple_spinner_item;

/**
 * User Add Fragment code file
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public class FragmentUserAdd extends Fragment implements OnClickListener, android.widget.AdapterView.OnItemSelectedListener {

    //ManagerAdminApi managerAdminApi;
    String apiUrl;
    Button btnAddUser;
    Button btnCancel;
    EditText etUsername;
    EditText etPassword;
    Spinner gradeSpinner;
    Spinner departmentSpinner;
    int gradeIndex;
    int departmentIndex;
    private final ArrayList<String> gradeNames = new ArrayList<>();
    private final ArrayList<String> departmentNames = new ArrayList<>();


    public FragmentUserAdd()  {
    }

    /**
     * The fragment onCreateView Method
     *
     * @param inflater the layout inflater
     * @param container the container that will hold the fragment
     * @param savedInstanceState the saved instance state
     * @return View object container the fragment view
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_add, container, false);
        assert getArguments() != null;
        String coName = getArguments().getString("CoName");
        apiUrl = getArguments().getString("ApiUrl");
        btnAddUser = view.findViewById(R.id.btnAddUser);
        btnCancel = view.findViewById(R.id.btnCancel);
        etUsername = view.findViewById(R.id.etUsername);
        etPassword = view.findViewById(R.id.etPassword);
        gradeSpinner = view.findViewById(R.id.gradeSpinner);
        departmentSpinner = view.findViewById(R.id.departmentSpinner);
        gradeIndex = 0;
        departmentIndex = 0;
        btnAddUser.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        gradeSpinner.setOnItemSelectedListener(this);
        departmentSpinner.setOnItemSelectedListener(this);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.user_add_title, coName));
        readGrades();
        readDepartments();
        return view;
    }

    /**
     * Method to read the Grade List from the database
     */

    private void readGrades() {
        ManagerAdminApi managerAdminApi = new ManagerAdminApi(apiUrl, "SCALARS");
        managerAdminApi.grade(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String jsonresponse = response.body();
                    displayGrades(jsonresponse);
                } else {
                    Toast.makeText(getContext(),"Failed loading Grade List", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * Method to load the grades into the  grades spinner
     * @param response JSON Object containing the Api response
     */
    private void displayGrades(String response){
        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("success").equals("true")) {
                ArrayList<ObjectGrade> gradeList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i=0; i < dataArray.length(); i++) {
                    ObjectGrade gradeSpinner = new ObjectGrade();
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    gradeSpinner.setId(dataObj.getInt("id"));
                    gradeSpinner.setGrade(dataObj.getString("grade"));
                    gradeList.add(gradeSpinner);
                }
                gradeNames.add("Select Grade");
                for (int i = 0; i < gradeList.size(); i++){
                    gradeNames.add(gradeList.get(i).getGrade());
                }
                ArrayAdapter<String> gradeArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), simple_spinner_item, gradeNames);
                gradeArrayAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
                gradeSpinner.setAdapter(gradeArrayAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to load the department list from the database
     */
    public void readDepartments() {
        ManagerAdminApi managerAdminApi = new ManagerAdminApi(apiUrl, "SCALARS");
        managerAdminApi.department(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String jsonresponse = response.body();
                    displayDepartments(jsonresponse);
                } else {
                    Toast.makeText(getContext(),"Failed loading Department List", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * Method to load the grades into the  grades spinner
     * @param response JSON Object containing the Api response
     */
    private void displayDepartments(String response){
        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("success").equals("true")) {
                ArrayList<ObjectDepartment> departmentList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i=0; i < dataArray.length(); i++) {
                    ObjectDepartment departmentSpinner = new ObjectDepartment();
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    departmentSpinner.setId(dataObj.getInt("id"));
                    departmentSpinner.setDepartment(dataObj.getString("department"));
                    departmentList.add(departmentSpinner);
                }
                departmentNames.add("Select Department");
                for (int i = 0; i < departmentList.size(); i++){
                    departmentNames.add(departmentList.get(i).getDepartment());
                }
                ArrayAdapter<String> departmentArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), simple_spinner_item, departmentNames);
                departmentArrayAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
                departmentSpinner.setAdapter(departmentArrayAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * onClick listener
     * @param v the view triggering the action
     */
    @Override
    public void onClick(View v) {
        ManagerAdminApi managerAdminApi = new ManagerAdminApi(apiUrl, "SCALARS");
        switch(v.getId()) {
            case R.id.btnCancel:
                ((ActivityMain) Objects.requireNonNull(getActivity())).ClearTopFrame();
                break;
            case R.id.btnAddUser:
                if (etUsername.getText().toString().isEmpty()) {
                    ((ActivityMain) Objects.requireNonNull(getActivity())).Error("A username is required", false);
                } else {
                    if (etPassword.getText().toString().isEmpty()) {
                        ((ActivityMain) Objects.requireNonNull(getActivity())).Error("A password is required", false);
                    } else {
                        if (gradeIndex == 0) {
                            ((ActivityMain) Objects.requireNonNull(getActivity())).Error("Select a grade", false);
                        } else {
                            if (departmentIndex == 0) {
                                ((ActivityMain) Objects.requireNonNull(getActivity())).Error("Select a department", false);
                            } else {
                                Call<String> call = managerAdminApi.getInstance().getApi().user(etUsername.getText().toString(), etPassword.getText().toString(), gradeIndex, departmentIndex);
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                        String body = response.body();
                                            try {
                                                assert body != null;
                                                JSONObject obj = new JSONObject(body);
                                                if (obj.optString("success").equals("false")) {
                                                    Toast.makeText(getContext(),obj.optString("message"), Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getContext(), "User successfully created", Toast.LENGTH_LONG).show();
                                                    ((ActivityMain) Objects.requireNonNull(getActivity())).LoadUserList();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                    }
                                    @Override
                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                    }
                                });
                            }
                        }
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    /**
     * Callback method to be invoked when an item in the spinner has been selected.
     * @param parent AdapterView: The AdapterView where the selection happened
     * @param view View: The view within the AdapterView that was clicked
     * @param position int: The position of the view in the adapter
     * @param id long: The row id of the item that is selected
     */
    @Override
    public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.gradeSpinner:
                gradeIndex = position;
                break;
            case R.id.departmentSpinner:
                departmentIndex = position;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from this view.
     * @param parent AdapterView: The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(android.widget.AdapterView<?> parent) {

    }
}
