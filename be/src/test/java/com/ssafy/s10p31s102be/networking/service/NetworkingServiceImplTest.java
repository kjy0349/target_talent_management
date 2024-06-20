//package com.ssafy.s10p31s102be.networking.service;
//
//import com.ssafy.s10p31s102be.member.exception.ExecutiveNotFoundException;
//import com.ssafy.s10p31s102be.member.exception.MemberNotFoundException;
//import com.ssafy.s10p31s102be.member.infra.entity.Authority;
//import com.ssafy.s10p31s102be.member.infra.entity.Executive;
//import com.ssafy.s10p31s102be.member.infra.entity.Member;
//import com.ssafy.s10p31s102be.member.infra.repository.AuthorityJpaRepository;
//import com.ssafy.s10p31s102be.member.infra.repository.MemberJpaRepository;
//import com.ssafy.s10p31s102be.networking.exception.NetworkingNotFoundException;
//import com.ssafy.s10p31s102be.networking.infra.entity.Networking;
//import com.ssafy.s10p31s102be.networking.infra.entity.NetworkingProfile;
//import com.ssafy.s10p31s102be.networking.infra.repository.ExecutiveJpaRepository;
//import com.ssafy.s10p31s102be.networking.infra.repository.NetworkingJpaRepository;
//
//import com.ssafy.s10p31s102be.networking.dto.request.NetworkingCreateDto;
//import com.ssafy.s10p31s102be.networking.dto.request.NetworkingUpdateDto;
//import com.ssafy.s10p31s102be.profile.exception.ProfileNotFoundException;
//import com.ssafy.s10p31s102be.profile.infra.entity.Profile;
//import com.ssafy.s10p31s102be.profile.infra.repository.ProfileJpaRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyInt;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//import static org.mockito.Mockito.*;
//
//class NetworkingServiceImplTest {
//
//    @Nested
//    @SpringBootTest
//    @Transactional
//    public class NetworkingServiceImplSpringBootTest {
//
//        @Autowired
//        NetworkingServiceImpl networkingService;
//        @Autowired
//        ProfileJpaRepository profileJpaRepository;
//        @Autowired
//        NetworkingJpaRepository networkingJpaRepository;
//        @Autowired
//        MemberJpaRepository memberJpaRepository;
//        @Autowired
//        ExecutiveJpaRepository executiveJpaRepository;
//        @Autowired
//        AuthorityJpaRepository authorityJpaRepository;
//        Integer memberId;
//        Integer executiveId;
//        Integer networkId;
//        @BeforeEach
//        public void TestSetting(){
//            Authority admin = Authority.builder()
//                .authName("admin")
//                .authLevel(1)
//                .build();
//
//
//        authorityJpaRepository.saveAll(Arrays.asList(admin));
//
//
//
//        Member member = Member.builder()
//                .name("윤주석")
//                .authority(admin)
//                .build();
//            memberJpaRepository.saveAndFlush(member);
//            memberId = member.getId();
//            Executive executive = Executive.builder()
//                    .name("테스트직급4")
//                    .build();
//            executiveJpaRepository.save(executive);
//            executiveId = executive.getId();
//            Networking networking = Networking.builder()
//                    .executive(executive)
//                    .member(member)
//                    .build();
//            networkingJpaRepository.save(networking);
//            networkId = networking.getId();
//
//        }
//        @Test
//        public void Networking을_삭제하면_매핑테이블과_Networking만_지워진다() throws InterruptedException {
//            Member member = memberJpaRepository.findById(memberId).get();
//            Profile profile = new Profile( member, "hi");
//
//            profileJpaRepository.save(profile);
//            Networking networking = networkingJpaRepository.findById( networkId ).get();
////            NetworkingProfile networkingProfile = new NetworkingProfile( profile, networking );
//            networking.getNetworkingProfiles().add( profile );
//            profile.updateNetworking( networking );
////            networkingProfileJpaRepository.save( networkingProfile );
//
//            networkingService.delete(memberId, networkId);
//
//            assertThat(executiveJpaRepository.findById(executiveId).get().getId()).isEqualTo(executiveId);
//            assertThat(networkingJpaRepository.findById(networkId).isEmpty()).isEqualTo(true);
//            assertThat(profileJpaRepository.findById(profile.getId()).isEmpty()).isEqualTo(false);
//        }
//    }
//
//    @Nested
//    @ExtendWith(MockitoExtension.class)
//    public class NetworkingServiceImplMockTest{
//
//        @InjectMocks NetworkingServiceImpl networkingService;
//
//
//        @Mock NetworkingJpaRepository networkingRepository;
//        @Mock MemberJpaRepository memberRepository;
//        @Mock ProfileJpaRepository profileRepository;
//        @Mock ExecutiveJpaRepository executiveRepository;
//
//
//        //-----------------------create---------------------------
//        @Test
//        public void 생성을_하면_repository_save가_실행된다(){
//            //given
//            NetworkingCreateDto dto = new NetworkingCreateDto();
//            Member member = new Member();
//            Executive executive = new Executive();
//            given( memberRepository.findById(any())).willReturn(Optional.of(member));
//            given( executiveRepository.findById(any())).willReturn(Optional.of(executive));
//
//            //when
//            networkingService.create(member.getId(), dto );
//            //then
//            then(memberRepository).should(times(1)).findById(any());
//            then(executiveRepository).should(times(1)).findById(any());
//            then(networkingRepository).should(times(1)).save(any());
//
//        }
//
//        @Test
//        public void 생성시_Member가_없으면_MemberNotFoundException이_발생한다(){
//            //given
//            NetworkingCreateDto dto = new NetworkingCreateDto();
//            Member member = new Member();
//            Executive executive = new Executive();
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.empty());
//            lenient().when(executiveRepository.findById(any())).thenReturn(Optional.of(executive));
//            //when
//            //then
//
//            assertThatThrownBy(()-> networkingService.create( 100, dto))
//                    .isInstanceOf(MemberNotFoundException.class);
//        }
//        @Test
//        public void 생성시_Executive가_없으면_ExecutiveNotFoundException이_발생한다(){
//            //given
//            NetworkingCreateDto dto = new NetworkingCreateDto();
//            Member member = new Member();
//            Executive executive = new Executive();
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.of(member));
//            lenient().when(executiveRepository.findById(any())).thenReturn(Optional.empty());
//            //when
//            //then
//
//            assertThatThrownBy(()-> networkingService.create( member.getId(), dto))
//                    .isInstanceOf(ExecutiveNotFoundException.class);
//        }
//
//        @Test
//        public void update시_updateDto의_필드값들로_해당_객체가_변경된다() {
//            // given
//
//            Member member = mock(Member.class);
//            Integer networkId = 1;
//            NetworkingUpdateDto dto = new NetworkingUpdateDto();
//            dto.setExecutiveId(2);
//            dto.setNetworkingProfileIds(Arrays.asList(1, 2, 3));
//            Networking networking = Networking
//                    .builder()
//                    .member(member)
//                    .build();
//            Executive executive = new Executive();
//            Profile profile1 = new Profile();
//            Profile profile2 = new Profile();
//            Profile profile3 = new Profile();
//            given(member.getId()).willReturn(1);
//            given(memberRepository.findById(any())).willReturn(Optional.of(member));
//            given(networkingRepository.findNetworkingByIdWithNetworkingProfiles(networkId))
//                    .willReturn(Optional.of(networking));
//            given(executiveRepository.findById(dto.getExecutiveId())).willReturn(Optional.of(executive));
//            given(profileRepository.findById(1)).willReturn(Optional.of(profile1));
//            given(profileRepository.findById(2)).willReturn(Optional.of(profile2));
//            given(profileRepository.findById(3)).willReturn(Optional.of(profile3));
//
//            // when
//            networkingService.update( 1, networkId, dto);
//
//            // then
//            then(networkingRepository).should(times(1)).findNetworkingByIdWithNetworkingProfiles(networkId);
//            then(executiveRepository).should(times(1)).findById(dto.getExecutiveId());
//            then(profileRepository).should(times(3)).findById(anyInt());
//            assertThat(networking.getNetworkingProfiles()).hasSize(3);
//        }
//
//        @Test
//        public void update시_network가_없으면_NetworkNotFoundException이_발생한다(){
//            // given
//            Member member = mock(Member.class);
//            Integer networkId = 1;
//            NetworkingUpdateDto dto = new NetworkingUpdateDto();
//            dto.setExecutiveId(2);
//            dto.setNetworkingProfileIds(Arrays.asList(1, 2, 3));
//            Networking networking = Networking
//                    .builder()
//                    .member(member)
//                    .build();
//            Executive executive = new Executive();
//            Profile profile1 = new Profile();
//            Profile profile2 = new Profile();
//            Profile profile3 = new Profile();
//            lenient().when(member.getId()).thenReturn(1);
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.of(member));
//            lenient().when(networkingRepository.findNetworkingByIdWithNetworkingProfiles(networkId))
//                    .thenReturn(Optional.empty());
//            lenient().when(executiveRepository.findById(dto.getExecutiveId())).thenReturn(Optional.of(executive));
//            lenient().when(profileRepository.findById(1)).thenReturn(Optional.of(profile1));
//            lenient().when(profileRepository.findById(2)).thenReturn(Optional.of(profile2));
//            lenient().when(profileRepository.findById(3)).thenReturn(Optional.of(profile3));
//
//
//            // when
//            // then
//            assertThatThrownBy( () -> networkingService.update(1, networkId, dto) ).isInstanceOf(NetworkingNotFoundException.class);
//        }
//        @Test
//        public void update시_executive가_없으면_ExecutiveworkNotFoundException이_발생한다(){
//            // given
//            Member member = mock(Member.class);
//            Integer networkId = 1;
//            NetworkingUpdateDto dto = new NetworkingUpdateDto();
//            dto.setExecutiveId(2);
//            dto.setNetworkingProfileIds(Arrays.asList(1, 2, 3));
//            Networking networking = Networking
//                    .builder()
//                    .member(member)
//                    .build();
//            Executive executive = new Executive();
//            Profile profile1 = new Profile();
//            Profile profile2 = new Profile();
//            Profile profile3 = new Profile();
//            lenient().when(member.getId()).thenReturn(1);
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.of(member));
//            lenient().when(networkingRepository.findNetworkingByIdWithNetworkingProfiles(networkId))
//                    .thenReturn(Optional.of(networking));
//            lenient().when(executiveRepository.findById(dto.getExecutiveId())).thenReturn(Optional.empty());
//            lenient().when(profileRepository.findById(1)).thenReturn(Optional.of(profile1));
//            lenient().when(profileRepository.findById(2)).thenReturn(Optional.of(profile2));
//            lenient().when(profileRepository.findById(3)).thenReturn(Optional.of(profile3));
//
//
//            // when
//            // then
//            assertThatThrownBy( () -> networkingService.update(1, networkId, dto) ).isInstanceOf(ExecutiveNotFoundException.class);
//        }
//
//        @Test
//        public void update시_profile이_없으면_ProfileNotFoundException이_발생한다(){
//            // given
//            Member member = mock(Member.class);
//            Integer networkId = 1;
//            NetworkingUpdateDto dto = new NetworkingUpdateDto();
//            dto.setExecutiveId(2);
//            dto.setNetworkingProfileIds(Arrays.asList(1, 2, 3));
//            Networking networking = Networking
//                    .builder()
//                    .member(member)
//                    .build();
//            Executive executive = new Executive();
//            Profile profile1 = new Profile();
//            Profile profile2 = new Profile();
//            Profile profile3 = new Profile();
//            lenient().when(member.getId()).thenReturn(1);
//            lenient().when(memberRepository.findById(any())).thenReturn(Optional.of(member));
//            lenient().when(networkingRepository.findNetworkingByIdWithNetworkingProfiles(networkId))
//                    .thenReturn(Optional.of(networking));
//            lenient().when(executiveRepository.findById(dto.getExecutiveId())).thenReturn(Optional.of(executive));
//            lenient().when(profileRepository.findById(1)).thenReturn(Optional.empty());
//            lenient().when(profileRepository.findById(2)).thenReturn(Optional.empty());
//            lenient().when(profileRepository.findById(3)).thenReturn(Optional.empty());
//
//
//            // when
//            // then
//            assertThatThrownBy(() -> networkingService.update(1, networkId, dto)).isInstanceOf(ProfileNotFoundException.class);
//
//        }
//    }
//
//}