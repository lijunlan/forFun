import java.util.Collection;
import java.util.Iterator;

public class ConcreteVisitor implements Visitor {
	//visitCollection实现了对Collection的元素的访问
	public void visitCollection(Collection collection) {
		System.out.println("{");
		Iterator item = collection.iterator();
		while(item.hasNext()){
			((Visitable)item.next()).accept(this);
			if(item.hasNext())System.out.print(",");
		}
		System.out.println("}");
	}

	public void visitFloat(FloatElement floatE) {
		System.out.println(floatE.getValue().toString()+"f");
	}

	public void visitString(StringElement stringE) {
		System.out.println("'"+stringE.getValue()+"'");
	}
}
