package at.vaeb.kv.ka.sample.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_rule_package")
public class RulePackage {
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "package_name")
	private String packageName;
	
	@Column(name = "description")
	private String description;

	public String getPackageName() {
		return packageName;
	}

	public String getDescription() {
		return description;
	}
}