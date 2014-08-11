var SiwMapRenderer = function ($scope, element, mapData, dataList) {
    this.$scope = $scope;
    this.element = element;

    this.mapData = mapData;
    this.dataList = dataList;

    this.map = null;
    this.markers = {};

    this.iconMaker = new SiwMapIcons(this.mapData.mapVariables.length);


    this.clusterMaxZoom = 18;
    this.markerFocusZoom = this.clusterMaxZoom + 1;

    var self = this;

    self._updateSelectedIndex();
    self.init();
    self.update();
};

SiwMapRenderer.prototype._updateSelectedIndex = function () {
    this.mapData.selectedIndex = this._indexById(this.mapData.selected);
};

SiwMapRenderer.prototype._indexById = function (id) {
    for (var i = 0; i < this.dataList.data.length; i++) {
        if (this.dataList.data[i].id == id) {
            return i;
        }
    }

    return -1;
};


SiwMapRenderer.prototype._calculateMaxMin = function () {
    var max = 0;
    var min = 0;

    for (var i = 0; i < this.dataList.data.length; i++) {
        for (var j = 0; j < this.mapData.mapVariables.length; j++) {
            var value = this.mapData.value(this.dataList.data[i], this.mapData.mapVariables[j]);
            max = Math.max(max, value);
            min = Math.min(min, value);
        }
    }

    this.iconMaker.update(max, min);
};

SiwMapRenderer.prototype.getIcon = function (i, mode) {
    return this.iconMaker.getIcon(this.mapData.iconText(this.dataList.data[i]), mode);
};

SiwMapRenderer.prototype.getLatLngById = function (id) {
    for (var i = 0; i < this.dataList.data.length; i++) {
        if (this.dataList.data[i].id == id) {
            return this.getLatLng(i);
        }
    }

    return null;
};

SiwMapRenderer.prototype.getLatLng = function (i) {
    var location = i < this.dataList.data.length ? this.mapData.location(this.dataList.data[i]) : false;
    return location ? new google.maps.LatLng(location.lat, location.lon) : false;
};

SiwMapRenderer.prototype.init = function () {
    this.element.css('height', '600px');

    var center = this.mapData.selectedIndex ? this.getLatLng(this.mapData.selectedIndex) : this.getLatLng(0);
    if (center) {
        this.centerSet = true;
    } else {
        this.centerSet = false;
        center = new google.maps.LatLng(0, 0);
    }

    var mapOptions = {
        center: center,
        zoom: this.centerSet ? this.markerFocusZoom : 2
    };

    this.map = new google.maps.Map(this.element[0], mapOptions);
    this.oms = new OverlappingMarkerSpiderfier(this.map, {
        keepSpiderfied: true
    });
    this.clusterer = new MarkerClusterer(this.map);
    this.clusterer.setMaxZoom(this.clusterMaxZoom);

    var self = this;


    this.oms.addListener('click', function (marker, event) {
        self.markers[marker.series_id].infowindow.open(self.map, marker);
        self.select(marker.data_id);
        return false;
    });


    this.oms.addListener('spiderfy', function (markers) {

    });

    this.oms.addListener('unspiderfy', function (markers) {
        for (var i = 0; i < markers.length; i++) {
            self.markers[markers[i].series_id].infowindow.close();
        }
    });

    google.maps.event.addListener(this.map, 'idle', function () {
        self.idle();
    });

};

SiwMapRenderer.prototype.reset = function () {
    for (var id in this.markers) {
        if (this.markers.hasOwnProperty(id)) {
            this.markers[id].marker.setMap(null);
            this.markers[id].infowindow.close();
        }
    }
    this.oms.clearMarkers();
    this.clusterer.clearMarkers();
    this.markers = {};
    if (this.heatMap) {
        this.heatMap.setMap(null);
        this.heatMap = null;
    }
};


SiwMapRenderer.prototype.select = function (itemId) {
    var self = this;
    this.$scope.$apply(function () {
        self.mapData.selected = itemId;
    });
};

SiwMapRenderer.prototype.updateSelection = function () {
    if (this.mapData.selectedIndex >= 0 && this.markers[this.mapData.selectedIndex]) {
        this.updateIcon(this.mapData.selectedIndex, 'normal');
        this.markers[this.mapData.selectedIndex].infowindow.close();
    }

    this._updateSelectedIndex();
    if (this.mapData.selectedIndex >= 0) {
        this.updateIcon(this.mapData.selectedIndex, 'selected');

        var pos = this.getLatLng(this.mapData.selectedIndex);
        if (pos) {
            this.map.panTo(pos);
        }
    }

};

SiwMapRenderer.prototype.update = function () {
    this.reset();
    this._calculateMaxMin();

    var heatData = [];


    this._updateSelectedIndex();
    console.log(this.mapData.selectedIndex);

    if (!this.centerSet && this.dataList.data.length > 0) {
        var center = this.mapData.selectedIndex ? this.getLatLng(this.mapData.selectedIndex) : this.getLatLng(0);
        if (center) {
            this.map.setZoom(this.markerFocusZoom);
            this.map.setCenter(center);
            this.centerSet = true;
        }
    }

    for (var i = 0; i < this.dataList.data.length; i++) {
        var item = this.dataList.data[i];
        var pos = this.getLatLng(i);
        if (pos) {
            heatData.push({location: pos, weight: this.mapData.getItemHeat(item)});
            var isSelected = i == this.mapData.selectedIndex;

            var marker = new google.maps.Marker({
                position: pos,
                map: this.map,
                animation: google.maps.Animation.DROP,
                icon: this.getIcon(i, isSelected ? 'selected' : 'normal'),
                title: "" + (i + 1),
                series_id: i,
                data_id: item.id
            });

            this.clusterer.addMarker(marker);
            this.oms.addMarker(marker);

            var content = this.mapData.infoWindow(item);
            /*            for (var j = 0; j < this.mapData.mapVariables.length; j++) {
             var v = this.mapData.mapVariables[j];

             if (content.length > 0) {
             content += '<br/>';
             }
             content += '<b>' + v.label + ':</b> ' + this.mapData.value(, v).toPrecision(4);
             }
             */
            this.markers[i] = {
                marker: marker,
                infowindow: new google.maps.InfoWindow({content: content})
            };

            if (isSelected) {
                this.markers[i].infowindow.open(this.map, marker);
            }
        }
    }

    this.heatMap = new google.maps.visualization.HeatmapLayer({
        data: heatData,
        dissipating: true,
        radius: 50
    });

    this.heatMap.setMap(this.map);

};

SiwMapRenderer.prototype.idle = function () {
};

SiwMapRenderer.prototype.updateIcon = function (index, mode) {
    if (this.markers[index]) {
        this.markers[index].marker.setIcon(this.getIcon(index, mode));
    }
};
