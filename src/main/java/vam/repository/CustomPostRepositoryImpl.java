package vam.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import vam.dto.VarFileDTO;
import vam.entity.VarFile;

public class CustomPostRepositoryImpl implements CustomPostRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<VarFile> findBy(VarFileDTO varFileDTO) {

		Query query = entityManager.createNativeQuery(
				"SELECT * from varfile p where author_name=:authorName and project_name=:projectName and version=:version",
				VarFile.class);
		query.setParameter("authorName", varFileDTO.getAuthorName());
		query.setParameter("projectName", varFileDTO.getProjectName());
		query.setParameter("version", varFileDTO.getVersion());
		List<VarFile> posts = query.getResultList();
		return posts;

	}
}