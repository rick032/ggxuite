/**
 * 
 */
package ggxuite.module;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * @author Rick
 * 
 */
@Entity
public class XuiteFile extends BaseEntity {
	
	private String xkey;
	private String parent;
	private String path;
	private String name;
	private BigInteger size;
	private Timestamp mtime;
	@ManyToOne(fetch = FetchType.EAGER)
	private XuiteUser user;

	public XuiteFile(String key, String parent, String path, String name,
			BigInteger size, Timestamp mtime, XuiteUser user) {
		this.xkey = key;
		this.parent = parent;
		this.path = path;
		this.name = name;
		this.size = size;
		this.mtime = mtime;
		this.user = user;
	}

	public String getXkey() {
		return xkey;
	}

	public void setXkey(String xkey) {
		this.xkey = xkey;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigInteger getSize() {
		return size;
	}

	public void setSize(BigInteger size) {
		this.size = size;
	}

	public Timestamp getMtime() {
		return mtime;
	}

	public void setMtime(Timestamp mtime) {
		this.mtime = mtime;
	}

	public XuiteUser getUser() {
		return user;
	}

	public void setUser(XuiteUser user) {
		this.user = user;
	}

}
