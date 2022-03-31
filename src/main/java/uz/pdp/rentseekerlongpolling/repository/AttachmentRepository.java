package uz.pdp.rentseekerlongpolling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import uz.pdp.rentseekerlongpolling.entity.Attachment;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

//    void deleteAllByHome_User_ChatId(String home_user_chatId);
}
