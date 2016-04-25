package at.vaeb.kv.ka.sample.rule.service;

import java.util.List;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message.Level;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.vaeb.kv.ka.sample.persistence.dao.DslExpanderDao;
import at.vaeb.kv.ka.sample.persistence.dao.DslrRuleDao;
import at.vaeb.kv.ka.sample.persistence.entity.DslExpander;
import at.vaeb.kv.ka.sample.persistence.entity.DslrRule;
import at.vaeb.kv.ka.sample.record.KAInputRecord;
import at.vaeb.kv.ka.sample.record.ValidationResultCollector;

@ApplicationScoped
@Named("kaRuleExecutor")
public class KARuleExecutorImpl implements KARuleExecutor {

	private DslrRuleDao dslrRuleDao;
	
	private DslExpanderDao dslExpanderDao;
	
	private String rulePackage;
	
	private KieSession kieSession = null;
	
	private static Logger log = LoggerFactory.getLogger(KARuleExecutorImpl.class);
	
	public KARuleExecutorImpl(String rulePackage, DslrRuleDao dslrRuleDao, DslExpanderDao dslExpanderDao) {
		this.rulePackage = rulePackage;
		this.dslrRuleDao = dslrRuleDao;
		this.dslExpanderDao = dslExpanderDao;
		
		initKieSession();	
	}

	private void initKieSession() {
		log.info(String.format("Constructing a Drools runtime for the rule package '%s'", rulePackage));
		
		List<DslrRule> dslrRules = dslrRuleDao.getAllRulesInPackage(rulePackage);
		List<DslExpander> dslExpanders = dslExpanderDao.getAllExpandersInPackage(rulePackage);
		
		log.info(String.format("%d DSLR rules and %d DSL expandes have been found in package '%s'", dslrRules.size(), dslExpanders.size(), rulePackage));
		
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		KieFileSystem kfs = ks.newKieFileSystem();
		
		for (DslExpander expander: dslExpanders) {
			kfs.write("src/main/resources/" + expander.getRulePackage().getPackageName() + "/" + expander.getExpanderName() + ".dsl", expander.getDefinition());
			//log.info(expander.getDefinition());
		}
		
		for (DslrRule rule: dslrRules) {
			kfs.write("src/main/resources/" + rule.getRulePackage().getPackageName() + "/" + rule.getRuleName() + ".dslr", rule.getDefinition());
			//log.info(rule.getDefinition());
		}
		
		KieBuilder kb = ks.newKieBuilder(kfs);
		kb.buildAll();
		
		if (kb.getResults().hasMessages(Level.ERROR)) {
			throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}

		KieContainer kieContainer = ks.newKieContainer(kr.getDefaultReleaseId());
		kieSession = kieContainer.newKieSession();
				
		//TODO: check, whether it would be better to keep the container and to open a new session for each check call			
	}
	
	@PreDestroy
	public void dispose() {
		if (kieSession != null) {
			kieSession.dispose();
		}
	}
	
	@Override
	public ValidationResultCollector validate(KAInputRecord inputRecord) {
		log.info("Validated object: " + inputRecord);
		
		kieSession.insert(inputRecord);
		ValidationResultCollector validationResult = new ValidationResultCollector(inputRecord);
		kieSession.setGlobal("validationResultCollector", validationResult);
		kieSession.fireAllRules();
		
		return validationResult;
	}

}
