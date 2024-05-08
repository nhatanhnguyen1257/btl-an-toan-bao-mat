package vn.dev.na.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.dev.na.auth.entitys.FileStorage;

public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
}
