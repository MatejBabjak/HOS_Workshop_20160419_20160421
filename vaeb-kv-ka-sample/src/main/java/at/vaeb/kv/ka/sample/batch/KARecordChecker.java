package at.vaeb.kv.ka.sample.batch;

import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.vaeb.kv.ka.sample.dao.IcdCodeDao;
import at.vaeb.kv.ka.sample.record.KAInputRecord;
import at.vaeb.kv.ka.sample.record.ValidationResultCollector;

@Named("kaRecordChecker")
@Dependent
public class KARecordChecker implements ItemProcessor {

	private static Logger log = LoggerFactory.getLogger(KARecordChecker.class);
	
	private KieSession kSession;
	
	@Inject
	private IcdCodeDao icdCodeDao;
	
	public KARecordChecker() {
        KieServices ks = KieServices.Factory.get();
	    KieContainer kContainer = ks.getKieClasspathContainer();
    	kSession = kContainer.newKieSession("ksession-rules");
	}
	
	@Override
	public Object processItem(Object item) throws Exception {
		KAInputRecord inputRecord = (KAInputRecord)item;
		log.info(inputRecord.toString());
		ValidationResultCollector resultCollector = new ValidationResultCollector(inputRecord);

    	kSession.setGlobal("icdCodeDao", icdCodeDao);
		kSession.setGlobal("validationResultCollector", resultCollector);
		kSession.insert(inputRecord);
		kSession.fireAllRules();
		
		return resultCollector;
	}

	public void setIcdCodeDao(IcdCodeDao icdCodeDao) {
		this.icdCodeDao = icdCodeDao;
	}
}
