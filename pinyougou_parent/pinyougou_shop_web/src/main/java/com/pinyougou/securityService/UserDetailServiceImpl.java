package com.pinyougou.securityService;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailServiceImpl implements UserDetailsService {
    private SellerService sellerService;

    public SellerService getSellerService() {
        return sellerService;
    }

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UserDeatilServiceImpl执行了。。。。");
        TbSeller seller = sellerService.findOne(username);
        List< GrantedAuthority> auth=new ArrayList<>();
        auth.add(new SimpleGrantedAuthority("ROLE_SELLER"));
        if (seller == null || !"1".equals(seller.getStatus())) {
            return null;
        }

        return new User(username,seller.getPassword(),auth);
    }
}
