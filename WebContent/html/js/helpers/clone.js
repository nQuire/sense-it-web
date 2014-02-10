

function siwClone(obj) {
  if (obj === null || typeof (obj) !== 'object')
    return obj;

  var temp = obj.constructor(); // changed

  for (var key in obj) {
    temp[key] = tClone(obj[key]);
  }


  return temp;
}