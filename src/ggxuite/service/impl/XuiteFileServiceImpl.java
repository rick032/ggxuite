/**
 * 
 */
package ggxuite.service.impl;

import ggxuite.module.XuiteFile;
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

}
