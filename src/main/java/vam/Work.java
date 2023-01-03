package vam;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vam.dto.VarFileDTO;

@Service("work")
public class Work extends WorkVarFile {

//	private String VAM_ROOT_PATH = "C:/VAM/";
//	private String VAM_FILE_PREFS = VAM_ROOT_PATH + "virt-a-mate 1.20.77.9/AddonPackagesFilePrefs/";

	// private String VAR_EXTENSION = ".var";

//	@Autowired
//	public WorkDeployVarFile workDeployVarFile;

	@Autowired
	public WorkUnDeployVarFile workUnDeployVarFile;

	public void allHide(String hideDirectrory) {
		File dir = new File(VAM_ROOT_PATH + hideDirectrory);
		List<VarFileDTO> list = fetchAllVarFiles(dir, VAR_EXTENSION);
		list.forEach(v -> v.realHide(VAM_FILE_PREFS));
	}

	public void allUnHide(String hideDirectrory) {
		File dir = new File(VAM_ROOT_PATH + hideDirectrory);
		List<VarFileDTO> list = fetchAllVarFiles(dir, VAR_EXTENSION);
		list.forEach(v -> v.unHide(VAM_FILE_PREFS));
	}

	public void loadVarFileIntoDB(String targetDirectrory) {
		File dir = new File(VAM_ROOT_PATH + targetDirectrory);
		allVarFilesToDB(dir);
		Long count = varFileRepository.count();
		System.out.println("total:" + count);
	}

//	public void createLinkFile() {
//		List<String> girlDirectories = new ArrayList<>();
//		girlDirectories.add("girl/realclone/");
//		createLinkFile(girlDirectories);
//
//		List<String> girlSupportDirectrories = new ArrayList<>();
//		girlSupportDirectrories.add("girl/realclone-support/");
//		createLinkFile(girlSupportDirectrories);
//
//		for (String girlSupportDirectrory : girlSupportDirectrories) {
//			allHide(girlSupportDirectrory);
//		}
//	}

	public void deploy(String targetDirectory) {
		process(targetDirectory);
	}

	public void unDeploy(String targetDirectory) {
		workUnDeployVarFile.process(targetDirectory);
	}
}
