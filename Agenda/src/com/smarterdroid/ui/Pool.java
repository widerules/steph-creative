package com.smarterdroid.ui;

import com.smarterdroid.dataaccess.EventAdapter;
import com.smarterdroid.dataaccess.PlaceAdapter;
import com.smarterdroid.dataaccess.TaskAdapter;
import com.smarterdroid.object.ServiceConfig;
import com.smarterdroid.service.MyService;

import android.content.Context;

/** A place where all common objects between activities are accessible */
public class Pool {

	static Context context;
	public static EventAdapter eventAdapter;
	public static PlaceAdapter placeAdapter;
	public static TaskAdapter taskAdapter;
	static MyService service;
	static ServiceConfig config;

}
