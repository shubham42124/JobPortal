package com.jp.dao;

import com.jp.entity.JobPosting;

public interface JobPostingDao {
	
JobPosting createJobPosting(JobPosting job, int cid);
	
	
	JobPosting getJobPosting(int id);

	
	boolean deleteJobPosting(int id);
	JobPosting updateJobPosting(JobPosting job);

}
