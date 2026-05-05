package com.dognose.platform.adoption;

import com.dognose.platform.user.User;
import com.dognose.platform.user.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdoptionApplicationService {

    private final AdoptionApplicationRepository applicationRepository;
    private final AdoptionPostRepository postRepository;
    private final UserRepository userRepository;

    public AdoptionApplicationService(
            AdoptionApplicationRepository applicationRepository,
            AdoptionPostRepository postRepository,
            UserRepository userRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AdoptionApplicationResponse apply(
            String applicantEmail,
            Long postId,
            AdoptionApplicationCreateRequest request
    ) {
        User applicant = findUser(applicantEmail);
        AdoptionPost post = findPost(postId);

        if (post.getStatus() != AdoptionPostStatus.OPEN) {
            throw new IllegalArgumentException("신청할 수 없는 분양글입니다.");
        }

        if (post.getSeller().getEmail().equals(applicantEmail)) {
            throw new IllegalArgumentException("본인이 작성한 분양글에는 신청할 수 없습니다.");
        }

        if (applicationRepository.existsByPostAndApplicant(post, applicant)) {
            throw new IllegalArgumentException("이미 신청한 분양글입니다.");
        }

        AdoptionApplication application = new AdoptionApplication(post, applicant, request.message());
        return AdoptionApplicationResponse.from(applicationRepository.save(application));
    }

    @Transactional(readOnly = true)
    public List<AdoptionApplicationResponse> getPostApplications(String sellerEmail, Long postId) {
        AdoptionPost post = findPost(postId);
        validateSeller(sellerEmail, post);

        return applicationRepository.findAllByPostOrderByCreatedAtDesc(post)
                .stream()
                .map(AdoptionApplicationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AdoptionApplicationResponse> getMyApplications(String applicantEmail) {
        User applicant = findUser(applicantEmail);
        return applicationRepository.findAllByApplicantOrderByCreatedAtDesc(applicant)
                .stream()
                .map(AdoptionApplicationResponse::from)
                .toList();
    }

    @Transactional
    public AdoptionApplicationResponse accept(String sellerEmail, Long applicationId) {
        AdoptionApplication application = findApplication(applicationId);
        validateSeller(sellerEmail, application.getPost());
        application.accept();
        return AdoptionApplicationResponse.from(application);
    }

    @Transactional
    public AdoptionApplicationResponse reject(String sellerEmail, Long applicationId) {
        AdoptionApplication application = findApplication(applicationId);
        validateSeller(sellerEmail, application.getPost());
        application.reject();
        return AdoptionApplicationResponse.from(application);
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private AdoptionPost findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("분양글을 찾을 수 없습니다."));
    }

    private AdoptionApplication findApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("입양 신청을 찾을 수 없습니다."));
    }

    private void validateSeller(String sellerEmail, AdoptionPost post) {
        if (!post.getSeller().getEmail().equals(sellerEmail)) {
            throw new IllegalArgumentException("분양글 작성자만 처리할 수 있습니다.");
        }
    }
}
