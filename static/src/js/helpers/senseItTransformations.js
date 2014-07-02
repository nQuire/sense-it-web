var SiwSenseItVariable = function (options) {
    this.raw = options.raw;
    this.editable = !this.raw;

    if (this.raw) {
        this.id = '' + options.input.id;
        this.name = SiwSenseItSensorData.sensorTypes[options.input.sensor].name + ' raw data';
        this.tx = null;
        this.output = SiwSenseItSensorData.sensorTypes[options.input.sensor].output;
        this.weight = options.weight;
    } else {
        this.id = options.tx.id;
        this.tx = options.tx;
        this.weight = options.tx.weight;
    }
};

SiwSenseItVariable.prototype.label = function () {
    return this.raw ? this.name : this.tx.name;
}

SiwSenseItVariable.prototype.outputLabel = function () {
    return this.output in SiwSenseItSensorData.dataTypes ? SiwSenseItSensorData.dataTypes[this.output] : '?';
};

SiwSenseItVariable.prototype.txLabel = function () {
    return SiwSenseItSensorData.transformations[this.tx.type].name;
};

var SiwSenseItTransformations = function (sensorInputs, transformations) {
    this.sensorInputs = sensorInputs;

    this.setTx(transformations);
};

SiwSenseItTransformations.prototype._variableById = function (vid) {
    for (var i = 0; i < this.variables.length; i++) {
        if (this.variables[i].id == vid) {
            return this.variables[i];
        }
    }

    return null;
};

SiwSenseItTransformations.prototype._inputVariables = function (variable) {
    var self = this;
    return variable.tx.inputs.map(function (inputId) {
        return self._variableById(inputId);
    });
};


SiwSenseItTransformations.prototype.inputVariables = function (variable) {
    if (variable.tx) {
        var inputs = this._inputVariables(variable);
        for (var i = inputs.length; i < 1; i++) {
            inputs.push(null);
        }
        return inputs;
    }
    return null;
};

SiwSenseItTransformations.prototype.setTx = function (transformations) {
    this.transformations = transformations != null ? transformations : [];
    this._updateVariables();
};

SiwSenseItTransformations.prototype.availableInputVariables = function (variable) {
    var options = SiwSenseItSensorData.transformations[variable.tx.type].data;
    var aiv = {};

    for (var i = 0; i < this.variables.length; i++) {
        var tv = this.variables[i];
        if (this.independent(tv, variable.id) && (options === 'any' || tv.output in options)) {
            aiv[tv.id] = tv;
        }
    }

    return aiv;
};

SiwSenseItTransformations.prototype.independent = function (variable, fromId) {
    if (variable.id == fromId) {
        return false;
    } else if (variable.raw) {
        return true;
    }

    var inputs = this._inputVariables(variable);
    for (var i = 0; i < inputs.length; i++) {
        if (!this.independent(inputs[i], fromId)) {
            return false;
        }
    }

    return true;
};

SiwSenseItTransformations.prototype._updateVariables = function () {
    this.variables = [];

    var outputSet = {};

    for (var i = 0; i < this.sensorInputs.length; i++) {
        var input = this.sensorInputs[i];
        if (input.sensor && input.sensor.length > 0) {
            var v = new SiwSenseItVariable({raw: true, input: input, weight: i - this.sensorInputs.length});
            this.variables.push(v);
            outputSet[v.id] = true;
        }
    }

    for (i = 0; i < this.transformations.length; i++) {
        v = new SiwSenseItVariable({raw: false, tx: this.transformations[i]});
        this.variables.push(v);
    }

    var continueChecking = true;
    while (continueChecking) {
        continueChecking = false;
        for (i = 0; i < this.transformations.length; i++) {
            var tx = this.transformations[i];
            if (!outputSet[tx.id]) {
                var inputsReady = tx.inputs.length == 1;
                if (inputsReady) {
                    for (var j = 0; j < 1; j++) {
                        if (!outputSet[tx.inputs[j]]) {
                            inputsReady = false;
                            break;
                        }
                    }
                }

                if (inputsReady) {
                    input = this._variableById(tx.inputs[0]).output;
                    var typeConversion = SiwSenseItSensorData.transformations[tx.type].data;
                    this._variableById(tx.id).output = typeConversion === 'any' ? input : typeConversion[input];
                    outputSet[tx.id] = true;
                    continueChecking = true;
                }
            }
        }
    }
}
;

SiwSenseItTransformations.prototype.sequenceVariables = function () {
    return this.variables.filter(function (v) {
        return v && v.output && v.output.length > 0 && v.output[0] == '[';
    });
};

SiwSenseItTransformations.prototype.nonSequenceVariables = function () {
    return this.variables.filter(function (v) {
        return v && v.output && v.output.length > 0 && v.output[0] != '[';
    });
};

