/**
 * 
 */
package ggxuite.service;

import ggxuite.module.XuiteFile;
import ggxuite.module.XuiteUser;

import java.util.List;

/**
 * @author Rick
 * 
 */
public interface XuiteUserService extends AbstractService<XuiteUser, String> {
	void saveXuiteFiles(XuiteUser user, List<XuiteFile> files);

	XuiteUser findByApiKey(String apiKey);

	void saveOrUpdate(XuiteUser user, List<XuiteFile> oldfiles);
	
	void saveUserAndNewFiles(XuiteUser user, List<XuiteFile> newFiles);
}
