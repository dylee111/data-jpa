package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    public void 회원등록() {
        Member member = new Member("MemberA", 20);

        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); // 같은 트랙젝션 내에 영속성 컨텍스트에서는 동일한 객체로 인식
    }

    @Test
    public void 회원_팀_등록() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        em.persist(teamA);
        em.persist(teamB);

        Member memberA = new Member("memberA", 10, teamA);
        Member memberB = new Member("memberB", 20, teamA);
        Member memberC = new Member("memberC", 30, teamB);
        Member memberD = new Member("memberD", 40, teamB);

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        em.persist(memberD);

        // 초기화
        em.flush();
        em.clear();

        // 확인
        List<Member> members = em.createQuery("SELECT m FROM Member m ", Member.class).getResultList();

        for (Member member : members) {
            System.out.println("members = " + member);
            System.out.println("-> member.team = " + member.getTeam());
        }
    }

    // 순수 JPA 사용
    @Test
    public void CRUD_테스트() {
        Member memberA = new Member("MemberA");
        Member memberB = new Member("MemberB");

        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        Member findMemberA = memberJpaRepository.findById(memberA.getId()).get();
        Member findMemberB = memberJpaRepository.findById(memberB.getId()).get();

        // 단건 조회 검증
        assertThat(findMemberA).isEqualTo(memberA);
        assertThat(findMemberB).isEqualTo(memberB);

        // 리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(memberA);
        memberJpaRepository.delete(memberB);
        long deleteCount = memberJpaRepository.count();
         assertThat(deleteCount).isEqualTo(0);
    }


    @Test
    public void JPA_이름_나이보다_큰() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> findMember = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(findMember.get(0).getUsername()).isEqualTo("AAA");
        assertThat(findMember.get(0).getAge()).isEqualTo(20);
        assertThat(findMember.size()).isEqualTo(1);
    }

    @Test
    public void 페이징테스트() {
        //GIVEN
        memberJpaRepository.save(new Member("AAA", 10));
        memberJpaRepository.save(new Member("BBB", 10));
        memberJpaRepository.save(new Member("CCC", 10));
        memberJpaRepository.save(new Member("DDD", 10));
        memberJpaRepository.save(new Member("EEE", 10));
        memberJpaRepository.save(new Member("FFF", 10));
        memberJpaRepository.save(new Member("GGG", 10));
        memberJpaRepository.save(new Member("HHH", 10));
        memberJpaRepository.save(new Member("III", 10));
        memberJpaRepository.save(new Member("JJJ", 10));

        int age = 10;
        int limit = 3;
        int offset = 0;

        //WHEN
        List<Member> pageResult = memberJpaRepository.findByPage(offset, limit, age);
        Long totalCount = memberJpaRepository.getTotalCount(age);

        //THEN
        assertThat(pageResult.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(10);

    }

}