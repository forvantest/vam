package vam;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import vam.dto.VarFileDTO;
import vam.util.FileUtil;

@Slf4j
@Service("work")
public class Work extends WorkDeployVarFile {

	@Autowired
	public WorkUnDeployVarFile workUnDeployVarFile;

	public void allHide(String hideDirectrory) {
		File dir = new File(VAM_ROOT_PATH + hideDirectrory);
		Set<String> varFileRefSet = fetchAllVarFiles(dir, VAR_EXTENSION);
		// varFileRefSet.forEach(v -> v.realHide(VAM_FILE_PREFS));
	}

	public void allUnHide(String hideDirectrory) {
		File dir = new File(VAM_ROOT_PATH + hideDirectrory);
		Set<String> varFileRefSet = fetchAllVarFiles(dir, VAR_EXTENSION);
		// list.forEach(v -> v.unHide(VAM_FILE_PREFS));
	}

	public void loadVarFileIntoDB(String targetDirectrory) {
		File dir = new File(VAM_ALLPACKAGES_PATH);
		allVarFilesToDB(dir);
		Long count = varFileService.count();
		System.out.println("total:" + count);
	}

	public void deploy(String targetDirectory) {
		process(targetDirectory);
	}

	public void unDeploy(String targetDirectory) {
		workUnDeployVarFile.process(targetDirectory);
	}

	public void moveReference() {
		workUnDeployVarFile.creatorNameSet.forEach(name -> {
			unDeploy(name + "/");
		});
	}

	public void deploy() {
		workUnDeployVarFile.creatorNameSet.forEach(name -> {
			process(name + "/");
		});
	}

	public void switchAuthor(String author) {
		switchPackage(author);
		switchFavorite(author);

		additional(author);
	}

	static Set<String> additionalVarList;
	{
		additionalVarList = new HashSet<>();
		additionalVarList.add("JayJayWon.BrowserAssist.latest");
		additionalVarList.add("JayJayWon.UIAssist(Patron).latest");
		additionalVarList.add("JayJayWon.VARHubThumbnails.latest");
		additionalVarList.add("JayJayWon.VUML.latest");
		additionalVarList.add("JayJayWon.SexAssist.latest");
	}

	private void additional(String author) {
		additionalVarList.forEach(varFileName -> {
			VarFileDTO varFileDTO = findSuitableVarFile(new VarFileDTO("", varFileName));
			additional(varFileDTO, author);
		});
	}

	private void additional(VarFileDTO varFileDTO, String author) {
		String linkFile = VAM_ADDON_PATH + author + "\\___VarsLink___\\" + varFileDTO.getVarFileName();
		String targetFile = varFileDTO.getFullPath() + varFileDTO.getVarFileName();
		boolean b = FileUtil.createLinkFile(linkFile, targetFile);
		if (!b)
			log.warn("\n---failed additional: " + targetFile);
	}

	private void switchPackage(String author) {
		String linkfolder = VAM_FILE_ADDONPACKAGES;
		String targetfolder = VAM_ADDON_PATH + author + "\\";
		boolean b = FileUtil.createLinkFile(linkfolder, targetfolder);
		if (!b)
			log.warn("\n---failed switchPackage: " + targetfolder);
	}

	private void switchFavorite(String author) {
		String linkfolder = VAM_FILE_ADDONPACKAGESFILEPREFS;
		String targetfolder = VAM_ALLFAVORITE_PATH + author + "\\";
		boolean b = FileUtil.createLinkFile(linkfolder, targetfolder);
		if (!b)
			log.warn("\n---failed switchFavorite: " + targetfolder);
	}

}
