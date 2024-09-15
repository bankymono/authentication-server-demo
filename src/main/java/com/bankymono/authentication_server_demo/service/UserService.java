package com.bankymono.authentication_server_demo.service;

import com.bankymono.authentication_server_demo.model.Otp;
import com.bankymono.authentication_server_demo.model.User;
import com.bankymono.authentication_server_demo.repo.OtpRepository;
import com.bankymono.authentication_server_demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    public void addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void auth(User user) {
        Optional<User> o =
        userRepository.findUserByUsername(user.getUsername());

        if(o.isPresent()) {
            User u = o.get();
            if (passwordEncoder.matches(
                    user.getPassword(),
                    u.getPassword())) {
                renewOtp(u);
            } else {
                throw new BadCredentialsException
                        ("Bad credentials.");
            }
        } else {
            throw new BadCredentialsException
                    ("Bad credentials.");
        }
    }


    private void renewOtp(User u) {
        String code = GenerateCodeUtil
                .generateCode();

        Optional<Otp> userOtp =
                otpRepository.findOtpByUsername(u.getUsername());

        if (userOtp.isPresent()) {
            Otp otp = userOtp.get();
            otp.setCode(code);
        } else {
            Otp otp = new Otp();
            otp.setUsername(u.getUsername());
            otp.setCode(code);
            otpRepository.save(otp);
        }
    }

    public boolean check(Otp otpToValidate) {
        Optional<Otp> userOtp = otpRepository.findOtpByUsername(otpToValidate.getUsername());

        if(userOtp.isPresent()){
            Otp otp = userOtp.get();
            if(otpToValidate.getCode().equals(otp.getCode())) {
                return true;
            }
        }

        return false;
    }
}
