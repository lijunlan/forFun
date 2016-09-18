package command;

import java.util.Stack;
import java.util.Iterator;

public class MacroCommand implements Command {
    // 命令的集合
    private Stack commands = new Stack();
    /* 需要另一个Stack保存Undo信息，用以实现Redo */
    private Stack recommands = new Stack();
    
    // 执行
    public void execute() {
        Iterator it = commands.iterator();
        while (it.hasNext()) {
            ((Command)it.next()).execute();
        }
    }
    // 新增
    public void append(Command cmd) {
        if (cmd != this) {
            commands.push(cmd);
            /* 每次新增后需删去保存Undo信息的Stack中的数据 */
            
        }
    }
    // 刪除最后一个命令
    public void undo() {
        if (!commands.empty()) {
            recommands.push(commands.pop());
        }
    }
    // 全部刪除
    public void clear() {
        commands.clear();
        recommands.clear();
    }
   
    /* 实现redo方法 */
    public void redo(){
    	if(!recommands.empty()){
    		commands.push(recommands.pop());
    	}
    }
	
}

