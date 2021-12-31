package study.datajpa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
}
