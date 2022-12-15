package vam;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import vam.dto.VarFile;
import vam.util.OsUtils;
import vam.util.ZipUtils;

public class Work {

	private String VAM_ROOT_PATH = "C:/VAM/";
	private String VAM_GIRL_PATH = VAM_ROOT_PATH + "girl/";

	private String VAR_EXTENSION = ".var";

	ZipUtils zipUtils = new ZipUtils();

	public Work() {
		super();

		if (OsUtils.isUnix()) {
			VAM_ROOT_PATH = "/home/forva/VAM/";
			VAM_GIRL_PATH = VAM_ROOT_PATH + "girl/";
		}
	}

	private VarFile readVarFile(String fullPath) {
		int index = StringUtils.lastIndexOf(fullPath, "/");
		if (index >= 0) {
			String varFileName = StringUtils.substring(fullPath, index + 1);
			String path = StringUtils.substring(fullPath, 0, index+1);
			return readVarFile(path, varFileName);
		}
		return null;
	}
	
	private VarFile readVarFile(String path, String varFileName) {
		File f = new File( path + varFileName );
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
			realHide(file.getAbsolutePath());
		}
	}

	private void realHide(String path) {
		VarFile varFile = readVarFile(path);
	}

	

}
