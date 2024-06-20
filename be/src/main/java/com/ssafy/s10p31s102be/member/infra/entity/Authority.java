
package com.ssafy.s10p31s102be.member.infra.entity;
import com.ssafy.s10p31s102be.admin.dto.request.AuthorityAdminUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Builder
    public Authority(String authName, Integer authLevel, String authDescription) {
        this.authName = authName;
        this.authLevel = authLevel;
        this.authDescription=authDescription;
    }
    @Id
    @GeneratedValue
    private Integer id;
    private String authName;
    private Integer authLevel;
    private String authDescription;
    //    @OneToMany(mappedBy = "authority", cascade = CascadeType.ALL)
//    private List<Member> members = new ArrayList<>();
    private Boolean isDeleted = false;
    public void update(AuthorityAdminUpdateDto dto) {
        this.authName = dto.getAuthName();
        this.authDescription = dto.getAuthDescription();
        this.authLevel = dto.getAuthLevel();
    }
    public void delete() {
        this.isDeleted = true;
    }
}
