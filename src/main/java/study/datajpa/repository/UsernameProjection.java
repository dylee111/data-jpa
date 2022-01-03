package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameProjection {

//    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
