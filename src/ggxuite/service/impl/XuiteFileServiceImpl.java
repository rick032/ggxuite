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

	@Override
	public List<XuiteFile> findByxKey(String xKey) {
		Query q = em
				.createQuery("select file from XuiteFile file where file.xKey=?1");
		q.setParameter(1, xKey);
		return q.getResultList();
	}

}
