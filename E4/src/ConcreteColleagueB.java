class ConcreteColleagueB extends AbstractColleague{  
    public ConcreteColleagueB(AbstractMediator mediator){  
        super(mediator);  
        super.med.register(this);  
    }  
    public void action() {  
        System.out.println("BBBBBBBBBBBBBBB");  
    }  
} 