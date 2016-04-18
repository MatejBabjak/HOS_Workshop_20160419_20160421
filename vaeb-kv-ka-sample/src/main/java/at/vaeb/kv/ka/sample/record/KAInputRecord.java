package at.vaeb.kv.ka.sample.record;

import java.time.LocalDate;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

@Record
public class KAInputRecord {
	@Field(at=0, length=8, format="ddMMyyyy", required=true)
	private LocalDate datumVon;
	
	@Field(at=8, length=8, format="ddMMyyyy")
	private LocalDate datumBis;
	
	@Field(at=16, length=20)
	private String vorname;
	
	@Field(at=36, length=30, required=true)
	private String nachname;
	
	@Field(at=66, length=10, required=true)
	private String diagnoseIcdCode;
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("KAInputRecord(");
		builder.append("datumVon = ").append(datumVon).append(", ");
		builder.append("datumBis = ").append(datumBis).append(", ");
		builder.append("vorname = '").append(vorname).append("', ");
		builder.append("nachname = '").append(nachname).append("', ");
		builder.append("diagnoseIcdCode = '").append(diagnoseIcdCode).append("'");
		builder.append(")");
		
		return builder.toString();
	}
}