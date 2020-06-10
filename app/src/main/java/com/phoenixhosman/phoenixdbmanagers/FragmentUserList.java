package com.phoenixhosman.phoenixdbmanagers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * User List Fragment code file
 *
 * @author Troy Marker
 * @version 1.0.0
 */
public class FragmentUserList extends Fragment {
    private ManagerAdminApi managerAdminApi;
    private RecyclerView.Adapter uAdapter;
    private ArrayList<ObjectUser> userList = new ArrayList<ObjectUser>();


    public FragmentUserList() {
    }

    /**
     * The fragment onCreateView Method
     *
     * @param inflater the layout inflater
     * @param container the container that will hold the fragment
     * @param savedInstanceState the saved instance state
     * @return View object container the fragment view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        assert getArguments() != null;
        String coName = getArguments().getString("CoName");
        String apiUrl = getArguments().getString("ApiUrl");
        managerAdminApi = new ManagerAdminApi(apiUrl);
        RecyclerView uRecyclerView = view.findViewById(R.id.recyclerViewUserList);
        LinearLayoutManager uLayoutManager = new LinearLayoutManager(this.getActivity());
        uRecyclerView.setHasFixedSize(true);
        uAdapter = new UserAdapter(userList);
        uRecyclerView.setLayoutManager(uLayoutManager);
        uRecyclerView.setAdapter(uAdapter);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.user_list_title, coName));
        readUsers();
        return view;
    }

    /**
     * Method to read the user list from the database
     */

    private void readUsers() {
        userList.clear();
        managerAdminApi.user(new Callback<ObjectUserResponse>() {
            @Override
            public void onResponse(@NonNull Call<ObjectUserResponse> call, @NonNull Response<ObjectUserResponse> response) {
                ObjectUserResponse JsonList = response.body();
                assert JsonList != null;
                ObjectUser[] data = JsonList.getData();
                for (int i = 0, dataLength = data.length; i < dataLength; i++) {
                    ObjectUser datum = data[i];
                    userList.add(new ObjectUser(
                        datum.getId(),
                        datum.getUsername(),
                        datum.getPassword(),
                        datum.getCreated(),
                        datum.getModified(),
                        datum.getGrade(),
                        datum.getGradeName(),
                        datum.getDepartment(),
                        datum.getDepartmentname()
                    ));
                }
                uAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(@NonNull Call<ObjectUserResponse> call, @NonNull Throwable t) {
            }
        });
    }

    /**
     * recyclerviewUserList display adapter
     */
    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
        final private List<ObjectUser> mUserList;
        class UserViewHolder extends RecyclerView.ViewHolder {
            final TextView tvUsername;
            final TextView tvCreated;
            final TextView tvGrade;
            final TextView tvDepartment;
            UserViewHolder(View userView) {
                super(userView);
                tvUsername = userView.findViewById(R.id.tvUsername);
                tvCreated = userView.findViewById(R.id.tvCreated);
                tvGrade = userView.findViewById(R.id.tvGrade);
                tvDepartment = userView.findViewById(R.id.tvDepartment);
            }
        }
        UserAdapter(ArrayList<ObjectUser> userlist) {
            mUserList = userlist;
        }
        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_user_list_item, parent,false);
            return new UserViewHolder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
            final ObjectUser currentUser = mUserList.get(position);
            holder.tvUsername.setText(currentUser.getUsername());
            holder.tvCreated.setText(currentUser.getCreated());
            holder.tvGrade.setText(currentUser.getGradeName());
            holder.tvDepartment.setText(currentUser.getDepartmentname());
        }
        @Override
        public int getItemCount() {
            return mUserList.size();
        }
    }
}
