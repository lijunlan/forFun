package com.sdll18.server.main;

import java.io.IOException;

import com.sdll18.server.network.MyService;

public class MainServer {

	public static void main(String[] args) throws IOException {
		MyService service = MyService.getService(8200);
		service.create();
		service.showIpInfo(); 
		service.start();
	}

}
