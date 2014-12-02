<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
    <head>
        <title>Matikkakone - ${function.statement}${statement.statement}</title>
        <link rel="stylesheet" type="text/css" href="/style.css" />
        <meta charset="UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="http://latex.codecogs.com/latexit.js"></script>
    </head>
    <body>

        <div class="yla">            
            <a class="ayla" href="/">Matikkakone</a>
            <a class="ayla" href="/tulkki">SAGE</a>
            <a class="ayla" href="/tulkki">GNU Octave</a>
            <a class="ayla" href="/tulkki">RScript</a>
            <a class="ayla" href="/api">API</a>
            <div style="clear:both"></div>
        </div>
        
       

        <div class="bar">
            <img style="margin-top:20px;" src="/logo.png" />
            <form action="/" method="get">
                <input type="text" value="${function.statement}${statement.statement}" id="eval" name="eval"/>
            </form>
        </div>
        <c:if test="${function!=null}">   
            <div class="palkki">
                <p>Syöte:</p>
                <br/>
                <div class="info" lang="latex">
                    ${function.latexStatement}
                </div>
            </div>


            <div class="palkki">
                <p>Juuret:</p>
                <br/>
                <c:forEach items="${function.roots}" var="root">
                    <div class="info" lang="latex">                
                        ${root}
                    </div>
                </c:forEach>
            </div>

            <div class="palkki">
                <p>Derivaatta:</p>
                <br/>
                <div class="info" lang="latex">
                    \frac{du}{dt}(${function.latexStatement})=${function.derivative}
                </div>
            </div>

            <div class="palkki">
                <p>Derivaatan ominaisuuksia:</p>
                <br/>
                <div class="info" lang="latex">
                    \\f'(${function.character})>0, kun ${function.derivativeHZero}
                </div>
                <div class="info" lang="latex">
                    \\f'(${function.character})<0, kun ${function.derivativeLZero}
                </div>
                <div class="info" lang="latex">
                    \\f'(${function.character})\geq 0, kun ${function.derivativeHEZero}
                </div>
                <div class="info" lang="latex">
                    \\f'(${function.character})\leq 0, kun ${function.derivativeLEZero}
                </div>
                <div class="info" lang="latex">
                    \\f'(${function.character})=0, kun ${function.derivativeEquZero}
                </div>
            </div>

            <div class="palkki">
                <p>Määräämätön integraali:</p>
                <br/>
                <div class="info" lang="latex">
                    \ \int ${function.latexStatement}\,d${function.character} = ${function.integral}+C
                </div>
            </div>

            <div class="palkki">
                <p>Raja-arvoja:</p>
                <br/>
                <div class="info" lang="latex">
                    $$\lim_{${function.character}\to\infty} ${function.latexStatement}=${function.limitInfinityPlus}$$
                </div>
                <div class="info" lang="latex">
                    $$\lim_{${function.character}\to-\infty} ${function.latexStatement}=${function.limitInfinityMinus}$$
                </div>
                <div class="info" lang="latex">
                    $$\lim_{${function.character}\to\0} ${function.latexStatement}=${function.limitInfinityZero}$$
                </div>
            </div>
           
        </c:if>
        <c:if test="${statement!=null}">
            <div class="palkki">
                <p>Syöte:</p>
                <br/>
                <div class="info" lang="latex">
                    ${statement.statement}
                </div>
            </div>

            <div class="palkki">
                <p>Ratkaisut:</p>
                <br/>
                <c:forEach items="${statement.solutions}" var="solution">
                    <div class="info" lang="latex">                
                        ${solution}
                    </div>
                </c:forEach>
            </div>

        </c:if>
        <br/><br/><br/><br/>
    </body>

</html>
