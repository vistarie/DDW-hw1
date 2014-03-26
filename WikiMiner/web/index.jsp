<%-- 
    Document   : index
    Created on : 23.3.2014, 14:51:48
    Author     : Martina
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="style.css" />
        <title>Wikipedia Miner</title>
    </head>
    <body>
        <div id="main">
        <h1>Wikipedia Miner</h1>
         <div id="content">
        <form name="urlAdress" action="response.jsp">
            Input wikipedia URL:<br />
            <input type="text" name="url" /><br />
            <input type="submit" value="OK" />
        </form>
         </div>
        </div>
    </body>
</html>
