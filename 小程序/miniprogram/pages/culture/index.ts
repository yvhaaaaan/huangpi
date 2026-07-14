Page({
  data: {
    routes: [
      { id: 1, title: '客家古村慢行线', desc: '参观古建筑、村史展陈和油茶待客体验。', duration: '半日', suitable: '亲子与散客', image: '/static/map-point.jpg' },
      { id: 2, title: '山乡油茶一日游', desc: '上午游古村，下午参加油茶和非遗体验。', duration: '一日', suitable: '团队研学', image: '/static/home-banner.jpg' },
    ],
  },

  onRouteTap(): void {
    wx.navigateTo({ url: '/pages/culture/detail' })
  },
})
