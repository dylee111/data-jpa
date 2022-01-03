package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity implements Persistable<String> {

    @Id
    private String itemId;

    public Item(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getId() {
        return itemId;
    }

    @Override
    public boolean isNew() {
        return this.getCreatedDate() == null;
    }
}
