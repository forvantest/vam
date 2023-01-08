package vam.repository;

import java.util.List;

import vam.dto.VarFileDTO;
import vam.entity.VarFile;

public interface CustomPostRepository {
	public List<VarFile> findBy(VarFileDTO varFileDTO);

	public List<VarFile> findByName(VarFileDTO varFileDTO);
}