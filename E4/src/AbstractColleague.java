
public abstract class AbstractColleague {
	protected AbstractMediator med;  
    public AbstractColleague(AbstractMediator mediator){  
        this.med=mediator;  
    }  
    public abstract void action();  
    public void changed(){  
        med.ColleagueChanged(this);  
    } 
}
