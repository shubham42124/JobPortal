package com.jp.controller;
import org.springframework.http.MediaType;
import java.sql.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jp.dao.InterviewDao;
import com.jp.dao.JobApplicationDao;
import com.jp.entity.Company;
import com.jp.entity.JobApplication;
import com.jp.entity.JobSeeker;

@RestController
public class InterviewControler {
	@Autowired
	InterviewDao interviewdao;

	@Autowired
	JobApplicationDao jobAppDao;

	

	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping(value = "/createinterview", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> createInterview(@RequestParam("appId") String appId,
			@RequestParam("location") String location, @RequestParam("datetime") String datetime) {
		System.out.println("started");
		JobApplication ja = jobAppDao.getJobApplication(Integer.parseInt(appId));
		JobSeeker jobSeeker = ja.getJobSeeker();
		ja.setInterviewFlag(true);
		ja.setInterviewLocation(location);
		ja.setInterviewTime(Date.valueOf(datetime));
		ja.setInterviewAccepted(false);
		jobAppDao.updateApplication(ja);
		String verificationUrl = "http://localhost:8080/acceptinterview?jobseekerid=" + ja.getAppId();
		System.out.println("interview created");
		
		return ResponseEntity.ok(ja);
	}

	@RequestMapping(value = "/acceptinterview", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> acceptinterview(@RequestParam("appId") String appId) {
		System.out.println("started");
		JobApplication ja = jobAppDao.getJobApplication(Integer.parseInt(appId));
		JobSeeker jobSeeker = ja.getJobSeeker();
		ja.setInterviewAccepted(false);
		jobAppDao.updateApplication(ja);
		Company c = ja.getJobPosting().getCompany();

		
		return ResponseEntity.ok(ja);
	}

}
