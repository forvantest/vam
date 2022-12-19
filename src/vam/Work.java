package vam;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import vam.dto.VarFile;
import vam.util.OsUtils;
import vam.util.ZipUtils;

public class Work {

	private String VAM_ROOT_PATH = "C:/VAM/";
	private String VAM_GIRL_PATH = VAM_ROOT_PATH + "girl/";
	private String VAM_FILE_PREFS = VAM_ROOT_PATH + "virt-a-mate 1.20.77.9/AddonPackagesFilePrefs/";

	private String VAR_EXTENSION = ".var";

	ZipUtils zipUtils = new ZipUtils();

	public Work() {
		super();

		if (OsUtils.isUnix()) {
			VAM_ROOT_PATH = "/home/forva/VAM/";
			VAM_GIRL_PATH = VAM_ROOT_PATH + "girl/";
		}
	}

	private String readPath(String fullPath) {
		int index = StringUtils.lastIndexOf(fullPath, File.separator);
		if (index >= 0) {
			String path = StringUtils.substring(fullPath, 0, index + 1);
			return path;
		}
		return null;
	}

	private VarFile readVarFile(String fullPath) {
		int index = StringUtils.lastIndexOf(fullPath, File.separator);
		if (index >= 0) {
			String varFileName = StringUtils.substring(fullPath, index + 1);
			String path = StringUtils.substring(fullPath, 0, index + 1);
			return readVarFile(path, varFileName);
		}
		return null;
	}

	private VarFile readVarFile(String path, String varFileName) {
		File f = new File(path + varFileName);
		if (f.exists()) {
			VarFile varFile = new VarFile(varFileName);
			zipUtils.unzipToVarFile(f, varFile);
			return varFile;
		}
		return null;
	}

	public void mainHide(String hideDirectrory) {
		File dir = new File(VAM_ROOT_PATH + hideDirectrory);
		if (dir.isDirectory()) {
			for (File file : dir.listFiles()) {
				hide(file);
			}
		}
	}

	private void hide(File file) {
		if (file.isDirectory()) {
			for (File file1 : file.listFiles()) {
				hide(file1);
			}
		} else {
			if(file.getAbsolutePath().endsWith(VAR_EXTENSION))
				realHide(file.getAbsolutePath());
		}
	}

	private void realHide(String path) {
		VarFile varFile = readVarFile(path);
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
					System.out.println("!hide:" + hideFile);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
