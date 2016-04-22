package at.vaeb.kv.ka.sample.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_dslr_rule")
public class DslrRule {
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "definition")
	private String definition;
	
	@Column(name = "rule_name")
	private String ruleName;
	
	@ManyToOne
	@JoinColumn(name = "rule_package_id")
	private RulePackage rulePackage;

	public Long getId() {
		return id;
	}

	public String getDefinition() {
		return definition;
	}

	public RulePackage getRulePackage() {
		return rulePackage;
	}
	
	public String getRuleName() {
		return ruleName;
	}
}