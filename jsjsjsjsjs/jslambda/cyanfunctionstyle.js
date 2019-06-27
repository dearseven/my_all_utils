(function() {
	function CyanLambda() {

	}

	CyanLambda.prototype.els = [];
	CyanLambda.prototype.progressValue = undefined;

	window.$_cyanLambda = CyanLambda;

	/**过滤
	 * @param {Object} func
	 */
	CyanLambda.prototype.filter = function(func) {
		console.log(this.els)
		var arr = [];
		for (var i = 0; i < this.els.length; i++) {
			if (func(this.els[i])) {
				arr.push(this.els[i]);
			}
		}
		this.els = arr;
		return this;
	}
	/**
	 * 转换
	 * @param {Object} func
	 */
	CyanLambda.prototype.map = function(func) {
		var arr = [];
		for (var i = 0; i < this.els.length; i++) {
			arr.push(func(this.els[i]));
		}
		this.els = arr;
		return this;
	}

	/**
	 * 要求执行的func返回的是Array，然后再迭代这个数组添加的Arr最后替换els
	 * @param {Object} func
	 * @param {Object} otherArr
	 */
	CyanLambda.prototype.flatMap = function(func) {
		var arr = [];
		for (var i = 0; i < this.els.length; i++) {
			var tempArr = (func(this.els[i]));
			if (tempArr instanceof Array) {
				for (var j = 0; j < tempArr.length; j++) {
					arr.push(tempArr[j]);
				}
			}
		}
		this.els = arr;
		return this;
	}

	/**
	 * 求和
	 */
	CyanLambda.prototype.sum = function() {
		this.progressValue = 0.0;
		for (var i = 0; i < this.els.length; i++) {
			this.progressValue += (parseFloat(this.els[i]))
		}
		return this;
	}

	/**
	 * 添加元素
	 * @param {Object} el
	 */
	CyanLambda.prototype.append = function(el) {
		this.els.push(el);
		return this;
	}

	/**
	 * 添加在最前面
	 * @param {Object} el
	 */
	CyanLambda.prototype.addFirst = function(el) {
		var arr = [el];
		for (var i = 0; i < this.els.length; i++) {
			arr.push(this.els[i]);
		}
		this.els = arr;
		return this;
	}

	/**
	 * 排序
	 * @param {Object} el
	 */
	CyanLambda.prototype.sort = function(func) {
		var tempArr = [];
		var arr = [];
		for (var i = 0; i < this.els.length; i++) {
			tempArr.push({
				"index": i,
				"factor": func(this.els[i])
			})
		}
		//
		tempArr.sort(function(a, b) {
			if (a.factor < b.factor) {
				return -1;
			}
			if (a.factor > b.factor) {
				return 1;
			}
			return 0;
		})
		for (var i = 0; i < tempArr.length; i++) {
			arr.push(this.els[tempArr[i].index]);
		}
		this.els = arr;
		return this;
	}

	/**
	 * 排序
	 * @param {Object} el
	 */
	CyanLambda.prototype.sortDesc = function(func) {
		var tempArr = [];
		var arr = [];
		for (var i = 0; i < this.els.length; i++) {
			tempArr.push({
				"index": i,
				"factor": func(this.els[i])
			})
		}
		//
		tempArr.sort(function(a, b) {
			if (a.factor < b.factor) {
				return 1;
			}
			if (a.factor > b.factor) {
				return -1;
			}
			return 0;
		})
		for (var i = 0; i < tempArr.length; i++) {
			arr.push(this.els[tempArr[i].index]);
		}
		this.els = arr;
		return this;
	}


	/**
	 * 转换
	 * @param {Object} func
	 */
	CyanLambda.prototype.groupBy = function(func) {
		var arr = [];
		for (var i = 0; i < this.els.length; i++) {
			var flag = func(this.els[i]);
			if (!hasKey(arr, flag)) {
				arr.push({
					"key": flag,
					"arr": [this.els[i]]
				})
			} else {
				for (var j = 0; j < arr.length; j++) {
					if (arr[j].key == flag) {
						arr[j].arr.push(this.els[i]);
					}
				}
			}
		}

		function hasKey(_arr, _k) {
			for (var i = 0; i < _arr.length; i++) {
				if (_arr[i].key == _k) {
					return true;
				}
			}
			return false;
		}
		this.els = arr;
		return this;
	}

	/**
	 * forEach
	 * @param {Object} func
	 */
	CyanLambda.prototype.forEach = function(func) {
		var arr = [];
		for (var i = 0; i < this.els.length; i++) {
			func(this.els[i]);
		}
		this.els = arr;
		return this;
	}


	CyanLambda.prototype.eachPrint = function(str) {
		if (str == undefined)
			console.log("----------eachPrint-----------")
		else
			console.log("----------eachPrint--" + str + "-----------")
		for (var i = 0; i < this.els.length; i++) {
			console.log(this.els[i])
		}
		return this;
	}

})()

/**
 * 不是我想这么写，而是我发现我用prototype会导致有时候和其他js配合的时候有其他问题，
 * @param arr
 * @returns
 */
function __toCyanStream(arr){
	var _cl = new $_cyanLambda();
	_cl.els = arr;
	return _cl;
}
//Array.prototype.stream = function() {
//	var _cl = new $_cyanLambda();
//	_cl.els = this;
//	return _cl;
//};
