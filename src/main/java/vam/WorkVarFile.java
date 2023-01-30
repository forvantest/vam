package vam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import vam.dto.VarFileDTO;
import vam.repository.VarFileService;
import vam.util.FileUtil;
import vam.util.OsUtils;
import vam.util.ZipUtils;

@Slf4j
public abstract class WorkVarFile {

	protected String VAM_ROOT_PATH = "C:\\VAM\\";
	protected String VAM_FILE_ADDONPACKAGES = VAM_ROOT_PATH + "virt-a-mate 1.20.77.9\\AddonPackages\\";
	protected String VAM_FILE_ADDONPACKAGESFILEPREFS = VAM_ROOT_PATH
			+ "virt-a-mate 1.20.77.9\\AddonPackagesFilePrefs\\";

	protected String VAM_ALLFAVORITE_PATH = VAM_ROOT_PATH
			+ "virt-a-mate 1.20.77.9\\___addonPackagesFilePrefsswitch ___\\";
	protected String VAM_ADDON_PATH = VAM_ROOT_PATH + "virt-a-mate 1.20.77.9\\___addonpacksswitch ___\\";

	protected String VAM_ALLPACKAGES_PATH = VAM_ROOT_PATH + "AllPackages\\___VarTidied___\\";
	protected String VAM_BASE_PATH = VAM_ALLPACKAGES_PATH + "base\\";
	private String VAM_BROKEN_PATH = VAM_ROOT_PATH + "Broken\\";
	private String VAM_DUPLICATE_PATH = VAM_ROOT_PATH + "Duplicate\\";
	protected String VAR_EXTENSION = ".var";
	protected String DEPEND_TXT_EXTENSION = ".depend.txt";

	@Autowired
	public ZipUtils zipUtils;

	@Autowired
	public VarFileService varFileService;

	@Autowired
	public ObjectMapper objectMapper;

	public WorkVarFile() {
		super();

		if (OsUtils.isUnix()) {
			VAM_ROOT_PATH = "/home/forva/VAM/virt-a-mate 1.20.77.9/";
			VAM_FILE_ADDONPACKAGESFILEPREFS = VAM_ROOT_PATH + "AddonPackagesFilePrefs/";
		}
	}

	protected VarFileDTO makeVarFileDTO(String fullPath) {
		int index = StringUtils.lastIndexOf(fullPath, File.separator);
		if (index >= 0) {
			String varFileName = StringUtils.substring(fullPath, index + 1);
			String path = StringUtils.substring(fullPath, 0, index + 1);
			return new VarFileDTO(path, varFileName);
		}
		return null;
	}

	protected Set<String> readDependTxt(String fullPath) {
		Set<String> varFileRefSet = new HashSet<>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(fullPath));
			String line = "";
			while (line != null) {
				line = reader.readLine();
				if (StringUtils.isEmpty(line))
					continue;
				String[] column = StringUtils.split(line);
				if (column.length < 4) {
					log.warn("---skip " + line);
					continue;
				}
//				System.out.println("s1: " + column[0]);
				varFileRefSet.add(column[0]);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return varFileRefSet;
	}

	protected VarFileDTO readVarFile(VarFileDTO varFileDTOQuery) {
		File f = new File(varFileDTOQuery.getFullPath() + varFileDTOQuery.getVarFileName());
		if (f.exists()) {
			zipUtils.unzipToVarFile(f, varFileDTOQuery);
			return varFileDTOQuery;
		}
		return null;
	}

	protected Set<String> fetchAllVarFiles(File file, String extension) {
		Set<String> varFileRefSet = new HashSet<>();
		if (file.isDirectory()) {
			for (File file1 : file.listFiles()) {
				varFileRefSet.addAll(fetchAllVarFiles(file1, extension));
			}
		} else {
			if (file.getAbsolutePath().endsWith(extension)) {
				if (VAR_EXTENSION.equals(extension)) {
					VarFileDTO varFileDTO = makeVarFileDTO(file.getAbsolutePath());
					varFileRefSet.add(varFileDTO.getVarFileName());
				} else if (DEPEND_TXT_EXTENSION.equals(extension)) {
					varFileRefSet.addAll(readDependTxt(file.getAbsolutePath()));
				}
			}
		}
		return varFileRefSet;
	}

