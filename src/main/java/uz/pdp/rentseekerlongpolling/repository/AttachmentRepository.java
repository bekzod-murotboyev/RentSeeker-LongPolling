package uz.pdp.rentseekerlongpolling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.rentseekerlongpolling.entity.Attachment;

import java.util.Optional;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    Optional<Attachment> findByFileUniqueId(String fileUniqueId);

    @Modifying(flushAutomatically = true)
    @Transactional
    @Query(nativeQuery = true,value = "update attachment set file_id=:file_id where file_path=:file_path")
    void updateFileId(@Param("file_path") String filePath,
                      @Param("file_id") String fileId);

}
