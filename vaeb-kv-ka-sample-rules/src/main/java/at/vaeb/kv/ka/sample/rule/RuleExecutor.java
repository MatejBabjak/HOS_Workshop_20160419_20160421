package at.vaeb.kv.ka.sample.rule;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message.Level;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import at.vaeb.kv.ka.sample.record.KAInputRecord;
import at.vaeb.kv.ka.sample.record.ValidationResultCollector;

public class RuleExecutor {

	private static final String RULE_DSL = "[condition][KAInputRecord] Der Spitalsaufenthalt=$kaRecord:KAInputRecord()\n"
			+ "[condition][KAInputRecord] - beinhaltet keinen Vornamen=eval(StringUtils.isBlank($kaRecord.getVorname()))\n"
			+ "[condition][KAInputRecord] - beinhaltet keinen Nachnamen=eval(StringUtils.isBlank($kaRecord.getNachname()))\n"
			+ "[condition][KAInputRecord] - beinhaltet kein Datum von=eval($kaRecord.getDatumVon() == null)\n"
			+ "[condition][KAInputRecord] - der Nachname ist kuerzer as {minNachnameLength} Zeichen=eval(!StringUtils.isBlank($kaRecord.getNachname()) && $kaRecord.getNachname().length() < {minNachnameLength})\n"
			+ "[condition][KAInputRecord] - laenger als {maxAufenthaltLength} Tage ist=eval($kaRecord.getAufenthaltsdauer() > {maxAufenthaltLength})\n"
			+ "[condition][KAInputRecord] - die Diagnose ist kein gueltiger ICD10 Code=eval(!icdCodeDao.existsIcd10Code($kaRecord.getDiagnoseIcdCode()))\n"
			+ "[consequence][]Warnung {text}=validationResultCollector.addWarning(\"{text}\");\n"
			+ "[consequence][]Fehler {text}=validationResultCollector.addError(\"{text}\");\n"
			+ "[consequence][]Ignorieren=";
	
	private static final String RULE_DSLR =
			 "package at.vaeb.kv.ka.sample\n"
					 + "\n"
					 + "import at.vaeb.kv.ka.sample.record.KAInputRecord;\n"
					 + "import at.vaeb.kv.ka.sample.record.ValidationResultCollector;\n"
					 + "/*import at.vaeb.kv.ka.sample.dao.IcdCodeDao;*/\n"
					 + "\n"
					 + "import org.apache.commons.lang3.StringUtils;\n"
					 + "import java.time.LocalDate;\n"
					 + "\n"
					 + "global ValidationResultCollector validationResultCollector;\n"
					 + "/*global IcdCodeDao icdCodeDao;*/\n"
					 + "\n"
					 //+ "/*expander KA.dsl*/\n"
					 //+ "\n"
					 + "rule \"Person soll einen Vornamen haben\"\n"
					 + "	when\n"
					 + "		Der Spitalsaufenthalt\n"
					 + "		- beinhaltet keinen Vornamen\n"
					 + "	then\n"
					 + "		Warnung Der Spitalsaufenthalt beinhaltet keinen Vornamen\n"
					 + "end\n"
					 + "\n"
					 + "rule \"Person muss einen Nachnamen haben\"\n"
					 + "	when\n"
					 + "		Der Spitalsaufenthalt\n"
					 + "		- beinhaltet keinen Nachnamen\n"
					 + "	then\n"
					 + "		Fehler Der Spitalsaufenthalt beinhaltet keinen Nachnamen\n"
					 + "end\n"
					 + "\n"
					 + "rule \"Spitalsaufenthalt muss ein Datum von beinhalten\"\n"
					 + "	when\n"
					 + "		Der Spitalsaufenthalt\n"
					 + "		- beinhaltet kein Datum von\n"
					 + "	then\n"
					 + "		Fehler Der Spitalsaufenthalt beinhaltet kein Datum von\n"
					 + "end\n"
					 + "\n"
					 + "rule \"Der Nachname muss mindestens 5 Zeichen lang sein\"\n"
					 + "	when\n"
					 + "		Der Spitalsaufenthalt\n"
					 + "		- der Nachname ist kuerzer as 5 Zeichen\n"
					 + "	then\n"
					 + "		Fehler Der Nachname ist kuerzer als 5 Zeichen\n"
					 + "end\n"
					 + "\n"
					 + "rule \"Der Aufenthalt darf maximal 30 Tage dauern\"\n"
					 + "	when\n"
					 + "		Der Spitalsaufenthalt\n"
					 + "		- laenger als 30 Tage ist\n"
					 + "	then\n"
					 + "		Warnung Der Spitalsaufenthalt soll maximal 30 Tage dauern\n"
					 + "end\n"
					 + "\n"
					 + "/*rule \"Die Diagnose ist kein gueltiger ICD10 Code\"\n"
					 + "	when\n"
					 + "		Der Spitalsaufenthalt\n"
					 + "		- die Diagnose ist kein gueltiger ICD10 Code\n"
					 + "	then\n"
					 + "		Fehler Die Diagnose ist kein gueltiger ICD10 Code\n"
					 + "end*/\n";			

	static KieContainer kContainer;

