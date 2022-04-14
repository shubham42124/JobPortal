package com.jp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.jp.entity.Company;
import com.jp.entity.JobPosting;

@Repository
@Service
@Transactional
public class JobPostingDaoImpl implements JobPostingDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public JobPosting createJobPosting(JobPosting job, int cid) {
		try {
			System.out.println("1");
			Company c = entityManager.find(Company.class, cid);
			job.setCompany(c);
			System.out.println("2");
			entityManager.persist(job);
			System.out.println("3");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return job;
	}

	@Override
	public JobPosting getJobPosting(int id) {
		JobPosting j = null;

		j = entityManager.find(JobPosting.class, id);

		return j;
	}

	@Override
	public boolean deleteJobPosting(int id) {
		JobPosting p = getJobPosting(id);
		if (p != null) {
			entityManager.remove(p);
		} else {
			return false;
		}
		return true;
	}

	@Override
	public JobPosting updateJobPosting(JobPosting p) {

		JobPosting p1 = getJobPosting(p.getJobId());
		p1.setDescription(p.getDescription());
		p1.setLocation(p.getLocation());
		p1.setResponsibilities(p.getResponsibilities());
		p1.setSalary(p.getSalary());
		p1.setCompanyName(p.getCompanyName());
		p1.setTitle(p.getTitle());
		try {
			if (p1 != null) {
				entityManager.merge(p1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p1;
	}

	@Override
	public List<JobPosting> getAllJobPosting() {
		TypedQuery<JobPosting> result = entityManager.createQuery("select e from JobPosting e", JobPosting.class);
		return result.getResultList();
	
	}

}
