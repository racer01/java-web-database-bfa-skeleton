<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>TODO Login</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TODO app</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
          crossorigin="anonymous">
    <link rel="stylesheet" href="index.css">
    <link rel="stylesheet" href="style.css">
    <script
            src="https://code.jquery.com/jquery-3.2.1.min.js"
            integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4="
            crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <script src="./sha1.min.js"></script>
    <script src="./login.js"></script>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-6">
            <nav class="navbar navbar-default">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                                data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                            <span class="sr-only">Toggle navigation</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="">TODO app</a>
                    </div>
                </div>
            </nav>
        </div>
    </div>
    <div class="row">
        <div class="col-md-2">
            <c:if test="${errortype == 'loginerror'}">
                <c:set var="loginClass" value="has-error" />
                <div class="alert alert-danger" role="alert">
                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                    <span class="sr-only">Error:</span>
                        ${errormsg}
                </div>
            </c:if>
            <form id="loginform" method="post" class="${loginClass}">
                <input name="method" value="login" hidden>
                <div class="form-group">
                    <label class="control-label" for="loginuser">Username</label>
                    <input type="text" class="form-control" id="loginuser" name="user" placeholder="Username" autofocus>
                </div>
                <div class="form-group">
                    <label class="control-label" for="loginpass">Password</label>
                    <input type="password" class="form-control" id="loginpass" name="pass" placeholder="Password">
                </div>
                <button type="button" class="btn btn-default" id="loginbtn" onclick="login()">Login</button>
            </form>
        </div>

        <div class="col-md-2">
            <c:if test="${errortype == 'regerror'}">
                <c:set var="regClass" value="has-error" />
                <div class="alert alert-danger" role="alert">
                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                    <span class="sr-only">Error:</span>
                        ${errormsg}
                </div>
            </c:if>
            <form id="regform" method="post" class="${regClass}">
                <input name="method" value="register" hidden>
                <div class="form-group">
                    <label class="control-label" for="reguser">Username</label>
                    <input type="text" class="form-control" id="reguser" name="user" placeholder="Username"
                           autofocus>
                </div>
                <div class="form-group">
                    <label class="control-label" for="regpass">Password</label>
                    <input type="password" class="form-control" id="regpass" name="pass" placeholder="Password">
                </div>
                <button type="button" class="btn btn-default" id="regbtn" onclick="register()">Register</button>
            </form>
        </div>
    </div>
    <div class="row">
    </div>
</div>
</body>
</html>
