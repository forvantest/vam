package vam;

import java.io.File;

import org.springframework.stereotype.Service;

import vam.dto.VarFileDTO;
import vam.entity.VarFile;

@Service("WorkUnDeployVarFile")
public class WorkUnDeployVarFile extends WorkDeployVarFile {

	@Override
	void work1(VarFileDTO varFileDTO) {
		varFileDTO.unHide(VAM_FILE_PREFS);
	}

	@Override
	VarFileDTO work2(String parent, VarFile varFileRef) {
		File realVarFile = new File(varFileRef.getFullPath() + varFileRef.getVarFileName());
		deleteLinkFile(realVarFile);
		VarFileDTO varFileDTORef = new VarFileDTO(varFileRef);
		varFileDTORef.unHide(VAM_FILE_PREFS);
		return varFileDTORef;
	}

	@Override
	void work3(File realVarFile) {
		deleteLinkFile(realVarFile);
	}

}
