var SiwMapIconsConsts = {
    plotHeight: 15,
    plotWidth: 30,
    vMargin: 4,
    hMargin: 5,
    ohMargin: 1,
    ovMargin: 4,
    maxLineWidth: 5,
    arroWidth: 20,
    arrowHeight: 20
};

var SiwMapIcons = function (n) {
    this.n = n;
    this.update(0, 0);
};

SiwMapIcons.prototype.update = function (max, min) {
    this.max = Math.max(max, 0);
    this.min = Math.min(min, 0);
    this.range = this.max - this.min;

    this.iconWidth = SiwMapIconsConsts.plotWidth + 2 * (SiwMapIconsConsts.hMargin + SiwMapIconsConsts.ohMargin);
    this.iconHeight = SiwMapIconsConsts.plotHeight + 2 * (SiwMapIconsConsts.vMargin + SiwMapIconsConsts.ovMargin) + SiwMapIconsConsts.arrowHeight;


    if (this.range > 0) {
        this.scale = SiwMapIconsConsts.plotHeight / this.range;
        this.zeroH = this.min * this.scale + SiwMapIconsConsts.arrowHeight + SiwMapIconsConsts.vMargin + SiwMapIconsConsts.ovMargin;
    } else {
        this.zeroH = 0;
        this.scale = 0;
    }

    var lineSpace = SiwMapIconsConsts.plotWidth / (this.n + 1);
    this.colors = [];
    this.positions = [];
    for (var i = 0; i < this.n; i++) {
        this.colors.push(SiwColorGenerator.getColor(i, 1, .7));
        this.positions.push(SiwMapIconsConsts.ohMargin + SiwMapIconsConsts.hMargin + lineSpace * (i + 1));
    }
    this.lineWidth = Math.min(SiwMapIconsConsts.maxLineWidth, lineSpace);

    this.svgBg = this.getBg(false);
    this.svgSelectedBg = this.getBg(true);
    this.svgLine = this.getZeroLine();
};

SiwMapIcons.prototype.getZeroLine = function () {
    var dx = .5 * SiwMapIconsConsts.plotWidth;
    var cx = .5 * this.iconWidth;
    var y = this.zeroH;
    var line = '<line x1="' + (cx - dx) + '" y1="' + y + '" x2="' + (cx + dx) + '" y2="' + y + '" style="stroke:black;stroke-width:1" />';
    return line;
};

SiwMapIcons.prototype.getBg = function (selected) {
    var cx = .5 * this.iconWidth;
    var hw = cx - SiwMapIconsConsts.ohMargin;
    var w = 2 * hw;
    var y0 = this.iconHeight - SiwMapIconsConsts.ovMargin;
    var aw = .5 * SiwMapIconsConsts.arroWidth;
    var sdx = hw - aw;
    var dy = SiwMapIconsConsts.plotHeight + 2 * SiwMapIconsConsts.vMargin;

    return '<path style="stroke:black;stroke-width:1; fill:' + (selected ? '#f39a19' : '#eee') + '" d="m' + cx + ',' + y0
        + ' a' + aw + "," + SiwMapIconsConsts.arrowHeight + ' 0 0,1 ' + aw + ',-' + SiwMapIconsConsts.arrowHeight
        + ' h' + sdx + ' v-' + dy + 'h-' + w + ' v' + dy + ' h' + sdx
        + ' a' + aw + "," + SiwMapIconsConsts.arrowHeight + ' 0 0,1 ' + aw + ',' + SiwMapIconsConsts.arrowHeight
        + '"/>';

};

SiwMapIcons.prototype.getIcon = function (value, mode) {
    var icon = 'data:image/svg+xml,<svg width="' + this.iconWidth + '" height="' + this.iconHeight + '" xmlns="http://www.w3.org/2000/svg"><g>'
        + (mode == 'selected' ? this.svgSelectedBg : this.svgBg);

    icon += '<text fill="black" font-family="sans-serif" font-size="10" style="text-anchor: middle;"' +
        ' x="' + (.5 * this.iconWidth) + '" y="' + (.4 * this.iconHeight) + '">' + value + '</text>';

    /*    for (var i = 0; i < values.length; i++) {
     icon += '<line x1="' + this.positions[i] + '" y1="' + this.zeroH + '" x2="' + this.positions[i] + '" y2="' + (this.zeroH - this.scale * values[i])
     + '" style="stroke:' + this.colors[i] + ';stroke-width:' + this.lineWidth + '" />';
     }
     icon += this.svgLine
     */
    icon += '</g></svg>';

    console.log("v: " + value);
    return icon;
};


