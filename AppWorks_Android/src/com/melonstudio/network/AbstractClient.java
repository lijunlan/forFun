package com.melonstudio.network;

/**
 * Abstract class for communicating with the server
 * @author Wo
 *
 */
public abstract class AbstractClient {
	public abstract int connectToServer(ClientParameter clientParameter);
	public abstract int registUser(ClientParameter clientParameter);
}
