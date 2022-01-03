package study.datajpa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.PageRequest;
import study.datajpa.entity.Member;

@Getter
@Setter
@ToString
public class MemberDto {

    private Long id;
    private String username;
    private int age;
    private String teamName;

    public MemberDto(Long id, String username,int age, String teamName) {
        this.id = id;
        this.age = age;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(Long id, String username, int age) {
        this.id = id;
        this.username = username;
        this.age = age;
    }

    // 엔티티를 매개변수로 직접 받을 경우
    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.age = member.getAge();
    }
}
