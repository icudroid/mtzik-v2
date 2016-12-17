package fr.k2i.adbeback.core.business.otp;

import fr.k2i.adbeback.core.business.BaseObject;
import fr.k2i.adbeback.core.business.IMetaData;
import fr.k2i.adbeback.core.business.converter.LocalDateTimePersistenceConverter;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * User: dimitri
 * Date: 18/02/13
 * Time: 11:29
 * Goal:
 */
@Data
@Entity
@Table(name= IMetaData.TableMetadata.OTP)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = IMetaData.ColumnMetadata.OTPSecurity.Discrimator.DISCRIMINATOR, discriminatorType = DiscriminatorType.STRING)
public abstract class OTPSecurity extends BaseObject implements Serializable {
    @Id
    @SequenceGenerator(name = "OTP_Gen", sequenceName = "OTP_Sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OTP_Gen")
    protected Long id;

    @Column(name=IMetaData.ColumnMetadata.OTPSecurity.KEY,length = 8)
    protected String key;


    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = IMetaData.ColumnMetadata.OTPSecurity.CREATION_DATE)
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    protected LocalDateTime creationDate;

    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = IMetaData.ColumnMetadata.OTPSecurity.EXPIRATION_DATE)
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    protected LocalDateTime expirationDate;


    public OTPSecurity(){
        creationDate = LocalDateTime.now();
    }

    public void expirationInHours(int expirationInHours) {
        expirationDate = LocalDateTime.now().plusHours(expirationInHours);
    }

    public void computeNewExpirationInHours(int expirationInHours) {
        creationDate = LocalDateTime.now();
        expirationInHours(expirationInHours);
    }

}
