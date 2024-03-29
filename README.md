# react-native-android-notification

<p style = "text-align: justify">A simple react-native module which is used to schedule and manage local notifications on android phones. It can also be used as an alarm. Events will be fired whether the implementing app is in the foreground or in the background.

# Getting Started

### Installation

Using npm

```	$ npm install react-native-android-notification --save```

Using yarn

```	$ yarn add react-native-android-notification```

### Linking
There are two options for linking:

##### 1. Automatic

```	react-native link react-native-android-notification```

##### 2. Manual

If the automatic linking fails for some reason, you can do the linking manually as follows:
 * add the following to <code>yourAppName/android/settings.gradle</code> file:
 
 ```
 	include ':react-native-android-notification'
 	project(':react-native-android-nofication').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-android-notification/android')
 ```

 * add the following inside the dependencies closure of  <code>yourAppName/android/app/build.gradle</code> file:
 ```
 	implementation project(':react-native-android-notification')
```

* add the following to your <code>MainApplication.java</code> file:
 ```
 	import com.sysapps.notification.NotifPackage;
 ```
 and also,
 ```
	@Override
	protected List<ReactPackage> getPackages() {
		return Arrays.<ReactPackage>asList(
			new MainReactPackage(),
			....
			new NotifPackage()                    <== Add this
		);
	}
 ```


### Usage
To schedule and manage notifications, first import the module to your app like so:

```   import { Notification } from 'react-native-android-notification';```

After this, you can call any of the functions stated below to schedule an alarm.

## Functions
<p style = "text-align: justify">The alarm module contains the following functions.</p>

``` 
    schedule(Object options)
    update(Object options)
    refer(String id)
    cancel(String id)
    cancelAll()    
```

## Events
<p style = "text-align: justify">An event is emitted when the notification is firing whether the app in the foreground or background. If you want to handle the event while the app is in the foreground, you have to add <code>onNotification</code> listener in your app. One way to do this is as follows: </p>

``` 
	import { DeviceEventEmitter } from "react-native";

	componentDidMount() {
		DeviceEventEmitter.addListener('onNotification', (e) => {
			const response = JSON.parse(e);
			console.log(response);
		});
  	}
```

If you want to handle the event while the app is in the background, you have to add the following at the bottom of your javascript file. You should also register the <code>EventEmitter</code> service in your AndroidManifest.xml file as described in the <a href="#eventEmitter">Permissions</a> section.</p>

``` 
    import { AppRegistry } from "react-native";

    AppRegistry.registerHeadlessTask('SysappsEventNotification', () => async (e) => {
        console.log(taskData);
    });
```

The response data sent via the event emitter is the string representation of the original object parameter passed to the <code>schedule()</code> function. Note that, only primitive data will be persisted and sent back as a response. 
 
### Permissions
<p style = "text-align: justify">In order to schedule and manage notifications, the following service should be added inside <code>application</code> tag of your AndroidManifest.xml file.</p>

```
	<service android:name="com.sysapps.notification.alarm.AlarmService"
    		android:permission="android.permission.BIND_JOB_SERVICE" />
```

<p id = "eventEmitter">To handle events when the app in the background, the following service should be registered in the AndroidManifest.xml file:</p>

 ```
    <service android:name="com.sysapps.notification.alarm.EventEmitter"/>
```

<p style = "text-align: justify">Besides, add the following permissions outside the <code>application</code> tag of the AndroidManifest.xml file.</p>

```
	<uses-permission android:name="android.permission.WAKE_LOCK"/>
	<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
	<uses-permission android:name="android.permission.VIBRATE"/>
```

### Description
<p style = "text-align: justify">The above functions are used to perform the following activities.</p>

#### schedule(Object options): 

<p style = "text-align: justify">is used to schedule an alarm notification. The <code>options</code> parameter can have, but not limited to, the properties shown below. </p>

<table>
<tr><th>Prop</th><th>Required</th><th>Default</th><th style =  "width: 150px">Type</th><th>Description</th></tr>
<tr><td>channelId</td><td>true</td><td>-</td><td>String</td><td style = "text-align: justify">A unique string identifier for the notification and for the channel throught which the notification will be streamed.</td></tr>
<tr><td>date</td><td>true</td><td>-</td><td>long</td><td style = "text-align: justify">The number of milli seconds between the date and time when the notification is desired to be posted and January 1, 1970 00:00:00. The value can easily be obtained by calling <code>getTime()</code> function on a javascript date object. </td></tr>
<tr><td>title</td><td> false</td><td> -</td><td>String</td><td style = "text-align: justify">The title of the notification.</td></tr>
<tr><td>content</td><td> false</td><td>-</td><td>String</td><td style = "text-align: justify">The content of the notification.</td></tr>
</table>

