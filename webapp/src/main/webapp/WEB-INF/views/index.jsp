<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>TITLE</title>

    <c:set var="url">${pageContext.request.requestURL}</c:set>
    <base href="${fn:substring(url, 0, fn:length(url) - fn:length(pageContext.request.requestURI))}${pageContext.request.contextPath}/"/>

    <link href="resources/css/bootstrap-3.0.2.css" rel="stylesheet" media="screen">
    <link href="resources/css/bootstrap-theme-3.0.2.css" rel="stylesheet">

    <script type="text/javascript" src="resources/js/jquery-1.8.2.js"></script>
    <script type="text/javascript" src="resources/js/jquery-ui-1.9.1.min.js"></script>
    <script type="text/javascript" src="resources/js/bootstrap-3.0.2.js"></script>
</head>

<body>

<div class="container">

    <div class="header">
        <ul class="nav nav-pills pull-right">
            <li class="active"><a href="${pageContext.request.contextPath}">Home</a></li>
            <li><a href="j_spring_cas_security_logout">Logout</a>
        </ul>
        <h2><img src="resources/img/logo.png"></h2>

        <h3 class="muted">Stellenbosch IVR</h3>
    </div>

    <hr>

    <h3>Add Contacts To Campaign</h3>
    <br>

    <div id="successMessage" class="alert alert-success">(placeholder)</div>

    <div id="failureMessage" class="alert alert-danger">(placeholder)</div>

    <form role="form" class="form-horizontal" enctype="multipart/form-data" id="uploadForm">
        <div class="form-group">
            <label for="campaignId" class="col-sm-2 control-label">Campaign ID:</label>
            <div class="col-sm-10">
                <input type="number" id="campaignId" name="campaignId" class="form-control" required/>
            </div>
        </div>
        <div class="form-group">
            <label for="file" class="col-sm-2 control-label">File:</label>
            <div class="col-sm-10">
                <input type="file" id="file" name="file" class="form-control" required/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button id="uploadButton" class="btn btn-default" type="submit">Submit
                </button>
            </div>
        </div>
    </form>


    <hr>

    <div class="footer">
        <p>&copy; Cell-Life (NPO) - 2013</p>
    </div>

</div>

<script>

    $(document).ready(function () {

        $("#successMessage").hide();
        $("#failureMessage").hide();

        $('#uploadForm').submit(function(event){

            event.preventDefault();

            var fd = new FormData(document.getElementById("uploadForm"))

            $.ajax({
                url: 'service/campaign/' + document.querySelector('#campaignId').value + '/contacts',
                type: 'POST',
                data: fd,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    document.getElementById("successMessage").innerHTML = "Contacts added successfully."
                    $("#successMessage").show();
                    $("#failureMessage").hide();

                },
                error: function (data) {
                    document.getElementById("failureMessage").innerHTML = "Error adding contacts."
                    $("#failureMessage").show();
                    $("#successMessage").hide();
                }
            });

        });

    });

</script>

</body>

</html>
