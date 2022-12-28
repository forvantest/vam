package vam.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import vam.dto.MetaJson;
import vam.dto.VarFileDTO;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "varfile")
public class VarFile implements Serializable, Comparable {

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

	private Integer referenced;

	private Integer favorite;

	private String fullPath;
	private String varFileName;

	public VarFile(VarFileDTO varFileDTO) {
		super();
		this.creatorName = varFileDTO.getCreatorName();
		this.packageName = varFileDTO.getPackageName();
		this.version = varFileDTO.getVersion();
		this.fullPath = varFileDTO.getFullPath();
		this.varFileName = varFileDTO.getVarFileName();
		if (Objects.nonNull(varFileDTO.getMetaJson()))
			this.dependenciesSize = varFileDTO.getMetaJson().getDependenciesMap().size();
	}

	public VarFile(String k, MetaJson v) {
		String[] ss = StringUtils.split(k, ".");
		this.creatorName = ss[0];
		this.packageName = ss[1];
		this.version = ss[2];
		this.fullPath = "";
		this.varFileName = "";
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

	public void increaseFavorite() {
		if (Objects.nonNull(favorite))
			favorite++;
		else
			favorite = 1;
	}

	public void increaseReferenced() {
		if (Objects.nonNull(referenced))
			referenced++;
		else
			referenced = 1;
	}

	@Override
	public int compareTo(Object object) {
		VarFile varFile2 = (VarFile) object;
		return this.version.compareTo(varFile2.getVersion());
	}

}
