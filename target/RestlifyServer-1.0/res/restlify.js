
var Restlify = (function () {

    return{
        signUp: function () {
            $("#successAlert").hide();
            $("#errorAlert").hide();
            var username = $("#username").val();
            var password1 = $("#password1").val();
            var password2 = $("#password2").val();
            var email = $("#email").val();
            if ($( "#termsCheck" ).prop( "checked")===true){
                $.post("/register", {username: username, password: password1, passwordagain: password2, email: email})
                        .done(function (data) {
                            var obj = data;
                            if (obj !== undefined) {
                                if (obj.success !== undefined) {
                                    if (obj.success === 1) {
                                        $("#successAlert").show();
                                    } else {
                                        $("#errorAlert").html("ERROR: " + obj.errorMessage);
                                        $("#errorAlert").show();
                                    }
                                    $("#username").val("");
                                    $("#password1").val("");
                                    $("#password2").val("");
                                    $("#email").val("");
                                }
                            }
                        });
            }else{
                $("#errorAlert").html("ERROR: Remember to accept Terms and Conditions.");
                $("#errorAlert").show();
            }
        }
    }

})();
