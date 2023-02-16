package vam;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import vam.dto.VarFileDTO;
import vam.dto.enumration.BestGirl;
import vam.dto.enumration.BestScene;
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

	public void unDeploy(BestGirl bestGirl) {
		Map<String, VarFileDTO> mLack = new HashMap<>();
		Map<String, Set<String>> msLack = new HashMap<>();
		unDeploy(bestGirl.getDescription(), mLack, msLack);
		List<String> ls = msLack.keySet().stream().collect(Collectors.toList());
		Collections.sort(ls);
		ls.forEach(v -> {
			System.out.println("--- old depenence missing: " + v + "\t\t" + msLack.get(v).toString());
		});
	}

	public void unDeploy(String targetDirectory, Map<String, VarFileDTO> mLack, Map<String, Set<String>> msLack) {
		for (String key : mLack.keySet()) {
			Set<String> ss = msLack.get(key);
			if (Objects.isNull(ss)) {
				ss = new HashSet<>();
				msLack.put(key, ss);
			}
			ss.add(targetDirectory);
		}
		mLack.putAll(workUnDeployVarFile.process(targetDirectory, targetDirectory));
	}

	public void moveReference() {
		Map<String, VarFileDTO> mLack = new HashMap<>();
		Map<String, Set<String>> msLack = new HashMap<>();
		BestGirl[] ea = BestGirl.values();
		for (int i = 0; i < ea.length; i++) {
			unDeploy(ea[i].getDescription(), mLack, msLack);
		}
		BestScene[] es = BestScene.values();
		for (int i = 0; i < es.length; i++) {
			unDeploy(es[i].getDescription(), mLack, msLack);
		}
		List<String> ls = msLack.keySet().stream().collect(Collectors.toList());
		Collections.sort(ls);
		ls.forEach(v -> {
			System.out.println("--- old depenence missing: " + v + "\t\t" + msLack.get(v).toString());
		});
	}

	public void deploy(String groupName) {
		BestGirl[] ea = BestGirl.values();
		for (int i = 0; i < ea.length; i++) {
			deployBestGirl(ea[i], groupName);
		}
		BestScene[] es = BestScene.values();
		for (int i = 0; i < es.length; i++) {
			deployBestScene(es[i], groupName);
		}
		switchAuthor(groupName);
	}

	public void deployBestGirl(BestGirl bestGirl, String groupName) {
		process(bestGirl.getDescription() + "/", groupName);
		if (StringUtils.isEmpty(groupName))
			switchAuthor(bestGirl.getDescription());
	}

	public void deployBestScene(BestScene bestScene, String groupName) {
		process(bestScene.getDescription() + "/", groupName);
		if (StringUtils.isEmpty(groupName))
			switchAuthor(bestScene.getDescription());
	}

	public void deployBestSceneGirl(BestScene bestScene, BestGirl bestGirl, String groupName) {
		process(bestGirl.getDescription(), groupName);
		process(bestScene.getDescription(), groupName);
		switchAuthor(groupName);
	}

	private void switchAuthor(String author) {
		addPackage(author);
		addFavorite(author);
		additional(author);
	}

	public void deployBestGirl(BestGirl bestGirl) {
		process(bestGirl.getDescription(), bestGirl.getDescription() + "/");
		switchAuthor(bestGirl.getDescription());
	}

	public void deployBestScene(BestScene bestScene) {
		process(bestScene.getDescription() + "/", bestScene.getDescription() + "/");
		switchAuthor(bestScene.getDescription());
	}

	public void switchAuthor(BestScene bs, BestGirl bg) {
		List<String> authorList = new ArrayList<>();
		authorList.add(bs.getDescription());
		authorList.add(bg.getDescription());
		switchAuthor(authorList);
	}

	private void switchAuthor(List<String> authorList) {
		for (String author : authorList) {
			addPackage(author);
			addFavorite(author);
		}
		// additional(author);
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
		String linkFileName = VAM_ADDON_PATH + author + "\\___VarsLink___\\" + varFileDTO.getVarFileName();
		String targetFileName = varFileDTO.getFullPath() + varFileDTO.getVarFileName();
		File targetFile = new File(targetFileName);
		FileUtil.createLinkFile2(targetFile, linkFileName, false);
//		boolean b =
//		if (!b)
//			log.warn("\n---failed additional: " + targetFile);
	}

	private void addPackage(String author) {
		String linkFileName = VAM_FILE_ADDONPACKAGES;
		String targetFileName = VAM_ADDON_PATH + author;
		File targetFile = new File(targetFileName);
		FileUtil.createLinkFile2(targetFile, linkFileName, true);
	}

	private void addFavorite(String author) {
		String linkFileName = VAM_FILE_ADDONPACKAGESFILEPREFS;
		String targetFileName = VAM_ALLFAVORITE_PATH + author + "\\";
		File targetFile = new File(targetFileName);
		FileUtil.createLinkFile2(targetFile, linkFileName, true);
	}

	public void clearUseLessDB() {
		List<VarFileDTO> list = varFileService.findAll();
		list.forEach(varFileDTO -> {
			String targetFile = varFileDTO.getFullPath() + varFileDTO.getVarFileName();
			File dir = new File(targetFile);
			if (!dir.exists()) {
				varFileService.delete(varFileDTO);
				log.warn("\n---delete db: " + varFileDTO.getVarFileName());
			}
		});
	}

}
