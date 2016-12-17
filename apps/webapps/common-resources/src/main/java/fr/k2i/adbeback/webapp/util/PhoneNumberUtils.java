package fr.k2i.adbeback.webapp.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberType;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import org.springframework.util.StringUtils;

import java.util.Locale;

/**
 * Declares method for normalizing phone numbers.
 * 
 */
public class PhoneNumberUtils {

    private static final String COUNTRY_CODE_PREFIX = "+";

    /**
     * Format phone number with country code (+33 at the moment actually)
     */
    public static String formatPhoneNumber(String phoneNumber) {
        return formatPhoneNumber(phoneNumber, null);
    }

    /**
     * Format phone number by removing spaces, dots and adding the country code
     */
    public static String formatPhoneNumber(String phoneNumber, String country) {
        if (!StringUtils.isEmpty(phoneNumber)) {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                if (!phoneNumber.replaceAll("[ \\.-]", "").startsWith(COUNTRY_CODE_PREFIX) && country == null) {
                    country = Locale.FRANCE.getCountry();
                }
                PhoneNumber number = phoneUtil.parseAndKeepRawInput(phoneNumber, country);
                return phoneUtil.format(number, PhoneNumberFormat.E164);
            } catch (NumberParseException e) {

                String formattedNumberPhone = phoneNumber.replaceAll("[ \\.-]", "");

                if (!formattedNumberPhone.startsWith(COUNTRY_CODE_PREFIX)) {

                    if (country == Locale.FRANCE.getCountry()) {
                        // delete the 0 of the number ( only when the given number starts with 0) we take +33 by default
                        formattedNumberPhone = "+"
                                + phoneUtil.getCountryCodeForRegion(country)
                                + (formattedNumberPhone.startsWith("0") ? formattedNumberPhone.substring(1)
                                        : formattedNumberPhone);
                    } else {
                        formattedNumberPhone = "+" + phoneUtil.getCountryCodeForRegion(country)
                                + formattedNumberPhone.substring(1);
                    }
                }
                return formattedNumberPhone;
            }
        } else {
            return null;
        }
    }

    /**
     * Format the phone number back in the french format (eg 0605040809).
     */
    public static String unformatInFrenchFormat(String phoneNumber) {
        if (!StringUtils.isEmpty(phoneNumber)) {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                PhoneNumber number = phoneUtil.parseAndKeepRawInput(phoneNumber, "FR");
                return phoneUtil.format(number, PhoneNumberFormat.NATIONAL).replace(" ", "");
            } catch (NumberParseException e) {
                return null;
            }
        } else {
            return phoneNumber;
        }
    }

    public static String toNationalFormat(String phoneNumber) {
        if (!StringUtils.isEmpty(phoneNumber)) {
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            try {
                PhoneNumber number = null;
                if (phoneNumber.startsWith("+")) {
                    number = phoneUtil.parseAndKeepRawInput(phoneNumber, null);
                } else {
                    // Défault mode France
                    number = phoneUtil.parseAndKeepRawInput(phoneNumber, "FR");
                }

                return phoneUtil.format(number, PhoneNumberFormat.NATIONAL).replace(" ", "");
            } catch (NumberParseException e) {
                return null;
            }
        } else {
            return phoneNumber;
        }
    }

    /**
     * Split a phone number between its country code and its national phone number.
     * 
     * @param phoneNumber
     *            The phone number to split.
     * @return A string array : [0] is the country code with the "+" (eg. "+33"). [1] is the national number (eg.
     *         612345789).
     */
    public static String[] splitPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return new String[] { "", "" };
        } else {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            try {
                PhoneNumber number = phoneNumberUtil.parse(phoneNumber, phoneNumber.startsWith("+") ? null : "FR");
                return new String[] { "+" + number.getCountryCode(), "" + number.getNationalNumber() };
            } catch (NumberParseException e) {
                return new String[] { "", "" };
            }
        }
    }

    /**
     * Formatage du numéro de téléphone en +XX XXXXXXXXXX
     * 
     * @param mobilePhone
     * @return +XX XXXXXXXXXX
     * @throws NumberParseException
     */
    public static String formatPhoneNumberWithSpace(String mobilePhone) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber number = null;
                try {
                    number = phoneNumberUtil.parseAndKeepRawInput(mobilePhone, null);
                } catch (NumberParseException e) {
                    //Should not happened
                  return "";
                }
        String currentCountryCode = "FR";
        return phoneNumberUtil.formatOutOfCountryCallingNumber(number,currentCountryCode);
    }

    public static boolean isMobilePhoneNumber(String phoneNumber) {
        return isMobilePhoneNumber(phoneNumber, null);
    }

    public static String countryToPrefix(String countryCode) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        int country = phoneUtil.getCountryCodeForRegion(countryCode);
        if (country == 0) {
            return "";
        }
        return COUNTRY_CODE_PREFIX + Long.toString(country);
    }

    public static boolean isMobilePhoneNumber(String phoneNumber, String country) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber number = phoneUtil.parseAndKeepRawInput(phoneNumber, country);
            return (country != null) ?
                        (phoneUtil.isValidNumberForRegion(number, country) && PhoneNumberType.MOBILE == phoneUtil
                                .getNumberType(number))
                    :
                        phoneUtil.isValidNumber(number) && PhoneNumberType.MOBILE == phoneUtil.getNumberType(number);
        } catch (NumberParseException e) {
            return false;
        }
    }



    public static boolean isValidFixedPhoneNumber(String phoneNumber) {
        return isValidFixedPhoneNumber(phoneNumber,null);
    }

    public static boolean isValidFixedPhoneNumber(String phoneNumber,String country) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber number = phoneUtil.parseAndKeepRawInput(phoneNumber, country);
            return (country != null) ?
                    (phoneUtil.isValidNumberForRegion(number, country) && (PhoneNumberType.FIXED_LINE == phoneUtil.getNumberType(number)
                                                                            ||
                                                                            PhoneNumberType.VOIP == phoneUtil.getNumberType(number)))
                    :
                    phoneUtil.isValidNumber(number) && (PhoneNumberType.FIXED_LINE == phoneUtil.getNumberType(number)
                                                           ||
                                                        PhoneNumberType.VOIP == phoneUtil.getNumberType(number));
        } catch (NumberParseException e) {
            return false;
        }
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return isValidPhoneNumber(phoneNumber,null);
    }

	public static boolean isValidPhoneNumber(String phoneNumber,String country) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            PhoneNumber number = phoneUtil.parseAndKeepRawInput(phoneNumber, country);
            return (country != null) ?
                    (phoneUtil.isValidNumberForRegion(number, country))
                :
                    phoneUtil.isValidNumber(number);
        } catch (NumberParseException e) {
            return false;
        }
	}
}
