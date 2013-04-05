/**
 * 
 */
package ggxuite.module;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.appengine.api.datastore.Key;

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
	private String shortLink;
	private BigInteger size;
	@Temporal(TemporalType.TIMESTAMP)
	private Date mtime;
	
	@ManyToOne(fetch = FetchType.LAZY)	
	@JoinColumn(name="ID")
	private XuiteUser user;

	public XuiteFile(Key id,String key, String parent, String path, String name,
			BigInteger size, Date mtime, XuiteUser user) {
		setId(id);
		this.xkey = key;
		this.parent = parent;
		this.path = path;
		this.name = name;
		this.size = size;
		this.mtime = mtime;
		this.user = user;
	}

	public XuiteFile(XuiteFile xuiteFile) {
		this.xkey = xuiteFile.getXkey();
		this.parent = xuiteFile.getParent();
		this.path = xuiteFile.getPath();
		this.name = xuiteFile.getName();
		this.size = xuiteFile.getSize();
		this.mtime = xuiteFile.getMtime();
		this.user = xuiteFile.getUser();
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

	@Basic
	public XuiteUser getUser() {
		return user;
	}

	public void setUser(XuiteUser user) {
		this.user = user;
	}

	public Date getMtime() {
		return mtime;
	}

	public void setMtime(Date mtime) {
		this.mtime = mtime;
	}

	public String getShortLink() {
		return shortLink;
	}

	public void setShortLink(String shortLink) {
		this.shortLink = shortLink;
	}
	
}
