/** 
 * Context 
 * 
 */  
class EncryptContext {  
    //组合策略对象   
	EncryptStrategy strategy;

    public EncryptContext(EncryptStrategy strategy){
    	this.strategy = strategy;
    }
      
    //执行具体的策略行为
    public void execute(){
    	strategy.encrypt();
    }
    
    public void execute(EncryptStrategy strategy){
    	this.strategy = strategy;
    	this.strategy.encrypt();
    }
}