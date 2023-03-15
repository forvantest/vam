package vam;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;
import vam.dto.VarFileDTO;
import vam.dto.enumration.BestGirl;

@Slf4j
public abstract class WorkDeployVarFile extends WorkVarFile {

//	static Set<String> creatorNameSet;
//	{
//		creatorNameSet = new HashSet<>();
//		creatorNameSet.add("Dnaddr");
//		creatorNameSet.add("realclone");
//		creatorNameSet.add("Archer");
//		creatorNameSet.add("VAM-YJ");
//		creatorNameSet.add("lv");
//		creatorNameSet.add("CMA");
//		creatorNameSet.add("VAMDoll");
//		creatorNameSet.add("Anom");
//		creatorNameSet.add("Hcg");
//		creatorNameSet.add("HT");
//		creatorNameSet.add("zzzat16h");
//		creatorNameSet.add("uugg");
//		creatorNameSet.add("mio");
//		creatorNameSet.add("callimohu");
//		creatorNameSet.add("ye666");
//		creatorNameSet.add("Xspada");
//		creatorNameSet.add("VKStyle");
//		creatorNameSet.add("VAM_GS");
//		creatorNameSet.add("sortof");
//		creatorNameSet.add("Solerrain");
//		creatorNameSet.add("rose1");
//		creatorNameSet.add("qingfeng");
//		creatorNameSet.add("Qing");
//		creatorNameSet.add("passerby");
//		creatorNameSet.add("MRdong");
//		creatorNameSet.add("Neiro");
//		creatorNameSet.add("Wolverine");
//		creatorNameSet.add("yesmola");
//		creatorNameSet.add("Eros");
//		creatorNameSet.add("VAMDoll");
//		creatorNameSet.add("KDollMASTA");
//		creatorNameSet.add("Bamair1984");
//		creatorNameSet.add("BIGDOG");
//		creatorNameSet.add("Keiaono");
//		creatorNameSet.add("MK47");
//		creatorNameSet.add("ReAcg");
//		creatorNameSet.add("rose1");
//		creatorNameSet.add("rose11");
//		creatorNameSet.add("QWERTY");
//		creatorNameSet.add("mai");
//		creatorNameSet.add("Thorn");
//		creatorNameSet.add("ADADE");
//		creatorNameSet.add("FRK");
//		creatorNameSet.add("hero774");
//	}

	int dependCount = 0;

	protected Map<String, VarFileDTO> process(String sourceDirectory, String targetDirectory) {
		Map<String, VarFileDTO> mVar = new HashMap<>();
		Map<String, VarFileDTO> mLack = processPri(mVar, sourceDirectory, targetDirectory);
		List<VarFileDTO> favList = mVar.values().stream()
				.filter(v -> v.getBFavorite() == null ? false : v.getBFavorite()).collect(Collectors.toList());
		List<VarFileDTO> hideList = mVar.values().stream().filter(v -> v.getBHide() == null ? false : v.getBHide())
				.collect(Collectors.toList());
		log.warn("+++ deploy:{} fav:{} hide:{} --- lack depenencies:{} ", sourceDirectory, favList.size(),
				hideList.size(), mLack.size());

		return mVar;
	}

	protected Map<String, VarFileDTO> processSomeGirl(String sourceDirectory, int num, String targetDirectory) {
		Map<String, VarFileDTO> mVar = new HashMap<>();
		Map<String, VarFileDTO> mLack = giveMeGirl(mVar, sourceDirectory, num, targetDirectory);
		List<VarFileDTO> favList = mVar.values().stream()
				.filter(v -> v.getBFavorite() == null ? false : v.getBFavorite()).collect(Collectors.toList());
		List<VarFileDTO> hideList = mVar.values().stream().filter(v -> v.getBHide() == null ? false : v.getBHide())
				.collect(Collectors.toList());
		log.warn("+++ deploy:{} fav:{} hide:{} --- lack depenencies:{} ", sourceDirectory, favList.size(),
				hideList.size(), mLack.size());

		return mVar;
	}

	private Map<String, VarFileDTO> giveMeGirl(Map<String, VarFileDTO> mVar, String author, int num,
			String targetDirectory) {
		List<VarFileDTO> varFileDTOList = new ArrayList<>();
		List<VarFileDTO> varFileDTOList3 = new ArrayList<>();
		if (Objects.nonNull(author)) {
			varFileDTOList3 = varFileService.findByAuthor(author);
		} else {
			for (BestGirl bestGirl : BestGirl.values()) {
				List<VarFileDTO> varFileDTOList2 = varFileService.findByAuthor(bestGirl.getDescription());
				varFileDTOList3.addAll(varFileDTOList2);
			}
		}
		for (int i = 0; i < num; i++) {
			int index = (int) (Math.random() * varFileDTOList3.size());
			VarFileDTO varFileDTO = varFileDTOList3.get(index);
			varFileDTOList.add(varFileDTO);
			log.warn("+++ deploy girl:{}/{} {}", i, num, varFileDTO.getVarFileName());
		}
		Set<String> varFileRefSet = varFileDTOList.stream().map(VarFileDTO::getVarFileName).collect(Collectors.toSet());
		return processPri(mVar, varFileRefSet, targetDirectory);
	}

