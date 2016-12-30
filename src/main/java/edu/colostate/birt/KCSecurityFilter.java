package edu.colostate.birt;

// Import required java libraries

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

// Implements Filter class
public class KCSecurityFilter implements Filter {
    public void init(FilterConfig config)
            throws ServletException {
        // Get init parameter
//        String testParam = config.getInitParameter("test-param");

        //Print the init parameter
        //       System.out.println("Test Param: " + testParam);
    }

    private String encodeRemoteUser(String remoteUser) {
        return remoteUser.replace('@', '_').replace('.', '_');
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws java.io.IOException, ServletException {

        // Get the IP address of client machine.
        String ipAddress = request.getRemoteAddr();

        // Log the IP address and current timestamp.
        System.out.println("IP " + ipAddress + ", Time "
                + new Date().toString());

        String remoteUser = (String) request.getAttribute("REMOTE_USER");
        String reportName = request.getParameter("__report");
        if (!reportName.endsWith(".rptdesign")) {
            String requestor = reportName.substring(reportName.lastIndexOf('.') + 1);
            if (remoteUser == null || !requestor.equals(encodeRemoteUser(remoteUser))) {
                throw new ServletException("User Not Authorized");
            }
        }
        // Pass request back down the filter chain
        chain.doFilter(request, response);
    }

    public void destroy() {
      /* Called before the Filter instance is removed
      from service by the web container*/
    }
}
