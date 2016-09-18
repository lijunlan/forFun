class ConcreteColleagueA extends AbstractColleague{  
    public ConcreteColleagueA(AbstractMediator mediator){  
        super(mediator);  
        super.med.register(this);  
    }  
    public void action() {  
        System.out.println("AAAAAAAAAAAAAAA");  
    }  
} 