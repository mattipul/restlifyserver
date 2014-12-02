<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <head>
        <title>Matikkakone - ${function.statement}${statement.statement}</title>
        <link rel="stylesheet" type="text/css" href="/style.css" />
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="codemirror/lib/codemirror.js"></script>
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css">
        <script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
        <link rel="stylesheet" href="codemirror/lib/codemirror.css">
        <script src="codemirror/mode/javascript/javascript.js"></script>
        <script type="text/javascript" src="http://latex.codecogs.com/latexit.js"></script>
    </head>
    <body>

        <div class="yla">            
            <a class="ayla" href="/">Matikkakone</a>
            <a class="ayla" href="/tulkki">Tulkki</a>
            <a class="ayla" href="/api">API</a>
            <div style="clear:both"></div>
        </div>
        <div style="background-color:rgb(230,230,230)" class="yla">            
            <span class="glyphicon glyphicon-play tool"></span>
            <span class="glyphicon glyphicon-paperclip tool"></span>
            <div style="clear:both"></div>
        </div>
        <div id="codearea">
            
        </div>

    </body>
    <script>
       
        var myCodeMirror = CodeMirror(document.getElementById('codearea'), {
            value: "function myScript(){return 100;}\n",
            lineNumbers: true,
            mode: "javascript"
        });
         myCodeMirror.setSize($(document).width(), $(window).height()-70);
    </script>
</html>