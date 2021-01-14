var tree_pick_mode_onlypick = 0;
var tree_pick_mode_pickandselect = 1;

var nowTree;
/**
 * 组织结构的bean，用于组织这些对象的顺序，然后显示在界面上
 */
function BeanOrg(_id, _name, lv, _son, pid) {
	this.orgId = _id;
	this.orgName = _name
	this.lv = lv; // 没有父节点的为-1，然后类推,就是层次，实际上这个没有用到
	this.son = _son; // 如果是true，则表示没有下一级了
	this.pid = pid; // 父节点，没有的话给-1
}
/**
 * 
 * 构造方法
 */
function CyanTreeUI2() {
	this.beanOrgs = new Array();
	nowTree = this;
	this.needAddBlank = true;
	/* 默认是tree_pick_mode_onlypick */
	this.tree_pick_mode = tree_pick_mode_onlypick;
}

CyanTreeUI2.prototype.setNeedAddBlank = function(need) {
	this.needAddBlank = need;
}

/**
 * 
 * @_beanOrg BeanOrg的一组数组
 */
CyanTreeUI2.prototype.init = function(_beanOrg, containerId) {
	this.beanOrgs = _beanOrg;
	this.containerId = containerId;
}
CyanTreeUI2.prototype.testInit = function(containerId) {
	this.containerId = containerId;
	var apis = new ServerAPIs();
	var url = apis.getAPI(apis.GetOrgStruct, {
		id : "1",
		name : "cyanking",
		others : "..."
	});
	// 下面是模拟数据
	//
	var bo1 = new BeanOrg(1, "总公司", -1, false, -1);
	var bo2 = new BeanOrg(2, "研发部", 1, true, 1);
	// var bo3 = new BeanOrg(101, "cyan", 2, true, 2);
	// var bo4 = new BeanOrg(102, "jlj", 2, true, 2);
	var bo5 = new BeanOrg(3, "测试部", 1, true, 1);
	// var bo6 = new BeanOrg(103, "www", 2, true, 3);
	var bo7 = new BeanOrg(4, "综合美术部", 1, false, 1);
	var bo8 = new BeanOrg(41, "综合美术分部1", 2, true, 4);
	// var bo9 = new BeanOrg(411, "cls1", 3, true, 41);
	var bo10 = new BeanOrg(42, "综合美术分部2", 2, true, 4);
	// var bo11 = new BeanOrg(421, "cls2", 3, true, 42);
	this.beanOrgs.push(bo1);
	this.beanOrgs.push(bo2);
	// this.beanOrgs.push(bo3);
	// this.beanOrgs.push(bo4);
	this.beanOrgs.push(bo5);
	// this.beanOrgs.push(bo6);
	this.beanOrgs.push(bo7);
	this.beanOrgs.push(bo8);
	// this.beanOrgs.push(bo9);
	this.beanOrgs.push(bo10);
	// this.beanOrgs.push(bo11);
}
/*
 * <ul class="persion_tree"> <li>+---123</li> <li>+---456 <ul> <li>----123
 * <ul> <li>----123</li> <li>+---456</li> </ul> </li> <li>+---456</li>
 * </ul> </li> </ul>
 */
/**
 * id的命名前缀，让整个dom模型中不会有同样的id
 */
CyanTreeUI2.prototype.drawTree = function(idPrex) {
	this.prex = idPrex;
	// 获取容器
	this.ctn = document.getElementById(this.containerId);
	// 节点列表
	this.nodes = new Array();
	// 跟节点的父节点- -！
	var rootUl = document.createElement("ul");
	rootUl.className = "cyan-tree-ui_4_scheduling_view";
	// 生成所有节点，第0个是根
	for ( var i in this.beanOrgs) {
		var bean = this.beanOrgs[i];
		// 节点本身li
		var node = document.createElement("li");
		node.className = "no-child";
		node.id = idPrex + "," + bean.orgId;
		// 节点显示名字的文本 ，有一些文本是在css里做的
		var a1 = document.createElement("a");
		var lv = parseInt(bean.lv);
		if (this.needAddBlank) {
			for (var k = 0; k < lv; k++) {
				a1.innerHTML += "&nbsp;&nbsp;&nbsp;&nbsp;";
			}
		}
		a1.innerHTML += " ";
		a1.style.fontSize = "1em";
		a1.style.paddingRight = "0.275em";
		a1.href = "javascript:tree_ui_click_es('" + bean.orgId + "')";
		var a2 = document.createElement("a");
		var lv = parseInt(bean.lv);
		// for (var k = 0; k < lv; k++) {
		// a2.innerHTML += "&nbsp;&nbsp;&nbsp;&nbsp;";
		// }
		// a2.innerHTML += "&nbsp;" + bean.orgName;
		a2.innerHTML += bean.orgName;
		a2.style.fontSize = "1em";
		a2.style.marginLeft = "0.25em"
		a2.href = "javascript:tree_ui_click('" + bean.orgId + "')";

		var ckbx = document.createElement("input");
		ckbx.type = "checkbox";
		ckbx.id = "tree_ckbox_" + bean.orgId;
		ckbx.onclick = (function(id) {
			return function() {
				_ckbx_tree_click(id);
			}
		})(bean.orgId);

		// 如果有子节点，才会用到这个ul
		var nodeUl = document.createElement("ul");
		// 添加节点们
		node.appendChild(nodeUl);

		node.appendChild(a1);
		if (this.tree_pick_mode == tree_pick_mode_pickandselect
				&& bean.pid != -1) {
			node.appendChild(ckbx);
		}
		node.appendChild(a2);

		node.appendChild(nodeUl);
		if (i == 0) {
			rootUl.appendChild(node);
		}
		this.nodes.push(node);
	}
	// 形成结构
	for ( var i in this.beanOrgs) {
		var bean = this.beanOrgs[i];
		// if (bean.pid == -1) {
		// continue; // 根不要处理
		// }
		if (bean.son == true) {
			this.nodes[i].style.display = "none";
		}
		for ( var j in this.beanOrgs) {
			if (this.beanOrgs[j].orgId == bean.pid) {
				if (j != 0) {
					this.nodes[j].style.display = "none";
					var str = this.nodes[j].getElementsByTagName("a")[0].innerHTML;
					this.nodes[j].getElementsByTagName("a")[0].innerHTML = str
							.substring(0, str.length - 1)
							+ "+";
				} else {
					var str = this.nodes[j].getElementsByTagName("a")[0].innerHTML;
					this.nodes[j].getElementsByTagName("a")[0].innerHTML = str
							.substring(0, str.length - 1)
							+ "+";
				}
				if (this.beanOrgs[j].son == false) {
					this.nodes[j].className = "has-child-shrinked";
				}
				this.nodes[j].getElementsByTagName("ul")[0]
						.appendChild(this.nodes[i]);
			}
		}
	}
	// 放到容器上
	this.ctn.innerHTML = "";
	this.ctn.appendChild(rootUl);
	// console.log(this.ctn.innerHTML);
}

