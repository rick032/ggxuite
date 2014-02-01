/**
 * 
 */
package ggxuite.service;

import java.util.List;

import ggxuite.module.XuiteFile;
import ggxuite.module.XuiteUser;

/**
 * @author Rick
 * 
 */
public interface XuiteFileService extends AbstractService<XuiteFile, String> {
	List<XuiteFile> findByxKey(String xKey);
	
	int updateShortLink(List<XuiteFile> files);
	
	List<XuiteFile> findByxUser(XuiteUser xUser);

	int deleteByXUser(XuiteUser oldXuiteUser);
}
