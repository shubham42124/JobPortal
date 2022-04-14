package com.jp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


// Annotation Calling 

@Entity
@Table(name="jobpostingsview")

// Class 

public class JobPostingsView {
	
	// Methods of Class 
	
	@Id
	@Column(name = "jobId")
	private int jobId;
	@Column(name = "title")
	private String title;
	@Column(name = "description")
	private String description;
	@Column(name = "responsibilities")
	private String responsibilites;
	@Column(name = "location")
	private String location;
	@Column(name = "salary")
	private String salary;
	@Column(name = "companyId")
	private int companyId;
	@Column(name = "companyName")
	private String companyName;
	@Column(name = "keywords")
	private String keywords;
	@Column(name = "state")
	private int state;
	
	// Generate Getters and Setters 

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResponsibilites() {
		return responsibilites;
	}
	public void setResponsibilites(String responsibilites) {
		this.responsibilites = responsibilites;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
