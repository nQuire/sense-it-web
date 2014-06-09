

var SiwColorGenerator = {
  
  k: 3,
  step: 120,
  
  getColor: function(n, s, v) {
    s = typeof s === 'undefined' ? 1 : s;
    v = typeof v === 'undefined' ? 1 : v;
    return this.getHtml(this.getRGB(this.getAngle(n), s, v));
  },
  
  getHtml: function(rgb) {
    var output = '#';
    for (var i = 0; i < 3; i++) {
      var pstr = Math.floor(255*rgb[i]).toString(16);
      while(pstr.length < 2) {
        pstr = '0' + pstr;
      }
      output += pstr;
    }
    return output;
  },
  getRGB: function(h, s, v) {
    var c = v * s;
    var x = c * (1 - Math.abs((h/60) % 2 - 1));
    var m = v - c;
    
    var r, g, b;
    if (h < 60) {
      r=c, g=x, b=0;
    } else if (h < 120) {
      r=x, g=c, b=0;
    } else if (h < 180) {
      r=0, g=c, b=x;
    } else if (h < 240) {
      r=0, g=x, b=c;
    } else if (h < 300) {
      r=x, g=0, b=c;
    } else {
      r=c, g=0, b=x;
    }
    
    return [r+m, g+m, b+m];
  },
  getAngle: function(n) {
    var k = 3;
    
    var n_k = Math.floor(n/k);
    
    var turn, n_in_turn, pow2;
    
    if (n_k > 0) {
      var l2 = Math.floor(Math.log(n_k) / Math.LN2);
      turn = l2 + 1;
      pow2 = Math.pow(2, l2);
      n_in_turn = n - 3 * pow2;      
    } else {
      turn = 0;
      n_in_turn = n;
      pow2 = 0;
    }
    
    var angle_step, angle_0;
    
    if (turn === 0) {
      angle_step = 120;
      angle_0 = 0;
    } else {
      angle_0 = 60 / pow2;
      angle_step = angle_0 * 2;
    }
    
    var angle = angle_0 + n_in_turn * angle_step;
    return angle;
  }
  
  
};

