package com.jp.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.jp.entity.Interested;

@Repository
@Transactional
@Service
public class InterestedDaoImpl implements InterestedDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Interested createInterest(Interested c) throws Exception {
		try {
			entityManager.persist(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}

	@Override
	public Interested getInterest(int id) {
		Interested is = null;

		is = entityManager.find(Interested.class, id);

		return is;
	}

	@Override
	public boolean deleteInterest(int id) {
		Interested i = getInterest(id);
		if (i != null) {
			entityManager.remove(i);
		} else {
			return false;
		}
		return true;
	}
	
	public List<?> getInterestedJobId(int jobId, int userId) {
		Query query = entityManager.createQuery("SELECT ID FROM Interested jd WHERE jd.jobId = :jobid and jd.jobSeekerId =:userid");
		query.setParameter("jobid", jobId);
		query.setParameter("userid", userId);
		List<?> querylist = query.getResultList();
		return querylist;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getAllInterestedJobId(int userId) {
		Query query = entityManager.createQuery("SELECT jobId FROM Interested jd WHERE jd.jobSeekerId =:userid");
		query.setParameter("userid", userId);
		List<Integer> querylist = query.getResultList();
		return querylist;
	}

}