	static int process = 0;
	static int skipProcess = 0;

	protected void allVarFilesToDB(File file) {
		if (file.isDirectory()) {
			for (File file1 : file.listFiles()) {
				allVarFilesToDB(file1);
			}
		} else {
			process++;
			if (process < skipProcess) {
				System.out.print("skip: " + process);
				return;
			} else if (process % 100 == 0) {
				System.out.println("skip: " + process);
			} else if (process % 10 == 0) {
				System.out.print(process);
			} else if (process == 8905) {
				System.out.print("debug");
			} else {
				System.out.print(".");
			}

			if (file.getAbsolutePath().endsWith(VAR_EXTENSION)) {
				VarFileDTO varFileDTOQuery = makeVarFileDTO(file.getAbsolutePath());
				List<VarFileDTO> varFileOldList2 = varFileService.findBy(varFileDTOQuery);
				if (CollectionUtils.isEmpty(varFileOldList2)) {
					VarFileDTO varFileDTONew = readVarFile(varFileDTOQuery);
					if (Objects.nonNull(varFileDTONew.getException())) {
						moveVarFileToBroken(varFileDTONew);
					} else {
						varFileDTONew.easySceneJson();
						varFileService.save(varFileDTONew);
					}
				} else {
					VarFileDTO varFileDTOOld = varFileOldList2.get(0);

//					VarFileDTO varFileDTONew = readVarFile(varFileDTOQuery);
//					if (varFileDTONew.getSceneJsonList() != varFileDTOOld.getSceneJsonList()) {
//						varFileDTOOld.setSceneJsonList(varFileDTONew.getSceneJsonList());
//						varFileService.update(varFileDTOOld);
//					}

					if (varFileDTOQuery.getFullPath().equals(varFileDTOOld.getFullPath())
							&& varFileDTOQuery.getVarFileName().equals(varFileDTOOld.getVarFileName())) {
						return;
					} else {
						File f = new File(varFileDTOOld.getFullPath() + varFileDTOOld.getVarFileName());
						if (f.exists()) {
							System.out.print("---duplicate:" + varFileDTOQuery);
							varFileDTOQuery.moveVarFileTo(VAM_DUPLICATE_PATH, "duplicate1");
							return;
						} else {
							log.warn("---db data deprecate:" + varFileDTOOld);
							varFileDTOOld.setFullPath(varFileDTOQuery.getFullPath());
							varFileDTOOld.setVarFileName(varFileDTOQuery.getVarFileName());
							varFileService.update(varFileDTOOld);
							return;
						}
					}
				}

//				VarFileDTO varFileDTONew = readVarFile(varFileDTOQuery);
//				if (Objects.nonNull(varFileDTONew.getException())) {
//					moveVarFileToBroken(varFileDTONew);
//					return;
//				}
////				VarFile varFileNew = new VarFile(varFileDTONew);
////				List<VarFileDTO> varFileDTOOldList = varFileService.findBy(varFileDTONew);
////				VarFileDTO varFileOld = varFileDTONew.getSameVersion(varFileOldList);
//				if (CollectionUtils.isEmpty(varFileDTOOldList)) {
//					varFileService.save(varFileDTONew);
//				} else {
//					VarFileDTO varFileDTOOld = varFileDTOOldList.get(0);
//					if (varFileDTONew.getSceneJsonList() != varFileDTOOld.getSceneJsonList()) {
//						varFileDTOOld.setSceneJsonList(varFileDTONew.getSceneJsonList());
//						varFileService.save(varFileDTOOld);
//					} else if (!varFileDTOOld.getFullPath().equals(varFileDTONew.getFullPath())
//							|| !varFileDTOOld.getVarFileName().equals(varFileDTONew.getVarFileName())) {
//						// System.out.print("duplicate:" + varFileNew);
//						varFileDTONew.moveVarFileTo(VAM_DUPLICATE_PATH, "duplicate2");
//					}
//				}
			}
		}
	}

