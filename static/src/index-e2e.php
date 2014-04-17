<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Sense It</title>

    <link rel="stylesheet" href="css/font-opensans.css">
    <link rel="stylesheet" href="css/nquire-it-bootstrap.css">

    <script src="js/libs/jquery-2.1.0.min.js"></script>
    <script src="js/libs/bootstrap.js"></script>
    <script src="js/libs/angular.js"></script>
    <script src="js/libs/angular-sanitize.js"></script>
    <script src="js/libs/angular-ui-router.js"></script>

    <script src="../backend-mockup/angular-mocks.js"></script>

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

    <script src="../backend-mockup/app-backend.js"></script>

    <?php
        foreach(explode(',', $_REQUEST['maps']) as $map) {
            echo "<script src=\"../backend-mockup/maps/$map.js\"></script>\n";
        }
    ?>


    <script src="js/app/services/services-module.js"></script>
    <script src="js/app/services/rest-service.js"></script>
    <script src="js/app/services/openid-service.js"></script>
    <script src="js/app/services/project-service.js"></script>
    <script src="js/app/services/project-data-service.js"></script>
    <script src="js/app/services/vote-service.js"></script>
    <script src="js/app/services/comment-service.js"></script>
    <script src="js/app/services/project-senseit-service.js"></script>
    <script src="js/app/services/project-challenge-editor-service.js"></script>
    <script src="js/app/services/project-challenge-admin-service.js"></script>
    <script src="js/app/services/project-challenge-participant-service.js"></script>
    <script src="js/app/services/project-challenge-outcome-service.js"></script>


    <script src="js/app/directives/side-menu-button.js"></script>
    <script src="js/app/directives/side-menu.js"></script>
    <script src="js/app/directives/vote-widget.js"></script>
    <script src="js/app/directives/file-select-widget.js"></script>
    <script src="js/app/directives/table-widget.js"></script>
    <script src="js/app/directives/map.js"></script>
    <script src="js/app/directives/project-description.js"></script>
    <script src="js/app/directives/youtube.js"></script>
    <script src="js/app/directives/profile-provider-item.js"></script>


    <script src="js/app/controllers/layout/main-controller.js"></script>
    <script src="js/app/controllers/layout/navbar-controller.js"></script>
    <script src="js/app/controllers/project-list/project-list-controller.js"></script>

    <script src="js/app/controllers/profile-controller.js"></script>
    <script src="js/app/controllers/data-project-menu-controller.js"></script>
    <script src="js/app/controllers/description-editor-controller.js"></script>
    <script src="js/app/controllers/comments-controller.js"></script>
    <script src="js/app/controllers/project-admin-controller.js"></script>
    <script src="js/app/controllers/project-admin-users-controller.js"></script>
    <script src="js/app/controllers/project-admin-challenge-controller.js"></script>


        <script src="js/app/controllers/project/project-controller.js"></script>
        <script src="js/app/controllers/project/project-view-controller.js"></script>

    <script src="js/app/controllers/project-view-challenge-outcome-controller.js"></script>
    <script src="js/app/controllers/project-view-data-controller.js"></script>
    <script src="js/app/controllers/project-view-senseit-controller.js"></script>
    <script src="js/app/controllers/project-view-senseit-analysis-controller.js"></script>
    <script src="js/app/controllers/project-view-challenge-answers-controller.js"></script>
    <script src="js/app/controllers/project-view-challenge-answers-table-controller.js"></script>
    <script src="js/app/controllers/project-view-challenge-answers-item-controller.js"></script>
    <script src="js/app/controllers/project-view-challenge-stage-proposal-controller.js"></script>
    <script src="js/app/controllers/project-view-challenge-stage-vote-controller.js"></script>
    <script src="js/app/controllers/project-view-challenge-stage-outcome-controller.js"></script>
    <script src="js/app/controllers/project-edit-controller.js"></script>
    <script src="js/app/controllers/project-edit-menu-controller.js"></script>
    <script src="js/app/controllers/project-edit-metadata-controller.js"></script>
    <script src="js/app/controllers/project-edit-senseit-controller.js"></script>
    <script src="js/app/controllers/project-edit-senseit-profile-controller.js"></script>
    <script src="js/app/controllers/project-edit-senseit-sensor-controller.js"></script>
    <script src="js/app/controllers/project-edit-senseit-analysis-controller.js"></script>
    <script src="js/app/controllers/project-edit-challenge-controller.js"></script>
    <script src="js/app/controllers/project-edit-challenge-field-controller.js"></script>
    <script src="js/app/controllers/create-controller.js"></script>

    <script src="js/app/controllers/project-view-senseit-test-upload-controller.js"></script>

</head>
<body data-ng-app="senseItWebDev">

    <div class="container" data-ng-controller="MainCtrl">
        <div data-ng-include="'partials/layout/navbar.html'"></div>
        <div data-ui-view></div>
    </div>

</body>
</html>
