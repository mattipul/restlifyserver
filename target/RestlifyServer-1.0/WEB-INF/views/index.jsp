 
<%-- 
    Document   : api
    Created on : Feb 22, 2015, 4:31:54 PM
    Author     : mattipul
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>RESTlify</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="Public, RESTful Data Repositories in the Cloud. We offer the platform, you can offer the data." />
        <meta name="keywords" content="REST, Cloud, Web service, service, SOA, JSON, RESTful, Restlify, api, database">
        <meta name="author" content="restlify">
        <meta property="og:title" content="RESTlify" />
        <meta property="og:description" content="Public, RESTful Data Repositories in the Cloud. We offer the platform, you can offer the data." />
        <meta property="og:url" content="http://restlify.com/" />
        <meta property="article:published_time" content="2015-01-13" />
        <meta property="article:modified_time" content="2015-01-24" />
        <meta property="og:site_name" content="restlify" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootswatch/3.3.2/journal/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
        <script src="https://google-code-prettify.googlecode.com/svn/loader/run_prettify.js"></script>
        <script src="/res/restlify.js"></script>
        <link rel="shortcut icon" href="/res/favicon.png" />
    </head>
    <style>
                body{
                        background:rgb(245,245,245);
                }
        .panel {
            border-radius:0px !important;
                        box-shadow:0px 0px 5px rgb(200,200,200);
        }
    </style>
    <body>












        <div class="modal" id="termsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">TERMS AND CONDITIONS</h4>
                    </div>
                    <div class="modal-body">
                        <p><strong>TERMS AND CONDITIONS</strong></p>
                        <p><br /><strong>1. Introduction.</strong><br />These Terms And Conditions (these &ldquo;Terms&rdquo; or these &ldquo;Terms And Conditions&rdquo;) contained<br />herein on this webpage, shall govern your use of this service, including all pages within this<br />website (collectively referred to herein below as this &ldquo;Service&rdquo;). These Terms apply in full force<br />and effect to your use of this Service and by using this Service, you expressly accept all terms<br />and conditions contained herein in full. You must not use this Service, if you have any objection<br />to any of these Service Standard Terms And Conditions.</p>
                        <p><br /><strong>2. Intellectual Property Rights.</strong><br />Other than content you own, which you may have opted to include on this Service, under these<br />Terms, Bittihattara(Toiminimi) and/or its licensors own all rights to the intellectual property and<br />material contained in this Service, and all such rights are reserved.<br />You are granted a limited license only, subject to the restrictions provided in these Terms, for<br />purposes of viewing the material contained on this Service,</p>
                        <p><br /><strong>3. Restrictions.</strong><br />You are expressly and emphatically restricted from all of the following:<br />1. using this Service in any way that is, or may be, damaging to this Service;<br />2. using this Service in any way that impacts user access to this Service;<br />3. using this Service contrary to applicable laws and regulations, or in a way that causes, or<br />may cause, harm to the Service, or to any person or business entity;<br />Certain areas of this Service are restricted from access by you and Bittihattara(Toiminimi) may<br />further restrict access by you to any areas of this Service, at any time, in its sole and absolute<br />discretion. Any user ID and password you may have for this Service are confidential and youmust maintain confidentiality of such information.</p>
                        <p><br /><strong>4. Your Content.</strong><br />In these Service Standard Terms And Conditions, &ldquo;Your Content&rdquo; shall mean any audio, video,<br />text, images or other material you choose to display on this Service. With respect to Your<br />Content, by displaying it, you grant Bittihattara(Toiminimi) a non-exclusive, worldwide,<br />irrevocable, royalty-free, sublicensable license to use, reproduce, adapt, publish, translate and<br />distribute it in any and all media.<br />Your Content must be your own and must not be infringing on any third party&rsquo;s rights.<br />Bittihattara(Toiminimi) reserves the right to remove any of Your Content from this Service at<br />any time, and for any reason, without notice.</p>
                        <p><br /><strong>5. No warranties.</strong><br />This Service is provided &ldquo;as is,&rdquo; with all faults, and Bittihattara(Toiminimi) makes no express or<br />implied representations or warranties, of any kind related to this Service or the materials<br />contained on this Service. Additionally, nothing contained on this Service shall be construed as<br />providing consult or advice to you.</p>
                        <p><br /><strong>6. Limitation of liability.</strong><br />In no event shall Bittihattara(Toiminimi), nor any of its officers, directors and employees, be<br />liable to you for anything arising out of or in any way connected with your use of this Service,<br />whether such liability is under contract, tort or otherwise, and Bittihattara(Toiminimi), including<br />its officers, directors and employees shall not be liable for any indirect, consequential or special<br />liability arising out of or in any way related to your use of this Service.</p>
                        <p><br /><strong>7. Indemnification.</strong><br />You hereby indemnify to the fullest extent Bittihattara(Toiminimi) from and against any and all<br />liabilities, costs, demands, causes of action, damages and expenses (including reasonable<br />attorney&rsquo;s fees) arising out of or in any way related to your breach of any of the provisions ofthese Terms.</p>
                        <p><br /><strong>8. Severability.</strong><br />If any provision of these Terms is found to be unenforceable or invalid under any applicable law,<br />such unenforceability or invalidity shall not render these Terms unenforceable or invalid as a<br />whole, and such provisions shall be deleted without affecting the remaining provisions herein.</p>
                        <p><br /><strong>9. Variation of Terms.</strong><br />Bittihattara(Toiminimi) is permitted to revise these Terms at any time as it sees fit, and by using<br />this Service you are expected to review such Terms on a regular basis to ensure you<br />understand all terms and conditions governing use of this Service.</p>
                        <p><br /><strong>10. Assignment.</strong><br />Bittihattara(Toiminimi) shall be permitted to assign, transfer, and subcontract its rights and/or<br />obligations under these Terms without any notification or consent required. However, .you shall<br />not be permitted to assign, transfer, or subcontract any of your rights and/or obligations under<br />these Terms.</p>
                        <p><br /><strong>11. Entire Agreement.</strong><br />These Terms, including any legal notices and disclaimers contained on this Service, constitute<br />the entire agreement between Bittihattara(Toiminimi) and you in relation to your use of this<br />Service, and supersede all prior agreements and understandings with respect to the same.</p>
                        <p><br /><strong>12. Governing Law &amp; Jurisdiction.</strong><br />These Terms will be governed by Finnish law, excluding connecting factor rules. Any possible<br />disputes shall be settled by the District Court of Helsinki according to Finnish law.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>






























        <nav class="navbar navbar-default navbar-xs" role="navigation">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#"><img style="height:22px" src="/res/favicon.png"/></a>
                <ul class="nav navbar-nav">
                    <li><a href="#">RESTlify</a></li>
                </ul>
            </div>

            <!-- Collect the nav links, forms, and other content for toggling -->
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav">

                </ul>
            </div><!-- /.navbar-collapse -->
        </nav>

        <div id="container" style="padding:20px;margin-top:-20px;max-width:1000px;margin:auto">
            <div style="display:none" class="alert alert-success" id="successAlert" role="alert">SUCCESS: You are now signed up! You can now start creating databases, defining classes and importing/exporting data.</div>
            <div style="display:none" class="alert alert-danger" id="errorAlert" role="alert"></div>
            <div class="panel panel-default">
                <div style="text-align:left" class="panel-body">
                    <div class="row">
                        <div class="col-md-8">
                            <h2>
                                RESTful Repository
                                <br/>
                                <small>A cloud storage and a RESTful Web Service in the same package.</small>
                            </h2><hr/>
                            <h3>- Sign Up <small>It's free, and it takes about a second.</small></h3>
                            <h3>- Create a database <small>Take a look at the API documentation.</small></h3>
                            <h3>- Define its structure <small>Define individual classes or a whole array of them.</small></h3>
                            <h3>- Start opening your data <small>Import stuff and publish it!</small></h3><hr/>
                        </div>
                        <div class="col-md-4">
                            <div style="text-align:left" class="panel panel-default">
                                <div class="panel-body">
                                    <h4>Sign Up Now!</h4><hr/>
                                    <input type="text" class="form-control" id="username" placeholder="Username" aria-describedby="sizing-addon1">
                                    <input type="password" class="form-control" id="password1" placeholder="Password" aria-describedby="sizing-addon1">
                                    <input type="password" class="form-control" id="password2" placeholder="Password again" aria-describedby="sizing-addon1">
                                    <input type="email" class="form-control" id="email" placeholder="Email" aria-describedby="sizing-addon1">
                                    <br/><p><input id="termsCheck" type="checkbox"/> I agree to <a href="#" data-toggle="modal" data-target="#termsModal">the Terms & Conditions.</a></p>
                                    <hr/><button type="button" class="btn btn-success" onclick="Restlify.signUp();">Sign Up</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div style="text-align:left" class="panel panel-default">
                <div class="panel-body">
                    <h3>What's this?</h3><hr/>
                    <p>Restlify is a platform-as-a-service(PaaS). This means that we provide a platform for your open, public data. Every Restlify database is an open, public database. And every Restlify database is a RESTful web service. We provide the cloud, the security, the time and the service. We made it easy for you to publicize and open data and information that can be used by others to develop something new and exciting. But remember to keep private stuff private! No passwords, secret keys or patient information here, please.</p>
                    <p>You and your friends and customers can use and consume your Restlify databases with any (generic or not) REST client or programming library. Take a look at the API documentation. It's quite simple, right? <b>Just remember that an "id"-attribute will be assigned to all objects by the database itself. Use the "id"-attribute, when you are updating or referencing. But, when creating new objects or defining classes, don't add the "id"-attributes yourself</b>.
                    </p>
                </div>
            </div>

                        <div style="text-align:left" class="panel panel-default">
                <div class="panel-body">
                    <h3>Getting started.</h3><hr/>
                                        <i>HINT: For every major web browser, there is some sort of generic HTTP/REST add-on client.</i><br/><br/>
                                        <p>Remember that we offer a platform(basically, a cloud database) for your open data. This means that <b>you</b> have to create and define the database you need. But it is fairly easy, if you know what HTTP, JSON and REST mean. If not, take a look at <a href="http://en.wikipedia.org/wiki/Representational_state_transfer">http://en.wikipedia.org/wiki/Representational_state_transfer</a>. That article will lead you to the right path. </p>
                                        <p>After signing up, you will need to authenticate yourself before making any request that are not nullipotent. So, if you want to create a database, you will have to log in to our system first.</p>
                                        <p>Creating a database is a simple task. Use a HTTP-client to authenticate yourself, and then send a POST-request to /create_database. Take a look at the API-documentation below. If everything goes well, the service will return an API-key for you.</p>
                                        <p>You can use this API-key to define classes. This is propably the most important step you will need to take. The API-documentation below describes how a class is defined as JSON-object. A class has a name and a right amount of attributes. Attributes have a key and a type. And they can be lists or single objects(references to other classes) or primitives. It is propably wise to define the whole database once as an array of class defining objects.</p>
                                        <p>After you finally manage to define the database structure, you can start updating and consuming the database through a RESTful web service.</p>
                                        <p>All web services are located at https://www.restlify.com/api/{apiKey}. <b>GET-requests can be made without any authentication.</b>
                                </div>
            </div>

             <div style="text-align:left" class="panel panel-default">
                 <div class="panel-body">
                     <h3>API</h3><hr/>
                     <pre class="prettify">
     <b>Create a database (body in JSON-format) (BASIC AUTHENTICATION REQUIRED)</b>
     POST /create_database HTTP/1.1    
     HOST: https://www.restlify.com
     {
        "name":"databaseName", 
        "description":"databaseDescription"
     }<br/>
     <i>RETURNS API-key: {"apiKey":"..."}</i>
                     </pre>

                     <pre class="prettify">
     <b>List databases (BASIC AUTHENTICATION REQUIRED)</b>
     GET /get_databases HTTP/1.1
     HOST: https://www.restlify.com
     <i>RETURNS API-key list in JSON-format: ["{apiKey1}", "{apiKey2}", ...]</i>
                     </pre>

                     <pre class="prettify">
     <b>Define a class (body in JSON-format) (BASIC AUTHENTICATION REQUIRED)</b>
     POST /define_class/{apiKey} HTTP/1.1
     HOST: https://www.restlify.com
     {
         "name":"className",
         "attributes":[
             {"key":"attributeName", "type":"int|double|string|boolean|otherClassName", "list":true|false}
             ...
         ]
     }
     <b>(When re-defining, the class must be "empty" of objects)</b>
                     </pre>

                     <pre class="prettify">
     <b>Define an array of classes (body in JSON-format) (BASIC AUTHENTICATION REQUIRED)</b>
     POST /define_class/{apiKey} HTTP/1.1
     HOST: https://www.restlify.com
     [
         {
             "name":"className",
             "attributes":[
                 {"key":"attributeName", "type":"int|double|string|boolean|otherClassName", "list":true|false}
                 ...
             ]
         }
         ...
     ]
     <b>(When re-defining, the classes must be "empty" of objects)</b>
                     </pre>



                     <pre class="prettify">
     <b>Get all class definitions (BASIC AUTHENTICATION REQUIRED)</b>
     POST /get_classes/{apiKey} HTTP/1.1
     HOST: https://www.restlify.com
     <i>RETURNS a list of class definitions in JSON-format</i>
                     </pre>


                     <pre class="prettify">
     <b>Get a class definition (BASIC AUTHENTICATION REQUIRED)</b>
     POST /get_class/{apiKey}/{className} HTTP/1.1
     HOST: https://www.restlify.com
     <i>RETURNS a class definition in JSON-format</i>
                     </pre>

                     <pre class="prettify">
     <b>Destroy a class (BASIC AUTHENTICATION REQUIRED)</b>
     DELETE /destroy_class/{apiKey}/{className} HTTP/1.1
     HOST: https://www.restlify.com
     <b>DELETES EVERY SINGLE INSTANCE OF THAT CLASS</b>
                     </pre>

 
                 </div>
             </div>


         </div>



            <footer class="footer">

                <p class="text-muted">2015 - Bittihattara - <small style="color:rgb(100,100,100);">contacts@restlify.com</small></p>

            </footer>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-60031371-1', 'auto');
  ga('send', 'pageview');

</script>
    </body>
</html>