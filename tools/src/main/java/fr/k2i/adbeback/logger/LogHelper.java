package fr.k2i.adbeback.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Helper responsible for log instances retrieval. getLog methods are used to
 * get Log instances. declareApplicationName and clearLogContext are used for
 * log context management. Requires commons-lang to be in the classpath.
 * 
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class LogHelper {

    /**
     * Map used to cache encapsulated logger instances.
     */
    private static final ConcurrentMap<String, Logger> LOGS = new ConcurrentHashMap<String, Logger>();

    private static final String BUSINESS_CATEGORY = "business.";
    private static final String PERF_CATEGORY = "perfs.";
    private static Logger auditLogger = LogHelper.getLogger("audit");

/*    public static final void audit(String message, Object... args) {
        StringBuilder messageSB = new StringBuilder(message);
        completeWithAuthentication(messageSB);
        auditLogger.info(messageSB.toString(), args);
    }*/

    public static final void business(String category, String message, Object... args) {
        getLogger(BUSINESS_CATEGORY + category).info(message, args);
    }


    public static final void businessWarn(String category, String message, Object... args) {
        getLogger(BUSINESS_CATEGORY + category).warn(message, args);
    }

    public static final void businessError(String category, String message, String... args) {
        getLogger(BUSINESS_CATEGORY + category).error(message, args);
    }

    public static final void perfLog(String category, long startTime) {
        Logger perfLogger = getLogger(PERF_CATEGORY + category);
        if (perfLogger.isInfoEnabled()) {
            long stopTime = System.currentTimeMillis();
            perfLogger.info("total time : {} ms", stopTime - startTime);
        }
    }


    /**
     * Retrieves a logger for the named category.
     * 
     * @param categoryName
     * 
     * @return the Log instance (an instance of BaseLog)
     */
    public static final Logger getLogger(String categoryName) {
        Logger log = LOGS.get(categoryName);

        if (log == null) {
            log = LoggerFactory.getLogger(categoryName);
            LOGS.put(categoryName, log);
        }
        return log;
    }

    /**
     * Retrieves a logger for the specified class.
     * 
     * @param clazz
     * 
     * @return the Log instance (an instance of BaseLog)
     */
    public static final Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }
/*
    public static void completeWithAuthentication(StringBuilder msg) {
        msg.append(" by ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            msg.append("anonymous");
        } else {
            msg.append(authentication.getName());
            if (authentication.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
                msg.append(" coming from " + details.getRemoteAddress());
            }
        }
    }*/
    
    /**
     * Get a logger whose category is prefixed with "business." .
     * 
     * @param clazz
     * @return
     */
    public static final Logger getBusinessLogger(Class<?> clazz) {
        return getLogger(BUSINESS_CATEGORY + clazz.getName());
    }

    // /**
    // * Removes the application name from the MDC and pops the NDC (= removes
    // the
    // * random string added by <code>declareApplicationName</code>.
    // */
    // public static void clearLogContext() {
    // MDC.clear();
    // }

    // public static void addInLogContext(String string) {
    // MDC.push(string);
    // }
    //
    // public static String popLogContext() {
    // return NDC.pop();
    // }

    // public static boolean isBusinessLogEnabled(String subCategory) {
    // return getLogger(BUSINESS_CATEGORY + subCategory).isInfoEnabled();
    // }

}
