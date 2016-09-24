public class Customer1 implements Observer {   
  
    public void update(String message) {   
        System.out.println("Customer1 ACCEPT message:"+message);   
    }   
  
}