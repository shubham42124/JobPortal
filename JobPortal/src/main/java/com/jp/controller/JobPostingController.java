package com.jp.controller;


//import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jp.dao.CompanyDao;
import com.jp.dao.JobPostingDao;
import com.jp.entity.Company;
import com.jp.entity.JobPosting;

@RestController
@RequestMapping("/jobposting")
public class JobPostingController {
	@Autowired
	JobPostingDao jobDao;

	@Autowired
	CompanyDao companyDao;

	// Create Job post Mapping 
	
	@RequestMapping(value="/create")
	public String showHomePage(@RequestParam("cid") String cid, Model model) {
		System.out.println(cid);
		
		Company company = companyDao.getCompany(Integer.parseInt(cid));
		model.addAttribute("cid", cid);
		model.addAttribute("company", company);
		return "postjob";
	}

	
	// Job Post Mapping 
	
	@RequestMapping("/jobPost")
	public String createJobPosting(@RequestParam("companyName") String cname ,@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("responsibilities") String responsibilities, @RequestParam("location") String location,
			@RequestParam("salary") String salary, @RequestParam("companyId") String cid, Model model) {
		JobPosting j = new JobPosting();
		j.setCompanyName(cname);
		j.setTitle(title);
		//j.setState(state);
		j.setDescription(description);
		j.setResponsibilities(responsibilities);
		j.setLocation(location);
		j.setSalary(salary);
		

		try {
			System.out.println("0");

			JobPosting p1 = jobDao.createJobPosting(j, Integer.parseInt(cid));
			model.addAttribute("job", p1);
			Company company = companyDao.getCompany(Integer.parseInt(cid));
			model.addAttribute("company", company);
			return "jobprofile";

		} catch (Exception e) {
			HttpHeaders httpHeaders = new HttpHeaders();
			Map<String, Object> message = new HashMap<String, Object>();
			Map<String, Object> response = new HashMap<String, Object>();
			message.put("code", "400");
			message.put("msg", "another passenger with the phone number  already exists.");
			response.put("BadRequest", message);
			JSONObject json_test = new JSONObject(response);
			String json_resp = json_test.toString();

			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return "error";
		}

	}

	@RequestMapping(value = "/delete/{id}")
	public String deleteJobPosting(@PathVariable("id") int id, Model model) {

		if (jobDao.deleteJobPosting(id)) {
			String message = "Job Posting with JobID " + id + " is deleted successfully";
			model.addAttribute("message", message);
			return "message";
		} else {
			return "error";
		}
	}

	@RequestMapping(value = "/update/{id}")
	public String showUpdatePage(@PathVariable("id") int id, @RequestParam("cid") String cid, Model model) {
		System.out.println(cid);
		System.out.println(id);
		
		Company company = companyDao.getCompany(Integer.parseInt(cid));
		JobPosting jp = jobDao.getJobPosting(id);
		model.addAttribute("job", jp);
		model.addAttribute("company", company);
		return "updatejob";
	}
	

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public String updateJobPosting(@PathVariable("id") int id, @RequestParam("CompanyName") String cname,
			@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("responsibilities") String responsibilities, @RequestParam("location") String location,
			@RequestParam("salary") String salary, @RequestParam("cid") String cid, Model model) {
		// TODO routing
		JobPosting job = jobDao.getJobPosting(id);
		if (job != null) {
			job.setjobId(id);
			job.setDescription(description);
			job.setCompanyName(cname);
			job.setTitle(title);
			job.setLocation(location);
			job.setResponsibilities(responsibilities);
			JobPosting p1 = jobDao.updateJobPosting(job);

			model.addAttribute("job", p1);
			Company company = companyDao.getCompany(Integer.parseInt(cid));
			model.addAttribute("company", company);
			return "jobprofile";
		}
		return "error";

	}

	@RequestMapping(value = "/modifyjobstate", method = RequestMethod.POST)
	public String modifyJobState(@RequestParam("jobId") String jobId, @RequestParam("CompanyName") String cname) {
		JobPosting jp = jobDao.getJobPosting(Integer.parseInt(jobId));
		jp.setCompanyName(cname);
		jp = jobDao.updateJobPosting(jp);
		if(jp==null){
			return "Error";
		}
		return "modified";
	}
	
	
	
	@RequestMapping(value="/ShowAllJobs",method = RequestMethod.GET)
	public List<JobPosting> getAllJobPosting() {
		return jobDao.getAllJobPosting();
		
	}

}
