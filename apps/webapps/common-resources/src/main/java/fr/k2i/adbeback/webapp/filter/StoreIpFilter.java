package fr.k2i.adbeback.webapp.filter;

import fr.k2i.adbeback.webapp.util.IPStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@Component("storeIpFilter") // be sure the name doesn't change : it's used by a DelegatingFilterProxy.
public class StoreIpFilter extends GenericFilterBean {

    private Logger log = LoggerFactory.getLogger(getClass());


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        String ip = getClientAddress(httpServletRequest);


        IPStorage.setLocation(ip);
        log.info(String.format("User IP = %s", ip));

        // export these informations
        HttpSession session = httpServletRequest.getSession(false);

        if (session != null) {
            session.setAttribute("ipAddress", ip);
        }

        chain.doFilter(req, res);

        IPStorage.resetLocation();
    }

    public String getClientAddress(HttpServletRequest request){
        String clientAddress;
        String xforwardedforHeader = request.getHeader("X-FORWARDED-FOR");
        if(xforwardedforHeader != null){
            int firstComa  = xforwardedforHeader.indexOf(',');
            clientAddress = firstComa>0?xforwardedforHeader.substring(0, firstComa):xforwardedforHeader;
        }else{
            log.warn("This should only happen in developement");
            clientAddress = request.getRemoteAddr();
        }
        if(log.isDebugEnabled()) {
            log.debug(String.format("X-FORWARDED-FOR = %s, user ip = %s", xforwardedforHeader, clientAddress));
        }
        return clientAddress;
    }
}
