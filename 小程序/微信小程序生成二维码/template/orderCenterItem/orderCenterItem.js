const QRCode = require('../../utils/weapp-qrcode.js')
import rpx2px from '../../utils/rpx2px.js'
let qrcode;
const qrcodeWidth = rpx2px(295)

Component({
  /**
   * 组件的属性列表
   */
  properties: {
    myProperty: { // 属性名
      type: String, // 类型（必填），目前接受的类型包括：String, Number, Boolean, Object, Array, null（表示任意类型）
      value: '', // 属性初始值（可选），如果未指定则会根据类型选择一个
      observer: function (newVal, oldVal) { } // 属性被改变时执行的函数（可选），也可以写成在methods段中定义的方法名字符串, 如：'_propertyChange'
    },
    orderItem: Object // 简化的定义方式
  },
  /**
   * 组件的初始数据
   */
  data: {
    _item: {}
  },
  lifetimes: {
    // 组件的生命周期函数，用于声明组件的生命周期
    // moved: () => { console.log(211) },
    // detached: () => { console.log(311) }
  },
  /***/
  ready: function() {
    let shortCode = this.properties.orderItem.shortCode;
    qrcode = new QRCode('canvas', {
      usingIn: this, // usingIn 如果放到组件里使用需要加这个参数
      text: shortCode,
      // image: '/images/bg.jpg',
      width: qrcodeWidth,
      height: qrcodeWidth,
      colorDark: "#de7921",
      colorLight: "white",
      correctLevel: QRCode.CorrectLevel.H,
    });

    //console.log(this.properties.orderItem)
    console.log("" ) 
  },

  /**
   * 组件的方法列表
   */
  methods: {
    sayHi: function () {

    },
  }
})
