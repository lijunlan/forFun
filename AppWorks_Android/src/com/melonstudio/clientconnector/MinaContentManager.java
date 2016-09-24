package com.melonstudio.clientconnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.json.JSONObject;

import com.melonstudio.appworks.AppContainer;
import com.melonstudio.util.ConfigureManager;
import com.melonstudio.util.MyLog;

import android.content.Context;

/**
 * 
 * Connect to the Mina Server Only use this to get the Notification. And the
 * message will be received in the messageReceived method of
 * ContentServerHandler.java
 * 
 * @author Wo
 * 
 */
public class MinaContentManager {
	private static final String TAG = "com.melonstudio.clientconnector.MinaContentManager";

	private void log(String msg) {
		MyLog.log(TAG, msg);
	}

	private NioSocketConnector connector;
	private Context context;
	private ConnectFuture connectFuture;

	private ConfigureManager configureManager;
	private AppContainer appContainer;

	public MinaContentManager(Context context) {
		this.context = context;

		configureManager = ConfigureManager.getInstance(this.context);
		AppContainer.setContext(this.context);
		appContainer = AppContainer.getInstance();

		connector = new NioSocketConnector();
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));
		connector.getFilterChain()
				.addLast("multi_thread", new ExecutorFilter());
		/**
		 * set the time out for connection
		 */
		connector.setConnectTimeoutMillis(30 * 1000);
		connector.setHandler(new ContentServerHandler(this));

	}

	/**
	 * connect to the server
	 */
	public void connect() {
		connectFuture = connector.connect(new InetSocketAddress(
				configureManager.getServerHost(), configureManager
						.getMina_serverPort()));
		connectFuture.awaitUninterruptibly();
		log("Mina Connected");
	}

	/**
	 * when the connection is established, this method will be called by
	 * ContentServerHandler.java
	 */
	public void connected() {

	}

	/**
	 * when the connection is lost, this method will be called by
	 * ContentServerHandler.java
	 */
	public void connectionLost() {

	}

	/**
	 * Send the message to the Mina Server
	 * 
	 * @param msg
	 */
	public void send(String msg) {
		connectFuture.getSession().write(msg);
	}

	/**
	 * do some post works
	 */
	public void closeConnection() {
		connectFuture.getSession().getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}

	/**
	 * this method was invoked from the ContentServerHandler
	 */
	public void handleMessage(JSONObject jsonObject) {
		try {
			String method = jsonObject.getString("method");
			switch (method) {
			case START:
				appStart(jsonObject);
				break;
			case HEART:
				break;
			case PASSAGE:
				String title = jsonObject.getString("title");
				String content = jsonObject.getString("content");
				appContainer.notificationRequest(title, content, "您有新的段子",
						configureManager.getContentMainActivity(), null);
				break;
			case MESSAGE:
				configureManager.setMessage_num(configureManager
						.getMessage_num() + 1);
				appContainer.refreshSlidingMenu();
				String from_user_name = jsonObject.getString("from_user_name");
				String text = jsonObject.getString("content");
				appContainer.notificationRequest("来自 " + from_user_name
						+ " 的私信", text, "有您的私信",
						configureManager.getChatListActivity(), null);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			log("Exception in handleMessage: " + e.toString());
		}
	}

	final static String START = "mina_start";
	final static String HEART = "mina_heart";
	final static String PASSAGE = "mina_passage";
	final static String MESSAGE = "mina_message";

	private void appStart(JSONObject jsonObject) {
		appContainer.app_start_process(jsonObject);
	}

	public String getUser_id() {
		return configureManager.getUser_id();
	}

	public boolean isConnected() {
		return connectFuture.isConnected();
	}
}
