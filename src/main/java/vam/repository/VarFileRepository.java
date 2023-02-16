package vam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vam.entity.VarFile;

@Repository
public interface VarFileRepository extends JpaRepository<VarFile, Long>, CustomPostRepository {

	List<VarFile> findByCreatorName(String creatorName);

}