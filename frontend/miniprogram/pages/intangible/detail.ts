Page({
  data: {
    highlights: ['传承人现场讲解油茶制作流程', '可参与研磨、冲煮和品鉴环节', '适合亲子、研学和团队活动', '体验结束可领取打卡纪念章'],
  },

  onSignupTap(): void {
    wx.navigateTo({ url: '/pages/activity/signup' })
  },
})
