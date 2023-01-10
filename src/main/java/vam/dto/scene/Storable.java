package vam.dto.scene;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Storable extends StorableGeometry {
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
