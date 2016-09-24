/**  
 * 信息发布接口  
 *  
 */  
public interface Noticer {   
    //添加订阅者   
    public void addObserver(Observer observer);   
    //移除订阅者   
    public void removeObserver(Observer observer);   
    //通知所有订阅者   
    public void notice(String message);   
}
