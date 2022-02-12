package leesh.devcom.backend.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(unique = true, length = 20, nullable = false)
    private String username;

    @Column(unique = true, length = 255, nullable = false)
    private String password;

    private Member(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public static Member createMember(@NotNull String email, @NotNull String username, @NotNull String password) {
        return new Member(email, username, password);
    }
}
