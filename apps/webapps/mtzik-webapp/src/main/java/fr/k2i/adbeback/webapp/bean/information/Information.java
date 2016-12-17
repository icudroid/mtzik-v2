package fr.k2i.adbeback.webapp.bean.information;

import lombok.Data;

/**
 * User: dimitri
 * Date: 24/03/14
 * Time: 17:02
 * Goal:
 */
@Data
public class Information {
    private AgeInformation age;
    private CityInformation city;
    private SexInformation sex;
    private  CountryInformation country;
}

