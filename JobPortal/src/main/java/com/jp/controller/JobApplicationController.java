
package com.jp.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jp.dao.InterestedDao;
import com.jp.dao.JobApplicationDao;
import com.jp.dao.JobPostingDao;
import com.jp.dao.JobSeekerDao;
import com.jp.entity.Company;
import com.jp.entity.JobApplication;
import com.jp.entity.JobPosting;
import com.jp.entity.JobSeeker;




@Controller
@RequestMapping(value = "/application")
public class JobApplicationController {

	@Autowired
	JobSeekerDao jobSeekerDao;


	@Autowired
	JobPostingDao jobDao;

	@Autowired
	JobApplicationDao jobAppDao;

	@Autowired
	InterestedDao interestedDao;

	@PersistenceContext
	private EntityManager entityManager;

	private static String UPLOADED_FOLDER = "C:/";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String applyPage(@RequestParam("userId") String jobSeekerId, @RequestParam("jobId") String jobId,
			Model model) {

		return "jobapplication";
	}

	
	@RequestMapping(value = "/apply")//, method = RequestMethod.POST)
	public String apply(@RequestParam("userId") String jobSeekerId, @RequestParam("jobId") String jobId,
			@RequestParam("resumeFlag") boolean resumeFlag,
			 Model model) throws IOException {
		if (resumeFlag == true) {

			
			//byte[] bytes = file.get().getBytes();
			//Path path = Paths.get(UPLOADED_FOLDER + file.get().getOriginalFilename());
			JobApplication ja = new JobApplication();
			ja = jobAppDao.apply(Integer.parseInt(jobSeekerId), Integer.parseInt(jobId), resumeFlag
					);// apply(Integer.parseInt(jobSeekerId),
										// Integer.parseInt(jobId),
										// resumeFlag, path);
			JobSeeker js = jobSeekerDao.getJobSeeker(Integer.parseInt(jobSeekerId));
			JobPosting jp = jobDao.getJobPosting(Integer.parseInt(jobId));
			
			Company company = jp.getCompany();
			List<?> ij = interestedDao.getAllInterestedJobId(Integer.parseInt(jobSeekerId));
			int i = 0;
			int j = 0;
			if (ij.contains(Integer.parseInt(jobId))) {
				i = 1;
			}

			List<Integer> il = getAppliedJobs(jobSeekerId);
			if (il.contains(Integer.parseInt(jobId))) {
				j = 1;
			}

			model.addAttribute("job", jp);
			model.addAttribute("seeker", js);
			model.addAttribute("company", company);
			model.addAttribute("interested", i);
			model.addAttribute("applied", j);
			return "userjobprofile";

		} else {
			JobApplication ja = new JobApplication();
			ja = jobAppDao.apply(Integer.parseInt(jobSeekerId), Integer.parseInt(jobId), resumeFlag);
			JobSeeker js = jobSeekerDao.getJobSeeker(Integer.parseInt(jobSeekerId));
			JobPosting jp = jobDao.getJobPosting(Integer.parseInt(jobId));
		
					
			Company company = jp.getCompany();
			List<?> ij = interestedDao.getAllInterestedJobId(Integer.parseInt(jobSeekerId));
			int i = 0, j = 0;
			if (ij.contains(Integer.parseInt(jobId))) {
				i = 1;
			}

			List<Integer> il = getAppliedJobs(jobSeekerId);
			if (il.contains(Integer.parseInt(jobId))) {
				j = 1;
			}

			model.addAttribute("job", jp);
			model.addAttribute("seeker", js);
			model.addAttribute("company", company);
			model.addAttribute("interested", i);
			model.addAttribute("applied", j);

			return "userjobprofile";

		}

	}

	@RequestMapping(value = "/cancel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String cancelApplication(@RequestParam("jobApplicationId") String jobAppId) {
		boolean deleted = jobAppDao.cancel(Integer.parseInt(jobAppId));
		if (deleted)
			return "Cancelled";
		return "Unable to delete";
	}

	
	@RequestMapping(value = "/modifyapplicationstate", method = RequestMethod.POST)
	public String modifyApplicationState(@RequestParam("jobAppId") String jobAppId,
			@RequestParam("state") String state) {
		JobApplication ja = jobAppDao.modifyJobApplicationStatus(Integer.parseInt(jobAppId), Integer.parseInt(state));
		if (ja == null) {
			return "Error";
		}
		return "modified";
	}

	
	@RequestMapping(value = "/company/getAppliedJobs", method = RequestMethod.GET)
	public ResponseEntity<?> getAppliedJobs(@RequestParam("companyId") int id) {
		Query query = entityManager.createQuery("SELECT jobId FROM JobPosting jp WHERE jp.companyId = :id");
		query.setParameter("id", id);
		List<Integer> list = new ArrayList<Integer>();
		List<?> querylist = query.getResultList();
		for (Iterator<?> iterator = querylist.iterator(); iterator.hasNext();) {
			int uid = (int) iterator.next();
			list.add(uid);
			System.out.println(uid);
		}

		return ResponseEntity.ok("data");
	}

	public List<Integer> getAppliedJobs(@RequestParam("jobSeekerId") String jobSeekerId) {
		List<?> jobSeekerAppliedList = jobSeekerDao.getJobSeeker(Integer.parseInt(jobSeekerId)).getJobApplicationList();
		List<Integer> jobIdList = new ArrayList<Integer>();
		for (Iterator iterator = jobSeekerAppliedList.iterator(); iterator.hasNext();) {
			JobApplication ja = (JobApplication) iterator.next();
			int jobId = ja.getJobPosting().getJobId();
			jobIdList.add(jobId);
		}
		return jobIdList;
	}

}
