import junit.framework.TestCase;

public class helloworldTest extends TestCase {

	private helloworld hello;
	private int x, y;

	protected void setUp() throws Exception {
		hello = new helloworld();
		x = 1;
		y = 2;
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPrintHello() {
		assertEquals(hello.printHello(x), 1);
		assertEquals(hello.printHello(x), 0);
		assertEquals(hello.printHello(y), 1);
		assertEquals(0,hello.printHello(y));
	}

}
