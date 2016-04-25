package at.vaeb.kv.ka.sample.rule.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Calendar;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import at.vaeb.kv.ka.sample.record.KAInputRecord;
import at.vaeb.kv.ka.sample.record.ValidationResultCollector;

@RunWith(Arquillian.class)
public class KARuleExecutorTest {
	@Inject
	@RuleExecutor(rulePackage = "at.vaeb.kv.rules.test")
	KARuleExecutor ruleExecutor;

	@Deployment
	public static WebArchive createDeployment() {
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
				.resolve("org.kie:kie-api", "org.beanio:beanio", "org.drools:drools-compiler", "org.slf4j:slf4j-simple", "org.apache.commons:commons-lang3")
				.withTransitivity().asFile();

		WebArchive war = ShrinkWrap.create(WebArchive.class, "KARuleExecutorTest.war").addAsLibraries(files)
				.addPackages(true, "at.vaeb.kv").addAsResource("META-INF/beans.xml")
				.addAsResource("persistence.xml", "META-INF/persistence.xml");

		return war;
	}
	
	@Test
	public void testAllOK() {				
		KAInputRecord kaInputRecord = new KAInputRecord();
		kaInputRecord.setVorname("Johannes");
		kaInputRecord.setNachname("Menzel");
		Calendar calVon = Calendar.getInstance();
		calVon.set(2015, 0, 1);
		kaInputRecord.setDatumVon(calVon.getTime());
		Calendar calBis = Calendar.getInstance();
		calBis.set(2015, 0, 10);
		kaInputRecord.setDatumBis(calBis.getTime());
		kaInputRecord.setDiagnoseIcdCode("A.00");
		
		assertNotNull(ruleExecutor);
		ValidationResultCollector validationResult = ruleExecutor.validate(kaInputRecord);
		
		assertNotNull(validationResult);
		assertEquals(0, validationResult.getErrorCount());
		assertEquals(0, validationResult.getWarningCount());
	}	
	
	@Test
	public void testNoVorname() {				
		KAInputRecord kaInputRecord = new KAInputRecord();
		kaInputRecord.setNachname("Menzel");
		Calendar calVon = Calendar.getInstance();
		calVon.set(2015, 0, 1);
		kaInputRecord.setDatumVon(calVon.getTime());
		Calendar calBis = Calendar.getInstance();
		calBis.set(2015, 0, 10);
		kaInputRecord.setDatumBis(calBis.getTime());
		kaInputRecord.setDiagnoseIcdCode("A.00");
		
		assertNotNull(ruleExecutor);
		ValidationResultCollector validationResult = ruleExecutor.validate(kaInputRecord);
		
		assertNotNull(validationResult);
		assertEquals(0, validationResult.getErrorCount());
		assertEquals(1, validationResult.getWarningCount());
		assertEquals("Der Spitalsaufenthalt beinhaltet keinen Vornamen", validationResult.getWarnings().get(0));
	}
	
	@Test
	public void testNoNachname() {				
		KAInputRecord kaInputRecord = new KAInputRecord();
		kaInputRecord.setVorname("Johannes");
		Calendar calVon = Calendar.getInstance();
		calVon.set(2015, 0, 1);
		kaInputRecord.setDatumVon(calVon.getTime());
		Calendar calBis = Calendar.getInstance();
		calBis.set(2015, 0, 10);
		kaInputRecord.setDatumBis(calBis.getTime());
		kaInputRecord.setDiagnoseIcdCode("A.00");
		
		assertNotNull(ruleExecutor);
		ValidationResultCollector validationResult = ruleExecutor.validate(kaInputRecord);
		
		assertNotNull(validationResult);
		assertEquals(1, validationResult.getErrorCount());
		assertEquals("Der Spitalsaufenthalt beinhaltet keinen Nachnamen", validationResult.getErrors().get(0));
		assertEquals(0, validationResult.getWarningCount());
	}
	
	@Test
	public void testNoDatumVon() {				
		KAInputRecord kaInputRecord = new KAInputRecord();
		kaInputRecord.setVorname("Johannes");
		kaInputRecord.setNachname("Menzel");
		Calendar calBis = Calendar.getInstance();
		calBis.set(2015, 0, 10);
		kaInputRecord.setDatumBis(calBis.getTime());
		kaInputRecord.setDiagnoseIcdCode("A.00");
		
		assertNotNull(ruleExecutor);
		ValidationResultCollector validationResult = ruleExecutor.validate(kaInputRecord);
		
		assertNotNull(validationResult);
		assertEquals(1, validationResult.getErrorCount());
		assertEquals("Der Spitalsaufenthalt beinhaltet kein Datum von", validationResult.getErrors().get(0));
		assertEquals(1, validationResult.getWarningCount());
		assertEquals("Der Spitalsaufenthalt soll maximal 30 Tage dauern", validationResult.getWarnings().get(0));
	}
	
	@Test
	public void testNachnameShorterThan5Chars() {				
		KAInputRecord kaInputRecord = new KAInputRecord();
		kaInputRecord.setVorname("Johannes");
		kaInputRecord.setNachname("Men");
		Calendar calVon = Calendar.getInstance();
		calVon.set(2015, 0, 1);
		kaInputRecord.setDatumVon(calVon.getTime());
		Calendar calBis = Calendar.getInstance();
		calBis.set(2015, 0, 10);
		kaInputRecord.setDatumBis(calBis.getTime());
		kaInputRecord.setDiagnoseIcdCode("A.00");
		
		assertNotNull(ruleExecutor);
		ValidationResultCollector validationResult = ruleExecutor.validate(kaInputRecord);
		
		assertNotNull(validationResult);
		assertEquals(1, validationResult.getErrorCount());
		assertEquals("Der Nachname ist kuerzer als 5 Zeichen", validationResult.getErrors().get(0));
		assertEquals(0, validationResult.getWarningCount());
	}
	
	@Test
	public void testAufenthaltLongerThan30Days() {				
		KAInputRecord kaInputRecord = new KAInputRecord();
		kaInputRecord.setVorname("Johannes");
		kaInputRecord.setNachname("Menzel");
		Calendar calVon = Calendar.getInstance();
		calVon.set(2015, 0, 1);
		kaInputRecord.setDatumVon(calVon.getTime());
		Calendar calBis = Calendar.getInstance();
		calBis.set(2015, 3, 10);
		kaInputRecord.setDatumBis(calBis.getTime());
		kaInputRecord.setDiagnoseIcdCode("A.00");
		
		assertNotNull(ruleExecutor);
		ValidationResultCollector validationResult = ruleExecutor.validate(kaInputRecord);
		
		assertNotNull(validationResult);
		assertEquals(0, validationResult.getErrorCount());		
		assertEquals(1, validationResult.getWarningCount());
		assertEquals("Der Spitalsaufenthalt soll maximal 30 Tage dauern", validationResult.getWarnings().get(0));
	}	
}
