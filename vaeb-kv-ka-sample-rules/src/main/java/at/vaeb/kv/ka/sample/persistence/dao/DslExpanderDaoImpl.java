package at.vaeb.kv.ka.sample.persistence.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.vaeb.kv.ka.sample.persistence.entity.DslExpander;

public class DslExpanderDaoImpl implements DslExpanderDao {

	@PersistenceContext(unitName = "VAEB_RULES")
	private EntityManager em;
	
	@Override
	public List<DslExpander> getAllExpandersInPackage(String packageName) {
		TypedQuery<DslExpander> query = em.createQuery("select de from DslExpander de where de.rulePackage.packageName = :packageName", DslExpander.class);
		query.setParameter("packageName", packageName);
		return query.getResultList();
	}
}