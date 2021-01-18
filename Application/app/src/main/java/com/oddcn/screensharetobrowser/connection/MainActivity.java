package com.oddcn.screensharetobrowser.connection;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import com.oddcn.screensharetobrowser.R;

public class MainActivity extends Activity
{
    TextView textView1;
    WifiApManager wifiApManager;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        textView1 = (TextView) findViewById(R.id.textView1);

        wifiApManager = new WifiApManager(this);
		if (wifiApManager == null)
		{
			// force to show the settings page for demonstration purpose of this method
			wifiApManager.showWritePermissionSettings(true);
		}
        scan();
    }

    @Override
    protected void onResume()
	{
        super.onResume();

        wifiApManager.showWritePermissionSettings(false);
    }

    private void scan()
	{
        wifiApManager.getClientList(false, new FinishScanListener() {

				@Override
				public void onFinishScan(final ArrayList<ClientScanResult> clients)
				{

					textView1.setText("WifiApState: " + wifiApManager.getWifiApState() + "\n\n");
					textView1.append("Clients: \n");
					for (ClientScanResult clientScanResult : clients)
					{
						textView1.append("####################\n");
						textView1.append("IpAddr: " + clientScanResult.getIpAddr() + "\n");
						textView1.append("Device: " + clientScanResult.getDevice() + "\n");
						textView1.append("HWAddr: " + clientScanResult.getHWAddr() + "\n");
						textView1.append("isReachable: " + clientScanResult.isReachable() + "\n");
					}
				}
			});
    }

    public boolean onCreateOptionsMenu(Menu menu)
	{
        menu.add(0, 0, 0, "Get Clients");
        menu.add(0, 1, 0, "Open AP");
        menu.add(0, 2, 0, "Close AP");
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
        switch (item.getItemId())
		{
            case 0:
                scan();
                break;
            case 1:
                wifiApManager.setWifiApEnabled(null, true);
                break;
            case 2:
                wifiApManager.setWifiApEnabled(null, false);
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }
}
