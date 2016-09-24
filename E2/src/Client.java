// 测试类  
public class Client {  
    public static void main(String[] args) {  
        // 创建指责链的所有节点（共三个）  
    	AbstractHandler handler01 = new Handler01();
    	AbstractHandler handler02 = new Handler02();
    	AbstractHandler handler03 = new Handler03();
    	
        // 进行链的组装，即头尾相连，一层套一层  
    	handler01.setNextHandler(handler02);
    	handler02.setNextHandler(handler03);
    	handler03.setNextHandler(handler01);
    	
        // 创建请求并提交到指责链中进行处理  
        AbstractRequest request01 = new Request01("请求-01");  
        AbstractRequest request02 = new Request02("请求-02");  
        AbstractRequest request03 = new Request03("请求-03");  
          
        // 每次提交都是从链头开始遍历  
        handler01.handleRequest(request01);  
        handler01.handleRequest(request02);  
        handler01.handleRequest(request03);  
    }  
}
