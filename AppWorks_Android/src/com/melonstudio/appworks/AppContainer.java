package com.melonstudio.appworks;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.melonstudio.appworks.fragments.WebviewFragment;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.model.AbstractContent;
import com.melonstudio.model.User;
import com.melonstudio.serverprotocal.DownLoadProtocol;
import com.melonstudio.serverprotocal.UpLoadProtocol;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

/**
 * 
 * @author Wo
 * 
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class AppContainer {

	private final static String TAG = "com.melonstudio.appworks.AppContainer";

	private static Context context;
	private HashMap<String, Object> map;
	private ConfigureManager configureManager;
	private NotificationManager notificationManager;

	private volatile static AppContainer appContainer;

	private int notification_count = 0;

	public static void setContext(Context context) {
		Log.w(TAG, "Set Context!");
		AppContainer.context = context;
	}

	public void request(JSONObject jsonObject) {
		try {
			log("Request Arrived!");
			log("method: " + jsonObject.getString("method"));
			ContentParameter parameter = ContentParameter
					.createFromJSONObject(jsonObject);
			if (map.get(configureManager.getNetworkService()) != null) {
				Object object = map.get(configureManager.getNetworkService());
				Method method = object.getClass().getDeclaredMethod(
						jsonObject.getString("method"),
						new Class[] { ContentParameter.class });
				method.invoke(object, parameter);
			} else {
				log("NetworkService isn't running now!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * request for Comment list or Passage list
	 * 
	 * @param jsonObject
	 * @return null or object
	 */
	public Object blockingRequest(JSONObject jsonObject) {
		try {
			log("Blocking Request Arrived!");
			log("method: " + jsonObject.getString("method"));
			ContentParameter parameter = ContentParameter
					.createFromJSONObject(jsonObject);
			if (map.get(configureManager.getNetworkService()) != null) {
				Object object = map.get(configureManager.getNetworkService());
				Method method = object.getClass().getDeclaredMethod(
						jsonObject.getString("method"),
						new Class[] { ContentParameter.class });
				Object result = method.invoke(object, parameter);
				return result;
			} else {
				log("NetworkService isn't running now!");
			}
		} catch (Exception e) {
			log("Exception in Line 95: " + e.toString());
		}
		return null;
	}

	public void handle(JSONObject jsonObject) {
		log("Handle Event Arrived!");
		try {
			switch ((String) jsonObject.get("method")) {
			case DownLoadProtocol.METHOD_CHANGE_INFO:
				if (map.get(configureManager.getUserinfoUpdateAcitivity()) != null) {
					log("Received Message From ContentService!");
					IActivity activity = (IActivity) map.get(configureManager
							.getUserinfoUpdateAcitivity());
					activity.handleMessage(jsonObject);
					log("METHOD_CHANGE_INFO: PASS EVENT TO UserInfoUpdateActivity!");
				} else {
					log("METHOD_CHANGE_INFO: UserInfoUpdateActivity is Null, request is from iconUrl! "
							+ jsonObject.toString());
				}
				break;
			case DownLoadProtocol.METHOD_CHANGE_PASSWORD:
				if (map.get(configureManager.getResetPasswordActivity()) != null) {
					log("Received Message From ContentService!");
					IActivity activity = (IActivity) map.get(configureManager
							.getResetPasswordActivity());
					activity.handleMessage(jsonObject);
					log("METHOD_LOGIN: PASS EVENT TO ResetPasswordActivity!");
					configureManager.refreshUserInfo();
				} else {
					// TODO
				}
				break;
			case DownLoadProtocol.METHOD_DELETE_COMMENT:
				break;
			case DownLoadProtocol.METHOD_DELETE_MSG:
				log(jsonObject.toString());

				break;
			case DownLoadProtocol.METHOD_GET_USER_INFO:
				log(jsonObject.toString());
				startOtherUserInfoActivity(jsonObject);
				break;
			case UpLoadProtocol.METHOD_UPLOAD_MSG:
				log(jsonObject.toString());
				if (map.get(configureManager.getChatActivity()) != null) {
					IActivity activity = (IActivity) map.get(configureManager
							.getChatActivity());
					activity.handleMessage(jsonObject);
				} else {
					log("ChatActivity is null");
				}
				break;
			case DownLoadProtocol.METHOD_LOGIN:
				if (map.get(configureManager.getLoginActivity()) != null) {
					log("Received Message From ContentService!");
					IActivity activity = (IActivity) map.get(configureManager
							.getLoginActivity());
					activity.handleMessage(jsonObject);
					log("METHOD_LOGIN: PASS EVENT TO LoginActivity!");
				} else {
					log("Received Message From ContentSerivce! The Request isn't from LoginAcitivty!");
					log("Login Result: " + jsonObject.toString());
					try {
						if (jsonObject.getString("state").equals("error")) {
							Toast.makeText(context,
									jsonObject.getString("msg"),
									Toast.LENGTH_SHORT).show();
						} else {
							configureManager.updateLocalInfo(jsonObject);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				configureManager.refreshUserInfo();
				/**
				 * notify the activity to refresh the sliding menu
				 */
				if (map.get(configureManager.getContentMainActivity()) != null) {
					IActivity activity = (IActivity) map.get(configureManager
							.getContentMainActivity());
					activity.handleMessage(null);
				}
				break;
			case DownLoadProtocol.METHOD_LOGOUT:
				if (map.get(configureManager.getSlidingmenuFragment()) != null) {
					log("METHOD_LOGOUT: SUCCEWSS!");
					IActivity activity = (IActivity) map.get(configureManager
							.getSlidingmenuFragment());
					activity.handleMessage(jsonObject);
				} else {
					log("METHOD_LOGOUT: FAILED!");
				}
				break;
			case UpLoadProtocol.METHOD_UPLOAD_COMMENT:
				// TODO process the returning message
				log(jsonObject.toString());
				break;
			case DownLoadProtocol.METHOD_REGISTER:
				if (map.get(configureManager.getRegistActivity()) != null) {
					log("METHOD_REGISTER: SUCCESS!");
					IActivity activity = (IActivity) map.get(configureManager
							.getRegistActivity());
					activity.handleMessage(jsonObject);
					log("METHOD_REGISTER: PASS EVENT TO RegistActivity!");
				} else {
					Log.e(TAG, "METHOD_REGISTER: ERROR!!!");
				}
				break;
			case DownLoadProtocol.METHOD_CHECK_PRAISE:
				log(jsonObject.toString());
				try {
					jsonObject.put("method", "check_praise");
					if (configureManager.getContentPopAcitivity() != null
							&& map.get(configureManager
									.getContentPopAcitivity()) != null) {
						IActivity activity = (IActivity) map
								.get(configureManager.getContentPopAcitivity());
						activity.handleMessage(jsonObject);
					}
				} catch (Exception e) {
					log("Handle Check Praise Exception: " + e.toString());
				}
				break;
			case DownLoadProtocol.METHOD_PRAISE:
				log(jsonObject.toString());
				try {
					jsonObject.put("method", "praise");
					if (configureManager.getContentPopAcitivity() != null
							&& map.get(configureManager
									.getContentPopAcitivity()) != null) {
						IActivity activity = (IActivity) map
								.get(configureManager.getContentPopAcitivity());
						activity.handleMessage(jsonObject);
					}
				} catch (Exception e) {
					log("Handle Praise Exception: " + e.toString());
				}
				break;
			case DownLoadProtocol.METHOD_CANCEL_PRAISE:
				log(jsonObject.toString());
				try {
					jsonObject.put("method", "cancel_praise");
					if (configureManager.getContentMainActivity() != null
							&& map.get(configureManager
									.getContentPopAcitivity()) != null) {
						IActivity activity = (IActivity) map
								.get(configureManager.getContentPopAcitivity());
						activity.handleMessage(jsonObject);
					}
				} catch (Exception e) {
					log("Handle Cancel Praise Exception: " + e.toString());
				}
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void handle(String method, JSONArray jsonArray) {
		if (DownLoadProtocol.METHOD_GET_COMMENT_LIST.equals(method)) {
			/*
			 * to wait for the CommentActivity to be ready
			 */
			while (true) {
				if (map.get(configureManager.getCommentAcitivity()) == null) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					break;
				}
			}
			IActivity activity = (IActivity) map.get(configureManager
					.getCommentAcitivity());
			activity.handleMessage(jsonArray);
		} else {
			// TODO
			log("No Routines: handle JSONArray!");
		}
	}

	private AppContainer() {
		map = new HashMap<String, Object>();
		configureManager = ConfigureManager.getInstance(context);
		notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		log("Initialized the Single Instance");
	}

	/**
	 * the method for other class to call, to start a Activity.
	 * 
	 * @param jsonObject
	 *            "name" is the Activity Full Path name. you can get it from
	 *            ConfigureManager "bundle" is the Bundle object. if you want to
	 *            pass data to the new Activity, you can set the value in the
	 *            Bundle object.
	 */
	public void startActivity(JSONObject jsonObject) {
		try {
			Class<?> clazz = Class.forName(jsonObject.getString("name"));
			Intent intent = new Intent(context, clazz);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (jsonObject.has("bundle")) {
				Bundle bundle = (Bundle) jsonObject.get("bundle");
				intent.putExtra("bundle", bundle);
			}
			context.startActivity(intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void startActivity(JSONObject jsonObject, Activity fromActivity) {
		try {
			Class<?> clazz = Class.forName(jsonObject.getString("name"));
			Intent intent = new Intent(fromActivity, clazz);
			if (jsonObject.has("bundle")) {
				Bundle bundle = (Bundle) jsonObject.get("bundle");
				intent.putExtra("bundle", bundle);
			}
			fromActivity.startActivity(intent);
		} catch (Exception e) {
			log("Exception in StartActivity: " + e.toString());
		}
	}

	public void registService(Object object) {
		map.put(object.getClass().getName(), object);
		System.out.println(object.getClass().getName());
	}

	public void unregistService(Object object) {
		map.remove(object.getClass().getName());
	}

	/**
	 * 
	 * @param title
	 *            这条通知 标题
	 * @param content
	 *            这条通知的内容
	 * @param hints
	 *            通知到达时，任务栏显示的文字
	 * @param activityName
	 *            要使用PendingIntent启动的Activity的全路径名。通过ConfigureManager获取
	 * @param bundle
	 *            要通过PendingIntent传递的数据。可以是Null
	 */
	public void notificationRequest(String title, String content, String hints,
			String activityName, Bundle bundle) {
		log("Receive Notification Request");
		if (!configureManager.isReceive_notify()) {
			log("Settings: don't receive notification!");
			return;
		}
		Notification.Builder builder = new Notification.Builder(context)
				.setContentTitle(title).setContentText(content)
				.setTicker(hints).setSmallIcon(R.drawable.ic_launcher);
		Notification notification = null;
		if (activityName == null) {
			notification = builder.build();
		} else {
			try {
				Class<?> clazz = Class.forName(activityName);
				Intent intent = new Intent(context, clazz);
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, intent, 0);
				if (bundle != null) {
					intent.putExtra("bundle", bundle);
				}
				builder.setContentIntent(pendingIntent);
				notification = builder.build();
			} catch (Exception e) {
				log("Exception in notification request of pendingIntent: "
						+ e.toString());
			}
		}
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(notification_count, notification);
		++notification_count;

	}

	public static AppContainer getInstance() {
		if (appContainer == null) {
			synchronized (AppContainer.class) {
				if (appContainer == null) {
					appContainer = new AppContainer();
				}
			}
		}
		return appContainer;
	}

	public void startOtherUserInfoActivity(JSONObject j) {
		log("begin to start OtherUserInfoActivity");
		User user = new User(j);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("name", configureManager.getOtherUserInfoActivity());
			Bundle bundle = new Bundle();
			bundle.putSerializable("user", user);
			jsonObject.put("bundle", bundle);
			startActivity(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	public void app_start_process(JSONObject jsonObject) {
		IActivity activity = (IActivity) map.get(configureManager
				.getWelcomeActivity());
		activity.handleMessage(jsonObject);
	}

	public void refreshSlidingMenu() {
		IActivity activity = (IActivity) map.get(configureManager
				.getContentMainActivity());
		activity.handleMessage(null);
	}

	public boolean responseUrl(AbstractContent abstractContent) {
		WebviewFragment webviewFragment = (WebviewFragment) map
				.get(WebviewFragment.class.getName());
		if (webviewFragment == null) {
			return false;
		} else {
			webviewFragment.loadUrl(abstractContent);
			return true;
		}
	}

}
