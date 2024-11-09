package mybatis.dao;

import com.kosa.mini.mvc.domain.member.SignupDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SignUpMapper {

    @Insert("INSERT INTO members (name, email, nickname, password, phone_number) " +
            "VALUES (#{name}, #{email}, #{nickname}, #{password}, #{phone_number})")
    boolean insert(SignupDTO dto);

    @Select("SELECT COUNT(*) FROM members WHERE email = #{email}")
    int existsByEmail(@Param("email") String email);

    @Select("SELECT COUNT(*) FROM members WHERE nickname = #{nickname}")
    int existsByNickname(@Param("nickname") String nickname);
}
