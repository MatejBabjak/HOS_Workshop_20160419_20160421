package at.vaeb.kv.ka.sample.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import at.vaeb.kv.ka.sample.entity.IcdCode;

@Named("icdCodeDao")
@ApplicationScoped
public class IcdCodeDaoImpl implements IcdCodeDao {

	@PersistenceContext(unitName = "VAEB_KV")
	private EntityManager em;

	public IcdCodeDaoImpl() {
		super();
	}
	
	@Override
	public boolean existsIcd10Code(String icd10Code) {
		return getIcdCodeByKuerzel(icd10Code) != null;
	}

	private IcdCode getIcdCodeByKuerzel(String icd10Code) {
		TypedQuery<IcdCode> query = em.createQuery("select i from IcdCode i where i.kuerzel = :icd10Code", IcdCode.class);
		query.setParameter("icd10Code", icd10Code);
		List<IcdCode> resultList = query.getResultList();

		if (resultList.size() > 1) {
			throw new IllegalArgumentException(String.format("More than one ICD10 code has been found for %s", icd10Code));
		}
		
		return resultList.size() == 1 ? resultList.get(0) : null; 
	}
}