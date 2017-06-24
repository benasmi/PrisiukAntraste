package com.mabe.productions.prisiukantraste;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TitleJob extends JobService {

    public static final int JOB_ADD_TITLE = 0;
    public static final int JOB_VOTE_UP_TITLE = 1;
    public static final int JOB_VOTE_DOWN_TITLE = 2;

    @Override
    public boolean onStartJob(JobParameters params) {

        int job_id = params.getJobId();


        String type = params.getExtras().getString("type");;
        String title = params.getExtras().getString("title");
        String url = params.getExtras().getString("url");
        Log.i("TEST", "job id: " + job_id + " url: " + url + " title: " + title);


        switch (job_id){
            case JOB_ADD_TITLE:
                new ServerManager(this, ServerManager.SERVER_ADDRESS_INSERT_TITLE, false, null).execute(url,title, type);
                break;

            case JOB_VOTE_UP_TITLE:
                new ServerManager(this, ServerManager.SERVER_ADDRESS_VOTING_UP,false,null).execute(url,title);
                break;


            case JOB_VOTE_DOWN_TITLE:
                new ServerManager(this, ServerManager.SERVER_ADDRESS_VOTING_DOWN,false,null).execute(url,title);

                break;
        }


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        int job_id = params.getJobId();
        return false;
    }

}