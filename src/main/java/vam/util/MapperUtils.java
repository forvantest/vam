package vam.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import vam.dto.SceneJson;
import vam.dto.VarFileDTO;
import vam.entity.VarFile;

@Slf4j
@Component
public class MapperUtils {

	@Autowired
	public ObjectMapper objectMapper;

	public VarFile convertVarFile(VarFileDTO varFileDTO) {
		VarFile varFile = new VarFile();
		varFile.setCreatorName(varFileDTO.getCreatorName());
		varFile.setPackageName(varFileDTO.getPackageName());
		varFile.setVersion(varFileDTO.getVersion());
		varFile.setFullPath(varFileDTO.getFullPath());
		varFile.setVarFileName(varFileDTO.getVarFileName());
		if (Objects.nonNull(varFileDTO.getDependencies())) {
			try {
//				this.metaDependencies = objectMapper
//						.writeValueAsString(varFileDTO.getMetaJson().getDependenciesAll(""));
				String dependencies = objectMapper.writeValueAsString(varFileDTO.getDependencies());
				varFile.setDependencies(dependencies);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		varFile.setFemaleCount(varFileDTO.getFemaleCount());
		varFile.setFemaleGenitaliaCount(varFileDTO.getFemaleGenitaliaCount());
		varFile.setMaleCount(varFileDTO.getMaleCount());
		varFile.setMaleGenitaliaCount(varFileDTO.getMaleGenitaliaCount());
		varFile.setScenes(varFileDTO.getSceneJsonList().size());
		varFile.setFavorite(varFileDTO.getFavorite());
		varFile.setReferencesJson(varFileDTO.getReferencesJson());
		try {
			List<SceneJson> easyList = new ArrayList<>();
			varFileDTO.getSceneJsonList().forEach(s -> easyList.add(new SceneJson(s)));
			String scenesJson = objectMapper.writeValueAsString(easyList);
			varFile.setScenesJson(scenesJson);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return varFile;
	}

	public VarFileDTO convertVarFileDTO(VarFile varFile) {
		VarFileDTO varFileDTO = new VarFileDTO();
		varFileDTO.setCreatorName(varFile.getCreatorName());
		varFileDTO.setPackageName(varFile.getPackageName());
		varFileDTO.setVersion(varFile.getVersion());
		varFileDTO.setFullPath(varFile.getFullPath());
		varFileDTO.setVarFileName(varFile.getVarFileName());
		varFileDTO.setFavorite(varFile.getFavorite());
		varFileDTO.setReferencesJson(varFile.getReferencesJson());
		try {
//			String str = varFile.getMetaDependencies();
//			if (Objects.nonNull(str)) {
//				this.setMetaJson(new MetaJson());
//				this.getMetaJson().setDependencies(objectMapper.readTree(str).deepCopy());
//			}
			String str2 = varFile.getScenesJson();
			if (Objects.nonNull(str2)) {
				List<SceneJson> myObjects = objectMapper.readValue(str2, new TypeReference<List<SceneJson>>() {
				});
				varFileDTO.setSceneJsonList(myObjects);
			}
			String str3 = varFile.getDependencies();
			if (Objects.nonNull(str3)) {
				Set<String> dependencies = objectMapper.readValue(str3, new TypeReference<Set<String>>() {
				});
				varFileDTO.setDependencies(dependencies);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return varFileDTO;
	}
}
