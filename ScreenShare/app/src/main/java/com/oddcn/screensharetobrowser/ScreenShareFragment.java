package com.oddcn.screensharetobrowser;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import com.oddcn.screensharetobrowser.main.view.CustomModeView;
import com.oddcn.screensharetobrowser.utils.NetUtil;

public class ScreenShareFragment extends Fragment implements View.OnClickListener
{

    private CustomModeView wifiMode;
    private CustomModeView hostpotMode;
    private boolean mode;
    
    private TextView localIpText;
    private TextView localPort;
    private Button btnServer;
    private Button btnRecord;
    private ImageView imgRefresh;
    private RecyclerView ipConnect;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_screen_share, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        wifiMode = (CustomModeView)view.findViewById(R.id.mode_view_wifi);
        wifiMode.setOnClickListener(this);
        hostpotMode = (CustomModeView)view.findViewById(R.id.mode_view_hotspot);
        hostpotMode.setOnClickListener(this);
        
        localIpText = (TextView)view.findViewById(R.id.tv_local_ip);
        localIpText.setText(NetUtil.getWifiIp(getActivity()));
        
        imgRefresh = (ImageView)view.findViewById(R.id.img_refresh_ip);
        
        localPort = (TextView)view.findViewById(R.id.tv_local_port);
        localPort.setText("8123");
        
        btnServer = (Button)view.findViewById(R.id.btn_server);
        btnServer.setText(!ApplicationContext.isWiFIConnected() == false ? getString(R.string.start_server):getString(R.string.stop_server));
        
        btnRecord = (Button)view.findViewById(R.id.btn_record);
        btnRecord.setText(!ApplicationContext.isWiFIConnected() == false ? getString(R.string.start_record):getString(R.string.stop_record));
        btnRecord.setEnabled(ApplicationContext.isServerRunning());
        
        ipConnect = (RecyclerView)view.findViewById(R.id.recycler_view_conn);
        
        if(ApplicationContext.isWiFIConnected())
        {
            setMode(true);
        }else{
            setMode(false);
        }
        
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.mode_view_wifi:
                Toast.makeText(getActivity(), "Anda Berada Dalam Mode Wifi", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mode_view_hotspot:
                Toast.makeText(getActivity(), "Anda Berada Dalam Mode Hostpot", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    
    public void setMode(boolean mode)
    {
        this.mode = mode;
        if(mode)
        {
            wifiMode.setOpen(true);
            hostpotMode.setOpen(false);
        }else
        {
            wifiMode.setOpen(false);
            hostpotMode.setOpen(true);
        }
    }
}
