package ggxuite.module;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.beans.factory.annotation.Configurable;

import com.google.appengine.api.datastore.Key;

@Configurable
@MappedSuperclass
public abstract class BaseEntity implements Persistable {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}
	@Version
	protected long version;

	@Override
	public long getVersion() {
		return version;
	}

	@Override
	public boolean equals(Object that) {
		if (that == null) {
			return false;
		}
		if (this == that) {
			return true;
		}
		if (!(that instanceof BaseEntity)) {
			return false;
		}
		return equals((BaseEntity) that);
	}

	public boolean equals(BaseEntity that) {
		if (that == null) {
			return false;
		}
		if (this == that) {
			return true;
		}
		if (this.id == null) {
			return that.id == null;
		}
		return this.id.equals(that.id);
	}

}
