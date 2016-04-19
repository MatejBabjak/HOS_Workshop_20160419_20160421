package at.vaeb.kv.ka.sample.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_ICD_CODE", schema = "KV")
@SuppressWarnings("nls")
public class IcdCode {
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "kuerzel")
	private String kuerzel;
	
	public Long getId() {
		return id;
	}
	
	public String getKuerzel() {
		return kuerzel;
	}
}
