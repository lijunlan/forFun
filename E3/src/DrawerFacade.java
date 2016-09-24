
public class DrawerFacade {
	private int key1,key2;
	private DrawerOne dw1;
	private DrawerTwo dw2;
	public DrawerFacade()
	{
		key1=1;
		key2=0;
		dw1=new DrawerOne();
		dw2=new DrawerTwo();
	}
	public void open1()
	{
		System.out.println("请求打开抽屉1");
		if(key1!=1){
			System.out.println("尚未得到抽屉1的钥匙");
			return;
		}
			
		dw1.open();
		key2=1;
	}
	public void open2()
	{
		System.out.println("请求打开抽屉2");
		if(key2!=1){
			System.out.println("尚未得到抽屉2的钥匙");
			return;
		}
		dw2.open();
	}

}
