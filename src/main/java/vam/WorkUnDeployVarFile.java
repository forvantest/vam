package vam;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vam.dto.VarFileDTO;
import vam.dto.enumration.BestGirl;
import vam.dto.enumration.BestScene;

@Slf4j
@Service("WorkUnDeployVarFile")
public class WorkUnDeployVarFile extends WorkDeployVarFile {

	@Override
	VarFileDTO work2(String parent, VarFileDTO varFileDTORef, String groupName) {
		File realVarFile = new File(varFileDTORef.getFullPath() + varFileDTORef.getVarFileName());
		deleteLinkFile(varFileDTORef, realVarFile, groupName);
		varFileDTORef.unHide(VAM_ALLFAVORITE_PATH + groupName);
		if (BestGirl.contains(varFileDTORef.getCreatorName())) {
			if (!StringUtils.startsWith(varFileDTORef.getFullPath(), VAM_BEST_GIRL_PATH)) {
				varFileDTORef.moveVarFileTo(VAM_BEST_GIRL_PATH, "used");
				varFileService.update(varFileDTORef);
			}
		} else if (BestScene.contains(varFileDTORef.getCreatorName())) {
			if (!StringUtils.startsWith(varFileDTORef.getFullPath(), VAM_BEST_SCENE_PATH)) {
				varFileDTORef.moveVarFileTo(VAM_BEST_SCENE_PATH, "used");
				varFileService.update(varFileDTORef);
			}
		} else if (!StringUtils.startsWith(varFileDTORef.getFullPath(), VAM_BASE_PATH)) {
			varFileDTORef.moveVarFileTo(VAM_BASE_PATH, "used");
			varFileService.update(varFileDTORef);
		}
		return varFileDTORef;
	}

	@Override
	protected Map<String, VarFileDTO> process(String sourceDirectory, String targetDirectory) {
		Map<String, VarFileDTO> mVar = new HashMap<>();
		Map<String, VarFileDTO> mLack = processPri(mVar, sourceDirectory, targetDirectory);
		log.warn("--- undeploy: " + targetDirectory + " --- lack depenencies: " + mLack.size());
		return mLack;
	}

}
