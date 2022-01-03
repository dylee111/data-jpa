package study.datajpa.repository;

public class ProjectionDto {

    private final String username;

    public ProjectionDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}