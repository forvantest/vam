package vam;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import vam.dto.VarFileDTO;

@Slf4j
public abstract class WorkDeployVarFile extends WorkVarFile {

	static Set<String> creatorNameSet;
	{
		creatorNameSet = new HashSet<>();
		creatorNameSet.add("Dnaddr");
		creatorNameSet.add("Archer");
		creatorNameSet.add("realclone");
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
	}

	int dependCount = 0;

	protected void process(String targetDirectory) {
		File dir = new File(VAM_ROOT_PATH + targetDirectory);

		Map<String, String> mAll = new HashMap<>();
		Set<String> varFileRefSet = fetchAllVarFiles(dir, VAR_EXTENSION);
		varFileRefSet.forEach(k -> processDependencies(mAll, k, null));

		Set<String> varFileRefSet2 = fetchAllVarFiles(dir, DEPEND_TXT_EXTENSION);
		varFileRefSet2.forEach(k -> processDependencies(mAll, k, varFileRefSet.iterator().next()));
	}

//	private void processVarFileRefSet(Map<String, String> mAll, Set<String> varFileRefSet, boolean toDo) {
//		process = 0;
//		for (String varFileName : varFileRefSet) {
//			VarFileDTO varFileDTOQuery = new VarFileDTO("", varFileName);
//			VarFile varFile = findSuitableVarFile(varFileDTOQuery);
//			if (Objects.isNull(varFile)) {
//				log.info("\n---missing depenence: " + varFileName);
//				continue;
//			}
//			VarFileDTO varFileDTO = new VarFileDTO(varFile);
//			processVarFile(mAll, varFileDTO);
////			if (toDo)
//////				work1(varFile);
////			else
////				work4(varFile);
//			echo(varFileRefSet.size());
//		}
//	}

//	protected void processVarFile(Map<String, String> mAll, VarFileDTO varFileDTO) {
//		String fullPathName = varFileDTO.getFullPath() + varFileDTO.getVarFileName();
//		File realVarFile = new File(fullPathName);
//		if (realVarFile.exists()) {
//			work3(realVarFile);
//		} else {
//			log.warn("warn8: varFile doesn't exist: " + varFileDTO.getFullPath() + "  " + varFileDTO.getVarFileName());
//		}
//
//		if (Objects.nonNull(varFileDTO.getMetaJson())) {
//
//			Map<String, String> m = varFileDTO.getMetaJson().getDependenciesAll(varFileDTO.getVarFileName());
//			mAll.putAll(m);
//			// System.out.println("+++before size: " + mAll.size());
//			m.forEach((k, v) -> processDependencies(mAll, k, v));
//			// System.out.println("+++after size: " + mAll.size());
//		}
//	}

	private void processDependencies(Map<String, String> mAll, String k, String parent) {
		VarFileDTO varFileQuery = new VarFileDTO(null, k);
		if ("realclone.annafix2.1.var".equals(varFileQuery.getVarFileName()))
			log.debug("debug " + varFileQuery.getVarFileName());
		VarFileDTO varFileRef = findSuitableVarFile(varFileQuery);
		if (Objects.nonNull(varFileRef)) {
			VarFileDTO varFileDTOOld = work2(parent, varFileRef);
			if (Objects.nonNull(varFileDTOOld)) {
				if (Objects.nonNull(varFileDTOOld.getDependencies())) {
					Map<String, String> m2 = new HashMap<>();
					for (String key : varFileDTOOld.getDependencies()) {
						m2.put(key, varFileDTOOld.getVarFileName());
					}
					Map<String, String> mapDiff = cuteMap(mAll, m2);
					mAll.putAll(mapDiff);
					if (!CollectionUtils.isEmpty(mapDiff)) {
						log.warn("+++ new depends size: " + mapDiff.size() + " total: " + mAll.size());
						mapDiff.forEach((k2, v2) -> processDependencies(mAll, k2, v2));
					}
				}
			}
		} else {
			log.error("--- old depends not exist: " + varFileQuery.getVarFileName());
		}
	}

//	void work1(VarFile varFile) {
//		VarFileDTO varFileDTO = new VarFileDTO(varFile);
//		varFileDTO.favorite(VAM_FILE_PREFS);
//		varFile.increaseFavorite();
//		varFileRepository.save(varFile);
//	}

	VarFileDTO work2(String parent, VarFileDTO varFileDTORef) {
		if (Objects.isNull(parent)) {
//			varFileDTORef = new VarFileDTO(varFileDTORef);
			varFileDTORef.favorite(VAM_FILE_PREFS);
			varFileDTORef.increaseFavorite();
			varFileService.update(varFileDTORef);
		} else {
			varFileDTORef.increaseReference(parent);
			varFileService.update(varFileDTORef);
			varFileDTORef.realHide(VAM_FILE_PREFS);
		}

		File realVarFile = new File(varFileDTORef.getFullPath() + varFileDTORef.getVarFileName());
		if (!realVarFile.exists()) {
			log.warn("---db data deprecate2: " + varFileDTORef);
			varFileService.delete(varFileDTORef);
			return null;
		}
		createLinkFile(realVarFile);
		return varFileDTORef;
	}

//	void work3(File realVarFile) {
//		createLinkFile(realVarFile);
//	}
//
//	void work4(VarFile varFile) {
//
//	}
}
