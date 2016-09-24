package com.melonstudio.network;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.melonstudio.appworks.AppContainer;
import com.melonstudio.clientconnector.ContentParameter;
import com.melonstudio.clientconnector.HttpHelper;
import com.melonstudio.clientconnector.MinaContentManager;
import com.melonstudio.clientconnector.RequestManager;
import com.melonstudio.serverprotocal.DownLoadProtocol;
import com.melonstudio.serverprotocal.UpLoadProtocol;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author guohaosu
 * 
 */
public class ContentService extends Service {

	private static final String TAG = "com.melonstudio.network.ContentService";

	private MinaContentManager manager;
	private RequestManager requestManager;
	private HttpHelper httpHelper;

	private ConfigureManager configureManager;
	private AppContainer appContainer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.v("ContentService", "onCreate");
		super.onCreate();
		configureManager = ConfigureManager
				.getInstance(getApplicationContext());
		AppContainer.setContext(getApplicationContext());
		appContainer = AppContainer.getInstance();
		appContainer.registService(this);

		requestManager = new RequestManager();
		httpHelper = new HttpHelper();

		initMinaManager();
	}

	private void initMinaManager() {
		manager = new MinaContentManager(getApplicationContext());
		new Thread(new Runnable() {

			@Override
			public void run() {
				log("begin to connect to Mina Server");
				manager.connect();
			}
		}).start();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 
	 * @param parameter
	 */
	public void register(final ContentParameter parameter) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String requestJson = requestManager.register(
						parameter.getUsername(), parameter.getPassword(),
						parameter.getEmail(), parameter.getName());
				try {
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					if (responseJson != null) {
						/*
						 * get the response from the server
						 */
						JSONObject jsonObject = new JSONObject(responseJson);
						jsonObject.put("method",
								DownLoadProtocol.METHOD_REGISTER);
						appContainer.handle(jsonObject);
					} else {
						/*
						 * TODO error response from the server
						 */
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 
	 * @param parameter
	 */
	public void login(final ContentParameter parameter) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String requestJson = requestManager.login(
						parameter.getUsername(), parameter.getPassword());
				try {
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method", DownLoadProtocol.METHOD_LOGIN);
					appContainer.handle(jsonObject);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 
	 * @param parameter
	 */
	public void logout(final ContentParameter parameter) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String requestJson = requestManager.logout(parameter
						.getUser_id());

				try {
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method", DownLoadProtocol.METHOD_LOGOUT);
					appContainer.handle(jsonObject);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void get_user_info(final ContentParameter parameter) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String requestJson = requestManager.getUserInfo(parameter
						.getUser_id());
				try {
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method",
							DownLoadProtocol.METHOD_GET_USER_INFO);
					appContainer.handle(jsonObject);
				} catch (Exception e) {
					log("Exception in get user info: " + e.toString());
				}
			}
		}).start();
	}

	/**
	 * 
	 * @param parameter
	 */
	public void changePassword(final ContentParameter parameter) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String requestJson = requestManager.changePassword(
						parameter.getUser_id(), parameter.getUsername(),
						parameter.getOld_password(),
						parameter.getNew_password());
				try {
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method",
							DownLoadProtocol.METHOD_CHANGE_PASSWORD);
					appContainer.handle(jsonObject);
				} catch (Exception e) {
					log("Exception in Change password: " + e.toString());
				}
			}
		}).start();
	}

	/**
	 * 
	 * @param parameter
	 */
	public void changeInfo(final ContentParameter parameter) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String requestJson = requestManager.changeInfo(
						parameter.getUser_id(), parameter.getAge(),
						parameter.getName(), parameter.getIntroduce(),
						parameter.getIconUrl(), parameter.getEmail(),
						parameter.getPhone(), parameter.getSex(),
						parameter.getShowPhone(), parameter.getShowEmail());
				try {
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method",
							DownLoadProtocol.METHOD_CHANGE_INFO);
					appContainer.handle(jsonObject);
				} catch (Exception e) {
					log("Exception in change info: " + e.toString());
				}
			}
		}).start();
	}

	public JSONObject blocking_changeInfo(final ContentParameter parameter) {
		String requestJson = requestManager.changeInfo(parameter.getUser_id(),
				parameter.getAge(), parameter.getName(),
				parameter.getIntroduce(), parameter.getIconUrl(),
				parameter.getEmail(), parameter.getPhone(), parameter.getSex(),
				parameter.getShowPhone(), parameter.getShowEmail());
		try {
			String responseJson = httpHelper.send_request(requestJson,
					configureManager.getDownloadServletUrl());
			JSONObject jsonObject = new JSONObject(responseJson);
			return jsonObject;
		} catch (Exception e) {
			log("Exception in blocking change info: " + e.toString());
			return null;
		}
	}

	/**
	 * 
	 * @param parameter
	 */
	public void getCommentList(final ContentParameter parameter) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				String reqestJson = requestManager.getCommentList(
						parameter.getPage(), parameter.getId_type(),
						parameter.isFormulate_time(), parameter.getUser_id(),
						parameter.getPassage_id());
				try {
					String responseJson = httpHelper.send_request(reqestJson,
							configureManager.getDownloadServletUrl());
					JSONArray jsonObject = new JSONArray(responseJson);
					appContainer.handle(
							DownLoadProtocol.METHOD_GET_COMMENT_LIST,
							jsonObject);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public JSONArray blocking_getCommentList(final ContentParameter parameter) {
		String reqestJson = requestManager.getCommentList(parameter.getPage(),
				parameter.getId_type(), parameter.isFormulate_time(),
				parameter.getUser_id(), parameter.getPassage_id());
		try {
			String responseJson = httpHelper.send_request(reqestJson,
					configureManager.getDownloadServletUrl());
			JSONArray jsonArray = new JSONArray(responseJson);
			return jsonArray;
		} catch (Exception e) {
			log("Exception in blocking get comment list: " + e.toString());
			return null;
		}
	}

	/**
	 * 
	 * @param parameter
	 */
	public void deleteComment(final ContentParameter parameter) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String requestJson = requestManager.deleteComment(
						parameter.getComment_id(), parameter.getUser_id(),
						parameter.getPassage_id());
				try {
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method",
							DownLoadProtocol.METHOD_DELETE_COMMENT);
					appContainer.handle(jsonObject);
				} catch (Exception e) {
					log("Delete comment error: " + e.toString());
				}
			}
		}).start();
	}

	/**
	 * 
	 * @param parameter
	 */
	public void uploadComment(final ContentParameter parameter) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String requestJson = requestManager.uploadComment(
						parameter.getPassage_id(), parameter.getUser_id(),
						parameter.getContent());
				try {
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getUploadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method",
							UpLoadProtocol.METHOD_UPLOAD_COMMENT);
					appContainer.handle(jsonObject);
				} catch (Exception e) {
					log("Upload comment error: " + e.toString());
				}
			}
		}).start();
	}

	public JSONObject blocking_uploadComment(final ContentParameter parameter) {
		String requestJson = requestManager.uploadComment(
				parameter.getPassage_id(), parameter.getUser_id(),
				parameter.getContent());
		try {
			String responseJson = httpHelper.send_request(requestJson,
					configureManager.getUploadServletUrl());
			JSONObject jsonObject = new JSONObject(responseJson);
			return jsonObject;
		} catch (Exception e) {
			log("Blocking Upload comment error: " + e.toString());
			return null;
		}

	}

	/**
	 * 
	 * @param parameter
	 */
	public Object getMessageList(final ContentParameter parameter) {

		String requestJson = requestManager.getMessageList(
				parameter.getRefresh_time(), parameter.isFormulate_time());
		try {
			String responseJson = httpHelper.send_request(requestJson,
					configureManager.getDownloadServletUrl());
			JSONArray jsonArray = new JSONArray(responseJson);
			return jsonArray;
		} catch (Exception e) {
			log("Get message list error: " + e.toString());
		}
		return null;
	}

	/**
	 * 
	 * @param parameter
	 */
	public void deleteMessage(final ContentParameter parameter) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String requestJson = requestManager.deleteMessage(parameter
						.getPassage_id());
				try {
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method",
							DownLoadProtocol.METHOD_DELETE_MESSAGE);
					appContainer.handle(jsonObject);
				} catch (Exception e) {
					log("Delete message error: " + e.toString());
				}
			}
		}).start();
	}

	public void checkPraise(final ContentParameter parameter) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String requestJson = requestManager.checkPraise(
						parameter.getUser_id(), parameter.getType(),
						parameter.getPassage_id(), parameter.getComment_id());
				try {
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method",
							DownLoadProtocol.METHOD_CHECK_PRAISE);
					appContainer.handle(jsonObject);
				} catch (Exception e) {
					log("Check praise error: " + e.toString());
				}
			}
		}).start();
	}

	public JSONObject blocking_checkPraise(final ContentParameter parameter) {

		String requestJson = requestManager.checkPraise(parameter.getUser_id(),
				parameter.getType(), parameter.getPassage_id(),
				parameter.getComment_id());
		try {
			String responseJson = httpHelper.send_request(requestJson,
					configureManager.getDownloadServletUrl());
			JSONObject jsonObject = new JSONObject(responseJson);
			return jsonObject;
		} catch (Exception e) {
			log("Check praise error in Line 420: " + e.toString());
			return null;
		}

	}

	public void praise(final ContentParameter parameter) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String requestJson = requestManager.praise(
						parameter.getUser_id(), parameter.getType(),
						parameter.getPassage_id(), parameter.getComment_id());
				try {
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method", DownLoadProtocol.METHOD_PRAISE);
					appContainer.handle(jsonObject);
				} catch (Exception e) {
					log("Praise error: " + e.toString());
				}
			}
		}).start();
	}

	public JSONObject blocking_parise(final ContentParameter parameter) {

		String requestJson = requestManager.praise(parameter.getUser_id(),
				parameter.getType(), parameter.getPassage_id(),
				parameter.getComment_id());
		try {
			String responseJson = httpHelper.send_request(requestJson,
					configureManager.getDownloadServletUrl());
			JSONObject jsonObject = new JSONObject(responseJson);
			return jsonObject;
		} catch (Exception e) {
			log("Praise error in Line 457: " + e.toString());
			return null;
		}

	}

	public void cancel_parise(final ContentParameter parameter) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String requestJson = requestManager.cancel_parise(
							parameter.getUser_id(), parameter.getType(),
							parameter.getPassage_id(),
							parameter.getComment_id());
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method",
							DownLoadProtocol.METHOD_CANCEL_PRAISE);
					appContainer.handle(jsonObject);

				} catch (Exception e) {
					log("Praise error: " + e.toString());
				}
			}
		}).start();
	}

	public Object get_msg_list(final ContentParameter parameter) {
		try {
			String requestJson = requestManager.get_msg_list(
					parameter.getOwn_user_id(), parameter.getOther_user_id(),
					parameter.getRefresh_time(), parameter.isFormulate_time());
			String responseJson = httpHelper.send_request(requestJson,
					configureManager.getDownloadServletUrl());
			JSONObject jsonObject = new JSONObject(responseJson);
			jsonObject.put("method", DownLoadProtocol.METHOD_GET_MSG_LIST);
			return jsonObject;
		} catch (Exception e) {
			log("Get MSG List Error: " + e.toString());
		}
		return null;
	}

	public void delete_msg(final ContentParameter parameter) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String requestJson = requestManager.delete_msg(
							parameter.getMessage_id(),
							parameter.getFrom_user_id(),
							parameter.getTo_user_id());
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getDownloadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject
							.put("method", DownLoadProtocol.METHOD_DELETE_MSG);
					appContainer.handle(jsonObject);
				} catch (Exception e) {
					log("Delete MSG Error: " + e.toString());
				}

			}
		}).start();
	}

	public void upload_msg(final ContentParameter parameter) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String requestJson = requestManager.upload_msg(
							parameter.getFrom_user_id(),
							parameter.getTo_user_id(), parameter.getContent());
					String responseJson = httpHelper.send_request(requestJson,
							configureManager.getUploadServletUrl());
					JSONObject jsonObject = new JSONObject(responseJson);
					jsonObject.put("method", UpLoadProtocol.METHOD_UPLOAD_MSG);
					appContainer.handle(jsonObject);
				} catch (Exception e) {
					log("Upload MSG Error: " + e.toString());
				}
			}
		}).start();
	}

	public JSONObject blocking_upload_msg(final ContentParameter parameter) {

		try {
			String requestJson = requestManager.upload_msg(
					parameter.getFrom_user_id(), parameter.getTo_user_id(),
					parameter.getContent());
			String responseJson = httpHelper.send_request(requestJson,
					configureManager.getUploadServletUrl());
			JSONObject jsonObject = new JSONObject(responseJson);
			return jsonObject;
		} catch (Exception e) {
			log("Upload MSG Error: " + e.toString());
			return null;
		}

	}

	/**
	 * Sync method. Blocking
	 * 
	 * @param parameter
	 */
	public JSONObject delete_many_msg(final ContentParameter parameter) {
		try {
			String requestJson = requestManager.delete_many_msg(
					parameter.getOwn_user_id(), parameter.getAnother_user_id());
			String responseJson = httpHelper.send_request(requestJson,
					configureManager.getDownloadServletUrl());
			JSONObject jsonObject = new JSONObject(responseJson);
			return jsonObject;
		} catch (Exception exception) {
			log("Exception in delete_many_msg: " + exception.toString());
			return null;
		}

	}

	/**
	 * Sync method. Blocking
	 * 
	 * @param parameter
	 * @return
	 */
	public JSONArray get_recent_list(final ContentParameter parameter) {
		try {
			String requestJson = requestManager.get_recent_list(
					parameter.getUser_id(), parameter.getPage(),
					parameter.isFormulate_time());
			String responseJson = httpHelper.send_request(requestJson,
					configureManager.getDownloadServletUrl());
			JSONArray jsonArray = new JSONArray(responseJson);
			return jsonArray;
		} catch (Exception e) {
			log("Exception in get_recent_list: " + e.toString());
			return null;
		}
	}

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	@Override
	public void onDestroy() {
		appContainer.unregistService(this);
		super.onDestroy();
	}

}
