package vam.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {

	/**
	 * 建立鏈結
	 * 
	 * @param targetFile 目標文件位置
	 * @param linkFile   鏈結文件位置
	 * @return
	 */
	private static boolean createLinkFile(String link1, String target1) {

		try {
			File targetFile = new File(target1);
			File linkFile1 = new File(link1);
			Path target = targetFile.toPath();
			Path link = linkFile1.toPath();
			deleteIfExists(link1);
			Files.createSymbolicLink(link, target);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private static void deleteIfExists(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
	}

	public static void deleteFolderIfEmpty(String folderPath) {
		File file = new File(folderPath);
		if (file.exists() && file.isDirectory() && file.list().length == 0) {
			file.delete();
		}
	}

	public static boolean createLinkFile(File targetFile, File linkFile) {
		try {
			if (!targetFile.exists()) {
				System.out.println("warn7: targetFile doesn't exist: " + targetFile);
				return false;
			} else {
				Path source = targetFile.toPath();
				String linkfolder = readPath(linkFile.getAbsolutePath());
				checkFolderExist(linkfolder);
				Path link = linkFile.toPath();
				Files.createSymbolicLink(link, source);
				log.info("linkFile create: " + linkFile);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean checkFolderExist(String linkfolder) {
		File linkfolder1 = new File(linkfolder);
		if (linkfolder1.exists()) {
			return true;
		} else {
			linkfolder1.mkdirs();
			return false;
		}
	}

	public static boolean checkFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}

	private static String readPath(String fullPath) {
		int index = StringUtils.lastIndexOf(fullPath, File.separator);
		if (index >= 0) {
			String path = StringUtils.substring(fullPath, 0, index + 1);
			return path;
		}
		return null;
	}

	public static void deleteLinkFile(String VAM_ADDON_PATH, String linkFileName) {
		File linkFile = new File(linkFileName);
		if (linkFile.exists()) {
			linkFile.delete();
			log.info("---linkFile deleted: " + linkFile);
		}
		String subPath = linkFileName;
		for (int i = 0; i < 5; i++) {
			subPath = clearLinkFolder(VAM_ADDON_PATH, subPath);
			if (Objects.isNull(subPath))
				break;
		}
	}

	private static String clearLinkFolder(String VAM_ADDON_PATH, String fullPath) {
		int index = StringUtils.lastIndexOf(fullPath, "\\");
		if (index > 0) {
			String linkFolderPath = StringUtils.substring(fullPath, 0, index);
			if (!StringUtils.contains(VAM_ADDON_PATH, linkFolderPath)) {
				File linkFolder = new File(linkFolderPath);
				if (linkFolder.exists() && linkFolder.list().length == 0) {
					log.info("---linkFolder deleted: " + linkFolder);
					linkFolder.delete();
					return linkFolderPath;
				}
			}
		}
		return null;
	}

	public static void createLinkFile2(File file, String linkFileName, boolean overwrite) {
		File linkFile = new File(linkFileName);
		boolean fileExist = linkFile.exists();
		boolean isSymbolicLink = Files.isSymbolicLink(linkFile.toPath());
		if (fileExist) {
			if (isSymbolicLink) {
				if (overwrite) {
					if (linkFile.delete()) {
						boolean b = createLinkFile(file, linkFile);
						if (!b)
							log.warn("\n---failed create link: " + file);
					}
				} else
					log.debug("---SymbolicLink exist1, skip: " + linkFileName);
			} else {
				if (linkFile.delete()) {
					boolean b = createLinkFile(file, linkFile);
					if (!b)
						log.warn("\n---failed create link: " + file);
				}
			}
		} else {
			if (isSymbolicLink) {
				log.debug("---SymbolicLink exist2, skip: " + linkFileName);
			} else {
				boolean b = createLinkFile(file, linkFile);
				if (!b)
					log.warn("\n---failed create link: " + file);
			}
		}
	}

}
