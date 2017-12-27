
package mainPack;

import cloneRepo.CloneExecutors;
import java.io.IOException;

public class MainApp {
    public static void main (String []args) throws IOException{
        
        CloneExecutors clone = new CloneExecutors();
        System.out.println("Cloning in process...");
        clone.initExecutor();
        System.out.println("Cloning completed!");
    }  
}
