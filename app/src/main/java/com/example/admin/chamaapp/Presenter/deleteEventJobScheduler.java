package com.example.admin.chamaapp.Presenter;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;

import com.example.admin.chamaapp.Model.Event;
import com.google.gson.Gson;

public class deleteEventJobScheduler extends JobService
{

    public static final String EVENT = "Event to be deleted ";
    public deleteEventJobScheduler()
    {

    }

    @Override
    public boolean onStartJob(JobParameters params)
    {
        final DeletePastEvents deletePastEvents = new DeletePastEvents();

        AsyncTask<JobParameters,Void,Void > task = new AsyncTask<JobParameters, Void, Void>() {
            @Override
            protected Void doInBackground(JobParameters... backgroundJobParameters)
            {
                JobParameters jobParams = backgroundJobParameters[0];
                String eventString = jobParams.getExtras().getString(EVENT);
                Gson g = new Gson();
                Event event = g.fromJson(eventString,Event.class);
                deletePastEvents.deleteAPastEvent(event);

                jobFinished(jobParams,false);
                Log.d("DeletePastEvents","This method has been called ");
                return null;
            }
        };

        task.execute(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


}
