package vam.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import vam.dto.VarFileDTO;
import vam.entity.VarFile;
import vam.util.MapperUtils;

@Service
public class VarFileService {

	@Autowired
	VarFileRepository varFileRepository;

	@Autowired
	MapperUtils mapperUtils;

	public List<VarFileDTO> findBy(VarFileDTO varFileDTOQuery) {
		List<VarFile> varFileList = findEnitiy(varFileDTOQuery);
		List<VarFileDTO> varFileDTOList = varFileList.stream().map(e -> mapperUtils.convertVarFileDTO(e))
				.collect(Collectors.toList());
		return varFileDTOList;
	}

	public List<VarFileDTO> findByName(VarFileDTO varFileDTO) {
		List<VarFile> varFileList = varFileRepository.findByName(varFileDTO);
		List<VarFileDTO> varFileDTOList = varFileList.stream().map(e -> mapperUtils.convertVarFileDTO(e))
				.collect(Collectors.toList());
		return varFileDTOList;
	}

	private List<VarFile> findEnitiy(VarFileDTO varFileDTOQuery) {
		List<VarFile> varFileList = varFileRepository.findBy(varFileDTOQuery);
		return varFileList;
	}

	public Long count() {
		return varFileRepository.count();
	}

	public void save(VarFileDTO varFileDTO) {
		varFileRepository.save(mapperUtils.convertVarFile(varFileDTO));
	}

	public void update(VarFileDTO varFileDTO) {
		List<VarFile> list = findEnitiy(varFileDTO);
		if (!CollectionUtils.isEmpty(list)) {
			VarFile varFile = list.get(0);
			BeanUtils.copyProperties(varFileDTO, varFile);
			varFileRepository.save(varFile);
		}
	}

	public void delete(VarFileDTO varFileDTO) {
		List<VarFile> list = findEnitiy(varFileDTO);
		if (!CollectionUtils.isEmpty(list)) {
			VarFile varFile = list.get(0);
			varFileRepository.delete(varFile);
		}
	}

	public List<VarFileDTO> findAll() {
		List<VarFile> list = varFileRepository.findAll();
		return list.stream().map(mapperUtils::convertVarFileDTO).collect(Collectors.toList());
	}

	public List<VarFileDTO> findByAuthor(String sourceDirectory) {
		List<VarFile> list = varFileRepository.findByCreatorName(sourceDirectory);
		return list.stream().map(mapperUtils::convertVarFileDTO).collect(Collectors.toList());
	}

}