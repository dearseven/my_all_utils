//app.js
App({
  onLaunch: function () {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        console.log(res)
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        let code=''
        if (res.errMsg == 'login:ok'){
          code=res.code;
          this.code2sessionKey(code)
        }
      }
    })
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo
              console.log("res.userInfo=:")
              console.log(res.userInfo);
              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }
      }
    })
  },
  code2sessionKey:function(code){
    console.log('code=' + code);
    var _this = this;
    wx.request({
      url: _this.globalData.serverApiUrl + 'code2sessionKeyV2',
      data: {
        'code': code,
        'encryptedData': 'xx',
        'lang': '1',
        'iv':'iv'
      },
      header: {
        'content-type': 'application/json', // 默认值
        'guitoujun':true
      },
      success: function (res) {
        console.log("code2sessionKey")
        console.log(res)
      }
    })
  },
  globalData: {
    userInfo: null,
    serverApiUrl:"https://xxx.xxx.xyz:444/xxx/",
    //shoppingCart:[{item:{},count:0}]
    shoppingCart: [{ id: "0", count: 0,singlePrice:"0.1",name:'None'}],
    //正在卖的商品，这个是index在加入购物车的时候套餐总数参与加减的
    nowList:null,
    canteenId: "1" ,
    //这是我们的系统的用户id
    customId:0 
  }
})