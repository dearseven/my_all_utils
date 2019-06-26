//设置自定义属性：el.setAttribute("_groupId",groupId);
//读取自定义属性：el.attributes["_groupId"].value;
(function () {
	var DataBindding = {

	}
	DataBindding.show = function (a) {
		alert(a)
	}
	DataBindding.elemts = {

	}

	DataBindding.arrs = {

	}

	// 变量改变的观察者，这个可以设置以后，会回调
	DataBindding.varWatcher = undefined

	/**
	 * @param {Object}
	 *            watcher function(varObj)
	 */
	DataBindding.setWatcher = function (watcher) {
		DataBindding.varWatcher = watcher;
	}

	DataBindding.bindArr = function (_id) {
		DataBindding.arrs[("#" + _id)] = {
			elemt: [],
			id: _id
		}
		DataBindding.arrs[("#" + _id)]["setArr"] = function (arr) {
			//console.log("this.id="+this.id)
			this.elemt = arr;
			DataBindding.__arrOk(this.id);
		}
		return DataBindding.arrs[("#" + _id)];
	}

	DataBindding.bindVar = function (_id, _pro, v) {
		var obj = {
			id: _id,
			tagName: "_DBIN_VAR",
			pro: _pro
		}
		obj[_pro] = v;
		DataBindding.elemts[("#" + _id)] = {
			id: _id,
			elemt: obj,
			pro: _pro,
			value: v,
			type: "var"// 普通变量
		}
		DataBindding.elemts[("#" + _id)].setValue = function (v) {
			DataBindding.elemts[("#" + _id)].value = v;
			DataBindding.setValue(this, v);
		}
		DataBindding.elemts[("#" + _id)][_pro] = v;
		DataBindding.setValue(DataBindding.elemts[("#" + _id)], v)
		return DataBindding.elemts[("#" + _id)];
	}

	/**
	 * 绑定一组radiobox
	 * 
	 * @param {Object}
	 *            groupId，就是每个radio的name啊，html是要求一组radio有同样的name
	 * @param {Object}
	 *            ids
	 * @param {Object}
	 *            checkstatus
	 * @param {Object}
	 *            checkedClass
	 * @param {Object}
	 *            uncheckedClass
	 */
	DataBindding.bindRadioBox = function (groupId, ids, checkstatus,
		checkedClass, uncheckedClass) {
		DataBindding.elemts[("#" + groupId)] = {
			id: groupId,
			value: "",
			type: "radiobox"// 单选
		}
		DataBindding.elemts[("#" + groupId)].elemts = [];
		//
		for (var i = 0; i < ids.length; i++) {
			var el = document.getElementById(ids[i]);
			el.setAttribute("_groupId", groupId);
			el.setAttribute("_index", i);
			DataBindding.elemts[("#" + groupId)].elemts.push(el)
			el.checked = checkstatus[i]
			if (el.checked) { // true|false
				DataBindding.elemts[("#" + groupId)].value += el.value + ",";
				el.className = el.className.replace(uncheckedClass, "");
				el.className = el.className.replace(checkedClass, "");
				el.className += " " + checkedClass;
			} else {
				el.className = el.className.replace(checkedClass, "");
				el.className = el.className.replace(uncheckedClass, "");
				el.className += " " + uncheckedClass;
			}
			el.onclick = (function () {
				var gid = this.attributes["_groupId"].value;
				var idx = this.attributes["_index"].value;
				console.log(gid + " " + idx)
				DataBindding.elemts[("#" + groupId)].value = "";
				for (var i = 0; i < DataBindding.elemts[("#" + gid)].elemts.length; i++) {
					if (DataBindding.elemts[("#" + gid)].elemts[i].checked) { // true|false
						DataBindding.elemts[("#" + groupId)].value += DataBindding.elemts[("#" + gid)].elemts[i].value
							+ ",";
						DataBindding.elemts[("#" + gid)].elemts[i].className = DataBindding.elemts[("#" + gid)].elemts[i].className
							.replace(uncheckedClass, "");
						DataBindding.elemts[("#" + gid)].elemts[i].className = DataBindding.elemts[("#" + gid)].elemts[i].className
							.replace(checkedClass, "");
						DataBindding.elemts[("#" + gid)].elemts[i].className += " "
							+ checkedClass;
					} else {
						DataBindding.elemts[("#" + gid)].elemts[i].className = DataBindding.elemts[("#" + gid)].elemts[i].className
							.replace(uncheckedClass, "");
						DataBindding.elemts[("#" + gid)].elemts[i].className = DataBindding.elemts[("#" + gid)].elemts[i].className
							.replace(checkedClass, "");
						DataBindding.elemts[("#" + gid)].elemts[i].className += " "
							+ uncheckedClass;
					}
				}
				DataBindding.elemts[("#" + groupId)].value = DataBindding.elemts[("#" + groupId)].value
					.substring(
						0,
						DataBindding.elemts[("#" + groupId)].value.length - 1)
				console.log("2 radio value:"
					+ DataBindding.elemts[("#" + groupId)].value)
				__onValueChange(groupId)
			})
			// attributes["_dbin_for_items"].value;
		}
		DataBindding.elemts[("#" + groupId)].value = DataBindding.elemts[("#" + groupId)].value
			.substring(0,
				DataBindding.elemts[("#" + groupId)].value.length - 1)
		console.log("1 radio value:"
			+ DataBindding.elemts[("#" + groupId)].value)
		__onValueChange(groupId)
		/**
		 * 通过代码设置checkbox的选中状态
		 * 
		 * @param {Object}
		 *            checkedStates [true,false,true],这一句有几个就设置几个
		 */
		DataBindding.elemts[("#" + groupId)].setCheckStatus = function (
			checkedStates) {
			DataBindding.elemts[("#" + groupId)].value = "";
			for (var i = 0; i < DataBindding.elemts[("#" + groupId)].elemts.length; i++) {
				DataBindding.elemts[("#" + groupId)].elemts[i].checked = checkedStates[i];
				if (checkedStates[i]) { // true|false
					DataBindding.elemts[("#" + groupId)].value += DataBindding.elemts[("#" + groupId)].elemts[i].value
						+ ",";
					DataBindding.elemts[("#" + groupId)].elemts[i].className = DataBindding.elemts[("#" + groupId)].elemts[i].className
						.replace(uncheckedClass, "");
					DataBindding.elemts[("#" + groupId)].elemts[i].className = DataBindding.elemts[("#" + groupId)].elemts[i].className
						.replace(checkedClass, "");
					DataBindding.elemts[("#" + groupId)].elemts[i].className += " "
						+ checkedClass;
				} else {
					DataBindding.elemts[("#" + groupId)].elemts[i].className = DataBindding.elemts[("#" + groupId)].elemts[i].className
						.replace(uncheckedClass, "");
					DataBindding.elemts[("#" + groupId)].elemts[i].className = DataBindding.elemts[("#" + groupId)].elemts[i].className
						.replace(checkedClass, "");
					DataBindding.elemts[("#" + groupId)].elemts[i].className += " "
						+ uncheckedClass;
				}
			}
			DataBindding.elemts[("#" + groupId)].value = DataBindding.elemts[("#" + groupId)].value
				.substring(
					0,
					DataBindding.elemts[("#" + groupId)].value.length - 1)
			console.log("3 radio value:"
				+ DataBindding.elemts[("#" + groupId)].value)
			__onValueChange(groupId)
		}
		return DataBindding.elemts[("#" + groupId)];
	}

	/**
	 * 绑定一组checkbox
	 * 
	 * @param {Object}
	 *            groupId 这一组的id
	 * @param {Object}
	 *            ids ["id1","id2"]
	 * @param {Object}
	 *            checkstatus [true,false] 选中状态
	 * @param {Object}
	 *            checkedClass "input_checked"
	 * @param {Object}
	 *            uncheckedClass ""input_unchecked""
	 */
	DataBindding.bindCheckBox = function (groupId, ids, checkstatus,
		checkedClass, uncheckedClass) {
		DataBindding.elemts[("#" + groupId)] = {
			id: groupId,
			value: "",
			type: "checkbox"// 多选
		}
		DataBindding.elemts[("#" + groupId)].elemts = [];
		//
		for (var i = 0; i < ids.length; i++) {
			var el = document.getElementById(ids[i]);
			el.setAttribute("_groupId", groupId);
			el.setAttribute("_index", i);
			DataBindding.elemts[("#" + groupId)].elemts.push(el)
			el.checked = checkstatus[i]
			if (el.checked) { // true|false
				DataBindding.elemts[("#" + groupId)].value += el.value + ",";
				el.className = el.className.replace(uncheckedClass, "");
				el.className = el.className.replace(checkedClass, "");
				el.className += " " + checkedClass;
			} else {
				el.className = el.className.replace(checkedClass, "");
				el.className = el.className.replace(uncheckedClass, "");
				el.className += " " + uncheckedClass;
			}
			el.onclick = (function () {
				var gid = this.attributes["_groupId"].value;
				var idx = this.attributes["_index"].value;
				console.log(gid + " " + idx)
				DataBindding.elemts[("#" + groupId)].value = "";
				for (var i = 0; i < DataBindding.elemts[("#" + gid)].elemts.length; i++) {
					if (DataBindding.elemts[("#" + gid)].elemts[i].checked) { // true|false
						DataBindding.elemts[("#" + groupId)].value += DataBindding.elemts[("#" + gid)].elemts[i].value
							+ ",";
						DataBindding.elemts[("#" + gid)].elemts[i].className = DataBindding.elemts[("#" + gid)].elemts[i].className
							.replace(uncheckedClass, "");
						DataBindding.elemts[("#" + gid)].elemts[i].className = DataBindding.elemts[("#" + gid)].elemts[i].className
							.replace(checkedClass, "");
						DataBindding.elemts[("#" + gid)].elemts[i].className += " "
							+ checkedClass;
					} else {
						DataBindding.elemts[("#" + gid)].elemts[i].className = DataBindding.elemts[("#" + gid)].elemts[i].className
							.replace(uncheckedClass, "");
						DataBindding.elemts[("#" + gid)].elemts[i].className = DataBindding.elemts[("#" + gid)].elemts[i].className
							.replace(checkedClass, "");
						DataBindding.elemts[("#" + gid)].elemts[i].className += " "
							+ uncheckedClass;
					}
				}
				DataBindding.elemts[("#" + groupId)].value = DataBindding.elemts[("#" + groupId)].value
					.substring(
						0,
						DataBindding.elemts[("#" + groupId)].value.length - 1)
				console.log("2 group value:"
					+ DataBindding.elemts[("#" + groupId)].value)
				__onValueChange(groupId)
			})
			// attributes["_dbin_for_items"].value;
		}
		DataBindding.elemts[("#" + groupId)].value = DataBindding.elemts[("#" + groupId)].value
			.substring(0,
				DataBindding.elemts[("#" + groupId)].value.length - 1)
		console.log("1 group value:"
			+ DataBindding.elemts[("#" + groupId)].value)
		__onValueChange(groupId)

		/**
		 * 通过代码设置checkbox的选中状态
		 * 
		 * @param {Object}
		 *            checkedStates [true,false,true],这一句有几个就设置几个
		 */
		DataBindding.elemts[("#" + groupId)].setCheckStatus = function (
			checkedStates) {
			DataBindding.elemts[("#" + groupId)].value = "";
			for (var i = 0; i < DataBindding.elemts[("#" + groupId)].elemts.length; i++) {
				DataBindding.elemts[("#" + groupId)].elemts[i].checked = checkedStates[i];
				if (checkedStates[i]) { // true|false
					DataBindding.elemts[("#" + groupId)].value += DataBindding.elemts[("#" + groupId)].elemts[i].value
						+ ",";
					DataBindding.elemts[("#" + groupId)].elemts[i].className = DataBindding.elemts[("#" + groupId)].elemts[i].className
						.replace(uncheckedClass, "");
					DataBindding.elemts[("#" + groupId)].elemts[i].className = DataBindding.elemts[("#" + groupId)].elemts[i].className
						.replace(checkedClass, "");
					DataBindding.elemts[("#" + groupId)].elemts[i].className += " "
						+ checkedClass;
				} else {
					DataBindding.elemts[("#" + groupId)].elemts[i].className = DataBindding.elemts[("#" + groupId)].elemts[i].className
						.replace(uncheckedClass, "");
					DataBindding.elemts[("#" + groupId)].elemts[i].className = DataBindding.elemts[("#" + groupId)].elemts[i].className
						.replace(checkedClass, "");
					DataBindding.elemts[("#" + groupId)].elemts[i].className += " "
						+ uncheckedClass;
				}
			}
			DataBindding.elemts[("#" + groupId)].value = DataBindding.elemts[("#" + groupId)].value
				.substring(
					0,
					DataBindding.elemts[("#" + groupId)].value.length - 1)
			console.log("3 group value:"
				+ DataBindding.elemts[("#" + groupId)].value)
			__onValueChange(groupId)
		}
		return DataBindding.elemts[("#" + groupId)];
	}

	DataBindding.bindElemt = function (id, initValue) {
		var el = document.getElementById(id);
		DataBindding.elemts[("#" + id)] = {
			"id": id,
			elemt: el,
		}
		var type = el.tagName;
		if (type == "input" || type == "INPUT") {
			DataBindding.elemts[("#" + id)].type = "input";
			DataBindding.elemts[("#" + id)].value = initValue
			DataBindding.elemts[("#" + id)].elemt.value = initValue;
			// Object.defineProperties(DataBindding.elemts[("#" + id)], {
			// value: {
			// configurable: true,
			// get: function () {
			// //console.log('databingding_get: ', a);
			// return this.value;
			// },
			// set: function (newvalue) {
			// //console.log("~~")
			// //console.log(this)
			// //a = newvalue;
			// // DataBindding.elemts[("#" + id)].elemt.value = a;
			// //value = newvalue;
			// //DataBindding.elemts[("#" + this.id)].value=newvalue;
			// //console.log('databingding_set: ', a);
			// //console.log(this.elemt.id)
			// this.elmt.value=newvalue
			// __onValueChange(this.elmt.id);
			// }
			// }
			// })
			DataBindding.elemts[("#" + id)].elemt.oninput = (function () {
				return function (e) {
					DataBindding.elemts[("#" + this.id)].value = DataBindding.elemts[("#" + this.id)].elemt.value
					console.log(("#" + this.id) + " on input： "
						+ DataBindding.elemts[("#" + this.id)].value);
					// console.log(this.id)
					__onValueChange(this.id);
					// console.log("11???")
					// var d1 = DataBindding.elemts["#inputText"].value
					// var d2 = DataBindding.elemts["#inputText2"].value
					// console.log("d1=" + d1 + ",d2=" + d2);
				}
			})(DataBindding.elemts[("#" + id)].elemt)
			DataBindding.elemts[("#" + id)].setValue = function (v) {
				// console.log("22???")
				DataBindding.setValue(this, v)
			}
			DataBindding.elemts[("#" + id)].elemt.onchange = function () {
				// __onValueChange(this.id);
				// console.log(this.id+" #########??:"+this.value)
			}
			__onValueChange(id);
		} else if (type == "select" || type == "SELECT") {
			DataBindding.elemts[("#" + id)].type = "select";
			// 当是select的时候，有两种初始化方法initValue的值可以是索引或者值
			// {"index":2}，{"value":"opel"}
			if (initValue.index != undefined) {
				DataBindding.elemts[("#" + id)].index = parseInt(initValue.index);
				// console.log(DataBindding.elemts[("#" + id)].elemt.options);
				DataBindding.elemts[("#" + id)].elemt.selectedIndex = DataBindding.elemts[("#" + id)].index;
				DataBindding.elemts[("#" + id)].value = DataBindding.elemts[("#" + id)].elemt.options[DataBindding.elemts[("#" + id)].index].value;
				DataBindding.elemts[("#" + id)].optValue = DataBindding.elemts[("#" + id)].elemt.options[DataBindding.elemts[("#" + id)].index].innerHTML;
				// console.log("####:"+ DataBindding.elemts[("#" + id)].value)
			} else if (initValue.value != undefined) {
				DataBindding.elemts[("#" + id)].value = (initValue.value);
				for (var i = 0; i < DataBindding.elemts[("#" + id)].elemt.options.length; i++) {
					if (DataBindding.elemts[("#" + id)].elemt.options[i].value == DataBindding.elemts[("#" + id)].value) {
						DataBindding.elemts[("#" + id)].elemt.selectedIndex = i;
						DataBindding.elemts[("#" + id)].index = i
						DataBindding.elemts[("#" + id)].optValue = DataBindding.elemts[("#" + id)].elemt.options[i].innerHTML;
						break;
					}
				}
			}
			DataBindding.elemts[("#" + id)].elemt.onchange = (function () {
				return function (e) {
					DataBindding.elemts[("#" + this.id)].optValue = e.target.options[e.target.selectedIndex].innerHTML;
					DataBindding.elemts[("#" + this.id)].index = e.target.selectedIndex;
					DataBindding.elemts[("#" + this.id)].value = e.target.options[e.target.selectedIndex].value;
					// console.log(DataBindding.elemts[("#" + this.id)]);
					// console.log(this.id)
					__onValueChange(this.id);
				}
			})()

			DataBindding.elemts[("#" + id)].setValue = function (v) {
				DataBindding.setValue(this, v)
			}
			__onValueChange(id);
		}
		// console.log(DataBindding.elemts[("#" + id)]);
		return DataBindding.elemts[("#" + id)];
	}

	//这个方法现在什么也不干了
	DataBindding.arrOk = function () {

	}
	DataBindding.__arrOk = function (_dbin_for_items_id) {
		var allFors = __getElementByClassName("_dbin_for");
		for (var i = 0; i < allFors.length; i++) {
			allFors = __getElementByClassName("_dbin_for")
			// 这里时获取自定义属性,用来判断
			var objId = allFors[i].attributes["_dbin_for_items"].value;
			if (objId != _dbin_for_items_id) {
				continue;//只更新对应的id的数据
			}
			console.log("要更新的objId=" + objId);
			console.log("要更新的_dbin_for_items_id=" + _dbin_for_items_id);
			// 拿到对应的html
			// if (objId == obj.id) {
			var rootHTML = allFors[i];
			// console.log(objId)
			// console.log(DataBindding.arrs["#"+objId])
			if (DataBindding.arrs["#" + objId] == undefined) {
				continue;
			}
			console.log("要更新的DataBindding.arrs[ #" + objId + "]=" + DataBindding.arrs["#" + objId])
			if (DataBindding.arrs["#" + objId].rawHtml == undefined)
				DataBindding.arrs["#" + objId].rawHtml = rootHTML.innerHTML;
			// console.log(rootHTML)
			rootHTML.innerHTML = "";
			console.log("DataBindding.arrs[ #" + objId + "].elemt.length="+DataBindding.arrs["#" + objId].elemt.length )
			if (DataBindding.arrs["#" + objId] != undefined
				&& DataBindding.arrs["#" + objId].elemt.length > 0) {
				console.log('2 objId：' + objId)

				for (var j = 0; j < DataBindding.arrs["#" + objId].elemt.length; j++) {
					var rawInnerHtml = DataBindding.arrs["#" + objId].rawHtml;
					var item = DataBindding.arrs["#" + objId].elemt[j];
					// var count = 0;
					// console.log("j=" + j)
					// console.log(rootHTML)
					var arrs = rawInnerHtml.split("{_dbin_for{$");
					for (var k = 0; k < arrs.length; k++) {
						var prop = arrs[k].substring(0, arrs[k]
							.lastIndexOf("}}"))
						if (prop != "") {
							prop = (prop.substring(prop.indexOf(".") + 1))
							// console.log("item." + prop)
							// rawInnerHtml =
							// rawInnerHtml.replace("{_dbin_for{$" + objId + "."
							// + prop + "}}", eval("(" + "item." + prop + ")"));
							var props = prop.split(".");
							var v = item;
							for (var p in props) {
								v = v[props[p]]
								// console.log("k="+props[p])
								// console.log("v="+v)
							}
							rawInnerHtml = rawInnerHtml.replace("{_dbin_for{$"
								+ objId + "." + prop + "}}", v);
						}
					}
					// console.log(arrs);
					// for (var p in item) {
					// rawInnerHtml = rawInnerHtml.replace("{_dbin_for{$" +
					// objId + "." + p + "}}",
					// eval("item."+ p))
					// count++;
					// }
					rootHTML.innerHTML += rawInnerHtml;
				}
			} else {

			}
			// console.log(rootHTML.innerHTML)
			// }
		}
	}

	DataBindding.setValue = function (obj, value) {
		if (obj.elemt.tagName == "INPUT" || obj.elemt.tagName == "input") {
			obj.elemt.value = value;
			DataBindding.elemts[("#" + obj.elemt.id)].value = value;
			__onValueChange(obj.elemt.id);
		} else if (obj.elemt.tagName == "select"
			|| obj.elemt.tagName == "SELECT") {
			// //当是select的时候，有两种初始化方法value的值可以是索引或者值
			if (value.index != undefined) {
				DataBindding.elemts[("#" + obj.elemt.id)].index = parseInt(value.index);
				// console.log(DataBindding.elemts[("#" + id)].elemt.options);
				DataBindding.elemts[("#" + obj.elemt.id)].elemt.selectedIndex = DataBindding.elemts[("#" + obj.elemt.id)].index;
				DataBindding.elemts[("#" + obj.elemt.id)].value = DataBindding.elemts[("#" + obj.elemt.id)].elemt.options[DataBindding.elemts[("#" + obj.elemt.id)].index].value;
				DataBindding.elemts[("#" + obj.elemt.id)].optValue = DataBindding.elemts[("#" + obj.elemt.id)].elemt.options[DataBindding.elemts[("#" + obj.elemt.id)].index].innerHTML;
				// console.log("####:"+ DataBindding.elemts[("#" + id)].value)
			} else if (value.value != undefined) {
				DataBindding.elemts[("#" + obj.elemt.id)].value = (value.value);
				for (var i = 0; i < DataBindding.elemts[("#" + obj.elemt.id)].elemt.options.length; i++) {
					if (DataBindding.elemts[("#" + obj.elemt.id)].elemt.options[i].value == DataBindding.elemts[("#" + obj.elemt.id)].value) {
						DataBindding.elemts[("#" + obj.elemt.id)].elemt.selectedIndex = i;
						DataBindding.elemts[("#" + obj.elemt.id)].index = i
						DataBindding.elemts[("#" + obj.elemt.id)].optValue = DataBindding.elemts[("#" + obj.elemt.id)].elemt.options[i].innerHTML;
						break;
					}
				}
			}
			__onValueChange(obj.elemt.id);
		} else if (obj.elemt.tagName == "_DBIN_VAR") { // 普通变量
			var pro = DataBindding.elemts[("#" + obj.elemt.id)].pro;
			DataBindding.elemts[("#" + obj.elemt.id)][pro] = value
			DataBindding.elemts[("#" + obj.elemt.id)].elemt.pro = value
			console.log(obj)
			__onValueChange(obj.elemt.id);
		}
	}

	DataBindding.getElemt = function (id) {
		return DataBindding.elemts[("#" + id)];
	}

	window.$_bdin = DataBindding;
	if (document.all) {
		window.attachEvent('onload', __domScan); // IE....不会用这个浏览器的
	} else {
		window.addEventListener('load', __domScan, false);
	}

	/**
	 * 如果通过js创建了html并且使用了databinding， 要调用refreshElemts来重新扫描到databinding中
	 */
	DataBindding.refreshElemts = function () {
		__domScan()
	}

	function __domScan() {
		// _dbin是有模板的文件
		var elemts = __getElementByClassName("_dbin");
		// 每次執行__domScan都是新的
		DataBindding.rawDoms = elemts;
		DataBindding.replaceElements = {};
		for (var i = 0; i < elemts.length; i++) {
			var name = " _dbin_id_" + i
			if (elemts[i].className.indexOf(name) < 0)
				elemts[i].className = elemts[i].className + name//" _dbin_id_" + i
			for (var j = 0; j < elemts[i].childNodes.length; j++) {
				if (elemts[i].childNodes[j].nodeName == "#text") {
					var clazArr = elemts[i].className.split(" ");
					var className;
					for (var k = 0; k < clazArr.length; k++) {
						if (clazArr[k].indexOf("_dbin_id") > -1) {
							className = (clazArr[k])
						}
					}
					// console.log(className+"|"+j)
					// console.log(elemts[i].childNodes[j].nodeValue)
					DataBindding.replaceElements[className + "|" + j] = elemts[i].childNodes[j].nodeValue;
				}
			}
		}
		__onValueChange("");
	}

	// 扫描所有同class的tag
	function __getElementByClassName(clazname) {
		var objArray = new Array(); // 定义返回对象数组
		var tags = document.getElementsByTagName("*"); // 获取页面所有元素
		var index = 0;
		for (var i in tags) {
			if (tags[i].nodeType == 1) {
				if (tags[i].getAttribute("class") != null
					&& __hasArrItem(tags[i].getAttribute("class")
						.split(" "), clazname)) { // 如果某元素的class值为所需要
					objArray[index] = tags[i];
					index++;
				}
			}
		}
		return objArray;
	}

	function __hasArrItem(arr, name) {
		for (var i = 0; i < arr.length; i++) {
			// console.log(arr[i])
			if (arr[i] == name) {
				return true;
			}
		}
		return false;
	}

	function __onValueChange(id) {
		if (id == "") {
			DataBindding.replaceIds = {};
			for (var p in DataBindding.replaceElements) {
				var s = (DataBindding.replaceElements[p]);
				// console.log("id=" + id + ",p=" + p + ",s=" + s);
				var ar = s.split("{_dbin{");
				var endS = "";
				for (var pa in ar) {
					var arr = (ar[pa].split("}}"));
					for (var i = 0; i < arr.length; i++) {
						if (i == 0 && arr[i].indexOf("$") > -1
							&& arr[i].indexOf(".") > -1) {

							var idp = arr[i].substring(1).split(".");
							var id = idp[0];
							var _p = idp[1];

							// console.log("id=" + id + ",p=" + p + ",s=" + s);
							if (DataBindding.replaceIds[id] == undefined) {
								DataBindding.replaceIds[id] = {}
							}
							DataBindding.replaceIds[id][p] = 1;

							if (DataBindding.elemts[("#" + id)] != undefined) {
								arr[i] = DataBindding.elemts[("#" + id)][_p];
							} else {
								arr[i] = "";
							}
						}
						endS += arr[i];
					}
				}
				// 替换到真实DOM
				var clzn = (p + "").split("|")[0];
				var idx = (p + "").split("|")[1];
				for (var i = 0; i < DataBindding.rawDoms.length; i++) {
					if (DataBindding.rawDoms[i].className.indexOf(clzn) > -1) {
						DataBindding.rawDoms[i].childNodes[idx].nodeValue = endS;
					}
				}
			}
			if (DataBindding.varWatcher) {
				console.log("varWatcher 空id~~~~" + id)
				// DataBindding.varWatcher(DataBindding.elemts[("#" + id)])
			}
		} else {
			let callbackelemtid = id
			console.log("callbackelemtid:" + callbackelemtid);

			var elemtsJson = DataBindding.replaceIds[id];
			if (elemtsJson != undefined) {
				for (var p in elemtsJson) {
					var s = (DataBindding.replaceElements[p])
					// //
					var ar = s.split("{_dbin{");
					var endS = "";
					for (var pa in ar) {
						var arr = (ar[pa].split("}}"));
						for (var i = 0; i < arr.length; i++) {
							if (i == 0 && arr[i].indexOf("$") > -1
								&& arr[i].indexOf(".") > -1) {

								var idp = arr[i].substring(1).split(".");
								var id = idp[0];
								var _p = idp[1];

								// console.log("id=" + id + ",p=" + p + ",s=" +
								// s);
								if (DataBindding.replaceIds[id] == undefined) {
									DataBindding.replaceIds[id] = {}
								}
								DataBindding.replaceIds[id][p] = 1;

								if (DataBindding.elemts[("#" + id)] != undefined) {
									arr[i] = DataBindding.elemts[("#" + id)][_p];
								} else {
									arr[i] = "";
								}
							}
							endS += arr[i];
						}
					}
					// 替换到真实DOM
					var clzn = (p + "").split("|")[0];
					var idx = (p + "").split("|")[1];
					for (var i = 0; i < DataBindding.rawDoms.length; i++) {
						if (DataBindding.rawDoms[i].className.indexOf(clzn) > -1) {
							DataBindding.rawDoms[i].childNodes[idx].nodeValue = endS;
						}
					}
				}
			}
			if (DataBindding.varWatcher) {
				console.log("varWatcher ~~~~" + callbackelemtid)
				DataBindding
					.varWatcher(DataBindding.elemts[("#" + callbackelemtid)])
			}
		}

	}
})()
