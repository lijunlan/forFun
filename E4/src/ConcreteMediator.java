import java.util.ArrayList;
import java.util.Iterator;




public class ConcreteMediator extends AbstractMediator {
	private ArrayList<AbstractColleague> colleagueList=new ArrayList<AbstractColleague>();

	@Override
	public void register(AbstractColleague ac) {
		// TODO Auto-generated method stub
		colleagueList.add(ac);
	}

	@Override
	public void ColleagueChanged(AbstractColleague ac) {
		// TODO Auto-generated method stub
		System.out.println("对象:"+ac+"正在变更");
		Iterator<AbstractColleague> it=colleagueList.iterator();
		while(it.hasNext())
		{
			AbstractColleague tmp=it.next();
			if(!tmp.equals(ac)){
				System.out.println("对象："+tmp+"收到对象："+ac+"的变更通知");
				tmp.action();
			}
		}
	}  
	  
    
}