	protected Map<String, VarFileDTO> processSingle(String varFileName, String targetDirectory) {
		Map<String, VarFileDTO> mVar = new HashMap<>();
		Map<String, VarFileDTO> mLack = giveMe(mVar, new VarFileDTO(null, varFileName), targetDirectory);
		List<VarFileDTO> favList = mVar.values().stream()
				.filter(v -> v.getBFavorite() == null ? false : v.getBFavorite()).collect(Collectors.toList());
		List<VarFileDTO> hideList = mVar.values().stream().filter(v -> v.getBHide() == null ? false : v.getBHide())
				.collect(Collectors.toList());
		log.warn("+++ deploy:{} fav:{} hide:{} --- lack depenencies:{} ", varFileName, favList.size(), hideList.size(),
				mLack.size());

		return mLack;
	}

	private Map<String, VarFileDTO> giveMe(Map<String, VarFileDTO> mVar, VarFileDTO varFileDTOQuery,
			String targetDirectory) {
		List<VarFileDTO> varFileDTOList2 = varFileService.findByName(varFileDTOQuery);
		Set<String> varFileRefSet = varFileDTOList2.stream().map(VarFileDTO::getVarFileName)
				.collect(Collectors.toSet());
		return processPri(mVar, varFileRefSet, targetDirectory);
	}

	protected Map<String, VarFileDTO> processPri(Map<String, VarFileDTO> mVar, String sourceDirectory,
			String targetDirectory) {
		List<VarFileDTO> varFileDTOList = varFileService.findByAuthor(sourceDirectory);
		Set<String> varFileRefSet = varFileDTOList.stream().map(VarFileDTO::getVarFileName).collect(Collectors.toSet());
		return processPri(mVar, varFileRefSet, targetDirectory);
	}

	private Map<String, VarFileDTO> processPri(Map<String, VarFileDTO> mVar, Set<String> varFileRefSet,
			String targetDirectory) {
//		File sourceDir = new File(VAM_ALLPACKAGES_PATH + sourceDirectory);
		Map<String, VarFileDTO> mAll = new HashMap<>();
		Map<String, VarFileDTO> mLack = new HashMap<>();
//		Set<String> varFileRefSet = fetchAllVarFiles(sourceDir, VAR_EXTENSION);
		varFileRefSet.forEach(k -> processDependencies(mVar, mAll, mLack, k, null, targetDirectory, 0));
//		Set<String> varFileRefSet2 = fetchAllVarFiles(dir, DEPEND_TXT_EXTENSION);
//		varFileRefSet2.forEach(k -> processDependencies(mAll, k, varFileRefSet.iterator().next()));
		return mLack;
	}

	private void processDependencies(Map<String, VarFileDTO> mVar, Map<String, VarFileDTO> mAll,
			Map<String, VarFileDTO> mLack, String k, String parent, String groupName, int level) {
		VarFileDTO varFileQuery = new VarFileDTO(null, k);
		if (StringUtils.endsWith(varFileQuery.getVarFileName(), ":"))
			log.debug("debug 2" + varFileQuery.getVarFileName());
		if ("Ark1F1.Raven_Update.1.var".equals(varFileQuery.getVarFileName()))
			log.debug("debug " + varFileQuery.getVarFileName());
		VarFileDTO varFileRef = findSuitableVarFile(varFileQuery);
		if (Objects.nonNull(varFileRef)) {
			VarFileDTO varFileDTOOld = work2(parent, varFileRef, groupName);
			if (Objects.nonNull(varFileDTOOld)) {
				if (Objects.nonNull(varFileDTOOld.getDependencies())) {
					Map<String, VarFileDTO> m2 = new HashMap<>();
					for (String key : varFileDTOOld.getDependencies()) {
						m2.put(key, varFileDTOOld);
					}
					Map<String, VarFileDTO> mapDiff = cuteMap(mAll, m2);
					mAll.putAll(mapDiff);
					if (!CollectionUtils.isEmpty(mapDiff) && level < 2) {
						log.debug("+++ new depends size: " + mapDiff.size() + " total: " + mAll.size());
						mapDiff.forEach((k2, v2) -> processDependencies(mVar, mAll, mLack, k2, v2.getVarFileName(),
								groupName, level + 1));
					}
				}
				mVar.put(varFileDTOOld.getVarFileName(), varFileDTOOld);
			}
		} else if (Objects.isNull(parent)) {
			mLack.put(varFileQuery.getVarFileName(), varFileQuery);
			log.info("--- old depends not exist: " + varFileQuery.getVarFileName());
		}
	}

	VarFileDTO work2(String parent, VarFileDTO varFileDTORef, String groupName) {
		if (Objects.isNull(parent)) {
			varFileDTORef.favorite(VAM_ALLFAVORITE_PATH + groupName);
			varFileDTORef.increaseFavorite();
			varFileService.update(varFileDTORef);
		} else {
			increaseReference(varFileDTORef, parent);
			varFileService.update(varFileDTORef);
			varFileDTORef.realHide(VAM_ALLFAVORITE_PATH + groupName);
		}

		File realVarFile = new File(varFileDTORef.getFullPath() + varFileDTORef.getVarFileName());
		if (!realVarFile.exists()) {
			log.warn("---db data deprecate2: " + varFileDTORef);
			varFileService.delete(varFileDTORef);
			return null;
		}
		createLinkFile(varFileDTORef, realVarFile, groupName);
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
