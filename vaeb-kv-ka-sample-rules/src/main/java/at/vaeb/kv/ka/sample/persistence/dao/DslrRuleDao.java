package at.vaeb.kv.ka.sample.persistence.dao;

import java.util.List;

import at.vaeb.kv.ka.sample.persistence.entity.DslrRule;

public interface DslrRuleDao {
	public List<DslrRule> getAllRulesInPackage(String packageName);
}
