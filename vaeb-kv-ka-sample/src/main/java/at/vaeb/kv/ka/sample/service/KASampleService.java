package at.vaeb.kv.ka.sample.service;

import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import at.vaeb.kv.ka.sample.batch.Constants;

@WebService
public class KASampleService {
	@WebMethod
	public long start(@WebParam(name = "inputCsvFile") String inputCsvFile,
			@WebParam(name = "outputReportFile") String outputReportFile) {
		Properties jobParameters = new Properties();
		jobParameters.setProperty(Constants.PARAMETER_INPUT_CSV_FILE, inputCsvFile);
		jobParameters.setProperty(Constants.PARAMETER_OUTPUT_REPORT_FILE, outputReportFile);
		JobOperator jobOperator = BatchRuntime.getJobOperator();
		return jobOperator.start(Constants.JOB_NAME, jobParameters);
	}
}
