angular.module('senseItWeb', null, null).controller('ProjectEditMetadataCtrl', function ($scope, $state, ProjectService) {

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

    $scope.addMetadataBlock = function (type) {
        var block;
        switch (type) {
            case 'text':
                block = {type: 'text', header: 'Title', content: 'Text'};
                break;
            case 'www':
                block = {type: 'www', where: "Where?", who: "Who?", when: 'When?'};
                break;
            default:
                block = null;
        }

        if (block != null) {
            $scope.projectData.project.description.blocks.push(block);
            $scope.projectWatcher.saveMetadata();
        }
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

