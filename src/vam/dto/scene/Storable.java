package vam.dto.scene;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Storable {
	String id;
	ObjectNode trigger;
	ObjectNode plugins;

	String positionState;
	String rotationState;
	String type;

	Double jointDriveSpring;
	Double jointDriveXTarget;
	Double mass;

	Position position;
	Position rotation;
	Position containerPosition;
	Position containerRotation;
	Position localPosition;
	Position localRotation;

	Boolean interactableInPlayMode;

	Boolean on;
	Boolean playbackEnabled;
	String linkTo;
	Boolean xPositionLock;
	Boolean yPositionLock;
	Boolean zPositionLock;
	Boolean xRotationLock;
	Boolean yRotationLock;
	Boolean zRotationLock;
}
