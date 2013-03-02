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
		List<XuiteUser> u = findByApiKey(user.getApiKey());
		if (u != null && !u.isEmpty()) {
			delete(u);
		}
		xFileService.save(files);
	}
	
	public void saveOrUpdate(XuiteUser user){
		List<XuiteUser> u = findByApiKey(user.getApiKey());
		if (u != null && !u.isEmpty()) {
			delete(u);
		}
		save(user);
	}

	public List<XuiteUser> findByApiKey(String apiKey) {
		Query q = em
				.createQuery("select user from XuiteUser user where user.apiKey=?1");
		q.setParameter(1, apiKey);
		return q.getResultList();
	}
}