	protected void moveVarFileToBroken(VarFileDTO varFileDTONew) {
		Path sDir = Paths.get(varFileDTONew.getFullPath(), varFileDTONew.getVarFileName());
		Path tDir = Paths.get(VAM_BROKEN_PATH, varFileDTONew.getVarFileName());
		try {
			System.out.println("\n---moving Broken: " + sDir);
			Files.move(sDir, tDir, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void createLinkFile(File file, String groupName) {
		if (file.isDirectory()) {
			for (File file1 : file.listFiles()) {
				createLinkFile(file1, groupName);
			}
		} else {
			if (file.getAbsolutePath().endsWith(VAR_EXTENSION)) {
				String linkFileName = makeLinkFileName(file, groupName);
				File linkFile = new File(linkFileName);
				if (!linkFile.exists()) {
					boolean b = FileUtil.createLinkFile(file, linkFile);
					if (!b)
						log.warn("\n---failed create link: " + file);
				}
			}
		}
	}

	protected void deleteLinkFile(File file, String groupName) {
		if (file.isDirectory()) {
			for (File file1 : file.listFiles()) {
				deleteLinkFile(file1, groupName);
			}
		} else {
			if (file.getAbsolutePath().endsWith(VAR_EXTENSION))
				FileUtil.deleteLinkFile(VAM_ADDON_PATH, makeLinkFileName(file, groupName));
		}
	}

	protected String makeLinkFileName(File file, String groupName) {
		String linkFileName = StringUtils.replace(file.getAbsolutePath(), VAM_ALLPACKAGES_PATH,
				VAM_ADDON_PATH + groupName + "\\___VarsLink___\\");
		return linkFileName;
	}

	protected void createLinkFile(List<String> targetDirectrories, String groupName) {
		for (String targetDirectrory : targetDirectrories) {
			File file = new File(VAM_ROOT_PATH + targetDirectrory);
			createLinkFile(file, groupName);
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

	protected Map<String, String> cuteMap(Map<String, String> m1, Map<String, String> m2) {
		Map<String, String> map3 = new HashMap<>();
		for (String key : m2.keySet()) {
			if (!m1.containsKey(key)) {
				map3.put(key, m2.get(key));
			}
		}
		return map3;
	}

	protected VarFileDTO findSuitableVarFile(VarFileDTO varFileDTO) {
		List<VarFileDTO> varFileOldList = null;
		if ("latest".equals(varFileDTO.getVersion())) {
			return findSuitableVarFile2(varFileDTO);
		} else {
			varFileOldList = varFileService.findBy(varFileDTO);
			if (CollectionUtils.isEmpty(varFileOldList)) {
				if (StringUtils.startsWith(varFileDTO.getCreatorName(), "https"))
					System.out.println("debug");
				log.debug("---missing depenence2: " + varFileDTO.getCreatorName() + "." + varFileDTO.getPackageName()
						+ "." + varFileDTO.getVersion());

				return findSuitableVarFile2(varFileDTO);
			} else {
				VarFileDTO varFile = varFileOldList.get(0);
				return varFile;
			}
		}
	}

	private VarFileDTO findSuitableVarFile2(VarFileDTO varFileDTO) {
		List<VarFileDTO> varFileOldList = varFileService.findByName(varFileDTO);
		if (CollectionUtils.isEmpty(varFileOldList)) {
			log.debug("---missing depenence1: " + varFileDTO.getCreatorName() + "." + varFileDTO.getPackageName() + "."
					+ varFileDTO.getVersion());
			return null;
		} else {
			Collections.sort(varFileOldList);
			VarFileDTO varFile = varFileOldList.get(0);
			return varFile;
		}
	}

	protected void echo(int total) {
		process++;
		if (process % 100 == 0) {
			System.out.println("done: " + process + "/" + total);
		} else if (process % 10 == 0) {
			System.out.print(process);
		} else {
			System.out.print(".");
		}
	}
}
