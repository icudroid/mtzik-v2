package fr.k2i.adbeback.dao.jpa;

import fr.k2i.adbeback.core.business.contact.ContactForm;
import fr.k2i.adbeback.dao.IContactFormDao;
import org.springframework.stereotype.Repository;


@Repository
public class ContactFormDao extends GenericDaoJpa<ContactForm, Long> implements IContactFormDao {



}

