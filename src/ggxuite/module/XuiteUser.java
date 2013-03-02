/**
 * 
 */
package ggxuite.module;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 * @author Rick
 * 
 */
@Entity
public class XuiteUser extends BaseEntity {

	private String apiKey;

	private String secretKey;

	private String oAuth;

	private Timestamp updateTime;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<XuiteFile> files;

	public XuiteUser(String apiKey, String secretKey) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.updateTime = new Timestamp(System.currentTimeMillis());
	}

	public XuiteUser(String apiKey, String secretKey, String oAuth) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.oAuth = oAuth;
		this.updateTime = new Timestamp(System.currentTimeMillis());
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getoAuth() {
		return oAuth;
	}

	public void setoAuth(String oAuth) {
		this.oAuth = oAuth;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public List<XuiteFile> getFiles() {
		return files;
	}

	public void setFiles(List<XuiteFile> files) {
		this.files = files;
	}

}