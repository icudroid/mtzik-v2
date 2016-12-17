package fr.k2i.adbeback.core.business.contact;

import fr.k2i.adbeback.core.business.BaseObject;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 14/01/14
 * Time: 11:15
 * To change this template use File | Settings | File Templates.
 */
@Data
@Entity
@Table(name = "contact_form")
public class ContactForm extends BaseObject {
    @Id
    @SequenceGenerator(name = "Contactform_Gen", sequenceName = "Contactform_Sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Contactform_Gen")
    protected Long id;

    @NotEmpty
    @Column(nullable = false)
    private String email;

    @NotEmpty
    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubjectMessage subject;
}
