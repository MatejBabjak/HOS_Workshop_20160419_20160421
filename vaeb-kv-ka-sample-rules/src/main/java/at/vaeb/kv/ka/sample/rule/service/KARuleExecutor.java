package at.vaeb.kv.ka.sample.rule.service;

import at.vaeb.kv.ka.sample.record.KAInputRecord;
import at.vaeb.kv.ka.sample.record.ValidationResultCollector;

public interface KARuleExecutor {
	public ValidationResultCollector validate(KAInputRecord inputRecord);
}
