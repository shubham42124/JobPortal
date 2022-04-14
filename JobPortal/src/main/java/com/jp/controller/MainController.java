package com.jp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jp.dao.CompanyDao;
import com.jp.dao.JobSeekerDao;
import com.jp.entity.Company;
import com.jp.entity.JobSeeker;

@Controller
@RequestMapping(value = "/")
public class MainController {
	
	@RequestMapping("/jobs")
	public String showHomePage() {
		return "index.html";
	}

	
	
		
	@RequestMapping(value = "/register")
	public String showRegisterPage() {
		return "register.html";
	}

	@Autowired
	JobSeekerDao jobSeekerDao;
	
	@Autowired
	CompanyDao companyDao;

	@RequestMapping(value = "/login" , method=RequestMethod.GET)
	public String login(@RequestParam("emailId") String emailId, @RequestParam("password") String password,
			@RequestParam("type") String type, Model model) {
		List<String> list = new ArrayList<String>();
		String email = emailId;
		String pwd = password;
		System.out.println(email + " : " + pwd);
		String message="<div class=\"alert alert-danger\">Invalid Login Credentials</div>";
		

		if (type.equals("recruiter")) {
			list = companyDao.PasswordLookUp(email);
			if (list.size() == 0) {
				
				model.addAttribute("message", message);
				return "login.html";
			} else {
				if (pwd.equals(list.get(0))) {
					List<Integer> cidl = new ArrayList<Integer>();
					cidl = companyDao.getCompanyIdFromEmail(email);
					Company cmp = companyDao.getCompany(cidl.get(0));
					model.addAttribute("company", cmp);
					
					return "Company.html";
				}

			}
			
		} else if (type.equals("seeker")) {
			list = jobSeekerDao.PasswordLookUp(email);
			if (list.size() == 0) {
				model.addAttribute("message", message);
				
				return "login.html";
			} else {
				if (pwd.equals(list.get(0))) {
					List<Integer> jsl = new ArrayList<Integer>();
					jsl = jobSeekerDao.getUserIdFromEmail(email);
					JobSeeker js = jobSeekerDao.getJobSeeker(jsl.get(0));
					   
					model.addAttribute("seeker", js);
					return "User.html";
				}

			}
		}

		System.out.println(list);
		model.addAttribute("message", message);
		
		return "login.html";

	}

	@RequestMapping(value = "/register/verify", method = RequestMethod.GET)
	public String verification(@RequestParam("type") String type, @RequestParam("pin") int pin,
			@RequestParam("userId") int userId, Model model) {

		if (type.equals("seeker")) {

			JobSeeker j = jobSeekerDao.getJobSeeker(userId);
			if (j.getVerificationCode() == pin) {
				j.setVerified(true);
				jobSeekerDao.verify(j);
				model.addAttribute("seeker", j);
				return "userregister";
			} else {
				return "error";

			}

		} else {

			Company j = companyDao.getCompany(userId);
			if (j.getVerificationCode() == pin) {
				j.setVerified(true);
				companyDao.verify(j);
				model.addAttribute("company", j);
				return "companyregister";
			} else {
				return "error";
			}

		}



}
}
