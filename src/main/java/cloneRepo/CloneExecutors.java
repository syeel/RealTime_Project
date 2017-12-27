
package cloneRepo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CloneExecutors {
    
    public void initExecutor() throws IOException{
        final int WAITTIME = 60;
        String fileSeperator = System.getProperty("file.separator");        

        TargetPath tar = new TargetPath();
        String reposMainFolder = tar.getTarget();
        
        MakeTargetDir dir = new MakeTargetDir(reposMainFolder);
        dir.checkDir();
        
        GitRepo repo = new GitRepo();
        repo.readRepoInfo();
        ArrayList<String> repoURIs = repo.getURIs();
        ArrayList<String> matricNums = repo.getMatrics();

        // Create folders for every matric number
        for (int a=0; a<matricNums.size(); a++){
            String tempMat = matricNums.get(a);
            File repoDir = new File (reposMainFolder + fileSeperator + tempMat);
            if (! repoDir.exists()){
                repoDir.mkdirs();
            }
        }
        
        int numOfThreads = matricNums.size();
        List<Future> list = new ArrayList<>();
        ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(numOfThreads);
        for(int i=0; i< numOfThreads; i++){
            Runnable worker = new JGitCloneWorker(reposMainFolder, repoURIs.get(i), matricNums.get(i));
            Future future = executor.submit(worker);
            list.add(future);
        }        
        executor.shutdown();
        
        try {
            if (!executor.awaitTermination(WAITTIME, TimeUnit.SECONDS)) {
                for (int i = 0; i < list.size(); i++) {
                    if (!list.get(i).isDone()) {
                        list.get(i).cancel(true);
                        //LoggingAdapter.cloneLog("Repo for " + nameAccountList[i] + " is not being cloned due to passing 1 minute waiting");
                    }
                }
                executor.shutdownNow();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }  
}
