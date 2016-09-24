import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**  
 * 信息发布者  
 *  
 */  
public class NoticerImpl implements Noticer{   
    private List<Observer> list = new ArrayList<Observer>();

	@Override
	public void addObserver(Observer observer) {
		list.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		list.remove(observer);
	}

	@Override
	public void notice(String message) {
		Iterator<Observer> item = list.iterator();
		while(item.hasNext()){
			item.next().update(message);
		}
	}   
    
    //添加观察者
  
    //删除观察者
  
    //通知观察者
} 