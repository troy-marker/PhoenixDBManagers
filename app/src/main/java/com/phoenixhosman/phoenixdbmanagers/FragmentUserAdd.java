package com.phoenixhosman.phoenixdbmanagers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
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
public class FragmentUserAdd extends Fragment {

    ManagerAdminApi managerAdminApi;
    Button btnAddUser;
    Spinner gradeSpinner;
    private ArrayList<ObjectGrade> gradeList = new ArrayList<>();
    private ArrayList<String> gradeNames = new ArrayList<>();

    public FragmentUserAdd() {
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
        String apiUrl = getArguments().getString("ApiUrl");
        managerAdminApi = new ManagerAdminApi(apiUrl);
        btnAddUser = view.findViewById(R.id.btnAddUser);
        gradeSpinner = view.findViewById(R.id.gradeSpinner);
        btnAddUser.setEnabled(false);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.user_add_title, coName));
        readGrades();


        return view;
    }

    /**
     * Method to read the Grade List from the database
     */

    private void readGrades() {
        managerAdminApi.grade(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i("Response String:", response.body().toString());
                if (response.isSuccessful()) {
                    Log.i("onSuccess: ", response.body().toString());
                    String jsonresponse = response.body().toString();
                    displayGrades(jsonresponse);
                } else {
                    Log.i("onEmptyResponse: ", "Returned Empty Response");
                    Toast.makeText(getContext(),"Nothing returned", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void displayGrades(String response){
        try {
            JSONObject obj = new JSONObject(response);
            if(obj.optString("success").equals("true")) {
                gradeList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i=0; i < dataArray.length(); i++) {
                    ObjectGrade gradeSpinner = new ObjectGrade();
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    gradeSpinner.setId(dataObj.getInt("id"));
                    gradeSpinner.setGrade(dataObj.getString("grade"));
                    gradeList.add(gradeSpinner);
                }
                for (int i = 0; i < gradeList.size(); i++){
                    gradeNames.add(gradeList.get(i).getGrade());
                }
                ArrayAdapter<String> gradeArrayAdapter = new ArrayAdapter<String> (Objects.requireNonNull(getActivity()), simple_spinner_item, gradeNames);
                gradeArrayAdapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
                gradeSpinner.setAdapter(gradeArrayAdapter);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
