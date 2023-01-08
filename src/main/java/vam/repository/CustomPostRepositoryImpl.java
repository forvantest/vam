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
				"SELECT * from varfile p where creator_name=:creatorName and package_name=:packageName and version=:version",
				VarFile.class);
		query.setParameter("creatorName", varFileDTO.getCreatorName());
		query.setParameter("packageName", varFileDTO.getPackageName());
		query.setParameter("version", varFileDTO.getVersion());
		List<VarFile> posts = query.getResultList();
		return posts;
	}

	@Override
	public List<VarFile> findByName(VarFileDTO varFileDTO) {
		Query query = entityManager.createNativeQuery(
				"SELECT * from varfile p where creator_name=:creatorName and package_name=:packageName order by version desc",
				VarFile.class);
		query.setParameter("creatorName", varFileDTO.getCreatorName());
		query.setParameter("packageName", varFileDTO.getPackageName());
		List<VarFile> posts = query.getResultList();
		return posts;
	}
}