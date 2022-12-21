package vam.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

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

	private String creatorName;

	private String packageName;

	private String version;

	private Integer dependenciesSize;

	private String fullPath;
	private String varFileName;

	public VarFile(VarFileDTO varFileDTO) {
		super();
		this.creatorName = varFileDTO.getCreatorName();
		this.packageName = varFileDTO.getPackageName();
		this.version = varFileDTO.getVersion();
		this.fullPath = varFileDTO.getFullPath();
		this.varFileName = varFileDTO.getVarFileName();
		this.dependenciesSize = varFileDTO.getMetaJson().getDependenciesMap().size();
	}

	public VarFile getSameVersion(List<VarFile> varFileOldList) {
		if (CollectionUtils.isEmpty(varFileOldList)) {
			return null;
		}

		for (VarFile varFileOld : varFileOldList) {
			String oleVersion = varFileOld.getVersion();
			if (getVersion().equals(oleVersion))
				return varFileOld;
		}
		return null;
	}
}
