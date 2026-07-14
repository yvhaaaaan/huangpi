Page({
  data: {
    steps: [
      { title: '备料', desc: '准备茶叶、花生、芝麻、姜和地方配料。' },
      { title: '炒香', desc: '小火炒香主料，激发油茶特有香气。' },
      { title: '研磨', desc: '将炒香原料研磨成细腻茶浆。' },
      { title: '冲煮', desc: '加入热水煮沸，搭配点心一起品尝。' },
    ],
  },

  onNavigateTap(event: WechatMiniprogram.TouchEvent): void {
    const url = event.currentTarget.dataset.url as string | undefined
    if (url) wx.navigateTo({ url })
  },

  onCollectTap(): void { wx.showToast({ title: '已收藏', icon: 'success' }) },

  onShareAppMessage() { return { title: '黄陂油茶文化', path: '/pages/tea/detail', imageUrl: '/static/real-tea.jpg' } },
})
