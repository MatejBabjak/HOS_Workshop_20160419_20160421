package at.vaeb.kv.ka.sample.batch;

import java.util.List;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

@Named("kaReportFileWriter")
@Dependent
public class KAReportFileWriter extends AbstractItemWriter {

	@Inject
	@BatchProperty
	private String outputReportFile;
	
	@Override
	public void writeItems(List<Object> items) throws Exception {
		// TODO Auto-generated method stub		
	}

}
