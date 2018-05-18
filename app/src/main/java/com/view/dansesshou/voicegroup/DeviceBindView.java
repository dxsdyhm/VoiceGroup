package com.view.dansesshou.voicegroup;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by dxs on 2018/5/16.
 */

public class DeviceBindView extends ItemViewBinder<Device,DeviceBindView.ViewHolder> {
    @NonNull
    @Override
    protected DeviceBindView.ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_rc_device, parent, false);
        return new DeviceBindView.ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull DeviceBindView.ViewHolder holder, @NonNull Device item) {
        holder.deviceName.setText(item.getName());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final TextView deviceName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.deviceName =itemView.findViewById(R.id.tx_device_name);
        }
    }
}
