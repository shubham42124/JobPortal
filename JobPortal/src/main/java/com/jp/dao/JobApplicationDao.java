package com.jp.dao;

import com.jp.entity.JobApplication;

public interface JobApplicationDao {
	
	boolean cancel(int jobAppId);

	
	JobApplication apply(int jobseekerId, int jobId, boolean resumeFlag);

	
	JobApplication getJobApplication(int jobAppId);

	
	JobApplication modifyJobApplicationStatus(int jobAppId, int state);
	
	
	JobApplication updateApplication(JobApplication ja);


	

}
