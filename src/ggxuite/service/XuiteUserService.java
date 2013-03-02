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

	List<XuiteUser> findByApiKey(String apiKey);

	void saveOrUpdate(XuiteUser user);
}
