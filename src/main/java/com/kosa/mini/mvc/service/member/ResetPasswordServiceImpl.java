package com.kosa.mini.mvc.service.member;

import com.kosa.mini.mvc.domain.member.ResetPasswordDTO;
import mybatis.dao.ResetPasswordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final ResetPasswordMapper resetPasswordMapper;

    @Autowired
    public ResetPasswordServiceImpl(ResetPasswordMapper resetPasswordMapper) {
        this.resetPasswordMapper = resetPasswordMapper;
    }

    @Override
    public boolean resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception {
        // 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmPassword())) {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 업데이트
        int result = resetPasswordMapper.updatePassword(resetPasswordDTO.getNewPassword(), resetPasswordDTO.getEmail());
        return result > 0;
    }

    @Override
    public ResetPasswordDTO searchMember(String name, String phoneNumber, String email) {
        return resetPasswordMapper.searchMember(name, phoneNumber, email);
    }
}
