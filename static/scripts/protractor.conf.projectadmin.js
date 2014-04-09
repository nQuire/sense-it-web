exports.config = {
    seleniumAddress: 'http://localhost:4444/wd/hub',
    baseUrl: 'http://localhost/~evilfer/nquire-it-e2e/src/index-e2e.html',
    specs: ['../test/e2e/ProjectAdminScenario.js'],
    capabilities: {
        'browserName': 'chrome'
    },
    jasmineNodeOpts: {
        showColors: true // Use colors in the command line report.
    }
};