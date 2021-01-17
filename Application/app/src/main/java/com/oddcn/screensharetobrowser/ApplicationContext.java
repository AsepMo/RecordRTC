package com.oddcn.screensharetobrowser;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.wifi.WifiManager;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedTransferQueue;


public class ApplicationContext extends Application {
    private static ApplicationContext instance;

    private ApplicationSettings applicationSettings;
    
    private volatile boolean isServerRunning;
    private volatile boolean isRecorderRunning;
    private volatile int webServerPort;
    private volatile int wsServerPort;
    private volatile int serverConnCount;
    private volatile String localIp;
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        applicationSettings = new ApplicationSettings(this);
        setWebServerPort(8123);
        setWsServerPort(8012);
        setServerConnCount(0);
        
    }

    public static ApplicationSettings getApplicationSettings() {
        return instance.applicationSettings;
    }

    public static boolean isServerRunning() {
        return instance.isServerRunning;
    }

    public static void setIsServerRunning(final boolean isRunning) {
        instance.isServerRunning = isRunning;
    }

    public static boolean isRecorderRunning() {
        return instance.isRecorderRunning;
    }

    public static void setIsRecordingRunning(final boolean isRunning) {
        instance.isRecorderRunning = isRunning;
    }
    
    public static void setLocalIp(final String port) {
        instance.localIp = port;
    }

    public static String getLocalIp()
    {
        return instance.localIp;
    }

    public static void setWebServerPort(final int port) {
        instance.webServerPort = port;
    }
    
    public static int getWebServerPort()
    {
        return instance.webServerPort;
    }
    
    public static void setWsServerPort(final int port) {
        instance.wsServerPort = port;
    }

    public static int getWsServerPort()
    {
        return instance.wsServerPort;
    }
    
    public static void setServerConnCount(final int port) {
        instance.serverConnCount = port;
    }

    public static int getServerConnCount()
    {
        return instance.serverConnCount;
    }
    
    public static String getServerAddress() {
        return "http://" + instance.getIPAddress() + ":" + instance.applicationSettings.getSeverPort();
    }

    public static boolean isWiFIConnected() {
        final WifiManager wifi = (WifiManager) instance.getSystemService(Context.WIFI_SERVICE);
        return wifi.getConnectionInfo().getNetworkId() != -1;
    }

    // Private methods
    private String getIPAddress() {
        final int ipInt = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress();
        return String.format(Locale.US, "%d.%d.%d.%d", (ipInt & 0xff), (ipInt >> 8 & 0xff), (ipInt >> 16 & 0xff), (ipInt >> 24 & 0xff));
    }
}
