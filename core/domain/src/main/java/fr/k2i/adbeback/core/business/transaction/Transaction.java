package fr.k2i.adbeback.core.business.transaction;

import fr.k2i.adbeback.core.business.BaseObject;
import fr.k2i.adbeback.core.business.IMetaData;
import fr.k2i.adbeback.core.business.media.Media;
import fr.k2i.adbeback.core.business.user.User;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 23/03/14
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */
@Data
@Entity
@Table(name = IMetaData.TableMetadata.TRANSACTION)
public class Transaction extends BaseObject {
    @Id
    @SequenceGenerator(name = "Transaction_Gen", sequenceName = "Transaction_Sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Transaction_Gen")
    private Long id;

    @ManyToMany(targetEntity = Media.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "transaction_media", joinColumns = @JoinColumn(name = "transaction_id"), inverseJoinColumns = @JoinColumn(name = "media_id"))
    private List<Media> medias;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private User user;

}
