package at.vaeb.kv.ka.sample.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "t_dsl_expander")
public class DslExpander {
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "expander_name")
	private String expanderName;
	
	@Column(name = "definition")
	private String definition;
	
	@ManyToOne
	@JoinColumn(name = "rule_package_id")
	private RulePackage rulePackage;

	public Long getId() {
		return id;
	}

	public String getExpanderName() {
		return expanderName;
	}

	public String getDefinition() {
		return definition;
	}

	public RulePackage getRulePackage() {
		return rulePackage;
	}	
}
