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

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final Activity activity;
    private List<MessageModal> messageModals = new ArrayList<>();

    public MessageAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_container_messages, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        MessageModal messageModal = messageModals.get(position);

        if (!TextUtils.isEmpty(messageModal.getMessage()))
            holder.message.setText(messageModal.getMessage());

        if (!TextUtils.isEmpty(messageModal.getName()))
            holder.userName.setText(messageModal.getName());

    }

    public void addItem(MessageModal messageModal) {
        messageModals.add(messageModal);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messageModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message_tv);
            userName = itemView.findViewById(R.id.user_name_tv);
        }
    }
}
