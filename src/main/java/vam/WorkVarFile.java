package vam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import vam.dto.MetaJson;
import vam.dto.VarFileDTO;
import vam.entity.VarFile;
import vam.repository.VarFileRepository;
import vam.util.FileUtil;
import vam.util.ZipUtils;

public abstract class WorkVarFile {

	protected String VAM_ROOT_PATH = "C:\\VAM\\virt-a-mate 1.20.77.9\\";
	protected String VAM_FILE_PREFS = VAM_ROOT_PATH + "AddonPackagesFilePrefs\\";
	protected String VAM_ALLPACKAGES_PATH = VAM_ROOT_PATH + "AllPackages\\";
	private String VAM_ADDON_PATH = VAM_ROOT_PATH + "AddonPackages\\";
	private String VAM_BROKEN_PATH = VAM_ROOT_PATH + "Broken\\";
	private String VAM_DUPLICATE_PATH = VAM_ROOT_PATH + "Duplicate\\";
	protected String VAR_EXTENSION = ".var";
	protected String DEPEND_TXT_EXTENSION = ".depend.txt";

	ZipUtils zipUtils = new ZipUtils();

	@Autowired
	public VarFileRepository varFileRepository;

//	protected VarFileDTO readVarFile(String fullPath) {
//		int index = StringUtils.lastIndexOf(fullPath, File.separator);
//		if (index >= 0) {
//			String varFileName = StringUtils.substring(fullPath, index + 1);
//			String path = StringUtils.substring(fullPath, 0, index + 1);
//			return readVarFile(makeVarFileDTO(fullPath));
//		}
//		return null;
//	}

	protected VarFileDTO makeVarFileDTO(String fullPath) {
		int index = StringUtils.lastIndexOf(fullPath, File.separator);
		if (index >= 0) {
			String varFileName = StringUtils.substring(fullPath, index + 1);
			String path = StringUtils.substring(fullPath, 0, index + 1);
			return new VarFileDTO(path, varFileName);
		}
		return null;
	}

