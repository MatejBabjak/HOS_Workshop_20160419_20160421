[condition][KAInputRecord] Der Spitalsaufenthalt=$kaRecord:KAInputRecord()
[condition][KAInputRecord] - beinhaltet keinen Vornamen=eval(StringUtils.isBlank($kaRecord.getVorname()))
[condition][KAInputRecord] - beinhaltet keinen Nachnamen=eval(StringUtils.isBlank($kaRecord.getNachname()))
[condition][KAInputRecord] - der Nachname ist kuerzer as {minNachnameLength} Zeichen=eval(!StringUtils.isBlank($kaRecord.getNachname()) && $kaRecord.getNachname().length() < {minNachnameLength})
[condition][KAInputRecord] - laenger als {maxAufenthaltLength} Tage ist=eval($kaRecord.getAufenthaltsdauer() > {maxAufenthaltLength})
[condition][KAInputRecord] - die Diagnose ist kein gueltiger ICD10 Code=eval(!icdCodeDao.existsIcd10Code($kaRecord.getDiagnoseIcdCode()))

[consequence][]Warnung {text}=validationResultCollector.addWarning("{text}");
[consequence][]Fehler {text}=validationResultCollector.addError("{text}");
[consequence][]Ignorieren=