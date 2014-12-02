<%-- 
    Document   : frame
    Created on : Nov 4, 2014, 10:04:57 PM
    Author     : mattipul
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Laatua - Ota yhteyttä!</title>
        <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/smoothness/jquery-ui.css" />
        <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
        <link href="//cdnjs.cloudflare.com/ajax/libs/bootswatch/3.3.0/yeti/bootstrap.css" rel="stylesheet">
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>   
        <link rel="shortcut icon" href="/favicon.png">
    </head>
    <body>

        <nav class="navbar navbar-invert" role="navigation">
            <div class="container-fluid">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="/">Laatua</a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">

                    
                </div><!-- /.navbar-collapse -->
            </div><!-- /.container-fluid -->
        </nav>

        <div class="container">

            <form role="form" action="/rek" method="post" style="width:400px; margin: 0 auto;">
                <h1>Ota yhteyttä!<br/><small>Tämä ei velvoita mihinkään.</small></h1>

                <input type="text" name="yritys" placeholder="Yritys" class="form-control">

                <input type="text" name="osoite" placeholder="Osoite" class="form-control">

                <input type="text" name="postinumero" placeholder="Postinumero" class="form-control">

                <input type="text" name="kaupunki" placeholder="Kaupunki" class="form-control">

                <input type="text" name="maa" placeholder="Maa" class="form-control">

                <input type="text" name="puhelinnumero" placeholder="Puhelinnumero" class="form-control">

                <input type="text" name="sahkopostiosoite" placeholder="Sähköpostiosoite" class="form-control">

                <textarea rows="3" name="viesti" class="form-control" placeholder="Viesti"></textarea>


                <button class="btn btn-primary">Lähetä!</button>
            </form>
        </div>

    </body>
</html> 