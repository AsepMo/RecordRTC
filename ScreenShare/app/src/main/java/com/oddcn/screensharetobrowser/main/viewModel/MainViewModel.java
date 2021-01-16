package com.oddcn.screensharetobrowser.main.viewModel;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.oddcn.screensharetobrowser.utils.NetUtil;
import java.util.List;
import java.util.ArrayList;


public class MainViewModel {

    private Context context;

    public int mode;// 0 wifi , 1 hotspot

    
    public static int webServerPort;
    public static int wsServerPort;
	public static String localIpText;
	
    public static boolean isServerRunning;

    public int serverConnCount;

    public boolean isRecorderRunning;

    public MainViewModel(Context context) {
        this.context = context;
        mode = 0;
        webServerPort = 8123;
        wsServerPort = 8012;
        serverConnCount = 0;
		localIpText = "";
    }

	public void setWebServerPort(int webServerPort)
	{
		this.webServerPort = webServerPort;
	}
	
	public int getWebServerPort()
	{
		return webServerPort;
	}
	
	public void setWsServerPort(int wsServerPort)
	{
		this.wsServerPort = wsServerPort;
	}

	public int getWsServerPort()
	{
		return wsServerPort;
	}
	
	public void setServerConnCount(int webServerPort)
	{
		this.serverConnCount = webServerPort;
	}

	public int getServerConnCount()
	{
		return serverConnCount;
	}
	
	public void setLocalIp(String localIp)
	{
		this.localIpText = localIp;
	}

	public String getLocalIp()
	{
		return localIpText;
	}
	
    public View.OnClickListener onWifiModeViewClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode != 0) {
                    Toast.makeText(context, "本机连接WIFI后，将自动切换为该模式", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public View.OnClickListener onHotspotModeViewClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode != 1) {
                    Toast.makeText(context, "本机开启热点后，将自动切换为该模式", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void refreshIp() {
        if (mode == 0) {
            localIpText = NetUtil.getWifiIp(context);
        } else if (mode == 1) {
            localIpText = "192.168.43.1";
        }
    }

    public View.OnClickListener onImgRefreshIpClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshIp();
                Toast.makeText(context, "已刷新本机IP", Toast.LENGTH_SHORT).show();
            }
        };
    }
}
