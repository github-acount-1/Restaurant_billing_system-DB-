
package restaurant.billing.system.test;

import java.io.IOException;

public class WaiterClient {
    
    public static void main(String[] args) throws Exception {
        String text = "one two three";
        
            try {
                String cmd = "echo hi > a.txt";
                System.out.println(cmd);
                Runtime.getRuntime().exec(cmd);
                //Runtime.getRuntime().exec("notepad /p Mak \"Restaurant Bill.txt\"");
            } catch (IOException e) {
                e.printStackTrace();
            }
            
    }
    
}
