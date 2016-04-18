package at.vaeb.kv.ka.sample.batch;

import javax.batch.api.chunk.ItemProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.vaeb.kv.ka.sample.record.KAInputRecord;

public class KARecordChecker implements ItemProcessor {

	private static Logger log = LoggerFactory.getLogger(KARecordChecker.class);
	
	@Override
	public Object processItem(Object item) throws Exception {
		KAInputRecord inputRecord = (KAInputRecord)item;
		log.info(inputRecord.toString());
		return inputRecord;
	}

}
