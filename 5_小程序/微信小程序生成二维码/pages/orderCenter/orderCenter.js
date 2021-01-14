//获取应用实例
const app = getApp()

Page({
  data: {
    backClass:[],
    //backClass: ["red"],
    //backClass: ["red", "blue"],
    //backClass: ["red", "blue", "green"],
    //backClass: ["red", "blue", "green", "orange"],
    currentItemId: 0,
  },
  onLoad: function (options) {
    console.log(options.jsonStr)
    let item = [JSON.parse(options.jsonStr)];
    console.log(item)

    this.setData({
      backClass: item
    })
    // console.log("未取餐订单")
    //this.getPaymentOrderList();
  },
  swiperChange: function (e) {
    var currentItemId = e.detail.currentItemId;
    this.setData({
      currentItemId: currentItemId
    })   
  },
  clickChange: function (e) {
    var itemId = e.currentTarget.dataset.itemId;
    this.setData({
      currentItemId: itemId
    })
    console.log("itemId=" + itemId)
  },    
})