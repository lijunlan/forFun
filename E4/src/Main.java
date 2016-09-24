public class Main {
	public static void main(String[] args) {
		AbstractMediator mediator = new ConcreteMediator();
		AbstractColleague colleagueA = new ConcreteColleagueA(mediator);
		AbstractColleague colleagueB = new ConcreteColleagueB(mediator);
		AbstractColleague colleagueC = new ConcreteColleagueC(mediator);
		colleagueA.changed();
		colleagueB.changed();
		colleagueC.changed();
	}
}
