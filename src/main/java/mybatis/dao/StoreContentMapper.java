package mybatis.dao;

import com.kosa.mini.mvc.domain.store.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface StoreContentMapper {

    // 가게 정보
    @Select("select   " +
            "    s.store_id,   " +
            "    s.owner_id,   " +
            "    s.store_name,   " +
            "    s.postcode,   " +
            "    s.road_address,   " +
            "    s.detail_address,   " +
            "    s.extra_address,   " +
            "    s.store_description,   " +
            "    s.opening_time,   " +
            "    s.closing_time,   " +
            "    s.website_info,  " +
            "    s.store_photo, " +
            "    s.contact_number, " +
            "    round(avg(r.rating),1) as 'ratingAvg',  " +
            "    count(r.review_id) as 'countReview' " +
            "FROM   " +
            "    stores s   " +
            "Left JOIN   " +
            "    reviews r  " +
            "ON s.store_id = r.store_id   " +
            "WHERE   " +
            "    s.store_id = #{num}")
    public StoreContentDTO storeInfo(int num);

    // 가게 메뉴
    @Select("SELECT " +
            "   menu_name, " +
            "   price, " +
            "   menu_photo " +
            "FROM " +
            "   menus " +
            "WHERE " +
            "   store_id = #{num}")
    public List<Menu> getStoreMenuAll(int num);


    // 가게 리뷰 + 답글
    @Select("select " +
            "r.store_id, r.member_id, r.review_text, r.rating, r.created_at, r.review_id, m.nickname, " +
            "rr.reply_id, rr.review_id, rr.owner_id, rr.reply_text " +
            "from  " +
            "reviews r  " +
            "left join review_replies rr " +
            "on r.review_id = rr.review_id " +
            "left join stores t " +
            "on t.store_id = rr.owner_id " +
            "left join members m " +
            "on r.member_id = m.member_id " +
            "where r.store_id = #{num} " +
            "order by r.created_at desc " +
            "limit 4")
    public List<ReviewReplyDTO> getStoreReplyAll(int num);

    @Insert("insert into reviews " +
            "   (store_id, member_id, review_text, rating) " +
            "values " +
            "   (#{storeId},  #{memberId}, #{reviewText}, #{rating})")
    public boolean insertUserReview(int storeId, int memberId, String reviewText, int rating);


    @Update("update " +
            "   reviews " +
            "set " +
            "   review_text = #{reviewText}, rating = #{rating} " +
            "where " +
            "   review_id = #{reviewId} and member_id = #{memberId}")
    public boolean updateUserReview(StoreReviewDTO reviewDTO);

    @Delete("delete from reviews " +
            "where member_id = #{num} and review_id = #{reviewId}")
    public boolean deleteUserReview(int num, int reviewId);

    @Select("Select select store_id " +
            "from reviews " +
            "where member_id = #{num}")
    public int getUserReview(int num);

    @Delete("delete from stores " +
            "where store_id = #{num}")
    public boolean deleteStore(int num);

    @Insert("insert into review_replies ( review_id, owner_id, reply_text) " +
            "values ( #{reviewId}, #{ownerId}, #{replyText})")
    boolean insertReply(ReplyDTO replyDTO);

    @Update("update review_replies " +
            "set " +
            "   reply_text = #{text} " +
            "where " +
            "   reply_id = #{replyId}")
    boolean updateReply(String text, int replyId);

    @Delete("delete from review_replies " +
            "where reply_id = ${replyId}")
    boolean deleteReply(int replyId);
}
