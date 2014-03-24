'use strict';

describe('Form manager Test', function () {

    var original, obj, fm;

    beforeEach(function () {
        module('senseItWeb');

        original = {a: 1, b: 2};
        obj = {a: 1, b: 2};
        fm = new SiwFormManager(obj, ['a']);
        fm.open();
    });

    it('show prepare form values on open', function () {
        expect(fm.values['a']).toBe(obj['a']);
    });

    it('show ignore unused object variables', function () {
        expect(typeof fm.values['b']).toBe('undefined');
    });

    describe('Tests with changes', function () {
        beforeEach(function () {
            fm.values['a'] = 4;
        });

        describe('On cancel', function () {
            beforeEach(function () {
                fm.cancel();
            });

            it('shouldn\' modify obj on cancel', function () {
                expect(obj.a).toBe(original.a);
            });
        });

        describe('On save', function () {
            beforeEach(function () {
                fm.save();
            });

            it('should have modified obj on save', function () {
                expect(obj.a).toBe(4);
            });
        });

    });

});
