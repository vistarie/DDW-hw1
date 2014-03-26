<%-- 
    Document   : response
    Created on : 23.3.2014, 15:03:07
    Author     : Martina
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="style.css" />
        <title>Wikipedia Miner</title>
    </head>
    <body>
        <jsp:useBean id="webtext" scope="session" class="mypackage.WebText" />
        <jsp:setProperty name="webtext" property="url" />
        
        <div id="main">
        <h1>Wikipedia Miner</h1>
        <div id="content">
            <strong>Results:</strong>
            <%
            webtext.mine();
            %>

            
          
           <c:set var="property" value="0" />          
            
            <c:forEach var="look" items="${webtext.lookup}"> 
                <c:if test="${fn:length(look) > 0}"> 
                <div class="column">
                
                <jsp:setProperty name="webtext" property="lookupId" value="${property}" />
                <c:if test="${property == 0}"><strong>Companies: </strong></c:if>
                <c:if test="${property == 1}"><strong>Countries: </strong></c:if>
                <c:if test="${property == 2}"><strong>Cities: </strong></c:if>
                <c:if test="${property == 3}"><strong>Buildings: </strong></c:if>
                </c:if>
                
                <c:forEach var="lookup" items="${look}"> 

                <jsp:setProperty name="webtext" property="urlString" value="${lookup}" />    
                
                <%
                webtext.checkUrl();
                %>
                <jsp:getProperty name="webtext" property="urlString" ></jsp:getProperty>
                
                </c:forEach>
                <c:set var="property" value="${property +1}" /> 
               
                <c:if test="${fn:length(look) > 0}"> 
                </div>
                </c:if>
            </c:forEach>

        </div>
           <div class="return">
        <a href="index.jsp">Return</a>
        </div>
        </div>
        
    </body>
</html>
