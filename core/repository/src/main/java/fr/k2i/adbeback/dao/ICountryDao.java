package fr.k2i.adbeback.dao;

import fr.k2i.adbeback.core.business.country.Country;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: dimitri
 * Date: 29/01/14
 * Time: 13:59
 * Goal:
 */
public interface ICountryDao extends IGenericDao<Country, Long> {
    @Transactional
    public Country findByCode(String code);
}
