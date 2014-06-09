<!doctype html>
<?php
// Report all PHP errors (see changelog)
error_reporting(E_ALL);

// Same as error_reporting(E_ALL);
ini_set('error_reporting', E_ALL);
?>

<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Sense It</title>

    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400italic,600italic,400,600,700,800' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="css/font-awesome.css" type='text/css'>
    <link rel="stylesheet" href="css/nquire-it-bootstrap.css" type='text/css'>

    <script src="js/libs/jquery-2.1.0.min.js"></script>
    <script src="js/libs/bootstrap.js"></script>
    <script src="js/libs/angular.js"></script>
    <script src="js/libs/angular-sanitize.js"></script>
    <script src="js/libs/angular-ui-router.js"></script>


    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=true"></script>
    <script src="js/libs/oms.min.js"></script>


    <script src="js/helpers/clone.js"></script>
    <script src="js/helpers/form.js"></script>
    <script src="js/helpers/votes.js"></script>
    <script src="js/helpers/compare.js"></script>
    <script src="js/helpers/senseItSensorData.js"></script>
    <script src="js/helpers/senseItTransformations.js"></script>
    <script src="js/helpers/utils.js"></script>
    <script src="js/helpers/colorgenerator.js"></script>
    <script src="js/helpers/mapicons.js"></script>
    <script src="js/helpers/maprenderer.js"></script>

    <script src="js/app/app.js"></script>


 <?php if(isset($_REQUEST['maps'])): ?>
    <script src="../backend-mockup/angular-mocks.js"></script>
    <script src="../backend-mockup/app-backend.js"></script>

    <?php foreach(explode(',', $_REQUEST['maps']) as $map): ?>
        <script src="../backend-mockup/maps/<?= $map ?>.js"></script>
    <?php endforeach; ?>

 <?php endif; ?>

    <script src="js/app/services/services-module.js"></script>
    <script src="js/app/services/rest-service.js"></script>
    <script src="js/app/services/openid-service.js"></script>
    <script src="js/app/services/project-service.js"></script>
    <script src="js/app/services/project-data-service.js"></script>
    <script src="js/app/services/vote-service.js"></script>
    <script src="js/app/services/comment-service.js"></script>
    <script src="js/app/services/project-senseit-editor-service.js"></script>
    <script src="js/app/services/project-challenge-editor-service.js"></script>
    <script src="js/app/services/project-challenge-admin-service.js"></script>
    <script src="js/app/services/project-challenge-participant-service.js"></script>
    <script src="js/app/services/project-challenge-outcome-service.js"></script>


    <script src="js/app/directives/back-image.js"></script>
    <script src="js/app/directives/side-menu-button.js"></script>
    <script src="js/app/directives/side-menu.js"></script>
    <script src="js/app/directives/vote-widget.js"></script>
    <script src="js/app/directives/file-select-widget.js"></script>
    <script src="js/app/directives/table-widget.js"></script>
    <script src="js/app/directives/map.js"></script>
    <script src="js/app/directives/youtube.js"></script>
    <script src="js/app/directives/profile-provider-item.js"></script>
    <script src="js/app/directives/user-block.js"></script>

    <script src="js/app/filters/uploaded-image-filter.js"></script>

    <script src="js/app/controllers/layout/main-controller.js"></script>

    <script src="js/app/controllers/profile/profile-controller.js"></script>

    <script src="js/app/controllers/project-list/project-list-controller.js"></script>




</head>

<?php if(isset($_REQUEST['maps'])): ?>
    <body data-ng-app="senseItWebDev">
<?php else: ?>
    <body data-ng-app="senseItWeb">
<?php endif; ?>

    <div id="main" data-ng-controller="MainCtrl">
        <div id="header" data-ng-include="'partials/layout/header.html'"></div>

        <div class="main-content-section">
            <div class="main-content">
                <div data-ui-view></div>
            </div>
        </div>
    </div>

</body>
</html>
