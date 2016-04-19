package at.vaeb.kv.ka.sample.record;

import java.util.LinkedList;
import java.util.List;

public class ValidationResultCollector {
	private KAInputRecord validatedRecord;
	private List<String> warnings = new LinkedList<String>();
	private List<String> errors = new LinkedList<String>();
	
	public ValidationResultCollector(KAInputRecord validatedRecord) {
		this.validatedRecord = validatedRecord;
	}
	
	public void addError(String error) {
		errors.add(error);
	}
	
	public void addWarning(String warning) {
		warnings.add(warning);
	}
	
	public int getErrorCount() {
		return errors.size();
	}
	
	public int getWarningCount() {
		return warnings.size();
	}
	
	public boolean hasErrors() {
		return getErrorCount() > 0;
	}
	
	public boolean hasWarnings() {
		return getWarningCount() > 0;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("ValidationResultCollector(");
		builder.append("validatedRecord = ").append(validatedRecord.toString()).append(", ");
		builder.append("errors = ").append(errors.toString()).append(", ");
		builder.append("warnings = ").append(warnings.toString());
		builder.append(")");
		
		return builder.toString();
	}
}
