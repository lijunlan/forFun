public class Test {   
    public static void main(String[] args) {   
        //创建发布者对象   
        NoticerImpl impl = new NoticerImpl();   
        //创建两个接收者   
        Customer1 c1 = new Customer1();   
        Customer2 c2 = new Customer2();   

        //为接收者进行注册
        impl.addObserver(c1);
        impl.addObserver(c2);
        //发布新消息   
        impl.notice("发布新消息啦！");

    }   
}