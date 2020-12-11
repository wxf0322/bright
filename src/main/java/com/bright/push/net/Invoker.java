/**  
 * 项目名称  ：  bright
 * 文件名称  ：  Invoker.java
 * 日期时间  ：  2020年12月10日 - 下午3:16:04
 */
package com.bright.push.net;

/**
 * 
 * 
 * @author 王晓峰
 * @since 1.0
 */
public class Invoker {  
    
    private Command command;  
      
    public Invoker(Command command) {  
        this.command = command;  
    }  
  
    public void action(){  
        command.execute();  
    }  
}
