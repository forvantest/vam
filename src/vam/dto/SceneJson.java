package vam.dto;

import java.util.List;

import lombok.Data;
import vam.dto.scene.Atom;
import vam.dto.scene.Position;

@Data
public class SceneJson {
	Double worldScale;
	Double playerHeightAdjust;
	Position monitorCameraRotation;
	Boolean useSceneLoadPosition;

	List<Atom> atoms;

}
