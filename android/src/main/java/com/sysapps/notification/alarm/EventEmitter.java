package com.sysapps.notification.alarm;

import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;
import com.facebook.react.bridge.Arguments;

import android.content.Intent;
import android.app.job.JobParameters;
import android.os.PersistableBundle;
import android.os.Bundle;

import javax.annotation.Nullable;

import java.util.Map;
import java.util.HashMap;

import android.util.Log;

public class EventEmitter extends HeadlessJsTaskService {

	@Override
	protected @Nullable
	HeadlessJsTaskConfig getTaskConfig(Intent intent) {	
	Log.i("RNSystem", "in get Task config");	
		Bundle bundle = intent.getExtras(); 

		return new HeadlessJsTaskConfig(
	        "SysappsEventNotification",
	        Arguments.makeNativeMap(bundle),
	        60000,
	        true
	    );    
	}
}
