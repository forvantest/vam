package vam;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;
import vam.dto.VarFileDTO;
import vam.entity.VarFile;

@Slf4j
public abstract class WorkDeployVarFile extends WorkVarFile {

	int dependCount = 0;

	protected void process(String targetDirectory) {
		File dir = new File(VAM_ROOT_PATH + targetDirectory);

		Map<String, String> mAll = new HashMap<>();
		Set<String> varFileRefSet = fetchAllVarFiles(dir, VAR_EXTENSION);
		processVarFileRefSet(mAll, varFileRefSet, true);

		Set<String> varFileRefSet2 = fetchAllVarFiles(dir, DEPEND_TXT_EXTENSION);
		processVarFileRefSet(mAll, varFileRefSet2, true);
	}

	private void processVarFileRefSet(Map<String, String> mAll, Set<String> varFileRefSet, boolean toDo) {
		process = 0;
		for (String varFileName : varFileRefSet) {
			VarFileDTO varFileDTOQuery = new VarFileDTO("", varFileName);
			VarFile varFile = findSuitableVarFile(varFileDTOQuery);
			if (Objects.isNull(varFile)) {
				log.info("\n---missing depenence: " + varFileName);
				continue;
			}
			VarFileDTO varFileDTO = new VarFileDTO(varFile);
			processVarFile(mAll, varFileDTO);
			if (toDo)
				work1(varFileDTO);
			echo(varFileRefSet.size());
		}
	}

	protected void processVarFile(Map<String, String> mAll, VarFileDTO varFileDTO) {
		String fullPathName = varFileDTO.getFullPath() + varFileDTO.getVarFileName();
		File realVarFile = new File(fullPathName);
		if (realVarFile.exists()) {
			work3(realVarFile);
		} else {
			log.warn("warn8: varFile doesn't exist: " + varFileDTO.getFullPath() + "  " + varFileDTO.getVarFileName());
		}

		if (Objects.nonNull(varFileDTO.getMetaJson())) {

			Map<String, String> m = varFileDTO.getMetaJson().getDependenciesAll(varFileDTO.getVarFileName());
			mAll.putAll(m);
			// System.out.println("+++before size: " + mAll.size());
			m.forEach((k, v) -> processDependencies(mAll, k, v));
			// System.out.println("+++after size: " + mAll.size());
		}
	}

	private void processDependencies(Map<String, String> mAll, String k, String parent) {
		VarFileDTO varFileQuery = new VarFileDTO(null, k);
		VarFile varFileRef = findSuitableVarFile(varFileQuery);
		if (Objects.nonNull(varFileRef)) {
			work2(parent, varFileRef);
			VarFileDTO varFileDTOOld = new VarFileDTO(varFileRef);
			if (Objects.nonNull(varFileDTOOld)) {
				if (Objects.nonNull(varFileDTOOld.getMetaJson())) {
					Map<String, String> m2 = varFileDTOOld.getMetaJson()
							.getDependenciesAll(varFileDTOOld.getVarFileName());
					Map<String, String> mapDiff = cuteMap(mAll, m2);
					mAll.putAll(mapDiff);
					if (!CollectionUtils.isEmpty(mapDiff)) {
						log.warn("+++ new depends size: " + mapDiff.size() + " total: " + mAll.size());
					}
					mapDiff.forEach((k2, v2) -> processDependencies(mAll, k2, v2));
				}
			}
		}
	}

	void work1(VarFileDTO varFileDTO) {
		varFileDTO.favorite(VAM_FILE_PREFS);
		List<VarFile> varFileFavList = varFileRepository.findBy(varFileDTO);
		if (!CollectionUtils.isEmpty(varFileFavList)) {
			VarFile varFile = varFileFavList.get(0);
			varFile.increaseFavorite();
			varFileRepository.save(varFile);
		}
	}

	VarFileDTO work2(String parent, VarFile varFileRef) {
		varFileRef.increaseReference(parent);
		varFileRepository.save(varFileRef);
		File realVarFile = new File(varFileRef.getFullPath() + varFileRef.getVarFileName());
		if (!realVarFile.exists()) {
			log.warn("---db data deprecate2: " + varFileRef);
			varFileRepository.delete(varFileRef);
			return null;
		}
		createLinkFile(realVarFile);
		VarFileDTO varFileDTORef = readVarFile(makeVarFileDTO(realVarFile.getAbsolutePath()));
		varFileDTORef.realHide(VAM_FILE_PREFS);
		return varFileDTORef;
	}

	void work3(File realVarFile) {
		createLinkFile(realVarFile);
	}

}
