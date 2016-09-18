public class Client {  
    /** 
     * Test Strategy Pattern 
     *  
     */  
    public static void main(String[] args) { 
    	
    	EncryptContext context;
    	
        //2种不同的策略  
        EncryptStrategy s1 = new MD5Strategy();
        EncryptStrategy s2 = new DesStrategy();
        //使用DES策略（算法）  
        context = new EncryptContext(s1);
        context.execute();
        context.execute(s2);
        //使用MD5策略（算法）  
    	
    	//使用自定义的策略
        EncryptStrategy s3 = new EncryptStrategy() {
			
			@Override
			public void encrypt() {
				System.out.println("自定义算法加密");
			}
		};
		
		context.execute(s3);
    }  
}