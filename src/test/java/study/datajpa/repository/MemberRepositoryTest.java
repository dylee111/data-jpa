package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private EntityManager em;

    @Test
    public void 레포지토리_회원등록() {
        Member member = new Member("MemberA", 20);

        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(member.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); // 같은 트랙젝션 내에 영속성 컨텍스트에서는 동일한 객체로 인식
    }

    // SPRING DATA JPA 사용
    @Test
    public void CRUD_테스트2() {
        Member memberA = new Member("MemberA");
        Member memberB = new Member("MemberB");

        memberRepository.save(memberA);
        memberRepository.save(memberB);

//        memberA.setUsername("MEMBER@@@@@@@@@@@@");

        Member findMemberA = memberRepository.findById(memberA.getId()).get();
        Member findMemberB = memberRepository.findById(memberB.getId()).get();

        // 단건 조회 검증
        assertThat(findMemberA).isEqualTo(memberA);
        assertThat(findMemberB).isEqualTo(memberB);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(memberA);
        memberRepository.delete(memberB);
        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    public void 스프링_이름_나이보다_큰() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(findMember.get(0).getUsername()).isEqualTo("AAA");
        assertThat(findMember.get(0).getAge()).isEqualTo(20);
        assertThat(findMember.size()).isEqualTo(1);
    }

    @Test
    public void 멤버_팀_조회() {
        Team team = new Team("TeamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA", 10, team);
        Member member2 = new Member("AAA", 20, team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<MemberDto> findMemberTeam = memberRepository.findMemberDto();

        for (MemberDto memberDto : findMemberTeam) {
            System.out.println("RESULT = " + memberDto);
        }
    }

    @Test
    public void 컬렉션_파라미터_테스트() {
        Member memberA = new Member("MemberA", 10);
        Member memberB = new Member("MemberB", 10);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> members = memberRepository.findByNames(Arrays.asList(memberA.getUsername(), memberB.getUsername()));
        for (Member member : members) {
            System.out.println("members = " + members);
        }
    }

    @Test
    public void 페이징_테스트() {
        //GIVEN
        memberRepository.save(new Member("MemberA",20));
        memberRepository.save(new Member("MemberB", 20));
        memberRepository.save(new Member("MemberC", 20));
        memberRepository.save(new Member("MemberD", 20));
        memberRepository.save(new Member("MemberE", 20));
        memberRepository.save(new Member("MemberF", 20));
        memberRepository.save(new Member("MemberG", 20));

        // page 시작은 0부터
        Pageable pageable = PageRequest.of(0, 3,
                Sort.by(Sort.Direction.DESC,"username"));
        int age = 20;

        //WHEN
        Page<Member> pageResult = memberRepository.findByAge(age, pageable);
        Page<MemberDto> toDtoPage = pageResult.map(member -> new MemberDto(member.getId(),
                member.getUsername(), member.getAge(), null));

        //THEN
//        List<Member> content = pageResult.getContent();     // 조회된 데이터
//        long totalElements = pageResult.getTotalElements();
//        for (Member member : content) {
//            System.out.println("member = " + member);
//        }
//        System.out.println("totalElements = " + totalElements);
//
//        assertThat(content.size()).isEqualTo(3);                // 조회된 데이터 수
//        assertThat(pageResult.getTotalElements()).isEqualTo(7); // 전체 데이터 수
//        assertThat(pageResult.getTotalPages()).isEqualTo(3);    // 전체 페이지 번호
//        assertThat(pageResult.getNumber()).isEqualTo(0);        // 조회된 페이지 번호
//        assertThat(pageResult.isFirst()).isTrue();              // 첫번째 항목인지?
//        assertThat(pageResult.hasNext()).isTrue();              // 다음 페이지 여부

        List<MemberDto> content = toDtoPage.getContent();     // 조회된 데이터
        long totalElements = toDtoPage.getTotalElements();
        for (MemberDto member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);                // 조회된 데이터 수
        assertThat(toDtoPage.getTotalElements()).isEqualTo(7); // 전체 데이터 수
        assertThat(toDtoPage.getTotalPages()).isEqualTo(3);    // 전체 페이지 번호
        assertThat(toDtoPage.getNumber()).isEqualTo(0);        // 조회된 페이지 번호
        assertThat(toDtoPage.isFirst()).isTrue();              // 첫번째 항목인지?
        assertThat(toDtoPage.hasNext()).isTrue();              // 다음 페이지 여부

    }

    @Test
    public void 벌크_연산_테스트() {
        memberRepository.save(new Member("MemberA", 10));
        memberRepository.save(new Member("MemberB", 28));
        memberRepository.save(new Member("MemberC", 14));
        memberRepository.save(new Member("MemberD", 20));
        memberRepository.save(new Member("MemberE", 21));
        memberRepository.save(new Member("MemberF", 60));

        int bulk = memberRepository.bulkAgePlus(20);
        assertThat(bulk).isEqualTo(4);

        // entityManager.flush();
        // entityManager.clear();

        MemberDto dtoMemberB = memberRepository.findDtoByUsername("MemberB");
        Member entityMemberB = memberRepository.findEntityByUsername("MemberB");
        System.out.println("DtoMemberB = " + dtoMemberB);
        System.out.println("EntityMemberB = " + entityMemberB);
    }

    @Test
    public void entityGraphTest() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        memberRepository.save(new Member("MemberA", 10, teamA));
        memberRepository.save(new Member("MemberB", 10, teamB));

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("-> member.teamClass = " + member.getTeam().getClass());
            System.out.println("-> member.team = " + member.getTeam().getName());
        }
    }
}
