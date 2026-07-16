Page({
  onNavTap(event) {
    const url = event.currentTarget.dataset.url
    if (url) wx.switchTab({ url })
  },
})
