package vam.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import vam.dto.scene.Atom;

@JsonInclude(Include.NON_NULL) 
@Data
public class PoseJson {

	List<Atom> atoms;

}
