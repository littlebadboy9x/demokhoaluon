package com.dormitory.management.service;

import com.dormitory.management.model.NguoiDung;
import com.dormitory.management.model.QuanTriVien;
import com.dormitory.management.repository.NguoiDungRepository;
import com.dormitory.management.repository.QuanTriVienRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final NguoiDungRepository nguoiDungRepository;
    private final QuanTriVienRepository quanTriVienRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        if (nguoiDung.getLoaiNguoiDung() == NguoiDung.LoaiNguoiDung.QUAN_TRI_VIEN) {
            QuanTriVien quanTriVien = quanTriVienRepository.findByTenDangNhap(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy quản trị viên"));
            
            if (quanTriVien.getQuyenHan() == QuanTriVien.QuyenHan.QUAN_TRI) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
            }
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return User.builder()
                .username(nguoiDung.getTenDangNhap())
                .password(nguoiDung.getMatKhau())
                .authorities(authorities)
                .build();
    }
} 