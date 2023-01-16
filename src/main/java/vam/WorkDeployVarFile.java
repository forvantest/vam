package vam;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;
import vam.dto.VarFileDTO;

@Slf4j
public abstract class WorkDeployVarFile extends WorkVarFile {

	static Set<String> creatorNameSet;
	{
		creatorNameSet = new HashSet<>();
		creatorNameSet.add("Dnaddr");
		creatorNameSet.add("realclone");
		creatorNameSet.add("Archer");
		creatorNameSet.add("VAM-YJ");
		creatorNameSet.add("lv");
		creatorNameSet.add("CMA");
		creatorNameSet.add("VAMDoll");
		creatorNameSet.add("Anom");
		creatorNameSet.add("Hcg");
		creatorNameSet.add("HT");
		creatorNameSet.add("zzzat16h");
		creatorNameSet.add("uugg");
		creatorNameSet.add("mio");
		creatorNameSet.add("callimohu");
		creatorNameSet.add("ye666");
		creatorNameSet.add("Xspada");
		creatorNameSet.add("VKStyle");
		creatorNameSet.add("VAM_GS");
		creatorNameSet.add("sortof");
		creatorNameSet.add("Solerrain");
		creatorNameSet.add("rose1");
		creatorNameSet.add("qingfeng");
		creatorNameSet.add("Qing");
		creatorNameSet.add("passerby");
		creatorNameSet.add("MRdong");
		creatorNameSet.add("Neiro");
		creatorNameSet.add("Wolverine");
		creatorNameSet.add("yesmola");
		creatorNameSet.add("Eros");
		creatorNameSet.add("VAMDoll");
		creatorNameSet.add("KDollMASTA");
		creatorNameSet.add("Bamair1984");
		creatorNameSet.add("BIGDOG");
		creatorNameSet.add("Keiaono");
		creatorNameSet.add("MK47");
		creatorNameSet.add("ReAcg");
		creatorNameSet.add("rose1");
		creatorNameSet.add("rose11");
		creatorNameSet.add("QWERTY");

	}

	int dependCount = 0;

	protected void process(String targetDirectory) {
		File dir = new File(VAM_ALLPACKAGES_PATH + targetDirectory);

		Map<String, String> mAll = new HashMap<>();
		Set<String> varFileRefSet = fetchAllVarFiles(dir, VAR_EXTENSION);
		varFileRefSet.forEach(k -> processDependencies(mAll, k, null, targetDirectory));

//		Set<String> varFileRefSet2 = fetchAllVarFiles(dir, DEPEND_TXT_EXTENSION);
//		varFileRefSet2.forEach(k -> processDependencies(mAll, k, varFileRefSet.iterator().next()));
	}

	private void processDependencies(Map<String, String> mAll, String k, String parent, String groupName) {
		VarFileDTO varFileQuery = new VarFileDTO(null, k);
		if ("realclone.annafix2.1.var".equals(varFileQuery.getVarFileName()))
			log.debug("debug " + varFileQuery.getVarFileName());
		VarFileDTO varFileRef = findSuitableVarFile(varFileQuery);
		if (Objects.nonNull(varFileRef)) {
			VarFileDTO varFileDTOOld = work2(parent, varFileRef, groupName);
			if (Objects.nonNull(varFileDTOOld)) {
				if (Objects.nonNull(varFileDTOOld.getDependencies())) {
					Map<String, String> m2 = new HashMap<>();
					for (String key : varFileDTOOld.getDependencies()) {
						m2.put(key, varFileDTOOld.getVarFileName());
					}
					Map<String, String> mapDiff = cuteMap(mAll, m2);
					mAll.putAll(mapDiff);
					if (!CollectionUtils.isEmpty(mapDiff)) {
						log.info("+++ new depends size: " + mapDiff.size() + " total: " + mAll.size());
						mapDiff.forEach((k2, v2) -> processDependencies(mAll, k2, v2, groupName));
					}
				}
			}
		} else {
			log.error("--- old depends not exist: " + varFileQuery.getVarFileName());
		}
	}

	VarFileDTO work2(String parent, VarFileDTO varFileDTORef, String groupName) {
		if (Objects.isNull(parent)) {
			varFileDTORef.favorite(VAM_FILE_PREFS);
			varFileDTORef.increaseFavorite();
			varFileService.update(varFileDTORef);
		} else {
			increaseReference(varFileDTORef, parent);
			varFileService.update(varFileDTORef);
			varFileDTORef.realHide(VAM_FILE_PREFS);
		}

		File realVarFile = new File(varFileDTORef.getFullPath() + varFileDTORef.getVarFileName());
		if (!realVarFile.exists()) {
			log.warn("---db data deprecate2: " + varFileDTORef);
			varFileService.delete(varFileDTORef);
			return null;
		}
		createLinkFile(realVarFile, groupName);
		return varFileDTORef;
	}

	public void increaseReference(VarFileDTO varFileDTO, String parent) {
		if (Objects.isNull(varFileDTO.getReferencesJson()))
			varFileDTO.setReferencesJson("[]");
		Set<String> reference;
		try {
			reference = objectMapper.readValue(varFileDTO.getReferencesJson(), HashSet.class);
			String key = parent;
			reference.add(key);
			String referencesJson = objectMapper.writeValueAsString(reference);
			varFileDTO.setReferencesJson(referencesJson);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
