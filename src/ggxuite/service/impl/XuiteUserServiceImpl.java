/**
 * 
 */
package ggxuite.service.impl;

import ggxuite.module.XuiteFile;
import ggxuite.module.XuiteUser;
import ggxuite.service.XuiteFileService;
import ggxuite.service.XuiteUserService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rick
 * 
 */
public class XuiteUserServiceImpl extends
		AbstractServiceImpl<XuiteUser, String> implements XuiteUserService {
	@Autowired
	XuiteFileService xFileService;

	public XuiteUserServiceImpl() {
		super(XuiteUser.class);
	}

	public XuiteUserServiceImpl(EntityManager em) {
		super(XuiteUser.class, em);
	}

	@Override
	public void saveXuiteFiles(XuiteUser user, List<XuiteFile> files) {
		XuiteUser u = findByApiKey(user.getApiKey());
		if (u != null) {
			delete(u);
		}
		xFileService.save(files);
	}
	
	public void saveOrUpdate(XuiteUser user, List<XuiteFile> oldFiles) {
		if (oldFiles != null) {
			xFileService.delete(oldFiles);
		}
		save(user);
	}
	
	public void saveUserAndNewFiles(XuiteUser user, List<XuiteFile> newFiles) {
		if(newFiles !=null){
			xFileService.save(newFiles);
		}
		save(user);
	}

	public XuiteUser findByApiKey(String apiKey) {
		try {
			Query q = em
					.createQuery("select user from XuiteUser user where user.apiKey=?1");
			q.setParameter(1, apiKey);
		return (XuiteUser) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}
