/**
 * 
 */
package ggxuite.service;

import ggxuite.module.LoginUser;

/**
 * @author Rick
 * 
 */
public interface LoginUserService extends AbstractService<LoginUser, String> {
	LoginUser findByUserId(String userId);
}
