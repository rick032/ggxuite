/**
 * 
 */
package ggxuite.service.impl;

import ggxuite.module.XuiteFile;
import ggxuite.module.XuiteUser;
import ggxuite.service.XuiteFileService;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * @author Rick
 * 
 */
public class XuiteFileServiceImpl extends
		AbstractServiceImpl<XuiteFile, String> implements XuiteFileService {

	public XuiteFileServiceImpl() {
		super(XuiteFile.class);
	}

	public XuiteFileServiceImpl(EntityManager em) {
		super(XuiteFile.class, em);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<XuiteFile> findByxKey(String xKey) {
		Query q = em
				.createQuery("select file from XuiteFile file where file.xkey=?1");
		q.setParameter(1, xKey);
		return q.getResultList();
	}

	@Override
	public int updateShortLink(List<XuiteFile> files) {

		int count = 0;
		for (XuiteFile file : files) {
			Query q = em
					.createQuery("UPDATE XuiteFile x SET x.shortLink = ?1 WHERE x.id = ?2");
			q.setParameter(1, file.getShortLink());
			q.setParameter(2, file.getId());
			count += q.executeUpdate();
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<XuiteFile> findByxUser(XuiteUser xUser) {
		Query q = em
				.createQuery("select file from XuiteFile file where file.user=?1");
		q.setParameter(1, xUser);
		return q.getResultList();
	}

	@Override
	public int deleteByXUser(XuiteUser xUser) {
		Query q = em.createQuery("delete from XuiteFile x where x.user=?1");
		q.setParameter(1, xUser);		
		return q.executeUpdate(); 
	}

}
