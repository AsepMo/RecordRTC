package com.androweb.screenshare;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;

import com.androweb.screenshare.R;
import com.androweb.screenshare.recorder.RecordService;
import com.androweb.screenshare.recorder.RecordServiceListener;
import com.androweb.screenshare.server.ServerService;
import com.androweb.screenshare.server.ServerServiceListener;
import com.androweb.screenshare.server.webServer.WebServer;
import com.androweb.screenshare.utils.PermissionUtil;
import com.androweb.screenshare.utils.NetUtil;
import com.androweb.screenshare.main.view.CustomModeView;
import com.androweb.screenshare.main.view.ConnAdapter;
import com.androweb.screenshare.connection.ConnectionActivity;

public class ScreenShareFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "MainFragment";

    private ServerService serverService;

    public static final int RECORD_REQUEST_CODE = 101;
    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private RecordService recordService;

    private ConnAdapter connAdapter;

    private BroadcastReceiver broadcastReceiverNetworkState;

    private CustomModeView wifiMode;
    private CustomModeView hostpotMode;
    private boolean mode;

    private TextView localIpText;
    private TextView localPort;
    private Button btnServer;
    private Button btnRecord;
    private ImageView imgRefresh;
    private RecyclerView recyclerViewConn;

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

        imgRefresh = (ImageView)view.findViewById(R.id.img_refresh_ip);
        imgRefresh.setOnClickListener(this);

        localPort = (TextView)view.findViewById(R.id.tv_local_port);    
        localPort.setText("8123");

        btnServer = (Button)view.findViewById(R.id.btn_server);
        btnServer.setText(R.string.start_server);

        btnRecord = (Button)view.findViewById(R.id.btn_record);
        btnRecord.setText(R.string.start_record);
        btnRecord.setEnabled(false);

        recyclerViewConn = (RecyclerView)view.findViewById(R.id.recycler_view_conn);


        initView();
        initEvent();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
		{
            case R.id.mode_view_wifi:
                Toast.makeText(getActivity(), "Anda Berada Dalam Mode Wifi", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mode_view_hotspot:
				Intent hostpot = new Intent(getActivity(), ConnectionActivity.class);
				startActivity(hostpot);
                //Toast.makeText(getActivity(), "Anda Berada Dalam Mode Hostpot", Toast.LENGTH_SHORT).show();
                break;
			case R.id.img_refresh_ip:
                StringBuilder sb = new StringBuilder();
                sb.append(ApplicationContext.getWebServerPort());

                localPort.setText(sb.toString());

                break;
        }
    }


    public void setMode(boolean mode)
    {
        this.mode = mode;
        if (mode)
        {
            wifiMode.setOpen(true);
            hostpotMode.setOpen(false);
            ApplicationContext.setLocalIp(NetUtil.getWifiIp(getActivity())); 
			localIpText.setText(NetUtil.getWifiIp(getActivity()));      
        }
		else
        {
            wifiMode.setOpen(false);
            hostpotMode.setOpen(true);
            ApplicationContext.setLocalIp("192.168.43.1");
			localIpText.setText("192.168.43.1");
        }
    }

    private void initView()
    {
        connAdapter = new ConnAdapter();
        recyclerViewConn.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewConn.setAdapter(connAdapter);
    }

    private void initEvent()
	{
        initBroadcastReceiverNetworkStateChanged();
        projectionManager = (MediaProjectionManager) getActivity().getSystemService(getContext().MEDIA_PROJECTION_SERVICE);

        Intent serverIntent = new Intent(getActivity(), ServerService.class);
        getActivity().bindService(serverIntent, serverConnection, BIND_AUTO_CREATE);

        Intent recordIntent = new Intent(getActivity(), RecordService.class);
        getActivity().bindService(recordIntent, recorderConnection, BIND_AUTO_CREATE);

        btnServer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
				{

                    if (serverService.isRunning())
					{
                        serverService.stopServer();
                        recordService.stopRecord();  
                        btnServer.setText(R.string.start_server);
                        btnRecord.setEnabled(false);
                    }
					else
					{
                        serverService.startServer();
                        btnServer.setText(R.string.stop_server);
                        btnRecord.setEnabled(true);
                    }
                }
            });
        btnRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
				{
                    PermissionUtil.requestPermission(
                        getActivity(),
                        new PermissionUtil.PermissionsListener() {
                            @Override
                            public void onGranted()
							{
                                if (recordService.isRunning())
								{
                                    recordService.stopRecord();
                                    btnRecord.setText(getString(R.string.start_record));                               
                                }
								else
								{
                                    btnRecord.setText(getString(R.string.stop_record));                               
                                    Intent captureIntent = projectionManager.createScreenCaptureIntent();
                                    startActivityForResult(captureIntent, RECORD_REQUEST_CODE);
                                }
                            }
                        },
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    );
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        if (requestCode == RECORD_REQUEST_CODE && resultCode == RESULT_OK)
		{
            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
            recordService.setMediaProject(mediaProjection);
            new Thread(new Runnable() {
                    @Override
                    public void run()
					{
                        recordService.startRecord();
                    }
                }).start();
        }
    }

    private ServiceConnection serverConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
		{
            ServerService.ServerServiceBinder binder = (ServerService.ServerServiceBinder) service;
            serverService = binder.getServerService();
            ApplicationContext.setIsServerRunning(serverService.isRunning());

            serverService.setListener(new ServerServiceListener() {
                    @Override
                    public void onServerStatusChanged(boolean isRunning)
					{

                        ApplicationContext.setIsServerRunning(isRunning);
                        ApplicationContext.setServerConnCount(0);
                        getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run()
								{
                                    List<String> emptyConns = new ArrayList<>();
                                    connAdapter.setData(emptyConns);
                                    connAdapter.notifyDataSetChanged();
                                }
                            });
                    }

                    @Override
                    public void onWebServerError(final int errorType)
					{
                        getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run()
								{
                                    if (errorType == WebServer.ERROR_TYPE_PORT_IN_USE)
                                        Toast.makeText(serverService, "因端口占用，已更换至可用端口", Toast.LENGTH_SHORT).show();
                                }
                            });
                    }

                    @Override
                    public void onWsServerError(final int errorType)
					{
                    }

                    @Override
                    public void onWsServerConnChanged(final List<String> connList)
					{
                        ApplicationContext.setServerConnCount(connList.size());
                        getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run()
								{
                                    connAdapter.setData(connList);
                                    connAdapter.notifyDataSetChanged();
                                }
                            });
                    }
                });
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
		{
        }
    };

    private ServiceConnection recorderConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service)
		{
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            RecordService.RecordBinder binder = (RecordService.RecordBinder) service;
            recordService = binder.getRecordService();
            recordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
            ApplicationContext.setIsRecordingRunning(recordService.isRunning());

            recordService.setListener(new RecordServiceListener() {
                    @Override
                    public void onRecorderStatusChanged(boolean isRunning)
					{
                        ApplicationContext.setIsRecordingRunning(isRunning);
                    }
                });
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
		{
        }
    };

    @Override
    public void onDestroy()
	{
        serverService.removeListener();
        recordService.removeListener();
        getActivity().unbindService(serverConnection);
        getActivity().unbindService(recorderConnection);
        if (broadcastReceiverNetworkState != null)
		{
            getActivity().unregisterReceiver(broadcastReceiverNetworkState);
        }
        super.onDestroy();
    }

    private void initBroadcastReceiverNetworkStateChanged()
	{
        final IntentFilter filters = new IntentFilter();
        filters.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filters.addAction("android.net.wifi.STATE_CHANGE");
        filters.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        broadcastReceiverNetworkState = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
			{
                Log.d(TAG, "onReceive: " + intent.getAction());
                int state = intent.getIntExtra("wifi_state", -66);
                Log.i(TAG, "state= " + state);
                if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(intent.getAction())
                    && state == 13)
				{
                    setMode(false);
                }
				else
				{
                    setMode(true);
                }

            }
        };
        getActivity().registerReceiver(broadcastReceiverNetworkState, filters);
    }

}
