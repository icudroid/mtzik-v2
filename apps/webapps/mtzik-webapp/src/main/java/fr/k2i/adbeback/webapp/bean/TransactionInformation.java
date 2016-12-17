package fr.k2i.adbeback.webapp.bean;

import fr.k2i.adbeback.webapp.bean.information.Information;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * User: dimitri
 * Date: 25/03/14
 * Time: 17:15
 * Goal:
 */

@Data
public class TransactionInformation implements Serializable {
    private double amount;
    private String currencyCode;
    private String idPartner;
    private String idTransaction;
    private Information informations;
    private String callSysUrl;
    private String callBackUrl;
    private Boolean selfAd;//utilise ses propre pub
    private Date transactionDate;
    private String validation;
}
