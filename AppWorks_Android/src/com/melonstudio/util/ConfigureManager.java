package com.melonstudio.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.melonstudio.appworks.R;

public class ConfigureManager {

	private final static String TAG = "com.melonstudio.util.ConfigureManager";

	private volatile static ConfigureManager configureManager;
	/**
	 * the ads url of the welcome activity
	 */
	private String adsImgUrl;
	/**
	 * the User's welcome image url for welcome activity
	 */
	private String userImgUrl;
	private static Context context;
	/**
	 * server address
	 */
	private String serverHost;
	/**
	 * server port
	 */
	private int serverPort;
	private int mina_serverPort;
	/**
	 * the name of this app. using this to combine the http request's url.
	 */
	private String app_name;
	public static String SHARED_PREFERENCE_NAME = "U-Media";
	private String downloadServletUrl;
	private String uploadServletUrl;
	private String uploadImageUrl;

	private String registActivity;
	private String networkService;
	private String contentMainActivity;
	private String loginActivity;
	private String resetPasswordActivity;
	private String commentAcitivity;
	private String userinfoActivity;
	private String userinfoUpdateAcitivity;
	private String otherUserInfoActivity;
	private String contentPopAcitivity;
	private String chatActivity;
	private String chatListActivity;
	private String aboutActivity;
	private String welcomeActivity;
	private String settingActivity;
	private String collectionActivity;

	private String slidingmenuFragment;

	private String user_id;
	private String iconUrl;
	private String username;
	private String name;
	private String age;
	private String introduce;
	private String email;
	private String phone;
	private String sex;
	private String password;

	/**
	 * private message num
	 */
	private int message_num;

	/**
	 * settings
	 */

	private boolean receive_notify;
	private boolean show_phone;
	private boolean show_email;

	private boolean login_state;

	private String default_image = "http://avatar.csdn.net/C/6/8/1_bz419927089.jpg";

	private ConfigureManager() {
		adsImgUrl = context.getString(R.string.adsImgUrl);
		userImgUrl = context.getString(R.string.userImgUrl);
		/**
		 * server configuration
		 */
		serverHost = context.getString(R.string.server_host);
		serverPort = Integer.parseInt(context.getString(R.string.server_port));
		mina_serverPort = Integer.parseInt(context
				.getString(R.string.mina_server_port));

		app_name = context.getString(R.string.ServerAppName);

		registActivity = context.getString(R.string.regist_activity);
		loginActivity = context.getString(R.string.login_activity);
		contentMainActivity = context.getString(R.string.content_activity);
		resetPasswordActivity = context
				.getString(R.string.reset_password_activity);
		commentAcitivity = context.getString(R.string.comment_activity);
		userinfoActivity = context.getString(R.string.userinfo_activity);
		otherUserInfoActivity = context
				.getString(R.string.other_userinfo_activity);
		networkService = context.getString(R.string.network_service);
		userinfoUpdateAcitivity = context
				.getString(R.string.userinfo_update_activity);
		chatActivity = context.getString(R.string.chat_activity);
		chatListActivity = context.getString(R.string.chat_list_activity);
		aboutActivity = context.getString(R.string.about_activity);
		welcomeActivity = context.getString(R.string.welcome_activity);
		settingActivity = context.getString(R.string.setting_activity);
		collectionActivity = context.getString(R.string.collection_activity);
		slidingmenuFragment = context.getString(R.string.sliding_menu_fragment);
		/*
		 * combine the servlet address for the client
		 */
		downloadServletUrl = "http://" + serverHost + ":" + serverPort + "/"
				+ app_name + "/" + ContentUtil.DOWNLOAD_SERVLET;
		uploadServletUrl = "http://" + serverHost + ":" + serverPort + "/"
				+ app_name + "/" + ContentUtil.UPLOAD_SERVLET;
		uploadImageUrl = "http://" + serverHost + ":" + serverPort + "/"
				+ app_name + "/" + ContentUtil.UPLOAD_IMAGE;

		refreshUserInfo();
	}

