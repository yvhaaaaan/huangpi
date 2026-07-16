Page({
  data: { name: '', phone: '', count: '1', remark: '' },

  onNameInput(event: WechatMiniprogram.Input): void { this.setData({ name: event.detail.value }) },
  onPhoneInput(event: WechatMiniprogram.Input): void { this.setData({ phone: event.detail.value }) },
  onCountInput(event: WechatMiniprogram.Input): void { this.setData({ count: event.detail.value }) },
  onRemarkInput(event: WechatMiniprogram.Input): void { this.setData({ remark: event.detail.value }) },

  onBackTap(): void { wx.navigateBack() },

  onSubmitTap(): void {
    if (!this.data.name || !this.data.phone || !this.data.count) {
      wx.showToast({ title: '请填写必填项', icon: 'none' })
      return
    }
    if (!/^1\d{10}$/.test(this.data.phone)) {
      wx.showToast({ title: '手机号格式错误', icon: 'none' })
      return
    }
    wx.navigateTo({ url: '/pages/activity/success' })
  },
})
