package vam.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import vam.dto.scene.Atom;
import vam.dto.scene.Position;

@Data
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SceneJson {
	private Double worldScale;
	private Double playerHeightAdjust;
	private Position monitorCameraRotation;
	private Boolean useSceneLoadPosition;

	private String playerNavCollider;

	private List<Atom> atoms;

	private String scenePath;
	private String version;

	public SceneJson() {
		super();
	}

	public SceneJson(SceneJson s) {
		this.scenePath = s.getScenePath();
	}

	Object makeEmptyFile(String extension) {
		return readFile(scenePath) + extension;
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