/**
 * 设置回调
 */
CyanTreeUI2.prototype.setClickCallback = function(_callback) {
	this._callback = _callback;
}

function tree_ui_click_es(id) {
	// 获取其父节点的状态
	var showOrHidden = -1;
	for ( var i in nowTree.nodes) {
		if (nowTree.beanOrgs[i].orgId == id) {
			var node = nowTree.nodes[i];
			if (node.className == 'has-child-shrinked') {
				node.className = "has-child-expanded";
				var str = node.getElementsByTagName("a")[0].innerHTML;
				node.getElementsByTagName("a")[0].innerHTML = str.substring(0,
						str.length - 1)
						+ "-";
				showOrHidden = 1;
			} else if (node.className == 'has-child-expanded') {
				node.className = "has-child-shrinked";
				var str = node.getElementsByTagName("a")[0].innerHTML;
				node.getElementsByTagName("a")[0].innerHTML = str.substring(0,
						str.length - 1)
						+ "+";
				showOrHidden = 0;
			}
		}
	}
	for ( var i in nowTree.nodes) {
		if (nowTree.beanOrgs[i].pid == id) {
			if (showOrHidden == 1) {
				nowTree.nodes[i].style.display = "block";
			} else {
				nowTree.nodes[i].style.display = "none";
			}
		}
	}
}

function tree_ui_click(id) {
	for ( var i in nowTree.nodes) {
		if (nowTree.beanOrgs[i].orgId == id) {
			var bean = nowTree.beanOrgs[i];
			// alert(bean.orgId + " " + bean.orgName+"
			// "+nowTree.nodes[i].className+" "+bean.son);
			if (nowTree.tree_pick_mode == tree_pick_mode_onlypick) {
				if (bean.son) {
					nowTree._callback(null, bean.orgId, bean.orgName);
				} else {
					tree_ui_click_es(id);
				}
			} else {// 这个模式给了一个勾选的框，所以这不回调了
				if (bean.son) {
					// nowTree._callback(null, bean.orgId, bean.orgName);
				} else {
					tree_ui_click_es(id);
					// _$_$_tree_$_redraw();
					// nowTree._callback(null, bean.orgId, bean.orgName);
				}
			}
			break;
		}
	}
}

function _ckbx_tree_click(id) {
	var ckid = "tree_ckbox_" + id;
	for ( var i in nowTree.nodes) {
		if (nowTree.beanOrgs[i].orgId == id) {
			var bean = nowTree.beanOrgs[i];
			// alert(bean.orgId + " " + bean.orgName+"
			// "+nowTree.nodes[i].className+" "+bean.son);
			var _id = (nowTree.prex + "," + bean.orgId);
			var li = document.getElementById(_id);
			var ipts = li.getElementsByTagName("input");
			var ckbox = document.getElementById(ckid);
			for (var i = 0; i < ipts.length; i++) {
				if (ckid != ipts[i].id && ipts[i].type == "checkbox") {
					if (ckbox.checked == true) {
						//ipts[i].style.visibility = "hidden";
						ipts[i].disabled = true;
					} else {
						//ipts[i].style.visibility = "visible";
						ipts[i].disabled = false;
					}
				}
			}
			if (ckbox.checked == true) {
				nowTree._callback(null, bean.orgId, bean.orgName);
			} else {
				//强行调用消除代码，这些代码真是把我的结构全部破坏了
				$$$__tree_remove(bean.orgId);
			}
			break;
		}
	}
}

/**
 * 被使用pickandselect模式的业务js调用，模式2加了一个checkbox，这个地方是显示树所在的弹出层的时候，恢复树的checkbox
 * @param id
 */
function _ckbx_tree_click_select(id) {
	var ckid = "tree_ckbox_" + id;
	var ckbox = document.getElementById(ckid);
	ckbox.checked = true;
	_ckbx_tree_click(id);
}