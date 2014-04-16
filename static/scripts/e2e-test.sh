#!/bin/bash

module="protractor.conf.${1}.js"
echo
echo
echo "Testing: ${module}"
echo
echo

../node_modules/protractor/bin/protractor ${module}
