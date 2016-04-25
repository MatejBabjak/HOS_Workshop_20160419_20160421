package at.vaeb.kv.ka.sample.persistence.dao;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.vaeb.kv.ka.sample.persistence.entity.DslrRule;

@Dependent
@Named("dslrRuleDao")
public class DslrRuleDaoImpl implements DslrRuleDao {

	@PersistenceContext(unitName = "VAEB_RULES")
	private EntityManager em;
	
	@Override
	public List<DslrRule> getAllRulesInPackage(String packageName) {
		TypedQuery<DslrRule> query = em.createQuery("select dr from DslrRule dr where dr.rulePackage.packageName = :packageName", DslrRule.class);
		query.setParameter("packageName", packageName);
		return query.getResultList();
	}
}