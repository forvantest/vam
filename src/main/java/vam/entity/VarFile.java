package vam.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import vam.dto.MetaJson;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(Include.NON_NULL)
@Entity
@Table(name = "varfile")
public class VarFile implements Serializable, Comparable {

	@Autowired
	public ObjectMapper objectMapper;
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

	private String metaJson;

	private String dependencies;

	private String referencesJson;

//	private Integer referenced;

	private Integer scenes;

	private String scenesJson;

	private String soundJson;
	private Integer soundCount;

	private Integer femaleCount;
	private Integer femaleGenitaliaCount;
	private Integer maleCount;
	private Integer maleGenitaliaCount;

	private Integer favorite;

	private String fullPath;
	private String varFileName;

//	public VarFile(VarFileDTO varFileDTO) {
//		super();
//
//		this.creatorName = varFileDTO.getCreatorName();
//		this.packageName = varFileDTO.getPackageName();
//		this.version = varFileDTO.getVersion();
//		this.fullPath = varFileDTO.getFullPath();
//		this.varFileName = varFileDTO.getVarFileName();
//		if (Objects.nonNull(varFileDTO.getDependencies())) {
//			try {
////				this.metaDependencies = objectMapper
////						.writeValueAsString(varFileDTO.getMetaJson().getDependenciesAll(""));
//				this.dependencies = objectMapper.writeValueAsString(varFileDTO.getDependencies());
//
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//			this.dependenciesSize = varFileDTO.getDependencies().size();
//		}
//		this.femaleCount = varFileDTO.getFemaleCount();
//		this.femaleGenitaliaCount = varFileDTO.getFemaleGenitaliaCount();
//		this.maleCount = varFileDTO.getMaleCount();
//		this.maleGenitaliaCount = varFileDTO.getMaleGenitaliaCount();
//		this.scenes = varFileDTO.getSceneJsonList().size();
//		this.favorite = varFileDTO.getFavorite();
//		this.referencesJson = varFileDTO.getReferencesJson();
//		try {
//			List<SceneJson> easyList = new ArrayList<>();
//			varFileDTO.getSceneJsonList().forEach(s -> easyList.add(new SceneJson(s)));
//			scenesJson = objectMapper.writeValueAsString(easyList);
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//	}

	public VarFile(String k, MetaJson v) {
		String[] ss = StringUtils.split(k, ".");
		this.creatorName = ss[0];
		this.packageName = ss[1];
		this.version = ss[2];
		this.fullPath = "";
		this.varFileName = "";
	}

//	public VarFile getSameVersion(List<VarFile> varFileOldList) {
//		if (CollectionUtils.isEmpty(varFileOldList)) {
//			return null;
//		}
//
//		for (VarFile varFileOld : varFileOldList) {
//			String oleVersion = varFileOld.getVersion();
//			if (getVersion().equals(oleVersion))
//				return varFileOld;
//		}
//		return null;
//	}

//	public void increaseReferenced() {
//		if (Objects.nonNull(referenced))
//			referenced++;
//		else
//			referenced = 1;
//	}

	@Override
	public int compareTo(Object object) {
		VarFile varFile2 = (VarFile) object;
		return this.version.compareTo(varFile2.getVersion());
	}

//	public void increaseReference(VarFileDTO varFileDTO) {
//		if (Objects.isNull(referencesJson))
//			referencesJson = "[]";
//		ObjectMapper objectMapper = new ObjectMapper();
//		Set<String> reference;
//		try {
//			reference = objectMapper.readValue(referencesJson, HashSet.class);
//			String key = varFileDTO.makeKey();
//			reference.add(key);
//			referencesJson = objectMapper.writeValueAsString(reference);
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		}
//	}

}
