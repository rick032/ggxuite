/**
 * 
 */
package ggxuite.controller;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import org.springframework.stereotype.Controller;

/**
 * @author Rick
 * 
 */
@Controller
public abstract class GgXuiteAbstractController {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	GgXuiteAbstractController() {
	}

	protected PersistenceManagerFactory getJDO() {
		return pmfInstance;
	}

}
