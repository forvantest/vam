package vam;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import vam.dto.MetaJson;
import vam.dto.VarFileDTO;
import vam.entity.VarFile;
import vam.util.OsUtils;

@Service("work")
public class Work extends WorkVarFile {

//	private String VAM_ROOT_PATH = "C:/VAM/";
//	private String VAM_FILE_PREFS = VAM_ROOT_PATH + "virt-a-mate 1.20.77.9/AddonPackagesFilePrefs/";

	// private String VAR_EXTENSION = ".var";

	public Work() {
		super();

		if (OsUtils.isUnix()) {
			VAM_ROOT_PATH = "/home/forva/VAM/";
			VAM_FILE_PREFS = VAM_ROOT_PATH + "virt-a-mate 1.20.77.9/AddonPackagesFilePrefs/";
		}
	}

	public void allHide(String hideDirectrory) {
		File dir = new File(VAM_ROOT_PATH + hideDirectrory);
		List<VarFileDTO> list = fetchAllVarFiles(dir);
		list.forEach(v -> realHide(v));
	}

	public void allUnHide(String hideDirectrory) {
		File dir = new File(VAM_ROOT_PATH + hideDirectrory);
		List<VarFileDTO> list = fetchAllVarFiles(dir);
		list.forEach(v -> unHide(v));
	}

	public void loadVarFileIntoDB(String targetDirectrory) {
		File dir = new File(VAM_ROOT_PATH + targetDirectrory);
		List<VarFileDTO> listVarFileDTO = fetchAllVarFiles(dir);
		for (VarFileDTO varFileDTO : listVarFileDTO) {
			VarFile varFileNew = new VarFile(varFileDTO);
			List<VarFile> varFileOldList = varFileRepository.findBy(varFileNew);
			VarFile varFileOld = varFileNew.getSameVersion(varFileOldList);
			if (Objects.isNull(varFileOld)) {
				varFileRepository.saveAndFlush(varFileNew);
			}
		}
		Long count = varFileRepository.count();
		System.out.println("total:" + count);
		// List<VarFile> list2 = varFileRepository.findAll();
		// varFileRepository.flush();
	}

	public void createLinkFile() {
		List<String> girlDirectories = new ArrayList<>();
		girlDirectories.add("girl/realclone/");
		createLinkFile(girlDirectories);

		List<String> girlSupportDirectrories = new ArrayList<>();
		girlSupportDirectrories.add("girl/realclone-support/");
		createLinkFile(girlSupportDirectrories);

		for (String girlSupportDirectrory : girlSupportDirectrories) {
			allHide(girlSupportDirectrory);
		}
	}

	public void deploy(String targetDirectory) {
//		List<String> girlDirectories = new ArrayList<>();
//		girlDirectories.add(targetDirectory);
		File dir = new File(VAM_ROOT_PATH + targetDirectory);
		List<VarFileDTO> listVarFileDTO = fetchAllVarFiles(dir);
		for (VarFileDTO varFileDTO : listVarFileDTO) {
			Map<String, MetaJson> map = varFileDTO.getMetaJson().getDependenciesMap();
			map.forEach((k, v) -> {
				VarFile varFileNew = new VarFile(k, v);
				List<VarFile> varFileOldList = varFileRepository.findBy(varFileNew);
				if (!CollectionUtils.isEmpty(varFileOldList))
					reference(varFileOldList.get(0));
				// System.out.println("varFileOldList:" + varFileOldList);
			});
		}
		createLinkFile(dir);

	}

	private void reference(VarFile varFile) {
		if (Objects.nonNull(varFile.getReferenced()))
			varFile.setReferenced(varFile.getReferenced() + 1);
		else
			varFile.setReferenced(1);
		varFileRepository.save(varFile);
		File realVarFile = new File(varFile.getFullPath() + varFile.getVarFileName());
		createLinkFile(realVarFile);
		VarFileDTO varFileDTO = readVarFile(realVarFile.getAbsolutePath());
		realHide(varFileDTO);
	}

}
