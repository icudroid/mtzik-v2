package fr.k2i.adbeback.dao.jpa;

import com.mysema.query.jpa.impl.JPAQuery;
import fr.k2i.adbeback.core.business.country.City;
import fr.k2i.adbeback.core.business.country.QCity;
import fr.k2i.adbeback.dao.ICityDao;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CityDao extends GenericDaoJpa<City, Long> implements ICityDao {



    @Override
    public List<City> findByZipcodeAndCountry_Code(String zipcode, String code) {
        QCity city = QCity.city1;

        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(city).where(city.zipcode.eq(zipcode).and(city.country.code.eq(code)));
        return query.list(city);
    }

    @Override
    public City findByZipcodeAndCityAndCountry_Code(String zipcode, String city, String code) {
        QCity qcity = QCity.city1;

        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(qcity).where(qcity.zipcode.eq(zipcode).and(qcity.country.code.eq(code)));
        return query.uniqueResult(qcity);

    }

    @Override
    public List<City> findByCityStartingWith(String name) {
        QCity city = QCity.city1;

        JPAQuery query = new JPAQuery(getEntityManager());
        query.from(city).where(city.city.startsWithIgnoreCase(name));
        return query.list(city);

    }
}

