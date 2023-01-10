package vam.dto.scene;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;
import vam.dto.enumration.AtomType;

@JsonInclude(Include.NON_NULL)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Atom {
	String id;
	Boolean on;
	AtomType type;
	Position position;
	Position rotation;
	Position containerPosition;
	Position containerRotation;
	ObjectNode trigger;
	ObjectNode plugins;

	String parentAtom;

	List<Storable> storables;

}
