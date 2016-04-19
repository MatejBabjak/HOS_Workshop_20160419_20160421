package at.vaeb.kv.ka.sample.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.List;

import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.vaeb.kv.ka.sample.record.KAInputRecord;
import at.vaeb.kv.ka.sample.record.ValidationResultCollector;

@Named("kaReportFileWriter")
@Dependent
public class KAReportFileWriter extends AbstractItemWriter {

	private static Logger log = LoggerFactory.getLogger(KAReportFileWriter.class);
	
	private BufferedWriter writer = null;
	
	@Inject
	@BatchProperty
	private String outputReportFile;
	
	@Override
	public void open(Serializable checkpoint) throws Exception {
		super.open(checkpoint);
		File outputFile = new File(outputReportFile);
		if (outputFile.exists()) {
			outputFile.delete();
		}
		writer = new BufferedWriter(new FileWriter(outputFile));
		
		//write the header
		writer.append("<table border=\"1\"><tr><td>Datum von</td><td>Datum bis</td><td>Vorname</td><td>Nachname</td><td>Diagnose</td><td>Fehler</td><td>Warnung</td></tr>");
	}
	
	@Override
	public void writeItems(List<Object> items) throws Exception {
		for (Object item: items) {
			ValidationResultCollector collector = (ValidationResultCollector)item;
			KAInputRecord validatedRecord = collector.getValidatedRecord();
			writer.append("<tr><td>");
			writer.append(validatedRecord.getDatumVon() != null ? validatedRecord.getDatumVon().toString() : "&nbsp;");
			writer.append("</td><td>");
			writer.append(validatedRecord.getDatumBis() != null ? validatedRecord.getDatumBis().toString() : "&nbsp;");
			writer.append("</td><td>");
			writer.append(validatedRecord.getVorname());
			writer.append("</td><td>");
			writer.append(validatedRecord.getNachname());
			writer.append("</td><td>");
			writer.append(validatedRecord.getDiagnoseIcdCode());
			writer.append("</td><td>");
			if (collector.hasErrors()) {
				writer.append("<ul>");
			}
			for (String error: collector.getErrors()) {
				writer.append("<li>");
				writer.append(error);
				writer.append("</li>");
			}
			if (collector.hasErrors()) {
				writer.append("</ul>");
			}			
			writer.append("</td><td>");
			if (collector.hasWarnings()) {
				writer.append("<ul>");
			}
			for (String warning: collector.getWarnings()) {
				writer.append("<li>");
				writer.append(warning);
				writer.append("</li>");
			}
			if (collector.hasWarnings()) {
				writer.append("</ul>");
			}	
			writer.append("</td></tr>");
			
			log.info(collector.toString());
		}
	}

	@Override
	public void close() throws Exception {
		if (writer != null) {			
			writer.append("</table>");
			writer.flush();
			writer.close();
		}
	}
}
