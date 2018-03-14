//数据格式
/*
 * "{'datas':[{'c1':'李小明','c9':{'n':'查看','p':'id1'}},{'c1':'李小明','c9':{'n':'查看','p':'id1'}}],'curPage':'1','totalPage':'30'}"
 */

/**
 * 某些页面也许需要自己加上 <style> 所有class为menu的div中的ul样式 .tab-contentul { list-style:
 * none; margin: 0px; padding: 0px; width: auto; } .tab-content ul li { float:
 * left; } </style>
 */

/* 这个是statistics.jsp的表格，要配合页面上的表格插件和css使用，这个是通过动态来生成表格，不用写好多次 */
(function() {
	function CyanStatisticsTable() {

	}

	CyanStatisticsTable.prototype.currentColumnIdenty = "";

	CyanStatisticsTable.prototype.currindex = 0;
	CyanStatisticsTable.prototype.maxPages = 0;

	CyanStatisticsTable.prototype.showSelf = function() {
		return "in global call me use:window.$cst";
	}

	CyanStatisticsTable.prototype.column_company = [ {
		"name" : "公司",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "迟到次数",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "早退次数",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "矿工天数",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "外出次数",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "漏卡次数",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "操作",
		"type" : 1, // 1表明是操作位
		"identy" : "column_company"
	}, {
		"name" : "导出excel",
		"type" : 1, // 1表明是操作位
		"identy" : "column_company"
	} ]

	CyanStatisticsTable.prototype.column_department = [ {
		"name" : "部门",
		"type" : 0,
		"identy" : "column_department"
	}, {
		"name" : "迟到次数",
		"type" : 0,
		"identy" : "column_department"
	}, {
		"name" : "早退次数",
		"type" : 0,
		"identy" : "column_department"
	}, {
		"name" : "矿工天数",
		"type" : 0,
		"identy" : "column_department"
	}, {
		"name" : "外出次数",
		"type" : 0,
		"identy" : "column_department"
	}, {
		"name" : "漏卡次数",
		"type" : 0,
		"identy" : "column_department"
	},{
		"name" : "操作",
		"type" : 1, // 1表明是操作位
		"identy" : "column_department"
	} ]
	//这里实时考勤的公司列对象没有直接用，而是当是查询实时考勤的时候，把它的值赋值给this($cst).column_company
	CyanStatisticsTable.prototype.column_company_realtime = [ {
		"name" : "公司",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "迟到次数",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "早退次数",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "矿工天数",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "外出次数",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "漏卡次数",
		"type" : 0,
		"identy" : "column_company"
	}, {
		"name" : "无需考勤",
		"type" : 1,
		"identy" : "column_company"
	}, {
		"name" : "操作",
		"type" : 1, // 1表明是操作位
		"identy" : "column_company"
	}, {
		"name" : "导出excel",
		"type" : 1, // 1表明是操作位
		"identy" : "column_company"
	} ]

	//这里实时考勤的公司列对象没有直接用，而是当是查询实时考勤的时候，把它的值赋值给this($cst).column_department
	CyanStatisticsTable.prototype.column_department_realtime = [ {
		"name" : "部门",
		"type" : 0,
		"identy" : "column_department"
	}, {
		"name" : "迟到次数",
		"type" : 0,
		"identy" : "column_department"
	}, {
		"name" : "早退次数",
		"type" : 0,
		"identy" : "column_department"
	}, {
		"name" : "矿工天数",
		"type" : 0,
		"identy" : "column_department"
	}, {
		"name" : "外出次数",
		"type" : 0,
		"identy" : "column_department"
	}, {
		"name" : "漏卡次数",
		"type" : 0,
		"identy" : "column_department"
	}, {
		"name" : "无需考勤",
		"type" : 1,
		"identy" : "column_department"
	}, {
		"name" : "操作",
		"type" : 1, // 1表明是操作位
		"identy" : "column_department"
	} ]
	

	CyanStatisticsTable.prototype.column_person = [ {
		"name" : "姓名",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "部门",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "正常考勤天数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "考勤天数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "迟到次数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "早退次数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "旷工天数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "外出次数",
		"type" : 0,
		"identy" : "column_person"
	},{
		"name" : "漏卡次数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "操作",
		"type" : 1, // 1表明是操作位
		"identy" : "column_person"
	} ]
	//这里实时考勤的个人列对象没有直接用，而是当是查询实时考勤的时候，把它的值赋值给this($cst).column_person
	CyanStatisticsTable.prototype.column_person_realtime = [ {
		"name" : "姓名",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "部门",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "迟到次数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "早退次数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "旷工天数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "外出次数",
		"type" : 0,
		"identy" : "column_person"
	},{
		"name" : "漏卡次数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "操作",
		"type" : 1, // 1表明是操作位
		"identy" : "column_person"
	} ]

	CyanStatisticsTable.prototype.column_person_now = [ {
		"name" : "姓名",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "部门",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "迟到次数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "早退次数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "旷工天数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "外出次数",
		"type" : 0,
		"identy" : "column_person"
	}, {
		"name" : "操作",
		"type" : 1, // 1表明是操作位
		"identy" : "column_person"
	} ]

	CyanStatisticsTable.prototype.column_detail8 = [ {
		"name" : "打卡日期",
		"type" : 0,
		"identy" : "column_detail8"
	}, {
		"name" : "第一次",
		"type" : 0,
		"identy" : "column_detail8"
	}, {
		"name" : "第二次",
		"type" : 0,
		"identy" : "column_detail8"
	}, {
		"name" : "第三次",
		"type" : 0,
		"identy" : "column_detail8"
	}, {
		"name" : "第四次",
		"type" : 0,
		"identy" : "column_detail8"
	}, {
		"name" : "第五次",
		"type" : 0,
		"identy" : "column_detail8"
	}, {
		"name" : "第六次",
		"type" : 0,
		"identy" : "column_detail8"
	}, {
		"name" : "第七次",
		"type" : 0,
		"identy" : "column_detail8"
	}, {
		"name" : "第八次",
		"type" : 0,
		"identy" : "column_detail8"
	} ]

	CyanStatisticsTable.prototype.column_detail6 = [ {
		"name" : "打卡日期",
		"type" : 0,
		"identy" : "column_detail6"
	}, {
		"name" : "第一次",
		"type" : 0,
		"identy" : "column_detail6"
	}, {
		"name" : "第二次",
		"type" : 0,
		"identy" : "column_detail6"
	}, {
		"name" : "第三次",
		"type" : 0,
		"identy" : "column_detail6"
	}, {
		"name" : "第四次",
		"type" : 0,
		"identy" : "column_detail6"
	}, {
		"name" : "第五次",
		"type" : 0,
		"identy" : "column_detail6"
	}, {
		"name" : "第六次",
		"type" : 0,
		"identy" : "column_detail6"
	} ]

	CyanStatisticsTable.prototype.column_detail4 = [ {
		"name" : "打卡日期",
		"type" : 0,
		"identy" : "column_detail4"
	}, {
		"name" : "第一次",
		"type" : 0,
		"identy" : "column_detail4"
	}, {
		"name" : "第二次",
		"type" : 0,
		"identy" : "column_detail4"
	}, {
		"name" : "第三次",
		"type" : 0,
		"identy" : "column_detail4"
	}, {
		"name" : "第四次",
		"type" : 0,
		"identy" : "column_detail4"
	} ]

	CyanStatisticsTable.prototype.column_detail2 = [ {
		"name" : "打卡日期",
		"type" : 0,
		"identy" : "column_detail2"
	}, {
		"name" : "第一次",
		"type" : 0,
		"identy" : "column_detail2"
	}, {
		"name" : "第二次",
		"type" : 0,
		"identy" : "column_detail2"
	} ]

	CyanStatisticsTable.prototype.clickDispatherData = [];

	CyanStatisticsTable.prototype.setItemClickCallBack = function(columnIdenty,
			_callBack) {
		var needSet = true;
		for (var int = 0; int < this.clickDispatherData.length; int++) {
			if (this.clickDispatherData[int].ci == columnIdenty) {
				needSet = false;
				break;
			}
		}
		if (needSet) {
			this.clickDispatherData.push({
				"ci" : columnIdenty,
				'cb' : _callBack
			});
		}
	}

	CyanStatisticsTable.prototype.setPageChangedClickCallBack = function(
			columnIdenty, _callBack) {
		var needSet = true;
		for (var int = 0; int < this.clickDispatherData.length; int++) {
			if (this.clickDispatherData[int].ci4pg == columnIdenty) {
				needSet = false;
				break;
			}
		}
		if (needSet) {
			this.clickDispatherData.push({
				"ci4pg" : columnIdenty,
				'pageCb' : _callBack
			});
		}
	}

	/**
	 * item的点击事件
	 * 
	 * @param param
	 *            示例 column_company,[others
	 *            param],参数按照逗号分隔，第0个参数是标识符，标识是那种列的item点击
	 */
	CyanStatisticsTable.prototype.itemClick = function(param) {
		for ( var i in this.clickDispatherData) {
			if (this.clickDispatherData[i].ci == param.split(",")[0]) {
				this.clickDispatherData[i].cb(param.split(",")[1]);
			}
		}
	}
	/**
	 * 换页面的点击事件
	 * 
	 * @param param
	 *            示例 column_company,[others
	 *            param],参数按照逗号分隔，第0个参数是标识符，标识是那种列的item点击
	 */
	CyanStatisticsTable.prototype.pageChangeClick = function(param) {
		for ( var i in this.clickDispatherData) {
			// console.log(this.clickDispatherData[i].ci4pg+"
			// "+param.split(",")[0]);
			if (this.clickDispatherData[i].ci4pg == param.split(",")[0]) {
				this.clickDispatherData[i].pageCb(param.split(",")[1]);
			}
		}
	}

	/**
	 * 修改当前的识别码
	 * @param idty
	 */
	CyanStatisticsTable.prototype.changeNowIdentity = function(idty) {
		this.currentColumnIdenty=idty;
	}
	
	/**
	 * 切换页面先由这个方法触发，再调用
	 * 
	 * @param type
	 *            0 直接切换页面，index给的是数字, 1后退10页，2前进10页，4后退1页，5前进1页,3指定页面，这三个情况
	 *            index的值都没有
	 * @param index
	 */
	CyanStatisticsTable.prototype.cyanStaticsitcTableGoPage = function(type,
			index) {
		this.currindex=parseInt(this.currindex+"");
		index=parseInt(index+"")
		if (type == 0) {
			this.currindex = index;
		} else if (type == 1) {
			if (this.currindex == 1) {
				return;
			}
			if (this.currindex - 10 < 1) {
				this.currindex = 1;
			} else {
				this.currindex = this.currindex - 10;
			}
		} else if (type == 2) {
			if (this.currindex == this.maxPages) {
				return;
			}
			if (this.currindex + 10 > this.maxPages) {
				this.currindex = this.maxPages;
			} else {
				this.currindex = this.currindex + 10;
			}
		} else if (type == 4) {
			if (this.currindex == 1) {
				return;
			}
			if (this.currindex - 1 < 1) {
				this.currindex = 1;
			} else {
				this.currindex = this.currindex - 1;
			}
		} else if (type == 5) {
			if (this.currindex == this.maxPages) {
				return;
			}
			if (this.currindex + 1 > this.maxPages) {
				this.currindex = this.maxPages;
			} else {
				this.currindex = this.currindex + 1;
			}
		} else if (type == 3) {
			var inputValue = document
					.getElementById("cyan_staticsitc_table_go_page_input").value
					.trim();
			if (inputValue == "") {
				inputValue = ".";
			}
			if (isNaN(inputValue.trim()) == false) {
				inputValue = parseInt(inputValue);
				if (inputValue < 1 || inputValue > this.maxPages) {
					alert("输入页数超过范围");
					return;
				}
			} else {
				alert("请输入数字");
				return;
			}
			this.currindex = inputValue;
		}
		if(this.currindex<1){
			return;
		}
		var param = this.currentColumnIdenty + "," + this.currindex;
		this.pageChangeClick(param);
	}

	/**
	 * 
	 * @param columnType
	 *            选择的列类型，上面定义了7个
	 * @param data
	 *            [{"data":"显示的数据","param":"点击事件的param"}]
	 * @param cantainer
	 *            容器id
	 * @param currpage
	 *            当前页
	 * @param maxpage
	 *            总页数
	 */
	CyanStatisticsTable.prototype.makeTable = function(columnType, data,
			cantainer, currpage, maxpage) {
		// 保存当前页和总页数以及当前是那个列数组在生成表格
		this.currindex = currpage;
		this.maxPages = maxpage;
		this.currentColumnIdenty = columnType[0].identy
		// 获取容器
		var ctn = document.getElementById(cantainer);
		ctn.innerHTML = "";
		var html = "<table  class='table-striped footable-res footable metro-blue' data-page-size='10'>";
		// 生成第一行，表头
		html += "<thead><tr >"
		for ( var i in columnType) {
			var cl = columnType[i];
			if (i == 0) {
				html += "<th>" + cl.name + "</th>";
			} else {
				html += "<th >" + cl.name + "</th>";
			}

		}
		html += "</tr><thead>";
		// 生成数据
		html += "<tbody>";
		data = data.datas;
		for ( var i in data) {
			var one = data[i];
			var index = 0;
			html += "<tr style='display: table-row;'>";
			for ( var j in one) {
				var el = one[j];
				// console.log(el);
				if (columnType[index].type == 0) {
					html += "<td>" + el + "</td>";
				} else {
//					html += "<td onclick='$cst.itemClick(\""
//							+ (columnType[index].identy + "," + el.p)
//							+ "\")'><a href='javascript:void(0)' class='label label-success' role='tab data-toggle='tab'>"
//							+ el.n + "</td>"
					
					html += "<td ><a href='javascript:void(0)' onclick='$cst.itemClick(\""
					+ (columnType[index].identy + "," + el.p)
					+ "\")' class='label label-success' role='tab data-toggle='tab'>"
					+ el.n + "</td>"
				}
				index++;
			}
			html += "</tr>";
		}
		html += "</tbody>";
		// 生成翻页区域1
		html += "<tfoot><tr><td colspan='" + columnType.length
				+ "'><div class='pagination pagination-centered'style='float: left;'>";
		var pages = "<ul><li onclick='$cst.cyanStaticsitcTableGoPage(1,-1);'><a data-page='first' href='#'>«</a></li><li onclick='$cst.cyanStaticsitcTableGoPage(4,-1);'><a data-page='prev' href='#'>‹</a></li>";

		// 生成页码
		// pages += "<li class='footable-page active'><a data-page='0'
		// href='#'>1</a></li><li class=''><a data-page='0' href='#'>2</a></li>"
		var start = 0, end = 0;
		if (parseInt(currpage) - 3 < 1) {
			start = 1;
		} else {
			start = parseInt(currpage) - 3;
		}
		//console.log(currpage);
		if (parseInt(currpage) + 3 > parseInt(maxpage)) {
			end = parseInt(maxpage);
		} else {
			end = parseInt(currpage) + 3;
		}
		//console.log(start + " " + end);
		for (var i = start; i <= end; i++) {
			if (i == currpage) {
				pages += "<li class='footable-page active' onclick='$cst.cyanStaticsitcTableGoPage(0,"
						+ i
						+ ");'><a data-page='0' href='#'>"
						+ i
						+ "</a></li>";
			} else {
				pages += "<li class='' onclick='$cst.cyanStaticsitcTableGoPage(0,"
						+ i
						+ ");'><a data-page='0' href='#'>"
						+ i
						+ "</a></li>";
			}
		}

		// 生成翻页区域2
		pages += "<li onclick='$cst.cyanStaticsitcTableGoPage(5,-1);'><a data-page='next' href='#'>›</a></li><li ><a data-page='last' href='#' onclick='$cst.cyanStaticsitcTableGoPage(2,-1);'>»</a></li></ul>";
		html += pages;
		html += "</div>";
		// 生成直接跳转区
		html += "<div style='float: left;padding: 4px 12px;line-height: 20px;text-decoration: none;border: 0px solid #cccccc; border-left-width: 0;'><span>总页数"
				+ maxpage
                + "，跳转到第</span><input  typy='text' style='border: 1px solid #cccccc;border-radius: 4px;width:3em;'id='cyan_staticsitc_table_go_page_input' /><span>页"
				+ "</span><a href='#' style='padding: 4px 12px;line-height: 20px;border: 1px solid #cccccc;border-radius: 4px;' type='button' onclick='$cst.cyanStaticsitcTableGoPage(3,-1);' value='跳转' >跳转</a></div>";
		html+="</td></tr></tfoot></table>";
		ctn.innerHTML = html;

	}

	window.$cst = new CyanStatisticsTable();

})();