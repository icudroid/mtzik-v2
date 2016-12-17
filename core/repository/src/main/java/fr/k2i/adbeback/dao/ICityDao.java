package fr.k2i.adbeback.dao;

import fr.k2i.adbeback.core.business.country.City;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: dimitri
 * Date: 29/01/14
 * Time: 13:55
 * Goal:
 */
public interface ICityDao extends IGenericDao<City, Long> {
    @Transactional
    public List<City> findByZipcodeAndCountry_Code(String zipcode,String code);
    @Transactional
    public City findByZipcodeAndCityAndCountry_Code(String zipcode,String city,String code);
    @Transactional
    public List<City> findByCityStartingWith(String name);
}
