package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.Entity;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    MemberDto findDtoByUsername(String username);

    Member findEntityByUsername(String username);

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("SELECT m FROM Member m WHERE m.username= ?1 AND m.age=?2")
    List<MemberDto> findMemberPosition(String username, int age);

    @Query("SELECT new study.datajpa.dto.MemberDto(m.id, m.username, m.age, t.name) " +
            "FROM Member m JOIN m.team t ")
    List<MemberDto> findMemberDto();

    @Query("SELECT m FROM Member m " +
            "WHERE m.username " +
            "IN :usernames")
    List<Member> findByNames(@Param("usernames") Collection<String> usernames);

    @Query(value = "SELECT m FROM Member m LEFT JOIN m.team t ",
            countQuery = "SELECT COUNT(m) FROM Member m ")
    Page<Member> findByAge(int age, Pageable pageable);

    // 벌크 연산
    @Modifying(clearAutomatically = true) // clearAutomatically 속성으로 영속성 컨텍스트 초기화
    @Query("Update Member m SET m.age = m.age + 1 WHERE m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    //공통 메서드 오버라이드
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    //JPQL + 엔티티 그래프
    @EntityGraph(attributePaths = {"team"})
    @Query("SELECT m FROM Member m ")
    List<Member> findMemberEntityGraph();

    //메서드 이름으로 쿼리에서 특히 편리
    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(String username);

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    /*
    * Projection
    */
    <T> List<T> findProjectionByUsername(String username, Class<T> type);

    @Query(value = "SELECT m.member_id as id, m.username, t.name as TeamName " +
            "FROM member m LEFT JOIN team t ",
            countQuery = "SELECT COUNT(*) FROM member ",
            nativeQuery = true)
    Page<MemberProjection> findByNativeQuery(Pageable pageable);
}
