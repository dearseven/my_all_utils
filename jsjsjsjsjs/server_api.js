/**
 * 这个是api接口的管理类，以前写js的时候都没有把api接口统一管理，导致难以维护。
 */

function ServerAPIs() {
	/*
	 * $.ajax({ type : '', url : url, data : data, success : success, dataType :
	 * dataType });
	 */
}

/**
 * 返回api?param，用于get,json转成kv对模式，不过没用上，项目里都是直接json
 * 
 * @param apiName
 *            除了ServerBaseURL的其他api地址
 * @param params
 *            json的对象和数据，都是string
 * @returns {string}
 */
// ServerAPIs.prototype.getAPI = function (apiName) {
ServerAPIs.prototype.gApiPathAndParam = function(apiName, params) {
	var appendParams = "?";
	for ( var p in params) {
		appendParams += (p + "=" + params[p] + "&");
	}
	appendParams += ("ts=" + new Date().getTime());
	return this.ServerBaseURL + apiName + appendParams;
}

/**
 * 单单返回param，json转成kv对模式，不过没用上，项目里都是直接json
 * 
 * @param params
 *            json的对象和数据，都是string
 */
ServerAPIs.prototype.pApiParam = function(params) {
	var appendParams = "";
	for ( var p in params) {
		appendParams += (p + "=" + params[p] + "&");
	}
	appendParams += ("ts=" + new Date().getTime());
	return appendParams;
}

/**
 * 返回api
 * 
 * @param apiName
 * 
 * @returns {string}
 */
// ServerAPIs.prototype.getAPI = function (apiName) {
ServerAPIs.prototype.apiPath = function(apiName) {
	return this.ServerBaseURL + apiName;
}

/**
 * 
 * @type {string}
 */
ServerAPIs.prototype.ServerBaseURL = "/attendance/";

/**
 * 这个api可以获取组织结构比如[公司-(部门1<员工1，员工2>, 部门2)]这样的结构
 * 
 * @type {string}
 */
ServerAPIs.prototype.GetOrgStruct = "getOrgStruct";



/**
 * 执行一个post请求，请求的参数是json
 */
ServerAPIs.prototype._post = function(_callback, _url, _data) {
	$.ajax({
		type : 'post',
		contentType : 'application/json;charset=utf-8',
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

/**
 * 执行一个post请求,传统的kv对方式传递参数,其实也可以传json，是因为springmvc要制定contentType，才有了上面的方法
 */
ServerAPIs.prototype._postTradition = function(_callback, _url, _data) {
	$.ajax({
		type : 'post',
		// contentType : 'application/json;charset=utf-8',
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

/**
 * 执行一个get请求
 */
ServerAPIs.prototype._getTradition = function(_callback, _url, _data) {
	$.ajax({
		type : 'get',
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		// contentType : 'application/json;charset=utf-8',
		url : _url + "?" + _data,
		success : _callback,
		dataType : 'text',
		error : function(error) {
			// alert("错误:" + error);
			// console.log(error);
		}
	});
}
