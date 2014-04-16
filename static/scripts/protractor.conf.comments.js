exports.config = {
    seleniumAddress: 'http://localhost:4444/wd/hub',
    baseUrl: 'http://localhost/~evilfer/nquire-it-e2e/src/',
    specs: ['../test/e2e/ProjectCommentsScenario.js'],
    capabilities: {
        'browserName': 'chrome'
    },
    jasmineNodeOpts: {
        showColors: true // Use colors in the command line report.
    }
};