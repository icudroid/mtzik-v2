package fr.k2i.adbeback.dao.jpa;

import com.mysema.query.jpa.impl.JPAQuery;
import fr.k2i.adbeback.core.business.country.Country;
import fr.k2i.adbeback.core.business.country.QCountry;
import fr.k2i.adbeback.dao.ICountryDao;
import org.springframework.stereotype.Repository;

/**
 * User: dimitri
 * Date: 29/01/14
 * Time: 14:00
 * Goal:
 */
@Repository
public class CountryDao extends GenericDaoJpa<Country, Long> implements ICountryDao {

    @Override
    public Country findByCode(String code) {
        QCountry qCountry = QCountry.country;
        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(qCountry).where(qCountry.code.eq(code));
        return query.uniqueResult(qCountry);
    }
}
