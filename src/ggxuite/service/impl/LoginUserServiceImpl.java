/**
 * 
 */
package ggxuite.service.impl;

import ggxuite.module.LoginUser;
import ggxuite.service.LoginUserService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * @author Rick
 * 
 */
public class LoginUserServiceImpl extends
		AbstractServiceImpl<LoginUser, String> implements LoginUserService {

	public LoginUserServiceImpl() {
		super(LoginUser.class);
	}

	public LoginUserServiceImpl(EntityManager em) {
		super(LoginUser.class, em);
	}

	@Override
	public LoginUser findByUserId(String userId) {
		try {
			Query q = em
					.createQuery("select user from LoginUser user where user.userId=?1");
			q.setParameter(1, userId);
			return (LoginUser) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
