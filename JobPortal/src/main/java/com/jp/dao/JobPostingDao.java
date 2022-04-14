package com.jp.dao;

import java.util.List;

import com.jp.entity.JobPosting;

public interface JobPostingDao {
	
JobPosting createJobPosting(JobPosting job, int cid);
	
	
	JobPosting getJobPosting(int id);
	
	List<JobPosting> getAllJobPosting();

	
	boolean deleteJobPosting(int id);
	
	JobPosting updateJobPosting(JobPosting job);

}
