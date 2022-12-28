package vam.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;

public class FileUtil {

	/**
	 * 建立鏈結
	 * 
	 * @param targetFile 目標文件位置
	 * @param linkFile   鏈結文件位置
	 * @return
	 */
	public static boolean createLinkFile(String targetFile, String linkFile) {

		try {
			File sourceFile = new File(targetFile);
			File linkFile1 = new File(linkFile);
			Path source = sourceFile.toPath();
			Path link = linkFile1.toPath();
			Files.createSymbolicLink(source, link);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static boolean createLinkFile(File targetFile, String linkFile) {
		try {
			File linkFile1 = new File(linkFile);
			if (linkFile1.exists()) {
				System.out.println("linkFile already exist: " + linkFile1);
				return false;
			}
			else {
				Path source = targetFile.toPath();
				String linkfolder = readPath(linkFile1.getAbsolutePath());
				checkFolderExist(linkfolder);
				Path link = linkFile1.toPath();
				Files.createSymbolicLink(link, source);
				System.out.println("linkFile create: " + linkFile1);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean checkFolderExist(String linkfolder) {
		File linkfolder1 = new File(linkfolder);
		if (linkfolder1.exists()) {
			return true;
		} else {
			linkfolder1.mkdirs();
			return false;
		}
	}

	private static String readPath(String fullPath) {
		int index = StringUtils.lastIndexOf(fullPath, File.separator);
		if (index >= 0) {
			String path = StringUtils.substring(fullPath, 0, index + 1);
			return path;
		}
		return null;
	}
}