	public static ConfigureManager getInstance(Context context) {
		if (configureManager == null) {
			synchronized (ConfigureManager.class) {
				if (configureManager == null) {
					ConfigureManager.context = context;
					configureManager = new ConfigureManager();
				}
			}
		}
		return configureManager;
	}

	public String getAdsImgUrl() {
		return adsImgUrl;
	}

	public String getUserImgUrl() {
		return userImgUrl;
	}

	public String getServerHost() {
		return serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	/**
	 * get the down servlet address for the request
	 * 
	 * @return the entire path of the down servlet. for example:
	 *         http://www.melonstudio.com:80/Test/down
	 */
	public String getDownloadServletUrl() {
		return downloadServletUrl;
	}

	/**
	 * get the up servlet address for the request
	 * 
	 * @return the entire path of the up servlet. for example:
	 *         http://www.melonstudio.com:80/Test/
	 */
	public String getUploadServletUrl() {
		return uploadServletUrl;
	}

	public String getRegistActivity() {
		return registActivity;
	}

	public void setRegistActivity(String registActivity) {
		this.registActivity = registActivity;
	}

	public String getNetworkService() {
		return networkService;
	}

	public void setNetworkService(String networkService) {
		this.networkService = networkService;
	}

	public String getContentMainActivity() {
		return contentMainActivity;
	}

	public void setContentMainActivity(String contentMainActivity) {
		this.contentMainActivity = contentMainActivity;
	}

	public String getLoginActivity() {
		return loginActivity;
	}

	public void setLoginActivity(String loginActivity) {
		this.loginActivity = loginActivity;
	}

	public String getResetPasswordActivity() {
		return resetPasswordActivity;
	}

	public void setResetPasswordActivity(String resetPasswordActivity) {
		this.resetPasswordActivity = resetPasswordActivity;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCommentAcitivity() {
		return commentAcitivity;
	}

	public void setCommentAcitivity(String commentAcitivity) {
		this.commentAcitivity = commentAcitivity;
	}

	public String getUserinfoActivity() {
		return userinfoActivity;
	}

	public void setUserinfoActivity(String userinfoActivity) {
		this.userinfoActivity = userinfoActivity;
	}

	public String getUserinfoUpdateAcitivity() {
		return userinfoUpdateAcitivity;
	}

	public void setUserinfoUpdateAcitivity(String userinfoUpdateAcitivity) {
		this.userinfoUpdateAcitivity = userinfoUpdateAcitivity;
	}

	public void refreshUserInfo() {
		try {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
			user_id = sharedPreferences.getString("user_id", "");
			iconUrl = sharedPreferences.getString("iconUrl", "");
			username = sharedPreferences.getString("username", "");
			name = sharedPreferences.getString("name", "");
			age = sharedPreferences.getString("age", "");
			introduce = sharedPreferences.getString("introduce", "");
			email = sharedPreferences.getString("email", "");
			phone = sharedPreferences.getString("phone", "");
			sex = sharedPreferences.getString("sex", "");
			password = sharedPreferences.getString("password", "");
			login_state = sharedPreferences.getBoolean("login", false);

			message_num = sharedPreferences.getInt("message_num", 0);

			/**
			 * settings
			 */
			receive_notify = sharedPreferences.getBoolean("receive_notify",
					true);
			show_email = sharedPreferences.getBoolean("show_phone", false);
			show_phone = sharedPreferences.getBoolean("show_phone", false);
			log("REFRESH USER INFO SUCCESS!");
		} catch (Exception e) {
			log("REFRESH USER INFO FAILED! " + e.toString());
		}

	}

	public void updateLocalInfo(JSONObject jsonObject) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigureManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		log(jsonObject.toString());
		try {
			editor.putString("username", jsonObject.getString("username")
					.trim());
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			editor.putString("phone", jsonObject.getString("phone").trim());
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			editor.putString("sex", jsonObject.getString("sex").trim());
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			editor.putString("email", jsonObject.getString("email").trim());
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			editor.putString("iconUrl", jsonObject.getString("iconUrl").trim());
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			editor.putString("name", jsonObject.getString("name").trim());
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			editor.putString("age", jsonObject.getString("age").trim());
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			editor.putString("introduce", jsonObject.getString("introduce")
					.trim());
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			editor.putString("user_id", jsonObject.getString("user_id").trim());
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			editor.putBoolean("show_email", Boolean.parseBoolean(jsonObject
					.getString("showEmail").trim()));
		} catch (JSONException e) {
			log(e.toString());
		}
		try {
			editor.putBoolean("show_phone", Boolean.parseBoolean(jsonObject
					.getString("showPhone").trim()));
		} catch (JSONException e) {
			log(e.toString());
		}
		/**
		 * to persist the login state
		 */
		editor.putBoolean("login", true);
		editor.commit();

		refreshUserInfo();
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigureManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putString("iconUrl", this.iconUrl);
		editor.commit();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtherUserInfoActivity() {
		return otherUserInfoActivity;
	}

	public void setOtherUserInfoActivity(String otherUserInfoActivity) {
		this.otherUserInfoActivity = otherUserInfoActivity;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	public String getContentPopAcitivity() {
		return contentPopAcitivity;
	}

	public void setContentPopAcitivity(String contentPopAcitivity) {
		this.contentPopAcitivity = contentPopAcitivity;
	}

	public String getDefault_image() {
		return default_image;
	}

	public boolean isLogin_state() {
		return login_state;
	}

	public void setLogin_state(boolean login_state) {
		this.login_state = login_state;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigureManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("login", this.login_state);
		editor.commit();
	}

	public String getSlidingmenuFragment() {
		return slidingmenuFragment;
	}

	public void setSlidingmenuFragment(String slidingmenuFragment) {
		this.slidingmenuFragment = slidingmenuFragment;
	}

	public String check_user_img(String img) {
		if (img == null || "".equals(img.trim()) || "null".equals(img.trim())) {
			img = getDefault_image();
		}
		return img;
	}

	public String getChatActivity() {
		return chatActivity;
	}

	public void setChatActivity(String chatActivity) {
		this.chatActivity = chatActivity;
	}

	public String getUploadImageUrl() {
		return uploadImageUrl;
	}

	public String getChatListActivity() {
		return chatListActivity;
	}

	public void setChatListActivity(String chatListActivity) {
		this.chatListActivity = chatListActivity;
	}

	public int getMessage_num() {
		return message_num;
	}

	public void setMessage_num(int message_num) {
		this.message_num = message_num;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigureManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt("message_num", this.message_num);
		editor.commit();
	}

	public String getAboutActivity() {
		return aboutActivity;
	}

	public void setAboutActivity(String aboutActivity) {
		this.aboutActivity = aboutActivity;
	}

	public int getMina_serverPort() {
		return mina_serverPort;
	}

	public void setMina_serverPort(int mina_serverPort) {
		this.mina_serverPort = mina_serverPort;
	}

	public String getWelcomeActivity() {
		return welcomeActivity;
	}

	public void setWelcomeActivity(String welcomeActivity) {
		this.welcomeActivity = welcomeActivity;
	}

	public String getSettingActivity() {
		return settingActivity;
	}

	public void setSettingActivity(String settingActivity) {
		this.settingActivity = settingActivity;
	}

	public boolean isReceive_notify() {
		return receive_notify;
	}

	public void setReceive_notify(boolean receive_notify) {
		this.receive_notify = receive_notify;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigureManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("receive_notify", this.receive_notify);
		editor.commit();

	}

	public boolean isShow_phone() {
		return show_phone;
	}

	public void setShow_phone(boolean show_phone) {
		this.show_phone = show_phone;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigureManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("show_phone", this.show_phone);
		editor.commit();
	}

	public boolean isShow_email() {
		return show_email;
	}

	public void setShow_email(boolean show_email) {
		this.show_email = show_email;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigureManager.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean("show_email", this.show_email);
		editor.commit();
	}

	public String getCollectionActivity() {
		return collectionActivity;
	}

	public void setCollectionActivity(String collectionActivity) {
		this.collectionActivity = collectionActivity;
	}

}
