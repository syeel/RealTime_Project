
package mainPack;

import cloneRepo.CloneExecutors;
import java.io.IOException;
import java.util.ArrayList;
import setupDirectory.MakeRepoDir;
import setupDirectory.MakeRoottDir;
import setupDirectory.TargetRoot;

public class MainApp {
    public static void main (String []args) throws IOException{
        
        TargetRoot tar = new TargetRoot();
        String reposMainFolder = tar.getTarget();
        
        MakeRoottDir dir = new MakeRoottDir(reposMainFolder);
        dir.checkDir();
        
        GitRepo repo = new GitRepo();
        repo.readRepoInfo();
        ArrayList<String> repoURIs = repo.getURIs();
        ArrayList<String> matricNums = repo.getMatrics();
        
        MakeRepoDir repoDir = new MakeRepoDir(reposMainFolder, matricNums);
        repoDir.checkRepoDir();
        
        CloneExecutors clone = new CloneExecutors(reposMainFolder, repoURIs, matricNums);
        System.out.println("Cloning in process...");
        clone.initExecutor();
        System.out.println("Cloning completed!");
    }  
}
