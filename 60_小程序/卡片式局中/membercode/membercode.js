//获取应用实例
const app = getApp()

Page({
  data: {
  },
  onLoad: function (options) {
 
  },
  onUnload: function () {
    let _this = this

  },
  onBack:function(){
    wx.navigateBack({
      delta: 1
    })
  }

})