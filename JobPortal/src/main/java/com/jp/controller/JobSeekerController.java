package com.jp.controller;

import org.springframework.http.MediaType;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jp.dao.CompanyDao;
import com.jp.dao.InterestedDao;
import com.jp.dao.JobPostingDao;
import com.jp.dao.JobSeekerDao;
import com.jp.dao.JobSeekerDaoImpl;
import com.jp.entity.Company;
import com.jp.entity.Interested;
import com.jp.entity.JobApplication;
import com.jp.entity.JobPosting;
import com.jp.entity.JobPostingsView;
import com.jp.entity.JobSeeker;

@RestController
@RequestMapping("/")
public class JobSeekerController {
	
	@Autowired
	JobSeekerDao jobSeekerDao;

	@Autowired
	InterestedDao interestedDao;

	@PersistenceContext
	private EntityManager entityManager;

	
	// Search Job Mapping 
	
	@RequestMapping(value = "/searchjobs", method = RequestMethod.GET)
	public List<?> searchJobs(@RequestParam("userId") String userId,
			@RequestParam("search") Optional<String> searchString,
			@RequestParam("locations") Optional<String> locations,
			@RequestParam("companies") Optional<String> companies, 
			@RequestParam("min") Optional<String> min,
			@RequestParam("max") Optional<String> max, Model model) {
		JobPostingsView jpv = new JobPostingsView();
		String search = "";
		if (!searchString.equals(Optional.empty())) {
			search = searchString.get();
		}
		
		List<?> jobIds = jobSeekerDao.searchJobs(search);
		if ((!locations.equals(Optional.empty())) && (locations.get()!="")) {
			System.out.println("location");
			jpv.setLocation(locations.get());
		}
		if (!companies.equals(Optional.empty()) && companies.get()!="") {
			System.out.println("comp");
			jpv.setCompanyName(companies.get());
		}
		if (!min.equals(Optional.empty()) && !max.equals(Optional.empty())) {
		String salary = min.get()+","+max.get();
		jpv.setSalary(salary);
		}

		List<?> jp = jobSeekerDao.filterJobs(jpv, jobIds);

		JobSeeker jobseeker = jobSeekerDao.getJobSeeker(Integer.parseInt(userId));
		
		model.addAttribute("jobs", jp);
		model.addAttribute("seeker", jobseeker);
		
		return jobSeekerDao.searchJobs(userId);
	}

	@Autowired
	CompanyDao companyDao;
	
	@Autowired
	JobPostingDao jobDao;
	
	// Show JObs mapping 
	
	@RequestMapping(value = "/showjob", method = RequestMethod.GET)
	public String showJob(@RequestParam("userId") String userId, @RequestParam("jobId") String jobId, Model model) {
		
		JobPosting job = jobDao.getJobPosting(Integer.parseInt(jobId));
		Company company = job.getCompany();
		JobSeeker seeker = jobSeekerDao.getJobSeeker(Integer.parseInt(userId));
		List<?> ij = interestedDao.getAllInterestedJobId(Integer.parseInt(userId));
		int i = 0,j=0;
		if(ij.contains(Integer.parseInt(jobId))){
			i = 1;
		}
		
		List<Integer> il = getAppliedJobs(userId);
		if(il.contains(Integer.parseInt(jobId))){
			j = 1;
		}

		
		
		model.addAttribute("job", job);
		model.addAttribute("seeker", seeker);
		model.addAttribute("company", company);
		model.addAttribute("interested", i);
		model.addAttribute("applied", j);
		
		return "userjobprofile";
	}

	@RequestMapping(value = "/createuser", method = RequestMethod.POST)  //http://localhost:5678/createuser
	public String createJobSeeker(@RequestParam("name") String name, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("type") String type, Model model)
			throws IOException, SQLException {

		int randomPIN = (int) (Math.random() * 9000) + 1000;
		String[] splited = name.split("\\s+");

		try {

			if (type.equals("seeker")) {

				JobSeeker j = new JobSeeker();
				j.setFirstName(splited[0]);
				j.setLastName(splited[1]);
				j.setPassword(password);
				j.setEmailId(email);
				
	
				j.setVerificationCode(randomPIN);
				j.setVerified(false);

				JobSeeker j1 = jobSeekerDao.createJobSeeker(j);

				String verificationUrl = "http://localhost:8080/register/verify?userId=" + j1.getJobseekerId() + "&pin="
						+ randomPIN + "&type=seeker";

				
				return "Seeker successfully Registered";

			}

			else {

				Company c = new Company();
				c.setVerified(false);
				c.setVerificationCode(randomPIN);
				c.setCompanyName(name);
				c.setCompanyUser(email);
				c.setPassword(password);
				c.setHeadquarters("head");

				Company c1 = companyDao.createCompany(c);

				String verificationUrl = "http://localhost:8080/register/verify?userId=" + c1.getCompanyId() + "&pin="
						+ randomPIN + "&type=recruiter";

				

				// Company c1 =companyDao.
				return "CodeSent";
			}

		} catch (SQLException se) {
			HttpHeaders httpHeaders = new HttpHeaders();
			Map<String, Object> message = new HashMap<String, Object>();
			Map<String, Object> response = new HashMap<String, Object>();
			message.put("code", "400");
			message.put("msg", "Email Already Exists");
			response.put("BadRequest", message);
			JSONObject json_test = new JSONObject(response);
			String json_resp = json_test.toString();

			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return "error";

		} catch (Exception se) {
			HttpHeaders httpHeaders = new HttpHeaders();

			Map<String, Object> message = new HashMap<String, Object>();
			Map<String, Object> response = new HashMap<String, Object>();
			message.put("code", "400");
			message.put("msg", "Error Occured");
			response.put("BadRequest", message);
			JSONObject json_test = new JSONObject(response);
			String json_resp = json_test.toString();

			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return "error";
		}
	}

