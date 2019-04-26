function foreachObj(obj) {
	// 用来保存所有的属性名称和值
	// 开始遍历
	for ( var p in obj) {
		// 方法
		if (typeof (obj[p]) == "function") {
			console.log(p + ":" + obj[p] + " ,is function");
		} else if (obj[p] == '[object Object]') {
			console.log(p + ":" + obj[p] + " ,is [object Object]");
		} else {
			console.log(p + ":" + obj[p] + " ,is value");
		}
	}
}