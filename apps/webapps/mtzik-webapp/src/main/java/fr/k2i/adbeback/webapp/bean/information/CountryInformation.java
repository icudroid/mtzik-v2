package fr.k2i.adbeback.webapp.bean.information;

import lombok.Data;

/**
 * User: dimitri
 * Date: 24/03/14
 * Time: 17:03
 * Goal:
 */
@Data
public class CountryInformation{
    private String countryCode;

    public CountryInformation(){}
    public CountryInformation(String code) {
        countryCode = code;
    }
}
