package vam;

import java.io.File;
import java.util.Objects;

import org.springframework.stereotype.Service;

import vam.dto.VarFileDTO;

@Service("WorkUnDeployVarFile")
public class WorkUnDeployVarFile extends WorkDeployVarFile {

//	@Override
//	void work1(VarFile varFile) {
//		VarFileDTO varFileDTO = new VarFileDTO(varFile);
//		varFileDTO.unHide(VAM_FILE_PREFS);
//	}

	@Override
	VarFileDTO work2(String parent, VarFileDTO varFileDTORef) {
		File realVarFile = new File(varFileDTORef.getFullPath() + varFileDTORef.getVarFileName());
		deleteLinkFile(realVarFile);
		varFileDTORef.unHide(VAM_FILE_PREFS);
		if (Objects.nonNull(parent)) {
			if (!creatorNameSet.contains(varFileDTORef.getCreatorName()))
				varFileDTORef.moveVarFileTo(VAM_BASE_PATH, "used");
		}
		return varFileDTORef;
	}

//	@Override
//	void work3(File realVarFile) {
//		deleteLinkFile(realVarFile);
//	}
//
//	@Override
//	void work4(VarFile varFile) {
//		VarFileDTO varFileDTO = new VarFileDTO(varFile);
//		if (!creatorNameSet.contains(varFileDTO.getCreatorName()))
//			varFileDTO.moveVarFileTo(VAM_BASE_PATH);
//	}
}
