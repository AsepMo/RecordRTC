package com.oddcn.screensharetobrowser.main.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oddcn.screensharetobrowser.R;
import com.oddcn.screensharetobrowser.main.viewModel.ConnViewModel;

import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;

/**
 * Created by oddzh on 2017/11/1.
 */

public class ConnAdapter extends RecyclerView.Adapter<ConnAdapter.ConnBindingHolder> {

    private List<ConnViewModel> connList = new ArrayList<>();

	private String connIp;
    public void setData(List<ConnViewModel> connList, String connIp) {
        this.connList = connList;
		this.connIp = connIp;
    }

    @Override
    public ConnAdapter.ConnBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conn, parent, false);
        return new ConnBindingHolder(view);
    }

    @Override
    public void onBindViewHolder(ConnAdapter.ConnBindingHolder holder, int position) {
		ConnViewModel v = connList.get(position);
        holder.ipConnect.setText(v.getConnIp());
    }

    @Override
    public int getItemCount() {
        return connList.size();
    }

    public class ConnBindingHolder extends RecyclerView.ViewHolder {

        public TextView ipConnect;

        public ConnBindingHolder(View itemView) {
            super(itemView);
            ipConnect = (TextView)itemView.findViewById(R.id.connIp);
        }
    }

}
