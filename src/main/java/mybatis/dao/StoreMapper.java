package mybatis.dao;

import com.kosa.mini.mvc.domain.member.Member;
import com.kosa.mini.mvc.domain.store.Menu;
import com.kosa.mini.mvc.domain.store.Store;
import com.kosa.mini.mvc.domain.store.StoreDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StoreMapper {

    @Insert("INSERT INTO stores (store_name, postcode, road_address, detail_address, extra_address, category_id, store_photo, store_description, owner_id, opening_time, closing_time, website_info, contact_number, created_at, updated_at, is_modified) " +
            "VALUES (#{storeName}, #{postcode}, #{roadAddress}, #{detailAddress}, #{extraAddress}, #{categoryId}, #{storePhoto}, #{storeDescription}, #{ownerId}, #{openingTime}, #{closingTime}, #{websiteInfo}, #{contactNumber}, #{createdAt}, #{updatedAt}, #{isModified})")
    @Options(useGeneratedKeys = true, keyProperty = "storeId")
    void insertStore(Store store);

    // 가게 정보 업데이트
    @Update("UPDATE stores SET " +
            "store_name = #{storeName}, " +
            "postcode = #{postcode}, " +
            "road_address = #{roadAddress}, " +
            "detail_address = #{detailAddress}, " +
            "extra_address = #{extraAddress}, " +
            "category_id = #{categoryId}, " +
            "store_photo = #{storePhoto}, " +
            "store_description = #{storeDescription}, " +
            "website_info = #{websiteInfo}, " +
            "contact_number = #{contactNumber}, " +
            "opening_time = #{openingTime}, " +
            "closing_time = #{closingTime}, " +
            "updated_at = #{updatedAt}, " +
            "is_modified = #{isModified} " +
            "WHERE store_id = #{storeId}")
    void updateStore(Store store);

    @Insert({
            "<script>",
            "INSERT INTO menus (store_id, menu_name, price, menu_photo) VALUES ",
            "<foreach collection='menus' item='menu' separator=','>",
            "(#{storeId}, #{menu.menuName}, #{menu.price}, #{menu.menuPhoto})",
            "</foreach>",
            "</script>"
    })
    void insertMenus(@Param("storeId") Long storeId, @Param("menus") List<Menu> menus);

    // 기존 메뉴 삭제
    @Delete("DELETE FROM menus WHERE store_id = #{storeId}")
    void deleteMenusByStoreId(@Param("storeId") Long storeId);

    // 사장님 할당 메서드
    @Update("UPDATE stores SET owner_id = #{ownerId} WHERE store_id = #{storeId}")
    void assignOwnerToStore(@Param("storeId") Long storeId, @Param("ownerId") Long ownerId);

    @Update("UPDATE members SET role_id = 3 WHERE member_id = #{ownerId}")
    void updateMemberRole(@Param("ownerId") Long ownerId);

    // 사용자 검색 메서드
    @Select("SELECT member_id, name, nickname, email, phone_number FROM members WHERE email LIKE CONCAT('%', #{email}, '%')")
    List<Member> searchMembersByEmail(@Param("email") String email);

    // 가게 검색 메서드
    @Select("SELECT store_id, store_name, road_address FROM stores WHERE store_name LIKE CONCAT('%', #{storeName}, '%')")
    List<StoreDTO> searchStoresByName(@Param("storeName") String storeName);

    @Select("SELECT * FROM stores WHERE owner_id = #{ownerId}")
    Store findByOwnerId(@Param("ownerId") Long ownerId);

    @Select("SELECT store_name, postcode, road_address, detail_address, extra_address, category_id, store_photo, store_description, " +
            "owner_id, opening_time, closing_time, website_info, contact_number, created_at, updated_at, is_modified " +
            "from stores " +
            "where store_id = #{storeId}")
    StoreDTO findStore(long storeId);

}

