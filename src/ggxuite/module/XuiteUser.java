/**
 * 
 */
package ggxuite.module;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Rick
 * 
 */
@Entity
public class XuiteUser extends BaseEntity {

	private String apiKey;

	private String secretKey;

	private String oAuth;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;

	private String sourceIP;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<XuiteFile> files;

	public XuiteUser(String apiKey, String secretKey) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.lastUpdate = new Date(System.currentTimeMillis());
	}

	public XuiteUser(String apiKey, String secretKey, String oAuth) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.oAuth = oAuth;
		this.lastUpdate = new Date(System.currentTimeMillis());
	}

	public XuiteUser(String apiKey, String secretKey, String oAuth,
			String remoteHost) {
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.oAuth = oAuth;
		this.sourceIP = remoteHost;
		this.lastUpdate = new Date(System.currentTimeMillis());
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

	public List<XuiteFile> getFiles() {
		return files;
	}

	public void setFiles(List<XuiteFile> files) {
		this.files = files;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	
}