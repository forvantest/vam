package vam;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import vam.dto.MetaJson;
import vam.dto.VarFileDTO;
import vam.entity.VarFile;
import vam.util.FileUtil;

@Service("WorkUnDeployVarFile")
public class WorkUnDeployVarFile extends WorkVarFile {

//	protected void unReference(VarFile varFile) {
//		File realVarFile = new File(varFile.getFullPath() + varFile.getVarFileName());
//		FileUtil.deleteLinkFile(VAM_ADDON_PATH, makeLinkFileName(realVarFile));
//		VarFileDTO varFileDTO = readVarFile(realVarFile.getAbsolutePath());
//		if (Objects.isNull(varFileDTO)) {
//			System.out.println("warn13: varFile not exist: " + realVarFile);
//			return;
//		}
//		varFileDTO.unHide(VAM_FILE_PREFS);
//		if (Objects.nonNull(varFileDTO.getMetaJson())) {
//			Map<String, MetaJson> map = varFileDTO.getMetaJson().getDependenciesMap();
//			map.forEach((k, v) -> {
//				VarFile varFileNew = new VarFile(k, v);
//				List<VarFile> varFileOldList = varFileRepository.findBy(varFileNew);
//				if (!CollectionUtils.isEmpty(varFileOldList)) {
//					unReference(varFileOldList.get(0));
//				} else {
//					System.out.println("---missing depenence: " + k);
//				}
//			});
//		}
//	}
	
	@Override
	void work1(VarFileDTO varFileDTO) {
		varFileDTO.unHide(VAM_FILE_PREFS);
	}
	
//	@Override
//	VarFileDTO work2(VarFileDTO varFileDTO,VarFile varFileRef) {
//		File realVarFile = new File(varFileRef.getFullPath() + varFileRef.getVarFileName());
//		deleteLinkFile(realVarFile);
//		VarFileDTO varFileDTORef = readVarFile(makeVarFileDTO(realVarFile.getAbsolutePath()));
//		varFileDTORef.unHide(VAM_FILE_PREFS);
//		return varFileDTORef;
//	}
	
	@Override
	VarFileDTO work2(String parent,VarFile varFileRef) {
		File realVarFile = new File(varFileRef.getFullPath() + varFileRef.getVarFileName());
		deleteLinkFile(realVarFile);
		VarFileDTO varFileDTORef = readVarFile(makeVarFileDTO(realVarFile.getAbsolutePath()));
		varFileDTORef.unHide(VAM_FILE_PREFS);
		return varFileDTORef;
	}
	
	@Override
	void work3(File realVarFile) {
		deleteLinkFile(realVarFile);
	}
	
	@Override
	void work4(VarFileDTO varFileDTORef) {
		varFileDTORef.unHide(VAM_FILE_PREFS);
	}

	
}
