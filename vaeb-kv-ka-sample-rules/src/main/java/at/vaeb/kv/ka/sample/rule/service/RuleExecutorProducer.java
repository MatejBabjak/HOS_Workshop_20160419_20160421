package at.vaeb.kv.ka.sample.rule.service;

import java.lang.reflect.Field;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import at.vaeb.kv.ka.sample.persistence.dao.DslExpanderDao;
import at.vaeb.kv.ka.sample.persistence.dao.DslrRuleDao;

@Dependent
public class RuleExecutorProducer {
	
	@Inject 
	private DslrRuleDao dslRuleDao;
	
	@Inject
	private DslExpanderDao dslExpanderDao;
	
	@Produces
	public KARuleExecutor produceRuleExecutor(InjectionPoint injectionPoint) {
		Field field = (Field)injectionPoint.getMember();
		RuleExecutor annotation = field.getAnnotation(RuleExecutor.class);

		if (annotation == null) {
			throw new IllegalArgumentException("The rule executor has to be annotated with the 'RuleExecutor' annotation");
		}
		
		String rulePackage = annotation.rulePackage();
		if (rulePackage == null) {
			throw new IllegalArgumentException("The rule package has not been specified");
		}
		
		return new KARuleExecutorImpl(rulePackage, dslRuleDao, dslExpanderDao);
	}
}
