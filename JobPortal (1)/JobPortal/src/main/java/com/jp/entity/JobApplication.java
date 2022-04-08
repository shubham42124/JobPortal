package com.jp.entity;

import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table (name="jobapplication")
public class JobApplication {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "appId", unique = true, nullable = false)
	private int appId;
	
	@ManyToOne
	@JoinColumn(name="jobId")
	private JobPosting jobposting;
	
	@ManyToOne
	@JoinColumn(name="jobSeekerId")
	@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="jobSeekerId")
	private JobSeeker jobSeeker;
	
	@Column(name="resume")
	private boolean resume;
	
	@Column(name="resumePath")
	private String resumePath;
	
	@Column(name="state")
	private int state;
	
	@Column(name="interviewFlag")
	private boolean interviewFlag;
	
	@Column(name="interviewLocation")
	private String interviewLocation;
	
	@Column(name="interviewTime")
	private Date interviewTime;
	
	@Column(name="interviewAccepted")
	private boolean interviewAccepted;
	
	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public JobPosting getJobPosting() {
		return jobposting;
	}

	public void setJobPosting(JobPosting jobPosting) {
		this.jobposting = jobPosting;
	}
	@JsonProperty
	public JobSeeker getJobSeeker() {
		return jobSeeker;
	}
	public void setJobSeeker(JobSeeker jobSeeker) {
		this.jobSeeker = jobSeeker;
	}

	public boolean isResume() {
		return resume;
	}

	public void setResume(boolean resume) {
		this.resume = resume;
	}

	public String getResumePath() {
		return resumePath;
	}

	public void setResumePath(String resumePath) {
		this.resumePath = resumePath;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isInterviewFlag() {
		return interviewFlag;
	}

	public void setInterviewFlag(boolean interviewFlag) {
		this.interviewFlag = interviewFlag;
	}

	public String getInterviewLocation() {
		return interviewLocation;
	}

	public void setInterviewLocation(String interviewLocation) {
		this.interviewLocation = interviewLocation;
	}

	public Date getInterviewTime() {
		return interviewTime;
	}

	public void setInterviewTime(Date interviewTime) {
		this.interviewTime = interviewTime;
	}

	public boolean isInterviewAccepted() {
		return interviewAccepted;
	}

	public void setInterviewAccepted(boolean interviewAccepted) {
		this.interviewAccepted = interviewAccepted;
	}

}
