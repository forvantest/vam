package vam;

import java.io.File;
import java.util.Objects;

import org.springframework.stereotype.Service;

import vam.dto.VarFileDTO;

@Service("WorkUnDeployVarFile")
public class WorkUnDeployVarFile extends WorkDeployVarFile {

	@Override
	VarFileDTO work2(String parent, VarFileDTO varFileDTORef, String groupName) {
		File realVarFile = new File(varFileDTORef.getFullPath() + varFileDTORef.getVarFileName());
		deleteLinkFile(realVarFile, groupName);
		varFileDTORef.unHide(VAM_FILE_PREFS);
		if (Objects.nonNull(parent)) {
			if (!creatorNameSet.contains(varFileDTORef.getCreatorName()))
				varFileDTORef.moveVarFileTo(VAM_BASE_PATH, "used");
		}
		return varFileDTORef;
	}

}
