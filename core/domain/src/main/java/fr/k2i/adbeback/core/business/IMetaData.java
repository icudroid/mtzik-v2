package fr.k2i.adbeback.core.business;

/**
 * Created with IntelliJ IDEA.
 * User: dev
 * Date: 16/12/13
 * Time: 21:44
 * To change this template use File | Settings | File Templates.
 */
public interface IMetaData {

    interface TableMetadata {
        String CITY =                                   "city";
        String OTP =                                    "one_time_pwd";
        String TRANSACTION =                            "transaction";
    }

    interface ColumnMetadata {

        public interface Contact {
            String ID =                                 "id";
            String LASTNAME =                           "lastname";
            String FIRSTNAME =                          "firstname";
            String SEX =                                "sex";
            String PHONE =                              "phone";
            String MOBILE =                             "mobile";
            String EMAIL =                              "email";
            String BRAND =                              "brand_id";
            String FUNCTION =                           "function";
        }

        public interface Product {
            String ID =                                 "id";
            String NAME =                               "name";
            String DESC =                               "description";
            String PUBLIC_FEE =                         "public_fee";
            String AD_FEE =                             "ad_fee";
            String LOGO =                               "logo";
        }

        public interface ViewedMedia {
            String ID =                                 "id";
            String PLAYER =                             "player_id";
            String AD =                                 "ad_id";
            String NB =                                 "nb";
            String RULE =                               "rule_id";
        }

        public interface City {
            String ZIPCODE =                            "zipcode";
            String CITY =                               "city";
            String LON =                                "lon";
            String LAT =                                "lat";
            String COUNTRY =                            "country_id";
        }

        public interface Address {
            String CITY_JOIN =                          "city_id";
            String COUNTRY_JOIN =                       "country_id";
        }

        public interface OTPSecurity {
            String KEY =                                "otp_key";
            String CREATION_DATE =                      "creation_date";
            String EXPIRATION_DATE =                    "expiration_date";
            String BRAND_JOIN =                         "brand_id";

            public interface Discrimator {
                String DISCRIMINATOR =                  "classe";
                String BRAND =                          "brand";
            }
        }

    }
}
