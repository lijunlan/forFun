package com.sdll18.server.network.handler.util;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.sdll18.server.network.data.SuperMap;

public abstract class MsgHandler {

	private IoSession mSession;
	private SuperMap mData;
	private Map<String, String> mMessage;
	
	public MsgHandler(){
		
	}
	
	public MsgHandler(IoSession session,Map<String, String> message){
		mSession = session;
		mMessage = message;
		mData = new SuperMap();
	}
	
	protected Map<String, String> getMessage(){
		return mMessage;
	}
	
	public SuperMap getMap(){
		return mData;
	}
	
	protected IoSession getSession(){
		return mSession;
	}
	
	protected abstract void setUpMap();
	
	public abstract void doit();
	
	protected void sendMsg(){
		mSession.write(mData.finishByJson());
	}
}
