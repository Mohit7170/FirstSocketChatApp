package com.testapps.livestreamingchatting.adpaters;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.testapps.livestreamingchatting.R;
import com.testapps.livestreamingchatting.modal.MessageModal;
import com.testapps.livestreamingchatting.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Activity activity;
    private final List<String> userNames = new ArrayList<>();

    public UserAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_container_users, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

        String userName = userNames.get(position);

        if (!TextUtils.isEmpty(userName))
            holder.userName.setText(userName);

    }

    public void addItem(String userName) {
        userNames.add(userName);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_tv);
        }
    }
}