	protected List<VarFileDTO> readDependTxt(String fullPath) {
		List<VarFileDTO> varFileDTOList = new ArrayList<>();
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
					System.out.println("---skip " + line);
					continue;
				}
				// System.out.println("s1: " + column[0]);
				varFileDTOList.add(new VarFileDTO(null, column[0]));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return varFileDTOList;
	}

	protected VarFileDTO readVarFile(VarFileDTO varFileDTOQuery) {
		File f = new File(varFileDTOQuery.getFullPath() + varFileDTOQuery.getVarFileName());
		if (f.exists()) {
			zipUtils.unzipToVarFile(f, varFileDTOQuery);
			return varFileDTOQuery;
		}
		return null;
	}

	protected List<VarFileDTO> fetchAllVarFiles(File file, String extension) {
		List<VarFileDTO> list = new ArrayList<>();
		if (file.isDirectory()) {
			for (File file1 : file.listFiles()) {
				list.addAll(fetchAllVarFiles(file1, extension));
			}
		} else {
			if (file.getAbsolutePath().endsWith(extension)) {
				if (VAR_EXTENSION.equals(extension)) {
					list.add(readVarFile(makeVarFileDTO(file.getAbsolutePath())));
				} else if (DEPEND_TXT_EXTENSION.equals(extension)) {
					list.addAll(readDependTxt(file.getAbsolutePath()));
				}
			}
		}
		return list;
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
			} else {
				System.out.print(".");
			}

			if (file.getAbsolutePath().endsWith(VAR_EXTENSION)) {
				VarFileDTO varFileDTOQuery = makeVarFileDTO(file.getAbsolutePath());
				List<VarFile> varFileOldList2 = varFileRepository.findBy(varFileDTOQuery);
				if (!CollectionUtils.isEmpty(varFileOldList2)) {
					VarFile varFile = varFileOldList2.get(0);
					if (varFileDTOQuery.getFullPath().equals(varFile.getFullPath())
							&& varFileDTOQuery.getVarFileName().equals(varFile.getVarFileName())) {
						return;
					} else {
						File f = new File(varFile.getFullPath() + varFile.getVarFileName());
						if (f.exists()) {
							System.out.print("---duplicate:" + varFileDTOQuery);
							moveVarFileToDuplicate(varFileDTOQuery);
							return;
						} else {
							System.out.print("---db data deprecate:" + varFile);
							varFile.setFullPath(varFileDTOQuery.getFullPath());
							varFile.setVarFileName(varFileDTOQuery.getVarFileName());
							varFileRepository.saveAndFlush(varFile);
							return;
						}
					}
				}

				VarFileDTO varFileDTONew = readVarFile(varFileDTOQuery);
				if (Objects.nonNull(varFileDTONew.getException())) {
					moveVarFileToBroken(varFileDTONew);
					return;
				}
				VarFile varFileNew = new VarFile(varFileDTONew);
				List<VarFile> varFileOldList = varFileRepository.findBy(varFileNew);
				VarFile varFileOld = varFileNew.getSameVersion(varFileOldList);
				if (Objects.isNull(varFileOld)) {
					varFileRepository.saveAndFlush(varFileNew);
				} else if (varFileNew.getFemaleCount() != varFileOld.getFemaleCount()) {
					varFileOld.setFemaleCount(varFileNew.getFemaleCount());
					varFileRepository.saveAndFlush(varFileOld);
				} else if (!varFileOld.getFullPath().equals(varFileNew.getFullPath())
						|| !varFileOld.getVarFileName().equals(varFileNew.getVarFileName())) {
					// System.out.print("duplicate:" + varFileNew);
					moveVarFileToDuplicate(varFileNew);
				}
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

	protected void moveVarFileToDuplicate(VarFileDTO varFileNew) {
		moveVarFileToDuplicate(new VarFile(varFileNew));
	}

	protected void moveVarFileToDuplicate(VarFile varFileNew) {
		Path sDir = Paths.get(varFileNew.getFullPath(), varFileNew.getVarFileName());
		Path tDir = Paths.get(VAM_DUPLICATE_PATH, varFileNew.getVarFileName());
		try {
			System.out.println("\n---moving Duplicate: " + sDir);
			Files.move(sDir, tDir, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void createLinkFile(File file) {
		if (file.isDirectory()) {
			for (File file1 : file.listFiles()) {
				createLinkFile(file1);
			}
		} else {
			if (file.getAbsolutePath().endsWith(VAR_EXTENSION))
				FileUtil.createLinkFile(file, makeLinkFileName(file));
		}
	}

	protected void deleteLinkFile(File file) {
		if (file.isDirectory()) {
			for (File file1 : file.listFiles()) {
				deleteLinkFile(file1);
			}
		} else {
			if (file.getAbsolutePath().endsWith(VAR_EXTENSION))
				FileUtil.deleteLinkFile(VAM_ADDON_PATH, makeLinkFileName(file));
		}
	}

	protected String makeLinkFileName(File file) {
		String linkFileName = StringUtils.replace(file.getAbsolutePath(), VAM_ALLPACKAGES_PATH, VAM_ADDON_PATH);
		return linkFileName;
	}

	protected void createLinkFile(List<String> targetDirectrories) {
		for (String targetDirectrory : targetDirectrories) {
			File file = new File(VAM_ROOT_PATH + targetDirectrory);
			createLinkFile(file);
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

	protected void process(String targetDirectory) {
		File dir = new File(VAM_ROOT_PATH + targetDirectory);
		List<VarFileDTO> listFavVarFileDTO = fetchAllVarFiles(dir, VAR_EXTENSION);
		for (VarFileDTO varFileDTO : listFavVarFileDTO) {
			processVarFile(varFileDTO);
			work1(varFileDTO);
		}

		List<VarFileDTO> listDependTxtDTO = fetchAllVarFiles(dir, DEPEND_TXT_EXTENSION);
//		for (int i = 0; i < listDependTxtDTO.size(); i++) {
//			VarFileDTO dependTxtDTO = listDependTxtDTO.get(i);
//			System.out.println(dependTxtDTO.getCreatorName() + "." + dependTxtDTO.getPackageName());
//			if ("hazmhox.vammoan".equals(dependTxtDTO.getCreatorName() + "." + dependTxtDTO.getPackageName())) {
//				processDependTxt(dependTxtDTO);
//				work1(dependTxtDTO);
//				System.out.println(i + "   " + dependTxtDTO.getCreatorName() + "." + dependTxtDTO.getPackageName());
//			}
//		}
		for (VarFileDTO dependTxtDTO : listDependTxtDTO) {
			processDependTxt(dependTxtDTO);
			work1(dependTxtDTO);
		}
	}

	protected void processVarFile(VarFileDTO varFileDTO) {
		String fullPathName = varFileDTO.getFullPath() + varFileDTO.getVarFileName();
		File realVarFile = new File(fullPathName);
		if (realVarFile.exists()) {
			work3(realVarFile);
		} else {
			System.out.println("warn8: varFile doesn't exist: " + fullPathName);
		}

		if (Objects.nonNull(varFileDTO.getMetaJson())) {
			varFileDTO.getMetaJson().getDependenciesMap().forEach((k, v) -> processDependenciesMap(varFileDTO, k, v));
		}
	}

	protected void processDependenciesMap(VarFileDTO varFileDTOParent, String k, MetaJson metaJson) {
		VarFileDTO varFileQuery = new VarFileDTO(null, k);
		VarFile varFileRef = findSuitableVarFile(varFileQuery);
		if (Objects.nonNull(varFileRef)) {
			VarFileDTO varFileDTORef = work2(varFileDTOParent, varFileRef);
			if (Objects.nonNull(varFileDTORef))
				processVarFile(varFileDTORef);
		} else {
			varFileQuery.setMetaJson(metaJson);
			processVarFile(varFileQuery);
		}
	}

	protected void processDependTxt(VarFileDTO varFileDTO) {
//		String fullPathName = varFileDTO.getFullPath() + varFileDTO.getVarFileName();
//		File realVarFile = new File(fullPathName);
//		if (!realVarFile.exists()) {
//			System.out.println("warn8: varFile doesn't exist: " + fullPathName);
//			return;
//		}
		VarFile varFile = findSuitableVarFile(varFileDTO);
		if (Objects.isNull(varFile))
			return;

		File realVarFile = new File(varFile.getFullPath() + varFile.getVarFileName());
		work3(realVarFile);
		VarFileDTO varFileDTORef = readVarFile(makeVarFileDTO(realVarFile.getAbsolutePath()));
		if (Objects.isNull(varFileDTORef)) {
			System.out.println("---missing depenence3: " + varFile.getCreatorName() + "." + varFile.getPackageName()
					+ "." + varFile.getVersion());
			return;
		}
		work4(varFileDTORef);
		if (Objects.nonNull(varFileDTORef.getMetaJson())) {
			varFileDTORef.getMetaJson().getDependenciesMap()
					.forEach((k, v) -> processDependenciesMap(varFileDTORef, k, v));
		}
	}

	private VarFile findSuitableVarFile(VarFileDTO varFileDTO) {
		List<VarFile> varFileOldList = null;
		if ("latest".equals(varFileDTO.getVersion())) {
			return findSuitableVarFile2(varFileDTO);
		} else {
			varFileOldList = varFileRepository.findBy(varFileDTO);
			if (CollectionUtils.isEmpty(varFileOldList)) {
				if (StringUtils.startsWith(varFileDTO.getCreatorName(), "https"))
					System.out.println("debug");
				System.out.println("---missing depenence2: " + varFileDTO.getCreatorName() + "."
						+ varFileDTO.getPackageName() + "." + varFileDTO.getVersion());

				return findSuitableVarFile2(varFileDTO);
			} else {
				VarFile varFile = varFileOldList.get(0);
				return varFile;
			}
		}
	}

	private VarFile findSuitableVarFile2(VarFileDTO varFileDTO) {
		List<VarFile> varFileOldList = varFileRepository.findBy(new VarFile(varFileDTO));
		if (CollectionUtils.isEmpty(varFileOldList)) {
			System.out.println("---missing depenence1: " + varFileDTO.getCreatorName() + "."
					+ varFileDTO.getPackageName() + "." + varFileDTO.getVersion());
			return null;
		} else {
			VarFile varFile = varFileOldList.get(0);
			return varFile;
		}
	}

	void work1(VarFileDTO varFileDTO) {
		varFileDTO.favorite(VAM_FILE_PREFS);
		List<VarFile> varFileFavList = varFileRepository.findBy(varFileDTO);
		if (!CollectionUtils.isEmpty(varFileFavList)) {
			VarFile varFile = varFileFavList.get(0);
			varFile.increaseFavorite();
			varFileRepository.save(varFile);
		}
	}

	VarFileDTO work2(VarFileDTO varFileDTOParent, VarFile varFileRef) {
		varFileRef.increaseReference(varFileDTOParent);
		varFileRepository.save(varFileRef);
		File realVarFile = new File(varFileRef.getFullPath() + varFileRef.getVarFileName());
		if (!realVarFile.exists()) {
			System.out.println("---db data deprecate2: " + varFileRef);
			varFileRepository.delete(varFileRef);
			return null;
		}
		VarFileDTO varFileDTORef = readVarFile(makeVarFileDTO(realVarFile.getAbsolutePath()));
		varFileDTORef.realHide(VAM_FILE_PREFS);
		return varFileDTORef;
	}

	void work3(File realVarFile) {
		createLinkFile(realVarFile);
	}

	void work4(VarFileDTO varFileDTORef) {
		varFileDTORef.realHide(VAM_FILE_PREFS);
	}

}
