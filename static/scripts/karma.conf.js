module.exports = function (config) {
    config.set({
        basePath: '../',

        files: [
            'src/js/libs/angular.js',
            'src/js/libs/angular-sanitize.js',
            'src/js/libs/angular-ui-router.js',
            'src/js/helpers/*.js',
            'src/js/app/app.js',
            'src/js/app/services/services-module.js',
            'src/js/app/services/*.js',
            'src/js/app/directives/*.js',
            'src/js/app/controllers/**/*.js',
            'test/lib/angular/angular-mocks.js',
            'test/unit/project-generic/*.js'
        ],

        exclude: [
        ],

        autoWatch: true,

        frameworks: ['jasmine'],

        browsers: ['Chrome'],

        plugins: [
            'karma-junit-reporter',
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine'
        ],

        junitReporter: {
            outputFile: 'test_out/unit.xml',
            suite: 'unit'
        }

    })
};
