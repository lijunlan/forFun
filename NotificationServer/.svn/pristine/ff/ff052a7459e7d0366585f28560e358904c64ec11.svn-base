package com.sdll18.notificationserver.network;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.sdll18.notificationserver.ui.C;

public class MyService {

	/**
	 * 类标识
	 */
	private final static String TAG = "MelonStudio.MyService";

	private static MyService singleInstance = null;

	public static synchronized MyService getService(int port) {
		if (singleInstance == null) {
			singleInstance = new MyService(port);
			return singleInstance;
		} else {
			if (singleInstance.getPort() == port)
				return singleInstance;
			else
				return null;
		}
	}

	/**
	 * 服务端端口号
	 */
	private int serverPort = 0;

	private MyNetWorkHandlerAdapter mHandlerAdapter;

	private IoAcceptor mAcceptor;

	private MyService(int port) {
		this();
		serverPort = port;
	}

	private MyService() {
		mHandlerAdapter = new MyNetWorkHandlerAdapter();
	}

	public int getPort() {
		return serverPort;
	}

	public void create() {
		C.i(TAG, "正在初始化服务");
		mAcceptor = new NioSocketAcceptor();
		TextLineCodecFactory lineCodec = new TextLineCodecFactory(
				Charset.forName("utf-8"));
		lineCodec.setDecoderMaxLineLength(1024 * 1024);
		lineCodec.setEncoderMaxLineLength(1024 * 1024);
		mAcceptor.getFilterChain().addLast("logger", new LoggingFilter());
		mAcceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(lineCodec));

		mAcceptor.setHandler(mHandlerAdapter);
		mAcceptor.getSessionConfig().setReadBufferSize(2048);
		mAcceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
	}

	public void start() throws IOException {
		C.i(TAG, "正在启动网络服务");
		mAcceptor.bind(new InetSocketAddress(serverPort));
		C.i(TAG, "服务器启动成功,监听Port:" + serverPort);
	}

	public boolean stop() {
		mAcceptor.dispose();
		return mAcceptor.isDisposed();
	}

	public void clean() {
		if (mAcceptor.isDisposed())
			singleInstance = null;
	}

	public void showIpInfo() {
		try {
			InetAddress[] addrs = InetAddress.getAllByName(InetAddress
					.getLocalHost().getHostName());
			for (InetAddress addr : addrs) {
				if (addr instanceof Inet4Address) {
					String ip = addr.getHostAddress().toString();
					String address = addr.getHostName().toString();
					C.i("网络信息", "IP地址:" + ip + ",主机名:" + address);
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
