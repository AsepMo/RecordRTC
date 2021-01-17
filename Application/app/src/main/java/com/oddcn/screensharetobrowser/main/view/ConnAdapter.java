package com.oddcn.screensharetobrowser.main.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oddcn.screensharetobrowser.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oddzh on 2017/11/1.
 */

public class ConnAdapter extends RecyclerView.Adapter<ConnAdapter.ConnBindingHolder> {

    private List<String> connList = new ArrayList<>();

    public void setData(List<String> connList) {
        this.connList = connList;
    }

    @Override
    public ConnAdapter.ConnBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conn, parent, false);
        return new ConnBindingHolder(view);
    }

    @Override
    public void onBindViewHolder(ConnAdapter.ConnBindingHolder holder, int position) {
        holder.connIp.setText(connList.get(position));
    }

    @Override
    public int getItemCount() {
        return connList.size();
    }

    public class ConnBindingHolder extends RecyclerView.ViewHolder {

        public TextView connIp;

        public ConnBindingHolder(View itemView) {
            super(itemView);
            connIp = (TextView)itemView.findViewById(R.id.connIp);;
        }
    }

}
