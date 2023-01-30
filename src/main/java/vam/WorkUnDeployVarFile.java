package vam;

import java.io.File;
import java.util.Objects;

import org.springframework.stereotype.Service;

import vam.dto.VarFileDTO;
import vam.dto.enumration.BestGirl;

@Service("WorkUnDeployVarFile")
public class WorkUnDeployVarFile extends WorkDeployVarFile {

	@Override
	VarFileDTO work2(String parent, VarFileDTO varFileDTORef, String groupName) {
		File realVarFile = new File(varFileDTORef.getFullPath() + varFileDTORef.getVarFileName());
		deleteLinkFile(realVarFile, groupName);
		varFileDTORef.unHide(VAM_ALLFAVORITE_PATH + groupName);
		if (Objects.nonNull(parent)) {
			if (!BestGirl.contains(varFileDTORef.getCreatorName()))
				varFileDTORef.moveVarFileTo(VAM_BASE_PATH, "used");
		}
		return varFileDTORef;
	}

}
