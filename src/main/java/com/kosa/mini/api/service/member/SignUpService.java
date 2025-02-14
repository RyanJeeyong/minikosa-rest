package com.kosa.mini.api.service.member;

import com.kosa.mini.api.dto.member.SignupDTO;
import com.kosa.mini.api.exception.DuplicateEmailException;
import com.kosa.mini.api.exception.DuplicateNicknameException;
import com.kosa.mini.api.exception.SignupException;

public interface SignUpService {
    boolean signUp(SignupDTO dto) throws DuplicateEmailException, DuplicateNicknameException, SignupException;

    // 중복 검사
    boolean isEmailExists(String email);
    boolean isNicknameExists(String nickname);
}
