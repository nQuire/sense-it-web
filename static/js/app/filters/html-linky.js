/**
 * Variation of https://github.com/angular/angular.js/blob/master/src/ngSanitize/filter/linky.js#L88
 * Can be applied to html source code.
 * Unlike the original linky, this filter does not sanitize output.
 */
angular.module('senseItWeb', null, null).filter('htmlLinky', function () {
  var LINKY_URL_REGEXP =
    /(<[^>]*)?((((ftp|https?):\/\/)|(mailto:)|(www\.))[A-Za-z0-9._%#+-\/]*)(\s*<\s*\/\s*a\s*>)?/;

  return function (text, target) {
    if (!text) return text;
    var match;
    var raw = text;
    var html = [];
    var mt;
    var www;
    var href;
    var openingTag, closingA, alreadyA;
    var i, end;

    while ((match = raw.match(LINKY_URL_REGEXP))) {
      // We can not end in these as they are sometimes found at the end of the sentence
      mt = match[0];
      openingTag = match[1];
      href = match[2];
      www = match[7];
      closingA = match[8];
      i = match.index;
      end = i + match[0].length;
      alreadyA = (typeof openingTag != 'undefined' && openingTag.length > 0) || (typeof closingA != 'undefined' && closingA.length > 0);

      if (alreadyA) {
        addText(raw.substr(0, end));
        raw = raw.substr(end);
      } else {
        if (typeof www != 'undefined') {
          mt = 'http://' + href;
        }

        addText(raw.substr(0, i));
        addLink(mt, href);
        raw = raw.substring(end);
      }
    }

    addText(raw);
    return html.join('');

    function addText(text) {
      if (!text) {
        return;
      }
      html.push(text);
    }

    function addLink(url, text) {
      html.push('<a ');
      if (angular.isDefined(target)) {
        html.push('target="');
        html.push(target);
        html.push('" ');
      }
      html.push('href="');
      html.push(url);
      html.push('">');
      addText(text);
      html.push('</a>');
    }
  };
});