	@RequestMapping(value = "/updatepage")
	public String updateSeekerPage(@RequestParam("id") String id, Model model) {

		JobSeeker j1 = new JobSeekerDaoImpl().getJobSeeker(Integer.parseInt(id));
		model.addAttribute("j", j1);
		
		return "updateSeeker";
	}
	
	
	@RequestMapping(value = "/Update")  //http://localhost:5678/Update
	public String updateJobSeeker(@RequestParam("id") String id, @RequestParam("firstName") Optional<String> firstName,
			@RequestParam("lastName") Optional<String> lastName, @RequestParam("emailId") Optional<String> emailId,
			@RequestParam("highestEducation") Optional<String> highestEducation,
			@RequestParam("password") Optional<String> password, @RequestParam("skills") Optional<String> skills,
			@RequestParam("workEx") Optional<String> workEx, Model model) throws Exception {
		JobSeeker js = new JobSeeker();

		js.setJobseekerId(Integer.parseInt(id));

		if (!emailId.equals(Optional.empty())) {
			System.out.println("emailid done : " + emailId.get() + ":::: " + emailId);
			js.setEmailId(emailId.get());
		}
		if (!firstName.equals(Optional.empty())) {
			System.out.println("fname done");
			js.setFirstName(firstName.get());
		}
		if (!lastName.equals(Optional.empty())) {
			System.out.println("lname done");
			js.setLastName(lastName.get());
		}
		if (!highestEducation.equals(Optional.empty())) {
			System.out.println("highest edu");
			js.setHighestEducation(Integer.parseInt(highestEducation.get()));
		}
		if (!password.equals(Optional.empty())) {
			System.out.println("password");
			js.setPassword(password.get());
		}
		if (!skills.equals(Optional.empty())) {
			System.out.println("skills : " + skills);
			js.setSkills(skills.get());
			System.out.println("huhuhuh : " + skills.get());
		}

		if (!workEx.equals(Optional.empty())) {
			System.out.println("workex : " + workEx);
			js.setWorkEx(Integer.parseInt(workEx.get()));
		}

		JobSeeker jobseeker = jobSeekerDao.getJobSeeker(Integer.parseInt(id));
		JobSeeker jobskr = null;
		if (jobseeker != null) {
			jobskr = jobSeekerDao.updateJobSeeker(js);
			System.out.println("updated");
		} else {
			jobskr = jobSeekerDao.createJobSeeker(js);
		}
		System.out.println("done");
		System.out.println(jobskr.getVerificationCode());

		model.addAttribute("seeker", jobskr);
		return "userprofile";

	}
 
	@RequestMapping(value = "/update/company" )//, method = RequestMethod.PUT)  //http://localhost:5678/update/company
	public String companyupdate(@RequestParam("id") String id, @RequestParam("companyName") Optional<String> name,
			@RequestParam("headquarters") Optional<String> headquarters,
			@RequestParam("companyUser") Optional<String> user,
			@RequestParam("description") Optional<String> description, Model model) {

		Company c = new Company();

		c.setCompanyId(Integer.parseInt(id));

		if (!name.equals(Optional.empty())) {

			c.setCompanyName(name.get());
		}
		if (!user.equals(Optional.empty())) {

			c.setCompanyUser(user.get());
		}
		if (!headquarters.equals(Optional.empty())) {
			c.setHeadquarters(headquarters.get());
		}
		if (!description.equals(Optional.empty())) {
			c.setDescription(description.get());
		}

		Company company = companyDao.getCompany(Integer.parseInt(id));
		Company c1 = null;
		if (company != null) {
			c1 = companyDao.updateCompany(c);

		} else {
			return "error";
		}
		System.out.println("done");
		model.addAttribute("company", c1);
		return "companyProfile";

	}

