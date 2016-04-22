package at.vaeb.kv.ka.sample.persistence.dao;

import java.util.List;

import at.vaeb.kv.ka.sample.persistence.entity.DslExpander;

public interface DslExpanderDao {
	public List<DslExpander> getAllExpandersInPackage(String packageName);
}
