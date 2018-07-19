
/**
 * 这个其实还是那个listviewtest.html的滚动列表控件，但是把焦点效果从RecyclerView+N上移出了，而是让用户自选择在某个控件上
 * @param {Object} height 可显示的总高度
 * @param {Object} root 填充数据的容器，dom元素
 * @param {Object} scroller 滚动的dom元素
 * @param {Object} data 数据 ，数组
 * @param {Object} views 数组，是html元素字符串
 * @param {Object} initSize 初始化的时候初始数据个数,
 * 最好的
 * 次好的做法是让可以显示的大小刚不是刚好填充完整的控件，比如可以显示4个办的样子，那么初始化大小为5
 * @param {Object} viewTypeFunction 因为views是一个数组，所以可能有多个不同的view，这个方法返回用哪个view,以及高度
 * 所以这个方法返回的是数组
 * @param {Object} createViewFucntion 这个方法是回调的，其实就是把数据和view绑在一起
 */
function RecyclerView(height, root, scroller, data, views, initSize, viewTypeFunction, createViewFucntion) {
	this.viewTypeFunction = viewTypeFunction;
	this.scroller = scroller;
	this.createViewFucntion = createViewFucntion;
	this.root = root;
	this.data = data;
	this.views = views;
	this.initSize = initSize;
	//this.updateDis = updateDis;
	this.index = 0; //索引 
	this.drawIndex = 0; //往下绘制到的索引
	this.preDrawIndex = 0; //往上绘制的索引
	this.inited = false; //是否初始化过了
	this.measureArr = {}; //用于保留显示view的高度
	this.height = height;
	this.focusClaz = "";
	this.blurClaz = "";
}
RecyclerView.prototype.setFocusClass = function(n) {
	this.focusClaz = n;
}
RecyclerView.prototype.setBlurClass = function(n) {
	this.blurClaz = n;
}
RecyclerView.prototype.getSize = function() {
	return this.data.length;
}

//获得实例以后可以初始化
RecyclerView.prototype.init = function() {
	if(this.inited == false) {
		this.inited = true;
		var min = this.initSize < this.data.length ? this.initSize : this.data.length;
		for(i = 0; i < min; i++) {
			var v = this.getViewDown();
			this.root.innerHTML += v;
			this.drawIndex++;
		}

//		if(min > 0)
//			document.getElementById("RecyclerView0").className = this.focusClaz;
	}
}

///**
// * 用户当前上下移动，
// * @param {Object} upOrDown 上1 ,下2
// * @param {Object} index 当前是谁
// */
//RecyclerView.prototype.setCurrentIndex = function(upOrDown, index) {
//	this.index = index;
//	//console.log(this.height + ",用户移动:" + (upOrDown == 1 ? "上" : "下") + "，索引:" + index + ",滚动:" + c.scrollTop);
//	if(index < this.preDrawIndex + this.initSize / 2 - 1 && this.preDrawIndex != 0 && this.root.children.length < 2 * this.initSize) {
//		console.log(index + ",添加尾部数据，移除头部数据，" + (this.preDrawIndex - 1));
//		this.preDrawIndex--;
//		var v = this.getViewUp();
//		this.root.innerHTML = v + this.root.innerHTML;
//		this.drawIndex--;
//		this.root.removeChild(document.getElementById("RecyclerView" + this.drawIndex));
//	} else if(index > this.drawIndex - this.initSize / 2 && this.drawIndex != this.data.length && this.root.children.length < 2 * this.initSize) {
//		console.log(index + ",添加尾部数据，移除头部数据，" + (this.drawIndex - 1));
//		var v = this.getViewDown();
//		this.drawIndex++;
//		this.root.innerHTML += v;
//		this.root.removeChild(document.getElementById("RecyclerView" + this.preDrawIndex));
//		this.preDrawIndex++;
//	}
//
//	if(upOrDown == 1) {
//		document.getElementById("RecyclerView" + (index + 1)).className = this.blurClaz;
//	} else {
//		document.getElementById("RecyclerView" + (index - 1)).className = this.blurClaz;
//	}
//	document.getElementById("RecyclerView" + (index)).className = this.focusClaz;
//
//	if(index == this.data.length - 1) {
//		this.scroller.scrollTop = (index + 0) * this.measureArr[index];
//	} else if(index == 0) {
//		this.scroller.scrollTop = 0;
//	}
//}

