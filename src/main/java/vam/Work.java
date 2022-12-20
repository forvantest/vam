package vam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vam.dto.VarFileDTO;
import vam.entity.VarFile;
import vam.repository.VarFileRepository;
import vam.util.OsUtils;
import vam.util.ZipUtils;

@Service("work")
public class Work {

	private String VAM_ROOT_PATH = "C:/VAM/";
	private String VAM_GIRL_PATH = VAM_ROOT_PATH + "girl/";
	private String VAM_FILE_PREFS = VAM_ROOT_PATH + "virt-a-mate 1.20.77.9/AddonPackagesFilePrefs/";

	private String VAR_EXTENSION = ".var";

	ZipUtils zipUtils = new ZipUtils();

	@Autowired
	VarFileRepository varFileRepository;

	public Work() {
		super();

		if (OsUtils.isUnix()) {
			VAM_ROOT_PATH = "/home/forva/VAM/";
			VAM_GIRL_PATH = VAM_ROOT_PATH + "girl/";
			VAM_FILE_PREFS = VAM_ROOT_PATH + "virt-a-mate 1.20.77.9/AddonPackagesFilePrefs/";
		}
	}

//	private String readPath(String fullPath) {
//		int index = StringUtils.lastIndexOf(fullPath, File.separator);
//		if (index >= 0) {
//			String path = StringUtils.substring(fullPath, 0, index + 1);
//			return path;
//		}
//		return null;
//	}

	private VarFileDTO readVarFile(String fullPath) {
		int index = StringUtils.lastIndexOf(fullPath, File.separator);
		if (index >= 0) {
			String varFileName = StringUtils.substring(fullPath, index + 1);
			String path = StringUtils.substring(fullPath, 0, index + 1);
			return readVarFile(path, varFileName);
		}
		return null;
	}

	private VarFileDTO readVarFile(String path, String varFileName) {
		File f = new File(path + varFileName);
		if (f.exists()) {
			VarFileDTO varFile = new VarFileDTO(varFileName);
			zipUtils.unzipToVarFile(f, varFile);
			return varFile;
		}
		return null;
	}

	public void mainHide(String hideDirectrory) {
		File dir = new File(VAM_ROOT_PATH + hideDirectrory);
		List<VarFileDTO> list = fetchAllVarFiles(dir);
		list.forEach(v -> realHide(v));
	}

	private List<VarFileDTO> fetchAllVarFiles(File file) {
		List<VarFileDTO> list = new ArrayList<>();
		if (file.isDirectory()) {
			for (File file1 : file.listFiles()) {
				list.addAll(fetchAllVarFiles(file1));
			}
		} else {
			if (file.getAbsolutePath().endsWith(VAR_EXTENSION))
				list.add(readVarFile(file.getAbsolutePath()));
		}
		return list;
	}

	private void realHide(VarFileDTO varFile) {
		String PATH_HIDE_PATH = VAM_FILE_PREFS + varFile.makeHidePath();
		File hidePath = new File(PATH_HIDE_PATH);
		if (!hidePath.exists()) {
			hidePath.mkdirs();
		}
		try {
			if (Objects.nonNull(varFile.getSceneJson())) {
				String PATH_HIDE_FILE = PATH_HIDE_PATH + varFile.getSceneJson().makeHideEmptyFile();
				File hideFile = new File(PATH_HIDE_FILE);
				if (!hidePath.exists()) {
					hideFile.createNewFile();
					System.out.println("--hide:" + hideFile);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadVarFileIntoDB(String targetDirectrory) {
		// connect();
		File dir = new File(VAM_ROOT_PATH + targetDirectrory);
		List<VarFileDTO> listVarFileDTO = fetchAllVarFiles(dir);
		for (VarFileDTO varFileDTO : listVarFileDTO) {
			VarFile varFileNew = new VarFile(varFileDTO);
			VarFile varFileOld=varFileRepository.findBy(varFileDTO);
			varFileRepository.saveAndFlush(varFileNew);
		}
		Long ll = varFileRepository.count();
//		VarFile varFile = varFileRepository.getById(11l);
		List<VarFile> list2 = varFileRepository.findAll();
		// varFileRepository.flush();
	}

//	public void connect() {
//		Connection conn = null;
//		try {
//			// db parameters
//			String url = "jdbc:sqlite:/home/forva/git/vam/resource/SQLiteVam.sqlite";
//			// create a connection to the database
//			conn = DriverManager.getConnection(url);
//
//			System.out.println("Connection to SQLite has been established.");
//
//		} catch (SQLException e) {
//			System.out.println(e.getMessage());
//		} finally {
//			try {
//				if (conn != null) {
//					conn.close();
//				}
//			} catch (SQLException ex) {
//				System.out.println(ex.getMessage());
//			}
//		}
//	}

}
