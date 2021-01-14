var _requestParam = {};
(function() {
	var _href = window.location.href;
	var i = _href.indexOf("?")
	if (i > -1) {
		var queryStr = _href.substr(i + 1);
		var queryArr = queryStr.split("&");
		for ( var i in queryArr) {
			var kv = queryArr[i].split("=");
			_requestParam[kv[0]] = kv[1];
		}
	}
})();