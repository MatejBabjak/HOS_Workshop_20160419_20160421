package at.vaeb.kv.ka.sample.record;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.inject.Inject;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

@Record
public class KAInputRecord {
	
	@Field(at=0, length=8, format="ddMMyyyy")
	private Date datumVon;
	
	@Field(at=8, length=8, format="ddMMyyyy")
	private Date datumBis;
	
	@Field(at=16, length=20)
	private String vorname;
	
	@Field(at=36, length=30)
	private String nachname;
	
	@Field(at=66, length=10)
	private String diagnoseIcdCode;
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("KAInputRecord(");
		builder.append("datumVon = ").append(datumVon).append(", ");
		builder.append("datumBis = ").append(datumBis).append(", ");
		builder.append("aufenthaltsdauer = ").append(getAufenthaltsdauer()).append(", ");
		builder.append("vorname = '").append(vorname).append("', ");
		builder.append("nachname = '").append(nachname).append("', ");
		builder.append("diagnoseIcdCode = '").append(diagnoseIcdCode).append("'");
		builder.append(")");
		
		return builder.toString();
	}
	
	//-- business logic ----------------------------//
	public LocalDate getDatumVon() {
		return datumVon != null ? datumVon.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
	}
	
	public LocalDate getDatumBis() {
		return datumBis != null ? datumBis.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
	}	
	
	public long getAufenthaltsdauer() {
		LocalDate von = getDatumVon();
		LocalDate bis = getDatumBis();
		
		if (von == null) {
			return Long.MAX_VALUE;
		}
		
		if (bis == null) {
			return ChronoUnit.DAYS.between(von, LocalDate.now());
		}
		
		return ChronoUnit.DAYS.between(von, bis);
	}
	
	public String getVorname() {
		return vorname;
	}
	
	public String getNachname() {
		return nachname;
	}
	
	public String getDiagnoseIcdCode() {		
		return diagnoseIcdCode;
	}
}