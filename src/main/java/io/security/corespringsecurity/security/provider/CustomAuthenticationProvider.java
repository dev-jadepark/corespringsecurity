package io.security.corespringsecurity.security.provider;

import io.security.corespringsecurity.security.common.FormWebAuthenticationDetails;
import io.security.corespringsecurity.security.service.AccountContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //검증을 위한 구현
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String)authentication.getCredentials();

        AccountContext accountContext = (AccountContext)userDetailsService.loadUserByUsername(username);

        if(!passwordEncoder.matches(password, accountContext.getAccount().getPassword())){
            throw new BadCredentialsException("BadCredentialException");
        }

        FormWebAuthenticationDetails formWebAuthenticationDetails = (FormWebAuthenticationDetails)authentication.getDetails();
        String secretKey = formWebAuthenticationDetails.getSecretKey();
        if(secretKey == null || !"secret".equals(secretKey)){
            throw new InsufficientAuthenticationException("InsufficientAuthenticationException");
        }


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());

        return authenticationToken;
    }

    //토큰의 타입이 인증할때 인증이 되도록 처리
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
