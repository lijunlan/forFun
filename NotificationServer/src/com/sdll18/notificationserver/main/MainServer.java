package com.sdll18.notificationserver.main;

import java.io.IOException;

import com.sdll18.notificationserver.network.MyService;

public class MainServer {

	public static void main(String[] args) throws IOException {
		MyService service = MyService.getService(9988);
		service.create();
		service.showIpInfo(); 
		service.start();
	}

}
