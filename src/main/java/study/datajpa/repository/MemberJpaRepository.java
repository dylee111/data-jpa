package study.datajpa.repository;

import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;

    // 등록
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    // 삭제
    public void delete(Member member) {
        em.remove(member);
    }

    // 단건 조회
    public Member find(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member); // 반환값이 null일 수 도 있음
    }

    public long count() {
        return em.createQuery("SELECT COUNT(m) FROM Member m ", Long.class)
                .getSingleResult();
    }

    // 리스트 조회
    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m ", Member.class).getResultList();
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("SELECT m FROM Member m WHERE m.username=:username AND m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> findByPage(int offset, int limit, int age) {
        return em.createQuery("SELECT m FROM Member m WHERE m.age=:age ORDER BY m.username DESC")
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public Long getTotalCount(int age) {
        return em.createQuery("SELECT COUNT(m) FROM Member m WHERE m.age=:age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }
}