/**
 * 用户当前上下移动，
 * @param {Object} upOrDown 上1 ,下2
 * @param {Object} index 当前是谁
 */
RecyclerView.prototype.setCurrentIndexWithFocusViewPartId = function(upOrDown, index,partId) {
	this.index = index;
	//console.log(this.height + ",用户移动:" + (upOrDown == 1 ? "上" : "下") + "，索引:" + index + ",滚动:" + c.scrollTop);
	if(index < this.preDrawIndex + this.initSize / 2 - 1 && this.preDrawIndex != 0 && this.root.children.length < 2 * this.initSize) {
		console.log(index + ",添加尾部数据，移除头部数据，" + (this.preDrawIndex - 1));
		this.preDrawIndex--;
		var v = this.getViewUp();
		this.root.innerHTML = v + this.root.innerHTML;
		this.drawIndex--;
		this.root.removeChild(document.getElementById("RecyclerView" + this.drawIndex));
	} else if(index > this.drawIndex - this.initSize / 2 && this.drawIndex != this.data.length && this.root.children.length < 2 * this.initSize) {
		console.log(index + ",添加尾部数据，移除头部数据，" + (this.drawIndex - 1));
		var v = this.getViewDown();
		this.drawIndex++;
		this.root.innerHTML += v;
		this.root.removeChild(document.getElementById("RecyclerView" + this.preDrawIndex));
		this.preDrawIndex++;
	}

	if(upOrDown == 1) {
		document.getElementById(partId + (index + 1)).className = this.blurClaz;
	} else {
		document.getElementById(partId + (index - 1)).className = this.blurClaz;
	}
	document.getElementById(partId + (index)).className = this.focusClaz;

	if(index == this.data.length - 1) {
		this.scroller.scrollTop = (index + 0) * this.measureArr[index];
	} else if(index == 0) {
		this.scroller.scrollTop = 0;
	}
}

/**
 * 不要在外部调用这个方法，往上方填充view
 * @param {Object} viewTypeFunction
 * @param {Object} createViewFucntion
 */
RecyclerView.prototype.getViewUp = function() {
	var v = this.views[0];
	try {
		//如果用户写了方法
		if(this.viewTypeFunction) {
			var arr = this.viewTypeFunction(this.preDrawIndex);
			v = this.views[arr[0]];
			this.measureArr[this.preDrawIndex + ""] = arr[1];
		}
	} catch(e) {
		console.log(e)
	}
	try {
		//如果用户写了方法
		if(this.createViewFucntion) {
			v = this.createViewFucntion(v, this.data[this.preDrawIndex], this.preDrawIndex);
			//console.log("2 " + v)
		}
	} catch(e) {
		console.log(e)
	}
	v = "<div id='RecyclerView" + this.preDrawIndex + "'>" + v + " </div> "
	return v;
}

/**
 * 不要在外部调用这个方法，往下方填充view
 * @param {Object} viewTypeFunction
 * @param {Object} createViewFucntion
 */
RecyclerView.prototype.getViewDown = function() {
	var v = this.views[0];
	try {
		//如果用户写了方法
		if(this.viewTypeFunction) {
			var arr = this.viewTypeFunction(this.drawIndex);
			v = this.views[arr[0]];
			this.measureArr[this.drawIndex + ""] = arr[1];
		}
	} catch(e) {
		console.log(e)
	}
	try {
		//如果用户写了方法
		if(this.createViewFucntion) {
			v = this.createViewFucntion(v, this.data[this.drawIndex], this.drawIndex);
			console.log("2 " + v)
		}
	} catch(e) {
		console.log(e)
	}
	v = "<div id='RecyclerView" + this.drawIndex + "'>" + v + " </div> "
	return v;
}