	@RequestMapping(value = "/interested")
	public String createInterest(@RequestParam("userId") String userId, @RequestParam("jobId") String jobId, Model model) {

		try {
			Interested in = new Interested();
			in.setJobId(Integer.parseInt(jobId));
			in.setJobSeekerId(Integer.parseInt(userId));
			Interested i1 = interestedDao.createInterest(in);
			
		} catch (Exception e) {

			HttpHeaders httpHeaders = new HttpHeaders();

			Map<String, Object> message = new HashMap<String, Object>();
			Map<String, Object> response = new HashMap<String, Object>();
			message.put("code", "400");
			message.put("msg", "Error Occured");
			response.put("BadRequest", message);
			JSONObject json_test = new JSONObject(response);
			String json_resp = json_test.toString();

			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return "error";

		}
		JobPosting job = jobDao.getJobPosting(Integer.parseInt(jobId));
		Company company = job.getCompany();
		JobSeeker seeker = jobSeekerDao.getJobSeeker(Integer.parseInt(userId));
		List<?> ij = interestedDao.getAllInterestedJobId(Integer.parseInt(userId));
		int i = 0, j = 0;
		if(ij.contains(Integer.parseInt(jobId))){
			i = 1;
		}
		String message="<div class=\"alert alert-success\">This job has been <strong>Successfully added</strong> to your interests</div>";
		
		List<Integer> il = getAppliedJobs(userId);
		if(il.contains(Integer.parseInt(jobId))){
			j = 1;
		}
		
		model.addAttribute("job", job);
		model.addAttribute("seeker", seeker);
		model.addAttribute("company", company);
		model.addAttribute("interested", i);
		model.addAttribute("message", message);
		model.addAttribute("applied", j);
		
		
		return "userjobprofile";
	}

	@RequestMapping(value = "/interested/delete", method = RequestMethod.POST)
	public String deleteInterest(@RequestParam("userId") String userId, @RequestParam("jobId") String jobId, Model model) {

		try {
			List<?> querylist = interestedDao.getInterestedJobId(Integer.parseInt(jobId), Integer.parseInt(userId));
			boolean interestDeleted = interestedDao.deleteInterest(Integer.parseInt(querylist.get(0).toString()));
			if (interestDeleted) {
				JobPosting job = jobDao.getJobPosting(Integer.parseInt(jobId));
				Company company = job.getCompany();
				JobSeeker seeker = jobSeekerDao.getJobSeeker(Integer.parseInt(userId));
				List<?> ij = interestedDao.getAllInterestedJobId(Integer.parseInt(userId));
				int i = 0;
				if(ij.contains(Integer.parseInt(jobId))){
					i = 1;
				}

				String message="<div class=\"alert alert-danger\">This job has been <strong>Successfully removed</strong> from your interests</div>";
				
				model.addAttribute("job", job);
				model.addAttribute("seeker", seeker);
				model.addAttribute("company", company);
				model.addAttribute("interested", i);
				model.addAttribute("message", message);
				model.addAttribute("applied", 1);
				
				
				return "userjobprofile";

			} else {
				return "error";
			}

		} catch (Exception e) {

			HttpHeaders httpHeaders = new HttpHeaders();

			Map<String, Object> message = new HashMap<String, Object>();
			Map<String, Object> response = new HashMap<String, Object>();
			message.put("code", "400");
			message.put("msg", "Error Occured");
			response.put("BadRequest", message);
			JSONObject json_test = new JSONObject(response);
			String json_resp = json_test.toString();

			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			return "error";

		}

	}

	@RequestMapping(value = "/getinterestedjobs", method = RequestMethod.GET)
	public String getInterestedJobsForJobSeeker(@RequestParam("jobSeekerId") String jobSeekerId, Model model) {
		
		JobSeeker jobseeker = jobSeekerDao.getJobSeeker(Integer.parseInt(jobSeekerId));
		List<?> jobSeekerInterestsList = jobSeekerDao.getJobSeeker(Integer.parseInt(jobSeekerId)).getInterestedjobs();
		
		model.addAttribute("jobs", jobSeekerInterestsList);
		model.addAttribute("seeker", jobseeker);
		return "interestedjobs";
	}

	@RequestMapping(value="/getappliedjobs")
	public List<Integer> getAppliedJobs(@RequestParam("jobSeekerId") String jobSeekerId){
		List<?> jobSeekerAppliedList =jobSeekerDao.getJobSeeker(Integer.parseInt(jobSeekerId)).getJobApplicationList();
		List<Integer> jobIdList = new ArrayList<Integer>();
		for (Iterator iterator = jobSeekerAppliedList.iterator(); iterator.hasNext();) {
			JobApplication ja = (JobApplication) iterator.next();
			int jobId = ja.getJobPosting().getJobId();
			jobIdList.add(jobId);
		}
		return jobIdList;
	}
	
	
	

}