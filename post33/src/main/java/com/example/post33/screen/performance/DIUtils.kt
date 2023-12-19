package com.example.post33.screen.performance

object DIUtils {
    lateinit var jobResultRepository: JobResultRepository

    fun init() {
        jobResultRepository = JobResultRepository()
    }
}