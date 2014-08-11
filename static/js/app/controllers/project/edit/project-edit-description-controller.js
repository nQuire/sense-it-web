angular.module('senseItWeb', null, null).controller('ProjectEditDescriptionCtrl', function ($scope, ModalService) {


    $scope.tempBlock = false;

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
        var block = {title: '', content: ''};
        $scope.tempBlock = $scope.projectData.project.metadata.blocks.length;
        $scope.projectData.project.metadata.blocks.push(block);
        $scope.projectWatcher.saveMetadata();

        $scope.form.open('block:' + ($scope.projectData.project.metadata.blocks.length - 1));
    };

    $scope.moveMetadataBlock = function (index, up) {
        if (SigUtils.moveArrayItem($scope.projectData.project.metadata.blocks, index, up)) {
            $scope.projectWatcher.saveMetadata();
        }
    };

    $scope.deleteMetadataBlock = function (index) {

        ModalService.open({
            body: 'Are you sure you want to delete this information block?',
            title: 'Delete block',
            ok: function () {
                $scope.projectData.project.metadata.blocks.splice(index, 1);
                $scope.projectWatcher.saveMetadata();
                return true;
            }
        });

    };

    $scope.form = new SiwFormManager(function () {
            return $scope.projectData.project;
        }, ['title', 'metadata'],
        function () {
            $scope.tempBlock = false;
            $scope.projectWatcher.saveMetadata($scope.form.files);
        }, function () {
            if ($scope.tempBlock !== false) {
                var index = $scope.tempBlock;
                $scope.tempBlock = false;
                $scope.deleteMetadataBlock(index);
            }
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

