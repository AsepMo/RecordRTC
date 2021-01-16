package com.oddcn.screensharetobrowser;

import android.app.Application;

public class ApplicationContext extends Application
{
	public static ApplicationContext isApplication;

	@Override
	public void onCreate()
	{
		// TODO: Implement this method
		super.onCreate();
		isApplication = this;
	}
	
	public static synchronized ApplicationContext getApplication()
	{
		return isApplication;
	}
}
