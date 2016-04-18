package at.vaeb.kv.ka.sample.batch;

import java.io.File;
import java.io.Serializable;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.beanio.builder.StreamBuilder;

import at.vaeb.kv.ka.sample.record.KAInputRecord;

@Named("kaCsvFileReader")
@Dependent
public class KACsvFileReader extends AbstractItemReader {
	@Inject
	@BatchProperty
	private String inputCsvFile;

	private BeanReader reader = null;
	
	@Override
	public void open(Serializable checkpoint) throws Exception {
	    StreamFactory factory = StreamFactory.newInstance();	    
	    StreamBuilder builder = new StreamBuilder("TestFile")
	        .format("fixedlength")
	        .addRecord(KAInputRecord.class);
	    factory.define(builder);	    
	    
		File inFile = new File(inputCsvFile);
		reader = factory.createReader("TestFile", inFile);
	}
	
	@Override
	public Object readItem() throws Exception {
		return reader.read();
	}
	
	@Override
	public void close() throws Exception {
		reader.close();
	}
}
