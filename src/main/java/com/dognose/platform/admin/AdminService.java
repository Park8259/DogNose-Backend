package com.dognose.platform.admin;

import com.dognose.platform.adoption.AdoptionPost;
import com.dognose.platform.adoption.AdoptionPostRepository;
import com.dognose.platform.adoption.AdoptionPostResponse;
import com.dognose.platform.adoption.AdoptionPostStatus;
import com.dognose.platform.user.User;
import com.dognose.platform.user.UserRepository;
import com.dognose.platform.user.UserRole;
import com.dognose.platform.verification.VerificationLog;
import com.dognose.platform.verification.VerificationLogRepository;
import com.dognose.platform.verification.VerificationResponse;
import com.dognose.platform.verification.VerificationResult;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final AdoptionPostRepository adoptionPostRepository;
    private final VerificationLogRepository verificationLogRepository;

    public AdminService(
            UserRepository userRepository,
            AdoptionPostRepository adoptionPostRepository,
            VerificationLogRepository verificationLogRepository
    ) {
        this.userRepository = userRepository;
        this.adoptionPostRepository = adoptionPostRepository;
        this.verificationLogRepository = verificationLogRepository;
    }

    @Transactional(readOnly = true)
    public List<AdoptionPostResponse> getAdoptionPosts(String adminEmail, AdoptionPostStatus status) {
        validateAdmin(adminEmail);
        List<AdoptionPost> posts = status == null
                ? adoptionPostRepository.findAllByOrderByCreatedAtDesc()
                : adoptionPostRepository.findAllByStatusOrderByCreatedAtDesc(status);
        return posts.stream()
                .map(AdoptionPostResponse::from)
                .toList();
    }

    @Transactional
    public AdoptionPostResponse updateAdoptionPostStatus(
            String adminEmail,
            Long postId,
            AdminPostStatusUpdateRequest request
    ) {
        validateAdmin(adminEmail);
        AdoptionPost post = adoptionPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("분양글을 찾을 수 없습니다."));
        post.changeStatus(request.status());
        return AdoptionPostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public List<VerificationResponse> getVerificationLogs(String adminEmail, VerificationResult result) {
        validateAdmin(adminEmail);
        List<VerificationLog> logs = result == null
                ? verificationLogRepository.findAllByOrderByCreatedAtDesc()
                : verificationLogRepository.findAllByResultOrderByCreatedAtDesc(result);
        return logs.stream()
                .map(VerificationResponse::from)
                .toList();
    }

    private void validateAdmin(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if (user.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("관리자만 처리할 수 있습니다.");
        }
    }
}
