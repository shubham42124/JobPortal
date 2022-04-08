package com.jp.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
@Transactional
public class UserDaoImpl implements UserDao
{
	@PersistenceContext
	private EntityManager entityManager;

}
