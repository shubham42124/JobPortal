package com.jp.dao;

import java.util.List;

import com.jp.entity.JobPostingsView;
import com.jp.entity.JobSeeker;

public interface JobSeekerDao {
	
	 
		public List<?> filterJobs(JobPostingsView jpv, List<?> jobIds);

		public JobSeeker createJobSeeker(JobSeeker job) throws Exception;

		
		public JobSeeker updateJobSeeker(JobSeeker js);

		
		public JobSeeker getJobSeeker(int id);

		
		public List<String> PasswordLookUp(String emailid);

		
		public void verify(JobSeeker j);
		
		public List<?> searchJobs(String searchString);

		
		public List<Integer> getUserIdFromEmail(String emailid);

}
