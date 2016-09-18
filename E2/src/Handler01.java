// 具体处理者-01  
class Handler01 extends AbstractHandler {  
    @Override 
    protected int getHandlerLevel() {  
        return Levels.LEVEL_01;  
    }  
 
    @Override 
    protected void handle(AbstractRequest request) {  
        System.out.println("处理者-01 处理 " + request.getContent() + "\n");  
    }  
} 