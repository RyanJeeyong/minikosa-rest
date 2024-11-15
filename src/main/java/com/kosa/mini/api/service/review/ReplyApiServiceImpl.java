package com.kosa.mini.api.service.review;

import com.kosa.mini.api.dto.review.ReplySaveDTO;
import com.kosa.mini.api.entity.Member;
import com.kosa.mini.api.entity.Review;
import com.kosa.mini.api.entity.ReviewReply;
import com.kosa.mini.api.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReplyApiServiceImpl implements ReplyApiService {

    private final ReplyRepository replyRepository;

    @Override
    public boolean deleteReply(Integer replyId) {
        boolean replyResult = replyRepository.existsById(replyId);
        if(replyResult) {
            replyRepository.deleteById(replyId);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    @Override
    public ReplySaveDTO createReview(ReplySaveDTO replySaveDTO, Integer ownerId) {
        ReviewReply reviewReply = new ReviewReply();
        Member member = new Member();
        Review review = new Review();
        review.setReviewId(replySaveDTO.getReviewId());
        member.setMemberId(ownerId);
        reviewReply = replySaveDTO.toEntity(review, member);
        replyRepository.save(reviewReply);
        replySaveDTO.fromEntity(reviewReply);
        return replySaveDTO;
    }
}
