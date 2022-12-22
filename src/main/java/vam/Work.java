package vam;

import java.io.File;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import vam.dto.VarFileDTO;
import vam.entity.VarFile;
import vam.util.OsUtils;

@Service("work")
public class Work extends WorkVarFile {

//	private String VAM_ROOT_PATH = "C:/VAM/";
//	private String VAM_GIRL_PATH = VAM_ROOT_PATH + "girl/";
//	private String VAM_FILE_PREFS = VAM_ROOT_PATH + "virt-a-mate 1.20.77.9/AddonPackagesFilePrefs/";

	private String VAR_EXTENSION = ".var";

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

}