You can also add any other key-value pairs in addition to the above so that you can recover them when the event is fired at the time of notification. These key-value pairs should be of primitive data types.

##### Sample code snippet

```
       import { Notification } from 'react-native-android-notification';
       ....
       ....
       _scheduleNotification = () => {
            const date = new Date(2019, 11, 1, 8, 30, 0); // Dec 01 2019 @ 8:30:00 AM        	 
        	const params = { 
                	"channelId": "abc123", 
                	"date": date.getTime(), 
                	"title": "my title", 
                	"content" : "my content", 
                	"key1": "value1", 
                	"key2": false, 
               		"key3": 14123 
        	};
        	Notification.schedule(params);
        } 
 ```

<p style = "text-align: justify">Invoking the <code>_scheduleNotification()</code> function schedules an alarm to be fired on Dec 01 2019 @ 8:30:00 AM local time. When the notification is fired an event will also be emitted and the string representation of the <code>params</code> field defined within the function will be sent back as a response.</code></p>

#### update(Object options): 

<p style = "text-align: justify">is used to update a scheduled notification. All of the parameters passed to <code>schedule()</code> function except the <code>channelId</code> property can be updated. Thus, the <code>options</code> parameter passed to the <code>update()</code> function should contain the <code>channelId</code> property of an already scheduled notification and any other properties either to be modified or added.</p>

##### Sample code snippet

```
        import { Notification } from 'react-native-android-notification';
       	....
       	....
	_updateNotif_abc123 = () => {
            const date = new Date(2019, 11, 2, 8, 30, 0); // Dec 02 2019 @ 8:30:00 AM  
        	Notification.update({ "channelId": "abc123", "date": date.getTime() });        	
        } 
```
<p style = "text-align: justify">Invoking the <code>_updateNotif_abc123()</code> function updates a scheduled notification with <code>channelId</code> of <code>abc123</code> by changing the date when the notification will be posted to Dec 02 2019 @ 8:30:00 AM local time.</p>

#### refer(String channelId): 

<p style = "text-align: justify">is used to refer a scheduled notification so as to have an overview of its properties. The parameter <code>channelId</code> represents the unique identifier associated with the scheduled notification. The response is the string representation of the object parameter passed to the <code>schedule()</code> or <code>update()</code> function. For unsuccessful requests a promise rejection will be sent.</p>

##### Sample code snippet

```
            import { Notification } from 'react-native-android-notification';
       		....
       		....
	    _referScheduledNotif = async () => {
                const params = await Notification.refer("abc123");
                console.log(params);
            } 
```
<p style = "text-align: justify">Invoking the <code>_referScheduledNotif()</code> function obtains the data associated with a scheduled alarm notification having a <code>channelId</code> of <code>abc123</code> and logs the response.</p>

#### cancel(String channelId): 

<p style = "text-align: justify">is used to cancel an alarm notification scheduled with a unique identifier of <code>channelId</code>. In addition, the data associated with the notification will be deleted.</p>

##### Sample code snippet

```
            import { Notification } from 'react-native-android-notification';
       		....
       		....
	    _cancelNotification = () => {
                Notification.cancel("abc123");
            } 
```
<p style = "text-align: justify">Invoking the <code>_cancelNotification()</code> cancels an alarm notification having a <code>channelId</code> of <code>abc123</code>.</p>

#### cancelAll(): 

<p style = "text-align: justify">is used to cancel all the notifications scheduled via the <code>schedule()</code> function. The data corresponding to all notifications will also be deleted.</p>

##### Sample code snippet

```
	    import { Notification } from 'react-native-android-notification';
       		....
       		....
            _cancelAllNotifications = () => {
                Notification.cancelAll();
            } 
```
<p style = "text-align: justify">Invoking the <code>_cancelAllNotifications()</code> function cancels all notifications scheduled via the <code>schedule()</code> function and deletes the corresponding data.</p>

## Issues or suggestions?
If you have any issues or if you want to suggest something , you can write it [here](https://github.com/Asaye/react-native-system-applications/issues).

## Additional info
This is a component of a more comprehensive [react-native-system-applications](https://www.npmjs.com/package/react-native-system-applications) module developed by the same author.
