package com.example.post33.screen.performance

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class PrefetchJobService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i("post33", "prefetch job started")
        DIUtils.jobResultRepository.setJobResult("Prefetch Job done")
        Log.i("post33", "prefetch job done")

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }
}