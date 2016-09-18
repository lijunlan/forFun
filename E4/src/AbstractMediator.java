
public abstract class AbstractMediator {
	// 注册中介对象
	public abstract void register(AbstractColleague ac);  
	// 一个中介对象的信息改变，中介者通知其他的所有对象
    public abstract void ColleagueChanged(AbstractColleague ac);
}
