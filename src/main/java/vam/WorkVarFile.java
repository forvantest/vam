package vam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import vam.dto.VarFileDTO;
import vam.repository.VarFileRepository;
import vam.util.ZipUtils;

public abstract class WorkVarFile {

	protected String VAM_ROOT_PATH = "C:/VAM/";
	protected String VAM_FILE_PREFS = VAM_ROOT_PATH + "virt-a-mate 1.20.77.9/AddonPackagesFilePrefs/";

	private String VAR_EXTENSION = ".var";

	ZipUtils zipUtils = new ZipUtils();

	@Autowired
	VarFileRepository varFileRepository;

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

	private VarFileDTO readVarFile(String fullPath, String varFileName) {
		File f = new File(fullPath + varFileName);
		if (f.exists()) {
			VarFileDTO varFileDTO = new VarFileDTO(fullPath, varFileName);
			zipUtils.unzipToVarFile(f, varFileDTO);
			return varFileDTO;
		}
		return null;
	}

	protected List<VarFileDTO> fetchAllVarFiles(File file) {
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

	protected void realHide(VarFileDTO varFile) {
		try {
			if (Objects.nonNull(varFile.getSceneJson())) {
				String PATH_HIDE_PATH = VAM_FILE_PREFS + varFile.makeHidePath();
				File hidePath = new File(PATH_HIDE_PATH);
				if (!hidePath.exists()) {
					hidePath.mkdirs();
				}
				String PATH_HIDE_FILE = PATH_HIDE_PATH + varFile.getSceneJson().makeHideEmptyFile();
				File hideFile = new File(PATH_HIDE_FILE);
				if (!hideFile.exists()) {
					hideFile.createNewFile();
					System.out.println("+++hide:" + hideFile);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void unHide(VarFileDTO varFile) {
		String PATH_HIDE_PATH = VAM_FILE_PREFS + varFile.makeTitle();
		File hidePath = new File(PATH_HIDE_PATH);
		if (hidePath.exists()) {
			try {
				FileUtils.forceDelete(hidePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("---unhide:" + hidePath);
		}
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
