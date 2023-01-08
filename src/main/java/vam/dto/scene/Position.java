package vam.dto.scene;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL) 
@Data
public class Position {
	Double x;
	Double y;
	Double z;
	
}
