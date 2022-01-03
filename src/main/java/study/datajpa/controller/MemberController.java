package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        MemberDto memberDto = new MemberDto(member);
        return "도메인 클래스 컨버터 사용 전 : " + memberDto.getUsername();
    }

    @GetMapping("/Members2/{id}")
    public String findMemberConverter(@PathVariable("id") Member member) {
        return "도메인 클래스 컨버터 사용 후 : " + new MemberDto(member).getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> memberList(@PageableDefault(size = 5)Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
//                .map(member -> new MemberDto(member))
    }

//    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("member" + i, i));
        }
    }
}