	static {
		KieServices ks = KieServices.Factory.get();
		KieRepository kr = ks.getRepository();
		KieFileSystem kfs = ks.newKieFileSystem();

		kfs.write("src/main/resources/at/vaeb/kv/ka/sample/KA.dsl", RULE_DSL);
		kfs.write("src/main/resources/at/vaeb/kv/ka/sample/KA.dslr", RULE_DSLR);

		KieBuilder kb = ks.newKieBuilder(kfs);

		kb.buildAll(); // kieModule is automatically deployed to KieRepository
						// if successfully built.
		if (kb.getResults().hasMessages(Level.ERROR)) {
			throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
		}

		kContainer = ks.newKieContainer(kr.getDefaultReleaseId());
	}

	public static ValidationResultCollector validate(KAInputRecord inputRecord) {
		KieSession kSession = kContainer.newKieSession();
		ValidationResultCollector validationResultCollector = new ValidationResultCollector(inputRecord);
		kSession.setGlobal("validationResultCollector", validationResultCollector);
		kSession.insert(inputRecord);
		kSession.fireAllRules();
		kSession.dispose();
		return validationResultCollector;
	}

	public static String getKAInputRecordDSL() {
		return "[condition][]Der Spitalsaufenthalt beinhaltet eine Person=eval(true)\n"
				+ "[consequence][]print=System.out.println(\"print\");";
		/*
		 * return
		 * "[condition][KAInputRecord] Der Spitalsaufenthalt=$kaRecord:KAInputRecord "
		 * +
		 * "[condition][KAInputRecord] - beinhaltet keinen Vornamen=eval(StringUtils.isBlank($kaRecord.getVorname())) "
		 * +
		 * "[condition][KAInputRecord] - beinhaltet keinen Nachnamen=eval(StringUtils.isBlank($kaRecord.getNachname())) "
		 * +
		 * "[condition][KAInputRecord] - beinhaltet kein Datum von=eval($kaRecord.getDatumVon() == null) "
		 * +
		 * "[condition][KAInputRecord] - der Nachname ist kuerzer as {minNachnameLength} Zeichen=eval(!StringUtils.isBlank($kaRecord.getNachname()) && $kaRecord.getNachname().length() < {minNachnameLength}) "
		 * +
		 * "[condition][KAInputRecord] - laenger als {maxAufenthaltLength} Tage ist=eval($kaRecord.getAufenthaltsdauer() > {maxAufenthaltLength}) "
		 * +
		 * "[consequence][]Warnung {text}=validationResultCollector.addWarning(\"{text}\"); "
		 * +
		 * "[consequence][]Fehler {text}=validationResultCollector.addError(\"{text}\"); "
		 * + "[consequence][]Ignorieren= ";
		 */
	}

	public static String getKAInputRecordDSLR() {
		return "package at.vaeb.kv.ka.sample\nimport at.vaeb.kv.ka.sample.record.KAInputRecord\nexpander KA.dsl\n"
				// + " rule \"test1\" when Der Spitalsaufenthalt beinhaltet eine
				// Person namens \"Johannes\" then
				// System.out.println(\"Johannes\"); end"
				+ "rule \"test2\"\n when\n Der Spitalsaufenthalt beinhaltet eine Person\n then\n print\n end";
	}
	/*
	 * return "package at.vaeb.kv.ka.sample " + " " +
	 * "import at.vaeb.kv.ka.sample.record.KAInputRecord; " +
	 * "import at.vaeb.kv.ka.sample.record.ValidationResultCollector; " + " " +
	 * "import org.apache.commons.lang3.StringUtils; " +
	 * "import java.time.LocalDate; " + " " +
	 * "global ValidationResultCollector validationResultCollector; " + " " +
	 * "expander KA.dsl " + " " + "rule \"Person soll einen Vornamen haben\" " +
	 * "	when " + "		Der Spitalsaufenthalt " +
	 * "		- beinhaltet keinen Vornamen " + "	then " +
	 * "		Warnung Der Spitalsaufenthalt beinhaltet keinen Vornamen " +
	 * "end " + " " + "rule \"Person muss einen Nachnamen haben\" " + "	when " +
	 * "		Der Spitalsaufenthalt " + "		- beinhaltet keinen Nachnamen "
	 * + "	then " +
	 * "		Fehler Der Spitalsaufenthalt beinhaltet keinen Nachnamen " +
	 * "end " + " " +
	 * "rule \"Spitalsaufenthalt muss ein Datum von beinhalten\" " + "	when " +
	 * "		Der Spitalsaufenthalt " + "		- beinhaltet kein Datum von " +
	 * "	then " +
	 * "		Fehler Der Spitalsaufenthalt beinhaltet kein Datum von " +
	 * "end " + " " +
	 * "rule \"Der Nachname muss mindestens 5 Zeichen lang sein\" " + "	when " +
	 * "		Der Spitalsaufenthalt " +
	 * "		- der Nachname ist kuerzer as 5 Zeichen " + "	then " +
	 * "		Fehler Der Nachname ist kuerzer als 5 Zeichen " + "end " + " " +
	 * "rule \"Der Aufenthalt darf maximal 30 Tage dauern\" " + "	when " +
	 * "		Der Spitalsaufenthalt " + "		- laenger als 30 Tage ist " +
	 * "	then " +
	 * "		Warnung Der Spitalsaufenthalt soll maximal 30 Tage dauern " +
	 * "end "; }
	 */
}
