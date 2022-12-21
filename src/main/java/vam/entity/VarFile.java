package vam.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import vam.dto.VarFileDTO;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "varfile")
public class VarFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -373559697135370802L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long key;

	private String authorName;

	private String projectName;

	private String version;
	
	private Integer dependenciesSize;

	public VarFile(VarFileDTO varFileDTO) {
		super();
		this.authorName = varFileDTO.getAuthorName();
		this.projectName = varFileDTO.getProjectName();
		this.version = varFileDTO.getVersion();
		this.dependenciesSize = varFileDTO.getMetaJson().getDependenciesMap().size();
	}
}
