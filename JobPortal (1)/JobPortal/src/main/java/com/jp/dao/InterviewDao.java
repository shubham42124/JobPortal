package com.jp.dao;

import java.util.List;

import com.jp.entity.Interview;

public interface InterviewDao {
public Interview createInterview(int jobseekerid,String company, String location, String datetime, String flag);
	
	public String acceptInterview(int jobseekerid);
	
	public List<Interview> getAllInterviews(int jobseekerid);

}
