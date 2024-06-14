package com.demo.campingnavi.service;

import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.domain.Role;
import com.demo.campingnavi.dto.CustomOauth2UserDetails;
import com.demo.campingnavi.dto.GoogleUserDetails;
import com.demo.campingnavi.info.OAuth2UserInfo;
import com.demo.campingnavi.repository.jpa.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes : {}", oAuth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        // 다른 소셜 서비스 로그인을 위한 구분 => 구글
        if(provider.equals("google")) {
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        }

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String loginId = provider + "_" + providerId;
        String name = oAuth2UserInfo.getName();
        String img = oAuth2UserInfo.getPicture();

        Member findMember = memberRepository.findByUsername(loginId);
        Member member;

        if (findMember == null) {
            member = Member.builder()
                    .username(loginId)
                    .name(name)
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .role(Role.USER.getKey())
                    .useyn("y")
                    .img(img)
                    .nickname("google_"+name)
                    .build();
            memberRepository.save(member);
        } else {
            member = findMember;
        }

        return new CustomOauth2UserDetails(member, oAuth2User.getAttributes());
    }
}
