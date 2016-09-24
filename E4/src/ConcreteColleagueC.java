class ConcreteColleagueC extends AbstractColleague{  
    public ConcreteColleagueC(AbstractMediator mediator){  
        super(mediator);  
        super.med.register(this);  
    }  
    public void action() {  
        System.out.println("CCCCCCCCCCCCCCC");  
    }  
}