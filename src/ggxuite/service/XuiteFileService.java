/**
 * 
 */
package ggxuite.service;

import java.util.List;

import ggxuite.module.XuiteFile;

/**
 * @author Rick
 * 
 */
public interface XuiteFileService extends AbstractService<XuiteFile, String> {
	List<XuiteFile> findByxKey(String xKey);
}
