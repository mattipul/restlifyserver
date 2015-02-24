<%-- 
    Document   : api
    Created on : Feb 22, 2015, 4:31:54 PM
    Author     : mattipul
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
   <head>
     <title>RESTlify - API Documentation</title>
     <meta name="viewport" content="width=device-width, initial-scale=1.0">
     <link rel="shortcut icon" href="/res/favicon.png" />
     <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootswatch/3.3.2/yeti/bootstrap.min.css">

     <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
     <!--[if lt IE 9]>
       <script src="http://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.2/html5shiv.js"></script>
       <script src="http://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.js"></script>
     <![endif]-->
        <style>
                body{
                        background:rgb(250,250,250);
                }
        </style>
   </head>
   <body>

    <nav class="navbar navbar-default navbar-xs" role="navigation">
             <!-- Brand and toggle get grouped for better mobile display -->
             <div class="navbar-header">
                 <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                     <span class="sr-only">Toggle navigation</span>
                     <span class="icon-bar"></span>
                     <span class="icon-bar"></span>
                     <span class="icon-bar"></span>
                 </button>
                 <ul class="nav navbar-nav">
                     <li><a class="navbar-brand" href="/">RESTlify</a></li>
                 </ul>
             </div>

             <!-- Collect the nav links, forms, and other content for toggling -->
             <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                 <ul class="nav navbar-nav">

                 </ul>
             </div><!-- /.navbar-collapse -->
         </nav>



     <div style="padding-top:0px;" class="container">
                <div class="panel panel-info">
                  <div class="panel-heading">API-key: ${db.apiKey}</div>
                  <div class="panel-body">
                      <b><c:out value="${db.name}"/></b><hr/>
                        <c:out value="${db.description}"/>
                  </div>
                </div>
     </div>



        <div class="container">
            <c:forEach items="${db.classes}" var="def">
                <div class="panel panel-info">
                  <div class="panel-heading" style="text-transform: capitalize">${def.className}</div>
                  <div class="panel-body">
                        <hr/>
                        <span class="label label-success">GET</span> <a href="">http://www.restlify.com/api/${db.apiKey}/${def.className}</a><br/><br/>
                        <span class="label label-success">GET</span> <a href="">http://www.restlify.com/api/${db.apiKey}/${def.className}/{id}</a><br/><br/>
                        <span class="label label-success">GET</span> <a href="">http://www.restlify.com/api/${db.apiKey}/${def.className}?{searchAttribute}={searchValue}</a> <br/><br/>
                        <span class="label label-warning">POST</span> <a href="">http://www.restlify.com/api/${db.apiKey}/${def.className}</a> <br/><br/>
                        <span class="label label-danger">DELETE</span> <a href="">http://www.restlify.com/api/${db.apiKey}/${def.className}/{id}</a> <br/><br/>
                        <hr/>
                        <span class="label label-success">GET</span> <a href="">https://secure.restlify.com/api/${db.apiKey}/${def.className}</a><br/><br/>
                        <span class="label label-success">GET</span> <a href="">https://secure.restlify.com/api/${db.apiKey}/${def.className}/{id}</a><br/><br/>
                        <span class="label label-success">GET</span> <a href="">https://secure.restlify.com/api/${db.apiKey}/${def.className}?{searchAttribute}={searchValue}</a><br/><br/>
                        <span class="label label-warning">POST</span> <a href="">https://secure.restlify.com/api/${db.apiKey}/${def.className}</a> <br/><br/>
                        <span class="label label-danger">DELETE</span> <a href="">https://secure.restlify.com/api/${db.apiKey}/${def.className}/{id}</a>
                  
                  </div>
                </div>
            </c:forEach>
     </div>



     <script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
     <script src="http://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.2.0/js/bootstrap.min.js"></script>

</script>
   </body>
</html>