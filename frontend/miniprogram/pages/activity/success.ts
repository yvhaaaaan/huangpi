Page({
  onNavTap(event: WechatMiniprogram.TouchEvent): void {
    const url = event.currentTarget.dataset.url as string | undefined
    if (url) wx.switchTab({ url })
  },
})
