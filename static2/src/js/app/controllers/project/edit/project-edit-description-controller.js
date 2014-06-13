angular.module('senseItWeb', null, null).controller('ProjectEditDescriptionCtrl', function ($scope, $state, ProjectService) {

    $scope.metadataEdit = true;

    $scope.metadataBlockButtons = function (block) {
        if ($scope.form.isOpen(block)) {
            return 'open';
        } else if ($scope.form.isOpen()) {
            return false;
        } else {
            return 'close';
        }
    };

    $scope.addMetadataBlock = function () {
        var block = {title: 'Title', content: 'Content'};
        $scope.projectData.project.description.blocks.push(block);
        $scope.projectWatcher.saveMetadata();

        $scope.form.open('block:' + ($scope.projectData.project.description.blocks.length - 1));
    };

    $scope.moveMetadataBlock = function (index, up) {
        var blocks = $scope.projectData.project.description.blocks;
        var otherIndex = index + (up ? -1 : 1);
        if (index >= 0 && index < blocks.length && otherIndex >= 0 && otherIndex < blocks.length) {
            var temp = blocks[otherIndex];
            blocks[otherIndex] = blocks[index];
            blocks[index] = temp;
            $scope.projectWatcher.saveMetadata();
        }
    };

    $scope.deleteMetadataBlock = function (index) {
        $scope.projectData.project.description.blocks.splice(index, 1);
        $scope.projectWatcher.saveMetadata();
    };

    $scope.form = new SiwFormManager(function () {
            return $scope.projectData.project;
        }, ['title', 'description'],
        function () {
            $scope.projectWatcher.saveMetadata($scope.form.files);
        }
    );


    $scope.filelistener = {
        set: function (key, file) {
            $scope.form.setFile(key, file);
        },
        clear: function (key) {
            $scope.form.clearFile(key);
        },
        deleteFile: function (key) {
            $scope.form.deleteFile(key);
        }
    };

});

