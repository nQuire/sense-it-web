var SiwMapRenderer = function (element, mapData, dataList) {
    this.element = element;

    this.mapData = mapData;
    this.dataList = dataList;

    this.map = null;
    this.markers = {};

    this.iconMaker = new SiwMapIcons(this.mapData.mapVariables.length);

    this.needToInitCloseMarkers = false;

    this.init();
    this.update();
};

SiwMapRenderer.prototype._calculateMaxMin = function () {
    var max = 0;
    var min = 0;

    for (var i = 0; i < this.dataList.items.length; i++) {
        for (var j = 0; j < this.mapData.mapVariables.length; j++) {
            var value = this.mapData.value(this.dataList.items[i], this.mapData.mapVariables[j]);
            max = Math.max(max, value);
            min = Math.min(min, value);
        }
    }

    this.iconMaker.update(max, min);
};

SiwMapRenderer.prototype.getIcon = function (i, mode) {
    var values = [];
    for (var vi = 0; vi < this.mapData.mapVariables.length; vi++) {
        var value = this.mapData.value(this.dataList.items[i], this.mapData.mapVariables[vi]);
        values.push(value);
    }

    return this.iconMaker.getIcon(values, mode);
};

SiwMapRenderer.prototype.getLatLng = function (i) {
    var location = i < this.dataList.items.length ? this.mapData.location(this.dataList.items[i]) : false;
    return location ? new google.maps.LatLng(location.lat, location.lon) : false;
};

SiwMapRenderer.prototype.init = function () {
    this.element.css('height', '600px');

    var center = this.getLatLng(0);
    if (center) {
        this.centerSet = true;
    } else {
        this.centerSet = false;
        center = new google.maps.LatLng(0, 0);
    }

    var mapOptions = {
        center: center,
        zoom: 8
    };

    this.map = new google.maps.Map(this.element[0], mapOptions);
    this.oms = new OverlappingMarkerSpiderfier(this.map);

    var self = this;


    this.oms.addListener('click', function (marker, event) {
        self.markers[marker.series_id].infowindow.open(self.map, marker);
    });


    this.oms.addListener('spiderfy', function (markers) {
        self.setMarkersIcon(markers, 'green');
        for (var i = 0; i < markers.length; i++) {
            self.markers[markers[i].series_id].infowindow.open(self.map, markers[i]);
        }
    });

    this.oms.addListener('unspiderfy', function (markers) {
        self.setMarkersIcon(markers, 'red');
    });

    google.maps.event.addListener(this.map, 'idle', function () {
        self.idle();
    });

};

SiwMapRenderer.prototype.reset = function () {
    for (var id in this.markers) {
        this.markers[id].marker.setMap(null);
        this.markers[id].infowindow.close();
    }
    this.oms.clearMarkers();
    this.markers = {};
};

SiwMapRenderer.prototype.update = function () {
    this.reset();
    this._calculateMaxMin();

    if (!this.centerSet && this.dataList.items.length > 0) {
        var center = this.getLatLng(0);
        if (center) {
            this.map.panTo(center);
            this.centerSet = true;
        }
    }

    for (var i = 0; i < this.dataList.items.length; i++) {
        var pos = this.getLatLng(i);
        if (pos) {

            var marker = new google.maps.Marker({
                position: pos,
                map: this.map,
                animation: google.maps.Animation.DROP,
                icon: this.getIcon(i, 'normal'),
                title: "" + (i + 1),
                series_id: i
            });

            this.oms.addMarker(marker);

            var content = '';
            for (var j = 0; j < this.mapData.mapVariables.length; j++) {
                var v = this.mapData.mapVariables[j];

                if (content.length > 0) {
                    content += '<br/>';
                }
                content += '<b>' + v.label + ':</b> ' + this.mapData.value(this.dataList.items[i], v).toPrecision(4);
            }

            this.markers[i] = {
                marker: marker,
                infowindow: new google.maps.InfoWindow({content: content})
            };
        }
    }

    this.needToInitCloseMarkers = true;

};

SiwMapRenderer.prototype.idle = function () {
    if (this.needToInitCloseMarkers) {
        this.needToInitCloseMarkers = false;
        this.setMarkersIcon(this.oms.markersNearAnyOtherMarker(), 'red');
    }
};

SiwMapRenderer.prototype.setMarkersIcon = function (markers, icon) {
    for (var i = 0; i < markers.length; i++) {
        //markers[i].setIcon(this.icons[icon]);
    }
};
