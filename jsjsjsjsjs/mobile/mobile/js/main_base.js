(function() {
	function Helper() {

	}

	Helper.prototype.sayHello = function() {
		alert("Hello world! Hellp $helper!")
	}

	Helper.prototype._post = function(_callback, _url, _data) {
		$.ajax({
			type : 'post',
			contentType:'application/x-www-form-urlencoded;charset=utf-8',
			//contentType : 'application/json;charset=utf-8',
			url : _url,
			data : _data,
			success : _callback,
			dataType : 'text',
			error : function(error) {
				// alert("错误:" + error);
				// console.log(error);
			}
		});
	}

	window.$helper = new Helper();
})();

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/g, "");
}
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/g, "");
}

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
