package at.vaeb.kv.ka.sample.batch;

import java.util.List;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.vaeb.kv.ka.sample.record.ValidationResultCollector;

@Named("kaReportFileWriter")
@Dependent
public class KAReportFileWriter extends AbstractItemWriter {

	private static Logger log = LoggerFactory.getLogger(KAReportFileWriter.class);
	
	@Inject
	@BatchProperty
	private String outputReportFile;
	
	@Override
	public void writeItems(List<Object> items) throws Exception {
		for (Object item: items) {
			ValidationResultCollector collector = (ValidationResultCollector)item;
			log.info(collector.toString());
		}
	}

}
