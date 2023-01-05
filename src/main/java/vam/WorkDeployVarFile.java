package vam;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.util.CollectionUtils;

import vam.dto.VarFileDTO;
import vam.entity.VarFile;

public abstract class WorkDeployVarFile extends WorkVarFile {

	int dependCount = 0;

	protected void process(String targetDirectory) {
		File dir = new File(VAM_ROOT_PATH + targetDirectory);
		List<VarFileDTO> listFavVarFileDTO = fetchAllVarFiles(dir, VAR_EXTENSION);
		for (VarFileDTO varFileDTO : listFavVarFileDTO) {
			dependCount = 0;
			processVarFile(varFileDTO);
			work1(varFileDTO);
		}

		List<VarFileDTO> listDependTxtDTO = fetchAllVarFiles(dir, DEPEND_TXT_EXTENSION);
		for (VarFileDTO dependTxtDTO : listDependTxtDTO) {
			processDependTxt(dependTxtDTO);
			work1(dependTxtDTO);
		}
	}

	protected void processVarFile(VarFileDTO varFileDTO) {
		String fullPathName = varFileDTO.getFullPath() + varFileDTO.getVarFileName();
		File realVarFile = new File(fullPathName);
		if (realVarFile.exists()) {
			work3(realVarFile);
		} else {
			System.out.println(
					"warn8: varFile doesn't exist: " + varFileDTO.getFullPath() + "  " + varFileDTO.getVarFileName());
		}

		if (Objects.nonNull(varFileDTO.getMetaJson())) {

			Map<String, String> m = varFileDTO.getMetaJson().getDependenciesAll(varFileDTO.getVarFileName());
			Map<String, String> mAll = new HashMap<>();
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
						System.out.println("+++depend size: " + mapDiff);
					}
					mapDiff.forEach((k2, v2) -> processDependencies(mAll, k2, v2));
				}
			}
		}
	}

	protected void processDependTxt(VarFileDTO varFileDTO) {
		VarFile varFile = findSuitableVarFile(varFileDTO);
		if (Objects.isNull(varFile))
			return;

		processVarFile(new VarFileDTO(varFile));

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
			System.out.println("---db data deprecate2: " + varFileRef);
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
