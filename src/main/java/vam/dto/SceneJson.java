package vam.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import vam.dto.scene.Atom;
import vam.dto.scene.Position;

@Data
public class SceneJson {
	Double worldScale;
	Double playerHeightAdjust;
	Position monitorCameraRotation;
	Boolean useSceneLoadPosition;

	String playerNavCollider;

	List<Atom> atoms;

	private String scenePath;

	public Object makeHideEmptyFile() {
		return readFile(scenePath) + ".hide";
	}

	protected Object makeHidePath() {
		return readPath(scenePath);
	}

	private String readPath(String fullPath) {
		int index = StringUtils.lastIndexOf(fullPath, "/");
		if (index >= 0) {
			String path = StringUtils.substring(fullPath, 0, index + 1);
			return path;
		}
		return null;
	}

	private String readFile(String fullPath) {
		int index = StringUtils.lastIndexOf(fullPath, "/");
		if (index >= 0) {
			String path = StringUtils.substring(fullPath, index + 1);
			return path;
		}
		return null;
	}
}
