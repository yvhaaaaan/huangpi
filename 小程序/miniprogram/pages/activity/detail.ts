Page({
  onSignupTap(): void { wx.navigateTo({ url: '/pages/activity/signup' }) },
  onCollectTap(): void { wx.showToast({ title: '已收藏活动', icon: 'success' }) },
  onMapTap(): void { wx.switchTab({ url: '/pages/map/index' }) },
  onShareAppMessage() { return { title: '油茶文化体验活动', path: '/pages/activity/detail', imageUrl: '/static/real-activity.jpg' } },
})
